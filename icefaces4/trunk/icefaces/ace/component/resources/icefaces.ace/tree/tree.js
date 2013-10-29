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

ice.ace.Tree = function (clientId, cfg) {
    cfg.id = clientId;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(cfg.id);
    this.element = ice.ace.jq(this.jqId);
    this.behaviors = cfg.behaviors;

    // Selectors
    this.expansionButtonDeselector = this.jqId + " * .if-tree * .if-node-sw, noexp";
    this.selectionTargetDeselector = this.jqId + " * .if-tree * .if-node, noselect";
    this.expansionButtonSelector = ".if-node-sw:not("+this.expansionButtonDeselector+")";
    this.selectionTargetSelector = ".if-node:not("+this.selectionTargetDeselector+")";
    this.nodeWrapperSelector = this.selectionTargetSelector + " > div.if-node-wrp";
    this.sortableTarget = '.if-node-sub';

    // Setup events
    // Expansion
    if (this.cfg.expansion) {
        this.tearDownExpansion();
        this.setupExpansion();
    }

    // Selection
    if (this.cfg.selection) {
        this.tearDownSelection();
        this.setupSelection();
    }

    // Reordering
    if (this.cfg.reorder) {
        this.tearDownReordering();
        this.setupReordering();
    }

    // Cleanup
    if (!window[this.cfg.widgetVar]) {
        var self = this;
        ice.onElementUpdate(cfg.id, function() { self.unload(); });
    }
};

ice.ace.Tree.prototype.unload = function() {
    this.tearDownReordering();
    this.tearDownSelection();
    this.tearDownExpansion();
};

ice.ace.Tree.prototype.tearDownExpansion = function() {
    this.element.off('click', this.expansionButtonSelector);
};

ice.ace.Tree.prototype.setupExpansion = function() {
    var self = this;
    this.element.on('click', this.expansionButtonSelector, function (event) {
        var container = ice.ace.jq(this),
            icon = container.find('> div > span.ui-icon'),
            expanded = icon.is('.ui-icon-minus'),
            node = container.closest('.if-node-cnt');

        if (self.cfg.expansionMode == 'server') {
            if (expanded)
                self.sendNodeContractionRequest(node);
            else
                self.sendNodeExpansionRequest(node);
        } else {
            if (expanded)
                self.doClientContraction(node);
            else
                self.doClientExpansion(node);
        }
    });
};

ice.ace.Tree.prototype.tearDownSelection = function() {
    this.element
            .off('click', this.selectionTargetSelector)
            .off('mouseenter', this.selectionTargetSelector)
            .off('mouseleave', this.selectionTargetSelector);
};

ice.ace.Tree.prototype.setupReordering = function () {
    var self = this;

    this.sortConfig = {
        connectWith:this.sortableTarget,

        receive:function (event, ui) {
            // Prevent bugged double submit
            if (!self.droppedItemSameParent(ui.item)) {
                var newParent = ice.ace.jq(this).closest('.if-node-cnt, .if-tree'),
                source = ice.ace.jq(ui.item),
                index = source.index();
                self.sendReorderingRequest(source, newParent, index);
            }
        },
        update:function (event, ui) {
            if (self.droppedItemSameParent(ui.item)) {
                var parent = ice.ace.jq(ui.item).parent().closest('.if-node-cnt, .if-tree'),
                    source = ice.ace.jq(ui.item);
                    index = source.index();
                self.sendReorderingRequest(source, parent, index, self.cfg.indexIds);
            }
        }
    }
	
	if (this.cfg.handle) this.sortConfig.handle = this.cfg.handle;

    this.element.find(this.sortableTarget).andSelf().sortable(this.sortConfig);
};

ice.ace.Tree.prototype.refreshSort = function(id) {
    ice.ace.jq(ice.ace.escapeClientId(id))
        .closest(this.sortableTarget).not('.ui-sortable').sortable(this.sortConfig);
}
ice.ace.Tree.prototype.rs = ice.ace.Tree.prototype.refreshSort;

ice.ace.Tree.prototype.reindexSiblings = function(source) {
    source.siblings().andSelf().each(function(i, val) {
        var tar = ice.ace.jq(val),
            oldid = tar.attr('id');
        oldid = oldid.substring(0, oldid.lastIndexOf(':'));
        var newid = oldid.substring(0, oldid.lastIndexOf(':')+1) + i;
        tar.attr('id', newid+':-');
        tar.find('*[id]').each(function(i, v) {
            var c = ice.ace.jq(v);
            c.attr('id', c.attr('id').replace(oldid, newid));
        });
    });
};


ice.ace.Tree.prototype.droppedItemSameParent = function(item) {
    var parent = item.parent().closest('.if-node-cnt, .if-tree'),
        parentid = parent.attr('id');

    parentid = parentid.substring(0, parentid.lastIndexOf(':-'));
    var childSize = this.getNodeKey(item).split(':').length - 1;
    var parentSize = parent.is('.if-tree') ? 0
            : this.getNodeKey(parent).split(':').length - 1;

    return item.is("[id^='"+parentid+"']") && (childSize - 1) == parentSize;
}

ice.ace.Tree.prototype.sendReorderingRequest = function(source, parent, index) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    },
    reorderKey = this.cfg.id+'_reorder',
    params = {};

    params[reorderKey] = this.getNodeKey(source)
            + '>' + this.getNodeKey(parent)
            + '@' + index;

    if (arguments[3])
        this.reindexSiblings(source);

    options.params = params;

    if (this.cfg.behaviors && this.cfg.behaviors['reorder']) {
        ice.ace.ab(ice.ace.extendAjaxArgs(
                this.cfg.behaviors['reorder'],
                ice.ace.clearExecRender(options)
        ));
    } else {
        ice.ace.AjaxRequest(options);
    }
}

ice.ace.Tree.prototype.tearDownReordering = function() {
    this.element.off("mouseover", this.sortableTarget);
    this.element.find(this.sortableTarget).sortable("destroy");
}

ice.ace.Tree.prototype.setupSelection = function() {
    var self = this;
    this.element.on('mouseenter', this.selectionTargetSelector, function(event) {
        var tar = ice.ace.jq(this),
            wrap = tar.find('> div.if-node-wrp'),
            selected = wrap.is('.ui-state-active');

        if (!selected) wrap.addClass('ui-state-hover');
    });

    this.element.on('mouseleave', this.selectionTargetSelector, function(event) {
        var tar = ice.ace.jq(this),
            wrap = tar.find('> div.if-node-wrp');

        wrap.removeClass('ui-state-hover');
    });

    this.element.on('click', this.selectionTargetSelector, function(event) {
        var tar = ice.ace.jq(this),
            wrap = tar.find('> div.if-node-wrp'),
            selected = wrap.is('.ui-state-active'),
            node = tar.closest('.if-node-cnt');

        if (self.cfg.selectionMode == 'server') {
            if (selected)
                self.sendNodeDeselectionRequest(node);
            else
                self.sendNodeSelectionRequest(node);
        } else {
            if (selected)
                self.doClientDeselection(node, wrap);
            else
                self.doClientSelection(node, wrap);
        }
    });
};

ice.ace.Tree.prototype.sendNodeDeselectionRequest = function(node) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    };

    this.append('deselect', this.getNodeKey(node));

    if (this.cfg.behaviors && this.cfg.behaviors['deselect']) {
        ice.ace.ab(ice.ace.extendAjaxArgs(
                this.cfg.behaviors['deselect'],
                ice.ace.clearExecRender(options)
        ));
    } else {
        ice.ace.AjaxRequest(options);
    }
};

ice.ace.Tree.prototype.sendNodeSelectionRequest = function(node) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    };

    this.append('select', this.getNodeKey(node));

    if (!this.cfg.multiSelect)
        this.deselectAll();

    if (this.cfg.behaviors && this.cfg.behaviors['select']) {
        ice.ace.ab(ice.ace.extendAjaxArgs(
                this.cfg.behaviors['select'],
                ice.ace.clearExecRender(options)
        ));
    } else {
        ice.ace.AjaxRequest(options);
    }
};

ice.ace.Tree.prototype.doClientDeselection = function(node, wrap) {
    var key = this.getNodeKey(node);

    wrap.removeClass('ui-state-active');

    this.append('deselect', key);
    this.remove('select', key);
};

ice.ace.Tree.prototype.doClientSelection = function(node, wrap) {
    var key = this.getNodeKey(node);

    if (!this.cfg.multiSelect)
        this.deselectAll();

    wrap.addClass('ui-state-active');

    this.append('select', key);
    this.remove('deselect', key);
};

ice.ace.Tree.prototype.doClientContraction = function(node) {
    var key = this.getNodeKey(node),
        icon = node.find(' > tbody > tr > td.if-node-sw > div > span.ui-icon'),
        sub = node.find(' > tbody > tr > td.if-node-sub');

    icon.removeClass('ui-icon-minus');
    icon.addClass('ui-icon-plus')

    sub.css('display', 'none');

    this.append('contract', key);
    this.remove('expand', key);
};

ice.ace.Tree.prototype.doClientExpansion = function(node) {
    var key = this.getNodeKey(node),
        icon = node.find('  > tbody > tr > td.if-node-sw > div  > span.ui-icon'),
        sub = node.find('  > tbody > tr > td.if-node-sub');

    icon.removeClass('ui-icon-plus');
    icon.addClass('ui-icon-minus')

    sub.css('display', 'block');

    this.append('expand', key);
    this.remove('contract', key);
};

ice.ace.Tree.prototype.sendNodeContractionRequest = function(node) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    };

    this.append('contract', this.getNodeKey(node));

    if (this.cfg.behaviors && this.cfg.behaviors['contract']) {
        ice.ace.ab(ice.ace.extendAjaxArgs(
                this.cfg.behaviors['contract'],
                ice.ace.clearExecRender(options)
        ));
    } else {
        ice.ace.AjaxRequest(options);
    }
};

ice.ace.Tree.prototype.sendNodeExpansionRequest = function(node) {
    var options = {
        source:this.cfg.id,
        execute:this.cfg.id,
        render:this.cfg.id
    };

    this.append('expand', this.getNodeKey(node));

    if (this.cfg.behaviors && this.cfg.behaviors['expand']) {
        ice.ace.ab(ice.ace.extendAjaxArgs(
                this.cfg.behaviors['expand'],
                ice.ace.clearExecRender(options)
        ));
    } else {
        ice.ace.AjaxRequest(options);
    }
};

ice.ace.Tree.prototype.deselectAll = function() {
    var self = this;
    this.element.find(this.nodeWrapperSelector+'.ui-state-active')
            .each(function() {
        var wrap = ice.ace.jq(this),
            node = wrap.closest('.if-node-cnt'),
            key = self.getNodeKey(node);

        wrap.removeClass('ui-state-active');

        self.append('deselect', key);
        self.remove('select', key);
    });
};

ice.ace.Tree.prototype.getNodeKey = function(node) {
    var startStr = this.cfg.id + ':-:',
        endStr = ':-',
        id = node.attr('id');

    // If we are trying to find the key of the root
    // return the component client id
    if (this.cfg.id == id)
        return id;

    var startIndex = id.indexOf(startStr) + startStr.length,
        endIndex = id.indexOf(endStr, startIndex);

    return id.substring(startIndex, endIndex);
};

ice.ace.Tree.prototype.write = function(key, val) {
    this.element.find(this.jqId+"_"+key).val(JSON.stringify(val));
};

ice.ace.Tree.prototype.read = function(key) {
    var val = this.element.find(this.jqId+"_"+key).val();
    if (val != "") return JSON.parse(val);
    else return [];
};

ice.ace.Tree.prototype.append = function(key, val) {
    var arr = this.read(key);
    arr.push(val);
    this.write(key, arr);
};

ice.ace.Tree.prototype.remove = function(key, val) {
    this.write(key, ice.ace.jq.grep(this.read(key),
        function (o) {
            return o != val;
        }
    ));
};
