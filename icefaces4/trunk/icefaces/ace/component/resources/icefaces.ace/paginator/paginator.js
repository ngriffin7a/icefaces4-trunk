ice.ace.DataTable.Paginator = function(table) {
    var labels = {} ;
    var cfg = table.cfg.paginator,
        container = ice.ace.jq(table.jqId + ' > .ui-paginator'),
        template = cfg.template || "{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}",
        activeIndex = cfg.initialPage,
    // Maximum number of pages given total row count, if rowsPerPage zero set to 1.
        max = cfg.rowsPerPage == 0 || cfg.totalRecords < cfg.rowsPerPage ? 1 : Math.ceil(cfg.totalRecords / cfg.rowsPerPage);
        labels.first = cfg.firstLbl,
        labels.last = cfg.lastLbl,
        labels.next = cfg.nextLbl,
        labels.prev = cfg.prevLbl,
    this.container = container;
    function initPageMarkup() {
        function getTemplateControlMarkup(keyword) {
            var markup = '';

            if (keyword == 'currentPageReport') {
                markup = '<span class="ui-paginator-current">('+activeIndex+' of '+ max +')</span>';
            }
            else if (keyword == 'firstPageLink') {
                var className = 'ui-paginator-first ui-state-default ui-corner-all';
                if (activeIndex == 1) className += ' ui-state-disabled';
                markup = '<a href="#" class="'+className+'"><span class="ui-icon ui-icon-seek-first">'+labels.first+'</span></a>';
            }
            else if (keyword == 'lastPageLink') {
                var className = 'ui-paginator-last ui-state-default ui-corner-all';
                if (activeIndex == max) className += ' ui-state-disabled';
                markup = '<a href="#" class="'+className+'"><span class="ui-icon ui-icon-seek-end">'+labels.last+'</span></a>';
            }
            else if (keyword == 'previousPageLink') {
                var className = 'ui-paginator-previous ui-state-default ui-corner-all';
                if (activeIndex == 1) className += ' ui-state-disabled';
                markup = '<a href="#" class="'+className+'"><span class="ui-icon ui-icon-seek-prev">'+labels.prev+'</span></a>';
            }
            else if (keyword == 'nextPageLink') {
                var className = 'ui-paginator-next ui-state-default ui-corner-all';
                if (activeIndex == max) className += ' ui-state-disabled';
                markup = '<a href="#" class="'+className+'"><span class="ui-icon ui-icon-seek-next">'+labels.next+'</span></a>';
            }
            else if (keyword == 'rowsPerPageDropdown' && cfg.rowsPerPageOptions) {
                markup = '<select class="ui-paginator-rpp-options" title="Rows per page">';

                for (var i = 0; i < cfg.rowsPerPageOptions.length; i++) {
                    var value = cfg.rowsPerPageOptions[i],
                        label = value;

                    if (value.text) {
                        value = cfg.rowsPerPageOptions[i].value;
                        label = cfg.rowsPerPageOptions[i].text;
                    }

                    var selected = value == cfg.rowsPerPage ? 'selected' : '';
                    markup += '<option '+selected+' value="'+value+'">'+label+'</option>';
                }
                markup += '</select>';
            }
            else if (keyword == 'pageLinks') {
                markup = '<span class="ui-paginator-pages">';

                // Adjust start page forward if more pages exist than page buttons
                var startPage = activeIndex - (cfg.pageLinks / 2) > 1 && cfg.pageLinks < max ? activeIndex - Math.floor(cfg.pageLinks / 2) : 1;
                var extraLinks = activeIndex + Math.round(cfg.pageLinks / 2) - max - 1;

                // Adjust start page backward if we're at a current position where adjusting buttons indexes would point to out of range pages
                if (extraLinks > 0 && cfg.pageLinks < max)
                    startPage -= extraLinks;

                for (var i = startPage; i <= (cfg.pageLinks + startPage - 1) && ((i-1) * cfg.rowsPerPage < cfg.totalRecords); i++) {
                    if (i == activeIndex)
                        markup += '<span class="ui-paginator-page ui-state-default ui-corner-all ui-paginator-current-page ui-state-active">'+i+'</span>';
                    else
                        markup += '<a href="#" class="ui-paginator-page ui-state-default ui-corner-all">'+i+'</a>';

                    // Only render a single page when non-positive integer is given for row count
                    if (cfg.rowsPerPage < 1) break;
                }

                markup += '</span>';
            }

            return markup;
        }

        function encodePaginatorTemplate() {
            var keywords = ['currentPageReport', 'firstPageLink', 'lastPageLink',
                    'previousPageLink', 'nextPageLink', 'jumpToPageDropdown',
                    'pageLinks', 'rowsPerPageDropdown'],
                t = template;

            for (var i = 0; i < keywords.length; i++) {
                t = t.replace(
                    new RegExp('({'+keywords[i]+'})', 'gi'),
                    getTemplateControlMarkup(keywords[i])
                );
            }

            container.html(t);
        }

        encodePaginatorTemplate();
    }

    function initControlEvents() {
        function addHoverEvents(i, control) {
            control = ice.ace.jq(control);
            if (control.hasClass('ui-paginator-pages')) control.children().each(addHoverEvents);
            else {
                control.on('mouseenter', function(e) {
                    ice.ace.jq(this).addClass('ui-state-hover');
                });

                control.on('mouseleave', function(e) {
                    ice.ace.jq(this).removeClass('ui-state-hover');
                });
            }
        };

        function addClickEvents(i, control) {
            function clickListener(e) {
                e.preventDefault();
                var control = ice.ace.jq(this);

                if (control.hasClass('ui-paginator-page'))
                    pageLinkClick(control);
                else if (control.hasClass('ui-paginator-first')
                    || control.hasClass('ui-paginator-next')
                    || control.hasClass('ui-paginator-last')
                    || control.hasClass('ui-paginator-previous'))
                    directionLinkClick(control);
                else alert(control[0].className);
            }

            function rowsPerPageChangeListener(e) {
                e.preventDefault();
                cfg.rowsPerPage = parseInt(e.currentTarget.children[e.currentTarget.selectedIndex].value);
                submit({ rowChangeEvent : true});
            }

            function pageLinkClick(control) {
                var oldIndex = activeIndex;
                activeIndex = parseInt(control.html());

                if (oldIndex != activeIndex) submit();
            }

            function directionLinkClick(control) {
                var oldIndex = activeIndex;

                if (control.hasClass('ui-paginator-first'))
                    activeIndex = 1;
                if (control.hasClass('ui-paginator-previous') && activeIndex > 1)
                    activeIndex--;
                if (control.hasClass('ui-paginator-next') && activeIndex < max)
                    activeIndex++;
                if (control.hasClass('ui-paginator-last'))
                    activeIndex = max;

                if (oldIndex != activeIndex) submit();
            }

            control = ice.ace.jq(control);
            if (control.hasClass('ui-paginator-pages')) control.children().each(addClickEvents);
            else {
                if (control.hasClass('ui-paginator-rpp-options'))
                    control.on('change', rowsPerPageChangeListener);
                else
                    control.on('click', clickListener);
            }
        };

        container.children().filter(':not(.ui-paginator-current, .ui-paginator-rpp-options)').each(addHoverEvents);
        container.children().filter(':not(.ui-paginator-current)').each(addClickEvents);
    }

    function initDisabledStyling() {

    }

    function getState() {
        return {
            rowsPerPage : cfg.rowsPerPage,
            page : activeIndex
        };
    }

    function submit(conf) {
        if (conf == undefined) conf = {};
        var newState = getState(),
            rowChangeEvent = conf.rowChangeEvent;


        var options = {
            source:table.id,
            render:table.id,
            execute:table.id,
            formId:table.cfg.formId
        };

        var _self = table;
        options.onsuccess = function (responseXML) {
            if (table.cfg.scrollable) table.resizeScrolling();
        };

        var params = {};
        params[table.id + "_paging"] = true;
        params[table.id + "_rows"] = newState.rowsPerPage;
        params[table.id + "_page"] = newState.page;

        options.params = params;


        if (table.behaviors && table.behaviors.page && !rowChangeEvent) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                table.behaviors.page,
                ice.ace.clearExecRender(options)
            ));
            return;
        } else if (table.behaviors && table.behaviors.rowsPerPage && rowChangeEvent) {
            ice.ace.ab(ice.ace.extendAjaxArgs(
                    table.behaviors.rowsPerPage,
                    ice.ace.clearExecRender(options)
            ));
            return;
        }

        ice.ace.AjaxRequest(options);
    }


    if (cfg.alwaysVisible == false && cfg.totalRecords < cfg.rowsPerPage) {
        container.css('display','none');
    } else {
        initPageMarkup();
        if (!cfg.disabled) initControlEvents();
        else initDisabledStyling();
    }
}

ice.ace.DataTable.Paginator.prototype.destroy = function() {
    function removeEvents(container) {
        container.children().each(function(i, child) {
            child = ice.ace.jq(child);
            if (child.hasClass('ui-paginator-pages')) removeEvents(child);
            else child.off('click change mouseenter mouseleave');
        });
    }

    removeEvents(this.container);
}