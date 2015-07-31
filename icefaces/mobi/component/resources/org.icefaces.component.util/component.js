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

/* ECMAScript 5 bind */
if (!Function.prototype.bind) {
    Function.prototype.bind = function (oThis) {
        if (typeof this !== "function") {
            // closest thing possible to the ECMAScript 5 internal IsCallable function
            throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
        }

        var aArgs = Array.prototype.slice.call(arguments, 1),
                fToBind = this,
                fNOP = function () {},
                fBound = function () {
                    return fToBind.apply(this instanceof fNOP && oThis
                            ? this
                            : oThis,
                            aArgs.concat(Array.prototype.slice.call(arguments)));
                };

        fNOP.prototype = this.prototype;
        fBound.prototype = new fNOP();

        return fBound;
    };
}

if (!('filter' in Array.prototype)) {
    Array.prototype.filter= function(filter, that /*opt*/) {
        var other= [], v;
        for (var i=0, n= this.length; i<n; i++)
            if (i in this && filter.call(that, v= this[i], i, this))
                other.push(v);
        return other;
    };
}
if (!('map' in Array.prototype)) {
    Array.prototype.map= function(mapper, that /*opt*/) {
        var other= new Array(this.length);
        for (var i= 0, n= this.length; i<n; i++)
            if (i in this)
                other[i]= mapper.call(that, this[i], i, this);
        return other;
    };
}
if (!('indexOf' in Array.prototype)) {
    Array.prototype.indexOf= function(find, i /*opt*/) {
        if (i===undefined) i= 0;
        if (i<0) i+= this.length;
        if (i<0) i= 0;
        for (var n= this.length; i<n; i++)
            if (i in this && this[i]===find)
                return i;
        return -1;
    };
}
if (!('lastIndexOf' in Array.prototype)) {
    Array.prototype.lastIndexOf= function(find, i /*opt*/) {
        if (i===undefined) i= this.length-1;
        if (i<0) i+= this.length;
        if (i>this.length-1) i= this.length-1;
        for (i++; i-->0;) /* i++ because from-argument is sadly inclusive */
            if (i in this && this[i]===find)
                return i;
        return -1;
    };
}
if (!('forEach' in Array.prototype)) {
    Array.prototype.forEach= function(action, that /*opt*/) {
        for (var i= 0, n= this.length; i<n; i++)
            if (i in this)
                action.call(that, this[i], i, this);
    };
}

if (!window.ice) {
    window.ice = {};
}
if (!window.ice.mobi) {
    window.ice.mobi = {};
}
//should be in ice.mobi namespace
if (!window['mobi']) {
    window.mobi = {};
}
if (!window.ice.mobi.impl) {
    window.ice.mobi.impl = {};
}

if (!window.console) {
    console = {};
    if (ice.logInContainer) {
        console.log = ice.logInContainer;
    } else {
        log = function() {
        };
    }
}

ice.mobi.escapeJsfId = function(id) {
    return id.replace(/:/g,"\\:");
}
ice.mobi.windowHeight = function(){
    return window.innerHeight || document.documentElement.clientHeight;
}
ice.mobi.addListener= function(obj, event, fnc){
    if (obj.addEventListener){
        obj.addEventListener(event, fnc, false);
    } else if (obj.attachEvent) {
        obj.attachEvent("on"+event, fnc);
    } else {
        ice.log.debug(ice.log, 'WARNING:- this browser does not support addEventListener or attachEvent');
    }
};
ice.mobi.isNumber = function(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}
ice.mobi.removeListener= function(obj, event, fnc){
    if (obj.addEventListener){
        obj.removeEventListener(event, fnc, false);
    } else if (obj.attachEvent){
        obj.detachEvent("on"+event, fnc);
    } else {
        ice.log.debug(ice.log, 'WARNING cannot remove listener for event='+event+' node='+obj);
    }
};
/** used by dataview **/
ice.mobi.matches = function(elem, selector) {
    var impl = elem.webkitMatchesSelector || elem.msMatchesSelector || elem.mozMatchesSelector;
    if( impl && impl.bind ){
        return (impl.bind(elem))(selector);
    }
    else{
        return Array.prototype.indexOf.call(document.querySelectorAll(selector), elem) > -1;
    }
};

ice.mobi.panelCenter = function(clientId, cfg){
    var paneNode = document.getElementById(clientId);
    var containerElem = cfg.containerElem || null;
    if (!paneNode){
       console.log ("Element Not Found id="+clientId);
       return;
    }
    var container =document.getElementById(containerElem);
    var contWidth;
    var contHeight;
    var elemWidth = cfg.width || paneNode.offsetWidth;
    var styleVar = "";
    var elemHeight = cfg.height|| paneNode.offsetHeight;
    if (cfg.width){
        var wStr = elemWidth+"px";
        styleVar += " width: "+wStr+";";
    }
    if (cfg.height){
        var hStr = elemHeight+"px";
        styleVar += " height: "+hStr+";";
    }
    var contWidth;
    var contHeight;
    if (container){
        contWidth = container.offsetWidth;
        contHeight = container.offsetHeight;
    } else {
        contWidth = mobi._windowWidth();
        contHeight = mobi._windowHeight();
    }
    var scrollTop = document.body.scrollTop;
    if (scrollTop == 0){
        scrollTop = document.documentElement.scrollTop;
    }
    if (contHeight > 0){
        var posStyle = " position: absolute;";
        var posLeft =((contWidth/2)-(elemWidth/2))+'px';
        var top = scrollTop +((contHeight/2)-(elemHeight/2))+'px';
        if (contHeight - elemHeight >0){
            styleVar += posStyle;
            styleVar += " top: " +top +";";
            styleVar +=" left:"+posLeft+";";
        }else {
            styleVar += posStyle;
            styleVar +=" left:"+posLeft+";";
        }
        if (cfg.style){
            styleVar+=cfg.style;
        }
        paneNode.setAttribute('style',styleVar);
    }  else {
        ice.log.debug(ice.log," Containing div or window has no height to autocenter popup of id="+clientId);
    }
};
mobi.panelAutoCenter = function (clientId) {
    var windowWidth = mobi._windowWidth();
    var windowHeight = mobi._windowHeight();
    var scrollTop = document.body.scrollTop;
    if (scrollTop == 0) {
        scrollTop = document.documentElement.scrollTop;
    }
    if (windowHeight > 0) {
        var contentElement = document.getElementById(clientId);
        if (contentElement) {
            var contentHeight = contentElement.offsetHeight;
            var contentWidth = contentElement.offsetWidth;
            if (windowHeight - contentHeight > 0) {
                contentElement.style.position = 'absolute';
                contentElement.style.top = scrollTop + ((windowHeight / 2) - (contentHeight / 2)) + 'px';
                contentElement.style.left = ((windowWidth / 2) - (contentWidth / 2)) + 'px';
            } else {
                contentElement.style.position = 'absolute';
                contentElement.style.top = 0;
                contentElement.style.left = ((windowWidth / 2) - (contentWidth / 2)) + 'px';
            }
        }
    }
};
mobi._windowHeight = function () {
    var windowHeight = 0;
    if (typeof(window.innerHeight) == 'number') {
        windowHeight = window.innerHeight;
    } else {
        if (document.documentElement && document.documentElement.clientHeight) {
            windowHeight = document.documentElement.clientHeight;
        } else {
            if (document.body && document.body.clientHeight) {
                windowHeight = document.body.clientHeight;
            }
        }
    }
    return windowHeight;
};
mobi._windowWidth = function () {
    var windowWidth = 0;
    if (typeof(window.innerWidth) == 'number') {
        windowWidth = window.innerWidth;
    } else {
        if (document.documentElement && document.documentElement.clientWidth) {
            windowWidth = document.documentElement.clientWidth;
        } else {
            if (document.body && document.body.clientWidth) {
                windowWidth = document.body.clientWidth;
            }
        }
    }
    return windowWidth;
};

ice.mobi.formOf = function(element) {
    var parent = element;
    while (null != parent) {
        if ("form" == parent.nodeName.toLowerCase()) {
            return parent;
        }
        parent = parent.parentNode;
    }
}

ice.mobi.unpackDeviceResponse = function (data)  {
    var result = {};
    var params = data.split("&");
    var len = params.length;
    for (var i = 0; i < len; i++) {
        var splitIndex = params[i].indexOf("=");
        var paramName = unescape(params[i].substring(0, splitIndex));
        var paramValue = unescape(params[i].substring(splitIndex + 1));
        if ("!" === paramName.substring(0,1))  {
            //ICEmobile parameters are set directly
            result[paramName.substring(1)] = paramValue;
        } else  {
            //only one user value is supported
            result.name = paramName;
            result.value = paramValue;
        }
    }
    return result;
}

ice.mobi.setInput = function(target, name, value, vtype)  {
    var hiddenID = name + "-hid";
    var existing = document.getElementById(hiddenID);
    if (existing)  {
        existing.setAttribute("value", value);
        return;
    }
    var targetElm = document.getElementById(target);
    if (!targetElm)  {
        return;
    }
    var hidden = document.createElement("input");

    hidden.setAttribute("type", "hidden");
    hidden.setAttribute("id", hiddenID);
    hidden.setAttribute("name", name);
    hidden.setAttribute("value", value);
    if (vtype)  {
        hidden.setAttribute("data-type", vtype);
    }
    targetElm.parentNode.insertBefore(hidden, targetElm);
}

ice.mobi.getStyleSheet = function (sheetId) {
    return Array.prototype.filter.call(document.styleSheets, function(s) {
        return s.title == sheetId;
    })[0];
};

ice.mobi.addStyleSheet = function (sheetId, parentSelector) {
    var s = document.createElement('style');
    s.type = 'text/css';
    s.rel = 'stylesheet';
    s.title = sheetId;
    document.querySelectorAll(parentSelector || "head")[0].appendChild(s);
    return ice.mobi.getStyleSheet(sheetId);
};


/* touch active state support */
ice.mobi.addListener(document, "touchstart", function(){});

(function(){
    ice.mobi.sizePagePanelsToViewPort = function(){
        var desktop = document.documentElement.className.indexOf('desktop') > -1;
        if( !desktop ){
            var pagePanels = document.querySelectorAll(".mobi-pagePanel"), i=0;
            while( i < pagePanels.length ){
                var hasHeader = pagePanels[i].querySelectorAll(".mobi-pagePanel-header").length > 0;
                var hasFixedHeader = pagePanels[i].querySelectorAll(".mobi-pagePanel-header.ui-header-fixed").length > 0;
                var hasFooter = pagePanels[i].querySelectorAll(".mobi-pagePanel-footer").length > 0;
                var hasFixedFooter = pagePanels[i].querySelectorAll(".mobi-pagePanel-footer.ui-footer-fixed").length > 0;
                var pagePanelBodyMinHeight = window.innerHeight;
                if( hasHeader && !hasFixedHeader ){
                    pagePanelBodyMinHeight -= 40;
                }
                if( hasFooter && !hasFixedFooter ){
                    pagePanelBodyMinHeight -= 40;
                }
                var body = pagePanels[i].querySelector(".mobi-pagePanel-body");
                if( body ){
                    body.style.minHeight = ''+pagePanelBodyMinHeight+'px';
                }
                i++;
            }

        }

    };
    if( window.innerHeight ){
        window.addEventListener('load', ice.mobi.sizePagePanelsToViewPort);
        ice.mobi.addListener(window,"orientationchange",ice.mobi.sizePagePanelsToViewPort);
        ice.mobi.addListener(window,"resize",ice.mobi.sizePagePanelsToViewPort);
    }
}());

ice.mobi.fallback = {};
ice.mobi.fallback.setupLaunchFailed = function(regularId, fallbackId) {
	bridgeit.launchFailed = function() {
		document.getElementById(regularId).style.display = 'none';
		document.getElementById(fallbackId).style.display = 'inline';
		bridgeit.launchFailed = function() {};
	};
	var checkTimeout = setTimeout(function() {
		clearTimeout(checkTimeout);
		bridgeit.launchFailed = function() {};	
	}, 3500);
};


ice.mobi.button = function(id) {
	var self = this;
	this.buttonElement = document.getElementById(id);
	var baseClasses = 'ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only';

	this.addClass(baseClasses);
	this.buttonElement.setAttribute('role', 'button');
	this.buttonElement.firstChild.className = 'ui-button-text';

	if (this.buttonElement.disabled) {
		this.addClass('ui-state-disabled');
	} else {
		this.buttonElement.onmouseenter = function(e) {	self.addClass('ui-state-hover'); };
		this.buttonElement.onmouseleave = function(e) {	self.removeClass('ui-state-hover');self.removeClass('ui-state-active'); };
		this.buttonElement.onfocus = function(e) { self.addClass('ui-state-focus'); };
		this.buttonElement.onblur = function(e) { self.removeClass('ui-state-focus'); };
		this.buttonElement.onmousedown = function(e) { self.addClass('ui-state-active'); };
		this.buttonElement.onmouseup = function(e) { self.removeClass('ui-state-active'); };
		this.buttonElement.onkeydown = function(e) { 
			if ( e.keyCode == 32 || e.keyCode == 13 ) {
				self.addClass('ui-state-active');
			}
		};
		this.buttonElement.onkeyup = function(e) { self.removeClass('ui-state-active'); };
	}
};
ice.mobi.button.prototype.addClass = function(className) {
	this.buttonElement.className += ' ' + className;
};
ice.mobi.button.prototype.removeClass = function(className) {
	this.buttonElement.className = this.buttonElement.className.replace(new RegExp('(?:^|\\\s)'+className+'(?!\\\S)','g') , '');
};

ice.mobi.setupImageButton = function(id) {
	var buttonElement = document.getElementById(id);
	buttonElement.style.border = '0';
	buttonElement.style.backgroundColor = 'transparent';
};

ice.mobi.fallback = {};
ice.mobi.fallback.setupLaunchFailed = function(regularId, fallbackId) {
	bridgeit.launchFailed = function() {
		document.getElementById(regularId).style.display = 'none';
		document.getElementById(fallbackId).style.display = 'inline';
		bridgeit.launchFailed = function() {};
	};
	var checkTimeout = setTimeout(function() {
		clearTimeout(checkTimeout);
		bridgeit.launchFailed = function() {};	
	}, 3500);
};