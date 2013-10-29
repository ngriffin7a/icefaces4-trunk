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

if (!ice.ace.ListControls) ice.ace.ListControls = {};

ice.ace.ListControl = function(id, cfg) {
    this.id = id;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.cfg = cfg;
    this.element = ice.ace.jq(this.jqId);
    this.selector = cfg.selector;
    this.sep = String.fromCharCode(this.cfg.separator);

    // If control hasn't been built before setup clean up callback
    if (!ice.ace.ListControls[id]) {
        var self = this;
        ice.onElementUpdate(this.id, function() { self.unload(); });
    }

    // global list of list control objects, used by lists to
    // find lists they share migration controls with, in order
    // to enforce mutually exclusive selection
    ice.ace.ListControls[id] = this;

    this.setupControls();
};

ice.ace.ListControl.prototype.unload = function() {
    delete ice.ace.ListControls[this.id];

    var selector;

    if (this.element.hasClass('if-list-nctrls'))
        selector = '> .if-list-ctrl-spcr > .if-list-nctrl';
    else
        selector = '> .if-list-nctrls .if-list-nctrl, > .if-list-dl > .if-list-nctrls .if-list-nctrl';

    this.element.find(selector).off('mouseenter mouseleave click');
}

ice.ace.ListControl.prototype.setupControls = function(){
    var self = this, selector;

    if (this.element.hasClass('if-list-nctrls'))
        selector = '> .if-list-ctrl-spcr > .if-list-nctrl';
    else
        selector = '> .if-list-nctrls .if-list-nctrl, > .if-list-dl > .if-list-nctrls .if-list-nctrl';

    this.element.find(selector)
        .off('mouseenter').on('mouseenter', function(e) {
            var ctrl = e.currentTarget;
            ice.ace.jq(ctrl).addClass('ui-state-hover');
        })
        .off('mouseleave').on('mouseleave', function(e) {
            var ctrl = e.currentTarget;
            ice.ace.jq(ctrl).removeClass('ui-state-hover');
        })
        .off('click').on('click', function(e) { self.controlClickHandler.call(self, e); });
};


ice.ace.ListControl.prototype.refreshLists = function() {
    this.lists = ice.ace.jq(this.selector);
};

ice.ace.ListControl.prototype.controlClickHandler = function(e) {
    var ctrl = e.currentTarget,
        jqCtrl = ice.ace.jq(ctrl),
        dir,
        all = false;

    this.refreshLists();

    if (!ice.ace.jq.browser.msie || ice.ace.jq.browser.version == 9) {
        jqCtrl.toggleClass('ui-state-active', 50)
        .toggleClass('ui-state-active', 50);
    }

    if (jqCtrl.hasClass('if-list-nctrl-alll')) {
        dir = "alll";
        all = true;
    }
    else if (jqCtrl.hasClass('if-list-nctrl-lft'))
        dir = "lft";
    else if (jqCtrl.hasClass('if-list-nctrl-rgt'))
        dir = "rgt";
    else if (jqCtrl.hasClass('if-list-nctrl-allr')) {
        dir = "allr";
        all = true;
    }

    var from = this.getSourceList(dir, all);
    if (!from) return;

    var to = this.getDestinationList(from, dir);
    if (!to) return;


    var im = [];
    im.push(from.id);
    im.push(this.getRecords(from, to, all));

    from.element.find('> ul > li').removeClass('if-list-last-clicked');
    to.element.find('> ul > li').removeClass('if-list-last-clicked');

    to.immigrantMessage = im;
    to.sendMigrateRequest();
};

ice.ace.ListControl.prototype.getSourceList = function(dir, all) {
    // If we are moving all the elements and this is dual list mode
    if (all && this.element.hasClass('if-list-dl-cnt')) {
        var list;
        if (dir.substr(dir.length-1) == 'r')
            list = this.element.find(' > div.if-list-dl > span.if-list-dl-1 > div > div.if-list');
        else
            list = this.element.find(' > div.if-list-dl > span.if-list-dl-2 > div > div.if-list');

        return ice.ace.Lists[list.attr('id')];
    }

    // Return first list in selector that has a selected row
    var activeList = this.lists.find('.if-list-item.ui-state-active:first');
    if (activeList.length > 0)
        return ice.ace.Lists[ activeList.closest('.if-list').attr('id') ];
    else
        return undefined;
};

ice.ace.ListControl.prototype.getDestinationList = function(source, dir) {
    var sourceIndex = this.lists.index(source.element);

    if (dir == 'allr' || dir == 'rgt') {
        if (sourceIndex != (this.lists.length-1))
            return ice.ace.Lists[ice.ace.jq(this.lists[sourceIndex+1]).attr('id')];
    }
    else
        if (sourceIndex != 0)
            return ice.ace.Lists[ice.ace.jq(this.lists[sourceIndex-1]).attr('id')];

    return undefined;
};

ice.ace.ListControl.prototype.getRecords = function(source, dest, all) {
    var childSelector = all ? '*' : '.ui-state-active' ,
        sourceChildren = ice.ace.jq(source.element).find('> ul.if-list-body').children(),
        sourceIds = sourceChildren.filter(childSelector).map(function() { return ice.ace.jq(this).attr('id'); }),
        sourceLength = sourceChildren.length,
        sourceReorderings = source.read('reorderings'),
        records = [],
        destIndex = ice.ace.jq(dest.element).find('> ul.if-list-body').children().length;

    for (var i = 0; i < sourceIds.length; i++) {
        var record = [], id = sourceIds[i];
        record.push(source.getUnshiftedIndex(sourceLength, sourceReorderings, parseInt(id.substr(id.lastIndexOf(this.sep)+1))));
        record.push(destIndex + i);
        records.push(record);
    }

    return records;
};
