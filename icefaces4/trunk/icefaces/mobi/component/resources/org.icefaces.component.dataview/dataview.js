/* ICEsoft HTML5 DataView
 * User: Nils Lundquist
 * Date: 2013-08-02 * Time: 9:59 AM
 */

(function(mStr) {
    if (window[mStr] === undefined) window[mStr] = {};
    var ice = window[mStr];

    /**
     * A JavaScript Table UI component. Supports flexible filtering, configurable row detail views, static headers,
     * auto-sizing, custom DOM events and 2-way databinding via a dust.js helper. The layout of table regions are
     * defined by templates that be overridden for the whole table or per-column, allowing any table styling desired
     * to be used. The DOM event broadcasting allows for integration with custom application logic, particularly
     * synchronizing table data changes with backend via a webservice.
     *
     * @constructor
     * Construct a new instance of the DataView with the given ID, data and configuration.
     *
     * @param {String} id Id of the new table to be rendered
     *
     * @param {Object[]} data array of row objects
     *
     * @param {Object} cfg
     * configuration object
     *
     * @fires construct
     *
     */
    ice.DataView = function(id, data, cfg) {
        /**
         * @cfg {Object} cfg
         * The rendering configuration of this component. Defines all facets of
         * component including templates. Features like filtering and 2-way
         * data binding are enabled by wrapping an input element in a column cell
         * template with a template directive. Should define 'target' and 'columns"
         * at a minimum.
         *
         * @cfg {String} cfg.target
         * The selector defining the DOM element to insert the table in after
         * rendering, most often an empty div.
         *
         * @cfg {boolean | Object} [cfg.fixedHeaderSizing=false]
         * Determines if the table should use JS sizing calculations to fix the
         * position of the header, footer and column widths. Passing in either
         * 'true' or a config object enables this feature.
         *
         * @cfg {boolean} [cfg.fixedHeaderSizing.sizeFromWindow=false]
         * Determine if the table JS sizing should use the window instead of
         * the parent container to determine the available space.
         *
         * @cfg {"row" | "global"} [cfg.detailPosition="row"]
         * A keyword defining the location to render the detail region when a
         * row is activated. Accepted values are 'global' or 'row'.
         *
         * @cfg {Object[]} cfg.columns
         * The Array of column definition objects.
         *
         * @cfg {String} cfg.columns.name
         * The name of the row object property to be rendered in this cell. If
         * the name is not a property of the row object the 'value' variable
         * passed to the bodycell template will be undefined.
         *
         * @cfg {String} [cfg.columns.headerText]
         * The text to be passed for rendering to the headcell template of this
         * column.
         *
         * @cfg {String} [cfg.columns.footerText]
         * The text to be passed for rendering to the footcell template of this
         * column.
         *
         * @cfg {Object} [cfg.columns.template]
         * The per-column custom templates defined for this column.
         *
         * @cfg {String} [cfg.columns.template.headcell]
         * The per-column header cell template defined for this column.
         *
         * @cfg {String} [cfg.columns.template.bodycell]
         * The per-column body cell template defined for this column.
         *
         * @cfg {String} [cfg.columns.template.footcell]
         * The per-column footer cell template defined for this column.
         *
         * @cfg {Object} [cfg.templates]
         * The object defining table wide custom templates.
         *
         * @cfg {Object} [cfg.templates.head]
         * The template defining the thead tag region.
         *
         * @cfg {Object} [cfg.templates.foot]
         * The template defining the tfoot tag region.
         *
         * @cfg {Object} [cfg.templates.body]
         * The template defining the tbody tag region.
         *
         * @cfg {Object} [cfg.templates.headcell]
         * The global header cell template defined for this table. Overridden by per-column templates.
         *
         * @cfg {Object} [cfg.templates.bodycell]
         * The global header cell template defined for this table. Overridden by per-column templates.
         *
         * @cfg {Object} [cfg.templates.footcell]
         * The global header cell template defined for this table. Overridden by per-column templates.
         *
         * @cfg {Object} [cfg.templates.table]
         * The template defining the table tag region.
         *
         * @cfg {Object} [cfg.templates.rowdetail]
         * The template defining the detail region wrapper when detailPosition "row" is used.
         *
         * @cfg {Object} [cfg.templates.globaldetail]
         * The template defining the detail region wrapper when detailPosition "global" is used.
         *
         * @cfg {Object} [cfg.templates.detail]
         * The template defining the detail region contents.
         */
        /**
         * @event construct
         * Triggered after the component gets rendered, sizing has finished and all events are bound.
         */
        /**
         * @event addrow
         * Triggered after a row is added via the component API.
         */
        /**
         * @event removerow
         * Triggered after a row is removed via the component API.
         */
        /**
         * @event activate
         * Triggered after a row detail region is rendered.
         */
        /**
         * @event deactivate
         * Triggered after a row detail region is removed.
         */
        /**
         * @event filter
         * Triggered after a the table has filtered its rows.
         */
        /**
         * @event update
         * Triggered after a row column property is updated using the template {auto} helper.
         */

        this.id = id;
        this.renderConf = cfg ? cfg : {};

        if (!data) data = [];

        // Check for dependencies
        if (!this.id)
            throw new Error('Required unique identifier was not supplied');

        if (!dustCheck())
            throw new Error('Required Dust.js dependency is not loaded.');

        if (!this.renderConf || !this.renderConf.target)
            throw new Error('Required \'target\' config parameter is not given. This selector defines where the table is rendered.');

        // Initialize DOM
        construct.call(this, data);
    }





    // Private dataView mutable state
    var filterCri = [];





    // Private dataView methods - call using privateMethod.call(dvInstance, argArray);
    function reconstruct() {
        construct.call(this, this.getRowDataList());
    }

    function construct(data) {
        var conf = this.renderConf;
        if (!conf.templates) conf.templates = {};
        if (!conf.animations) conf.animations = {};

        // Row type definitions TODO
        conf.defaultRow = data[0];

        var templates = {
            bodycell    : conf.templates.bodycell ? conf.templates.bodycell : defaultTemplates.bodycell,
            headcell    : conf.templates.headcell ? conf.templates.headcell : defaultTemplates.headcell,
            footcell    : conf.templates.footcell ? conf.templates.footcell : defaultTemplates.footcell,
            row         : conf.templates.row ? conf.templates.row : defaultTemplates.row,
            body        : conf.templates.body ? conf.templates.body : defaultTemplates.body,
            head        : conf.templates.head ? conf.templates.head : defaultTemplates.head,
            foot        : conf.templates.foot ? conf.templates.foot : defaultTemplates.foot,
            table       : conf.templates.table ? conf.templates.table : defaultTemplates.table
        }

        var base = getDustBase(), id = this.id;

        // Register Dust templates
        var columns = conf.columns ? conf.columns : [], self = this;
        for (var elem in templates) {
            // Register per-column templates if defined
            if (elem.indexOf('cell') > -1) {
                columns.forEach(function(col) {
                    if (col.template && col.template[elem]) dust.loadSource(dust.compile(col.template[elem], self.id+'_'+col.name+'_'+elem));
                    else dust.loadSource(dust.compile(templates[elem], self.id+'_'+col.name+'_'+elem));
                })
            } else {
                dust.loadSource(dust.compile(templates[elem], this.id+'_'+elem));
            }
        }

        /* Attach the row object to every generated row */
        function attachData() {
            Array.prototype.forEach.call(
                document.querySelectorAll('#'+id+' > tbody > tr'),
                function(row, idx) {
                    row.data = data[idx];
                    return true;
                });
        }

        function attachFilterListener(thead, tbody, dv) {
            thead.addEventListener('change', function(event) {
                var filterTag = (function(parent){
                        while (parent != thead) {
                            if (parent.classList.contains('dv-filter'))
                                break;
                            parent = parent.parentNode;
                        }
                        return parent === thead ? undefined : parent;
                    })(event.target.parentNode),
                    propName = filterTag.getAttribute('data-prop'),
                    value = convertValue(tbody.firstChild.data[propName], getValue(event.target));

                if (filterTag && value && value != '')
                    dv.addFilter(propName, value);
                else
                    dv.removeFilter(propName);
            });
        }

        function attachAutoListener(tbody) {
            tbody.addEventListener('change', function(event) {
                var autoTag = (function(parent){
                    while (parent != tbody) {
                        if (parent.classList.contains('dv-auto'))
                            break;
                        parent = parent.parentNode;
                    }
                    return parent === tbody ? undefined : parent;
                })(event.target.parentNode);

                if (autoTag)
                    window[mStr].DataView.autoUpdate(autoTag, event);
            });
        }

        function applyFixedHeaderSizing(target) {
            var table = target.querySelector('#'+id),
                head = table.querySelector('thead'),
                headCells = head.querySelectorAll('tr > th'),
                foot = table.querySelector('tfoot'),
                footCells = foot.querySelectorAll('tr > td'),
                tbody = table.querySelector('tbody'),
                firstRowCells = tbody.querySelector('tr:first-child').childNodes,
                dupeHead = head.cloneNode(true),
                dupeHeadCells = dupeHead.querySelectorAll('tr > th'),
                dupeFoot = foot.cloneNode(true),
                dupeFootCells = dupeFoot.querySelectorAll('tr > td');

            var dupeHeadTable = document.createElement('table'),
                dupeFootTable = document.createElement('table'),
                headWrapper = document.createElement('div'),
                footWrapper = document.createElement('div');

            dupeHeadTable.appendChild(dupeHead);
            dupeFootTable.appendChild(dupeFoot);

            headWrapper.style.overflow = 'hidden';
            footWrapper.style.overflow = 'hidden';

            headWrapper.appendChild(dupeHeadTable);
            footWrapper.appendChild(dupeFootTable);

            target.insertBefore(headWrapper, target.firstChild);
            target.appendChild(footWrapper);

            table.style.tableLayout = 'fixed';

            if( window.getComputedStyle ){
                var frcWidths = Array.prototype.map.call(
                    firstRowCells,
                    function(n) {
                        var compd = document.defaultView.getComputedStyle(n, null);
                        return n.clientWidth;
                    });

                /* fix body column widths */
                // webkit has bugged colgroup : http://code.google.com/p/chromium/issues/detail?id=20865
                if (!navigator.userAgent.indexOf('webkit') >= 0) {
                    for (var i = 0; i < frcWidths.length; i++) {
                        Array.prototype.forEach.call(tbody.childNodes, function(x) {
                            x.childNodes[i].style.minWidth = frcWidths[i] + 'px';
                        });
                    }
                } else {
                    var oldColGroup = table.querySelector('colgroup[data-fixedhead]'),
                        colGroup = document.createElement('colgroup');

                    colGroup.setAttribute('data-fixedhead', '');

                    for (var i = 0; i < frcWidths.length; i++) {
                        var col = document.createElement('col');
                        colGroup.appendChild(col);
                        col.style.minWidth = frcWidths[i] + 'px';
                        col.style.width = frcWidths[i] + 'px';
                    }

                    if (oldColGroup)
                        oldColGroup.parentNode.replaceChild(colGroup, oldColGroup);
                    else table.insertBefore(colGroup, table.firstChild);
                }

                var headCellWidths = Array.prototype.map.call(
                    headCells,
                    function(n) {
                        return n.clientWidth;
                    });

                var footCellWidths = Array.prototype.map.call(
                    footCells,
                    function(n) {
                        return n.clientWidth;
                    });

                /* copy head col widths from duplicate header */
                for (var i = 0; i < headCellWidths.length; i++) {
                    dupeHeadCells[i].style.minWidth = headCellWidths[i] + 'px';
                    dupeHeadCells[i].style.maxWidth = headCellWidths[i] + 'px';
                }

                /* copy foot col widths from duplicate footer */
                if (footCells.length > 0)
                    for (var i = 0; i < footCellWidths.length; i++) {
                        dupeFootCells[i].style.minWidth = footCellWidths[i] + 'px';
                        dupeFootCells[i].style.maxWidth = footCellWidths[i] + 'px';
                    }
            }

            function attachScrollHandler(wrapper) {
                wrapper.addEventListener('scroll', function() {
                    dupeHeadTable.parentNode.scrollLeft = wrapper.scrollLeft;
                    dupeFootTable.parentNode.scrollLeft = wrapper.scrollLeft;
                })
            }

            /* hide duplicate header */
            setTimeout(function() {
                if (dupeHead) {
                    head.style.display = 'none';
                    table.style.marginTop = '-1px';
                    dupeHeadTable.style.marginBottom = '0';
                }
                if (dupeFoot) {
                    foot.style.display = 'none';
                    table.style.marginBottom = '0';
                    table.style.borderBottom = '0px';
                    dupeFootTable.style.marginBottom = '0';
                }
                recalcScrollHeight.call(self, table, dupeHeadTable, dupeFootTable, conf.fixedHeaderSizing.sizeFromWindow);
            }, 0) /* hiding instantly broke scrolling when init'ing the first time on landscape ipad */

            attachScrollHandler(table.parentNode);
        };

        // Exec table renderer
        var dv = this;
        dust.render(id+'_table',
            base.push({ id : id, data : data, columns : conf.columns }),
            function(err, out) {
                if (out) {
                    if (conf.fixedHeaderSizing)
                        out = '<div>'+out+'</div>';

                    // Attach rendered output
                    var target = getDOMTarget.call(dv);
                    target.insertAdjacentHTML('afterbegin',out);

                    var table = target.querySelector('#'+dv.id),
                        tbody = table.querySelector('tbody');
                    attachAutoListener(tbody);
                    attachData();

                    if (conf.fixedHeaderSizing)
                        applyFixedHeaderSizing(target);

                    // Apply filter events after fixed header manipulations
                    attachFilterListener(target.querySelector('thead'), tbody, dv);
                    raiseEvent(table, 'construct', { table : table });
                }
                else throw Error(err);
            }
        )
    }

    function getDOMTarget() {
        return document.querySelector(this.renderConf.target);
    }

    function getTableElement() {
        return document.getElementById(this.id);
    }

    function recalcScrollHeight(table, dupeHeadTable, dupeFootTable, sizeFromWindow) {
        if (table === undefined) table = document.getElementById(this.id);
        if (dupeHeadTable === undefined) dupeHeadTable = getDOMTarget.call(this).firstChild;
        if (dupeFootTable === undefined) dupeFootTable = getDOMTarget.call(this).lastChild;
        if (sizeFromWindow === undefined) sizeFromWindow = this.renderConf.fixedHeaderSizing.sizeFromWindow;

        var bodyDivWrapper = table.parentNode,
            headHeight = dupeHeadTable.offsetHeight,
            footHeight = dupeFootTable.offsetHeight,
            maxHeight = sizeFromWindow ? window.innerHeight : bodyDivWrapper.parentNode.clientHeight,
            fullHeight = maxHeight - headHeight - footHeight;

        /* set height to full visible size of parent */
        if( isNumber(fullHeight) ) {
            bodyDivWrapper.style.height = fullHeight + 'px';
            bodyDivWrapper.style.overflow = 'auto';
        }
    }

    function processFilters() {
        var condition =
            filterCri.map(function(cri) {
                return function(data) {
                    return (data[cri.prop] === cri.value);
                }
            }).reduce(function(prev, cur) {
                return function(data) {
                    return prev(data) && cur(data);
                }
            }, function() {return true;});

        Array.prototype.forEach.call(this.getRowElementList(), function(row) {
            if (!condition(row.data))
                row.classList.add('dv-filtered');
            else
                row.classList.remove('dv-filtered');
        });

        raiseEvent(getTableElement.call(this), 'filter', { rows : this.getFilteredRowElementList()});
    }





    // Private global methods - could be moved to other files
    function dustCheck() { return window['dust'] ? true : false; }

    function isNumber(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    }

    function getDustBase() {
        return dust.makeBase({
            auto: function(chunk, ctx, bodies, params) {
                if (!params) params = {};
                // if no explicit property name, see if a prop name variable exists, as in a col context
                var propName = params.propName ? params.propName : ctx.get('name');

                // written as onmouseover to bind onchange to avoid Chrome error with onchange
                // https://code.google.com/p/chromium/issues/detail?id=131574
                chunk.write('<span class="dv-auto" data-prop="'+propName+'">');
                chunk = chunk.render( bodies.block, ctx );
                chunk.write('</span>')
            },

            filter: function(chunk, ctx, bodies, params) {
                if (!params) params = {};
                // if no explicit property name, see if a prop name variable exists, as in a col context
                var propName = params.propName ? params.propName : ctx.get('name');

                chunk.write('<span class="dv-filter" data-prop="'+propName+'">');
                chunk = chunk.render( bodies.block, ctx );
                chunk.write('</span>')
            },

            addVar : function(chunk, context, bodies, params) {
                var val = params.obj[params.prop];

                context.stack.head[params.var] = val;
                var out = chunk.render( bodies.block, context );
                delete context.stack.head[params.var];
                return out;
            }
        })
    }

    function matches(elem, selector) {
        var impl = elem.webkitMatchesSelector || elem.msMatchesSelector || elem.mozMatchesSelector;
        if( impl && impl.bind ){
            return (impl.bind(elem))(selector);
        }
        else{
            return Array.prototype.indexOf.call(document.querySelectorAll(selector), elem) > -1;
        }
    }

    function getValue(elem) {
        // getValue is only for abstracting the extraction of a js object out of our component instances

        // converting or validating that object should be done in respective methods

        if (elem.type === 'checkbox') return elem.checked;
        else return elem.value;
    }

    function convertValue(expected, value) {
        var out = value;
        // Basic type conversion
        if (typeof expected === 'boolean')
            out = value === true || value === 'true' ? true : value === false || value === 'false' ? false : undefined;
        else if (isNumber(expected))
            out = parseFloat(value);

        return out;
    }

    function processAnimation(elem, anim) {
        if (anim.init) anim.init(elem);
        var elapsed = 0;

        var start = new Date().getTime();
        var stepper = setInterval(function() {
            elapsed = new Date().getTime() - start;
            anim.step(elem, elapsed, anim.length);
        }, anim.stepFreq);

        setTimeout(function() {
            clearInterval(stepper);
        }, anim.length + 1);
    }

    function raiseEvent(src, name, args) {
        args.bubbles = true;
        args.cancelable = true;
        var event = new CustomEvent(name, args);
        src.dispatchEvent(event);
    }





    // Private constant values
    var fadeOutAnim = {
            step:function(elem, elapsed, length) {
                elem.style.opacity = 1 - elapsed / length;
            },
            length:1000,
            stepFreq:50,
            init:function(elem) {}
        },
        fadeInAnim = {
            step:function(elem, elapsed, length) {
                elem.style.opacity = elapsed / length;
            },
            length:1000,
            stepFreq:50,
            init:function(elem) {
                elem.style.opacity = 0;
            }
        },
        defaultTemplates = {
            head        : '<thead class="dv-head"><tr>{#columns}{>"{id}_{.name}_headcell" /}{/columns}</tr></thead>',
            foot        : '<tfoot class="dv-foot"><tr>{#columns}{>"{id}_{.name}_footcell" /}{/columns}</tr></tfoot>',

            headcell    : '<th class="{.name}">{.headerText}</th>',

            footcell    : '<td class="{.name}">{.footerText}</td>',

            bodycell    : '<td class="{.name}">{.value}</td>',

            row         : '<tr>' +
                            '{#columns}' +
                                '{#addVar var="value" obj=rowData prop=.name}' +
                                    '{>"{id}_{.name}_bodycell" /}' +
                                '{/addVar}' +
                            '{/columns}' +
                          '</tr>',

            body        : '<tbody class="dv-body">' +
                            '{#data}' +
                                '{>"{id}_row" rowData=./}' +
                            '{:else}' +
                                'No Data!' +
                            '{/data}' +
                          '</tbody>',

            table       : '<table id="{id}">' +
                            '{>"{id}_head"/}' +
                            '{>"{id}_body"/}' +
                            '{>"{id}_foot"/}' +
                          '</table>',

            detail : '{data}',
            rowdetail : '<tr class="dv-detail"><td>{>"{id}_detail"/}</td></tr>',
            globaldetail : '<div class="dv-detail">{>"{id}_detail"/}</div>'
        };





    // 'static' dataView interface
    ice.DataView.autoUpdate = function(elem, event) {
        var src = event.target,
            propName = elem.getAttribute('data-prop'),
            val = getValue(src),
            row = (function(){
                var parent = elem;
                while (parent = parent.parentNode)
                    if (matches(parent, 'tr'))
                        return parent;

            })();

        val = convertValue(row.data[propName], val);
        // validateValue();

        if (val != undefined) row.data[propName] = val;

        raiseEvent(row, 'update', { propName : propName, value : row.data[propName] });
    };





    // Exposed instanced dataView interface
    (function(dvProto) {
        // Create ---------------- //
        /**
         * Add a new row to the end of the rows object or before the given target object, if target is given. Renders that new row and raises the 'addrow' event following.
         * @param {Object} data row data object
         * @param {Object} [target] row index, data object or tr element to insert new row before
         * @fires addrow
         */
        dvProto.addRow = function(data, target) {
            var dv = this;
            dust.render(this.id+'_row',
                getDustBase().push({ id : this.id, rowData : data, columns : this.renderConf.columns }),
                function(err, out) {
                    // Create row
                    var rowHtml = out,
                        row,
                        curRow = dv.getRowElement(target);

                    // Insert row
                    if (curRow) {
                        curRow.insertAdjacentHTML('beforebegin', rowHtml);
                        row = curRow.previousSibling;
                    }
                    else {
                        var tbody = dv.getTBodyElement();
                        tbody.insertAdjacentHTML('beforeend', rowHtml);
                        row = tbody.lastChild;
                    }

                    // Attach data
                    row.data = data;

                    raiseEvent(row,  'addrow', {row : row});
                }
            )
        };

        /**
         * Attach another array of row objects, replacing any current model.
         * @param {Object[]} data
         */
        dvProto.attachData = function(data) {
            // TODO: just rebuild tbody...?
            construct.call(this, data);
        };

        /**
         * Attach a rendering configuration, replacing any current config.
         * @param {Object} config
         */
        dvProto.attachConfig = function(config) {
            this.renderConf = config;
            reconstruct.call(this);
        };

        /**
         * Get the list of row objects representing the current rendering.
         * @returns {Object[]}
         */
        dvProto.getRowDataList = function() {
            return Array.prototype.map.call(getRowElementList(), function(x) {
                return x.data;
            });
        };

        /**
         * Get the NodeList of table row elements of the current rendering.
         * @returns {NodeList}
         */
        dvProto.getRowElementList = function() {
            return this.getTBodyElement().childNodes;
        };

        /**
         * Get the NodeList of table row elements of the current rendering visible after filtering.
         * @returns {NodeList}
         */
        dvProto.getFilteredRowElementList = function() {
            return Array.prototype.filter.call(this.getRowElementList(), function(row) {
                return row.classList.contains('dv-filtered') >= 0;
            });
        };

        /**
         * Get the list of row objects of the current rendering visible after filtering.
         * @returns {Object[]}
         */
        dvProto.getFilteredRowDataList = function() {
            return Array.prototype.map.call(getRowElementList(), function(x) {
                return x.data;
            });
        };

        /**
         * Get the row object of the given index, matching predicate, table row,
         * or equal row object.
         * @param {Number | Function | HTMLElement | Object}target
         * @returns {Object}
         */
        dvProto.getRowData = function(target) {
            return this.getRowElement.call(this, target).data;
        };

        /**
         * Get the row element of the given index, matching predicate, row object,
         * or equal table row element.
         * @param {Number | Function | HTMLElement | Object}target
         * @returns {HTMLElement}
         */
        dvProto.getRowElement = function(target) {
            if (target == undefined) return;

            if (isNumber(target)) {
                /* Get row at index */
                return this.getTBodyElement().childNodes[target];
            } else if ({}.toString.call(target) === '[object Function]') {
                /* Get row by predicate */
                var rows = this.getTBodyElement().childNodes;
                for (var idx in rows) {
                    var row = rows[idx];
                    if (target(row.data)) {
                        return row;
                    }
                }
            } else {
                /* Get tr if object equals rowData*/
                var rows = this.getTBodyElement().childNodes,
                    targetJSON = JSON.stringify(target);

                for (var idx in rows) {
                    var row = rows[idx];
                    /* not the most robust equality, but considering we took JSON as input it should work */
                    if (targetJSON===JSON.stringify(row.data)) {
                        return row;
                    }
                }
            }
        };

        /**
         * Return true if the current set of row objects contains the given row object.
         * @param rowData row object
         * @returns {boolean}
         */
        dvProto.containsRowData = function(rowData) {
            /* Get row by equality */
            var rows = this.getTBodyElement().childNodes,
                targetJSON = JSON.stringify(target);

            for (var idx in rows) {
                var row = rows[idx];
                /* not the most robust equality, but considering we took JSON as input it should work */
                if (targetJSON===JSON.stringify(row.data)) {
                    return true;
                }
            }

            return false;        };

        /**
         * Get the table body element of this component.
         * @returns {HTMLElement}
         */
        dvProto.getTBodyElement = function() {
            return document.querySelector('#'+ this.id +' > tbody');
        };

        // Update ---------------- //
        /**
         * Allow the application to replace the properties of a target row object with those defined in a given object.
         * Raises the update event for each altered property.
         * @param {Number | Function | HTMLElement | Object}target
         * @param {Object}input
         * @fires update
         */
        dvProto.updateRowData = function(target, input) {
            var data = this.getRowData(target);

            if (!data) throw Error('Update failed: Row cannot be found or no row data attached to row.');

            for (var prop in input) {
                data[prop] = input[prop];
                raiseEvent(target, 'update', {propName : prop, value:input[prop]});
            }
        };

        // Delete ---------------- //
        /**
         * Remove the row object defined by the target.
         * @param {Number | Function | HTMLElement | Object}target
         * @fires removerow
         */
        dvProto.removeRow = function(target) {
            var elem = target instanceof HTMLElement ? target : this.getRowElement(target),
                old = elem.parentNode.removeChild(elem);
            raiseEvent(document.getElementById(this.id), 'removerow', { row : old });
        };

        /**
         * Detach the entire set of row objects, clearing the table body of elements.
         */
        dvProto.detachData = function() {
            this.getTBodyElement().innerHTML = '';
        };

        /**
         * Remove the entire component.
         */
        dvProto.destroy = function() {
            // Unbind events TODO

            // Remove DOM elem
            var table = this.getTBodyElement().parent;
            table.parent.removeChild(table);
        };

        // Activation ---------------- //
        /**
         * Render the details region of the given row object. This renders the
         * details template with that row object and inserts the resulting HTML
         * at the location defined by detailsPosition. When "row", the details
         * region is rendered in a new row following the activated row. When "global"
         * the details region is rendered above the table.
         * @param {Number | Function | HTMLElement | Object}target
         */
        dvProto.activateDetail = function(target) {
            var row, data, detailTemplate, conf = this.renderConf,
                position = (conf.detailPosition) ? conf.detailPosition : 'row',
                table = this,
                activateAnim = this.renderConf.animations.deactivate
                        ? this.renderConf.animations.deactivate
                        : fadeInAnim;

            if (target instanceof HTMLElement) {
                row = target;
                data = row.data;
            } else {
                data = target;
                row = this.getRowElement(data);
                if (!row) {
                    throw Error('No row with given data to activate.');
                }
            }

            if (row.classList.contains('dv-active')) {
                console.log('Row to activate was already active.');
                return;
            }

            // If using a global region deactivate any current active rows
            if (position === 'global') this.deactivateDetail();

            function writeDetail(contents) {
                var detail;
                // Insert detail region at config'd position
                if (position === 'row') {
                    row.insertAdjacentHTML('afterend', contents);
                    detail = row.nextSibling;
                    detail.firstChild.setAttribute('colspan', table.renderConf.columns.length);
                    detail.data = data;
                    detail.className = 'dv-detail';
                    row.classList.add('dv-active')
                } else if (position === 'global') {
                    document.querySelector(conf.target).insertAdjacentHTML('afterbegin', contents);

                    var wrapper = row.parentNode.parentNode.parentNode;
                    detail = document.querySelector(conf.target).querySelector('.dv-detail');
                    var cstyle = window.getComputedStyle(detail);

                    wrapper.style.height = (parseFloat(wrapper.style.height) - detail.offsetHeight - parseFloat(cstyle.marginTop) - parseFloat(cstyle.marginBottom)) + 'px';
                }
                processAnimation(detail, activateAnim);
                raiseEvent(detail, 'activate', { row : row});
            }

            // Build template for detail type
            var templates = {
                detail      : conf.templates.detail ? conf.templates.detail : defaultTemplates.detail,
                rowdetail   : conf.templates.rowdetail ? conf.templates.rowdetail : defaultTemplates.rowdetail,
                globaldetail: conf.templates.globaldetail ? conf.templates.globaldetail : defaultTemplates.globaldetail
            }

            dust.loadSource(dust.compile(templates.detail, this.id+'_detail'));

            switch (position) {
                case 'row':
                    detailTemplate = this.id+'_rowdetail';
                    dust.loadSource(dust.compile(templates.rowdetail, detailTemplate));
                    break;
                case 'global':
                    detailTemplate = this.id+'_globaldetail';
                    dust.loadSource(dust.compile(templates.globaldetail, detailTemplate));
                    break;
                default:
                    throw Error('Invalid detail position defined.');
            }

            // Render detail region contents
            dust.render(detailTemplate, getDustBase().push({ id : this.id, data : data, columns : conf.columns }),
                function(err, out) {
                    if (err) throw Error(err);
                    else writeDetail(out);
                });
        };

        /**
         * Removes the detail region of the given row object, or if none is
         * defined removes all detail regions.
         * @param {Number | Function | HTMLElement | Object}[target]
         */
        dvProto.deactivateDetail = function(target) {
            var table = this,
                position = (this.renderConf.detailPosition) ? this.renderConf.detailPosition : 'row',
                deactivateAnim = this.renderConf.animations.deactivate
                        ? this.renderConf.animations.deactivate
                        : fadeOutAnim;

            function removeGlobalDetail() {
                target.parentNode.removeChild(target);
                if (table.renderConf.fixedHeaderSizing) recalcScrollHeight.call(table);
                raiseEvent(target, 'deactivate', { row : table.getRowElement(target.data) });
            }

            function removeRowDetail() {
                var detail = target.nextSibling;
                detail.parentNode.removeChild(detail);
                raiseEvent(target, 'deactivate', { row : target });
            }

            function removeEveryRowDetail() {
                Array.prototype.filter.call(table.getRowElementList(), function(row) {
                    return row.classList.contains('dv-active');
                }).forEach(function(row) {
                    table.deactivateDetail(row);
                });
            }

            if (position === 'global') {
                target = document.querySelector(this.renderConf.target).querySelector('.dv-detail');
                if (target) {
                    processAnimation(target, deactivateAnim);
                    setTimeout(removeGlobalDetail, deactivateAnim.length + 10);
                }
            } else if (position === 'row') {
                if (target) {
                    if (!(target instanceof HTMLElement)) target = this.getRowElement(target);

                    if (target.classList.contains('dv-active')) {
                        target.classList.remove('dv-active');
                        processAnimation(target.nextSibling, deactivateAnim);
                        setTimeout(removeRowDetail, deactivateAnim.length + 10);
                    } else {
                        console.log('Row to deactivate was not active.');
                    }
                } else {
                    removeEveryRowDetail();
                }
            }
        };

        // Filter ---------------- //
        /**
         * Filter the row objects of the table based on an exact match of the
         * value of the row object for the given property, and the value given.
         * Replaces the filter value if that property name is already filtered.
         * @param propName row object property name
         * @param value filter value
         * @fires filter
         */
        dvProto.addFilter = function(propName, value) {
            var existing = (function() {
                for (var idx in filterCri) {
                    if (filterCri[idx].prop === propName)
                        return filterCri[idx];
                }
                return undefined;
            })();

            if (existing) existing.value = value;
            else filterCri.push({prop:propName, value: value});

            processFilters.call(this);
        };

        /**
         * Remove the filtering on the given row object property.
         * @param propName row object property name
         * @fires filter
         */
        dvProto.removeFilter = function(propName) {
            filterCri = filterCri.filter(function(cri) {
                return cri.prop != propName;
            });
            processFilters.call(this);
        };

        /**
         * Remove filtering from all row object properties.
         */
        dvProto.clearFilters = function() {
            filterCri = [];
            processFilters.call(this);
        };
    })(ice.DataView.prototype);
})('mobi');
