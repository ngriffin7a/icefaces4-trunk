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

if (!ice.ace.ComboBoxes) ice.ace.ComboBoxes = {};

ice.ace.ComboBox = function(id, updateId, rowClass, highlightedRowClass, selectedRowClass, height, showListOnInput, behaviors, cfg, clientSideModeCfg, effects, placeholder) {
	this.id = id;
	var isInitialized = false;
	if (ice.ace.ComboBoxes[this.id] && ice.ace.ComboBoxes[this.id].initialized) isInitialized = true;
	if (isInitialized) this.selectedIndex = ice.ace.ComboBoxes[this.id].selectedIndex;
	ice.ace.ComboBoxes[this.id] = this;
	this.clientSideModeCfg = clientSideModeCfg;
	this.height = height == 0 ? 'auto' : height;
	this.direction = 'down';
	this.showListOnInput = showListOnInput;
	var options = {};
	this.root = ice.ace.jq(ice.ace.escapeClientId(this.id));
	var $box = this.root.find('.ui-combobox-value');
	var $element = this.root.find('input[type=text]');
	this.element = $element.get(0);
	this.element.id = this.id + "_input";
	this.hidden = this.root.find('input[type=hidden]').get(0);
	var $downArrowButton = $box.find('div');
	$element.css('width', $box.width() - $downArrowButton.outerWidth(true) - ($element.outerWidth(true) - $element.width()));
	this.downArrowButton = $downArrowButton.eq(0);
	this.downArrowButton.css('height', $box.height());
	var height = this.downArrowButton.height();
	var padding = (height - ice.ace.ComboBox.DELTA_HEIGHT) / 2;
	this.downArrowButton.children().eq(0).css('height', padding);
	if (ice.ace.jq.browser.msie) {// ie7 fix
		if (ice.ace.jq.browser.version < 8) {
			if (navigator.userAgent.indexOf("Trident/5") < 0) {
				$downArrowButton.css('height', '').css('position', 'absolute');
			}
		}
	}
	var $update = ice.ace.jq(ice.ace.escapeClientId(updateId))
	$update.css('width', $box.width());
	this.update = $update.get(0);
	this.cfg = cfg;
	this.effects = effects;
	if (placeholder && !('placeholder' in document.createElement('input'))) { // if 'placeholder' isn't supported, use label inField
		this.cfg.inFieldLabel = placeholder;
	}
	$element.data("labelIsInField", this.cfg.labelIsInField);
	
	if (isInitialized) {
		this.initialize(this.element, this.update, options, rowClass, highlightedRowClass, selectedRowClass, behaviors);
	} else {
        if (ice.ace.jq.trim($element.val()) == "" && this.cfg.inFieldLabel) {
            $element.val(this.cfg.inFieldLabel);
            $element.addClass(this.cfg.inFieldLabelStyleClass);
            $element.data("labelIsInField", true);
        }
		var self = this;
		var triggerInit = function() {
			$element.off('focus');
			$downArrowButton.off('click');
			if ($element.data("labelIsInField")) {
				self.element.value = '';
				$element.removeClass(self.cfg.inFieldLabelStyleClass);
				$element.data("labelIsInField", false);
				self.cfg.labelIsInField = false;
			}
			self.initialize(self.element, self.update, options, rowClass, highlightedRowClass, selectedRowClass, behaviors); 
		};
		$element.on('focus', triggerInit);
		$downArrowButton.on('click', triggerInit);
	}
};

ice.ace.ComboBox.setDimensionsOnly = function(id) {
	var root = ice.ace.jq(ice.ace.escapeClientId(id));
	var $box = root.find('.ui-combobox-value');
	var $element = root.find('input');
	var $downArrowButton = $box.find('div');
	$element.css('width', $box.width() - $downArrowButton.outerWidth(true) - ($element.outerWidth(true) - $element.width()));
	var downArrowButton = $downArrowButton.eq(0);
	downArrowButton.css('height', $box.height());
	var height = downArrowButton.height();
	var padding = (height - ice.ace.ComboBox.DELTA_HEIGHT) / 2;
	downArrowButton.children().eq(0).css('height', padding);
	if (ice.ace.jq.browser.msie) {// ie7 fix
		if (ice.ace.jq.browser.version < 8) {
			if (navigator.userAgent.indexOf("Trident/5") < 0) {
				$downArrowButton.css('height', '').css('position', 'absolute');
			}
		}
	}
};

ice.ace.ComboBox.LABEL_CLASS = 'ui-combobox-item-label';
ice.ace.ComboBox.VALUE_CLASS = 'ui-combobox-item-value';
ice.ace.ComboBox.IGNORE_CLASS = 'ui-combobox-item-ignore';
ice.ace.ComboBox.DELTA_WIDTH = 26;
ice.ace.ComboBox.DELTA_HEIGHT = 16;

ice.ace.ComboBox.keys = {
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

ice.ace.ComboBox.Browser = (function() {
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

ice.ace.ComboBox.collectTextNodes = function(element) {
	var children = element.childNodes;
	var str = '';
	for (var i = 0; i < children.length; i++) {
		var node = children[i];
		str += node.nodeType == 3 ? node.nodeValue : ( node.childNodes.length > 0 ? ice.ace.ComboBox.collectTextNodes(node) : '');
	}
	return str;
};

ice.ace.ComboBox.collectTextNodesIgnoreClass = function(element, className) {
	var children = element.childNodes;
	var str = '';
	for (var i = 0; i < children.length; i++) {
		var node = children[i];
		str += node.nodeType == 3 ? node.nodeValue : ( node.childNodes.length > 0 && !ice.ace.jq(node).hasClass(className) ? ice.ace.ComboBox.collectTextNodesIgnoreClass(node, className) : '' );
	}
	return str;
};

ice.ace.ComboBox.cleanWhitespace = function(element) {
	var node = element.firstChild;
	while (node) {
		var nextNode = node.nextSibling;
		if (node.nodeType == 3 && !/\S/.test(node.nodeValue))
			element.removeChild(node);
		node = nextNode;
	}
	return element;
};

ice.ace.ComboBox.prototype = {

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
					self.downArrowButton.addClass('ui-state-hover');
					self.calculateListPosition();
                    self.showEffect(update);
					self.active = true;
                } catch(e) {
                    //logger.info(e);
                }
            };
        this.options.onHide = this.options.onHide ||
            function(element, update) {
			self.downArrowButton.removeClass('ui-state-hover');
			self.hideEffect(update);
			self.active = false;
            };

        ice.ace.jq(this.update).hide();
		ice.ace.jq(this.element).on("blur", function(e) { self.onBlur.call(self, e); });
		ice.ace.jq(this.element).on("focus", function(e) { self.onFocus.call(self, e); });
		ice.ace.jq(this.downArrowButton).on("click", function(e) { self.onElementClick.call(self, e); });
		if (ice.ace.ComboBox.Browser.IE) {
			ice.ace.jq(this.downArrowButton).children().on("click", function(e) { 
				self.onElementClick.call(self, e); 
				e.stopPropagation();
				e.preventDefault();
			});
		}
        var keyEvent = "keypress";
        if (ice.ace.ComboBox.Browser.IE || ice.ace.ComboBox.Browser.WebKit) {
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
		
		// prepare data model for autocomplete functionality
		this.setContent(ice.ace.jq(ice.ace.escapeClientId(this.id + '_update')).get(0).firstChild.innerHTML);
		
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
    },

    startIndicator: function() {
        if (this.options.indicator) ice.ace.jq(this.options.indicator).show();
    },

    stopIndicator: function() {
        if (this.options.indicator) ice.ace.jq(this.options.indicator).hide();
    },

    onKeyPress: function(event) {
		this.hidden.value = this.element.value;
        if (!this.active) {
            switch (event.keyCode) {
                case ice.ace.ComboBox.keys.KEY_TAB:
					ice.setFocus('');
					return;
                case ice.ace.ComboBox.keys.KEY_RETURN:
                    this.getUpdatedChoices(true, event, -1);
					event.stopPropagation();
					event.preventDefault();
                    return;
				case ice.ace.ComboBox.keys.KEY_UP:
					var self = this;
					setTimeout(function(){self.clientSideModeUpdate();},50);
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.ComboBox.keys.KEY_DOWN:
					var self = this;
					setTimeout(function(){self.clientSideModeUpdate();},50);
					event.stopPropagation();
					event.preventDefault();
                    return;
				default:
					this.hidden.value = this.element.value;
					if (this.showListOnInput) {
						var self = this;
						setTimeout(function(){self.clientSideModeUpdate();},50);
					}
					return;
            }
        } else {
			switch (event.keyCode) {
                case ice.ace.ComboBox.keys.KEY_TAB:
					ice.setFocus('');
					return;
                case ice.ace.ComboBox.keys.KEY_RETURN:
					var idx = this.selectEntry();
					this.getUpdatedChoices(true, event, idx);
					this.hide();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.ComboBox.keys.KEY_ESC:
                    this.hide();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.ComboBox.keys.KEY_UP:
                    this.markPrevious();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
                case ice.ace.ComboBox.keys.KEY_DOWN:
                    this.markNext();
                    this.render();
					event.stopPropagation();
					event.preventDefault();
                    return;
				default:
					var self = this;
					setTimeout(function(){self.clientSideModeUpdate();},50);
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
		var $box = this.root.find('.ui-combobox-value');
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
				var widthX=$box.position().left+$box.width();
				var heightX=$box.position().top+$box.height()+parseFloat(this.height)+10;
				if ( this.active && (posx>$box.position().left && posx<=widthX) && (posy>$box.position().top && posy<heightX) ) {
					var self = this;
					this.ieScrollbarFixObserver = setTimeout(function(){self.element.focus();}, 200);
					return;
				}
			}
		}
		var self = this;
        this.hideObserver = setTimeout(function () { // needed to make click events work
				self.hide();
				var element = ice.ace.jq(self.element);
				if (ice.ace.jq.trim(self.element.value) == '' && self.cfg.inFieldLabel) {
					self.element.value = self.cfg.inFieldLabel;
					element.addClass(self.cfg.inFieldLabelStyleClass);
					element.data("labelIsInField", true);
				}
			}, 400);
        this.hasFocus = false;
		ice.setFocus('');
		if (this.ajaxBlur) {
			if (this.blurObserver) clearTimeout(this.blurObserver);
			this.ajaxBlur.params = this.ajaxBlur.params || {};
			var self = this;
			this.blurObserver = setTimeout(function() { try{ice.ace.ab(self.ajaxBlur);}catch(e){} }, 390);
		}
    },

    onFocus: function(event) {
        var element = ice.ace.jq(this.element);
        if (element.data("labelIsInField")) {
			this.element.value = '';
            element.removeClass(this.cfg.inFieldLabelStyleClass);
            element.data("labelIsInField", false);
        }
		
		// place caret at the end of text
		if (this.element.createTextRange) { //IE
			if (this.element.value.length > 0) {
				var fieldRange = this.element.createTextRange();
				fieldRange.moveStart('character', this.element.value.length);
				fieldRange.collapse(false);
				fieldRange.select();
			}
		} else { // other browsers
			var length = this.element.value.length;
			this.element.setSelectionRange(length, length);
		}
    },
	
	onElementClick: function(event) {
		if (this.active) {
			this.hide();
			if (this.hideObserver) clearTimeout(this.hideObserver);
			if (this.blurObserver) clearTimeout(this.blurObserver);
			this.element.focus();
		} else {
			if (this.hideObserver) clearTimeout(this.hideObserver);
			if (this.blurObserver) clearTimeout(this.blurObserver);
			this.clientSideModeUpdate(this.noFilter);
		}
	},

    render: function() {
        if (this.entryCount > 0) {
			this.updateSelectedIndex();
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
            }
        } else {
            this.hide();
        }
    },
	
	markFirst: function() {
		this.index = -1;
		this.markNext();
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
	
	markFirstMatch: function() {
		// we skip disabled entries
		var found = false;
		var i;
		for (i = 0; i < this.entryCount; i++) {
			var entry = this.getEntry(i);
			if (entry && !ice.ace.jq(entry).hasClass('ui-state-disabled')) {
				var labelRoot = ice.ace.jq(entry).children('.'+ice.ace.ComboBox.LABEL_CLASS).get(0);
				if (labelRoot) {
					var entryValue = ice.ace.ComboBox.collectTextNodesIgnoreClass(labelRoot, ice.ace.ComboBox.IGNORE_CLASS);
					if (entryValue) {
						entryValue = ice.ace.jq.trim(entryValue);
						entryValue = entryValue.toLowerCase();
						var mainValue = this.element.value.toLowerCase();
						if (entryValue.indexOf(mainValue) == 0) {
							found = true;
							break;
						}
					}
				}
			}
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
				if (itemTop < 0 || itemTop > $update.height()) {
					$update.scrollTop($update.scrollTop() + itemTop);
				}
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
	
	getEntryFromContent: function(index) {
		if (index >= 0) {
			return this.$content.get(index);
		} else {
			return null;
		}
	},

    getCurrentEntry: function() {
        return this.getEntry(this.index);
    },

    selectEntry: function() {
        var idx = -1;
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
        value = ice.ace.ComboBox.collectTextNodesIgnoreClass(selectedEntry, ice.ace.ComboBox.LABEL_CLASS);

		this.updateValue(value);
        this.element.focus();
    },

    updateChoices: function(choices) {
        if (!this.changed && this.hasFocus) {
            this.update.innerHTML = choices;
			this.calculateListPosition();
            ice.ace.ComboBox.cleanWhitespace(this.update);
            ice.ace.ComboBox.cleanWhitespace(this.update.firstChild);

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
        if (ice.ace.ComboBox.Browser.IE || ice.ace.ComboBox.Browser.WebKit)
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
	
	clientSideModeUpdate: function(filter) {
		
		var model = this.clientSideModeCfg.model;
		var length = model.length;
		var caseSensitive = this.clientSideModeCfg.caseSensitive;
		var rows = this.clientSideModeCfg.rows;
		var value = this.element.value;
		if (!caseSensitive) value = value.toLowerCase();
		if (!filter) { 
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
		}
		
		var rowCount = 0;
		var result = ice.ace.jq('<div />');
		for (var i = 0; i < length; i++) {
			if (model[i]) {
				var item = caseSensitive ? model[i] : model[i].toLowerCase();
				if (filter(item, value)) {
					rowCount++;
					result.append(this.$content.get(i).cloneNode(true));
				}
				if (rowCount >= rows) break;
			}
		}
		this.updateNOW('<div>'+result.html()+'</div>');
		if (filter == this.noFilter) {
			this.markFirstMatch();
		} else {
			this.markFirst();
		}
		this.render();
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
	
	setContent: function(content) {
		this.content = content;
		this.$content = ice.ace.jq(this.content).children();
		this.entryCount = this.$content.size();
		
		if (this.clientSideModeCfg) {
			var model = [];
			var markup = [];
			var n = this.entryCount;
			var i;
			for (i = 0; i < n; i++) {
				var entry = this.$content.get(i);
				if (entry && !ice.ace.jq(entry).hasClass('ui-state-disabled')) {
					var labelRoot = ice.ace.jq(entry).children('.'+ice.ace.ComboBox.LABEL_CLASS).get(0);
					if (labelRoot) {
						var entryValue = ice.ace.ComboBox.collectTextNodesIgnoreClass(labelRoot, ice.ace.ComboBox.IGNORE_CLASS);
						if (entryValue) {
							entryValue = ice.ace.jq.trim(entryValue);
						} else {
							entryValue = '';
						}
						model.push(entryValue);
					}
				} else {
					model.push(null);
				}
			}
			this.clientSideModeCfg.model = model;
		}
	},

    updateNOW: function(text) {

		if (!text) return;
        this.hasFocus = true;
        ice.ace.ComboBox.cleanWhitespace(this.update);
		this.updateChoices(text);
		this.show();
		this.render();
		this.element.focus();
    },
	
	updateValue: function(value) {
		if (value) {
			this.hidden.value = value;
		} else {
			this.hidden.value = '';
			var element = ice.ace.jq(this.element);
			if (this.cfg.inFieldLabel) {
				this.element.value = this.cfg.inFieldLabel;
				element.addClass(this.cfg.inFieldLabelStyleClass);
				element.data("labelIsInField", true);
			} else {
				this.element.value = '';
			}
		}
		this.updateSelectedIndex();
		// update label
		if (value) {
			var currentEntry = this.getEntryFromContent(this.selectedIndex);
			if (currentEntry) {
				var labelSpan = ice.ace.jq(currentEntry).find('.'+ice.ace.ComboBox.LABEL_CLASS).get(0);
				var label = ice.ace.ComboBox.collectTextNodesIgnoreClass(labelSpan, ice.ace.ComboBox.IGNORE_CLASS);
				this.element.value = label;
			} else {
				this.element.value = value;
			}
		}
	},
	
	// update selected index if value was changed programmatically or was pre-selected
	updateSelectedIndex: function() {
		if (typeof this.selectedIndex != 'number' && !this.selectedIndex) this.selectedIndex = -1;
		var found = false;
		var size = this.$content.length;
		for (var i = 0; i < size; i++) {
			var entry = this.getEntryFromContent(i);
			if (entry && (this.hidden.value == ice.ace.ComboBox.collectTextNodesIgnoreClass(entry, ice.ace.ComboBox.LABEL_CLASS))) {
				found = true;
				this.selectedIndex = i;
				break;
			}
		}
		if (!found) this.selectedIndex = -1;
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
