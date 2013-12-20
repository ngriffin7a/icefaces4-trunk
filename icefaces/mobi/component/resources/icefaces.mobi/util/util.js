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

if (!window['ice']) {
    window.ice = {};
}
if (!window['ice']['ace']) {
    window.ice.ace = {};
}
if (!window.ice.ace['jq']) {
    ice.ace.jq = jQuery; /* bind our JQ impl merged above for later use*/
    jQuery.noConflict(true);
}
if (!window['ice']['ace']['util']) {
    window.ice.ace.util = {};
}


/* General JS Utilities *******************************************************/
ice.ace.util.clone = function(obj) {
    // Handle the 3 simple types, and null or undefined
    if (null == obj || "object" != typeof obj) return obj;

    if (obj instanceof Date) {
        var copy = new Date();
        copy.setTime(obj.getTime());
        return copy;
    }

    if (obj instanceof Array) {
        var copy = [];
        var len;
        for (var i = 0, len = obj.length; i < len; ++i) {
            copy[i] = ice.ace.util.clone(obj[i]);
        }
        return copy;
    }

    if (obj instanceof Object) {
        var copy = {};
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr)) copy[attr] = ice.ace.util.clone(obj[attr]);
        }
        return copy;
    }
    throw new Error("Unable to copy obj! Its type isn't supported.");
}

ice.ace.util.extend = function(t, s) {
    for (var attrname in s) { t[attrname] = s[attrname]; }
}
/******************************************************************************/



/* Element Selection Utilities ************************************************/
ice.ace.util.formOf = function(element) {
    var parent = element.parentNode;
    while (parent) {
        if (parent.tagName && parent.tagName.toLowerCase() == 'form') return parent;
        parent = parent.parentNode;
    }

    throw 'Cannot find enclosing form.';
}
/******************************************************************************/



/* Element Insertion Utilities ************************************************/
ice.ace.util.insertElementAtIndex = function(parentElem, insertElem, index) {
    if (!parentElem.hasChildNodes()) {
        parentElem.appendChild(insertElem);
    } else {
        var afterElem = parentElem.childNodes[index];
        if (afterElem) {
            parentElem.insertBefore(insertElem, afterElem);
        } else {
            parentElem.appendChild(insertElem);
        }
    }
};
/******************************************************************************/



/* Event Utilities ************************************************************/
ice.ace.util.isEventSourceInputElement = function(event) {
    var elem = ice.ace.util.eventTarget(event);
    var tag = elem.tagName.toLowerCase();
    if (tag == 'input' || tag == 'select' || tag == 'option' || tag == 'a' || tag == 'textarea' || tag == 'button') {
        return true;
    } else {
        return false;
    }
};

ice.ace.util.isEventSourceInputElementWithin = function(event, container) {
    var elem = ice.ace.util.eventTarget(event);
    while (elem && elem != container) {
        var tag = elem.tagName.toLowerCase();
        if (tag == 'input' || tag == 'select' || tag == 'option' || tag == 'a' || tag == 'textarea' || tag == 'button') {
            return true;
        }
        elem = elem.parentNode;
    }
    return false;
};

ice.ace.util.eventTarget = function(event) {
       event = event || window.event;           
       return(event.target || event.srcElement);
};

ice.ace.util.isMouseOver = function(elem, jqEvent) {
    elem = ice.ace.jq(elem);

    var offset = elem.offset(),
            xMin = offset.left,
            xMax = xMin + elem.outerWidth(),
            yMin = offset.top,
            yMax = yMin + elem.outerHeight(),
            mouseY = jqEvent.pageY,
            mouseX = jqEvent.pageX;

    return (mouseY < yMax && mouseY > yMin && mouseX < xMax && mouseX > xMin);
}

ice.ace.util.getEvent = function() {
    if (ice.ace.jq.browser.msie && ice.ace.jq.browser.version < 9) return window.event;

    var source = ice.ace.util.getEvent.caller; // firefox compatible caller usage
    while (source) {
        source = source.caller;
        if (source && source.arguments[0] instanceof Event)
            return source.arguments[0];
    }

    return null;
}
/******************************************************************************/



/* Array Utilities ************************************************************/

/* Given an array, return an array of all elements not contained in it*/
Array.prototype.diff = function(a) {
    return this.filter(function(i) {return !(a.indexOf(i) > -1);});
};

if ('function' !== typeof Array.prototype.reduce) {
    Array.prototype.reduce = function(callback, opt_initialValue){
        'use strict';
        if (null === this || 'undefined' === typeof this) {
            // At the moment all modern browsers, that support strict mode, have
            // native implementation of Array.prototype.reduce. For instance, IE8
            // does not support strict mode, so this check is actually useless.
            throw new TypeError(
                'Array.prototype.reduce called on null or undefined');
        }
        if ('function' !== typeof callback) {
            throw new TypeError(callback + ' is not a function');
        }
        var index = 0, length = this.length >>> 0, value, isValueSet = false;
        if (1 < arguments.length) {
            value = opt_initialValue;
            isValueSet = true;
        }
        for ( ; length > index; ++index) {
            if (!this.hasOwnProperty(index)) continue;
            if (isValueSet) {
                value = callback(value, this[index], index, this);
            } else {
                value = this[index];
                isValueSet = true;
            }
        }
        if (!isValueSet) {
            throw new TypeError('Reduce of empty array with no initial value');
        }
        return value;
    };
}

ice.ace.util.arrayIndexOf = function(arr, elem, fromIndex) {
	if (arr.indexOf) {
		return arr.indexOf(elem, fromIndex);
	}
	var len = arr.length;
	if (fromIndex == null) {
		fromIndex = 0;
	} else if (fromIndex < 0) {
		fromIndex = Math.max(0, len + fromIndex);
	}
	for (var i = fromIndex; i < len; i++) {
		if (arr[i] === elem) {
			return i;
		}
	}
	return -1;
};

// One level deep comparison, not deep recursive
ice.ace.util.arraysEqual = function(arr1, arr2) {
    if (!arr1 && !arr2) {
        return true;
    }
    else if (!arr1 || !arr2) {
        return false;
    }
    var len1 = arr1.length;
    var len2 = arr2.length;
    if (len1 != len2) {
        return false;
    }
    for (var i = 0; i < len1; i++) {
        if (arr1[i] !== arr2[i]) {
            return false;
        }
    }
    return true;
};
/******************************************************************************/



/* Style Utilities ************************************************************/
ice.ace.util.getStyleSheet = function (sheetId) {
    for (var i = 0; i < document.styleSheets.length; i++) {
        if (document.styleSheets[i].title == sheetId) return document.styleSheets[i];
    }
};

ice.ace.util.addStyleSheet = function (sheetId, parentSelector) {
    var s = document.createElement('style');
    s.type = 'text/css';
	if (s.styleSheet) { // IE
		s.styleSheet.cssText = "";
	} else {
		s.appendChild(document.createTextNode(""));
	}
    document.querySelector(parentSelector || "head").appendChild(s);

    if (ice.ace.jq.browser.safari || ice.ace.jq.browser.chrome) // must title after insertion to prevent chrome bug
        setTimeout(function() { s.title = sheetId; }, 1000);
    else
        s.title = sheetId;

    if (s.styleSheet) return s.styleSheet;
	return s.sheet;
};

ice.ace.util.getOpacity = function(elem) {
    var ori = ice.ace.jq(elem).css('opacity');
    var ori2 = ice.ace.jq(elem).css('filter');
    if (ori2) {
        ori2 = parseInt( ori2.replace(')','').replace('alpha(opacity=','') ) / 100;
        if (!isNaN(ori2) && ori2 != '') {
            ori = ori2;
        }
    }
    return ori;
};

ice.ace.util.bindHoverFocusStyle = function(input) {
    input.hover(
            function() { ice.ace.jq(this).addClass('ui-state-hover'); },
            function() { ice.ace.jq(this).removeClass('ui-state-hover'); }
    ).focus(
            function() { ice.ace.jq(this).addClass('ui-state-focus'); }
    ).blur(
            function() { ice.ace.jq(this).removeClass('ui-state-focus'); }
    );
};
/******************************************************************************/



/* Outside Utilities **********************************************************/
/**
 * jQuery Cookie plugin
 *
 * Copyright (c) 2010 Klaus Hartl (stilbuero.de)
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */
ice.ace.jq.cookie = function (key, value, options) {

    // key and value given, set cookie...
    if (arguments.length > 1 && (value === null || typeof value !== "object")) {
        options = ice.ace.jq.extend({}, options);

        if (value === null) {
            options.expires = -1;
        }

        if (typeof options.expires === 'number') {
            var days = options.expires, t = options.expires = new Date();
            t.setDate(t.getDate() + days);
        }

        return (document.cookie = [
            encodeURIComponent(key), '=',
            options.raw ? String(value) : encodeURIComponent(String(value)),
            options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
            options.path ? '; path=' + options.path : '',
            options.domain ? '; domain=' + options.domain : '',
            options.secure ? '; secure' : ''
        ].join(''));
    }

    // key and possibly options given, get cookie...
    options = value || {};
    var result, decode = options.raw ? function (s) {return s;} : decodeURIComponent;
    return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? decode(result[1]) : null;
};
/******************************************************************************/