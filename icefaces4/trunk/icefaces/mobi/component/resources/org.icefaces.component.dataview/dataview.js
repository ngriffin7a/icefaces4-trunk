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

(function(im) {
    var isTouchDevice = 'ontouchstart' in document.documentElement,
        indicatorSelector = "i.mobi-dv-si",
        blankInicatorClass = 'mobi-dv-si';
    function DataView(clientId, cfg) {
        function hideAddressBar() {
            if(!window.location.hash) {
                window.scrollTo(0, 0);
            }
        }
        if (navigator.userAgent.match(/iPhone/i) || navigator.userAgent.match(/iPod/i)) {
            ice.mobi.addListener(window, "load", function(){ if(!window.pageYOffset){ hideAddressBar(); } } );
            ice.mobi.addListener(window, "orientationchange", hideAddressBar );
        }

        var config = cfg,
            selectorId = '#' + im.escapeJsfId(clientId),
            bodyRowSelector = selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > tbody > tr',
            headCellSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-head > thead > tr > th';

        function getNode(elem) {
            var footCellSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-foot > tfoot > tr > td',
                firstRowSelector = bodyRowSelector + ':first-child',
                detailsSelector = selectorId + ' > .mobi-dv-det',
                headSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-head > thead',
                footSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-foot > tfoot',
                bodyDivSelector = selectorId + ' > .mobi-dv-mst > div';

            switch (elem) {
                case 'det': return document.querySelector(detailsSelector);
                case 'head': return document.querySelector(headSelector);
                case 'foot': return document.querySelector(footSelector);
                case 'body': return document.querySelector(bodyDivSelector);
                case 'elem': return document.getElementById(clientId);
                case 'headcells': return document.querySelectorAll(headCellSelector);
                case 'bodyrows': return document.querySelectorAll(bodyRowSelector);
                case 'firstrow': return document.querySelector(firstRowSelector);
                case 'footcells': return document.querySelectorAll(footCellSelector);
            }
        }

        function closest(start, target) {
            var t = start;
            while (t && t != document&& !im.matches(t,target))
                t = t.parentNode;

            return t == document ? null : t;
        }

        function getScrollableContainer(element) {
            if (!element){
            //    console.log("getScrollablecontainer NO ELEMENT SO return BODY");
                return document.body;
            }
            var height = element.clientHeight,
                parent = element.parentNode;

            while (parent != null && parent.scrollHeight == parent.clientHeight)
                parent = parent.parentNode;

            return parent;
        }

        function getIndexInput(details) {
            var r = Array.prototype.filter.call(details.children, function(n) {
                return n.nodeName.toLowerCase() == "input" && n.getAttribute('name') == clientId+'_active';
            });
            return r[0];
        }

        function initTableAlignment() {
            var dupeHead = document.querySelector(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > thead'),
                dupeHeadCells = document.querySelectorAll(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > thead > tr > th'),
                dupeFoot = document.querySelector(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > tfoot'),
                dupeFootCells = document.querySelectorAll(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > tfoot > tr > td'),
                head = getNode('head'),
                headCells = getNode('headcells'),
                foot = getNode('foot'),
                footCells = getNode('footcells'),
                bodyDivWrapper = getNode('body'),
                firstrow = getNode('firstrow'),
                sheet = im.getStyleSheet(clientId + '_colvis');

            if (!sheet) sheet = im.addStyleSheet(clientId + '_colvis', selectorId);;

            var firstRowBodyCells = Array.prototype.filter.call(firstrow.children, function(val){
                return val.nodeName.toLowerCase() == "td"; /* remove hidden input */
            });

            if( window.getComputedStyle ){
                var frbcWidths = Array.prototype.map.call(
                    firstRowBodyCells,
                    function(n) {
                        var compd = document.defaultView.getComputedStyle(n, null);
                        return n.clientWidth - Math.round(parseFloat(compd.paddingLeft)) - Math.round(parseFloat(compd.paddingRight));
                });

                /* fix body column widths */
                for (var i = 0; i < frbcWidths.length; i++) {
                    if( sheet.insertRule ){
                        sheet.insertRule(selectorId + " ." + firstRowBodyCells[i].classList[0] + " { max-width: " + frbcWidths[i] + "px;min-width: " + frbcWidths[i] + "px;width: " + frbcWidths[i] + "px;}", 0);
                    }
                }

                var dupeHeadCellWidths = Array.prototype.map.call(
                    dupeHeadCells,
                    function(n) {
                        var compd = document.defaultView.getComputedStyle(n, null);
                        return n.clientWidth - Math.round(parseFloat(compd.paddingLeft)) - Math.round(parseFloat(compd.paddingRight));
                    });

                var dupeFootCellWidths = Array.prototype.map.call(
                    dupeFootCells,
                    function(n) {
                        var compd = document.defaultView.getComputedStyle(n, null);
                        return n.clientWidth - Math.round(parseFloat(compd.paddingLeft)) - Math.round(parseFloat(compd.paddingRight));
                    });

                /* copy head col widths from duplicate header */
                for (var i = 0; i < dupeHeadCellWidths.length; i++) {
                    headCells[i].style.width = dupeHeadCellWidths[i] + 'px';
                }

                /* copy foot col widths from duplicate footer */
                if (footCells.length > 0)
                    for (var i = 0; i < dupeFootCellWidths.length; i++) {
                        footCells[i].style.width = dupeFootCellWidths[i] + 'px';
                    }
            }

            if (config.colvispri) setupColumnVisibiltyRules(firstrow.children);

            /* hide duplicate header */
            setTimeout(function() {
                if (dupeHead) dupeHead.style.display = 'none';
                if (dupeFoot) dupeFoot.style.display = 'none';
            }, 50) /* hiding instantly broke scrolling when init'ing the first time on landscape ipad */

            recalcScrollHeight(head, foot, bodyDivWrapper);
        }

        function setupColumnVisibiltyRules(firstRowCells) {
            var minDevWidth = firstRowCells[0].getBoundingClientRect().left;
            var colVisSheet = im.getStyleSheet(clientId + '_colvis');
            var hideRule = '@media only all {';

            if (!colVisSheet) colVisSheet = im.addStyleSheet(clientId + '_colvis', selectorId);

            var prioritizedCells = config.colvispri.map(function(pri, i) {
               var index = config.colvispri.indexOf(i);
               return index > -1 ? firstRowCells[index] : undefined;
            }).filter(function(cell) { return cell != undefined});

            for (var i = 0; i < prioritizedCells.length; i++) {
                var columnClassname = Array.prototype.filter.call(
                        prioritizedCells[i].classList,
                        function(name) {if (name.indexOf('mobi-dv-c') != -1) return true;}
                    )[0];

                minDevWidth += prioritizedCells[i].clientWidth;

                // add column conditional visibility rule
                hideRule += 'th.'+columnClassname+', td.'+columnClassname;
                if (i != (prioritizedCells.length - 1)) hideRule += ', ';
                if( colVisSheet.insertRule )
                    colVisSheet.insertRule('@media screen and (min-width: '+minDevWidth+'px) { td.'+columnClassname+', th.'+columnClassname+' { display: table-cell; }}', 0);
            }

            hideRule += '{ display:none; }}';
            if( colVisSheet.insertRule )
                colVisSheet.insertRule(hideRule, 0);
        }

        /* arguments optional to avoid lookup */
        function recalcScrollHeight(inHead, inFoot, inDivWrap) {
            /* set scroll body to maximum height, reserving space for head / foot */
            var head = inHead ? inHead : getNode('head'),
                foot = inFoot ? inFoot : getNode('foot'),
                bodyDivWrapper = inDivWrap ? inDivWrap : getNode('body'),
                element = getNode('elem');

            // Exit if dataview has been removed from page.
            if (!element) return;

            var dim = element.getBoundingClientRect(),
                maxHeight = window.innerHeight - dim.top,
                headHeight = head ? head.clientHeight : 0,
                footHeight = foot ? foot.clientHeight : 0,
                fullHeight = maxHeight - headHeight - footHeight - 1;

            /* set height to full visible size of parent */
            if( isNumber(fullHeight) )
                bodyDivWrapper.style.height = fullHeight + 'px';

            /* set height to full visible size of parent minus
             height of all following elements */
            var container = getScrollableContainer(element),
                bottomResize = function() {
                    if (!container){
                     //   console.log (" NO CONTAINER SO RETURN");
                        return;
                    }
                    fullHeight -= (container.scrollHeight - container.clientHeight);
                    if( isNumber(fullHeight)){
                        if (navigator.userAgent.match(/iPhone/i) || navigator.userAgent.match(/iPod/i))
                            fullHeight += 60;
                        bodyDivWrapper.style.height = fullHeight + 'px';
                    }
                };

            if (container) bottomResize();
        }

        var touchedHeadCellIndex = {},
            touchedFirstCell = false,
            pendingSortClick,
            lastTouchTime;

        function headCellTouchStart(e) {
			if (config.disabled) return;
            var cell = e.currentTarget;
            /* targetTouches[0] - ignore multi touch starting here */
            touchedHeadCellIndex[e.targetTouches[0].identifier] = getIndex(cell);
            if (im.matches(cell,headCellSelector+":first-child"))
                touchedFirstCell = true;

            /*prevent scrolling due to drags */
            e.preventDefault();
        }

        function headCellTouchEnd(e) {
			if (config.disabled) return;
            var touch = e.changedTouches[0],
                cell = closest(document.elementFromPoint(touch.pageX, touch.pageY), 'th');

            /* do jump scroll to top */
            if (lastTouchTime && (new Date().getTime() - lastTouchTime < 300)) {
                clearTimeout(pendingSortClick);
                getNode('body').scrollTop = 0;
            } else {
                /* do sorting or drag behaviors */
                if (cell) {
                    var index = getIndex(cell);
                    // clear sort if dragged from first to last cell
                    if (touchedFirstCell && im.matches(cell,headCellSelector+":last-child")) {
                        clearSort();
                    } else if (index == touchedHeadCellIndex[touch.identifier])
                        // delay sort to see if jump scroll tap occurs
                        var sort = sortColumn;
                        pendingSortClick = setTimeout(function () {sort(e);}, 320);
                } else {
                    // dragged from header cell to top 25 px of detail region - close region
                    var detTop = getNode('det').getBoundingClientRect().top;

                    if (touch.pageY < detTop + 25) {
                        deactivateDetail();
                    }
                }
            }

            lastTouchTime = new Date().getTime();
            touchedFirstCell = false;
            touchedHeadCellIndex[touch.identifier] = undefined;
        }

        var touchedRowIndex = {};
        function rowTouchStart(e) {
			if (config.disabled) return;
            var row = e.delegateTarget;

            touchedRowIndex[e.targetTouches[0].identifier] = {
                i : row.getAttribute("data-index"),
                y : e.targetTouches[0].pageY,
                x : e.targetTouches[0].pageX
            };
        }

        function rowTouchEnd(e) {
			if (config.disabled) return;
            var row = closest(document.elementFromPoint(e.changedTouches[0].pageX, e.changedTouches[0].pageY), 'tr'),
                index = row.getAttribute("data-index"),
                y = touchedRowIndex[e.changedTouches[0].identifier].y - e.changedTouches[0].pageY ,
                x = touchedRowIndex[e.changedTouches[0].identifier].x - e.changedTouches[0].pageX;

            /* prevent input when scrolling rows or drag in wide cell*/
            y = y > -25 && y < 25;
            x = x > -25 && y < 25;

            if (index == touchedRowIndex[e.changedTouches[0].identifier].i && y && x){
                tapFlash(e.delegateTarget);

                if (e.delegateTarget.classList.contains('ui-state-active'))
                    deactivateDetail()
                else activateRow(e);
            }

            touchedRowIndex[e.changedTouches[0].identifier] = null;
        }

        function initSortingEvents() {
            var headCells = getNode('headcells');
            for (var i = 0; i < headCells.length; ++i) {
                var cell = headCells[i];
                if (isTouchDevice) {
                    ice.mobi.addListener(cell, "touchend", headCellTouchEnd);
                    ice.mobi.addListener(cell, "touchstart", headCellTouchStart);
                } else {
                    ice.mobi.addListener(cell, "click", sortColumn);
                }
            }
        }

		var activationTimeout = null;

        function initActivationEvents() {
            var element = getNode('elem'),
                /* filter events for those bubbled from tr elems */
                isRowEvent = function(callback) {
                    return function(e) {
                        var tr = closest(e.srcElement || e.target, "tr");
                        if (tr && im.matches(tr, bodyRowSelector)) {
                            e.delegateTarget = tr;
                            callback(e);
                        }
                    };
                }

            if (isTouchDevice) {
                ice.mobi.addListener(element, "touchend", function(e) {
					if (!activationTimeout) {
                        //stop from triggering the synthetic click event
                        e.stopPropagation();
						activationTimeout = setTimeout(function() {
								var tr = closest(e.srcElement || e.target, "tr");
								if (tr && im.matches(tr, bodyRowSelector)) {
									e.delegateTarget = tr;
									rowTouchEnd(e);
								}
								clearTimeout(activationTimeout);
								activationTimeout = null;
							}
						,100);
					}
				});
                ice.mobi.addListener(element, "touchstart", isRowEvent(rowTouchStart));
                ice.mobi.addListener(element, "click", function(e) {
					if (!activationTimeout) {
						activationTimeout = setTimeout(function() {
								var tr = closest(e.srcElement || e.target, "tr");
								if (tr && im.matches(tr, bodyRowSelector)) {
									e.delegateTarget = tr;
									activateRow(e);
								}
								clearTimeout(activationTimeout);
								activationTimeout = null;
							}
						,100);
					}
				});
            } else {
                ice.mobi.addListener(element, "click", isRowEvent(activateRow));
            }
        }

        function processUpdateStr(dir) {
			if( !dir ){
				return;
			}
            var valueParts = dir.split('|');
            var details = getNode('det');

            /* lookup elem by id and apply updates */
            for (var i = 0; i < valueParts.length; i++) {
                var v = valueParts[i].split('='),
                        elem = details.querySelector('[id$='+im.escapeJsfId(v[0])+']'),
                        dir = v[1],
                        value;

                switch (dir) {
                    case 'html':
                        elem.innerHTML = v[2];
                        break;

                    case 'attr':
                        if (v[2] == 'checked') {
                            if (v[3] == 'true') elem.checked = true;
                            else elem.checked = false;
                        } else {
                            elem.setAttribute(v[2], v[3]);
                        }
                        break;

                    default :
                        value = '';
                        break;
                }
            };
        }

        var sortCriteria = [];
        function isNumber(n) {
            return !isNaN(parseFloat(n)) && isFinite(n);
        }

        function isDate(n) {
            return !isNaN(Date.parse(n));
        }

        function isCheckboxCol(n) {
            return n.className.indexOf('mobi-dv-bool') != -1;
        }

        function getRowComparator() {
            var firstrow = getNode('firstrow');

            function getValueComparator(cri) {
                var firstRowVal = firstrow.children[cri.index].innerHTML;
                var ascending = cri.ascending ? 1 : -1;

                if (isNumber(firstRowVal))
                    return function(a,b) {
                        return (a.children[cri.index].innerHTML
                            - b.children[cri.index].innerHTML) * ascending;
                    }
                if (isDate(firstRowVal))
                    return function(a,b) {
                        var av = new Date(a.children[cri.index].innerHTML),
                            bv = new Date(b.children[cri.index].innerHTML);

                        if (av > bv) return 1 * ascending;
                        else if (bv > av) return -1 * ascending;
                        return 0;
                    }
                if (isCheckboxCol(firstrow.children[cri.index]))
                /* checkmark markup is shorter - reverse string sort */
                    return function(a,b) {
                        var av = a.children[cri.index].innerHTML,
                            bv =  b.children[cri.index].innerHTML;

                        if (av < bv) return 1 * ascending;
                        else if (bv < av) return -1 * ascending;
                        return 0;
                    }
                else
                /* fall back to string comparison */
                    return function(a,b) {
                        var av = a.children[cri.index].innerHTML,
                            bv =  b.children[cri.index].innerHTML;

                        if (av > bv) return 1 * ascending;
                        else if (bv > av) return -1 * ascending;
                        return 0;
                    }
            }

            return sortCriteria.map(getValueComparator)
                    .reduceRight(function(comp1, comp2) {
                if (comp1 == undefined) return function(a,b) { return comp2(a, b); }
                else return function(a,b) {
                    var v = comp2(a, b);
                    if (v != 0) return v;
                    else return comp1(a, b);
                }
            });
        }

        function getIndex(elem) {
            var columnIndex = 0, sib = elem;
            while( (sib = sib.previousSibling) != null )
                columnIndex++;
            return columnIndex;
        }

        function deactivateDetail() {
            var det = getNode('det');
            getIndexInput(det).setAttribute('value', '-1');
            Array.prototype.every.call(getNode('bodyrows'), function(e) {
                e.classList.remove('ui-state-active');
                return true
            });
            recalcScrollHeight();
        }

        function clearSort() {
            sortCriteria = [];
            Array.prototype.every.call(getNode('headcells'), function(c) {
                    var indi = c.querySelector(indicatorSelector);
                    if (indi) {
                        indi.className = blankInicatorClass;
                    }
                    return true;
                });

            var bodyRows = getNode('bodyrows'),
                tbody = bodyRows[0].parentNode;

            /* return rows to 'natural ordering' */
            bodyRows = Array.prototype.map.call(bodyRows, function(r) {return r;})
                .sort(function(a,b) { return a.getAttribute('data-index') - b.getAttribute('data-index');});

            tbody.innerHTML = '';
            bodyRows.every(function(row) {
                tbody.appendChild(row);
                return true;
            });
        }

        function sortColumn(event) {
			if (config.disabled) return;
            var sortedRows, asc,
                headCell = event.target,
                ascendingClass = blankInicatorClass + ' icon-caret-up',
                descendingClass = blankInicatorClass + ' icon-caret-down';

            var ascIndi = headCell.querySelector(indicatorSelector);
            if (!ascIndi) {
                //non-sortable column
                return;
            }

            var ascClass = ascIndi.className;

            /* find col index */
            var columnIndex = getIndex(headCell);

            /* set ascending flag and indicator */
            asc = ascClass == descendingClass || ascClass == blankInicatorClass;
            ascIndi.className = asc ? ascendingClass : descendingClass;

            // sortCriteria = sortCriteria.filter(function(c) { return c.index != columnIndex});

            // forced single sort
            sortCriteria = [{ascending : asc, index : columnIndex}];

            /* remove indicator from other cols */
            var sortedIndexes = sortCriteria.map(function(c) {return c.index});
            Array.prototype.filter.call(getNode('headcells'), function (c) {return sortedIndexes.indexOf(getIndex(c)) == -1;})
                .every(function(c) {
                    var indi = c.querySelector(indicatorSelector);
                    if (indi) {
                        indi.className = blankInicatorClass;
                    }
                    return true;
                });

            var bodyRows = getNode('bodyrows');
            /* return bodyRows NodeList as Array for sorting */
            sortedRows = Array.prototype.map.call(bodyRows, function(row) { return row; });
            sortedRows = sortedRows.sort(getRowComparator());

            /* remove previous tbody conent and re-add in new order */
            var tbody = bodyRows[0].parentNode;
            Array.prototype.every.call(sortedRows, function(row) {
                tbody.removeChild(row);
                return true;
            });
            Array.prototype.every.call(sortedRows, function(row) {
                tbody.appendChild(row);
                return true;
            });
        }

        function tapFlash(elem) {
            elem.style.backgroundColor = '#194FDB';
            elem.style.backgroundImage = 'none';
            setTimeout(function() {
                elem.style.backgroundColor = '';
                elem.style.backgroundImage = '';
            }, 100);
        }

        function activateRow(event) {
			if (config.disabled) return;
            var newIndex = event.delegateTarget.getAttribute('data-index'),
                details = getNode('det'),
                indexIn = getIndexInput(details);

			var target = event.delegateTarget;
			if (target.classList.contains('ui-state-active')) {
				target.classList.remove('ui-state-active');
				deactivateDetail();
				return;
			} else {
				target.classList.add('ui-state-active');
			}

			var sib = event.delegateTarget.nextElementSibling,
				removeActiveClass = function (s) { s.classList.remove('ui-state-active'); };

			while (sib != null) {removeActiveClass(sib); sib = sib.nextElementSibling;};
			sib = event.delegateTarget.previousElementSibling;
			while (sib != null) {removeActiveClass(sib); sib = sib.previousElementSibling;};

            indexIn.setAttribute("value", newIndex);

            if (config.active == 'client') {
                var newValue = event.delegateTarget.getAttribute('data-state');

                processUpdateStr(newValue);

                // if vertical orientation
                recalcScrollHeight();
            } else {
				if (config.behaviors && config.behaviors.select)
					ice.ace.ab(config.behaviors.select);
            }
        }

        function update(newCfg){
            config = newCfg;
			if (newCfg.disabled) {
				deactivateDetail();
				clearSort();
			} else {
            initActivationEvents();
            initSortingEvents();
			}
            initTableAlignment();
        }

		if (!config.disabled) {
			initActivationEvents();
			initSortingEvents();
		}

        /* first alignment needs to occur shortly after script eval
        *  else heights are miscalculated for following elems */
        setTimeout(initTableAlignment, 100);

        /* resize height adjust */
        ice.mobi.addListener(window, "resize", function() {
            // Timeout to prevent double recalc when resize is due to orientation
            if (!oriChange) {
                setTimeout(function() {
                    if (!oriChange) {
                        recalcScrollHeight();
                    }
                },100);
            }
        });

        var oriChange = false;
        ice.mobi.addListener(window, "orientationchange", function() {
            oriChange = true;

            setTimeout(function() { recalcScrollHeight(); },500);
            // prevent resize-init'd height recalcs for the next 200ms
            setTimeout(function() { oriChange = false; },2000);
        });

		var det = getNode('det'), idx = getIndexInput(det), v = idx.getAttribute("value");

		if (v > -1) {
			var i = 0;
            Array.prototype.every.call(getNode('bodyrows'), function(e) {
                if (i == v) e.classList.add('ui-state-active');
				i++;
                return true;
            });
		}

        /* Instance API */
        return { update: update }
    }

    im.dataView = {
        instances: {},
        create: function(clientId, cfg) {
            if (this.instances[clientId]) this.instances[clientId].update(cfg);
            else this.instances[clientId] = DataView(clientId, cfg);

            return this.instances[clientId];
        }
    }

})(ice.mobi);