/*
* Original Code Copyright Prime Technology.
* Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM
*
* Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c).
*
* Code Modification 1: Integrated with ICEfaces Advanced Component Environment.
* Contributors: ICEsoft Technologies Canada Corp. (c)
*
* Code Modification 2: Improved Scrollable DataTable Column Sizing - ICE-7028
* Contributors: Nils Lundquist
*
* Code Modification 3: Added CustomUpdate Param - Fixed DomDiff - ICE-6950
* Contributors: Nils Lundquist
*
* Code Modification 4: Added Keyboard Navigation
* Contributors: Nils Lundquist
*
* Code Modification 5: Row Deselection Tracking
* Contributors: Nils Lundquist
*
*/
/**
* DataTable Widget
*/
if (!window.ice['ace']) {
    window.ice.ace = {};
}

// JQuery Utilities
(function ($) {
    var rootrx = /^(?:html)$/i;

    var converter = {
        vertical:{ x:false, y:true },
        horizontal:{ x:true, y:false },
        both:{ x:true, y:true },
        x:{ x:true, y:false },
        y:{ x:false, y:true }
    };

    var scrollValue = {
        auto:true,
        scroll:true,
        visible:false,
        hidden:false
    };

    $.getScrollWidth = function() {
        var inner = $('<p></p>').css({
            'width':'100%',
            'height':'100%'
        });
        var outer = $('<div></div>').css({
            'position':'absolute',
            'width':'100px',
            'height':'100px',
            'top':'0',
            'left':'0',
            'visibility':'hidden',
            'overflow':'hidden'
        }).append(inner);

        $(document.body).append(outer);

        var w1 = inner.width();
        outer.css('overflow','scroll');
        var w2 = inner.width();
        if (w1 == w2 && outer[0].clientWidth) {
            w2 = outer[0].clientWidth;
        }

        outer.detach();

        return (w1 - w2);
    };

    $.extend($.expr[":"], {
        scrollable:function (element, index, meta, stack) {
            var direction = converter[typeof (meta[3]) === "string" && meta[3].toLowerCase()] || converter.both;
            var styles = (document.defaultView && document.defaultView.getComputedStyle ? document.defaultView.getComputedStyle(element, null) : element.currentStyle);
            var overflow = {
                x:scrollValue[styles.overflowX.toLowerCase()] || false,
                y:scrollValue[styles.overflowY.toLowerCase()] || false,
                isRoot:rootrx.test(element.nodeName)
            };

            // check if completely unscrollable (exclude HTML element because it's special)
            if (!overflow.x && !overflow.y && !overflow.isRoot) {
                return false;
            }

            var size = {
                height:{
                    scroll:element.scrollHeight,
                    client:element.clientHeight
                },
                width:{
                    scroll:element.scrollWidth,
                    client:element.clientWidth
                },
                // check overflow.x/y because iPad (and possibly other tablets) don't dislay scrollbars
                scrollableX:function () {
                    return (overflow.x || overflow.isRoot) && this.width.scroll > this.width.client;
                },
                scrollableY:function () {
                    return (overflow.y || overflow.isRoot) && this.height.scroll > (this.height.client + 1);
                }
            };
            return direction.y && size.scrollableY() || direction.x && size.scrollableX();
        }
    });

    var OSDetect = {
        init:function () {
            this.OS = this.searchString(this.dataOS) || "an unknown OS";
        },

        searchString:function (data) {
            for (var i = 0; i < data.length; i++) {
                var dataString = data[i].string;
                var dataProp = data[i].prop;
                if (dataString) {
                    if (dataString.indexOf(data[i].subString) != -1)
                        return data[i].identity;
                }
                else if (dataProp)
                    return data[i].identity;
            }
        },

        dataOS:[
            {   string:navigator.platform,
                subString:"Win",
                identity:"win"
            },
            {   string:navigator.platform,
                subString:"Mac",
                identity:"mac"
            },
            {   string:navigator.userAgent,
                subString:"iPhone",
                identity:"ios"
            },
            {   string:navigator.platform,
                subString:"Linux",
                identity:"linux"
            }
        ]
    };

    OSDetect.init();
    $.browser.chrome = /chrome/.test(navigator.userAgent.toLowerCase()) && !/chromeframe/.test(navigator.userAgent.toLowerCase());
    // Chrome has 'safari' in its user string so we need to exclude it explicitly
    $.browser.safari = /safari/.test(navigator.userAgent.toLowerCase()) && !$.browser.chrome;
    $.browser['os'] = OSDetect.OS;
})(ice.ace.jq);





// Constructor
ice.ace.DataTable = function (id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.sortOrder = [];
    this.columnPinOrder = {};
    this.columnPinPosition = {};
    this.columnPinScrollListener = {};
    this.parentResizeDelaySet = false;
    this.delayedFilterCall = null;
    this.behaviors = cfg.behaviors;
    this.parentSize = 0;
    this.scrollLeft = 0;
    this.scrollTop = 0;
    this.currentPinRegionOffset = 0;
    this.lastSelectIndex = -1;
    this.element = ice.ace.jq(this.jqId);
    this.selectionHolder = this.jqId + '_selection';
    this.deselectionHolder = this.jqId + '_deselection';
    this.selection = [];
    this.deselection = [];

    var oldInstance = arguments[2];
    var rowEditors = this.getRowEditors();

    // Persist State
    if (oldInstance) {
        this.scrollLeft = oldInstance.scrollLeft;
        this.scrollTop = oldInstance.scrollTop;
    }

    if (this.cfg.paginator)
        this.setupPaginator();

    if (!this.cfg.disabled) {
        this.setupClickEvents();

        if (this.cfg.sorting)
            this.setupSortEvents();

        if (this.cfg.configPanel)
            if (this.cfg.configPanel.indexOf(":") == 0)
                this.cfg.configPanel = this.cfg.configPanel.substring(1);

        if (this.cfg.panelExpansion)
            this.setupPanelExpansionEvents();

        if (this.cfg.rowExpansion)
            this.setupRowExpansionEvents();

        if (this.cfg.scrollable)
            this.setupScrolling();

        if (rowEditors.length > 0)
            this.setupCellEditorEvents(rowEditors);

        if (this.cfg.resizableColumns)
            this.setupResizableColumns();

        // blur and keyup are handled by the xhtml on____ attributes, and written by the renderer
        if (this.cfg.filterEvent && this.cfg.filterEvent != "blur" && this.cfg.filterEvent != "keyup")
            this.setupFilterEvents();

        if (this.cfg.reorderableColumns) {
            this.reorderStart = 0;
            this.reorderEnd = 0;
            this.setupReorderableColumns();
        }
    } else
        this.setupDisabledStyling();

    // Explicitly dereference helper variables & old DT instance.
    oldInstance = null;
    rowEditors = null;

    // Setup unload callback if not already done
    if (!window[this.cfg.widgetVar]) {
        var self = this;
        ice.onElementUpdate(this.id, function() { ice.ace.destroy(self.id); });
    }
}


// Selectors
ice.ace.DataTable.prototype.sortColumnSelector = ' > div > table > thead > tr > th > div.ui-sortable-column';
ice.ace.DataTable.prototype.sortControlSelector = ' > div > table > thead > tr > th > div.ui-sortable-column > span > span.ui-sortable-control';
ice.ace.DataTable.prototype.sortUpSelector = ice.ace.DataTable.sortControlSelector + ' a.ui-icon-triangle-1-n';
ice.ace.DataTable.prototype.sortDownSelector = ice.ace.DataTable.sortControlSelector + ' a.ui-icon-triangle-1-s';
ice.ace.DataTable.prototype.rowSelector = ' > div > table > tbody.ui-datatable-data > tr:not(.ui-unselectable)';
ice.ace.DataTable.prototype.cellSelector = ' > div > table > tbody.ui-datatable-data > tr:not(.ui-unselectable) > td';
ice.ace.DataTable.prototype.scrollBodySelector = ' > div.ui-datatable-scrollable-body';
ice.ace.DataTable.prototype.bodyTableSelector = '> div > table > tbody.ui-datatable-data',
ice.ace.DataTable.prototype.filterSelector = ' > div > table > thead > tr > th > div > input.ui-column-filter';
ice.ace.DataTable.prototype.panelExpansionSelector = ' > div > table > tbody.ui-datatable-data > tr:not(.ui-expanded-row-content) > td *:not(tbody) a.ui-row-panel-toggler';
ice.ace.DataTable.prototype.rowExpansionSelector = ' > div > table > tbody.ui-datatable-data > tr > td *:not(tbody) a.ui-row-toggler';
// 'link' will be replaced with the style class of the element in question
ice.ace.DataTable.prototype.cellEditorSelector = ' > div > table > tbody.ui-datatable-data > tr > td > div.ui-row-editor link, ' +
    ' > div > table > tbody.ui-datatable-data > tr > td > div > div.ui-row-editor link';


/* ########################################################################
   ########################## Event Binding & Setup #######################
   ######################################################################## */
ice.ace.DataTable.prototype.destroy = function() {
    // Remove dynamic stylesheet
    // ice.ace.util.removeStyleSheet(this.jqId.substr(1)+'_colSizes');

    // Cleanup sort events
    this.element.find(this.sortColumnSelector).unbind("click").unbind("mousemove").unbind("mouseleave");

    var sortControls = this.element.find(this.sortControlSelector);
    sortControls.unbind("click mousemove mouseleave");
    this.element.off('keypress', this.sortUpSelector);
    this.element.off('keypress', this.sortDownSelector);

    // Clear selection events
    this.element.off('click dblclick', this.cellSelector)
            .off('click dblclick', this.rowSelector);

    // Unbind hover
    this.element.off('mouseenter')
            .find(this.bodyTableSelector).parent().unbind('mouseleave')
            .find('thead').unbind('mouseenter');

    // Clear scrolling
    ice.ace.jq(window).unbind('resize', this.scrollableResizeCallback);
    this.element.find(this.scrollBodySelector).unbind('scroll');

    // Clear filter events
    this.element.off('keyup keypress', this.filterSelector);

    // Clear panel expansion events
    this.element.off('keyup click', this.panelExpansionSelector);

    // Clear row expansion events
    this.element.off('keyup click', this.rowExpansionSelector);

    // Clear cell editor events
    var icoSel = this.cellEditorSelector.replace(/link/g, 'a.ui-icon-pencil');
    this.element.off('click keyup', icoSel);

    icoSel = this.cellEditorSelector.replace(/link/g, 'a.ui-icon-check');
    this.element.off('click keyup', icoSel);

    icoSel = this.cellEditorSelector.replace(/link/g, 'a.ui-icon-close');
    this.element.off('click keyup', icoSel);

    this.getRowEditors().closest('tr')
            .find(' > div.ui-cell-editor > span > input')
            .unbind('keypress');

    if (this.paginator) {
        this.paginator.destroy();
        delete this.paginator;
    }
    window[this.cfg.widgetVar] = undefined;

    var clientState = {scrollTop : this.scrollTop, scrollLeft : this.scrollLeft};

    return clientState;
}

ice.ace.DataTable.prototype.setupFilterEvents = function () {
    var _self = this;
    if (this.cfg.filterEvent == "enter")
        this.element.on('keypress', this.filterSelector, function (event) {
            event.stopPropagation();
            if (event.which == 13) {
                _self.filter(event);
                return false; // Don't run form level enter key handling
            }
        });

    else if (this.cfg.filterEvent == "change")
        this.element.on('keyup', this.filterSelector, function (event) {
            var _event = event;
            if (event.which == 8 || event.which == 13 || event.which > 40 || event.isTrigger) {
                if (_self.delayedFilterCall)
                    clearTimeout(_self.delayedFilterCall);

                _self.delayedFilterCall = setTimeout(function () {
                    _self.filter(_event);
                }, 400);
            }
        });
}

ice.ace.DataTable.prototype.setupPaginator = function () {
    this.paginator = new ice.ace.DataTable.Paginator(this);
}

ice.ace.DataTable.prototype.restoreSortState = function(savedState) {
    this.element.find(this.sortColumnSelector + ' a.ui-icon').removeClass('ui-toggled').fadeTo(0,0.33);

    this.sortOrder = savedState[0];

    ice.ace.jq(savedState[1]).each(function (i, val) {
        var column = ice.ace.jq(ice.ace.escapeClientId(val[0]));

        if (val[1]) {
            column.find('.ui-icon-triangle-1-n:first').addClass('ui-toggled').fadeTo(0, 1);
        } else {
            column.find('.ui-icon-triangle-1-s:first').addClass('ui-toggled').fadeTo(0, 1);
        }
    });
};

ice.ace.DataTable.prototype.saveSortState = function() {
    var self = this,
        sortState = [];

    if (this.sortOrder.length == 0) {
        this.element.find(this.sortControlSelector).each(function () {
            var $this = ice.ace.jq(this);
            if (ice.ace.util.getOpacity($this.find(' > span.ui-sortable-column-icon > a.ui-icon-triangle-1-n')[0]) == 1 ||
                    ice.ace.util.getOpacity($this.find(' > span.ui-sortable-column-icon > a.ui-icon-triangle-1-s')[0]) == 1)
                self.sortOrder.splice(
                        parseInt($this.find(' > span.ui-sortable-column-order').html()) - 1,
                        0,
                        $this.closest('.ui-header-column')
                );
        });
    }

    ice.ace.jq(this.sortOrder).each(function (i, val) {
        var columnState = [];
        columnState.push(val.closest('.ui-header-column').attr('id'));
        columnState.push(val.find(".ui-icon-triangle-1-n:first").hasClass('ui-toggled'));
        sortState.push(columnState);
    });

    return [this.sortOrder, sortState];
}

ice.ace.DataTable.prototype.setupSortRequest = function (_self, $this, event, headerClick, altY, altMeta) {
    var topCarat = $this.find(".ui-icon-triangle-1-n")[0],
        bottomCarat = $this.find(".ui-icon-triangle-1-s")[0],
        headerCell = (headerClick) ? $this : $this.parent().parent(),
        controlOffset = $this.offset(),
        controlHeight = !_self.cfg.singleSort ? $this.outerHeight() : 22,
        descending = false,
        metaKey = (altMeta == undefined) ? (event.metaKey || event.ctrlKey ) : altMeta,
        ieOffset = ice.ace.jq.browser.msie ? 7 : 0,
        // altY and altMeta allow these event parameters to be optionally passed in
        // from an event triggering this event artificially
        eventY = (altY == undefined) ? event.pageY : altY,
        savedState = this.saveSortState();

    if (eventY > (controlOffset.top + (controlHeight / 2) - ieOffset))
        descending = true;

    if (headerClick) {
        if (ice.ace.jq(topCarat).hasClass('ui-toggled')) {
            descending = true;
        } else {
            descending = false;
        }
    }


    if (!metaKey || _self.cfg.singleSort) {
        // Remake sort criteria
        // Reset all other arrows
        _self.sortOrder = [];
        $this.closest('div.ui-header-column').siblings().find('> span > span > span.ui-sortable-column-icon > a').css('opacity', .2).removeClass('ui-toggled');
        headerCell.parent().siblings().find('> th > div.ui-header-column > span > span > span.ui-sortable-column-icon > a').css('opacity', .2).removeClass('ui-toggled');
    }

    var cellFound = false, index = 0;
    ice.ace.jq(_self.sortOrder).each(function () {
        if (headerCell.attr('id') === this.attr('id')) {
            cellFound = true;
            // If the cell already exists in our list, update the reference
            _self.sortOrder.splice(index, 1, headerCell);
        }
        index++;
    });

    if (metaKey && cellFound) {
        if ((ice.ace.util.getOpacity(topCarat) == 1 && !descending) ||
            (ice.ace.util.getOpacity(bottomCarat) == 1 && descending)) {
            // Remove from sort order
            _self.sortOrder.splice(headerCell.find('.ui-sortable-column-order').html() - 1, 1);
            ice.ace.jq(bottomCarat).css('opacity', .2).removeClass('ui-toggled');
            ice.ace.jq(topCarat).css('opacity', .2).removeClass('ui-toggled');
            if (!_self.cfg.singleSort) {
                headerCell.find('.ui-sortable-column-order').html('&#160;');
                var i = 0;
                ice.ace.jq(_self.sortOrder).each(function () {
                    this.find('.ui-sortable-column-order').html(parseInt(i++) + 1);
                });
            }
        } else {
            // Not a deselect, just a meta-toggle
            if (descending) {
                ice.ace.jq(bottomCarat).addClass('ui-toggled');
                ice.ace.jq(topCarat).removeClass('ui-toggled');
            } else {
                ice.ace.jq(topCarat).addClass('ui-toggled');
                ice.ace.jq(bottomCarat).removeClass('ui-toggled');
            }
        }
    } else {
        if (descending) {
            ice.ace.jq(bottomCarat).addClass('ui-toggled');
            ice.ace.jq(topCarat).removeClass('ui-toggled');
        } else {
            ice.ace.jq(topCarat).addClass('ui-toggled');
            ice.ace.jq(bottomCarat).removeClass('ui-toggled');
        }

        // add to sort order
        cellFound = false;
        ice.ace.jq(_self.sortOrder).each(function () {
            if (headerCell.attr('id') === this.attr('id')) {
                cellFound = true;
            }
        });
        if (cellFound == false) _self.sortOrder.push(headerCell);
    }
    // submit sort info
    _self.sort(_self.sortOrder, savedState);

    return false;
}

ice.ace.DataTable.prototype.setupSortEvents = function () {
    var _self = this;

    // Bind `clickable header events
    if (_self.cfg.clickableHeaderSorting) {
        this.element.find(this.sortColumnSelector)
            .unbind('click').bind("click", function (event) {
                var target = ice.ace.jq(event.target);

                var $this = ice.ace.jq(this),
                    topCarat = ice.ace.jq($this.find(".ui-icon-triangle-1-n")[0]),
                    bottomCarat = ice.ace.jq($this.find(".ui-icon-triangle-1-s")[0]);
                selectionMade = bottomCarat.hasClass('ui-toggled') || topCarat.hasClass('ui-toggled');

                // If the target of the event is not a layout element or
                // the target is a child of a sortable-control do not process event.
                if ((!(event.target.nodeName == 'SPAN') && !(event.target.nodeName == 'DIV') && !(event.target.nodeName == 'A')) ||
                    ((target.closest('.ui-sortable-control').length > 0) && !selectionMade))
                    return;

                _self.setupSortRequest(_self, ice.ace.jq(this), event, true);
            })
            .unbind('mousemove').bind('mousemove', function (event) {
                var target = ice.ace.jq(event.target);

                // If the target of the event is not a layout element do not process event.
                if ((!(event.target.nodeName == 'SPAN') && !(event.target.nodeName == 'DIV')
                    && !(event.target.nodeName == 'A'))) {
                    target.mouseleave();
                    return;
                }

                // if the target is a child of a sortable-control do not process the event
                if (target.closest('span.ui-sortable-control').length > 0) return;

                var $this = ice.ace.jq(this),
                    topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                    bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]);
                selectionMade = bottomCarat.hasClass('ui-toggled') || topCarat.hasClass('ui-toggled');

                if (_self.cfg.clickableHeaderSorting && !selectionMade) {
                    topCarat.fadeTo(0, .66);
                } else if (!_self.cfg.clickableHeaderSorting) {
                    if (!topCarat.hasClass('ui-toggled')) topCarat.fadeTo(0, .66);
                    else bottomCarat.fadeTo(0, .66);
                }

                if ($this.closest('th').find('> hr').size() == 0)
                    $this.closest('th').addClass('ui-state-hover');
                else
                    $this.closest('div.ui-sortable-column').addClass('ui-state-hover');
            })
            .unbind('mouseleave').bind('mouseleave', function (event) {
                var $this = ice.ace.jq(this),
                    topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                    bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]);

                if (!bottomCarat.hasClass('ui-toggled'))
                    if (topCarat.hasClass('ui-toggled') & _self.cfg.clickableHeaderSorting)
                        bottomCarat.fadeTo(0, 0);
                    else bottomCarat.fadeTo(0, .33);

                if (!topCarat.hasClass('ui-toggled'))
                    if (bottomCarat.hasClass('ui-toggled') & _self.cfg.clickableHeaderSorting)
                        topCarat.fadeTo(0, 0);
                    else topCarat.fadeTo(0, .33);

                if ($this.closest('th').find('> hr').size() == 0)
                    $this.closest('th').removeClass('ui-state-hover');
                else
                    $this.closest('div.ui-sortable-column').removeClass('ui-state-hover');
            });
    }

    // Bind clickable control events
    this.element.find(this.sortControlSelector)
        .unbind('click').bind("click", function (event, altY, altMeta) {
            var $this = ice.ace.jq(this),
                topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]),
                selectionMade = bottomCarat.hasClass('ui-toggled') || topCarat.hasClass('ui-toggled');

            if ((_self.cfg.clickableHeaderSorting && !selectionMade) || (!_self.cfg.clickableHeaderSorting)) {
                _self.setupSortRequest(_self, ice.ace.jq(this), event, false, altY, altMeta);
                event.stopPropagation();
            }
        })
        .unbind('mousemove').bind('mousemove', function (event) {
            var target = ice.ace.jq(event.target);

            var $this = ice.ace.jq(this),
                topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]),
                controlOffset = $this.offset(),
                controlHeight = !_self.cfg.singleSort ? $this.outerHeight() : 22,
                ieOffset = ice.ace.jq.browser.msie ? 4 : 0;

            if (!(_self.cfg.clickableHeaderSorting) || (!bottomCarat.hasClass('ui-toggled') && !topCarat.hasClass('ui-toggled'))) {
                if (event.pageY > (controlOffset.top + (controlHeight / 2) - ieOffset)) {
                    if (!bottomCarat.hasClass('ui-toggled'))
                        bottomCarat.fadeTo(0, .66);
                    if (!topCarat.hasClass('ui-toggled'))
                        topCarat.fadeTo(0, .33);
                } else {
                    if (!topCarat.hasClass('ui-toggled'))
                        topCarat.fadeTo(0, .66);
                    if (!bottomCarat.hasClass('ui-toggled'))
                        bottomCarat.fadeTo(0, .33);
                }
            }

            if (_self.cfg.clickableHeaderSorting)
                if ($this.closest('th').find('> hr').size() == 0)
                    $this.closest('th').addClass('ui-state-hover');
                else
                    $this.closest('div.ui-sortable-column').addClass('ui-state-hover');
        })
        .unbind('mouseleave').bind('mouseleave',function (event) {
            var $this = ice.ace.jq(this),
                topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]);

            if (!bottomCarat.hasClass('ui-toggled'))
                if (topCarat.hasClass('ui-toggled') & _self.cfg.clickableHeaderSorting)
                    bottomCarat.fadeTo(0, 0);
                else bottomCarat.fadeTo(0, .33);

            if (!topCarat.hasClass('ui-toggled'))
                if (bottomCarat.hasClass('ui-toggled') & _self.cfg.clickableHeaderSorting)
                    topCarat.fadeTo(0, 0);
                else topCarat.fadeTo(0, .33);

            if ($this.closest('th').find('> hr').size() == 0)
                $this.closest('th').removeClass('ui-state-hover');
            else
                $this.closest('div.ui-sortable-column').removeClass('ui-state-hover');
        }).each(function () {
            // Prefade sort controls
            var $this = ice.ace.jq(this),
                topCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-n")[0]),
                bottomCarat = ice.ace.jq($this.find("a.ui-icon-triangle-1-s")[0]);
            selectionMade = bottomCarat.hasClass('ui-toggled') || topCarat.hasClass('ui-toggled');

            if (_self.cfg.clickableHeaderSorting && selectionMade) {
                if (!topCarat.hasClass('ui-toggled')) topCarat.fadeTo(0, 0);
                else bottomCarat.fadeTo(0, 0);
            } else {
                if (!topCarat.hasClass('ui-toggled')) topCarat.fadeTo(0, .33);
                if (!bottomCarat.hasClass('ui-toggled')) bottomCarat.fadeTo(0, .33);
            }
        });

    // Bind keypress kb-navigable sort icons
    ice.ace.jq(this.jqId)
        .off('keypress', this.sortUpSelector)
        .on('keypress', this.sortUpSelector, function (event) {
            if (event.which == 32 || event.which == 13) {
                var $currentTarget = ice.ace.jq(event.currentTarget);
                $currentTarget.closest('.ui-sortable-control')
                    .trigger('click', [$currentTarget.offset().top, event.metaKey]);
                return false;
            }
        });

    ice.ace.jq(this.jqId)
        .off('keypress', this.sortDownSelector)
        .on('keypress', this.sortDownSelector, function (event) {
            if (event.which == 32 || event.which == 13) {
                var $currentTarget = ice.ace.jq(event.currentTarget);
                $currentTarget.closest('.ui-sortable-control')
                    .trigger('click', [$currentTarget.offset().top + 6, event.metaKey]);
                return false;
            }
        });
}

ice.ace.DataTable.prototype.setupClickEvents = function() {
     function setupCellClick(obsList, options) {
        if (obsList.length == 0) return;

        var execObsList = obsList.reduce(function(preObs, curObs) {
                return function(event) {
                    if (preObs) preObs(event);
                    curObs(event);
                }
            }),
            self = this,
            timeout = options.noDbl ? 0 : 350

        this.element.on('click', this.cellSelector, function (event) {
            if (self.blockCellClick == true) return;

            var target = ice.ace.jq(event.target);

            if (options.allColumnClicks || target.is('td,span,div')) {
                // wait for dblclick
                self.cellClickWaiting = setTimeout(function() {
                    execObsList.call(self, event);
                    // console.log('cell click');
                }, timeout);

                // cancel further clicks while waiting for behaviour to eval
                self.blockCellClick = true;

                // seperate timeout to enable further clicks to occur
                // above timeout (click behaviour) may be cancelled
                setTimeout(function() {
                    self.blockCellClick = false;
                }, timeout);
            }
        });
    };

    function setupRowClick(obsList, options) {
        if (obsList.length == 0) return;

        var execObsList = obsList.reduce(function(preObs, curObs) {
                return function(event) {
                    if (preObs) preObs(event);
                    curObs(event);
                }
            }),
            self = this,
            timeout = options.noDbl ? 0 : 350;

        this.element.on('click', this.rowSelector, function (event) {
            if (self.blockRowClick == true) return;

            var target = ice.ace.jq(event.target);

            if (options.allColumnClicks || target.is('td,span,div')) {
                self.rowClickWaiting = setTimeout(function() {
                    execObsList.call(self, event);
                    // console.log('row click');
                }, timeout);

                self.blockRowClick = true;

                // seperate timeout - first timeout behaviour may be cancelled
                setTimeout(function() {
                    self.blockRowClick = false;
                }, timeout);
            }
        });
    };

    function setupCellDoubleClick(obsList, options) {
        if (obsList.length == 0) return;

        var execObsList = obsList.reduce(function(preObs, curObs) {
                return function(event) {
                    if (preObs) preObs(event);
                    curObs(event);
                }
            });

        var self = this;
        this.element.on('dblclick', this.cellSelector, function (event) {
            var target = ice.ace.jq(event.target);

            if (options.allColumnClicks || target.is('td,span,div')) {
                if (self.rowClickWaiting) clearTimeout(self.rowClickWaiting);
                if (self.cellClickWaiting) clearTimeout(self.cellClickWaiting);

                execObsList.call(self,event);

                // console.log('cell double click');
            }
        });
    };

    function setupRowDoubleClick(obsList, options) {
        if (obsList.length == 0) return;

        var execObsList = obsList.reduce(function(preObs, curObs) {
                return function(event) {
                    if (preObs) preObs(event);
                    curObs(event);
                }
            });

        var self = this;
        this.element.on('dblclick', this.rowSelector, function (event) {
            var target = ice.ace.jq(event.target);

            if (options.allColumnClicks || target.is('td,span,div')) {
                if (self.rowClickWaiting) clearTimeout(self.rowClickWaiting);
                if (self.cellClickWaiting) clearTimeout(self.cellClickWaiting);

                execObsList.call(self, event);

                // console.log('row double click');
            }
        });
    };

    function doRowSelect (event) {
        var row = ice.ace.jq(event.currentTarget);

        if (!this.isSingleSelection() && event.shiftKey && this.lastSelectIndex > -1)
            this.doMultiRowSelectionEvent(this.lastSelectIndex, row);
        else if (row.hasClass('ui-selected'))
            this.doSelectionEvent('row', true, row);
        else
            this.doSelectionEvent('row', false, row);

        this.lastSelectIndex = row.index();
    }

    function doCellSelect(event) {
        var cell = ice.ace.jq(event.currentTarget);
        if (cell.hasClass('ui-selected'))
            this.doSelectionEvent('cell', true, cell);
        else
            this.doSelectionEvent('cell', false, cell);
    }

    function getRowIndex(e) {
        var index = /_row_([0-9]+)/g.exec(ice.ace.jq(e.target).closest('tr').attr('id'))[1];
        return index;
    }

    function getCellIndex(e) {
        var index = /ui-col-([0-9]+)/g.exec(ice.ace.jq(e.target).closest('td')[0].className)[1];
        return index;
    }


    var self = this,
        cellClickObs = [], rowClickObs = [],
        rowDblClickObs = [], cellDblClickObs = [];

    // Add cell click ace:ajax handler
    if (this.behaviors && this.behaviors.cellClick)
        cellClickObs.push(function(e) {
            var opts = { params : {} };

            opts.params[this.id + '_rowIndex'] = getRowIndex(e),
            opts.params[this.id + '_colIndex'] = getCellIndex(e)

            ice.ace.ab(ice.ace.extendAjaxArgs(self.behaviors.cellClick, opts));
        });

    // Add cell dbl click ace:ajax handler
    if (this.behaviors && this.behaviors.cellDblClick)
        cellDblClickObs.push(function(e) {
            var opts = { params : {} };

            opts.params[this.id + '_rowIndex'] = getRowIndex(e),
            opts.params[this.id + '_colIndex'] = getCellIndex(e)

            ice.ace.ab(ice.ace.extendAjaxArgs(self.behaviors.cellDblClick,opts));
        });

    // Add selection listeners
    if (this.isSelectionEnabled()) {
        if (this.isCellSelectionEnabled()) {
            if (this.cfg.dblclickSelect)
                cellDblClickObs.push(function(event) { doCellSelect.call(self, event); });
            else
                cellClickObs.push(function(event) { doCellSelect.call(self, event); })
        }
        else {
            if (this.cfg.dblclickSelect)
                rowDblClickObs.push(function(event) { doRowSelect.call(self, event); });
            else
                rowClickObs.push(function(event) { doRowSelect.call(self, event); });
        }

        this.setupSelectionHover();
    }

    // Initialize listener sets
    var options = {
        noDbl : cellDblClickObs.length == 0 && rowDblClickObs.length == 0,
        allColumnClicks : this.cfg.allColClicks
    };

    setupCellClick.call(this, cellClickObs, options);
    setupRowClick.call(this, rowClickObs, options);

    if (!options.noDbl) {
        setupCellDoubleClick.call(this, cellDblClickObs, options);
        setupRowDoubleClick.call(this, rowDblClickObs, options);
    }
}

ice.ace.DataTable.prototype.setupSelectionHover = function () {
    var _self = this,
        selector = this.isCellSelectionEnabled()
            ? this.cellSelector
            : this.rowSelector,
        hoverSelector = '> tbody.ui-datatable-data > tr.ui-state-hover,  > tbody.ui-datatable-data > tr > td.ui-state-hover';

    this.element.find(this.bodyTableSelector).parent()
        .bind('mouseleave', function () {
            ice.ace.jq(this).find(hoverSelector).removeClass('ui-state-hover');
        })
        .find('thead').bind('mouseenter', function () {
            ice.ace.jq(this).siblings().closest('table').find(hoverSelector).removeClass('ui-state-hover');
        });

    this.element
        .off('mouseenter', selector)
        .on('mouseenter', selector, function (e) {
            var src = ice.ace.jq(e.currentTarget);

            src.siblings().removeClass('ui-state-hover');

            // Skip conditional rows and their cells
            if (src.hasClass('dt-cond-row') || src.parent().hasClass('dt-cond-row'))
                return;

            src.addClass('ui-state-hover');

            if (_self.isCellSelectionEnabled()) {
                src.parent().siblings()
                            .children('.ui-state-hover')
                            .removeClass('ui-state-hover');
            }
        });
}

ice.ace.DataTable.prototype.setupReorderableColumns = function () {
    var _self = this;
    ice.ace.jq(this.jqId + ' > div > table > thead').sortable({
        items:'th.ui-reorderable-col', helper:'clone',
        axis:'x', appendTo:this.jqId + ' thead',
        cursor:'move', placeholder:'ui-state-hover',
        cancel:'.ui-header-right, :input, button, .ui-tableconf-button, .ui-header-text'})
        .bind("sortstart", function (event, ui) {
            _self.reorderStart = ui.item.index();
        })
        .bind("sortstop", function (event, ui) {
            _self.reorderEnd = ui.item.index();
            if (_self.reorderStart != _self.reorderEnd)
                _self.reorderColumns(_self.reorderStart, _self.reorderEnd);
        });
}

ice.ace.DataTable.prototype.setupRowExpansionEvents = function () {
    var table = this;
    var selector = this.rowExpansionSelector;
    ice.ace.jq(this.jqId)
        .off('keyup click', selector)
        .on('keyup', selector, function (event) {
            if (event.which == 32 || event.which == 13) {
                table.toggleExpansion(this);
            }
        })
        .on('click', selector, function (event) {
            event.stopPropagation();
            table.toggleExpansion(this);
        });
}

ice.ace.DataTable.prototype.setupPanelExpansionEvents = function () {
    var table = this;
    var selector = this.panelExpansionSelector;
    ice.ace.jq(this.jqId)
        .off('keyup click', selector)
        .on('keyup', selector, function (event) {
            if (event.which == 32 || event.which == 13) {
                table.toggleExpansion(this);
            }
        })
        .on('click', selector, function (event) {
            event.stopPropagation();
            table.toggleExpansion(this);
        });
}

ice.ace.DataTable.prototype.setupScrolling = function () {
    var startTime = new Date().getTime(),
        _self = this,
        delayedCleanUpResizeToken,
        delayedCleanUpResize = function () {
            _self.resizeScrolling();
        };

    this.resizeScrolling();

    // Persist scrolling position if one has been loaded from previous instance
    var scrollBody = this.element.find(this.scrollBodySelector);
    if (this.scrollTop) scrollBody.scrollTop(this.scrollTop);
    if (this.scrollLeft) scrollBody.scrollLeft(this.scrollLeft);

    scrollBody.bind('scroll', function () {
        var $this = ice.ace.jq(this),
            $header = ice.ace.jq(_self.jqId + ' > div.ui-datatable-scrollable-header'),
            $footer = ice.ace.jq(_self.jqId + ' > div.ui-datatable-scrollable-footer'),
            scrollLeftVal = $this.scrollLeft(),
            scrollTopVal = $this.scrollTop();

        if (ice.ace.jq.browser.mozilla) {
            if (scrollLeftVal == 0) {
                $header.scrollLeft(-1);
                $footer.scrollLeft(-1);
            } else if (scrollLeftVal == (this.scrollWidth - this.clientWidth)){
                $header.scrollLeft(scrollLeftVal + 1);
                $footer.scrollLeft(scrollLeftVal + 1);
            }

            $header.scrollLeft(scrollLeftVal);
            $footer.scrollLeft(scrollLeftVal);
        } else {
            $header.scrollLeft(scrollLeftVal);
            $footer.scrollLeft(scrollLeftVal);
        }

        _self.scrollLeft = scrollLeftVal;
        _self.scrollTop = scrollTopVal;
    });

    this.scrollableResizeCallback = function () {
        var parentWidth = ice.ace.jq(_self.jqId).parent().width();
        if (_self.parentSize != parentWidth) {
            _self.parentSize = parentWidth;
            clearTimeout(delayedCleanUpResizeToken);
            delayedCleanUpResizeToken = setTimeout(delayedCleanUpResize, 100);
        }
    }

    ice.ace.jq(window).bind('resize', this.scrollableResizeCallback);

    //live scroll
    if (this.cfg.liveScroll) {
        var bodyContainer = ice.ace.jq(this.jqId + ' > div > table > tbody[display!=none]');
        this.scrollOffset = this.cfg.scrollStep;
        this.shouldLiveScroll = true;

        bodyContainer.scroll(function () {
            if (_self.shouldLiveScroll) {
                var $this = ice.ace.jq(this);
                var sTop = $this.scrollTop(), sHeight = this.scrollHeight, viewHeight = $this.height();
                if (sTop >= (sHeight - viewHeight)) _self.loadLiveRows();
            }
        });
    }

    if (window.console && this.cfg.devMode) {
        console.log("ace:dataTable - ID: " + this.id + " - setupScrolling - " + (new Date().getTime() - startTime)/1000 + "s");
    }
}

ice.ace.DataTable.prototype.setupResizableColumns = function () {
    //Add resizers
    ice.ace.jq(this.jqId + ' > div > table > thead th:not(th:last)')
            .children('.ui-header-column')
            .append('<div class="ui-column-resizer"></div>');

    //Setup resizing
    this.columnWidthsCookie = this.id + '_columnWidths',
        resizers = ice.ace.jq(this.jqId + ' > div > table > thead > tr > th > div > div.ui-column-resizer'),
        columns = ice.ace.jq(this.jqId + ' > div > table > thead > tr > th'),
        _self = this;

    resizers.draggable({
        axis:'x',
        drag:function (event, ui) {
            var column = ui.helper.closest('th'),
                newWidth = ui.position.left + ui.helper.outerWidth();

            column.css('width', newWidth);
        },
        stop:function (event, ui) {
            ui.helper.css('left', '');

            var columnWidths = [];

            columns.each(function (i, item) {
                var columnHeader = ice.ace.jq(item);
                columnWidths.push(columnHeader.css('width'));
            });
            ice.ace.jq.cookie(_self.columnWidthsCookie, columnWidths.join(','));
        }
    });

    //restore widths on postback
    var widths = ice.ace.jq.cookie(this.columnWidthsCookie);
    if (widths) {
        widths = widths.split(',');
        for (var i = 0; i < widths.length; i++) {
            ice.ace.jq(columns.get(i)).css('width', widths[i]);
        }
    }
}

ice.ace.DataTable.prototype.sizingHasWaited = false;
ice.ace.DataTable.prototype.resizeScrolling = function () {
    var startTime = new Date().getTime(),
        scrollableTable = this.element,
        _self = this;

    // Reattempt resize in 100ms if I or a parent of mine is currently hidden.
    // Sizing will not be accurate if the table is not being displayed, like at tabset load.
    if (!(this.cfg.nohidden) && ((ie7 && scrollableTable.width() == 0) || (!ie7 && scrollableTable.is(':hidden'))) && !this.cfg.disableHiddenSizing) {
        setTimeout(function () {
            _self.sizingHasWaited = true;
            _self.resizeScrolling()
        }, 100);
    }
    else if (!_self.sizingHasWaited) {
        setTimeout(function () {
            _self.sizingHasWaited = true;
            _self.resizeScrolling()
        }, 0);
    }
    else {
        var resizableTableParents = scrollableTable.parents('.ui-datatable-scrollable');

        // If our parents are resizeable tables, allow them to resize before I resize myself
        if (resizableTableParents.size() > 0) {
            if (!this.parentResizeDelaySet) {
                this.parentResizeDelaySet = true;
                var _self = this;
                setTimeout(function () {
                    _self.resizeScrolling()
                }, resizableTableParents.size() * 5);
                return;
            }
        }

        var safari = ice.ace.jq.browser.safari,
            chrome = ice.ace.jq.browser.chrome,
            mac = ice.ace.jq.browser.os == 'mac',
            ie7 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 7,
            ie8 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 8,
            ie9 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 9,
			ie10 = ice.ace.jq.browser.msie && !ie7 && !ie8 && !ie9,
            ie8as7 = this.cfg.scrollIE8Like7 && ie8,
            firefox = ice.ace.jq.browser.mozilla;

        if (ie8as7) {
            ie7 = true;
            ie8 = false;
        }

        var headerTable, footerTable, bodyTable, dupeHead, dupeFoot, bodyFirstConditional = false, bodyTableParent;

        var initializeVar = function() {
            headerTable = scrollableTable.find(' > div.ui-datatable-scrollable-header > table');
            footerTable = scrollableTable.find(' > div.ui-datatable-scrollable-footer > table');
            bodyTable = scrollableTable.find(' > div.ui-datatable-scrollable-body > table');
            bodyTableParent = bodyTable.parent().css('overflow', '');
            dupeHead = bodyTable.find(' > thead');
            dupeFoot = bodyTable.find(' > tfoot');
            bodyFirstConditional = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body:first > table > tbody > tr:visible:first').is('.dt-cond-row');
        }
        initializeVar();

		if (this.styleSheets) { // remove previous column size rules
			for (var i = 0; i < this.styleSheets.length; i++) {
				var sheet = this.styleSheets[i];
				if (ie8 || ie9) {
					sheet.cssText = "";
				} else {
					sheet.parentNode.removeChild(sheet);
				}
			}
			while (this.styleSheets.length > 0) {
				this.styleSheets.pop();
			}
		}

        if (!ie7) {
            var dupeHeadCols = dupeHead.find('th > div.ui-header-column').get().reverse();
            var dupeFootCols = dupeFoot.find('td > div.ui-footer-column').get().reverse();

            var realHeadCols = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-header:first > table > thead > tr > th > .ui-header-column').get().reverse();
            var realFootCols = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-footer:first > table > tfoot > tr > td > .ui-footer-column').get().reverse();
            var bodySingleCols = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body:first > table > tbody > tr:visible:not(.dt-cond-row):first > td > div').get().reverse();
            var bodySingleColsTds = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body:first > table > tbody > tr:visible:not(.dt-cond-row):first > td').get().reverse();
        }

        var resetScrollingOverflow = function() {
            // Reset overflow if it was disabled as a hack from previous sizing
            headerTable.parent().css('overflow', '');
            footerTable.parent().css('overflow', '');
            headerTable.css('width', '');
            footerTable.css('width', '');
        }
        resetScrollingOverflow();

        if (!ie7) {
            // Reset fixed sizing if set by previous sizing.
            for (var i = 0; i < bodySingleCols.length; i++)
                ice.ace.jq(bodySingleCols[i]).css('width', '');
				ice.ace.jq(bodySingleColsTds[i]).css('width', '');
        }

        // Reset padding if added to offset scrollbar issues
        bodyTableParent.css('padding-right', '');

        if (!ie7) {
            var unsizedVScrollShown = bodyTableParent.is(':scrollable(vertical)'),
                unsizedBodyVScrollShown = ice.ace.jq('html').is(':scrollable(vertical)');
        }

        // Get Duplicate Header/Footer Sizing
        var dupeHeadColumn, dupeFootColumn, dupeHeadColumnWidths = [], bodySingleColWidths = [],
            dupeFootColumnWidths = [], realHeadColumn, realFootColumn, bodyColumn, dupeCausesScrollChange = false,
            dupeCausesBodyScrollChange = false;

        if (!ie7) {
            // If duplicate header/footer row causes body table to barely
            // exceed min-table size (causing scrollbar)

            var vScrollShown = bodyTable.parent().is(':scrollable(vertical)'),
                bodyVScrollShown = ice.ace.jq('html').is(':scrollable(vertical)');

            if (!unsizedVScrollShown && vScrollShown)
                dupeCausesScrollChange = true;

            if (!unsizedBodyVScrollShown && bodyVScrollShown)
                dupeCausesBodyScrollChange = true;

            // Change table rendering algorithm to get more accurate sizing
            bodyTable.css('table-layout', 'auto');
            // Show Duplicate Header / Footer
            dupeHead.css('display', 'table-header-group');
            dupeFoot.css('display', 'table-footer-group');
        }

        var ie7ScrollbarFix = function() {
            if (bodyTable.size() > 0 && bodyTable.parent().is(':scrollable(vertical)')) {
                bodyTable.parent().css('overflow-x', 'hidden');
                bodyTable.parent().css('padding-right', '17px');
                headerTable.parent().css('padding-right', '17px');
                footerTable.parent().css('padding-right', '17px');
            }
        }
        if (ie7) ie7ScrollbarFix();

        // Get Duplicate Sizing
        if (!ie7) {
            // Return overflow to visible so sizing doesn't have scrollbar errors
            if (dupeCausesScrollChange) {
                bodyTable.parent().css('overflow', 'visible');
            }
            if (dupeCausesBodyScrollChange) {
                ice.ace.jq('html').css('overflow', 'hidden');
            }

            for (var i = 0; i < bodySingleCols.length; i++) {
                bodyColumn = ice.ace.jq(bodySingleCols[i]);
                bodySingleColWidths[i] = bodyColumn.width();
            }

            // Return overflow value
            if (dupeCausesScrollChange) {
                bodyTable.parent().css('overflow', '');
            }
            if (dupeCausesBodyScrollChange) {
                ice.ace.jq('html').css('overflow', '');
            }
        }

        var tableLayout = function() {
            headerTable.css('table-layout', 'fixed');
            bodyTable.css('table-layout', 'fixed');
            footerTable.css('table-layout', 'fixed');
        }
        if (ie7) tableLayout();

        // Set Duplicate Sizing
        var cssid = firefox ? 'global' : this.jqId.substr(1)+'_colSizes';

        if (!ie7) {
            // must use global stylesheet as dynamic rule doesn't work reliably in FF
            // when using different stylesheets per table, rules added to anything but the first sheet are not applied in FF
            // though isolated test of this works, the rules from the multiple stylesheets are applied
            var styleSheet = ice.ace.util.getStyleSheet(cssid) || ice.ace.util.addStyleSheet(cssid);
			var text = '';

            for (var i = 0; i < bodySingleCols.length; i++) {
                bodyColumn = ice.ace.jq(bodySingleCols[i]);

                // Work around webkit bug described here: https://bugs.webkit.org/show_bug.cgi?id=13339
                var bodyColumnWidth = (safari && ice.ace.jq.browser.version < 6)
                    ? bodySingleColWidths[i] + parseInt(bodyColumn.parent().css('padding-right')) + parseInt(bodyColumn.parent().css('padding-left')) + 1
                    : bodySingleColWidths[i];

                // Adjust last column size to stop prevent horizontal scrollbar / align vertical
                if (i == 0) {
                    if (ie9) bodyColumnWidth = bodySingleColWidths[i] - 1;
                    else if (firefox && !mac) bodyColumnWidth = bodySingleColWidths[i];
                }

                // Set Duplicate Header Sizing to Body Columns
                // Equiv of max width
                var index = /ui-col-([0-9]+)/g.exec(bodySingleCols[i].parentNode.className)[1],
                    selector =  this.jqId+' .ui-col-'+index+' > div';
				text += selector + ' { width:' + bodyColumnWidth + 'px; }\n';

                // Adjust last column size to stop prevent horizontal scrollbar / align vertical
                if (i == 0) {
                    if (ie9) bodyColumnWidth = bodySingleColWidths[i] - 2;
                    else if (firefox && !mac) bodyColumnWidth = bodySingleColWidths[i];
                    else bodyColumnWidth = bodySingleColWidths[i] - 1;
                }

                // Equiv of min width
                selector =  this.jqId+' .ui-col-'+index;
				text += selector + ' { width:' + bodyColumnWidth + 'px; }\n';
            }

			if (!this.styleSheets) this.styleSheets = [];
			if (ie8 || ie9) {
				styleSheet.cssText = text;
				this.styleSheets.push(styleSheet);
			} else {
				var rulesNode = document.createTextNode(text);
				styleSheet.ownerNode.appendChild(rulesNode);
				this.styleSheets.push(rulesNode);
			}
			
			// Get Duplicate Sizing
			// Return overflow to visible so sizing doesn't have scrollbar errors
			if (dupeCausesScrollChange) {
				bodyTable.parent().css('overflow', 'visible');
			}
			if (dupeCausesBodyScrollChange) {
				ice.ace.jq('html').css('overflow', 'hidden');
			}

			for (var i = 0; i < dupeHeadCols.length; i++) {
				dupeHeadColumn = ice.ace.jq(dupeHeadCols[i]);
				dupeHeadColumnWidths[i] = dupeHeadColumn.width();
			}

			for (var i = 0; i < dupeFootCols.length; i++) {
				dupeFootColumn = ice.ace.jq(dupeFootCols[i]);
				dupeFootColumnWidths[i] = dupeFootColumn.width();
			}

			// Return overflow value
			if (dupeCausesScrollChange) {
				bodyTable.parent().css('overflow', '');
			}
			if (dupeCausesBodyScrollChange) {
				ice.ace.jq('html').css('overflow', '');
			}

            for (var i = 0; i < realHeadCols.length; i++) {
                realHeadColumn = ice.ace.jq(realHeadCols[i]);

                var realHeadColumnWidth = dupeHeadColumnWidths[i];

                if (i == 0) {
                    if (ie9) realHeadColumnWidth = realHeadColumnWidth - 1;
                    else if (firefox && !mac) realHeadColumnWidth = realHeadColumnWidth;
                }

                // Set Duplicate Header Sizing to True Header Columns
                realHeadColumn.width(realHeadColumnWidth);
                // Apply same width to stacked sibling columns
                realHeadColumn.siblings('.ui-header-column').width(realHeadColumnWidth);
                // Equiv of max width
                realHeadColumn.parent().width(realHeadColumnWidth);
            }

            for (var i = 0; i < realFootCols.length; i++) {
                realFootColumn = ice.ace.jq(realFootCols[i]);

                // Work around webkit bug described here: https://bugs.webkit.org/show_bug.cgi?id=13339
                var realFootColumnWidth = (safari && ice.ace.jq.browser.version < 6)
                    ? dupeFootColumnWidths[i] + parseInt(realFootColumn.parent().css('padding-right')) + parseInt(realFootColumn.parent().css('padding-left'))
                    : dupeFootColumnWidths[i];

                if (i == 0) {
                    if (ie9) realFootColumnWidth = realFootColumnWidth - 1;
                    else if (firefox && !mac) realFootColumnWidth = realFootColumnWidth;
                }

                // Set Duplicate Header Sizing to True Header Columns
                realFootColumn.parent().width(realFootColumnWidth);
                realFootColumn.width(realFootColumnWidth);
                // Apply same width to stacked sibling columns
                realFootColumn.siblings('.ui-footer-column').width(realFootColumnWidth);
            }
        }

        // Browser / Platform specific scrollbar fixes
        // Fix body scrollbar overlapping content
        // Instance check to prevent IE7 dynamic scrolling change errors
        // Recheck scrollable, it may have changed again post resize
        /*if (ie9 && bodyTable.parent().is(':scrollable(horizontal)')) {
            bodyTable.css('table-layout','fixed');
			for (var i = 0; i < bodySingleCols.length; i++) {
				bodySingleCols[i].parentNode.style.width = '';
			}
        }*/


        if (!ie7 && vScrollShown && bodyTable.parent().is(':scrollable(vertical)')) {
            if (((firefox) || ((safari || chrome) && !mac) || (ie9 || ie8)) && !dupeCausesScrollChange) {
                var offset = ice.ace.jq.getScrollWidth();

                if (ie8) bodyTableParent.css('padding-right', '1px');

                headerTable.parent().css('margin-right', offset + 'px');
                footerTable.parent().css('margin-right', offset + 'px');
            }
            else if (dupeCausesScrollChange) {
                /* Correct scrollbars added when unnecessary. */
                if (safari || chrome || firefox || ie9) {
                    bodyTable.parent().css('overflow', 'visible');
                    headerTable.parent().css('overflow', 'visible');
                    footerTable.parent().css('overflow', 'visible');
                    headerTable.css('width', '100%');
                    headerTable.css('table-layout', '');
                    footerTable.css('width', '100%');
                    footerTable.css('table-layout', '');
                }

                /* Clean up IE 8/9 sizing bug in dupe scroll change case */
                headerTable.parent().css('margin-right', '');
                footerTable.parent().css('margin-right', '');
                headerTable.find('tr th:last').css('padding-right', '');
                footerTable.find('tr td:last').css('padding-right', '');
            }
        } else {
            headerTable.css('width','100%').parent().css('margin-right', '');
            footerTable.css('width','100%').parent().css('margin-right', '');
            headerTable.find('tr th:last').css('padding-right', '');
            footerTable.find('tr td:last').css('padding-right', '');
        }

        // If the body of the table starts with a conditonal row, duplicate the first non
        // conditional row and insert it before the conditionals with styling to hide it,
        // while still keeping it in flow to set the table sizing.
        var firstRowConditionalHandling = function() {
            var firstNonCond = ice.ace.jq(this.jqId + ' .ui-datatable-scrollable-body:first > table > tbody > tr:not(.dt-cond-row):first');

            firstNonCond.clone().attr('id', this.id + '_condAlgnr_'+Math.floor((Math.random()*100)+1))
                .css('visibility', 'hidden').prependTo(bodyTable.find('> tbody'))
                .find('td').attr('rowspan', 1);

            bodyTable.css('margin-top',0 - firstNonCond.height());
        }
        if (bodyFirstConditional & !ie7)
            firstRowConditionalHandling();

        // Hide Duplicate Segments
        dupeHead.css('display', 'none');
        dupeFoot.css('display', 'none');

        if (ie7 && this.cfg.ie7Width) {
            this.cfg.ie7Width = parseInt(this.cfg.ie7Width);
            var cellWidth = bodyTable.find('> tbody > tr:first-child > td:first-child').width();

            footerTable.parent().add(headerTable.parent()).css('width', this.cfg.ie7Width + 'px');
            bodyTable.parent().css('width', this.cfg.ie7Width + 'px').css('position','static');
            bodyTable.parent().parent().css('overflow-x','scroll');
        }

        if (window.console && this.cfg.devMode) {
            console.log("ace:dataTable - ID: " + this.id + " - resizeScrolling - " + (new Date().getTime() - startTime)/1000 + "s");
        }

        if (this.cfg.pinning) {
            this.pinningHolder = this.jqId + '_pinning';
            this.initializePinningState();
        }
    }
}

ice.ace.DataTable.prototype.initializePinningState = function() {
    this.readPinningState();
    var table = this;
    var pinOrder = [];

    ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body > table > tbody > tr:first-child > td')
            .each(function(i,e) {
                if (table.columnPinOrder[i] != undefined) {
                    pinOrder[table.columnPinOrder[i]] = i;
                }
            });

    for (var i = 0; i < pinOrder.length; i++)
        if (pinOrder[i] == 0 || pinOrder[i]) // explcitly catch 0 case
            this.pinColumn(pinOrder[i] + 1, true);
};

ice.ace.DataTable.prototype.repairPinnedColumn = function(i) {
    var tbody = arguments[1] ? arguments[1]
                    : ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body > table'),
        bodyCells = arguments[2] ? arguments[2]
                : tbody.find(' > tbody > tr > td:nth-child('+i+')'),

        headCells = arguments[3] ? arguments[3]
                : ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-header > table > thead > tr > th:nth-child('+i+')'),

        footCells = arguments[4] ? arguments[4]
                : ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-footer > table > tfoot > tr > td:nth-child('+i+')'),

        cellWidth = bodyCells.eq(0).width(),
        offset = this.columnPinPosition[i],
        ie7 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 7,
        ie8 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 8,
        ie9 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 9,
        firefox = ice.ace.jq.browser.mozilla;

    if (ie8 || ie9) {
        bodyCells.first().css('border-top','0px');
        bodyCells.css('top', '').css('height','').css('position','relative')
                .each(function(i,e) {
                    var topVal = e.offsetTop;
                    if (((i + 1)%3 == 0) || ((i + 2)%3 == 0))
                        topVal = topVal - 1;
                    ice.ace.jq(e).css('top', topVal);
                });
    }

    bodyCells.css('position','absolute')
            .css('left', offset)
            .css('width', cellWidth)
            .css('border-bottom','0px')
            .css('border-left','1px solid') // correct previously removed border if removed due to pinning corrections
            .addClass('pinned')
            .addClass('ui-widget-content')
            .find('> div').css('width', cellWidth).end()
            .first().css('border-top','0px').addClass('pinned');

    headCells.add(footCells).css('left', offset);

    if (firefox)
        bodyCells.css('margin-top','-1px');

    var scrollTopVal = tbody.parent().scrollTop();
    bodyCells.each(function(i,e) {
        e = ice.ace.jq(e);
        var sibling = e.nextAll(':not(.pinned)').first();

        if (sibling.length == 0)
            sibling = e.prevAll(':not(.pinned)').first();

        if (!firefox)
            e.css('margin-top', 0-scrollTopVal);

        if (e.parent().is(':last-child'))
            e.css('height', sibling.height() + ice.ace.jq.getScrollWidth());
        else
            e.css('height', sibling.height() + 1);

        e.css('border-color', sibling.css('border-right-color'));
    });

    var nextUnpinnedIndex = bodyCells.first().next('td:not(.pinned)').index();

    if (nextUnpinnedIndex >= 0) {
        nextUnpinnedIndex = nextUnpinnedIndex+1;
        tbody.find(' > tbody > tr > td:nth-child('+nextUnpinnedIndex+')').css('border-left','0px');
    }

    var nonBodyCellWidth = cellWidth + 20;

    headCells.add(footCells).css('width', nonBodyCellWidth)
            .find('> div').css('width', cellWidth).end();

    // Add scrolling
    if (this.columnPinScrollListener[i])
        tbody.parent().unbind('scroll', this.columnPinScrollListener[i]);

    this.columnPinScrollListener[i] = function() {
        if (!firefox) {
            var scrollTopVal = tbody.parent().scrollTop();
            bodyCells.css('margin-top', 0-scrollTopVal);
        } else {
            bodyCells.css('display', 'none');
            setTimeout(function() {
                bodyCells.css('display', '');
            }, 1);
        }
    };

    tbody.parent().bind('scroll', this.columnPinScrollListener[i]);
}

ice.ace.DataTable.prototype.fixPinnedColumnPositions = function(state) {
    var table = this,
        order = state.order,
        offset = state.offset,
        columns = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body > table > tbody > tr:first-child > td');

    columns.each(function(i) {
        if (table.columnPinOrder[i] > order) {
            table.columnPinOrder[i] = table.columnPinOrder[i] - 1;
            table.columnPinPosition[i + 1] = table.columnPinPosition[i + 1] - offset;
            table.repairPinnedColumn(i + 1);
        }
    });
}

ice.ace.DataTable.prototype.pinThisColumn = function(event) {
    var target = (event && event.target) ? event.target : window.event.srcElement,
        cell = ice.ace.jq(target).closest('th,td'),
        ie7 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 7;

    if (cell.hasClass('pinned')) {
        if (ie7) this.ie7UnpinColumn(cell.index() + 1);
        else this.unpinColumn(cell.index() + 1);
    }
    else {
        if (ie7) this.ie7PinColumn(cell.index() + 1);
        else this.pinColumn(cell.index() + 1);
    }
}

ice.ace.DataTable.prototype.ie7UnpinColumn = function(i) {
    var safari = ice.ace.jq.browser.safari,
        chrome = ice.ace.jq.browser.chrome,
        ie8 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 8,
        ie9 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 9,
        firefox = ice.ace.jq.browser.mozilla;

    var tbody = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body > table'),
        thead = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-header > table'),
        tfoot = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-footer > table');

    // Handle non static header cases
    if (tbody.length == 0) tbody = ice.ace.jq(this.jqId + ' > div > table');

    var bodyCells = tbody.find('tbody:first > tr > td:nth-child('+i+')'),
        headCells = thead.find('thead:first > tr > th:nth-child('+i+')'),
        footCells = tfoot.find('tfoot:first > tr > td:nth-child('+i+')'),
        offsetWidth = headCells.first().width(),
        bodyContainer = tbody.parent();

    if (safari || chrome) offsetWidth = offsetWidth + 1;

    if (this.columnPinScrollListener[i - 1])
        tbody.parent().unbind('scroll', this.columnPinScrollListener[i - 1]);

    // Add new column to pinning state
    var oldState = { order : this.columnPinOrder[i - 1], offset : bodyCells.eq(i-1).outerWidth()};
    this.columnPinOrder[i - 1] = undefined;
    this.columnPinPosition[i] = undefined;
    this.columnPinScrollListener[i - 1] = undefined;
    this.writePinningState();

    this.fixPinnedColumnPositions(oldState);

    bodyCells.add(footCells).add(headCells).css('border','1px solid').css('position','')
            .css('height','').css('top','').removeClass('pinned');

    this.currentPinRegionOffset = this.currentPinRegionOffset - offsetWidth;
    tbody.add(tfoot.parent()).add(thead.parent()).css('margin-left', this.currentPinRegionOffset);

    // Send request
    if (this.behaviors && this.behaviors.unpin) {
        var options = { source: this.id }
        ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.unpin, options));
    }
}

ice.ace.DataTable.prototype.unpinColumn = function(i) {
    var safari = ice.ace.jq.browser.safari,
        chrome = ice.ace.jq.browser.chrome,
        ie8 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 8,
        ie9 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 9,
        firefox = ice.ace.jq.browser.mozilla;

    var tbody = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body > table'),
        thead = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-header > table'),
        tfoot = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-footer > table');

    // Handle non static header cases
    if (tbody.length == 0) tbody = ice.ace.jq(this.jqId + ' > div > table');

    var bodyCells = tbody.find(' > tbody > tr > td:nth-child('+i+')'),
        headCells = thead.find(' > thead > tr > th:nth-child('+i+')'),
        footCells = tfoot.find(' > tfoot > tr > td:nth-child('+i+')'),
        offsetWidth = headCells.first().width(),
        bodyContainer = tbody.parent();

    if (this.columnPinScrollListener[i])
        tbody.parent().unbind('scroll', this.columnPinScrollListener[i]);

    bodyCells.add(footCells).add(headCells).css('position','').css('height','')
            .css('top','').css('left','').removeClass('pinned ui-widget-content')

    if (safari || chrome) offsetWidth = offsetWidth + 1;

    // Remove col from pinning state
    var oldState = { order : this.columnPinOrder[i - 1], offset : offsetWidth};
    this.columnPinOrder[i - 1] = undefined;
    this.columnPinPosition[i] = undefined;
    this.columnPinScrollListener[i] = undefined;
    this.writePinningState();

    this.fixPinnedColumnPositions(oldState);

    // Remove border if unpinned not the left most column
    if (i != 1) {
        bodyCells.add(footCells).add(headCells).css('border-left-width','0px');
    }

    this.currentPinRegionOffset = this.currentPinRegionOffset - offsetWidth;

    bodyContainer.add(tfoot.parent()).add(thead.parent()).css('margin-left',
            this.currentPinRegionOffset > 0 ? this.currentPinRegionOffset : 0);

    // Send request
    if (this.behaviors && this.behaviors.unpin) {
        var options = {
            source: this.id
        }
        ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.unpin, options));
    }
}

ice.ace.DataTable.prototype.pinColumn = function(i) {
    var safari = ice.ace.jq.browser.safari,
        chrome = ice.ace.jq.browser.chrome,
        ie7 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 7,
        ie8 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 8,
        ie9 = ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 9,
        firefox = ice.ace.jq.browser.mozilla,
        isInit = arguments[1];

    if (ie7) return this.ie7PinColumn(i, isInit);

    var tbody = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body > table'),
        thead = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-header > table'),
        tfoot = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-footer > table');

    // Handle non static header cases
    if (tbody.length == 0) tbody = ice.ace.jq(this.jqId + ' > div > table');

    // Set table as position bounds and hide overflowing pinned cols
    tbody.parent().parent().css('position', 'relative').css('overflow','hidden');

    var bodyCells = tbody.find(' > tbody > tr > td:nth-child('+i+')'),
        headCells = thead.find(' > thead > tr > th:nth-child('+i+')'),
        footCells = tfoot.find(' > tfoot > tr > td:nth-child('+i+')'),
        cellWidth = bodyCells.eq(0).width();

    if (safari || chrome) cellWidth++;

    // Exit if already pinned
    if (bodyCells.first().is('.pinned'))
        return this.repairPinnedColumn(i, tbody, bodyCells, headCells, footCells);

    // Add new column to pinning state
    this.columnPinOrder[i - 1] = this.getNextPinnedIndex();
    this.writePinningState();

    // Raise head cell z-index to prevent overlap in IE & FF
    headCells.css('z-index', '1');

    if (ie8 || ie9) {
        headCells.css('margin-top','-1px').height(headCells.eq(0).height() + 'px');
        bodyCells.first().css('border-top','0px');
        bodyCells.css('position','relative')
                .each(function(i,e) {
                    ice.ace.jq(e).css('top', e.offsetTop);
                });
    }

    // Reposition cells
    this.columnPinPosition[i] = this.currentPinRegionOffset;

    bodyCells.css('position','absolute')
            .css('left', this.columnPinPosition[i])
            .css('width', cellWidth)
            .css('border-bottom','0px solid')
            .css('border-left','1px solid') // correct previously removed border if removed due to pinning corrections
            .addClass('pinned')
            .addClass('ui-widget-content')
            .find('> div').css('width', cellWidth).end()
            .first().css('border-top','0px solid');

    var scrollTopVal = tbody.parent().scrollTop(),
            borderRightColor;
    bodyCells.each(function(i,e) {
        e = ice.ace.jq(e);
        var sibling = e.nextAll(':not(.pinned)').first();

        if (sibling.length == 0)
            sibling = e.prevAll(':not(.pinned)').first();

        if (!firefox)
            e.css('margin-top', 0-scrollTopVal);

        if (!borderRightColor) borderRightColor = sibling.css('border-right-color');
        e.css('border-color', borderRightColor);

        var siblingHeight = sibling.height(),
            ownHeight = e.height();

        if (siblingHeight < ownHeight) {
            if (e.parent().is(':last-child'))
                e.css('height', ownHeight + ice.ace.jq.getScrollWidth());
            else
                e.siblings().css('height', ownHeight);
        } else {
            if (e.parent().is(':last-child'))
                e.css('height', siblingHeight + ice.ace.jq.getScrollWidth());
            else if (safari || chrome)
                e.css('height', siblingHeight + 1);
            else
                e.css('height', siblingHeight);
        }
    });

    headCells.each(function(i,e) {
        e = ice.ace.jq(e);
        var sibling = e.nextAll(':not(.pinned)').first();

        if (sibling.length == 0) sibling = e.prevAll(':not(.pinned)').first();

        if (safari || chrome)
            e.css('height', sibling.height() + 1);
        else
            e.css('height', sibling.height());
    });

    if (firefox) {
        bodyCells.css('display', 'none');
        setTimeout(function() {
            bodyCells.css('display', '');
        }, 1);
    }

    var nextUnpinnedIndex = bodyCells.first().next('td:not(.pinned)').index();

    if (nextUnpinnedIndex >= 0) {
        nextUnpinnedIndex = nextUnpinnedIndex+1;
        tbody.find(' > tbody > tr > td:nth-child('+nextUnpinnedIndex+')').css('border-left','0px');
        thead.find(' > thead > tr > th:nth-child('+nextUnpinnedIndex+')').css('border-left','0px');
        tfoot.find(' > tfoot > tr > td:nth-child('+nextUnpinnedIndex+')').css('border-left','0px');
    }

    var nonBodyCellWidth = cellWidth + 20;

    headCells.add(footCells).css('position','absolute')
            .css('border-left','1px solid') // correct previously removed border if removed due to pinning corrections
            .css('border-color','inherit').css('border-color','') // fix webkit border color error
            .css('left',this.columnPinPosition[i])
            .css('width', nonBodyCellWidth)
            .addClass('pinned');

    if (firefox)
        bodyCells.add(headCells).add(footCells).css('margin-top','-1px');

    tbody.find('> tbody').css('border-left','0px');

    // Add table offset
    this.currentPinRegionOffset = cellWidth + 21 + this.currentPinRegionOffset;
    tbody.add(thead.add(tfoot)).parent().css('margin-left', this.currentPinRegionOffset);

    // Add scrolling
    if (this.columnPinScrollListener[i])
        tbody.parent().unbind('scroll', this.columnPinScrollListener[i]);

    this.columnPinScrollListener[i] = function() {
        if (!firefox) {
            var scrollTopVal = tbody.parent().scrollTop();
            bodyCells.css('margin-top', 0-scrollTopVal);
        } else {
            bodyCells.css('display', 'none');
            setTimeout(function() {
                bodyCells.css('display', '');
            }, 1);
        }
    };

    tbody.parent().bind('scroll', this.columnPinScrollListener[i]);

    // Send request
    if (!isInit && this.behaviors && this.behaviors.pin) {
        var options = {
            source: this.id
        }

        ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.pin, options));
    }
}

ice.ace.DataTable.prototype.ie7PinColumn = function(i) {
    var tbody = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body > table'),
        thead = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-header > table'),
        tfoot = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-footer > table'),
        isInit = arguments[1];

    // Handle non static header cases
    if (tbody.length == 0) tbody = ice.ace.jq(this.jqId + ' > div > table');

    // Set table as position bounds and hide overflowing pinned cols
    tbody.parent().parent().parent().css('position', 'relative').css('overflow-y','hidden');

    var bodyCells = tbody.find('tbody:first > tr > td:not:nth-child('+i+')'),
        headCells = thead.find('thead:first > tr > th:not:nth-child('+i+')'),
        footCells = tfoot.find('tfoot:first > tr > td:not:nth-child('+i+')'),
        cellWidth = bodyCells.eq(0).width();

    // Add new column to pinning state
    this.columnPinOrder[i - 1] = this.getNextPinnedIndex();
    this.writePinningState();

    this.columnPinPosition[i] = this.currentPinRegionOffset;
    var cellOffsets = [];
    bodyCells
        .css('position','relative')
        .each(function(i,e) {
            cellOffsets[i] = i > 0 ? e.offsetTop - (i - 1) : e.offsetTop;
        })
        .css('position','absolute')
        .css('left',this.currentPinRegionOffset)
        .css('border-top','none')
        .each(function(i,e) {
            e = ice.ace.jq(e);
            var sibling = e.nextAll(':not(.pinned)').first();

            if (sibling.length == 0)
                sibling = e.prevAll(':not(.pinned)').first();

            var siblingHeight = sibling.outerHeight()
                - parseInt(sibling.css('padding-top'))
                - parseInt(sibling.css('padding-bottom')) - 1;
            e.css('height', siblingHeight);

//            e.clone()
//                .prependTo(ice.ace.jq(e).parent())
//                .addClass('cloned')
//                .css('position','')
//                .css('visibility','hidden')
//            .end()
            e.css('width', cellWidth)
            .css('top', cellOffsets[i])
            .addClass('pinned');
        });

    headCells.add(footCells)
        .css('width', cellWidth + 20)
        .css('position','absolute')
        .addClass('pinned')
        .css('left',this.currentPinRegionOffset);

    this.currentPinRegionOffset = cellWidth + 20 + this.currentPinRegionOffset;

    footCells.each(function (i,e) {
        e = ice.ace.jq(e);
        e.css('height', e.height() + ice.ace.jq.getScrollWidth());
    });

    // Add table offset
    thead.add(tfoot).parent().css('margin-left', this.currentPinRegionOffset);
    tbody.css('margin-left', this.currentPinRegionOffset);

    // Raise head cell z-index to prevent overlap in IE & FF
    headCells.css('z-index', '1');

    var incWidth = function (i,e) {
        e = ice.ace.jq(e);
        if (i > 0) e.width(cellWidth + 20);
        else e.width(cellWidth + 21);
    };

    thead.find(' > thead > tr > th:not(.pinned)').each(incWidth);
    tfoot.find(' > tfoot > tr > td:not(.pinned)').each(incWidth);

    // Add scrolling
    var cachedScrollTopVal = tbody.parent().scrollTop();
    this.columnPinScrollListener[i - 1] = function() {
        var scrollTopVal = tbody.parent().scrollTop();
        bodyCells.each(function(i,e) {
            e.style.top = (e.offsetTop + (cachedScrollTopVal - scrollTopVal)) + "px";
        });
        cachedScrollTopVal = scrollTopVal;
    }

    tbody.parent().bind('scroll', this.columnPinScrollListener[i - 1]);

    if (!isInit && this.behaviors && this.behaviors.pin) {
        var options = {
            source: this.id
        }

        ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.pin, options));
    }
}

ice.ace.DataTable.prototype.getPinnedColumns = function() {
    var table = this,
        tbody = ice.ace.jq(this.jqId + ' > div.ui-datatable-scrollable-body > table');
    if (tbody.length == 0) tbody = ice.ace.jq(this.jqId + ' > div > table');

    return tbody.find('> tbody > tr:first-child > td.pinned').sort(function(a,b) {
        return table.columnPinOrder[ice.ace.jq(a).index()] - table.columnPinOrder[ice.ace.jq(b).index()];
    });
}

ice.ace.DataTable.prototype.getNextPinnedIndex = function(i) {
    return this.getPinnedColumns().length;
}

ice.ace.DataTable.prototype.writePinningState = function () {
    ice.ace.jq(this.pinningHolder).val(JSON.stringify(this.columnPinOrder));
};

ice.ace.DataTable.prototype.readPinningState = function () {
    this.columnPinOrder = ice.ace.jq.parseJSON(ice.ace.jq(this.pinningHolder).val());
};

ice.ace.DataTable.prototype.setupDisabledStyling = function () {
    // Fade out controls
    ice.ace.jq(this.jqId + ' > table > tbody.`ui-datatable-data > tr > td a.ui-row-toggler, ' +
        this.jqId + ' > table > thead > tr > th > div > input.ui-column-filter, ' +
        this.jqId + ' > table > thead > tr > th > div.ui-sortable-column span.ui-sortable-control:first, ' +
        this.jqId + ' > table > tbody.ui-datatable-data > tr > td > div.ui-row-editor span.ui-icon'
    ).css({opacity:0.4});

    // Add pagination disabled style
    ice.ace.jq(this.jqId + ' > .ui-paginator .ui-icon, ' +
        this.jqId + ' > .ui-paginator .ui-paginator-current-page, ' +
        this.jqId + ' > table > thead .ui-tableconf-button a').each(function () {
            ice.ace.jq(this).parent().addClass('ui-state-disabled');
        });

    // Disable filter text entry
    ice.ace.jq(this.jqId + ' > table > thead > tr > th > div > input.ui-column-filter').keypress(function () {
        return false;
    });

    // Row style
    ice.ace.jq(this.jqId + ' > table > tbody.ui-datatable-data > tr > td')
        .css({backgroundColor:'#EDEDED', opacity:0.8});

    ice.ace.jq(this.jqId).addClass('ui-disabled');
}

/* #########################################################################
############################### Requests ################################
######################################################################### */
ice.ace.DataTable.prototype.reorderColumns = function (oldIndex, newIndex) {
    var options = {
        source:this.id,
        execute:this.id,
        render:(this.cfg.configPanel) ? this.id + " " + this.cfg.configPanel : this.id,
        formId:this.cfg.formId
    };

    var params = {},
        _self = this;
    params[this.id + '_columnReorder'] = oldIndex + '-' + newIndex;

    options.params = params;
    options.onsuccess = function (responseXML) {
        return false;
    };

    if (this.behaviors)
        if (this.behaviors.reorder) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.reorder,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadLiveRows = function () {
    var options = {
            source:this.id,
            execute:this.id,
            render:this.id,
            formId:this.cfg.formId
        },
        _self = this;

    options.onsuccess = function (responseXML) {
        ice.ace.eachCustomUpdate(responseXML, function (id, content) {
            if (id == _self.id) {
                ice.ace.jq(_self.jqId + ' div.ui-datatable-scrollable-body:first > table > tbody > tr:last').after(content);
                _self.scrollOffset += _self.cfg.scrollStep;
                if (_self.scrollOffset == _self.cfg.scrollLimit) _self.shouldLiveScroll = false;
            }
            else ice.ace.AjaxResponse.updateElem(id, content);
        });
        _self.resizeScrolling();
        if (_self.cfg.pinning) _self.initializePinningState();
        return false;
    };

    var params = {};
    params[this.id + "_scrolling"] = true;
    params[this.id + "_scrollOffset"] = this.scrollOffset;
    params['ice.customUpdate'] = this.id;

    options.params = params;

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.paginate = function (newState) {
    var options = {
        source:this.id,
        render:this.id,
        execute:this.id,
        formId:this.cfg.formId
    };

    var _self = this;
    options.onsuccess = function (responseXML) {
        if (_self.cfg.scrollable) _self.resizeScrolling();

        return false;
    };

    var params = {};
    params[this.id + "_paging"] = true;
    params[this.id + "_rows"] = newState.rowsPerPage;
    params[this.id + "_page"] = newState.page;

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.page) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.page,
                ice.ace.clearExecRender(options)
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.sort = function (headerCells, savedState) {
    var options = {
        source:this.id,
        render:(this.cfg.configPanel) ? this.id + " " + this.cfg.configPanel : this.id,
        execute:this.id,
        formId:this.cfg.formId
    };

    var _self = this;
    options.onsuccess = function (responseXML) {
        var xmlDoc = responseXML.documentElement,
                extensions = xmlDoc.getElementsByTagName("extension"),
                args = {};

        for (i = 0; i < extensions.length; i++) {
            var extension = extensions[i];
            if (extension.getAttributeNode('aceCallbackParam')) {
                var jsonObj = ice.ace.jq.parseJSON(extension.firstChild.data);

                for (var paramName in jsonObj)
                    if (paramName) args[paramName] = jsonObj[paramName];
            }
        }

        if (args.validationFailed)
            _self.restoreSortState(savedState);

        if (_self.cfg.scrollable) _self.resizeScrolling();
        _self.setupSortEvents();
        return false;
    };

    var params = {}, sortDirs = [], sortKeys = [];
    params[this.id + "_sorting"] = true;
    ice.ace.jq.each(headerCells, function () {
        sortKeys.push(ice.ace.jq(this).attr('id'));
    });
    params[this.id + "_sortKeys"] = sortKeys;
    ice.ace.jq.each(headerCells, function () {
        // Have to "refind" the elements by id, as in IE browsers, the dom
        // elements referenced by headerCells return undefined for
        // .hasClass('ui-toggled')
        sortDirs.push(ice.ace.jq(ice.ace.escapeClientId(ice.ace.jq(this).attr('id')))
            .find('a.ui-icon-triangle-1-n').hasClass('ui-toggled'));
    });
    params[this.id + "_sortDirs"] = sortDirs;

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.sort) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.sort,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.filter = function (evn) {
    var options = {
        source:this.id,
        render:(this.cfg.configPanel) ? this.id + " " + this.cfg.configPanel : this.id,
        execute:this.id,
        formId:this.cfg.formId
    };

    var _self = this;
    var params = {};
    params[this.id + "_filtering"] = true;
    params[this.id + "_filteredColumn"] = ice.ace.jq((evn.target) ? evn.target : evn.srcElement).attr('id');
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.filter) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.filter,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
    ice.setFocus('');
}

ice.ace.DataTable.prototype.doSelectionEvent = function (type, deselection, element) {
    // Get Id(s) //
    var targetId, deselectedId, firstRowSelected, adjustStyle = !this.cfg.instantSelect;
    if (type == 'row') {
        targetId = element.attr('id').split('_row_')[1];
    }
    else if (type == 'cell') {
        var rowId = element.parent().attr('id').split('_row_')[1],
            columnIndex = element.index();
        targetId = rowId + '#' + columnIndex;
    }

    var firstRowSelected = ice.ace.jq(element).closest('tr').parent().children(':first').hasClass('ui-selected');

    // Sync State //
    this.readSelections();

    // Adjust State //
    if (!deselection) {
        if (this.isSingleSelection()) {
            // If single selection unselect previous selection
            if (adjustStyle) {
                if (type == 'row')
                    element.siblings('.ui-selected').removeClass('ui-selected ui-state-active');
                else if (type == 'cell')
                    ice.ace.jq(this.jqId + ' tbody.ui-datatable-data:first > tr > td').removeClass('ui-selected ui-state-active');
            }

            // Add current selection to deselection delta
            this.deselection = [];
            deselectedId = this.selection[0];
            this.deselection.push(deselectedId);

            // The new selection will be the only member of the delta
            this.selection = [];
        }

        if (adjustStyle) {
            // Add selected styling
            element.addClass('ui-state-active ui-selected');
        }
        // Filter id from deselection delta
        this.deselection = ice.ace.jq.grep(this.deselection, function (r) {
            return r != targetId;
        });
        // Add filter id to selection delta
        this.selection.push(targetId);
    } else {
        if (adjustStyle) {
            // Remove selected styling
            element.removeClass('ui-selected ui-state-active');
        }

        // Remove from selection
        this.selection = ice.ace.jq.grep(this.selection, function (r) {
            return r != targetId;
        });
        // Add to deselection
        this.deselection.push(targetId);
    }

    // Write State //
    this.writeSelections();

    // Submit State //
    if (this.cfg.instantSelect) {
        var options = {
            source:this.id,
            execute:this.id,
            formId:this.cfg.formId
        };

        var params = {},
            _self = this;

        if (type == 'row') {
            if (!deselection) {
                // Submit selected index and deselection if single selection enabled
                params[this.id + '_instantSelectedRowIndexes'] = targetId;
                if (deselectedId) params[this.id + '_instantUnselectedRowIndexes'] = deselectedId;
            } else {
                // Submit deselected index
                params[this.id + '_instantUnselectedRowIndexes'] = targetId;
            }
        }

        // If first row is in this selection, deselection, or will be implicitly deselected by singleSelection
        // resize the scrollable table. IE7 only now
        options.onsuccess = function (responseXML) {
            if (ice.ace.jq.browser.msie && ice.ace.jq.browser.version == 7 && (_self.cfg.scrollable
                && (ice.ace.jq.inArray("0", _self.selection) > -1 || ice.ace.jq.inArray("0", _self.deselection) > -1
                || (firstRowSelected && _self.isSingleSelection()))))
                _self.resizeScrolling();
        };


        options.params = params;

        if (this.behaviors)
            if (this.behaviors.select && !deselection) {
                ice.ace.ab(ice.ace.extendAjaxArgs(
                    this.behaviors.select,
                    ice.ace.clearExecRender(options)
                ));
                return;
            } else if (this.behaviors.deselect && deselection) {
                ice.ace.ab(ice.ace.extendAjaxArgs(
                    this.behaviors.deselect,
                    ice.ace.clearExecRender(options)
                ));
                return;
            }

        ice.ace.AjaxRequest(options);
    }
}

ice.ace.DataTable.prototype.doMultiRowSelectionEvent = function (lastIndex, current) {
    var self = this,
        tbody = current.closest('tbody'),
        last = ice.ace.jq(tbody.children().get(lastIndex)),
        lower = current.index() < lastIndex,
        elemRange = lower ? last.prevUntil(current.prev()) : last.nextUntil(current.next()),
        deselectedId, firstRowSelected;

    // Sync State //
    self.readSelections();

    elemRange.each(function (i, elem) {
        var element = ice.ace.jq(elem),
            targetId = element.attr('id').split('_row_')[1];

        // Adjust State //
        element.addClass('ui-state-active ui-selected');
        self.deselection = ice.ace.jq.grep(self.deselection, function (r) {
            return r != targetId;
        });
        self.selection.push(targetId);
    });

    // Write State //
    self.writeSelections();

    // Submit State //
    if (self.cfg.instantSelect) {
        var options = {
            source:self.id,
            execute:self.id,
            formId:self.cfg.formId
        };

        var params = {};
        params[self.id + '_instantSelectedRowIndexes'] = this.selection;

        var firstRowSelected = tbody.children(':first').hasClass('ui-selected');

        // If first row is in this selection, deselection, or will be implicitly deselected by singleSelection
        // resize the scrollable table.
            options.onsuccess = function (responseXML) {
                if (self.cfg.scrollable && (ice.ace.jq.inArray("0", self.selection) > -1 || ice.ace.jq.inArray("0", self.deselection) > -1 || (firstRowSelected && self.isSingleSelection())))
                    self.resizeScrolling();
                if (self.cfg.pinning) self.initializePinningState();
                return false;
            };

        options.params = params;

        if (this.behaviors)
            if (this.behaviors.select) {
                ice.ace.ab(ice.ace.extendAjaxArgs(
                    this.behaviors.select,
                    ice.ace.clearExecRender(options)
                ));
                return;
            }

        ice.ace.AjaxRequest(options);
    }
}


/* #########################################################################
########################### Expansion ###################################
######################################################################### */
ice.ace.DataTable.prototype.toggleExpansion = function (expanderElement) {
    var expander = ice.ace.jq(expanderElement),
        row = expander.closest('tr'),
        expanded = row.hasClass('ui-expanded-row');
    $this = (this);

    if (expanded) {
        var removeTargets = row.siblings('[id^="' + row.attr('id') + '."]');
        if (removeTargets.size() == 0) removeTargets = row.next('tr.ui-expanded-row-content');
        expander.removeClass('ui-icon-circle-triangle-s');
        expander.addClass('ui-icon-circle-triangle-e');
        row.removeClass('ui-expanded-row');
        removeTargets.fadeOut(function () {
            ice.ace.jq(this).remove();
        });
        if ($this.cfg.scrollable) $this.setupScrolling();
        if (!expander.hasClass('ui-row-panel-toggler')) this.sendRowContractionRequest(row);
        else this.sendPanelContractionRequest(row);
    } else {
        expander.removeClass('ui-icon-circle-triangle-e');
        expander.addClass('ui-icon-circle-triangle-s');
        row.addClass('ui-expanded-row');
        if (expander.hasClass('ui-row-panel-toggler')) this.loadExpandedPanelContent(row);
        else this.loadExpandedRows(row);
    }
}

ice.ace.DataTable.prototype.sendPanelContractionRequest = function (row) {
    var options = {
            source:this.id,
            execute:this.id,
            render:this.id,
            formId:this.cfg.formId
        },
        rowId = row.attr('id').split('_row_')[1];
    _self = this;

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    options.params = params;

    options.onsuccess = function (responseXML) {
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    if (this.behaviors)
        if (this.behaviors.contract) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.contract,
                ice.ace.clearExecRender(options)
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.sendRowContractionRequest = function (row) {
    var options = {
            source:this.id,
            execute:this.id,
            render:this.id,
            formId:this.cfg.formId
        },
        rowId = row.attr('id').split('_row_')[1];
    _self = this;

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    ;
    options.params = params;

    options.onsuccess = function (responseXML) {
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    if (this.behaviors)
        if (this.behaviors.contract) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.contract,
                ice.ace.clearExecRender(options)
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadExpandedRows = function (row) {
    var options = {
            source:this.id,
            execute:this.id,
            render:this.id,
            formId:this.cfg.formId
        },
        rowId = row.attr('id').split('_row_')[1],
        _self = this;

    options.onsuccess = function (responseXML) {
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.expand) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.expand,
                ice.ace.clearExecRender(options)
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.loadExpandedPanelContent = function (row) {
    var options = {
            source:this.id,
            execute:this.id,
            render:this.id,
            formId:this.cfg.formId
        },
        rowId = row.attr('id').split('_row_')[1],
        _self = this;

    options.onsuccess = function (responseXML) {
        if (_self.cfg.scrollable) _self.setupScrolling();
        return false;
    };

    var params = {};
    params[this.id + ':' + rowId + '_rowExpansion'] = true;
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.expand) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.expand,
                ice.ace.clearExecRender(options)
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}


/* #########################################################################
########################### Row Editing #################################
######################################################################### */
ice.ace.DataTable.prototype.showEditors = function (element) {
    this.doRowEditShowRequest(element);
}

ice.ace.DataTable.prototype.saveRowEdit = function (element) {
    this.doRowEditSaveRequest(element);
}

ice.ace.DataTable.prototype.cancelRowEdit = function (element) {
    this.doRowEditCancelRequest(element);
}

ice.ace.DataTable.prototype.doRowEditShowRequest = function (element) {
    var row = ice.ace.jq(element).closest('tr'),
        rowEditorId = row.find('> td > div.ui-row-editor, > td > div > div.ui-row-editor').attr('id'),
        options = {
            source:rowEditorId,
            execute:'@this',
            formId:this.cfg.formId
        },
        _self = this,
        cellsToRender = new Array();

    row.find('> td > div.ui-cell-editor, > td > div > div.ui-cell-editor').each(function () {
        cellsToRender.push(ice.ace.jq(this).attr('id'));
    });
    options.render = cellsToRender.join(' ');
    options.render = options.render + " @this";

    options.onsuccess = function (responseXML) {
        var xmlDoc = responseXML.documentElement;

        _self.args = {};

        if (_self.cfg.scrollable)
            _self.resizeScrolling();

        if (_self.cfg.pinning) _self.initializePinningState();

        return false;
    };

    var params = {};
    params[rowEditorId] = rowEditorId;
    params[this.id + '_editShow'] = row.attr('id').split('_row_')[1];
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.editStart) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.editStart,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.doRowEditCancelRequest = function (element) {
    var row = ice.ace.jq(element).closest('tr'),
        rowEditorId = row.find('> td > div.ui-row-editor, > td > div > div.ui-row-editor').attr('id'),
        options = {
            source:rowEditorId,
            execute:'@this',
            formId:this.cfg.formId
        },
        _self = this,
        editorsToProcess = new Array();

    row.find('> td > div.ui-cell-editor, > td > div > div.ui-cell-editor').each(function () {
        editorsToProcess.push(ice.ace.jq(this).attr('id'));
    });
    options.render = editorsToProcess.join(' ');
    options.render = options.render + " @this";

    options.onsuccess = function (responseXML) {
        var xmlDoc = responseXML.documentElement;

        _self.args = {};

        if (_self.cfg.scrollable)
            _self.resizeScrolling();

        if (_self.cfg.pinning) _self.initializePinningState();

        return false;
    };

    var params = {};
    params[rowEditorId] = rowEditorId;
    params[this.id + '_editCancel'] = row.attr('id').split('_row_')[1];
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.editCancel) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.editCancel,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.doRowEditSaveRequest = function (element) {
    var row = ice.ace.jq(element).closest('tr'),
        rowEditorId = row.find('> td > div.ui-row-editor, > td > div > div.ui-row-editor').attr('id'),
        options = {
            source:rowEditorId,
            formId:this.cfg.formId
        },
        _self = this,
        editorsToProcess = new Array();

    row.find('> td > div.ui-cell-editor, > td > div > div.ui-cell-editor').each(function () {
        editorsToProcess.push(ice.ace.jq(this).attr('id'));
    });
    options.execute = editorsToProcess.join(' ');
    options.execute = options.execute + " @this";
    options.render = options.execute;

    options.onsuccess = function (responseXML) {
        var xmlDoc = responseXML.documentElement,
            extensions = xmlDoc.getElementsByTagName("extension");

        _self.args = {};
        for (i = 0; i < extensions.length; i++) {
            var extension = extensions[i];
            if (extension.getAttributeNode('aceCallbackParam')) {
                var jsonObj = ice.ace.jq.parseJSON(extension.firstChild.data);

                for (var paramName in jsonObj)
                    if (paramName) _self.args[paramName] = jsonObj[paramName];
            }
        }

        if (!_self.args.validationFailed) {
            if (_self.cfg.scrollable) _self.resizeScrolling();
        }
        return false;
    };

    var params = {};
    params[rowEditorId] = rowEditorId;
    params[this.id + '_editSubmit'] = row.attr('id').split('_row_')[1];

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.editSubmit) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                this.behaviors.editSubmit,
                options
            ));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.DataTable.prototype.getRowEditors = function () {
    return this.element.find(this.cellEditorSelector.replace(/link/g, ''));
}

ice.ace.DataTable.prototype.setupCellEditorEvents = function (rowEditors) {
    var _self = this;

    // unbind and rebind these events.
    var showEditors = function (event) {
            event.stopPropagation();
            _self.showEditors(event.target);
        },
        saveRowEditors = function (event) {
            event.stopPropagation();
            _self.saveRowEdit(event.target);
        },
        cancelRowEditors = function (event) {
            event.stopPropagation();
            _self.cancelRowEdit(event.target);
        },
        inputCellKeypress = function (event) {
            if (event.which == 13) return false;
        };
    var selector = this.cellEditorSelector;

    var icoSel = selector.replace(/link/g, 'a.ui-icon-pencil');
    ice.ace.jq(this.jqId).off('click keyup', icoSel)
            .on('click', icoSel, showEditors)
            .on('keyup', icoSel, function (event) {
        if (event.which == 32 || event.which == 13) {
            showEditors(event);
        }
    });

    icoSel = selector.replace(/link/g, 'a.ui-icon-check');
    ice.ace.jq(this.jqId).off('click keyup', icoSel)
            .on('click', icoSel, saveRowEditors)
            .on('keyup', icoSel, function (event) {
        if (event.which == 32 || event.which == 13) {
            saveRowEditors(event);
        }
    });

    icoSel = selector.replace(/link/g, 'a.ui-icon-close');
    ice.ace.jq(this.jqId).off('click keyup', icoSel)
            .on('click', icoSel, cancelRowEditors)
            .on('keyup', icoSel, function (event) {
        if (event.which == 32 || event.which == 13) {
            cancelRowEditors(event);
        }
    });

    rowEditors.closest('tr').find(' > div.ui-cell-editor > span > input')
            .bind('keypress', inputCellKeypress);
}


/* #########################################################################
 ########################## Selection Helpers ############################
 ######################################################################### */
ice.ace.DataTable.prototype.writeSelections = function () {
    // Writes selection state to hidden field for submission
    ice.ace.jq(this.selectionHolder).val(this.selection.join(','));
    ice.ace.jq(this.deselectionHolder).val(this.deselection.join(','));
};

ice.ace.DataTable.prototype.readSelections = function () {
    // Reading clears JS selected state following delta field submissions
    var selectionVal = ice.ace.jq(this.selectionHolder).val(),
        deselectionVal = ice.ace.jq(this.deselectionHolder).val();
    this.selection = (selectionVal == '') ? [] : selectionVal.split(',');
    this.deselection = (deselectionVal == '') ? [] : deselectionVal.split(',');
}

ice.ace.DataTable.prototype.isSingleSelection = function () {
    return this.cfg.selectionMode == 'single' || this.cfg.selectionMode === 'singlecell';
};

ice.ace.DataTable.prototype.isSelectionEnabled = function () {
    return this.cfg.selectionMode == 'single' || this.cfg.selectionMode == 'multiple' || this.isCellSelectionEnabled();
};

ice.ace.DataTable.prototype.isCellSelectionEnabled = function () {
    return this.cfg.selectionMode === 'singlecell' || this.cfg.selectionMode === 'multiplecell';
};

ice.ace.DataTable.prototype.clearSelection = function () {
    this.selection = [];
    ice.ace.jq(this.selectionHolder).val('');
};
