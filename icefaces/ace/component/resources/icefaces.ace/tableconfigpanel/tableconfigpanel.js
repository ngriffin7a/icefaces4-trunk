/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

        // Size the headers and body to line up. ordering, name, visibility, sorting
        var maxWidth;
        maxWidth = Math.max.apply(Math, panel.find('.ui-tableconf-body .ordering').
                map(function(idx, elem) {return elem.offsetWidth;}).get());
        if (maxWidth > 0) panel.find('.ui-tableconf-body .ordering').width(maxWidth);
        maxWidth = Math.max.apply(Math, panel.find('.ui-tableconf-body .name').
                map(function(idx, elem) {return elem.offsetWidth;}).get());
        if (maxWidth > 0) panel.find('.ui-tableconf-body .name').width(maxWidth);
        maxWidth = Math.max.apply(Math, panel.find('.ui-tableconf-body .visibility').
                map(function(idx, elem) {return elem.offsetWidth;}).get());
        if (maxWidth > 0) panel.find('.ui-tableconf-body .visibility').width(maxWidth);
        maxWidth = Math.max.apply(Math, panel.find('.ui-tableconf-body .sorting').
                map(function(idx, elem) {return elem.offsetWidth;}).get());
        if (maxWidth > 0) panel.find('.ui-tableconf-body .sorting').width(maxWidth);
    }

    var unload = function() {
        launcher.off('click mouseenter mouseleave keyup keypress');
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
            if (e.which == 13 || e.which == 32) activate(e);
        }).keypress(function(e) {
            if (e.which == 13 || e.which == 32) return false;
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
    dragConfig.containment = "document";

    if (cfg.handle)
        dragConfig.handle = cfg.handle;
    else
        dragConfig.handle = ".ui-tableconf-header";

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

        if (cfg.sortableContainerIds) {
            for (var i = 0; i < cfg.sortableContainerIds.length; i++) {
                this.$this.find('.'+cfg.sortableContainerIds[i]).sortable({
                    axis:'y',
                    // Problematic or impossible to move items to very top or bottom with parent containment
                    //containment:'parent',
                    items:'>div.ui-tableconf-subtree:not(.ui-disabled)',
                    handle:'.ui-sortable-handle:not(.ui-disabled)'
                });
                var handles = this.$this.find('.' + cfg.sortableContainerIds[i] + '>div.ui-tableconf-subtree:not(.ui-disabled) .ui-sortable-handle:not(.ui-disabled)');
                handles.on('click', function(event) {
                    event.preventDefault();
                });
                handles.disableSelection();
            }
        }
    }

    if (cfg.sortable) {
        var _self = this;

        this.$this.find('.ui-disabled').on('dragstart', function(event) {
            if (event.preventDefault) {
                event.preventDefault();
            } else {
                event.returnValue = false;
            }
        });

        this.$this.find('.ui-tableconf-body div.ui-tableconf-item:not(.ui-disabled) .ui-sortable-control')
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
                            var foundNorth = $this.find('.ui-icon-triangle-1-n')[0];
                            var opacityNorth = ice.ace.util.getOpacity(foundNorth);
                            var foundSouth = $this.find('.ui-icon-triangle-1-s')[0];
                            var opacitySouth = ice.ace.util.getOpacity(foundSouth);
                            if (opacityNorth == 1 || opacitySouth == 1) {
                                var columnOrder = $this.find('.ui-sortable-column-order');
                                var columnOrderHtml = columnOrder.html();
                                var columnOrderParsed1 = parseInt(columnOrderHtml)-1;
                                var closestRow = $this.closest('div[data-tableconf-headcol]');
                                _self.sortOrder.splice(
                                        columnOrderParsed1,
                                        0,
                                        closestRow);
                            }
                            /*
                            if (ice.ace.util.getOpacity($this.find('.ui-icon-triangle-1-n')[0]) == 1 ||
                                    ice.ace.util.getOpacity($this.find('.ui-icon-triangle-1-s')[0]) == 1 )
                                _self.sortOrder.splice(
                                        parseInt($this.find('.ui-sortable-column-order').html())-1,
                                        0,
                                        $this.closest('div.ui-tableconf-item')
                                );
                            */
                        });
                    }

                    if (!metaKey || _self.cfg.singleSort) {
                        // Remake sort criteria
                        // Reset all other arrows
                        _self.sortOrder = [];
                        var controlRowIdentifier = controlRow.attr('data-tableconf-headcol');
                        var everyOtherRow = _self.$this.find('.ui-tableconf-body div.ui-tableconf-item:not(.ui-disabled)[data-tableconf-headcol!="'+controlRowIdentifier+'"] .ui-sortable-control');
                                everyOtherRow.find('.ui-icon-triangle-1-n, .ui-icon-triangle-1-s')                                .animate({opacity : .33}, 200)
                                .removeClass('ui-toggled');

                        if (!_self.cfg.singleSort) {
                            everyOtherRow.find('.ui-sortable-column-order')
                                .html('&#160;');
                        }
                        // remove previous gradients
                        //headerCell.siblings().removeClass('ui-state-active').find('.ui-sortable-column-icon').removeClass('ui-icon-triangle-1-n ui-icon-triangle-1-s');
                    }

                    var rowFound = false;
                    var controlRowIdentifier = controlRow.attr('data-tableconf-headcol');
                    ice.ace.jq(_self.sortOrder).each(function() {
                        var iterRowIdentifier = this.attr('data-tableconf-headcol');
                        if (controlRowIdentifier === iterRowIdentifier) { rowFound = true; }
                    });

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
                                var columnOrder = controlCell.find('.ui-sortable-column-order');
                                columnOrder.html('&#160;');
                                var i = 0;
                                ice.ace.jq(_self.sortOrder).each(function(){
                                    var columnOrderOther = this.find('.ui-sortable-column-order');
                                    var j = parseInt(i++)+1;
                                    columnOrderOther.html(j);
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
                        var controlRowIdentifier = controlRow.attr('data-tableconf-headcol');
                        ice.ace.jq(_self.sortOrder).each(function() {
                            var iterRowIdentifier = this.attr('data-tableconf-headcol');
                            if (controlRowIdentifier === iterRowIdentifier) { rowFound = true; }
                        });

                        if (!rowFound) {
                            var order = _self.sortOrder.push(controlRow);
                            // write control display value
                            if (!_self.cfg.singleSort) {
                                var columnOrder = controlCell.find('.ui-sortable-column-order');
                                columnOrder.html(order);
                            }
                        }
                    }
                });

        // Pre-fade and bind keypress to kb-navigable sort icons
        this.$this.find('.ui-tableconf-body div.ui-tableconf-item:not(.ui-disabled) .ui-sortable-control')
                .find('.ui-icon-triangle-1-n')
                .keypress(function(event) {
                    var metaKey = ice.ace.jq.browser.os == 'mac' ? event.metaKey : event.ctrlKey;

                    if (event.which == 32 || event.which == 13) {
                        var $currentTarget = ice.ace.jq(event.currentTarget);
                        $currentTarget.closest('.ui-sortable-control')
                                .trigger('click', [$currentTarget.offset().top, metaKey]);
						return false;
                    }}).not('.ui-toggled').fadeTo(0, 0.33);

        this.$this.find('.ui-tableconf-body div.ui-tableconf-item:not(.ui-disabled) .ui-sortable-control')
                .find('.ui-icon-triangle-1-s')
                .keypress(function(event) {
                    var metaKey = ice.ace.jq.browser.os == 'mac' ? event.metaKey : event.ctrlKey;

                    if (event.which == 32 || event.which == 13) {
                        var $currentTarget = ice.ace.jq(event.currentTarget);
                        $currentTarget.closest('.ui-sortable-control')
                                .trigger('click', [$currentTarget.offset().top + 6, metaKey]);
						return false;
                    }}).not('.ui-toggled').fadeTo(0, 0.33);
    }

	this.$this.find("input[type='checkbox']").off('keyup keypress').keypress(function(event) {
		if (event.which == 32 || event.which == 13) {
			return false;
		}
	}).keyup(function(event) {
		if (event.which == 13) {
			$element = ice.ace.jq(this);
			var value = $element.is(':checked');
			if (value) $element.removeAttr('checked');
			else $element.attr('checked', 'checked');
		}
	});
};

ice.ace.TableConf.prototype.setupOkButton = function() {
    var self = this;
    ice.ace.jq(this.id + "_tableconf_ok")
            .off('mouseenter mouseleave click keyup keypress')
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
			})
			.keyup(function (e) {
				if (e.which == 13 || e.which == 32) this.click();
			})
			.keypress(function (e) {
				if (e.which == 13 || e.which == 32) return false;
            });
}

ice.ace.TableConf.prototype.setupTrashButton = function() {
    var self = this;
    ice.ace.jq(this.id + "_tableconf_trash")
            .off('mouseenter mouseleave click keyup keypress')
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
			})
			.keyup(function (e) {
				if (e.which == 13 || e.which == 32) this.click();
			})
			.keypress(function (e) {
				if (e.which == 13 || e.which == 32) return false;
            });
}

ice.ace.TableConf.prototype.setupCloseButton = function() {
    var self = this;
    ice.ace.jq(this.id + "_tableconf_close")
            .off('mouseenter mouseleave click keyup keypress')
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
			})
			.keyup(function (e) {
				if (e.which == 13 || e.which == 32) this.click();
			})
			.keypress(function (e) {
				if (e.which == 13 || e.which == 32) return false;
            });
}

ice.ace.TableConf.prototype.getSortAscending= function() {
    var sortAsc = {}, maxOrder = 0;
    this.$this.find('div.ui-tableconf-item .ui-sortable-column-icon').each(function(i, val) {
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

    /*
    div.ui-tableconf-item[data-tableconf-headcol]
     div.stacked-first / stacked-subsequent [data-tableconf-headcol]
      span.sorting
        span.ui-tableconf-sort-cont
          span.ui-sortable-control
            span.ui-sortable-column-icon
              a.ui-icon-triangle-1-n / ui-icon-triangle-1-s
            span.ui-sortable-column-order
     */
    // collect order and id for all sorted controls
    this.$this.find('div.ui-tableconf-item .ui-sortable-column-icon').each(function(i, val) {
        var $val = ice.ace.jq(val),
                topCarat = $val.find('.ui-icon-triangle-1-n')[0],
                bottomCarat = $val.find('.ui-icon-triangle-1-s')[0],
                $row = ice.ace.jq($val.closest('div[data-tableconf-headcol]')),
                $order = ice.ace.jq($row.find('.ui-sortable-column-order')[0]);

        if (ice.ace.util.getOpacity(topCarat) == 1 || ice.ace.util.getOpacity(bottomCarat) == 1) {
            sortOrders[parseInt($order.html())-1] = $row.attr('data-tableconf-headcol');
            maxOrder++;
        }
    });
    return sortOrders;
};

// Depth first traversal


ice.ace.TableConf.prototype.getColOrder = function() {
    var columnOrders = [];
    this.$this.find('div[data-tableconf-col]').each(
        function(i, val) {
            var elem = ice.ace.jq(val);
            var idx = elem.attr('data-tableconf-col');
            columnOrders.push(idx);
        });
    return columnOrders;
};

// Breadth first traversal
ice.ace.TableConf.prototype.getHeadColOrder = function() {
    var headColumnOrders = [];
    this.breadthFirstTraversal(this.$this.find(".ui-tableconf-body").first(), function(elem) {
        if (elem.hasClass("ui-tableconf-item")) {
            var idx = elem.attr('data-tableconf-headcol');
            if (idx !== undefined) {
                headColumnOrders.push(idx);
            } else {
                elem.children('div[class*="stacked-"]').each(
                    function(i, val) {
                        var stackElem = ice.ace.jq(val);
                        idx = stackElem.attr('data-tableconf-headcol');
                        if (idx !== undefined) {
                            headColumnOrders.push(idx);
                        }
                    });
            }
        }
    });
    return headColumnOrders;
};

ice.ace.TableConf.prototype.breadthFirstTraversal = function(node, action) {
    var queue = [node];
    while (queue.length > 0) {
        var elem = queue.shift();

        action(elem);

        // elem.firstChild / child.nextSibling
        for (var child=ice.ace.jq(">:first-child", elem);
             child != null && child.length > 0;
             child=child.next()) {
            queue.push(child);
        }
    }
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
};


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
    params[id+'_tcp_headcolorder'] = this.getHeadColOrder();
    params[id+'_tcp_colorder'] = this.getColOrder();
    params[id+'_sortKeys'] = this.getSortOrder();
    params[id+'_sortDirs'] = this.getSortAscending();

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.submit) {
            ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.submit, options));
            return;
        }

    ice.ace.AjaxRequest(options);
};

ice.ace.TableConf.prototype.show = function() {
	var panel = this.$this,
		modal = panel.next('.ui-tableconf-modal');

	panel.toggle();
	modal.toggle();

	if (panel.is(':not(:visible)'))
		this.submitTableConfig(panel.find('.ui-tableconf-header').get(0));
	else if (this.behaviors && this.behaviors.open)
		ice.ace.ab(this.behaviors.open);

	// Size the headers and body to line up. ordering, name, visibility, sorting
	var maxWidth;
	maxWidth = Math.max.apply(Math, panel.find('.ui-tableconf-body .ordering').
			map(function(idx, elem) {return elem.offsetWidth;}).get());
	if (maxWidth > 0) panel.find('.ui-tableconf-body .ordering').width(maxWidth);
	maxWidth = Math.max.apply(Math, panel.find('.ui-tableconf-body .name').
			map(function(idx, elem) {return elem.offsetWidth;}).get());
	if (maxWidth > 0) panel.find('.ui-tableconf-body .name').width(maxWidth);
	maxWidth = Math.max.apply(Math, panel.find('.ui-tableconf-body .visibility').
			map(function(idx, elem) {return elem.offsetWidth;}).get());
	if (maxWidth > 0) panel.find('.ui-tableconf-body .visibility').width(maxWidth);
	maxWidth = Math.max.apply(Math, panel.find('.ui-tableconf-body .sorting').
			map(function(idx, elem) {return elem.offsetWidth;}).get());
	if (maxWidth > 0) panel.find('.ui-tableconf-body .sorting').width(maxWidth);
};