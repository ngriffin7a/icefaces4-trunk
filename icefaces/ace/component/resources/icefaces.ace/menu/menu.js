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
* Code Modification 2: [ADD BRIEF DESCRIPTION HERE] 
* Contributors: ______________________ 
* Contributors: ______________________ 
* 
*/

/**
 *  Menubar Widget
 */
ice.ace.Menubar = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.jq = ice.ace.jq(this.jqId + " > ul");

    if(!this.cfg.autoSubmenuDisplay) {
        this.cfg.trigger = this.jqId + ' li';
        this.cfg.triggerEvent = 'click';
    }

    var _self = this;
    this.cfg.select = function(event, ui) {
        _self.jq.wijmenu('deactivate');
    };
	
	// determine X and Y directions
	var direction = this.cfg.direction;
	var left = direction.search(/left/i);
	var right = direction.search(/right/i);
	var center = direction.search(/center/i);
	if (left > -1) {
		if ((right > -1 && right < left) || (center > -1 && center < left)) {}
		else this.cfg.directionX = 'left';
	}
	if (right > -1) {
		if ((left > -1 && left < right) || (center > -1 && center < right)) {}
		else this.cfg.directionX = 'right';
	}
	if (center > -1) {
		if ((left > -1 && left < center) || (right > -1 && right < center)) {}
		else this.cfg.directionX = 'center';
	}
	if (!this.cfg.directionX) this.cfg.directionX = 'auto';

	var up = direction.search(/up/i);
	var down = direction.search(/down/i);
	var middle = direction.search(/middle/i);
	if (up > -1) {
		if ((down > -1 && down < up) || (middle > -1 && middle < up)) {}
		else this.cfg.directionY = 'up';
	}
	if (down > -1) {
		if ((up > -1 && up < down) || (middle > -1 && middle < down)) {}
		else this.cfg.directionY = 'down';
	}
	if (middle > -1) {
		if ((up > -1 && up < middle) || (down > -1 && down < middle)) {}
		else this.cfg.directionY = 'middle';
	}
	if (!this.cfg.directionY) this.cfg.directionY = 'auto';
	
    this.cfg.position = {
            my: 'left top',
            using: function(to) {

			// default values
			var _myFirst = 'left top';
			var _atFirst = 'left bottom';
			var _collisionFirst = 'flip';
			var _my = 'left top';
			var _at = 'right top';
			var _collision = 'flip';
			
			
			if (_self.cfg.directionX == 'auto' && _self.cfg.directionY == 'auto') { // use default values
				// do nothing
			} else { // construct new value strings
				// process horizontal direction
				if (_self.cfg.directionX == 'left') {
					_myFirst = 'right ';
					_atFirst = 'right ';
					_collisionFirst = 'none ';
					_my = 'right ';
					_at = 'left ';
					_collision = 'none ';
				} else if (_self.cfg.directionX == 'right') {
					_myFirst = 'left ';
					_atFirst = 'left ';
					_collisionFirst = 'none ';
					_my = 'left ';
					_at = 'right ';
					_collision = 'none ';
				} else if (_self.cfg.directionX == 'center') {
					_myFirst = 'center ';
					_atFirst = 'center ';
					_collisionFirst = 'none ';
					_my = 'left ';
					_at = 'right ';
					_collision = 'flip ';
				} else {
					_myFirst = 'left ';
					_atFirst = 'left ';
					_my = 'left ';
					_at = 'right ';
				}
				// process vertical direction
				if (_self.cfg.directionY == 'up') {
					_myFirst += 'bottom';
					_atFirst += 'top';
					_collisionFirst += 'none';
					_my += 'bottom';
					_at += 'bottom';
					_collision += 'none';
				} else if (_self.cfg.directionY == 'down') {
					_myFirst += 'top';
					_atFirst += 'bottom';
					_collisionFirst += 'none';
					_my += 'top';
					_at += 'top';
					_collision += 'none';
				} else if (_self.cfg.directionY == 'middle') {
					_myFirst += 'top';
					_atFirst += 'bottom';
					_collisionFirst += 'flip';
					_my += 'center';
					_at += 'center';
					_collision += 'none';
				} else {
					_myFirst += 'top';
					_atFirst += 'bottom';
					_my += 'top';
					_at += 'top';
				}
			}
			
			var _this = ice.ace.jq(this);
			if (!_this.parent().get(0)) return;
			if (_this.parent().get(0).id == _self.id) { // root menu
				// do nothing
			} else { // submenus
				var isFirstSubmenu = function(item) { // utility function
					var ulParents = item.parentsUntil(ice.ace.jq(_self.jqId), 'ul');
					return ulParents.size() == 1;
				};
				
				var relativeToMenubar = false;
				if (_this.attr('relativeto')) {
					if (_this.attr('relativeto') == 'menubar') {
						relativeToMenubar = true;
					}
				}
				_this.css('list-style-type', 'none');
				var _item = relativeToMenubar ? _self.jq.parent().parent() : _this.parents('li:first');
				var _itemLabel = _this.parents('li:first');
				var offset = _item.offset();
				if (_self.cfg.directionX == 'auto') {
					if (ice.ace.ContextMenu.shouldDisplayLeft(offset.left, _this.width(), _item.width())) {
						_collision = 'flip '; _collisionFirst = 'flip ';
					} else {
						_collision = 'none '; _collisionFirst = 'none ';
					}
				}
				if (_self.cfg.directionY == 'auto') {
					if (ice.ace.ContextMenu.shouldDisplayAbove(offset.top, _this.height())) {
						_collision += 'flip'; _collisionFirst += 'flip';
					} else {
						_collision += 'none'; _collisionFirst += 'none';
					}
				}
				if (isFirstSubmenu(_itemLabel)) { // first submenu level
					_this.position({
						my: _myFirst,
						at: _atFirst,
						of: _item.get(0),
						collision: _collisionFirst
					});				
				} else { // deeper submenu levels
					_this.position({
						my: _my,
						at: _at,
						of: _item.get(0),
						collision: _collision
					});
				}
				if (_this.attr('top')) {
					if (relativeToMenubar) {
						var menubarTop = _self.jq.parent().parent().offset().top;
						var labelTop = _itemLabel.offset().top;
						var adjustmentTop = menubarTop - labelTop;
						var totalTop = parseInt(_this.attr('top')) + adjustmentTop;
						_this.css('top', totalTop + 'px');					
					} else {
						_this.css('top', _this.attr('top') + 'px');
					}
				}
				if (_this.attr('left')) {
					if (relativeToMenubar) {
						var menubarLeft = _self.jq.parent().parent().offset().left;
						var labelLeft = _itemLabel.offset().left;
						var adjustmentLeft = menubarLeft - labelLeft;
						var totalLeft = parseInt(_this.attr('left')) + adjustmentLeft;
						_this.css('left', totalLeft + 'px');
					} else {
						_this.css('left', _this.attr('left') + 'px');
					}
				}
			}
            }
        }


    this.jq.wijmenu(this.cfg);
	ice.ace.jq(this.jqId).attr('style', '');

    if(this.cfg.style)
        this.jq.parent().parent().attr('style', this.cfg.style);
    if(this.cfg.styleClass)
        this.jq.parent().parent().addClass(this.cfg.styleClass);
}

/**
 *  Menubar Widget
 */
ice.ace.Menu = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.jq = ice.ace.jq(this.jqId + ' ul:first');

    this.cfg.orientation = 'vertical';

    if(this.cfg.position == 'dynamic') {
        this.cfg.position = {
            my: this.cfg.my
            ,at: this.cfg.at
        }

        this.cfg.trigger = ice.ace.escapeClientId(this.cfg.trigger);
    }
	
	this.cfg.menuComponent = 'true';

    var _self = this;
    this.cfg.select = function(event, ui) {
        _self.jq.wijmenu('deactivate');
    };

    this.jq.wijmenu(this.cfg);
	ice.ace.jq(this.jqId).attr('style', '');

    this.element = this.jq.parent().parent();       //overlay element
    this.element.css('z-index', this.cfg.zindex);

    if(this.cfg.style)
        this.element.attr('style', this.cfg.style);
    if(this.cfg.styleClass)
        this.element.addClass(this.cfg.styleClass);
}

/*
 *  MenuButton Widget
 */
ice.ace.MenuButton = function(id, cfg) {
	this.id = id;
	this.cfg = cfg;
	this.jqId = ice.ace.escapeClientId(id);
	this.root = ice.ace.jq(this.jqId);
    this.jqbutton = ice.ace.jq(this.jqId + '_button');
    this.jqMenu = ice.ace.jq(this.jqId + ' ul:first');

    //menu options
    this.cfg.trigger = this.jqId + '_button';
    this.cfg.orientation = 'vertical';
    this.cfg.position = {
        my: 'left top'
        ,at: 'left bottom'
    };

    var _self = this;
    this.cfg.select = function(event, ui) {
        _self.jqMenu.wijmenu('deactivate');
    };

	ice.ace.jq(this.jqId + ' script').remove(); // remove script to avoid executing it again
	
    //crete button and menu
    this.jqbutton.button({icons:{primary:'ui-icon-triangle-1-s'}});
	var disabled = this.cfg.disabled;
	delete this.cfg.disabled; // avoid passing parameter to menu widget
    this.jqMenu.wijmenu(this.cfg);
	this.root.attr('style', '');

    if(disabled) {
        this.jqbutton.button('disable');
    }

    this.jqMenu.parent().parent().css('z-index', this.cfg.zindex);      //overlay element
	
	this.root.addClass('wijmo-wijmenu-menubutton');
	
    if(this.cfg.style)
        this.root.attr('style', this.cfg.style);
    if(this.cfg.styleClass)
        this.root.addClass(this.cfg.styleClass);
}

/*
 *  ContextMenu Widget
 */
ice.ace.ContextMenu = function(id, cfg) {

    //mouse tracking
    if(!ice.ace.ContextMenu.mouseTracking) {
        ice.ace.ContextMenu.mouseTracking = true;

        ice.ace.jq(document).mousemove(function(e){
            ice.ace.ContextMenu.pageX = e.pageX;
            ice.ace.ContextMenu.pageY = e.pageY;
            ice.ace.ContextMenu.event = e;
        });
    }
	
	if (!cfg.forDelegate) {
		this.initialize(id, cfg);
	} else {
		var delegateContainer = ice.ace.jq(ice.ace.escapeClientId(cfg.forDelegate));
		var delegateNode = delegateContainer.children().get(0);
		delegateContainer.undelegate('*', 'contextmenu').delegate('*', 'contextmenu', function(event, ignoreEvent) {
			// 'this' in this scope refers to the current DOM node in the event bubble
			if (this === delegateNode && !ignoreEvent) { // event bubbled to the highest point, we can now begin
				var findTargetComponent = function(node) {
					if (node) {
						if (node.id && ice.ace.ContextMenu.endsWith(node.id, cfg.forComponent)) {
							return node.id;
						} else {
							return findTargetComponent(node.parentNode);
						}
					}
					return '';
				}
				var targetComponent = findTargetComponent(event.target);
				if (targetComponent) {
					var formId = ice.ace.jq(ice.ace.escapeClientId(id)).parents('form:first').attr('id');
					var options = {
						source: id,
						execute: id,
						formId: formId,
						async: true
					};

					var params = {};
					params[id + '_activeComponent'] = targetComponent;

					options.params = params;
					
					ice.ace.AjaxRequest(options);
				}
			}
		});
		if (cfg.showNow) {
			this.initialize(id, cfg);
			ice.ace.jq(this.cfg.trigger).trigger('contextmenu', [true]); // flag for delegate node to ignore this simulated event
		}
	}
};

ice.ace.ContextMenu.prototype.initialize = function(id, cfg) {
	this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.jq = ice.ace.jq(this.jqId + ' ul:first');

    //configuration
    this.cfg.orientation = 'vertical';
    this.cfg.triggerEvent = 'rtclick';
    this.cfg.trigger = typeof this.cfg.target == 'string' ? ice.ace.escapeClientId(this.cfg.target) : this.cfg.target;

    var _self = this;
	
	// determine X and Y directions
	var direction = this.cfg.direction;
	var left = direction.search(/left/i);
	var right = direction.search(/right/i);
	if (left >= 0 && right >= 0) {
		if (left < right) this.cfg.directionX = 'left';
	} else if (left >= 0) this.cfg.directionX = 'left';
	else if (right >= 0) this.cfg.directionX = 'right';
	else this.cfg.directionX = 'auto';

	var up = direction.search(/up/i);
	var down = direction.search(/down/i);
	if (up >= 0 && down >= 0) {
		if (up < down) this.cfg.directionY = 'up';
	} else if (up >= 0) this.cfg.directionY = 'up';
	else if (down >= 0) this.cfg.directionY = 'down';
	else this.cfg.directionY = 'auto';
	
    this.cfg.position = {
            my: 'left top',
            using: function(to) {
			// default values
			var _my = 'left top';
			var _at = 'right top';
			var _collision = 'flip';
			
			if (_self.cfg.directionX == 'auto' && _self.cfg.directionY == 'auto') { // use default values
				// do nothing
			} else { // construct new value strings
				// process horizontal direction
				if (_self.cfg.directionX == 'left') {
					_my = 'right ';
					_at = 'left ';
					_collision = 'none ';
				} else if (_self.cfg.directionX == 'right') {
					_my = 'left ';
					_at = 'right ';
					_collision = 'none ';
				} else {
					_my = 'left ';
					_at = 'right ';
				}
				// process vertical direction
				if (_self.cfg.directionY == 'up') {
					_my += 'bottom';
					_at += 'bottom';
					_collision += 'none';
				} else if (_self.cfg.directionY == 'down') {
					_my += 'top';
					_at += 'top';
					_collision += 'none';
				} else {
					_my += 'top';
					_at += 'top';
				}
			}
			
			var _this = ice.ace.jq(this);
			if (!_this.parent().get(0)) return;
			if (_this.parent().get(0).id == _self.id) { // root menu
				if (_self.cfg.directionX == 'auto') {
					if (ice.ace.ContextMenu.shouldDisplayLeft(ice.ace.ContextMenu.pageX, _this.width(), 0)) _collision = 'flip ';
					else _collision = 'none ';
				}
				if (_self.cfg.directionY == 'auto') {
					if (ice.ace.ContextMenu.shouldDisplayAbove(ice.ace.ContextMenu.pageY, _this.height())) _collision += 'flip';
					else _collision += 'none';
				}
				_this.position({
					my: _my,
					of: ice.ace.ContextMenu.event,
					collision: _collision
				});
			} else { // submenus
				_this.css('list-style-type', 'none');
				var _item = _this.parents('li:first');
				var offset = _item.offset();
				if (offset) {
					if (_self.cfg.directionX == 'auto') {
						if (ice.ace.ContextMenu.shouldDisplayLeft(offset.left, _this.width(), _item.width())) _collision = 'flip ';
						else _collision = 'none ';
					}
					if (_self.cfg.directionY == 'auto') {
						if (ice.ace.ContextMenu.shouldDisplayAbove(offset.top, _this.height())) _collision += 'flip';
						else _collision += 'none';
					}
				}
				_this.position({
					my: _my,
					at: _at,
					of: _item.get(0),
					collision: _collision
				});
			}
            }
        }

    this.cfg.select = function(event, ui) {
        _self.jq.wijmenu('deactivate');
    };
	
	if (!ice.ace.jq.support.leadingWhitespace) { // ICE-7532 not supported in IE 7/8
		if (this.cfg.animation) { 
			delete this.cfg.animation;
		}
	}

    this.jq.wijmenu(this.cfg);
	ice.ace.jq(this.jqId).attr('style', '');

    this.element = this.jq.parent().parent();   //overlay element
    this.element.css('z-index', this.cfg.zindex);

	this.element.addClass('wijmo-wijmenu-flyout');
	
    if(this.cfg.style)
        this.element.attr('style', this.cfg.style + ';' + this.element.attr('style'));
    if(this.cfg.styleClass)
        this.element.addClass(this.cfg.styleClass);
}

ice.ace.ContextMenu.shouldDisplayAbove = function(top, height) {
	var up = false;
	var winHeight = ice.ace.jq(window).height();
	var docHeight = ice.ace.jq(document).height();
	var scrollTop = ice.ace.jq(document).scrollTop()
	var lengthAbove = top - scrollTop;
	var lengthBelow = scrollTop + winHeight - top;
	if (lengthBelow < height) {
		if (lengthAbove >= height)
			up = true;
	}
	return up;
}

ice.ace.ContextMenu.shouldDisplayLeft = function(left, width, itemWidth) {
	var leftside = false;
	var winWidth = ice.ace.jq(window).width();
	var docWidth = ice.ace.jq(document).width();
	var scrollLeft = ice.ace.jq(document).scrollLeft()
	var lengthLeft = left - scrollLeft;
	var lengthRight = scrollLeft + winWidth - left - itemWidth;
	if (lengthRight < width) {
		if (lengthLeft >= width)
			leftside = true;
	}
	return leftside;
}

ice.ace.ContextMenu.endsWith = function(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
};

ice.ace.BreadcrumbMenu = function (id, cfg) {
    var $ul = ice.ace.jq(document.getElementById(id + "_ul"));
    $ul.wijmenu({
        orientation: "horizontal",
        backLink: false
    });
};
