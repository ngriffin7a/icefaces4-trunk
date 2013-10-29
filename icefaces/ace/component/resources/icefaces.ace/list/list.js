/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

if (!window.ice['ace']) {
    window.ice.ace = {};
}

if (!ice.ace.Lists) ice.ace.Lists = {};

ice.ace.List = function(id, cfg) {
    var self = this;
    this.id = id;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.cfg = cfg;
    this.sep = String.fromCharCode(this.cfg.separator);
    this.element = ice.ace.jq(this.jqId);
    this.behaviors = cfg.behaviors;

    // global list of list objects, used by a component
    // to rebuild widget lists following an inserting update.
    if (ice.ace.Lists[id]) {
        var old = ice.ace.Lists[id];
        if (old.disableClickHandling)
            this.disableClickHandling = true;
        if (old.disableDoubleClickHandling)
            this.disableDoubleClickHandling = true;

        setTimeout(function() {
            self.disableClickHandling = false;
            self.disableDoubleClickHandling = false;
        }, 500);
    }
    ice.ace.Lists[id] = self;

    // Setup drag wrapped drag events
    this.appStartHandler = this.cfg.start;
    cfg.start = function(event, ui) {
        var placeholder = ice.ace.jq(event.currentTarget).find('.if-list-plhld'),
            li = ice.ace.jq(event.originalEvent.target).closest('li'),
            width = li.width(),
            height = li.height();

        // Copy size into placeholder while dragging element around
        placeholder.width(width+"px").height(height+"px");

        self.dragFromHandler.call(self, event, ui);

        if (self.appStartHandler)
            return self.appStartHandler(event, ui);
        return true;
    };

    this.appStopHandler = this.cfg.stop;
    cfg.stop = function(event, ui) {
        self.dragToHandler.call(self, event, ui);
        if (self.appStopHandler)
            return self.appStopHandler.call(self, event, ui);
        return true;
    };

    this.appRecieveHandler = this.cfg.receive;
    cfg.receive = function(event, ui) {
        self.itemReceiveHandler.call(self, event, ui);
        if (self.appRecieveHandler)
            return self.appRecieveHandler.call(self, event, ui);
        return true;
    };

    if (cfg.selection)
        this.setupSelection();

    if (cfg.dblclk_migrate)
        this.setupClickMigration();

    if (cfg.controls)
        this.setupControls();

    if (cfg.dragging)
        this.element.find("> ul").sortable(cfg);
};

// ************************************************************************* //
// List Features //
// ************************************************************************* //

ice.ace.List.prototype.itemReceiveHandler = function(event, ui) {
    var item = ui.item,
        id = item.attr('id'),
        fromIndex = parseInt(id.substr(id.lastIndexOf(this.sep)+1)),
        srcId = ui.sender.closest('.if-list').attr('id'),
        src = ice.ace.Lists[srcId];

    fromIndex = src.getUnshiftedIndex(
            src.element.find('> ul').children().length,
            src.read('reorderings'),
            fromIndex);

    this.immigrantMessage = [];
    this.immigrantMessage.push(srcId);
    this.immigrantMessage.push([[fromIndex , item.index()]]);

    // Deselect all in connected lists but the currently
    // dragged item.
    if (src.cfg.selection) {
        this.deselectConnectedLists();
        this.deselectAll(item);
        src.addSelectedItem(item, fromIndex);
    }

    this.sendMigrateRequest();
};

ice.ace.List.prototype.sendMigrateRequest = function(onsuccess) {
    var destList = this,
        sourceListId = destList.immigrantMessage[0],
        sourceList = ice.ace.Lists[sourceListId],
        options = {
            source: destList.id,
            execute: destList.id + " " + sourceListId,
            render: destList.id + " " + sourceListId
        };

    var params = {};
    params[sourceListId+'_emigration'] = destList.id;
    params[destList.id+'_immigration'] = JSON.stringify(destList.immigrantMessage);
    options.params = params;

    options.onsuccess = function(responseXML) {;
        destList.element = ice.ace.jq(ice.ace.escapeClientId(destList.element.attr('id')));
        if (destList.cfg.dragging) destList.element.find("> ul").sortable(destList.cfg);

        sourceList.element = ice.ace.jq(ice.ace.escapeClientId(sourceList.element.attr('id')));
        if (sourceList.cfg.dragging) sourceList.element.find("> ul").sortable(sourceList.cfg);

        if (ice.ace.jq.isFunction(onsuccess))
            onsuccess();

        return true;
    };

    if (this.behaviors)
        if (this.behaviors.migrate) {
            ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.migrate, options));

            // Clear submitted states
            this.clearState();
            sourceList.clearState();
            // Remove items undergoing migration from DOM to prevent rapid clicks from
            // causing premature subsequent migrations to this selection
            sourceList.element.find('> ul > li.ui-state-active').remove();
            return;
        }

    ice.ace.AjaxRequest(options);

    // Clear submitted states
    this.clearState();
    sourceList.clearState();
    // Remove items undergoing migration from DOM to prevent rapid clicks from
    // causing premature subsequent migrations to this selection
    sourceList.element.find('> ul > li.ui-state-active').remove();
};

ice.ace.List.prototype.dragFromHandler = function(event, ui) {
    this.startIndex = ui.item.index();
};

ice.ace.List.prototype.dragToHandler = function(event, ui) {
    // If moving in list

    // Align FF and IE with Webkit, produce a mouseout event
    // on the dropped item if 100ms post drop it has been aligned
    // out from under our cursor.
    var item = ui.item,
        self = this;

    if (!(ice.ace.jq.browser.chrome || ice.ace.jq.browser.safari)) {
        setTimeout(function () {
            var ie = ice.ace.jq.browser.msie && (ice.ace.jq.browser.version == 8 || ice.ace.jq.browser.version == 7);
            if (!ice.ace.util.isMouseOver(item, event)) self.itemLeave({currentTarget : item});
        }, 100);
    }

    /*
        For an odd reason jq.closest() returns no results incorrectly here for some IDs.
        I have the feeling it doesn't process the escaping in the selector for the ':' character correctly */
    if (ui.item.parents(this.jqId).length > 0) {
        var swapRecords = this.read('reorderings'),
            recordStart = swapRecords.length,
            index = ui.item.index(),
            lower = (index > this.startIndex),
            to = ui.item,
            from = lower ? to.prev() : to.next(),
            id = item.attr('id'),
            idIndex = parseInt(id.substr(id.lastIndexOf(this.sep)+1));

        idIndex = this.getUnshiftedIndex(
                        this.element.find('> ul').children().length,
                        this.read('reorderings'),
                        idIndex);

        // If item is in a new position
        if (index != this.startIndex) {
            // Alter selection state
            if (this.cfg.selection) {
                this.deselectAll();
                this.deselectConnectedLists();
                this.addSelectedItem(item, idIndex);
            }

            // Update ID indexes of all previous items
            do {
                var record = [];
                record.push(from.index());
                record.push(to.index());

                swapRecords.splice(recordStart,0,record);
                this.swapIdPrefix(from, to);

                to = from;
                from = lower ? to.prev() : to.next();
            } while (to.index() != this.startIndex);
        }

        this.write('reorderings', swapRecords);

        if (this.behaviors)
            if (this.behaviors.move) {
                var self = this;
                this.behaviors.move.oncomplete = function() {
                    self.clearState();
                };
                ice.ace.ab(this.behaviors.move);
            }
    }
    // Migrating between lists handled by new item insertion handler, not this drop handler
};

ice.ace.List.prototype.setupControls = function() {
    var self = this,
        itemSelector = ' > div.if-list-ctrls .if-list-ctrl';

    this.element
            .off('mouseenter', itemSelector).on('mouseenter', itemSelector, function(e) {
                var ctrl = e.currentTarget;
                ice.ace.jq(ctrl).addClass('ui-state-hover');
            })
            .off('mouseleave', itemSelector).on('mouseleave', itemSelector, function(e) {
                var ctrl = e.currentTarget;
                ice.ace.jq(ctrl).removeClass('ui-state-hover');
            })
            .off('click', itemSelector).on('click', itemSelector, function(e) {
                self.controlClickHandler.call(self, e);
            });
};

ice.ace.List.prototype.controlClickHandler = function(e) {
    var ctrl = e.currentTarget,
        jqCtrl = ice.ace.jq(ctrl),
        dir;

    if (!ice.ace.jq.browser.msie || ice.ace.jq.browser.version == 9) {
        jqCtrl.toggleClass('ui-state-active', 50)
            .toggleClass('ui-state-active', 50);
    }

    if (jqCtrl.hasClass('if-list-ctrl-top'))
        dir = "top";
    else if (jqCtrl.hasClass('if-list-ctrl-up'))
        dir = "up";
    else if (jqCtrl.hasClass('if-list-ctrl-dwn'))
        dir = "dwn";
    else if (jqCtrl.hasClass('if-list-ctrl-btm'))
        dir = "btm";

    this.moveItems(dir);
};

ice.ace.List.prototype.setupSelection = function() {
    var self = this,
        selector = ' > ul > li:not(.disabled)';

    ice.ace.jq(this.element)
            .off('mouseenter mouseleave click', selector)
            .on('mouseenter', selector, this.itemEnter)
            .on('mouseleave', selector, this.itemLeave)
            .on('click', selector, function(e) {
                self.itemClickHandler.call(self, e);
        });
};

ice.ace.List.prototype.setupClickMigration = function() {
    var self = this,
        selector = ' > ul > li';

    ice.ace.jq(this.element)
            .off('dblclick', selector)
            .on('dblclick', selector, function(e) { self.itemDoubleClickHandler.call(self, e); });
}

ice.ace.List.prototype.itemEnter = function(e) {
    ice.ace.jq(e.currentTarget).addClass('ui-state-hover');
};

ice.ace.List.prototype.itemLeave = function(e) {
    ice.ace.jq(e.currentTarget).removeClass('ui-state-hover');
};

ice.ace.List.prototype.itemDoubleClickHandler = function(e) {
    if (!this.disableDoubleClickHandling) {
        var item = ice.ace.jq(e.currentTarget),
            self = this,
            id = item.attr('id'),
            from = this,
            fromIndex = parseInt(id.substr(id.lastIndexOf(this.sep)+1)),
            to = this.getSiblingList(e.shiftKey);

        clearTimeout(this.pendingClickHandling);
        this.pendingClickHandling = undefined;

        fromIndex = this.getUnshiftedIndex(
            this.element.find('> ul').children().length,
            this.read('reorderings'),
            fromIndex);

        if (to == undefined) return;

        to.immigrantMessage = [];
        to.immigrantMessage.push(this.id);
        to.immigrantMessage.push([[fromIndex , to.element.find('> ul').children().length]]);

        if (this.cfg.selection) {
            to.deselectConnectedLists();
            to.deselectAll();
            this.addSelectedItem(item, fromIndex);
        }

        setTimeout(function() {
            self.disableDoubleClickHandling = false;
            self.disableClickHandling = false;
        },500);

        // Only allow more clicks to occur once migration has finished
        to.sendMigrateRequest(function() {
            from.doubleClickAttempted = false;
        });
    }
}

/* Get the following (or if shift is held, previous) list in the first
   listControl binding that associates this list with another */
ice.ace.List.prototype.getSiblingList = function (shift) {
    for(var controlId in ice.ace.ListControls) {
        if(ice.ace.ListControls.hasOwnProperty(controlId)) {
            var listSet = ice.ace.jq(ice.ace.ListControls[controlId].selector),
                listContainer = this.element.parent().parent(),
                lastSibling = (shift || listContainer.hasClass('if-list-dl-2')),
                listIndex = listSet.index(this.element);

            if (listIndex < 0) continue;

            listIndex = lastSibling ? listSet.index(this.element)-1 : listSet.index(this.element)+1;

            if ((!lastSibling && listIndex >= listSet.length) || (lastSibling && listIndex < 0))
                return undefined;

            if (listIndex >= 0)
                return ice.ace.Lists[ice.ace.jq(listSet[listIndex]).attr('id')];
        }
    }

    return undefined;
}

ice.ace.List.prototype.pendingClickHandling;
ice.ace.List.prototype.lastClickedIndex;
ice.ace.List.prototype.disableClickHandling = false;
ice.ace.List.prototype.disableDoubleClickHandling = false;

ice.ace.List.prototype.itemClickHandler = function(e) {
    // Prevent click handling if waiting to see if we've double clicked
    // or prevent click handling if we have already double clicked and are
    // mindlessly mashing on this element
    if (this.pendingClickHandling == undefined && !this.disableClickHandling) {
        var li = e.currentTarget,
            jqLi = ice.ace.jq(li),
            self = this,
            timeout = this.cfg.dblclk_migrate ? 250 : 0;

        this.disableClickHandling = true;

        this.pendingClickHandling =
            setTimeout(function () {
                var index = jqLi.index();

                // find connected lists and deselect all
                self.deselectConnectedLists();

                if (e.shiftKey && self.cfg.selection != "single") {
                    // Clear selection from shift key use
                    self.clearSelection();

                    var lower, higher, last_clicked = self.lastClickedIndex ? self.lastClickedIndex : -1;
                    if (last_clicked < index) {
                        lower = last_clicked + 1;
                        higher = index + 1;
                    } else {
                        lower = index;
                        higher = last_clicked;
                    }

                    jqLi.parent().children().slice(lower, higher).filter(":not(.ui-state-active)").each(function () {
                        self.addSelectedItem(ice.ace.jq(this), null, true);
                    });

                    if (self.behaviors && self.behaviors.select) {
                        self.behaviors.select.oncomplete = function() {
                            self.clearState();
                        };
                        ice.ace.ab(self.behaviors.select);
                    }
                }
                else {
                    var deselection = jqLi.hasClass('ui-state-active');

                    function modifyState() {
                        if (deselection) {
                            self.removeSelectedItem(jqLi);
                        } else {
                            self.addSelectedItem(jqLi);
                        }
                    }

                    // If this is single selection or no ctrl key is depressed
                    // deselect all before modifying selection
                    if (!(e.metaKey || e.ctrlKey) || self.cfg.selection == "single") {
                        self.deselectAll(null, modifyState);
                    } else {
                        modifyState();
                    }
                }

                self.lastClickedIndex = index;
                self.pendingClickHandling = undefined;
                self.disableClickHandling = false;
            }, timeout);
    }
};

/* Determines the original index of an item at a particular index */
ice.ace.List.prototype.getUnshiftedIndex = function(length, reorderings, index) {
    var indexes = [];
    for (var i = 0; length - i >= 0; i++) indexes.push(i);
    for (var i = 0; i < reorderings.length; i++) {
        var from = reorderings[i][0];
            to = reorderings[i][1];
            t = indexes[to];

        indexes[to] = indexes[from];
        indexes[from] = t;
    }

    return indexes[index];
};

ice.ace.List.prototype.addSelectedItem = function(item, inputIndex, skipSubmit) {
    if (!item.hasClass('ui-state-active')) {
        var selections = this.read('selections'),
            deselections = this.read('deselections'),
            reorderings = this.read('reorderings'),
            id = item.attr('id'),
            index;

        if (inputIndex) index = inputIndex;
        else {
            index = id.substr(id.lastIndexOf(this.sep)+1),
            index = this.getUnshiftedIndex(item.siblings().length, reorderings, parseInt(index));
        }

        item.addClass('ui-state-active');

        deselections = ice.ace.jq.grep(deselections, function(r) { return r != index; });
        selections.push(index);

        this.write('selections', selections);
        this.write('deselections', deselections);

        if (this.behaviors && !skipSubmit)
            if (this.behaviors.select) {
                var self = this;
                this.behaviors.select.oncomplete = function() {
                    self.clearState();
                };
                ice.ace.ab(this.behaviors.select);
            }
    }
};


ice.ace.List.prototype.deselectConnectedLists = function() {
    for(var controlId in ice.ace.ListControls) {
        if(ice.ace.ListControls.hasOwnProperty(controlId)) {
            var listSet = ice.ace.jq(ice.ace.ListControls[controlId].selector);
            if (listSet.is(this.element))
                listSet.not(this.element)
                        .each(function (i, elem) {
                            ice.ace.Lists[ice.ace.jq(elem).attr('id')].deselectAll();
                        });
        }
    }
}

ice.ace.List.prototype.removeSelectedItem = function(item) {
    if (item.hasClass('ui-state-active')) {
        var selections = this.read('selections'),
            deselections = this.read('deselections'),
            reorderings = this.read('reorderings'),
            id = item.attr('id'),
            index = id.substr(id.lastIndexOf(this.sep)+1),
            origIndex = this.getUnshiftedIndex(item.siblings().length, reorderings, parseInt(index));

        item.removeClass('ui-state-active');

        selections = ice.ace.jq.grep(selections, function(r) { return r != index; });
        deselections.push(origIndex);

        this.write('selections', selections);
        this.write('deselections', deselections);

        if (this.behaviors)
            if (this.behaviors.deselect) {
                var self = this;
                this.behaviors.deselect.oncomplete = function() {
                    self.clearState();
                };
                ice.ace.ab(this.behaviors.deselect);
            }
    }
};

ice.ace.List.prototype.deselectAll = function(except, done) {
    var self = this,
        reorderings = this.read('reorderings'),
        selections = this.read('selections'),
        deselections = this.read('deselections');

    this.element.find('> ul.if-list-body > li.if-list-item.ui-state-active').each(function(i, elem) {{
                if (except != undefined && except.is(elem)) return;

                var item = ice.ace.jq(elem),
                    id = item.attr('id'),
                    index = parseInt(id.substr(id.lastIndexOf(self.sep)+1));

                item.removeClass('ui-state-active');

                if (index != undefined)
                    index = self.getUnshiftedIndex(item.parent().children().length, reorderings, index);

                if (index != undefined) {
                    deselections.push(index);
                    selections = ice.ace.jq.grep(selections, function(r) { return r != index; });
                }
            }});

    this.write('selections', selections);
    this.write('deselections', deselections);

    if (this.behaviors && this.behaviors.deselect && !isNaN(deselections.length) && deselections.length > 0) {
        var s = this;
        this.behaviors.deselect.oncomplete = function() {
            s.clearState();
            if (done) done();
        };
        ice.ace.ab(this.behaviors.deselect);
    } else {
        if (done) done();
    }
}

ice.ace.List.prototype.clearState = function() {
    // Clear state to avoid having stale state being added to enqueued requests.
    this.write('reorderings', []);
    this.write('selections', []);
    this.write('deselections', []);
}

ice.ace.List.prototype.moveItems = function(dir) {
    var selectedItems = this.element.find('.ui-state-active');

    if (selectedItems.length > 0) {
        // do element swaps
        var swapRecords = this.read('reorderings');

        if (dir == "top") {
            for (var i = selectedItems.length-1; i >= 0; i--) {
                var item = ice.ace.jq(selectedItems[i]),
                        target = item.prev();

                if (target.length > 0) do {
                    var record = [];
                    record.push(item.index());
                    target.before(item);
                    record.push(item.index());

                    swapRecords.push(record);
                    this.swapIdPrefix(item, target);

                    target = item.prev();
                } while (target.length > 0);
            }
        }
        else if (dir == "up") {
            for (var i = 0; i < selectedItems.length; i++) {
                var item = ice.ace.jq(selectedItems[i]),
                        record = [];
                record.push(item.index());

                var target = item.prev(':first');
                target.before(item);

                record.push(item.index());
                swapRecords.push(record);
                this.swapIdPrefix(item, target);
            }
        }
        else if (dir == "dwn" || dir == "down") {
            for (var i = selectedItems.length-1; i >= 0; i--) {
                var item = ice.ace.jq(selectedItems[i]),
                        record = [];
                record.push(item.index());

                var target = item.next(':first');
                target.after(item);

                record.push(item.index());
                swapRecords.push(record);
                this.swapIdPrefix(item, target);
            }
        }
        else if (dir == "btm" || dir == "bottom") {
            for (var i = 0; i < selectedItems.length; i++) {
                var item = ice.ace.jq(selectedItems[i]),
                        target = item.next();

                if (target.length > 0) do {
                    var record = [];
                    record.push(item.index());
                    target.after(item);
                    record.push(item.index());

                    swapRecords.push(record);
                    this.swapIdPrefix(item, target);

                    target = item.next();
                } while (target.length > 0);
            }
        }

        // write swaps or ajax submit
        this.write('reorderings', swapRecords);

        if (this.behaviors)
            if (this.behaviors.move) {
                var self = this;
                this.behaviors.move.oncomplete = function() {
                    self.clearState();
                };
                ice.ace.ab(this.behaviors.move);
            }
    }
};

// Used to keep id for each child in place, so per-item updates
// occur as expected
ice.ace.List.prototype.swapIdPrefix = function(from, to) {
    if (from.length == 0 || to.length == 0) return;

    var fromId = from.attr('id'),
        toId = to.attr('id'),
        fromElems = from.find('*[id^="'+fromId+'"]'),
        toElems = to.find('*[id^="'+toId+'"]');

    from.attr('id', toId);
    to.attr('id', fromId);

    for (var x = 0; x < fromElems.length; x++) {
        var i = ice.ace.jq(fromElems[x]);
        i.attr('id', i.attr('id').replace(fromId, toId));
    }

    for (var x = 0; x < toElems.length; x++) {
        var i = ice.ace.jq(toElems[x]);
        i.attr('id', i.attr('id').replace(toId, fromId));
    }
};

ice.ace.List.prototype.read = function(field) {
    var contents = ice.ace.jq(ice.ace.escapeClientId(this.element.attr('id'))).children('input[name="'+this.jqId.substr(1)+'_'+field+'"]').val();
    if ((contents != "") && (contents != undefined))
        return JSON.parse(contents);
    else return [];
};

ice.ace.List.prototype.write= function(field, data) {
    var element = ice.ace.jq(ice.ace.escapeClientId(this.element.attr('id'))).children('input[name="'+this.jqId.substr(1)+'_'+field+'"]');
    element.attr('value', JSON.stringify(data));
};

ice.ace.List.prototype.clearSelection = function() {
    if (window.getSelection) {
        if (window.getSelection().empty) {  // Chrome
            window.getSelection().empty();
        } else if (window.getSelection().removeAllRanges) {  // Firefox
            window.getSelection().removeAllRanges();
        }
    } else if (document.selection) {  // IE?
        document.selection.empty();
    }
};
