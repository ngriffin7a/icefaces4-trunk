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

if (!ice.ace.SelectMenus) ice.ace.SelectMenus = {};

ice.ace.SelectMenu = function(id, updateId, rowClass, highlightedRowClass, selectedRowClass, height, behaviors, cfg, effects) {
	this.id = id;
	var isInitialized = false;
	if (ice.ace.SelectMenus[this.id] && ice.ace.SelectMenus[this.id].initialized) isInitialized = true;
	if (isInitialized) this.selectedIndex = ice.ace.SelectMenus[this.id].selectedIndex;
	ice.ace.SelectMenus[this.id] = this;
	this.height = height == 0 ? 'auto' : height;
	this.direction = 'down';
	var options = {};
	this.root = ice.ace.jq(ice.ace.escapeClientId(this.id));
	var $element = this.root.find('.ui-selectmenu-value');
	this.element = $element.get(0);
	this.element.id = this.id + "_input";
	this.displayedValue = $element.find('span').get(0);
	this.$downArrowButton = $element.find('div').eq(0);
	var $displayedValue = ice.ace.jq(this.displayedValue);
	$displayedValue.css('width', $element.width() - this.$downArrowButton.outerWidth(true) - ($displayedValue.outerWidth(true) - $displayedValue.width()));
	if (ice.ace.jq.browser.msie) {// ie7 fix
		if (ice.ace.jq.browser.version < 8) {
			if (navigator.userAgent.indexOf("Trident/5") < 0) {
				this.$downArrowButton.css('position', 'absolute');
			}
		}
	}
	this.adjustDownArrowButtonHeight();
	var $input = this.root.find('input[name="'+this.id+'_input"]');
	this.input = $input.get(0);
	this.input.id = this.id + "_input";
	var $update = ice.ace.jq(ice.ace.escapeClientId(updateId))
	$update.css('width', $element.width());
	this.update = $update.get(0);
	this.cfg = cfg;
	this.effects = effects;
	$element.data("labelIsInField", this.cfg.labelIsInField);
	
	if (isInitialized) {
		this.initialize(this.element, this.update, options, rowClass, highlightedRowClass, selectedRowClass, behaviors);
	} else {
		var self = this;
		$element.on('focus', function() {
			$element.off('focus');
			if (ice.ace.SelectMenu.Browser.IE) { $element.children().off('click'); }
			if ($element.data("labelIsInField")) {
				self.displayedValue.innerHTML = '&nbsp;';
				$element.removeClass(self.cfg.inFieldLabelStyleClass);
				$element.data("labelIsInField", false);
				self.cfg.labelIsInField = false;
			}
			self.initialize(self.element, self.update, options, rowClass, highlightedRowClass, selectedRowClass, behaviors);
			if (ice.ace.SelectMenu.Browser.IE) {
				self.updateNOW(self.content);
			}
		});
		if (ice.ace.SelectMenu.Browser.IE) {
			$element.children().on('click', function(e) {
				$element.off('focus');
				$element.children().off('click');
				if ($element.data("labelIsInField")) {
					self.displayedValue.innerHTML = '&nbsp;';
					$element.removeClass(self.cfg.inFieldLabelStyleClass);
					$element.data("labelIsInField", false);
					self.cfg.labelIsInField = false;
				}
				self.initialize(self.element, self.update, options, rowClass, highlightedRowClass, selectedRowClass, behaviors); 
				e.stopPropagation();
				e.preventDefault();
				if (ice.ace.SelectMenu.Browser.IE) {
					self.updateNOW(self.content);
				}
			});
		}
	}
};

ice.ace.SelectMenu.LABEL_CLASS = 'ui-selectmenu-item-label';
ice.ace.SelectMenu.VALUE_CLASS = 'ui-selectmenu-item-value';
ice.ace.SelectMenu.IGNORE_CLASS = 'ui-selectmenu-item-ignore';

ice.ace.SelectMenu.keys = {
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

ice.ace.SelectMenu.Browser = (function() {
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

ice.ace.SelectMenu.collectTextNodes = function(element) {
	var children = element.childNodes;
	var str = '';
	for (var i = 0; i < children.length; i++) {
		var node = children[i];
		str += node.nodeType == 3 ? node.nodeValue : ( node.childNodes.length > 0 ? ice.ace.SelectMenu.collectTextNodes(node) : '');
	}
	return str;
};

ice.ace.SelectMenu.collectTextNodesIgnoreClass = function(element, className) {
	var children = element.childNodes;
	var str = '';
	for (var i = 0; i < children.length; i++) {
		var node = children[i];
		str += node.nodeType == 3 ? node.nodeValue : ( node.childNodes.length > 0 && !ice.ace.jq(node).hasClass(className) ? ice.ace.SelectMenu.collectTextNodesIgnoreClass(node, className) : '' );
	}
	return str;
};

ice.ace.SelectMenu.cleanWhitespace = function(element) {
	var node = element.firstChild;
	while (node) {
		var nextNode = node.nextSibling;
		if (node.nodeType == 3 && !/\S/.test(node.nodeValue))
			element.removeChild(node);
		node = nextNode;
	}
	return element;
};

ice.ace.SelectMenu.prototype = {

    initialize: function(element, update, options, rowC, highlightedRowClass, selectedRowC, behaviors) {
        var self = this;
        this.hasFocus = false;
        this.changed = false;
        this.active = false;
        this.index = -1;
		if (!(typeof this.selectedIndex == 'number' && this.selectedIndex > -1)) this.selectedIndex = -1;
        this.rowClass = rowC;
		this.highlightedRowClass = highlightedRowClass;
        this.selectedRowClass = selectedRowC;

        if (this.setOptions)
            this.setOptions(options);
        else
            this.options = options || {};

        this.options.onShow = this.options.onShow ||
            function(element, update) {
                try {
					self.$downArrowButton.addClass('ui-state-hover');
					self.calculateListPosition();
                    self.showEffect(update);
                } catch(e) {
                    //logger.info(e);
                }
            };
        this.options.onHide = this.options.onHide ||
            function(element, update) {
			self.$downArrowButton.removeClass('ui-state-hover');
			self.hideEffect(update);
            };

        ice.ace.jq(this.update).hide();
		ice.ace.jq(this.element).on("blur", function(e) { self.onBlur.call(self, e); });
		ice.ace.jq(this.element).on("focus", function(e) { self.onFocus.call(self, e); });
		ice.ace.jq(this.element).on("click", function(e) {
            self.onElementClick.call(self, e);
        });
		if (ice.ace.SelectMenu.Browser.IE) {
			ice.ace.jq(this.element).children().on("click", function(e) { 
				self.onElementClick.call(self, e); 
				e.stopPropagation();
				e.preventDefault();
			});
		}
        var keyEvent = "keypress";
        if (ice.ace.SelectMenu.Browser.IE || ice.ace.SelectMenu.Browser.WebKit) {
            keyEvent = "keydown";
        }
		ice.ace.jq(this.element).on(keyEvent, function(e) { self.onKeyPress.call(self, e); } );
		
		// ajax behaviors
		if (behaviors) {
			if (behaviors.behaviors) {
				if (behaviors.behaviors.change) {
					this.ajaxValueChange = behaviors.behaviors.change;
				}
				if (behaviors.behaviors.blur) {
					this.ajaxBlur = behaviors.behaviors.blur;
				}
			}
		}
		
		this.initialized = true;
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
				jqUpdate.css({ position: "absolute", marginTop: 0, marginLeft: 0, maxHeight: this.height, overflow: "auto" });
				var savedPos = element.style.position;
				element.style.position = "relative";
				update.style.left = element.offsetLeft + "px";
				update.style.top = (element.offsetTop - updateHeight) + "px";
				element.style.position = savedPos;
			} else {
				jqUpdate.css({ position: "absolute", marginTop: 0, marginLeft: 0, maxHeight: this.height, overflow: "auto" });
				var savedPos = element.style.position;
				element.style.position = "relative";
				update.style.left = element.offsetLeft + "px";
				update.style.top = (element.offsetTop + element.offsetHeight - 1) + "px";
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
		this.active = false;
        this.stopIndicator();
        if (ice.ace.jq(this.update).css('display') != 'none') this.options.onHide(this.element, this.update);
        if (this.iefix) ice.ace.jq(this.iefix).hide();
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
                case ice.ace.SelectMenu.keys.KEY_TAB:
					ice.setFocus('');
					return;
                case ice.ace.SelectMenu.keys.KEY_RETURN:
                    this.getUpdatedChoices(true, event, -1);
					event.stopPropagation();
					event.preventDefault();
                    return;
				case ice.ace.SelectMenu.keys.KEY_UP:
					this.index = this.selectedIndex;
                    this.markPrevious();
					this.selectEntry();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.SelectMenu.keys.KEY_DOWN:
					this.index = this.selectedIndex;
                    this.markNext();
					this.selectEntry();
					event.stopPropagation();
					event.preventDefault();
                    return;
				case ice.ace.SelectMenu.keys.KEY_HOME:
                    this.markFirst();
					this.selectEntry();
					event.stopPropagation();
					event.preventDefault();
					return;
				case ice.ace.SelectMenu.keys.KEY_END:
                    this.markLast();
					this.selectEntry();
					event.stopPropagation();
					event.preventDefault();
					return;
				case ice.ace.SelectMenu.keys.KEY_PAGEUP:
                    this.markPageUp();
                    this.selectEntry();
					event.stopPropagation();
					event.preventDefault();
					return;
				case ice.ace.SelectMenu.keys.KEY_PAGEDOWN:
                    this.markPageDown();
                    this.selectEntry();
					event.stopPropagation();
					event.preventDefault();
					return;
				default:
					if (event.which > 0) this.markFirstMatch(event.which);
                    this.selectEntry();
					event.stopPropagation();
					event.preventDefault();
					return;
            }
        } else {
			switch (event.keyCode) {
                case ice.ace.SelectMenu.keys.KEY_TAB:
					ice.setFocus('');
					return;
                case ice.ace.SelectMenu.keys.KEY_RETURN:
					var idx = this.selectEntry();
					this.getUpdatedChoices(true, event, idx);
					this.hide();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.SelectMenu.keys.KEY_ESC:
                    this.hide();
                    this.active = false;
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.SelectMenu.keys.KEY_UP:
                    this.markPrevious();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.SelectMenu.keys.KEY_DOWN:
                    this.markNext();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
				case ice.ace.SelectMenu.keys.KEY_HOME:
                    this.markFirst();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
					return;
				case ice.ace.SelectMenu.keys.KEY_END:
                    this.markLast();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
					return;
				case ice.ace.SelectMenu.keys.KEY_PAGEUP:
                    this.markPageUp();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
					return;
				case ice.ace.SelectMenu.keys.KEY_PAGEDOWN:
                    this.markPageDown();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
					return;
				default:
					if (event.which > 0) this.markFirstMatch(event.which);
                    this.render();
					event.stopPropagation();
					event.preventDefault();
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
		var $element = ice.ace.jq(event.currentTarget).closest('div');
		var element = $element.get(0);
        this.index = element.autocompleteIndex;
        var idx = element.autocompleteIndex;
		var interactiveElement = this.isInteractive(event.target, element);
		if (!$element.hasClass('ui-state-disabled') && !interactiveElement) {
			this.selectEntry();
			this.getUpdatedChoices(true, event, idx);
			this.hide();
		}
		if (interactiveElement) {
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
        var element = ice.ace.jq(this.element);
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
				var widthX=element.position().left+element.width();
				var heightX=element.position().top+element.height()+parseFloat(this.height)+10;
				if ( this.active && (posx>element.position().left && posx<=widthX) && (posy>element.position().top && posy<heightX) ) {
					var self = this;
					this.ieScrollbarFixObserver = setTimeout(function(){self.element.focus();}, 200);
					return;
				}
			}
		}
        if (ice.ace.jq.trim(this.displayedValue.innerHTML) == '&nbsp;' && this.cfg.inFieldLabel) {
			this.displayedValue.innerHTML = this.replaceSpaces(this.cfg.inFieldLabel);
            element.addClass(this.cfg.inFieldLabelStyleClass);
            element.data("labelIsInField", true);
        }
        // needed to make click events work
		var self = this;
        this.hideObserver = setTimeout(function () { self.hide(); }, 250);
        this.hasFocus = false;
        this.active = false;
		ice.setFocus('');
		if (this.ajaxBlur) {
			if (this.blurObserver) clearTimeout(this.blurObserver);
			this.ajaxBlur.params = this.ajaxBlur.params || {};
			var self = this;
			this.blurObserver = setTimeout(function() { ice.ace.ab(self.ajaxBlur); }, 200);
		}
    },

    onFocus: function(event) {
        var element = ice.ace.jq(this.element);
        if (element.data("labelIsInField")) {
			this.displayedValue.innerHTML = '&nbsp;';
            element.removeClass(this.cfg.inFieldLabelStyleClass);
            element.data("labelIsInField", false);
        }
    },
	
	onElementClick: function(event) {
		if (this.active) {
			this.hide();
			if (this.hideObserver) clearTimeout(this.hideObserver);
			if (this.blurObserver) clearTimeout(this.blurObserver);
		} else {
			if (this.hideObserver) clearTimeout(this.hideObserver);
			if (this.blurObserver) clearTimeout(this.blurObserver);
			this.updateNOW(this.content);
		}
	},

    render: function() {
        if (this.entryCount > 0) {
            for (var i = 0; i < this.entryCount; i++)
				if (this.selectedIndex == i) {
                    ar = this.rowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).removeClass(ar[ai]);
                    ar = this.highlightedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).removeClass(ar[ai]);
                    ar = this.selectedRowClass.split(" ");
                    for (var ai = 0; ai < ar.length; ai++)
                        ice.ace.jq(this.getEntry(i)).addClass(ar[ai]);				
				} else if (this.index == i) {
					if (!ice.ace.jq(this.getEntry(i)).hasClass('ui-state-disabled')) {
						ar = this.rowClass.split(" ");
						for (var ai = 0; ai < ar.length; ai++)
							ice.ace.jq(this.getEntry(i)).removeClass(ar[ai]);
						ar = this.highlightedRowClass.split(" ");
						for (var ai = 0; ai < ar.length; ai++)
							ice.ace.jq(this.getEntry(i)).addClass(ar[ai]);
					}
                } else {
                    ar = this.highlightedRowClass.split(" ");
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
		// we skip disabled entries
		var found = false;
		var i = this.index;
		while (true) {
			if (i > 0) i--
			else i = this.entryCount - 1;
			var entry = this.getEntry(i);
			if (entry && !ice.ace.jq(entry).hasClass('ui-state-disabled')) {
				found = true;
				break;
			}
			if (i == this.index) break; // we did one full loop already
			if (this.index == -1 && i == 0) break; // special case
		}
		if (found) {
			this.index = i;
			this.scrollToMarkedItem();
		}
    },

    markNext: function() {
		// we skip disabled entries
		var found = false;
		var i = this.index;
		while (true) {
			if (i < this.entryCount - 1) i++;
			else i = 0;
			var entry = this.getEntry(i);
			if (entry && !ice.ace.jq(entry).hasClass('ui-state-disabled')) {
				found = true;
				break;
			}
			if (i == this.index) break; // we did one full loop already
			if (this.index == -1 && i == this.entryCount) break; // special case
		}
		if (found) {
			this.index = i;
			this.scrollToMarkedItem();
		}
    },
	
	markFirst: function() {
		this.index = -1;
		this.markNext();
	},
	
	markLast: function() {
		this.index = this.entryCount;
		this.markPrevious();
	},
	
	markPageUp: function() {
		var i = this.index - 9;
		if (i < 0) {
			this.markFirst();
			return;
		}
		this.index = i;
		this.markPrevious();
	},
	
	markPageDown: function() {
		var i = this.index + 9;
		if (i >= this.entryCount) {
			this.markLast();
			return;
		}
		this.index = i;
		this.markNext();
	},
	
	markFirstMatch: function(charCode) {
		var eventChar = String.fromCharCode(charCode).toLowerCase();
		// we skip disabled entries
		var found = false;
		var i = this.index;
		while (true) {
			if (i < this.entryCount - 1) i++;
			else i = 0;
			var entry = this.getEntry(i);
			if (entry && !ice.ace.jq(entry).hasClass('ui-state-disabled')) {
				var labelNode = ice.ace.jq(entry).children('.' + ice.ace.SelectMenu.LABEL_CLASS).get(0);
				value = ice.ace.SelectMenu.collectTextNodesIgnoreClass(labelNode, ice.ace.SelectMenu.IGNORE_CLASS);
				if (value) {
					value = value.replace(/^\s+|\s+$/g, ''); // trim
					var firstChar = String.fromCharCode(value.charCodeAt(0)).toLowerCase();
					if (eventChar == firstChar) {
						found = true;
						break;
					}
				}
			}
			if (i == this.index) break; // we did one full loop already
			if (this.index == -1 && i == (this.entryCount - 1)) break; // special case
		}
		if (found) {
			this.index = i;
			this.scrollToMarkedItem();
		}	
	},
	
	scrollToMarkedItem: function() {
		if (this.active) {
			var entry = this.getEntry(this.index);
			if (entry) {
				var itemTop = ice.ace.jq(entry).position().top;
				var $update = ice.ace.jq(this.update);
				$update.scrollTop($update.scrollTop() + itemTop);
			}
		}
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
			this.selectedIndex = this.index;
            this.updateElement(this.getCurrentEntry());
            this.index = -1;
        }
        return idx;
    },

    updateElement: function(selectedEntry) {
        var value = '';
        value = ice.ace.SelectMenu.collectTextNodesIgnoreClass(selectedEntry, ice.ace.SelectMenu.LABEL_CLASS);

		this.updateValue(value);
        this.element.focus();
    },

    updateChoices: function(choices) {
        if (!this.changed && this.hasFocus) {
            this.update.innerHTML = choices;
			this.calculateListPosition();
            ice.ace.SelectMenu.cleanWhitespace(this.update);
            ice.ace.SelectMenu.cleanWhitespace(this.update.firstChild);

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
        if (ice.ace.SelectMenu.Browser.IE || ice.ace.SelectMenu.Browser.WebKit)
			ice.ace.jq(this.element).off('keydown');
    },

    onObserverEvent: function() {
        this.changed = false;
        this.startIndicator();
        this.getUpdatedChoices(false, undefined, -1);
    },

    getUpdatedChoices: function(isHardSubmit, event, idx) {
        if (!event) {
            event = new Object();
        }

		if (this.blurObserver) clearTimeout(this.blurObserver);
		if (this.ajaxValueChange) {
			var ajaxCfg = {};
			var options = {params: {}};
			options.params['ice.event.keycode'] = event.keyCode;
			ice.ace.jq.extend(ajaxCfg, this.ajaxValueChange, options);
			ice.ace.ab(ajaxCfg);
		}
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
	
	setContent: function(content) {
		this.content = content;
		this.update.innerHTML = this.content;
		if (this.update.firstChild && this.update.firstChild.childNodes) {
			this.entryCount = this.update.firstChild.childNodes.length;
		}
	},

    updateNOW: function(text) {

		if (!text) return;
        this.hasFocus = true;
        ice.ace.SelectMenu.cleanWhitespace(this.update);
		this.updateChoices(text);
		this.show();
		this.render();
		this.element.focus();
    },
	
	updateValue: function(value) {
		if (value) {
			this.input.value = value;
		} else {
			this.input.value = '';
		}
		this.updateSelectedIndex();
		// update label
		if (value) {
			var currentEntry = this.getEntry(this.selectedIndex);
			if (currentEntry) {
				var labelSpan = ice.ace.jq(currentEntry).find('.'+ice.ace.SelectMenu.LABEL_CLASS).get(0);
				var label = ice.ace.SelectMenu.collectTextNodesIgnoreClass(labelSpan, ice.ace.SelectMenu.IGNORE_CLASS);
				this.displayedValue.innerHTML = this.replaceSpaces(label);
			} else {
				this.displayedValue.innerHTML = '&nbsp;';
			}
		} else {
			var element = ice.ace.jq(this.element);
			if (this.cfg.inFieldLabel) {
				this.displayedValue.innerHTML = this.replaceSpaces(this.cfg.inFieldLabel);
				element.addClass(this.cfg.inFieldLabelStyleClass);
				element.data("labelIsInField", true);
			} else {
				this.displayedValue.innerHTML = '&nbsp;';
			}
		}
		this.adjustDownArrowButtonHeight();
	},
	
	adjustDownArrowButtonHeight: function() {
		this.$downArrowButton.css('height', ice.ace.jq(this.displayedValue).outerHeight());
		var height = this.$downArrowButton.height();
		var padding = (height - 16) / 2;
		this.$downArrowButton.children().eq(0).css('height', padding);
	},
	
	replaceSpaces: function(str) {
		if (str) return str.replace(/ /g, '&nbsp;');
		else return '';
	},
	
	// update selected index if value was changed programmatically or was pre-selected
	updateSelectedIndex: function() {
		if (typeof this.selectedIndex != 'number' && !this.selectedIndex) this.selectedIndex = -1;
		var currentEntry = this.getEntry(this.selectedIndex);
		if ((currentEntry && (this.input.value != ice.ace.SelectMenu.collectTextNodesIgnoreClass(currentEntry, ice.ace.SelectMenu.LABEL_CLASS)))
			|| (this.selectedIndex == -1 && this.input.value)) {
			var found = false;
			for (var i = 0; i < this.entryCount; i++) {
				var entry = this.getEntry(i);
				if (entry && (this.input.value == ice.ace.SelectMenu.collectTextNodesIgnoreClass(entry, ice.ace.SelectMenu.LABEL_CLASS))) {
					found = true;
					this.selectedIndex = i;
					break;
				}
			}
			if (!found) this.selectedIndex = -1;
		}
	},
	
	showEffect: function(update) {
		var list = ice.ace.jq(update);
		list.css('opacity', 1);
		var e = this.effects.show;
		e = e ? e.toLowerCase() : '';
		if (e == 'blind' || e == 'bounce' || e == 'clip' || e == 'drop' || e == 'explode'
				|| e == 'fold' || e == 'puff' || e == 'pulsate' || e == 'scale' || e == 'slide' || e == 'shake') {
			list.show(e, {}, this.effects.showLength);
		} else list.fadeIn(this.effects.showLength);
	},
	
	hideEffect: function(update) {
		var list = ice.ace.jq(update);
		var e = this.effects.hide;
		e = e ? e.toLowerCase() : '';
		if (e == 'blind' || e == 'bounce' || e == 'clip' || e == 'drop' || e == 'explode'
				|| e == 'fold' || e == 'puff' || e == 'pulsate' || e == 'scale' || e == 'slide') {
			list.hide(e, {}, this.effects.hideLength);
		} else list.fadeOut(this.effects.hideLength);
	}
};
