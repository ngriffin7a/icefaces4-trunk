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
ice.ace.TableConfLauncher = function(clientId, panelJsId) {
    var launcher = ice.ace.jq(ice.ace.escapeClientId(clientId));

    var activate = function(e) {
        var panel = ice.ace.jq(ice.ace.escapeClientId(clientId.replace('_tableconf_launch', ''))),
            modal = panel.next('.ui-tableconf-modal');

        panel.toggle();
        modal.toggle();
        ice.ace.jq(e.currentTarget).toggleClass('ui-state-active').removeClass('ui-state-hover');

        if (panel.is(':not(:visible)'))
            panelJsId.submitTableConfig(e.currentTarget);
        else if (panelJsId.behaviors && panelJsId.behaviors.open)
            ice.ace.ab(panelJsId.behaviors.open);

        e.stopPropagation();
    }

    var unload = function() {
        launcher.off('click mouseenter mouseleave keyup');
    }

    // Toggle active state when initialized via hover
    launcher.toggleClass('ui-state-hover').addClass()
        .mouseenter(function(e) {
            ice.ace.jq(e.currentTarget).addClass('ui-state-hover');
        })
        .mouseleave(function(e) {
            ice.ace.jq(e.currentTarget).removeClass('ui-state-hover');
        })
        .click(function(e){
            activate(e);
        }).keyup(function(e) {
            if (e.which == 13) activate(e);
        });

    ice.onElementUpdate(clientId, unload);
}

ice.ace.TableConf = function (id, cfg) {
    var self = this;
    setTimeout(function(){
        self.init(id, cfg);
    }, 500);
}

ice.ace.TableConf.prototype.init = function(id, cfg) {
    this.id = ice.ace.escapeClientId(id);
    this.tableId = ice.ace.escapeClientId(cfg.tableId);
    this.$this = ice.ace.jq(this.id);
    this.modal =  this.$this.next('.ui-tableconf-modal');
    this.$table = ice.ace.jq(this.tableId);
    this.behaviors = cfg.behaviors;
    this.cfg = cfg;
    this.sortOrder = [];

    var dragConfig = {};

    if (cfg.handle)
        dragConfig.handle = cfg.handle;

    this.setupOkButton();
    this.setupTrashButton();
    this.setupCloseButton();

    this.$this.draggable(dragConfig);

    var isTopSet = ice.ace.jq.isNumeric(this.cfg.top),
            isLeftSet = ice.ace.jq.isNumeric(this.cfg.left);

    if (isTopSet || isLeftSet) {
        if (isTopSet) this.$this.css('top', this.cfg.top);
        if (isLeftSet) this.$this.css('left', this.cfg.left);
    }
    else this.$this.css('top', this.$table.offset().top + 15);

    if (cfg.reorderable) {
        // Return a helper with preserved width of cells
        var fixHelper = function(e, ui) {
            ui.children().each(function() {
                ice.ace.jq(this).width(ice.ace.jq(this).width());
            });
            return ui;
        };

        this.$this.find('.ui-tableconf-body > table > tbody')
                .sortable({
                    axis:'y',
                    containment:'parent',
                    helper: fixHelper,
                    items:'tr:not(.ui-disabled)',
                    handle:'.ui-sortable-handle:not(.ui-disabled)'
                });
    }

    if (cfg.sortable) {
        var _self = this;
        this.$this.find('.ui-tableconf-body tr:not(.ui-disabled) .ui-sortable-control')
                .click(function(event, altY, altMeta) {
                    event.stopPropagation();
                    var $this = ice.ace.jq(this),
                            topCarat = $this.find(".ui-icon-triangle-1-n")[0],
                            bottomCarat = $this.find(".ui-icon-triangle-1-s")[0],
                            controlCell =  $this.parent().parent(),
                            controlRow  =  ice.ace.jq(controlCell).parent(),
                            controlOffset = $this.offset(),
                            controlHeight = !_self.cfg.singleSort ? $this.outerHeight() : 22,
                            descending = false,
                            metaKey = altMeta;

                    if (metaKey == undefined) {
                        if (ice.ace.jq.browser.os == 'mac')
                            metaKey = event.metaKey;
                        else
                            metaKey = event.ctrlKey;
                    }

                    // altY and altMeta allow these event parameters to be optionally passed in
                    // from an event triggering this event artificially
                    var eventY = (altY == undefined) ? event.pageY : altY;
                    if (eventY > (controlOffset.top + (controlHeight / 2)+2))
                        descending = true;

                    // If we are looking a freshly rendered DT initalize our JS sort state
                    // from the state of the rendered controls
                    if (_self.sortOrder.length == 0) {
                        _self.$this.find('.ui-tableconf-body .ui-sortable-control').each(function() {
                            var $this = ice.ace.jq(this);
                            if (ice.ace.util.getOpacity($this.find('.ui-icon-triangle-1-n')[0]) == 1 ||
                                    ice.ace.util.getOpacity($this.find('.ui-icon-triangle-1-s')[0]) == 1 )
                                _self.sortOrder.splice(
                                        parseInt($this.find('.ui-sortable-column-order').html())-1,
                                        0,
                                        $this.closest('tr')
                                );
                        });
                    }

                    if (!metaKey || _self.cfg.singleSort) {
                        // Remake sort criteria
                        // Reset all other arrows
                        _self.sortOrder = [];
                        controlRow.siblings()
                                .find('.ui-icon-triangle-1-n, .ui-icon-triangle-1-s')
                                .animate({opacity : .33}, 200)
                                .removeClass('ui-toggled');

                        if (!_self.cfg.singleSort) controlRow.siblings()
                                .find('.ui-sortable-column-order')
                                .html('&#160;');
                        // remove previous gradients
                        //headerCell.siblings().removeClass('ui-state-active').find('.ui-sortable-column-icon').removeClass('ui-icon-triangle-1-n ui-icon-triangle-1-s');
                    }

                    var rowFound = false;
                    ice.ace.jq(_self.sortOrder).each(function() {
                        if (controlRow.attr('class') === this.attr('class')) { rowFound = true; } });

                    // if meta clicking a currently sorted row
                    if (metaKey && rowFound) {
                        // if deselecting
                        if ((ice.ace.util.getOpacity(topCarat) == 1 && !descending) ||
                                (ice.ace.util.getOpacity(bottomCarat) == 1 && descending)) {
                            // Remove from sort order
                            _self.sortOrder.splice(controlCell.find('.ui-sortable-column-order').html()-1,1);
                            ice.ace.jq(bottomCarat).animate({opacity : .33},  200).removeClass('ui-toggled');
                            ice.ace.jq(topCarat).animate({opacity : .33},  200).removeClass('ui-toggled');
                            if (!_self.cfg.singleSort) {
                                controlCell.find('.ui-sortable-column-order').html('&#160;');
                                var i = 0;
                                ice.ace.jq(_self.sortOrder).each(function(){
                                    this.find('.ui-sortable-column-order').html(parseInt(i++)+1);
                                });
                            }
                        } else {
                            // Not a deselect, just meta toggle
                            if (descending) {
                                ice.ace.jq(bottomCarat).animate({opacity : 1},  200).addClass('ui-toggled');
                                ice.ace.jq(topCarat).animate({opacity : .33},  200).removeClass('ui-toggled');
                            } else {
                                ice.ace.jq(topCarat).animate({opacity : 1},  200).addClass('ui-toggled');
                                ice.ace.jq(bottomCarat).animate({opacity : .33},  200).removeClass('ui-toggled');
                            }
                        }
                        // if not a deselect
                    } else {
                        // add header gradient
                        //headerCell.addClass('ui-state-active');
                        if (descending) {
                            ice.ace.jq(bottomCarat).animate({opacity : 1},  200).addClass('ui-toggled');
                            ice.ace.jq(topCarat).animate({opacity : .33},  200).removeClass('ui-toggled');
                        } else {
                            ice.ace.jq(topCarat).animate({opacity : 1},  200).addClass('ui-toggled');
                            ice.ace.jq(bottomCarat).animate({opacity : .33},  200).removeClass('ui-toggled');
                        }

                        // add to sort order
                        var rowFound = false;
                        ice.ace.jq(_self.sortOrder).each(
                                function() { if (controlRow.attr('class') === this.attr('class')) { rowFound = true; } });
                        if (!rowFound) {
                            var order = _self.sortOrder.push(controlRow);
                            // write control display value
                            if (!_self.cfg.singleSort) controlCell.find('.ui-sortable-column-order').html(order);
                        }
                    }
                });

        // Pre-fade and bind keypress to kb-navigable sort icons
        this.$this.find('.ui-tableconf-body tr:not(.ui-disabled) .ui-sortable-control')
                .find('.ui-icon-triangle-1-n')
                .keypress(function(event) {
                    var metaKey = ice.ace.jq.browser.os == 'mac' ? event.metaKey : event.ctrlKey;

                    if (event.which == 32 || event.which == 13) {
                        var $currentTarget = ice.ace.jq(event.currentTarget);
                        $currentTarget.closest('.ui-sortable-control')
                                .trigger('click', [$currentTarget.offset().top, metaKey]);
                    }}).not('.ui-toggled').fadeTo(0, 0.33);

        this.$this.find('.ui-tableconf-body tr:not(.ui-disabled) .ui-sortable-control')
                .find('.ui-icon-triangle-1-s')
                .keypress(function(event) {
                    var metaKey = ice.ace.jq.browser.os == 'mac' ? event.metaKey : event.ctrlKey;

                    if (event.which == 32 || event.which == 13) {
                        var $currentTarget = ice.ace.jq(event.currentTarget);
                        $currentTarget.closest('.ui-sortable-control')
                                .trigger('click', [$currentTarget.offset().top + 6, metaKey]);
                    }}).not('.ui-toggled').fadeTo(0, 0.33);
    }
}

ice.ace.TableConf.prototype.setupOkButton = function() {
    var self = this;
    ice.ace.jq(this.id + "_tableconf_ok")
            .off('mouseenter mouseleave click')
            .hover(function (event) {
                ice.ace.jq(event.currentTarget).toggleClass('ui-state-hover');
            })
            .click(function (event) {
                ice.ace.jq(self.id + "_tableconf_launch").removeClass('ui-state-active');

                self.$this.toggle();
                self.modal.toggle();

                var panel = ice.ace.jq(self.id);
                if (panel.is(':not(:visible)'))
                    self.submitTableConfig(event.currentTarget);
            });
}

ice.ace.TableConf.prototype.setupTrashButton = function() {
    var self = this;
    ice.ace.jq(this.id + "_tableconf_trash")
            .off('mouseenter mouseleave click')
            .hover(function (event) {
                ice.ace.jq(event.currentTarget).toggleClass('ui-state-hover');
            })
            .click(function (event) {
                ice.ace.jq(self.id + "_tableconf_launch").removeClass('ui-state-active');

                self.$this.toggle();
                self.modal.toggle();

                var panel = ice.ace.jq(self.id);
                if (panel.is(':not(:visible)'))
                    self.trashTableConfig(event.currentTarget);
            });
}

ice.ace.TableConf.prototype.setupCloseButton = function() {
    var self = this;
    ice.ace.jq(this.id + "_tableconf_close")
            .off('mouseenter mouseleave click')
            .hover(function (event) {
                ice.ace.jq(event.currentTarget).toggleClass('ui-state-hover');
            })
            .click(function (event) {
                ice.ace.jq(self.id + "_tableconf_launch")
                        .removeClass('ui-state-active');

                self.$this.toggle();
                self.modal.toggle();

                if (self.cfg.behaviors && self.behaviors.cancel)
                    ice.ace.ab(self.behaviors.cancel);
            });
}

ice.ace.TableConf.prototype.getSortAscending= function() {
    var sortAsc = {}, maxOrder = 0;
    this.$this.find('tr[class^="ui-tableconf-row"] .ui-sortable-column-icon').each(function(i, val) {
        var $val = ice.ace.jq(val),
            $order = ice.ace.jq(ice.ace.jq($val.closest('.ui-sortable-control')).find('.ui-sortable-column-order')[0]),
            topCarat = $val.find('.ui-icon-triangle-1-n')[0],
            bottomCarat = $val.find('.ui-icon-triangle-1-s')[0];
        if (ice.ace.util.getOpacity(topCarat) == 1 || ice.ace.util.getOpacity(bottomCarat) == 1) {
            sortAsc[parseInt($order.html())] = (ice.ace.util.getOpacity(topCarat) == 1);
            maxOrder++;
        }
    });

    var ascList = [], i = 0;
    while (i < maxOrder) {
        i++;
        ascList.push(sortAsc[i]);
    }

    return ascList;
}

ice.ace.TableConf.prototype.getSortOrder = function() {
    var sortOrders = [],
        _self = this,
        maxOrder = 0,
        columnOrders = this.getColOrder();

    // collect order and id for all sorted controls
    this.$this.find('tr[class^="ui-tableconf-row"] .ui-sortable-column-icon').each(function(i, val) {
        var $val = ice.ace.jq(val),
                topCarat = $val.find('.ui-icon-triangle-1-n')[0],
                bottomCarat = $val.find('.ui-icon-triangle-1-s')[0],
                $row = ice.ace.jq($val.closest('tr')),
                $order = ice.ace.jq($row.find('.ui-sortable-column-order')[0]);

        if (ice.ace.util.getOpacity(topCarat) == 1 || ice.ace.util.getOpacity(bottomCarat) == 1) {
            sortOrders[parseInt($order.html())-1] = $row.attr('class').split(" ")[0].replace("ui-tableconf-row-","");
            maxOrder++;
        }
    });

    // Change row index into column position.
    var sortList = [];
    for (var i = 0; i < sortOrders.length; i++) {
        sortList.push(columnOrders.indexOf(sortOrders[i]));
    }

    return sortList;
}

ice.ace.TableConf.prototype.getColOrder = function() {
    var columnOrders = [];
    this.$this.find('tr[class^="ui-tableconf-row"]').each(function(i, val) {
        columnOrders.push(ice.ace.jq(val).attr('class').split(" ")[0].replace("ui-tableconf-row-",""));
    });
    return columnOrders;
}

ice.ace.TableConf.prototype.trashTableConfig = function (target) {
    var id = this.tableId.replace("#","").replace(/\\/g,""),
        selfId = this.id.replace("#","").replace(/\\/g,""),
        options = {
            source: id,
            execute: id,
            render: id + ' ' + selfId,
            params: {}
        };

    options.params[id+"_tabletrash"] = true;

    if (this.behaviors)
        if (this.behaviors.trash) {
            ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.trash, options));
            return;
        }

    ice.ace.AjaxRequest(options);
}


ice.ace.TableConf.prototype.submitTableConfig = function (target) {
    var id = this.tableId.replace("#","").replace(/\\/g,""),
        selfId = this.id.replace("#","").replace(/\\/g,""),
        body = ice.ace.jq(target).parents('.ui-tableconf').find('.ui-tableconf-body:first'),
        options = {
            source: id,
            execute: id,
            render: id + ' ' + selfId
        };

    var params = {}, panelConf = "";
    if (this.cfg.reorderable) panelConf += " colOrd";
    if (this.cfg.sortable) panelConf += " colSor";
    if (this.cfg.naming) panelConf += " colName";
    if (this.cfg.visibility) panelConf += " colVis";
    if (this.cfg.position == "first-col")
        panelConf += " butPos-first-col";
    else if (this.cfg.position == "last-col")
        panelConf += " butPos-last-col";

    params[id+"_tableconf"] = panelConf;
    params[id+'_colorder'] = this.getColOrder();
    params[id+'_sortKeys'] = this.getSortOrder();
    params[id+'_sortDirs'] = this.getSortAscending();

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.submit) {
            ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.submit, options));
            return;
        }

    ice.ace.AjaxRequest(options);
}
