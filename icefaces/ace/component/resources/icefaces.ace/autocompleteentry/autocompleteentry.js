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

if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};
if (!ice.ace.Autocompleters) ice.ace.Autocompleters = {};

ice.ace.Autocompleter = function(id, updateId, rowClass, selectedRowClass, delay, minChars, height, direction, behaviors, cfg, clientSideModeCfg, effects, placeholder) {
	this.id = id;
	var isInitialized = false;
	if (ice.ace.Autocompleters[this.id] && ice.ace.Autocompleters[this.id].initialized) isInitialized = true;
	this.showingList = false;
	if (isInitialized) this.showingList = ice.ace.Autocompleters[this.id].showingList;
	ice.ace.Autocompleters[this.id] = this;
	this.clientSideModeCfg = clientSideModeCfg;
	this.delay = delay;
	this.minChars = minChars;
	this.height = height == 0 ? 'auto' : height;
	this.direction = direction;
    this.cfg = cfg;
	var options = {minChars:0};
	this.root = ice.ace.jq(ice.ace.escapeClientId(this.id));
	var $element = this.root.find('input[name="'+this.id+'_input"]');
	this.element = $element.get(0);
	this.element.id = this.id + "_input";
	this.update = ice.ace.jq(ice.ace.escapeClientId(updateId)).get(0);
	this.effects = effects;
	if (placeholder && !('placeholder' in document.createElement('input'))) { // if 'placeholder' isn't supported, use label inField
		this.cfg.inFieldLabel = placeholder;
	}
	$element.data("labelIsInField", this.cfg.labelIsInField);
	
	if (isInitialized) {
		this.initialize(this.element, this.update, options, rowClass, selectedRowClass, behaviors);
	} else {
        if (ice.ace.jq.trim($element.val()) == "" && this.cfg.inFieldLabel) {
            $element.val(this.cfg.inFieldLabel);
            $element.addClass(this.cfg.inFieldLabelStyleClass);
            $element.data("labelIsInField", true);
        }
		var self = this;
		$element.on('focus', function() {
			$element.off('focus');
			if ($element.data("labelIsInField")) {
				$element.val("");
				$element.removeClass(self.cfg.inFieldLabelStyleClass);
				$element.data("labelIsInField", false);
				self.cfg.labelIsInField = false;
			}
			self.initialize(self.element, self.update, options, rowClass, selectedRowClass, behaviors); 
		});
	}
};

ice.ace.Autocompleter.keys = {
KEY_BACKSPACE: 8,
KEY_TAB:       9,
KEY_RETURN:   13,
KEY_ESC:      27,
KEY_LEFT:     37,
KEY_UP:       38,
KEY_RIGHT:    39,
KEY_DOWN:     40,
KEY_DELETE:   46,
KEY_HOME:     36,
KEY_END:      35,
KEY_PAGEUP:   33,
KEY_PAGEDOWN: 34,
KEY_INSERT:   45
};

ice.ace.Autocompleter.Browser = (function() {
        var ua = navigator.userAgent;
        var isOpera = Object.prototype.toString.call(window.opera) == '[object Opera]';
        return {
            IE:             !!window.attachEvent && !isOpera,
            Opera:          isOpera,
            WebKit:         ua.indexOf('AppleWebKit/') > -1,
            Gecko:          ua.indexOf('Gecko') > -1 && ua.indexOf('KHTML') === -1,
            MobileSafari:   /Apple.*Mobile/.test(ua)
        }
    })();

ice.ace.Autocompleter.collectTextNodes = function(element) {
	var children = element.childNodes;
	var str = '';
	for (var i = 0; i < children.length; i++) {
		var node = children[i];
		str += node.nodeType == 3 ? node.nodeValue : ( node.childNodes.length > 0 ? ice.ace.Autocompleter.collectTextNodes(node) : '');
	}
	return str;
};

ice.ace.Autocompleter.collectTextNodesIgnoreClass = function(element, className) {
	var children = element.childNodes;
	var str = '';
	for (var i = 0; i < children.length; i++) {
		var node = children[i];
		str += node.nodeType == 3 ? node.nodeValue : ( node.childNodes.length > 0 && !ice.ace.jq(node).hasClass(className) ? ice.ace.Autocompleter.collectTextNodesIgnoreClass(node, className) : '' );
	}
	return str;
};

ice.ace.Autocompleter.cleanWhitespace = function(element) {
	var node = element.firstChild;
	while (node) {
		var nextNode = node.nextSibling;
		if (node.nodeType == 3 && !/\S/.test(node.nodeValue))
			element.removeChild(node);
		node = nextNode;
	}
	return element;
};

ice.ace.Autocompleter.prototype = {

    initialize: function(element, update, options, rowC, selectedRowC, behaviors) {
        var self = this;
        this.hasFocus = false;
        this.changed = false;
        this.active = false;
        this.index = -1;
        this.entryCount = 0;
        this.rowClass = rowC;
        this.selectedRowClass = selectedRowC;

        if (this.setOptions)
            this.setOptions(options);
        else
            this.options = options || {};

        this.options.paramName = this.options.paramName || this.element.name;
        this.options.tokens = this.options.tokens || [];
        this.options.frequency = this.options.frequency || 0.4;
        this.options.minChars = this.options.minChars || 1;
        this.options.onShow = this.options.onShow ||
            function(element, update) {
                try {
					self.calculateListPosition();
					self.showEffect(update);
                } catch(e) {
                    //logger.info(e);
                }
            };
        this.options.onHide = this.options.onHide ||
            function(element, update) {
			self.hideEffect(update);
            };

        if (typeof(this.options.tokens) == 'string')
            this.options.tokens = new Array(this.options.tokens);

        this.observer = null;
        this.element.setAttribute('autocomplete', 'off');
        ice.ace.jq(this.update).hide();
		ice.ace.jq(this.element).data("labelIsInField", this.cfg.labelIsInField);
		ice.ace.jq(this.element).on("blur", function(e) { self.onBlur.call(self, e); });
		ice.ace.jq(this.element).on("focus", function(e) { self.onFocus.call(self, e); });
        var keyEvent = "keypress";
        if (ice.ace.Autocompleter.Browser.IE || ice.ace.Autocompleter.Browser.WebKit) {
            keyEvent = "keydown";
        } else {
			ice.ace.jq(this.element).on("keyup", function(e) { if (!self.justSubmitted) { self.onKeyPress.call(self, e);} } );
		}
		ice.ace.jq(this.element).on(keyEvent, function(e) { self.onKeyPress.call(self, e); } );
        // ICE-3830
        if (ice.ace.Autocompleter.Browser.IE || ice.ace.Autocompleter.Browser.WebKit)
		ice.ace.jq(this.element).on("paste", function(e) { self.onPaste.call(self, e); });
		
		// ajax behaviors
		if (behaviors) {
			if (behaviors.behaviors) {
				if (behaviors.behaviors.submit) {
					this.ajaxSubmit = behaviors.behaviors.submit;
				}
				if (behaviors.behaviors.blur) {
					this.ajaxBlur = behaviors.behaviors.blur;
				}
				if (behaviors.behaviors.textChange) {
					this.ajaxTextChange = behaviors.behaviors.textChange;
				}
				if (behaviors.behaviors.change) {
					this.ajaxValueChange = behaviors.behaviors.change;
				}
			}
		}
		
		// prepare data model for client side mode
		if (this.clientSideModeCfg) {
			var model = [];
			var $root = ice.ace.jq(ice.ace.escapeClientId(this.id + '_update')).children('div:first');
			this.clientSideModeCfg.data = $root.children();
			if ($root.hasClass('facet')) {
				this.clientSideModeCfg.data.children('span.label').each(function(i,e){model.push(e.innerHTML)});
			} else {
				this.clientSideModeCfg.data.each(function(i,e){model.push(e.innerHTML)});
			}
			this.clientSideModeCfg.model = model;
			//$root.detach();
		}
		
		this.initialized = true;
		
		if (this.clientSideModeCfg && this.showingList) {
			this.clientSideModeUpdate();
		}
    },
	
	calculateListPosition: function() {
		var element = this.element;
		var update = this.update;
		if (update["style"] && (!update.style.position || update.style.position == 'absolute')) {
			update.style.position = 'absolute';
			var jqElement = ice.ace.jq(element);
			var jqUpdate = ice.ace.jq(update);
			var pos = jqElement.offset();
			var autoUp = false;
			if (this.direction == 'auto') {
				var updateHeight = jqUpdate.height();
				updateHeight = updateHeight > this.height ? this.height : updateHeight;
				var winHeight = ice.ace.jq(window).height();
				var docHeight = ice.ace.jq(document).height();
				var scrollTop = ice.ace.jq(document).scrollTop()
				var lengthAbove = pos.top - scrollTop;
				var lengthBelow = scrollTop + winHeight - pos.top - element.offsetHeight;
				if (lengthBelow < updateHeight) {
					if (lengthAbove > lengthBelow)
						autoUp = true;
				}
			}
			if (this.direction == 'up' || autoUp) {
				var updateHeight = jqUpdate.height();
				updateHeight = updateHeight > this.height ? this.height : updateHeight;
				jqUpdate.css({ position: "absolute", marginTop: 0, marginLeft: 0, width: jqElement.width(), maxHeight: this.height, overflow: "auto" });
				var savedPos = element.style.position;
				element.style.position = "relative";
				update.style.left = element.offsetLeft + "px";
				update.style.top = (element.offsetTop - updateHeight) + "px";
				element.style.position = savedPos;
			} else {
				jqUpdate.css({ position: "absolute", marginTop: 0, marginLeft: 0, width: jqElement.width(), maxHeight: this.height, overflow: "auto" });
				var savedPos = element.style.position;
				element.style.position = "relative";
				update.style.left = element.offsetLeft + "px";
				update.style.top = (element.offsetTop + element.offsetHeight) + "px";
				element.style.position = savedPos;
			}
		}
	},

    show: function() {
        try {
            if (ice.ace.jq(this.update).css('display') == 'none')this.options.onShow(this.element, this.update);
            if (!this.iefix &&
                (navigator.appVersion.indexOf('MSIE') > 0) &&
                (navigator.userAgent.indexOf('Opera') < 0) &&
                (ice.ace.jq(this.update).css('position') == 'absolute')) {
                ice.ace.jq('<iframe id="' + this.update.id + '_iefix" title="IE6_Fix" ' +
                        'style="display:none;position:absolute;filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" ' +
                        'src="javascript:\'<html></html>\'" frameborder="0" scrolling="no"></iframe>').insertAfter(this.update);
                this.iefix = ice.ace.jq('#' + this.update.id + '_iefix').get(0);
            }
		  var self = this;
            if (this.iefix) setTimeout(function() { self.fixIEOverlapping.call(self) }, 50);
            this.element.focus();
        } catch (e) {
            //logger.info(e);
        }
    },

    fixIEOverlapping: function() {
        try {
		var pos = ice.ace.jq(this.update).offset();
            ice.ace.jq(this.iefix).css(pos);
            this.iefix.style.zIndex = 1;
            this.update.style.zIndex = 2;
            ice.ace.jq(this.iefix).show();
        } catch(e) {
            //logger.info(e);
        }
    },

    hide: function() {
        this.stopIndicator();
        if (ice.ace.jq(this.update).css('display') != 'none') this.options.onHide(this.element, this.update);
        if (this.iefix) ice.ace.jq(this.iefix).hide();
		this.showingList = false;
    },

    startIndicator: function() {
        if (this.options.indicator) ice.ace.jq(this.options.indicator).show();
    },

    stopIndicator: function() {
        if (this.options.indicator) ice.ace.jq(this.options.indicator).hide();
    },

    onKeyPress: function(event) {
        if (!this.active) {
            switch (event.keyCode) {
                case ice.ace.Autocompleter.keys.KEY_TAB:
					ice.setFocus('');
					return;
                case ice.ace.Autocompleter.keys.KEY_RETURN:
					if (this.element.value.length < this.minChars) {
						event.stopPropagation();
						event.preventDefault();
						return false;
					}
                    this.getUpdatedChoices(true, event, -1, 'enter');
					event.stopPropagation();
					event.preventDefault();
                    return;
				case ice.ace.Autocompleter.keys.KEY_UP:
                case ice.ace.Autocompleter.keys.KEY_DOWN:
                    this.getUpdatedChoices(false, event, -1);
                    return;
            }
        }

        if (this.active) {
            switch (event.keyCode) {
                case ice.ace.Autocompleter.keys.KEY_TAB:
					ice.setFocus('');
					return;
                case ice.ace.Autocompleter.keys.KEY_RETURN:
					if (this.element.value.length < this.minChars) {
						event.stopPropagation();
						event.preventDefault();
						return false;
					}

                    this.hidden = true; // Hack to fix before beta. Was popup up the list after a selection was made
                    var idx = this.selectEntry();
                    this.getUpdatedChoices(true, event, idx, 'enter');
                    this.hide();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.Autocompleter.keys.KEY_ESC:
                    this.hide();
                    this.active = false;
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.Autocompleter.keys.KEY_LEFT:
                case ice.ace.Autocompleter.keys.KEY_RIGHT:
                    return;
                case ice.ace.Autocompleter.keys.KEY_UP:
                    this.markPrevious();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.Autocompleter.keys.KEY_DOWN:
                    this.markNext();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
            }
        }
        else {
            if (event.keyCode == ice.ace.Autocompleter.keys.KEY_TAB || event.keyCode == ice.ace.Autocompleter.keys.KEY_RETURN) return;
        }
		
		if (!this.isCharacterCode(event.keyCode)) return;

        this.changed = true;
        this.hasFocus = true;
        this.index = -1;
        //This is to avoid an element being select because the mouse just happens to be over the element when the list pops up
        this.skip_mouse_hover = true;
        if (this.active) this.render();
        if (this.observer) clearTimeout(this.observer);
		var self = this;
        this.observer = setTimeout(function() { self.onObserverEvent() }, this.delay);
    },

    onKeyDown: function(event) {
        if (!this.active) {
            switch (event.keyCode) {
				case ice.ace.Autocompleter.keys.KEY_UP:
                case ice.ace.Autocompleter.keys.KEY_DOWN:
                    this.getUpdatedChoices(false, event, -1);
                    return;
                case ice.ace.Autocompleter.keys.KEY_BACKSPACE:
                case ice.ace.Autocompleter.keys.KEY_DELETE:
                    if (this.observer) clearTimeout(this.observer);
				var self = this;
                    this.observer = setTimeout( function() { self.onObserverEvent() }, this.delay);
                    return;
            }
        }
        else if (this.active) {
            switch (event.keyCode) {
                case ice.ace.Autocompleter.keys.KEY_UP:
                    this.markPrevious();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.Autocompleter.keys.KEY_DOWN:
                    this.markNext();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.Autocompleter.keys.KEY_ESC:
                    if (ice.ace.Autocompleter.Browser.WebKit) {
                        this.hide();
                        this.active = false;
					event.stopPropagation();
					event.preventDefault();
                        return;
                    }
                case ice.ace.Autocompleter.keys.KEY_BACKSPACE:
                case ice.ace.Autocompleter.keys.KEY_DELETE:
                    if (this.observer) clearTimeout(this.observer);
					var self = this;
                    this.observer = setTimeout(function() { self.onObserverEvent() }, this.delay);
                    return;
            }
        }
    },

    activate: function() {
        this.changed = false;
        this.hasFocus = true;
    },

    onHover: function(event) {
		var element = ice.ace.jq(event.currentTarget).closest('div').get(0);
        if (this.index != element.autocompleteIndex) {
            if (!this.skip_mouse_hover) this.index = element.autocompleteIndex;
            this.render();
        }
		event.stopPropagation();
		event.preventDefault();
    },

    onMove: function(event) {
        if (this.skip_mouse_hover) {
            this.skip_mouse_hover = false;
            this.onHover(event);
        }
    },

    onClick: function(event) {
		if (this.ieScrollbarFixObserver) clearTimeout(this.ieScrollbarFixObserver);
		var element = ice.ace.jq(event.currentTarget).closest('div').get(0);
        this.index = element.autocompleteIndex;
        var idx = element.autocompleteIndex;
		var interactiveElement = this.isInteractive(event.target, element);
		if (!interactiveElement) {
			this.hidden = true;
			this.selectEntry();
			this.getUpdatedChoices(true, event, idx);
			this.hide();
		} else {
			var self = this;
			var jqInteractiveElement = ice.ace.jq(interactiveElement);
			if (!jqInteractiveElement.data("onBlurHandlerRegistered")) {
				jqInteractiveElement.one('blur', function() {jqInteractiveElement.data("onBlurHandlerRegistered", false);self.onBlur();});
				jqInteractiveElement.data("onBlurHandlerRegistered", true);
			}
		}
		if (this.hideObserver) clearTimeout(this.hideObserver);
		if (this.blurObserver) clearTimeout(this.blurObserver);
    },

    onBlur: function(event) {
        var input = ice.ace.jq(this.element);
		// check if last click was done on scrollbar
		if (navigator.userAgent.indexOf("MSIE") >= 0) {
			var n = this.height;
			if (n!=null && n!='' && typeof n === 'number' && n % 1 == 0) {
				var posx=0; var posy=0;
				var e = window.event;
				if (e.pageX || e.pageY) {
					posx = e.pageX;
					posy = e.pageY;
				} else if (e.clientX || e.clientY) {
					posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
					posy = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
				}
				var widthX=input.position().left+input.width();
				var heightX=input.position().top+input.height()+parseFloat(this.height)+10;
				if ( this.active && (posx>input.position().left && posx<=widthX) && (posy>input.position().top && posy<heightX) ) {
					var self = this;
					this.ieScrollbarFixObserver = setTimeout(function(){self.element.focus();}, 200);
					return;
				}
			}
		}
        if (ice.ace.jq.trim(input.val()) == "" && this.cfg.inFieldLabel) {
            input.val(this.cfg.inFieldLabel);
            input.addClass(this.cfg.inFieldLabelStyleClass);
            input.data("labelIsInField", true);
        }
        // needed to make click events work
		var self = this;
        this.hideObserver = setTimeout(function () { self.hide(); }, 400);
        this.hasFocus = false;
        this.active = false;
		ice.setFocus('');
		if (this.ajaxBlur) {
			if (this.blurObserver) clearTimeout(this.blurObserver);
			this.ajaxBlur.params = this.ajaxBlur.params || {};
			this.ajaxBlur.params[this.id + '_hardSubmit'] = true;
			var self = this;
			this.blurObserver = setTimeout(function() { try{ice.ace.ab(self.ajaxBlur);}catch(e){} }, 390);
		}
    },

    onFocus: function(event) {
        var input = ice.ace.jq(this.element);
        if (input.data("labelIsInField")) {
            input.val("");
            input.removeClass(this.cfg.inFieldLabelStyleClass);
            input.data("labelIsInField", false);
        }
      if (this.element.createTextRange) {
       //IE  
	  this.element.focus();
		if (this.element.value.length > 0) {
			var fieldRange = this.element.createTextRange();  
			fieldRange.moveStart('character', this.element.value.length);  
			fieldRange.collapse(false);  
			fieldRange.select();
		}
       }  
      else {
       this.element.focus();
       var length = this.element.value.length;  
       this.element.setSelectionRange(length, length);  
      } 
    },

    // ICE-3830
    onPaste: function(event) {
        this.changed = true;
        this.hasFocus = true;
        this.index = -1;
        this.skip_mouse_hover = true;
        if (this.active) this.render();
        if (this.observer) clearTimeout(this.observer);
		var self = this;
        this.observer = setTimeout(function() { self.onObserverEvent(); }, this.delay);
        return;
    },

    render: function() {
        if (this.entryCount > 0) {
            for (var i = 0; i < this.entryCount; i++)
                if (this.index == i) {
                    ar = this.rowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).removeClass(ar[ai]);
                    ar = this.selectedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).addClass(ar[ai]);
                }
                else {
                    ar = this.selectedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).removeClass(ar[ai]);
                    ar = this.rowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).addClass(ar[ai]);
                }
            if (this.hasFocus) {
                this.show();
                this.active = true;
            }
        } else {
            this.active = false;
            this.hide();
        }
    },

    markPrevious: function() {
        if (this.index > 0) this.index--
        else this.index = this.entryCount - 1;
    },

    markNext: function() {
        if (this.index == -1) {
            this.index++;
            return;
        }
        if (this.index < this.entryCount - 1) this.index++
        else this.index = 0;
    },

    getEntry: function(index) {
        try {
            return this.update.firstChild.childNodes[index];
        } catch(ee) {
            return null;
        }
    },

    getCurrentEntry: function() {
        return this.getEntry(this.index);
    },

    selectEntry: function() {
        var idx = -1;
        this.active = false;
        if (this.index >= 0) {
            idx = this.index;
            this.updateElement(this.getCurrentEntry());
            this.index = -1;
        }
        return idx;
    },

    updateElement: function(selectedElement) {
        if (this.options.updateElement) {
            this.options.updateElement(selectedElement);
            return;
        }
        var value = '';
        if (this.options.select) {
            var nodes = document.getElementsByClassName(this.options.select, selectedElement) || [];
            if (nodes.length > 0) value = ice.ace.Autocompleter.collectTextNodes(nodes[0], this.options.select);
        } else {
            value = ice.ace.Autocompleter.collectTextNodesIgnoreClass(selectedElement, 'informal');
	}

        var lastTokenPos = this.findLastToken();
        if (lastTokenPos != -1) {
            var newValue = this.element.value.substr(0, lastTokenPos + 1);
            var whitespace = this.element.value.substr(lastTokenPos + 1).match(/^\s+/);
            if (whitespace)
                newValue += whitespace[0];
            this.element.value = newValue + value;
        } else {
            this.element.value = value;
        }
        this.element.focus();

        if (this.options.afterUpdateElement)
            this.options.afterUpdateElement(this.element, selectedElement);
    },

    updateChoices: function(choices) {
        if (!this.changed && this.hasFocus) {
            this.update.innerHTML = choices;
			this.calculateListPosition();
            ice.ace.Autocompleter.cleanWhitespace(this.update);
            ice.ace.Autocompleter.cleanWhitespace(this.update.firstChild);

            if (this.update.firstChild && this.update.firstChild.childNodes) {
                this.entryCount =
                    this.update.firstChild.childNodes.length;
                for (var i = 0; i < this.entryCount; i++) {
                    var entry = this.getEntry(i);
                    entry.autocompleteIndex = i;
                    this.addObservers(entry);
                }
            } else {
                this.entryCount = 0;
            }
            this.stopIndicator();
            this.index = -1;
            this.render();
        } else {

        }
    },

    addObservers: function(element) {
		var self = this;
		ice.ace.jq(element).on("mouseover", function(e) { self.onHover.call(self, e); });
		ice.ace.jq(element).on("click", function(e) { self.onClick.call(self, e); });
		ice.ace.jq(element).on("mousemove", function(e) { self.onMove.call(self, e); });
    },

    dispose:function() {
        for (var i = 0; i < this.entryCount; i++) {
            var entry = this.getEntry(i);
            entry.autocompleteIndex = i;
			ice.ace.jq(entry).off('mouseover');
			ice.ace.jq(entry).off('click');
			ice.ace.jq(entry).off('mousemove');
        }
		ice.ace.jq(this.element).off('mouseover');
		ice.ace.jq(this.element).off('click');
		ice.ace.jq(this.element).off('mousemove');
		ice.ace.jq(this.element).off('blur');
		ice.ace.jq(this.element).off('keypress');
        if (ice.ace.Autocompleter.Browser.IE || ice.ace.Autocompleter.Browser.WebKit)
			ice.ace.jq(this.element).off('keydown');
    },

    onObserverEvent: function() {
        this.changed = false;
        if (this.getToken().length >= this.options.minChars) {
            this.startIndicator();
            this.getUpdatedChoices(false, undefined, -1);
        } else {
            this.active = false;
            this.hide();
            this.getUpdatedChoices(false, undefined, -1);
        }
    },

    getToken: function() {
        var tokenPos = this.findLastToken();
        if (tokenPos != -1)
            var ret = this.element.value.substr(tokenPos + 1).replace(/^\s+/, '').replace(/\s+$/, '');
        else
            var ret = this.element.value;

        return /\n/.test(ret) ? '' : ret;
    },

    findLastToken: function() {
        var lastTokenPos = -1;

        for (var i = 0; i < this.options.tokens.length; i++) {
            var thisTokenPos = this.element.value.lastIndexOf(this.options.tokens[i]);
            if (thisTokenPos > lastTokenPos)
                lastTokenPos = thisTokenPos;
        }
        return lastTokenPos;
    },

    getUpdatedChoices: function(isHardSubmit, event, idx, trigger) {
		if (this.element.value.length < this.minChars) return; // this.hide()
        if (!event) {
            event = new Object();
        }
        entry = encodeURIComponent(this.options.paramName) + '=' +
            encodeURIComponent(this.getToken());

		if (this.observer) clearTimeout(this.observer);
		if (this.blurObserver) clearTimeout(this.blurObserver);
		if (isHardSubmit) {
			if (this.ajaxValueChange || this.ajaxSubmit) {
				var ajaxCfg = {};
				var options = {params: {}};
				options.params[this.id + '_hardSubmit'] = true;
				options.params['ice.event.keycode'] = event.keyCode;
				if (this.ajaxValueChange) {
					ice.ace.jq.extend(ajaxCfg, this.ajaxValueChange, options);
				} else {
					ice.ace.jq.extend(ajaxCfg, this.ajaxSubmit, options);
				}
				ajaxCfg.trigger = trigger;
				ice.ace.ab(ajaxCfg);
			} else if (!this.clientSideModeCfg) {
				ice.s(event, this.element);
			}
		} else {
			if (this.clientSideModeCfg) {
				this.clientSideModeUpdate();
			}
			if (this.ajaxTextChange || this.ajaxSubmit) {
				var ajaxCfg = {};
				var options = {params: {}};
				options.params['ice.event.keycode'] = event.keyCode;
				if (this.ajaxTextChange) {
					ice.ace.jq.extend(ajaxCfg, this.ajaxTextChange, options);
				} else {
					ice.ace.jq.extend(ajaxCfg, this.ajaxSubmit, options);
				}
				ice.ace.ab(ajaxCfg);
			} else if (!this.clientSideModeCfg) {
				ice.s(event, this.element);
			}
		}
		this.justSubmitted = true;
		var self = this;
		if (this.justSubmittedObserver) clearTimeout(this.justSubmittedObserver);
		this.justSubmittedObserver = setTimeout(function() {self.justSubmitted = false;},500);
    },
	
	clientSideModeUpdate: function() {
		
		var data = this.clientSideModeCfg.data;
		var model = this.clientSideModeCfg.model;
		var length = this.clientSideModeCfg.model.length;
		var caseSensitive = this.clientSideModeCfg.caseSensitive;
		var rows = this.clientSideModeCfg.rows;
		var value = this.element.value;
		if (!caseSensitive) value = value.toLowerCase();
		var filter;
		switch (this.clientSideModeCfg.filterMatchMode) {
			case 'contains':
			filter = this.containsFilter;
			break;
			case 'exact':
			filter = this.exactFilter;
			break;
			case 'startsWith':
			filter = this.startsWithFilter;
			break;
			case 'endsWith':
			filter = this.endsWithFilter;
			break;
			default:
			filter = this.noFilter;
			break;
		}
		
		var rowCount = 0;
		var result = ice.ace.jq('<div />');
		for (var i = 0; i < length; i++) {
			var item = caseSensitive ? model[i] : model[i].toLowerCase();
			if (filter(item, value)) {
				rowCount++;
				result.append(data.get(i).cloneNode(true));
			}
			if (rowCount >= rows) break;
		}
		this.updateNOW('<div>'+result.html()+'</div>');
		this.showingList = true;
	},
	
	containsFilter: function(item, value) {
		return item.indexOf(value) > -1;
	},
	exactFilter: function(item, value) {
		return item == value;
	},
	startsWithFilter: function(item, value) {
		return item.indexOf(value) == 0;
	},
	endsWithFilter: function(item, value) {
		return item.indexOf(value, item.length - value.length) > -1;
	},
	noFilter: function(item, value) {
		return true;
	},
	
	isCharacterCode: function(keyCode) {
		if (keyCode == 8 || keyCode == 46) return true; // backspace, del
		if (keyCode >= 16 && keyCode <= 20) return false; // shift, ctrl, alt, pause, caps lock
		if (keyCode >= 33 && keyCode <= 40) return false; // pg up, pg down, end, home, arrow keys
		if (keyCode == 44 || keyCode == 45) return false; // print screen, insert
		if (keyCode == 144 || keyCode == 145) return false; // num lock, scroll lock
		if (keyCode >= 91 && keyCode <= 93) return false; // windows keys, context menu
		if (keyCode >= 112 && keyCode <= 123) return false; // f keys
		if (keyCode == 9 || keyCode == 10 || keyCode == 13 || keyCode ==  27) return false; // tab, lf, cr, esc
		return true;
	},
	
	isInteractive: function(element, container) {
		if (element) {
			var result;
			var currentNode = element;
			while (currentNode !== container) {
				result = this.isInteractiveElement(currentNode);
				if (result) return result;
				else currentNode = currentNode.parentNode;
			}
		}
		return false;
	},
	
	isInteractiveElement: function(element) {
		if (element) {
			var tag = element.tagName;
			if (tag) {
				tag = tag.toLowerCase();
				switch (tag) {
					case 'input':
					case 'select':
					case 'label':
					case 'textarea':
					case 'button':
					case 'a':
						return element;
				}
			}
		}
		return false;
	},
	
    updateNOW: function(text) {

		if (!text) return;
        if (this.hidden) {
            this.hidden = false;
        }
        this.hasFocus = true;
        ice.ace.Autocompleter.cleanWhitespace(this.update);
		if (ice.ace.jq.support.leadingWhitespace) { // browsers other than IE7/8
			this.updateChoices(text);
			this.show();
			this.render();
			this.element.focus();
		}
		else { // give time to IE7/8 to have nodes ready when the full form has been updated
			var self = this;
			setTimeout(function() { 
				self.updateChoices(text);
				self.show();
				self.render();
				if (focus) ice.ace.jq(ice.ace.escapeClientId(self.element.id)).focus(); 
			}, 50);
		}
    },
	
	updateField: function(value, focus) {
		var currentValue = this.element.value;
		if (currentValue.indexOf(value) != 0)
			this.element.value = value;
		if (focus && ice.ace.jq.support.leadingWhitespace) this.element.focus(); // browsers other than IE7/8
		if (!ice.ace.jq.support.leadingWhitespace) { // force IE7/8 to set focus on the text field
			var element = this.element;
			setTimeout(function() { if (focus) ice.ace.jq(ice.ace.escapeClientId(element.id)).focus(); }, 50);
		}
	},
	
	showEffect: function(update) {
		var list = ice.ace.jq(update);
		list.css('opacity', 1);
		var e = this.effects.show;
		e = e ? e.toLowerCase() : '';
		if (e == 'blind' || e == 'bounce' || e == 'clip' || e == 'drop' || e == 'explode'
				|| e == 'fold' || e == 'puff' || e == 'pulsate' || e == 'scale' || e == 'slide' || e == 'shake') {
			list.toggle(e, {}, this.effects.showLength);
		} else list.fadeIn(this.effects.showLength);
	},
	
	hideEffect: function(update) {
		var list = ice.ace.jq(update);
		var e = this.effects.hide;
		e = e ? e.toLowerCase() : '';
		if (e == 'blind' || e == 'bounce' || e == 'clip' || e == 'drop' || e == 'explode'
				|| e == 'fold' || e == 'puff' || e == 'pulsate' || e == 'scale' || e == 'slide') {
			list.toggle(e, {}, this.effects.hideLength);
		} else list.fadeOut(this.effects.hideLength);
	}
};
