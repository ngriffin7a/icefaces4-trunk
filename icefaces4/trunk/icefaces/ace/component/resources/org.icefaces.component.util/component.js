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

/* HTML 5 Element ClassList attribute */
if (typeof document !== "undefined" && !("classList" in document.createElement("a"))) {

    (function (view) {

        "use strict";

        if (!('HTMLElement' in view) && !('Element' in view)) return;

        var
                classListProp = "classList"
                , protoProp = "prototype"
                , elemCtrProto = (view.HTMLElement || view.Element)[protoProp]
                , objCtr = Object
                , strTrim = String[protoProp].trim || function () {
                    return this.replace(/^\s+|\s+$/g, "");
                }
                , arrIndexOf = Array[protoProp].indexOf || function (item) {
                    var
                            i = 0
                            , len = this.length
                            ;
                    for (; i < len; i++) {
                        if (i in this && this[i] === item) {
                            return i;
                        }
                    }
                    return -1;
                }
        // Vendors: please allow content code to instantiate DOMExceptions
                , DOMEx = function (type, message) {
                    this.name = type;
                    this.code = DOMException[type];
                    this.message = message;
                }
                , checkTokenAndGetIndex = function (classList, token) {
                    if (token === "") {
                        throw new DOMEx(
                                "SYNTAX_ERR"
                                , "An invalid or illegal string was specified"
                        );
                    }
                    if (/\s/.test(token)) {
                        throw new DOMEx(
                                "INVALID_CHARACTER_ERR"
                                , "String contains an invalid character"
                        );
                    }
                    return arrIndexOf.call(classList, token);
                }
                , ClassList = function (elem) {
                    var
                            trimmedClasses = strTrim.call(elem.className)
                            , classes = trimmedClasses ? trimmedClasses.split(/\s+/) : []
                            , i = 0
                            , len = classes.length
                            ;
                    for (; i < len; i++) {
                        this.push(classes[i]);
                    }
                    this._updateClassName = function () {
                        elem.className = this.toString();
                    };
                }
                , classListProto = ClassList[protoProp] = []
                , classListGetter = function () {
                    return new ClassList(this);
                }
                ;
// Most DOMException implementations don't allow calling DOMException's toString()
// on non-DOMExceptions. Error's toString() is sufficient here.
        DOMEx[protoProp] = Error[protoProp];
        classListProto.item = function (i) {
            return this[i] || null;
        };
        classListProto.contains = function (token) {
            token += "";
            return checkTokenAndGetIndex(this, token) !== -1;
        };
        classListProto.add = function () {
            var
                    tokens = arguments
                    , i = 0
                    , l = tokens.length
                    , token
                    , updated = false
                    ;
            do {
                token = tokens[i] + "";
                if (checkTokenAndGetIndex(this, token) === -1) {
                    this.push(token);
                    updated = true;
                }
            }
            while (++i < l);

            if (updated) {
                this._updateClassName();
            }
        };
        classListProto.remove = function () {
            var
                    tokens = arguments
                    , i = 0
                    , l = tokens.length
                    , token
                    , updated = false
                    ;
            do {
                token = tokens[i] + "";
                var index = checkTokenAndGetIndex(this, token);
                if (index !== -1) {
                    this.splice(index, 1);
                    updated = true;
                }
            }
            while (++i < l);

            if (updated) {
                this._updateClassName();
            }
        };
        classListProto.toggle = function (token, forse) {
            token += "";

            var
                    result = this.contains(token)
                    , method = result ?
                            forse !== true && "remove"
                            :
                            forse !== false && "add"
                    ;

            if (method) {
                this[method](token);
            }

            return !result;
        };
        classListProto.toString = function () {
            return this.join(" ");
        };

        if (objCtr.defineProperty) {
            var classListPropDesc = {
                get: classListGetter
                , enumerable: true
                , configurable: true
            };
            try {
                objCtr.defineProperty(elemCtrProto, classListProp, classListPropDesc);
            } catch (ex) { // IE 8 doesn't support enumerable:true
                if (ex.number === -0x7FF5EC54) {
                    classListPropDesc.enumerable = false;
                    objCtr.defineProperty(elemCtrProto, classListProp, classListPropDesc);
                }
            }
        } else if (objCtr[protoProp].__defineGetter__) {
            elemCtrProto.__defineGetter__(classListProp, classListGetter);
        }

    }(self));
}

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
ice.mobi.BUTTON_UNPRESSED = " ui-btn-up-c";
ice.mobi.BUTTON_PRESSED = " ui-btn-down-c";
mobi.BEHAVIOR_EVENT_PARAM = "javax.faces.behavior.event";
mobi.PARTIAL_EVENT_PARAM = "javax.faces.partial.event";
/* utilities*/
ice.mobi.swapClasses = function(aNode, c1, c2){
    if (!aNode.className){
        aNode.className = c2;
    }else if (ice.mobi.hasClass(aNode, c1)) {
        var tempClass = aNode.className;
        var temp = tempClass.replace(c1, c2 );
      //  console.log(" orig class="+ tempClass+" after replace temp="+temp);
        if (tempClass !== temp){
           aNode.className= temp;
        }
    }
};
ice.mobi.ready = function (callback) {
    if (document.addEventListener){
        document.addEventListener('DOMContentLoaded', callback, false);
    } else {
        window.attachEvent('onload', callback);
    }
};
ice.mobi.escapeJsfId = function(id) {
    return id.replace(/:/g,"\\:");
}
ice.mobi.addListener= function(obj, event, fnc){
    if (obj.addEventListener){
        obj.addEventListener(event, fnc, false);
    } else if (obj.attachEvent) {
        obj.attachEvent("on"+event, fnc);
    } else {
        ice.log.debug(ice.log, 'WARNING:- this browser does not support addEventListener or attachEvent');
    }
} ;
ice.mobi.removeListener= function(obj, event, fnc){
    if (obj.addEventListener){
        obj.removeEventListener(event, fnc, false);
    } else if (obj.attachEvent){
        obj.detachEvent("on"+event, fnc);
    } else {
        ice.log.debug(ice.log, 'WARNING cannot remove listener for event='+event+' node='+obj);
    }
};
ice.mobi.hasClass = function(ele, remove_cls) {
    return ele.className.replace( /(?:^|\s)remove_cls(?!\S)/ , '' );
};
mobi.findForm = function (sourceId) {
    if (!sourceId){
        console.log("source cannot be null to find form for ajax submit") ;
        return null;
    }
    var node = document.getElementById(sourceId);
    while (node.nodeName.toLowerCase() != "form" && node.parentNode) {
        node = node.parentNode;
    }
    ice.log.debug(ice.log, 'parent form node =' + node.name);
    return node;
};
/* copied from icemobile.js in jsp project for menuButton so can use same js */
ice.formOf = function formOf(element) {
    var parent = element;
    while (null != parent) {
        if ("form" == parent.nodeName.toLowerCase()) {
            return parent;
        }
        parent = parent.parentNode;
    }
}
ice.mobi.findFormFromNode = function (sourcenode) {
    if (!sourcenode){
        console.log("source cannot be null to find form for ajax submit") ;
        return null;
    }
    var node = sourcenode;
    while (node.nodeName.toLowerCase() != "form" && node.parentNode) {
        node = node.parentNode;
    }
    return node;
};

ice.mobi.matches = function(elem, selector) {
    var impl = elem.webkitMatchesSelector || elem.msMatchesSelector || elem.mozMatchesSelector;
    if( impl && impl.bind ){
        return (impl.bind(elem))(selector);
    }
    else{
        return Array.prototype.indexOf.call(document.querySelectorAll(selector), elem) > -1;
    }
};

mobi.AjaxRequest = function (cfg) {

    if (cfg.onstart && !cfg.onstart.call(this, cfg)) {
        return;//cancel request
    }
    var form = mobi.findForm(cfg.source);
    var source = (typeof cfg.source == 'string') ? document.getElementById(cfg.source) : cfg.source;
    if (!source) {
        if (cfg.node) {
            source = cfg.node;
            source.id = cfg.source;
        }
    }
    if (form.length == 0) {
        form = source.form;
        ice.log.debug(ice.log, "had to find form via element form length = " + form.length);
    }
    if (form.length == 0) {
        form = document.forms[0]; //just return first form in the page
        ice.log.debug(ice.log, 'had to find first form on page');
    }
    var jsfExecute = cfg.execute || '@all';
    var jsfRender = cfg.render || '@all';

    ice.fullSubmit(jsfExecute, jsfRender, null, source || form[0], function (parameter) {
        if (cfg.event) {
            parameter(mobi.BEHAVIOR_EVENT_PARAM, cfg.event);

            var domEvent = cfg.event;
            if (cfg.event == 'valueChange') {
                domEvent = 'change';
            } else if (cfg.event == 'action') {
                domEvent = 'click';
            }

            parameter(mobi.PARTIAL_EVENT_PARAM, domEvent);
        } else {
            parameter(cfg.source, cfg.source);
        }

        if (cfg.params) {
            var cfgParams = cfg.params;
            for (var p in cfgParams) {
                parameter(p, cfgParams[p]);
            }
        }
    }, function (onBeforeSubmit, onBeforeUpdate, onAfterUpdate, onNetworkError,
                 onServerError) {
        var context = {};
        onAfterUpdate(function (responseXML) {
            if (cfg.onsuccess && !cfg.onsuccess.call(context, responseXML, null /*status*/, null /*xhr*/)) {
                return;
            }
        });
        if (cfg.oncomplete) {
            onAfterUpdate(function (responseXML) {
                cfg.oncomplete.call(context, null /*xhr*/, null /*status*/, context.args);
            });
        }
        if (cfg.onerror) {
            onNetworkError(function (responseCode, errorDescription) {
                cfg.onerror.call(context, null /*xhr*/, responseCode /*status*/, errorDescription /*error description*/)
            });
            onServerError(function (responseCode, responseText) {
                cfg.onerror.call(context, null /*xhr*/, responseCode /*status*/, responseText /*error description*/)
            });
        }
    });
};
ice.mobi.extendAjaxArguments = function(callArguments, options) {
    // Return a modified copy of the original arguments instead of modifying the original.
    // The cb arguments, being a configured property of the component will live past this request.
    callArguments = ice.mobi.clone(callArguments);

    var params     = options.params,
        execute    = options.execute,
        render     = options.render,
        node       = options.node,
        onstart    = options.onstart,
        onerror    = options.onerror,
        onsuccess  = options.onsuccess,
        oncomplete = options.oncomplete;

    if (params) {
        if (callArguments['params'])
            ice.mobi.extend(callArguments['params'], params);
        else
            callArguments['params'] = params;
    }

    if (execute) {
        if (callArguments['execute'])
            callArguments['execute'] = callArguments['execute'] + " " + execute;
        else
            callArguments['execute'] = execute;
    }

    if (render) {
        if (callArguments['render'])
            callArguments['render'] = callArguments['render'] + " " + render;
        else
            callArguments['render'] = render;
    }

    if (node) {
        callArguments['node'] = node;
    }

    if (onstart) {
        if (callArguments['onstart']) {
            var existingStartCall = callArguments['onstart'];
            callArguments['onstart'] = function(xhr) {
                existingStartCall(xhr);
                onstart(xhr);
            }
        } else {
            callArguments['onstart'] = onstart;
        }
    }

    if (onerror) {
        if (callArguments['onerror']) {
            var existingErrorCall = callArguments['onerror'];
            callArguments['onerror'] = function(xhr, status, error) {
                existingErrorCall(xhr, status, error);
                onerror(xhr, status, error);
            }
        } else {
            callArguments['onerror'] = onerror;
        }
    }

    if (onsuccess) {
        if (callArguments['onsuccess']) {
            var existingSuccessCall = callArguments['onsuccess'];
            callArguments['onsuccess'] = function(data, status, xhr, args) {
                existingSuccessCall(data, status, xhr, args);
                onsuccess(data, status, xhr, args);
            }
        } else {
            callArguments['onsuccess'] = onsuccess;
        }
    }

    if (oncomplete) {
        if (callArguments['oncomplete']) {
            var existingCompleteCall = callArguments['oncomplete'];
            callArguments['oncomplete'] = function(xhr, status, args) {
                existingCompleteCall(xhr, status, args);
                oncomplete(xhr, status, args);
            }
        } else {
            callArguments['oncomplete'] = oncomplete;
        }
    }

    return callArguments;
};
ice.mobi.clone = function(obj) {
    // Handle the 3 simple types, and null or undefined
    if (null == obj || "object" != typeof obj) return obj;

    // Handle Date
    if (obj instanceof Date) {
        var copy = new Date();
        copy.setTime(obj.getTime());
        return copy;
    }

    // Handle Array
    if (obj instanceof Array) {
        var copy = [];
        var len;
        for (var i = 0, len = obj.length; i < len; ++i) {
            copy[i] = ice.mobi.clone(obj[i]);
        }
        return copy;
    }

    // Handle Object
    if (obj instanceof Object) {
        var copy = {};
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr)) copy[attr] = ice.mobi.clone(obj[attr]);
        }
        return copy;
    }

    throw new Error("Unable to copy obj! Its type isn't supported.");
};
ice.mobi.ab = function(cfg) { mobi.AjaxRequest(cfg); };
ice.mobi.extend = function(targetObject, sourceObject) {
    for (var attrname in sourceObject) { targetObject[attrname] = sourceObject[attrname]; }
}
mobi.registerAuxUpload = function (sessionid, uploadURL) {

    var splashClause = ice.mobi.impl.getSplashClause();

    var sxURL = "icemobile:c=register&r=" +
                        escape(window.location) + "&JSESSIONID=" + sessionid +
                        splashClause +
                        "&u=" + escape(uploadURL);

    if (window.chrome)  {
        window.location.href = sxURL;
        return;
    }

    var auxiframe = document.getElementById('auxiframe');
    if (null == auxiframe) {
        auxiframe = document.createElement('iframe');
        auxiframe.setAttribute("id", "auxiframe");
        auxiframe.setAttribute("style", "width:0px; height:0px; border: 0px");
        auxiframe.setAttribute("src", sxURL);
        document.body.appendChild(auxiframe);
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

ice.mobi.serialize = function(form, typed) {
    var els = form.elements;
    var len = els.length;
    var qString = [];
    var addField = function(name, value) {
        var tmpStr = "";
        if (qString.length > 0) {
            tmpStr = "&";
        }
        tmpStr += encodeURIComponent(name) + "=" + encodeURIComponent(value);
        qString.push(tmpStr);
    };
    for (var i = 0; i < len; i++) {
        var el = els[i];
        if (!el.disabled) {
            var prefix = "";
            if (typed) {
                var vtype = el.getAttribute("data-type");
                if (vtype) {
                    prefix = vtype + "-";
                } else {
                    prefix = el.type + "-";
                }
            }
            switch (el.type) {
                case 'submit':
                case 'button':
                case 'fieldset':
                    break;
                case 'text':
                case 'password':
                case 'hidden':
                case 'textarea':
                    addField(prefix + el.name, el.value);
                    break;
                case 'select-one':
                    if (el.selectedIndex >= 0) {
                        addField(prefix + el.name, el.options[el.selectedIndex].value);
                    }
                    break;
                case 'select-multiple':
                    for (var j = 0; j < el.options.length; j++) {
                        if (el.options[j].selected) {
                            addField(prefix + el.name, el.options[j].value);
                        }
                    }
                    break;
                case 'checkbox':
                case 'radio':
                    if (el.checked) {
                        addField(prefix + el.name, el.value);
                    }
                    break;
                default:
                    addField(prefix + el.name, el.value);
            }
        }
    }
    // concatenate the array
    return qString.join("");
}

function html5getViewState(form) {
    if (!form) {
        throw new Error("jsf.getViewState:  form must be set");
    }
    return ice.mobi.serialize(form, false);
}

function html5handleResponse(context, data) {
    if (null == context.sourceid) {
        //was not a jsf upload
        return;
    }

    var jsfResponse = {};
    var parser = new DOMParser();
    var xmlDoc = parser.parseFromString(data, "text/xml");

    jsfResponse.responseXML = xmlDoc;
    jsf.ajax.response(jsfResponse, context);

}

function html5submitFunction(element, event, options) {
    var source = event ? event.target : element;
    source = source ? source : element;
    var form = element;
    while ((null != form) && ("form" != form.tagName.toLowerCase())) {
        form = form.parentNode;
    }
    var formData = new FormData(form);
    var formId = form.id;
    var sourceId = element ? element.id : event.target.id;

    if (options.execute) {
        var executeArray = options.execute.split(' ');
        if (executeArray.indexOf("@none") < 0) {
            if (executeArray.indexOf("@all") < 0) {
                options.execute = options.execute.replace("@this", element.id);
                options.execute = options.execute.replace("@form", form.id);
                if (executeArray.indexOf(element.name) < 0) {
                    options.execute = element.name + " " + options.execute;
                }
            } else {
                options.execute = "@all";
            }
        }
    } else {
//        options.execute = element.name + " " + element.id;
        //ICEfaces default render @all
        options.execute = "@all";
    }

    if (options.render) {
        var renderArray = options.render.split(' ');
        if (renderArray.indexOf("@none") < 0) {
            if (renderArray.indexOf("@all") < 0) {
                options.render = options.render.replace("@this", element.id);
                options.render = options.render.replace("@form", form.id);
            } else {
                options.render = "@all";
            }
        }
    } else {
        //ICEfaces default execute @all
        options.execute = "@all";
    }

    formData.append("javax.faces.source", sourceId);
    formData.append(source.name, source.value);
    formData.append("javax.faces.partial.execute", options.execute);
    formData.append("javax.faces.partial.render", options.render);
    formData.append("javax.faces.partial.ajax", "true");

    if (event) {
        formData.append("javax.faces.partial.event", event.type);
    }

    if (options) {
        for (var p in options) {
            if ("function" != typeof(options[p])) {
                formData.append(p, options[p]);
            }
        }
    }

    var context = {
        source: source,
        sourceid: sourceId,
        formid: formId,
        element: element,
        //'begin' event is not triggered since we do not invoke jsf.ajax.request -- onBeforeSubmit relies on this event
        onevent: options.onevent,
        onerror: options.onerror || function (param) {
            alert("JSF error " + param.source + " " + param.description);
        }
    };

    var xhr = new XMLHttpRequest();
    xhr.open("POST", form.getAttribute("action"));
    xhr.setRequestHeader("Faces-Request", "partial/ajax");
    xhr.onreadystatechange = function () {
        if ((4 == xhr.readyState) && (200 == xhr.status)) {
            html5handleResponse(context, xhr.responseText);
        }
    };
    xhr.send(formData);
}

if (window.addEventListener) {
    window.addEventListener("load",
            function () {
                if ((undefined !== window.FormData) &&
                        (undefined === window.ice.mobile) &&
                        ((undefined === window.clientInformation) ||
                            ("BlackBerry" !== window.clientInformation.platform) ||
                            (navigator.userAgent.indexOf(" Version/6.0") < 0)) &&
                        (undefined !== window.Worker)) {
                    ice.submitFunction = html5submitFunction;
                }
            }, false);

    window.addEventListener("pagehide", function () {
        if (ice.push) {
            ice.log.debug(ice.log, 'PAUSING from pagehide');
            ice.push.connection.pauseConnection();
        }
    }, false);

    window.addEventListener("pageshow", function () {
        if (ice.push) {
            ice.log.debug(ice.log, 'RESUMING from pageshow');
            ice.push.connection.resumeConnection();
        }
    }, false);

    window.addEventListener("hashchange", function () {
        var data = ice.mobi.getDeviceCommand();
        var deviceParams;
        if (null != data)  {
            var name;
            var value;
            var needRefresh = true;
            if ("" != data)  {
                deviceParams = ice.mobi.unpackDeviceResponse(data);
                if (deviceParams.name)  {
                    name = deviceParams.name;
                    value = deviceParams.value;
                    ice.mobi.setInput(name, name, value);
                    needRefresh = false;
                }
            }
            if (needRefresh)  {
                if (window.ice.ajaxRefresh)  {
                    ice.ajaxRefresh();
                }
            }
            setTimeout( function(){
                var loc = window.location;
                //changing hash to temporary value ensures changes
                //to repeated values are detected
                history.pushState("", document.title,
                        loc.pathname + loc.search + "#clear-icemobilesx");
                history.pushState("", document.title,
                        loc.pathname + loc.search);
                var sxEvent = {
                    name : name,
                    value : value
                };
                if ("!r" === name.substring(0,2))  {
                    //need to implement iteration over the full set
                    //of response values
                    sxEvent.response = value;
                    sxEvent.name = "";
                    sxEvent.value = "";
                }
                if (ice.mobi.deviceCommandCallback)  {
                    ice.mobi.deviceCommandCallback(sxEvent);
                    ice.mobi.deviceCommandCallback = null;
                }
            }, 1);
        }
    }, false);
}
/* javascript for mobi:commandButton component put into component.js as per MOBI-200 */
ice.mobi.button = {
    select: function(clientId, event, cfg) {
        //get class and add the pressed state
        var me = document.getElementById(clientId);
        if (cfg.pDisabled){
            return; // no change on which button can be selected
        }
        var curClass = me.className;
  //      console.log("curClass="+curClass);
        if (ice.mobi.hasClass(me, ice.mobi.BUTTON_UNPRESSED )){
            var newCls = me.className.replace('up','down');
            me.className = newCls;
        } else {
            //what to do?  we always will render it this way...
        }
        //check if it's part of a commandButtonGroup
        if (cfg.groupId){
            var groupElem = document.getElementById(cfg.groupId+"_hidden");
            if (groupElem){
            //    console.log("for groupId "+cfg.groupId+" value is "+clientId);
                groupElem.value = clientId; //update group to this button selected
            }
        }
        var params = cfg.params || null;

        //otherwise, just check for behaviors, singleSubmit and go
        var singleSubmit = cfg.singleSubmit || false;
        var behaviors = cfg.behaviors || null;
        var event = event || window.event;
        var keyCall = function(xhr, status, args) {ice.mobi.button.unSelect(clientId, curClass);};
        var options = {
            onsuccess: keyCall,
            source : clientId
        };
        if (behaviors && behaviors.click){
            /* does not yet support mobi ajax for panelConf or submitNotification need to modify first */
            /* need to rework AjaxBehaviorRenderer before I can combine the options and cfg */
             behaviors.click();
            /* once I rework mobi ajax support will be able to support all the callbacks in next call */
            //ice.mobi.ab(ice.mobi.extendAjaxArguments(behaviors, options));
        }else{
            if (singleSubmit){
                options.execute="@this";
            } else {
                options.execute="@all";
            }
            if (params !=null){
                options.params = params;
            }else {
                options.params = {};
            }
            options.render = "@all";
            if (cfg.pcId) {
              //  console.log("throw control to panelConfirmation id="+cfg.pcId);
                ice.mobi.panelConf.init(cfg.pcId, clientId, cfg, options);
                return;
             }
             if (cfg.snId) {
                 ice.mobi.submitnotify.open(cfg.snId, clientId, cfg, options);
                 return;
             }
            //if here, then no panelConfirmation as this action is responsible for submit
             else {
                 mobi.AjaxRequest(options);
            }
        }
    },
    unSelect: function(clientId, classNm){
        var elem = document.getElementById(clientId);
        if( elem ){
            var oldClass = elem.className;
            elem.className=oldClass.replace('down','up');
          //  console.log('id='+clientId+' unSelect call back -> class='+document.getElementById(clientId).className);
        }
        
    }
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

ice.mobi.getDeviceCommand = function()  {
    var sxkey = "#icemobilesx";
    var sxlen = sxkey.length;
    var locHash = "" + window.location.hash;
    if (sxkey === locHash.substring(0, sxlen))  {
        return locHash.substring(sxlen + 1);
    }
    return null;
}

ice.mobi.sx = function (element, uploadURL) {
    var ampchar = String.fromCharCode(38);
    var form = ice.mobi.formOf(element);
    var formAction = form.getAttribute("action");
    var command = element.getAttribute("data-command");
    var id = element.getAttribute("data-id");
    var sessionid = element.getAttribute("data-jsessionid");
    var ub = element.getAttribute("data-ub");
    if ((null == id) || ("" == id)) {
        id = element.getAttribute("id");
    }
    var params = element.getAttribute("data-params");
    var windowLocation = window.location;
    var barURL = windowLocation.toString();
    var baseURL = barURL.substring(0,
            barURL.lastIndexOf("/")) + "/";
    var ubConfig = "";
    if ((null != ub) && ("" != ub)) {
        if ("." === ub) {
            ubConfig = "ub=" + escape(baseURL) + ampchar;
        } else {
            ubConfig = "ub=" + escape(ub) + ampchar;
        }
    }

    if (!uploadURL) {
        uploadURL = element.getAttribute("data-posturl");
    }
    if (!uploadURL) {
        if (0 === formAction.indexOf("/")) {
            uploadURL = window.location.origin + formAction;
        } else if ((0 === formAction.indexOf("http://")) ||
                (0 === formAction.indexOf("https://"))) {
            uploadURL = formAction;
        } else {
            uploadURL = baseURL + formAction;
        }
    }
//    } else {
//        uploadURL += '/';
//    }

    var returnURL = "" + window.location;
    if ("" == window.location.hash) {
        var lastHash = returnURL.lastIndexOf("#");
        if (lastHash > 0) {
            returnURL = returnURL.substring(0, lastHash);
        }
        returnURL += "#icemobilesx";
    }

    if ("" != params) {
        params = ubConfig + params;
    }

//    var splashClause = ice.mobi.impl.getSplashClause();

    var sessionidClause = "";
    if ("" != sessionid) {
        sessionidClause = "&JSESSIONID=" + escape(sessionid);
    }
    var sxURL = "icemobile:c=" + escape(command +
            "?id=" + id + ampchar + params) +
            "&u=" + escape(uploadURL) + "&r=" + escape(returnURL) +
            sessionidClause +
//            splashClause +
            "&p=" + escape(ice.mobi.serialize(form, false));

    window.location = sxURL;
}

ice.mobi.impl.getSplashClause = function()  {
    var splashClause = "";
    if (null != ice.mobi.splashImageURL)  {
        var splashImage = "i=" + escape(ice.mobi.splashImageURL);
        splashClause = "&s=" + escape(splashImage);
    }
    return splashClause;
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

ice.mobi.invoke = function(element)  {
    var command = element.getAttribute("data-command");
    if (ice[command])  {
        var params = element.getAttribute("data-params");
        var id = element.getAttribute("data-id");
        if ((null == id) || ("" == id)) {
            id = element.getAttribute("id");
        }
        ice[command](id,params);
    } else {
        ice.mobi.sx(element);
    }
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

ice.mobi.storeLocation = function(id, coords) {
    if (!coords) {
        return;
    }
    var el = document.getElementById(id);
    if (!el) {
        return;
    }
    var elValue = (el.value) ? el.value : "";
    var parts = elValue.split(',');
    if (4 != parts.length) {
        parts = new Array(4)
    }
    ;
    parts[0] = coords.latitude;
    parts[1] = coords.longitude;
    parts[2] = coords.altitude;
    el.value = parts.join();
}

ice.mobi.storeDirection = function(id, orient) {
    if (orient.webkitCompassAccuracy <= 0) {
        return;
    }
    var el = document.getElementById(id);
    if (!el) {
        return;
    }
    var elValue = (el.value) ? el.value : "";
    var parts = elValue.split(',');
    if (4 != parts.length) {
        parts = new Array(4)
    }
    parts[3] = orient.webkitCompassHeading;
    el.value = parts.join();
}


ice.mobi.geolocation = {
    watchId: 0,
    clientId: "",

    /**
     * Perform a call to watchPosition to fetch running updates to position.
     *
     * @param pClientId base id of hidden field
     * @param highAccuracy true if highAccuracy results to be fetched
     * @param maxAge oldest acceptable cached results (in ms)
     * @param timeout longest time to wait for value (in ms)
     */
    watchLocation: function (pClientId, highAccuracy, maxAge, timeout) {

        ice.mobi.geolocation.clientId = pClientId;
        ice.mobi.geolocation.clearWatch();
        // It seems like on Android that passing any argument at all for enableHighAccuracy
        // enables high accuracy.
        var geoParams = {};
        if (maxAge > 0)  {
            geoParams.maximumAge = maxAge * 1000;
        }
        if (timeout > 0)  {
            geoParams.timeout = timeout * 1000;
        }
        if (highAccuracy != 'false')  {
            geoParams.enableHighAccuracy = true;
        }
        console.log('Launching watchPosition, ' +
            'maxAge: ' + geoParams.maximumAge + '(ms),' +
            ' timeout: ' + geoParams.timeout + '(ms)' +
            ' highAccuracy: ' + geoParams.enableHighAccuracy);

        ice.mobi.geolocation.watchId = navigator.geolocation.watchPosition(
                this.successCallback, this.errorCallback,
                geoParams );

        ice.mobi.addListener(window, 'deviceorientation', ice.mobi.geolocation.orientationCallback);
        ice.onElementRemove(pClientId, ice.mobi.geolocation.clearWatch);
        console.log('Lauching positionWatch for client: ' + pClientId +
                ' watchId: ' + ice.mobi.geolocation.watchId);
    },

    /**
     * Perform a single call to the navigator getCurrentPosition call.
     */
    getLocation: function (pClientId, highAccuracy, maxAge, timeout) {

        ice.mobi.geolocation.clientId = pClientId;
        ice.mobi.geolocation.clearWatch();

        var geoParams = {};
        if (maxAge > 0)  {
            geoParams.maximumAge = maxAge * 1000;
        }
        if (timeout > 0)  {
            geoParams.timeout = timeout * 1000;
        }
        if (highAccuracy != 'false')  {
            geoParams.enableHighAccuracy = true;
        }
        console.log('Launching getCurrentPosition, ' +
            'maxAge: ' + geoParams.maximumAge + '(ms),' +
            ' timeout: ' + geoParams.timeout + '(ms)' +
            ' highAccuracy: ' + geoParams.enableHighAccuracy);

       navigator.geolocation.getCurrentPosition(this.successCallback, this.errorCallback,
                    geoParams );

        ice.mobi.addListener(window, 'deviceorientation', ice.mobi.geolocation.orientationCallback);
        ice.onElementRemove(pClientId, ice.mobi.geolocation.clearWatch);
    },

    successCallback: function(pos) {
        console.log('Position update for client: ' + ice.mobi.geolocation.clientId);
        try {
            inputId = ice.mobi.geolocation.clientId + "_locHidden";
            ice.mobi.storeLocation(inputId, pos.coords);

        } catch(e) {
            console.log('Exception: ' + e);
        }
    },

    errorCallback: function(positionError) {
        console.log('Error in watchPosition, code: ' + positionError.code + ' Message: ' + positionError.message);
        ice.mobi.geolocation.clearWatch();
    },

    orientationCallback: function(orient) {
        inputId = ice.mobi.geolocation.clientId + "_locHidden";
        ice.mobi.storeDirection(inputId, orient);
    },

    // Clear any existing positionUpdate listeners
    clearWatch: function() {
        if (ice.mobi.geolocation.watchId > 0) {
            console.log('Existing positionWatch: ' + ice.mobi.geolocation.watchId + ' removed');
            navigator.geolocation.clearWatch(ice.mobi.geolocation.watchId);
            ice.mobi.geolocation.watchId = 0;
        }
        window.removeEventListener('deviceorientation', ice.mobi.geolocation.orientationCallback);
    }
};

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

(function(im) {
    var isTouchDevice = 'ontouchstart' in document.documentElement,
        indicatorSelector = "i.mobi-dv-si",
        blankInicatorClass = 'mobi-dv-si';
    function DataView(clientId, cfg) {
        function hideAddressBar() {
            if(!window.location.hash) {
                window.scrollTo(0, 0);
            }
        }
        if (navigator.userAgent.match(/iPhone/i) || navigator.userAgent.match(/iPod/i)) {
            ice.mobi.addListener(window, "load", function(){ if(!window.pageYOffset){ hideAddressBar(); } } );
            ice.mobi.addListener(window, "orientationchange", hideAddressBar );
        }

        var config = cfg,
            selectorId = '#' + im.escapeJsfId(clientId),
            bodyRowSelector = selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > tbody > tr',
            headCellSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-head > thead > tr > th';

        function getNode(elem) {
            var footCellSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-foot > tfoot > tr > td',
                firstRowSelector = bodyRowSelector + ':first-child',
                detailsSelector = selectorId + ' > .mobi-dv-det',
                headSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-head > thead',
                footSelector = selectorId + ' > .mobi-dv-mst > .mobi-dv-foot > tfoot',
                bodyDivSelector = selectorId + ' > .mobi-dv-mst > div';

            switch (elem) {
                case 'det': return document.querySelector(detailsSelector);
                case 'head': return document.querySelector(headSelector);
                case 'foot': return document.querySelector(footSelector);
                case 'body': return document.querySelector(bodyDivSelector);
                case 'elem': return document.getElementById(clientId);
                case 'headcells': return document.querySelectorAll(headCellSelector);
                case 'bodyrows': return document.querySelectorAll(bodyRowSelector);
                case 'firstrow': return document.querySelector(firstRowSelector);
                case 'footcells': return document.querySelectorAll(footCellSelector);
            }
        }

        function closest(start, target) {
            var t = start;
            while (t && t != document&& !im.matches(t,target))
                t = t.parentNode;

            return t == document ? null : t;
        }

        function getScrollableContainer(element) {
            var height = element.clientHeight,
                parent = element.parentNode;

            while (parent != null && parent.scrollHeight == parent.clientHeight)
                parent = parent.parentNode;

            return parent;
        }

        function getIndexInput(details) {
            var r = Array.prototype.filter.call(details.children, function(n) {
                return n.nodeName.toLowerCase() == "input" && n.getAttribute('name') == clientId+'_active';
            });
            return r[0];
        }

        function initTableAlignment() {
            var dupeHead = document.querySelector(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > thead'),
                dupeHeadCells = document.querySelectorAll(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > thead > tr > th'),
                dupeFoot = document.querySelector(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > tfoot'),
                dupeFootCells = document.querySelectorAll(selectorId + ' > .mobi-dv-mst > div > .mobi-dv-body > tfoot > tr > td'),
                head = getNode('head'),
                headCells = getNode('headcells'),
                foot = getNode('foot'),
                footCells = getNode('footcells'),
                bodyDivWrapper = getNode('body'),
                firstrow = getNode('firstrow'),
                sheet = im.getStyleSheet(clientId + '_colvis');

            if (!sheet) sheet = im.addStyleSheet(clientId + '_colvis', selectorId);;

            var firstRowBodyCells = Array.prototype.filter.call(firstrow.children, function(val){
                return val.nodeName.toLowerCase() == "td"; /* remove hidden input */
            });

            if( window.getComputedStyle ){
                var frbcWidths = Array.prototype.map.call(
                    firstRowBodyCells,
                    function(n) {
                        var compd = document.defaultView.getComputedStyle(n, null);
                        return n.clientWidth - Math.round(parseFloat(compd.paddingLeft)) - Math.round(parseFloat(compd.paddingRight));
                });

                /* fix body column widths */
                for (var i = 0; i < frbcWidths.length; i++) {
                    if( sheet.insertRule ){
                        sheet.insertRule(selectorId + " ." + firstRowBodyCells[i].classList[0] + " { width: "+frbcWidths[i] + 'px'+"; }", 0);
                    }
                }

                var dupeHeadCellWidths = Array.prototype.map.call(
                    dupeHeadCells,
                    function(n) {
                        var compd = document.defaultView.getComputedStyle(n, null);
                        return n.clientWidth - Math.round(parseFloat(compd.paddingLeft)) - Math.round(parseFloat(compd.paddingRight));
                    });

                var dupeFootCellWidths = Array.prototype.map.call(
                    dupeFootCells,
                    function(n) {
                        var compd = document.defaultView.getComputedStyle(n, null);
                        return n.clientWidth - Math.round(parseFloat(compd.paddingLeft)) - Math.round(parseFloat(compd.paddingRight));
                    });

                /* copy head col widths from duplicate header */
                for (var i = 0; i < dupeHeadCellWidths.length; i++) {
                    headCells[i].style.width = dupeHeadCellWidths[i] + 'px';
                }

                /* copy foot col widths from duplicate footer */
                if (footCells.length > 0)
                    for (var i = 0; i < dupeFootCellWidths.length; i++) {
                        footCells[i].style.width = dupeFootCellWidths[i] + 'px';
                    }
            }

            if (config.colvispri) setupColumnVisibiltyRules(firstrow.children);

            /* hide duplicate header */
            setTimeout(function() {
                if (dupeHead) dupeHead.style.display = 'none';
                if (dupeFoot) dupeFoot.style.display = 'none';
            }, 50) /* hiding instantly broke scrolling when init'ing the first time on landscape ipad */

            recalcScrollHeight(head, foot, bodyDivWrapper);
        }

        function setupColumnVisibiltyRules(firstRowCells) {
            var minDevWidth = firstRowCells[0].getBoundingClientRect().left;
            var colVisSheet = im.getStyleSheet(clientId + '_colvis');
            var hideRule = '@media only all {';

            if (!colVisSheet) colVisSheet = im.addStyleSheet(clientId + '_colvis', selectorId);

            var prioritizedCells = config.colvispri.map(function(pri, i) {
               var index = config.colvispri.indexOf(i);
               return index > -1 ? firstRowCells[index] : undefined;
            }).filter(function(cell) { return cell != undefined});

            for (var i = 0; i < prioritizedCells.length; i++) {
                var columnClassname = Array.prototype.filter.call(
                        prioritizedCells[i].classList,
                        function(name) {if (name.indexOf('mobi-dv-c') != -1) return true;}
                    )[0];

                minDevWidth += prioritizedCells[i].clientWidth;

                // add column conditional visibility rule
                hideRule += 'th.'+columnClassname+', td.'+columnClassname;
                if (i != (prioritizedCells.length - 1)) hideRule += ', ';
                if( colVisSheet.insertRule )
                    colVisSheet.insertRule('@media screen and (min-width: '+minDevWidth+'px) { td.'+columnClassname+', th.'+columnClassname+' { display: table-cell; }}', 0);
            }

            hideRule += '{ display:none; }}';
            if( colVisSheet.insertRule )
                colVisSheet.insertRule(hideRule, 0);
        }

        /* arguments optional to avoid lookup */
        function recalcScrollHeight(inHead, inFoot, inDivWrap) {
            /* set scroll body to maximum height, reserving space for head / foot */
            var head = inHead ? inHead : getNode('head'),
                foot = inFoot ? inFoot : getNode('foot'),
                bodyDivWrapper = inDivWrap ? inDivWrap : getNode('body'),
                element = getNode('elem');

            // Exit if dataview has been removed from page.
            if (!element) return;

            var dim = element.getBoundingClientRect(),
                maxHeight = window.innerHeight - dim.top,
                headHeight = head ? head.clientHeight : 0,
                footHeight = foot ? foot.clientHeight : 0,
                fullHeight = maxHeight - headHeight - footHeight - 1;

            /* set height to full visible size of parent */
            if( isNumber(fullHeight) )
                bodyDivWrapper.style.height = fullHeight + 'px';

            /* set height to full visible size of parent minus
             height of all following elements */
            var container = getScrollableContainer(element),
                bottomResize = function() {
                    fullHeight -= (container.scrollHeight - container.clientHeight);
                    if( isNumber(fullHeight)){
                        if (navigator.userAgent.match(/iPhone/i) || navigator.userAgent.match(/iPod/i))
                            fullHeight += 60;
                        bodyDivWrapper.style.height = fullHeight + 'px';
                    }
                };

            if (container) bottomResize();
        }

        var touchedHeadCellIndex = {},
            touchedFirstCell = false,
            pendingSortClick,
            lastTouchTime;

        function headCellTouchStart(e) {
            var cell = e.currentTarget;
            /* targetTouches[0] - ignore multi touch starting here */
            touchedHeadCellIndex[e.targetTouches[0].identifier] = getIndex(cell);
            if (im.matches(cell,headCellSelector+":first-child"))
                touchedFirstCell = true;

            /*prevent scrolling due to drags */
            e.preventDefault();
        }

        function headCellTouchEnd(e) {
            var touch = e.changedTouches[0],
                cell = closest(document.elementFromPoint(touch.pageX, touch.pageY), 'th');

            /* do jump scroll to top */
            if (lastTouchTime && (new Date().getTime() - lastTouchTime < 300)) {
                clearTimeout(pendingSortClick);
                getNode('body').scrollTop = 0;
            } else {
                /* do sorting or drag behaviors */
                if (cell) {
                    var index = getIndex(cell);
                    // clear sort if dragged from first to last cell
                    if (touchedFirstCell && im.matches(cell,headCellSelector+":last-child")) {
                        clearSort();
                    } else if (index == touchedHeadCellIndex[touch.identifier])
                        // delay sort to see if jump scroll tap occurs
                        var sort = sortColumn;
                        pendingSortClick = setTimeout(function () {sort(e);}, 320);
                } else {
                    // dragged from header cell to top 25 px of detail region - close region
                    var detTop = getNode('det').getBoundingClientRect().top;

                    if (touch.pageY < detTop + 25) {
                        deactivateDetail();
                    }
                }
            }

            lastTouchTime = new Date().getTime();
            touchedFirstCell = false;
            touchedHeadCellIndex[touch.identifier] = undefined;
        }

        var touchedRowIndex = {};
        function rowTouchStart(e) {
            var row = e.delegateTarget;

            touchedRowIndex[e.targetTouches[0].identifier] = {
                i : row.getAttribute("data-index"),
                y : e.targetTouches[0].pageY,
                x : e.targetTouches[0].pageX
            };
        }

        function rowTouchEnd(e) {
            var row = closest(document.elementFromPoint(e.changedTouches[0].pageX, e.changedTouches[0].pageY), 'tr'),
                index = row.getAttribute("data-index"),
                y = touchedRowIndex[e.changedTouches[0].identifier].y - e.changedTouches[0].pageY ,
                x = touchedRowIndex[e.changedTouches[0].identifier].x - e.changedTouches[0].pageX;

            /* prevent input when scrolling rows or drag in wide cell*/
            y = y > -25 && y < 25;
            x = x > -25 && y < 25;

            if (index == touchedRowIndex[e.changedTouches[0].identifier].i && y && x){
                tapFlash(e.delegateTarget);

                if (e.delegateTarget.classList.contains('ui-state-active'))
                    deactivateDetail()
                else activateRow(e);
            }

            touchedRowIndex[e.changedTouches[0].identifier] = null;
        }

        function initSortingEvents() {
            var headCells = getNode('headcells');
            for (var i = 0; i < headCells.length; ++i) {
                var cell = headCells[i];
                if (isTouchDevice) {
                    ice.mobi.addListener(cell, "touchend", headCellTouchEnd);
                    ice.mobi.addListener(cell, "touchstart", headCellTouchStart);
                } else {
                    ice.mobi.addListener(cell, "click", sortColumn);
                }
            }
        }

        function initActivationEvents() {
            var element = getNode('elem'),
                /* filter events for those bubbled from tr elems */
                isRowEvent = function(callback) {
                    return function(e) {
                        var tr = closest(e.srcElement, "tr");
                        if (tr && im.matches(tr, bodyRowSelector)) {
                            e.delegateTarget = tr;
                            callback(e);
                        }
                    };
                }

            if (isTouchDevice) {
                ice.mobi.addListener(element, "touchend", isRowEvent(rowTouchEnd));
                ice.mobi.addListener(element, "touchstart", isRowEvent(rowTouchStart));
            } else {
                ice.mobi.addListener(element, "click", isRowEvent(activateRow));
            }
        }

        function processUpdateStr(dir) {
            var valueParts = dir.split('|');
            var details = getNode('det');

            /* lookup elem by id and apply updates */
            for (var i = 0; i < valueParts.length; i++) {
                var v = valueParts[i].split('='),
                        elem = details.querySelector('[id$='+im.escapeJsfId(v[0])+']'),
                        dir = v[1],
                        value;

                switch (dir) {
                    case 'html':
                        elem.innerHTML = v[2];
                        break;

                    case 'attr':
                        if (v[2] == 'checked') {
                            if (v[3] == 'true') elem.checked = true;
                            else elem.checked = false;
                        } else {
                            elem.setAttribute(v[2], v[3]);
                        }
                        break;

                    default :
                        value = '';
                        break;
                }
            };
        }

        var sortCriteria = [];
        function isNumber(n) {
            return !isNaN(parseFloat(n)) && isFinite(n);
        }

        function isDate(n) {
            return !isNaN(Date.parse(n));
        }

        function isCheckboxCol(n) {
            return n.className.indexOf('mobi-dv-bool') != -1;
        }

        function getRowComparator() {
            var firstrow = getNode('firstrow');

            function getValueComparator(cri) {
                var firstRowVal = firstrow.children[cri.index].innerHTML;
                var ascending = cri.ascending ? 1 : -1;

                if (isNumber(firstRowVal))
                    return function(a,b) {
                        return (a.children[cri.index].innerHTML
                            - b.children[cri.index].innerHTML) * ascending;
                    }
                if (isDate(firstRowVal))
                    return function(a,b) {
                        var av = new Date(a.children[cri.index].innerHTML),
                            bv = new Date(b.children[cri.index].innerHTML);

                        if (av > bv) return 1 * ascending;
                        else if (bv > av) return -1 * ascending;
                        return 0;
                    }
                if (isCheckboxCol(firstrow.children[cri.index]))
                /* checkmark markup is shorter - reverse string sort */
                    return function(a,b) {
                        var av = a.children[cri.index].innerHTML,
                            bv =  b.children[cri.index].innerHTML;

                        if (av < bv) return 1 * ascending;
                        else if (bv < av) return -1 * ascending;
                        return 0;
                    }
                else
                /* fall back to string comparison */
                    return function(a,b) {
                        var av = a.children[cri.index].innerHTML,
                            bv =  b.children[cri.index].innerHTML;

                        if (av > bv) return 1 * ascending;
                        else if (bv > av) return -1 * ascending;
                        return 0;
                    }
            }

            return sortCriteria.map(getValueComparator)
                    .reduceRight(function(comp1, comp2) {
                if (comp1 == undefined) return function(a,b) { return comp2(a, b); }
                else return function(a,b) {
                    var v = comp2(a, b);
                    if (v != 0) return v;
                    else return comp1(a, b);
                }
            });
        }

        function getIndex(elem) {
            var columnIndex = 0, sib = elem;
            while( (sib = sib.previousSibling) != null )
                columnIndex++;
            return columnIndex;
        }

        function deactivateDetail() {
            var det = getNode('det');
            det.removeAttribute('data-index');
            getIndexInput(det).setAttribute('value', '-1');
            Array.prototype.every.call(getNode('bodyrows'), function(e) {
                e.classList.remove('ui-state-active');
                return true
            });
            recalcScrollHeight();
        }

        function clearSort() {
            sortCriteria = [];
            Array.prototype.every.call(getNode('headcells'), function(c) {
                    var indi = c.querySelector(indicatorSelector);
                    indi.className = blankInicatorClass;
                    return true;
                });

            var bodyRows = getNode('bodyrows'),
                tbody = bodyRows[0].parentNode;

            /* return rows to 'natural ordering' */
            bodyRows = Array.prototype.map.call(bodyRows, function(r) {return r;})
                .sort(function(a,b) { return a.getAttribute('data-index') - b.getAttribute('data-index');});

            tbody.innerHTML = '';
            bodyRows.every(function(row) {
                tbody.appendChild(row);
                return true;
            });
        }

        function sortColumn(event) {
            var sortedRows, asc,
                headCell = event.target,
                ascendingClass = blankInicatorClass + ' icon-caret-up',
                descendingClass = blankInicatorClass + ' icon-caret-down',
                ascIndi = headCell.querySelector(indicatorSelector),
                ascClass = ascIndi.className;

            /* find col index */
            var columnIndex = getIndex(headCell);

            /* set ascending flag and indicator */
            asc = ascClass == descendingClass || ascClass == blankInicatorClass;
            ascIndi.className = asc ? ascendingClass : descendingClass;

            // sortCriteria = sortCriteria.filter(function(c) { return c.index != columnIndex});

            // forced single sort
            sortCriteria = [{ascending : asc, index : columnIndex}];

            /* remove indicator from other cols */
            var sortedIndexes = sortCriteria.map(function(c) {return c.index});
            Array.prototype.filter.call(getNode('headcells'), function (c) {return sortedIndexes.indexOf(getIndex(c)) == -1;})
                .every(function(c) {
                    var indi = c.querySelector(indicatorSelector);
                    indi.className = blankInicatorClass;
                    return true;
                });

            var bodyRows = getNode('bodyrows');
            /* return bodyRows NodeList as Array for sorting */
            sortedRows = Array.prototype.map.call(bodyRows, function(row) { return row; });
            sortedRows = sortedRows.sort(getRowComparator());

            /* remove previous tbody conent and re-add in new order */
            var tbody = bodyRows[0].parentNode;
            tbody.innerHTML = '';
            Array.prototype.every.call(sortedRows, function(row) {
                tbody.appendChild(row);
                return true;
            });
        }

        function tapFlash(elem) {
            elem.style.backgroundColor = '#194FDB';
            elem.style.backgroundImage = 'none';
            setTimeout(function() {
                elem.style.backgroundColor = '';
                elem.style.backgroundImage = '';
            }, 100);
        }

        function activateRow(event) {
            var newIndex = event.delegateTarget.getAttribute('data-index'),
                details = getNode('det'),
                indexIn = getIndexInput(details);

            event.delegateTarget.classList.add('ui-state-active');

            var sib = event.delegateTarget.nextElementSibling,
                removeActiveClass = function (s) { s.classList.remove('ui-state-active'); };

            while (sib != null) {removeActiveClass(sib); sib = sib.nextElementSibling;};
            sib = event.delegateTarget.previousElementSibling;
            while (sib != null) {removeActiveClass(sib); sib = sib.previousElementSibling;};

            indexIn.setAttribute("value", newIndex);
            details.setAttribute("data-index", newIndex);

            if (config.active == 'client') {
                var newValue = event.delegateTarget.getAttribute('data-state');

                processUpdateStr(newValue);

                // if vertical orientation
                recalcScrollHeight();
            } else {
                var options = {
                    source : clientId,
                    execute : '@this',
                    render : '@this'
                };

                im.ab(options);
            }
        }

        function update(newCfg){
            config = newCfg;
            initActivationEvents();
            initSortingEvents();
            initTableAlignment();
        }

        initActivationEvents();
        initSortingEvents();

        /* first alignment needs to occur shortly after script eval
        *  else heights are miscalculated for following elems */
        setTimeout(initTableAlignment, 100);

        /* resize height adjust */
        ice.mobi.addListener(window, "resize", function() {
            // Timeout to prevent double recalc when resize is due to orientation
            if (!oriChange) {
                setTimeout(function() {
                    if (!oriChange) {
                        recalcScrollHeight();
                    }
                },100);
            }
        });

        var oriChange = false;
        ice.mobi.addListener(window, "orientationchange", function() {
            oriChange = true;

            setTimeout(function() { recalcScrollHeight(); },500);
            // prevent resize-init'd height recalcs for the next 200ms
            setTimeout(function() { oriChange = false; },2000);
        });

        /* Instance API */
        return { update: update }
    }

    im.dataView = {
        instances: {},
        create: function(clientId, cfg) {
            if (this.instances[clientId]) this.instances[clientId].update(cfg);
            else this.instances[clientId] = DataView(clientId, cfg);

            return this.instances[clientId];
        }
    }

})(ice.mobi);

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

//view manager
(function(im){
    var viewHistory =  [];
    var transitionType = 'horizontal'; //horizontal, vertical
    var currentWidth = 0;
    var currentHeight = 0;
    var proxyFormId;
    var transitionDuration = 100;
    var vendor = (function () {
        var styles = window.getComputedStyle(document.documentElement, ''),
          pre = (Array.prototype.slice
            .call(styles)
            .join('')
            .match(/-(moz|webkit|ms)-/) || (styles.OLink === '' && ['', 'o'])
          )[1],
          dom = ('WebKit|Moz|MS|O').match(new RegExp('(' + pre + ')', 'i'))[1];
        return {
          dom: dom,
          lowercase: pre,
          css: '-' + pre + '-',
          js: pre[0].toUpperCase() + pre.substr(1)
        };
      })();
    var vendorPrefix = vendor.lowercase;

    function getCurrentView(){
        var views = document.getElementsByClassName('mobi-vm-view');
        for( var i = 0 ; i < views.length ; i++ ){
            var selected = views[i].getAttribute('data-selected');
            if( selected == "true" ){
                return views[i].getAttribute('data-view');
            }
        }
    }
    function getNodeForView(view){
        var views = document.getElementsByClassName('mobi-vm-view');
        for( var i = 0 ; i < views.length ; i++ ){
            var viewId = views[i].getAttribute('data-view');
            if( viewId ==  view ){
                return views[i];
            }
        }
    }
    function supportsTransitions(){
        var b = document.body || document.documentElement;
        var s = b.style;
        var p = 'transition';
        if(typeof s[p] == 'string') {return true; }

        var v = ['Moz', 'Webkit', 'Khtml', 'O', 'ms'];
        p = p.charAt(0).toUpperCase() + p.substr(1);
        for(var i=0; i<v.length; i++) {
          if(typeof s[v[i] + p] == 'string') { return true; }
        }
        return false;
    }
    function setOrientation(orient){
        document.body.setAttribute("data-orient", orient);
        if (orient == 'portrait'){
            document.body.classList.remove('landscape');
            document.body.classList.add('portrait');
        }
        else if (orient == 'landscape'){
            document.body.classList.remove('portrait');
            document.body.classList.add('landscape');
        }
        else{
            document.body.classList.remove('portrait');
            document.body.classList.remove('landscape');
        }
        setTimeout(scrollTo, 100, 0, 1);
    }

    function refreshViewDimensions(){
        console.log('refreshViewDimensions()');
        if ((window.innerWidth != currentWidth) || (window.innerHeight != currentHeight)){
            currentWidth = window.innerWidth;
            currentHeight = window.innerHeight;
            var orient = (currentWidth < currentHeight) ? 'portrait' : 'landscape';
            setOrientation(orient);
        }

        var contentHeight = currentHeight - 45; //adjust for header
        var currentView = getNodeForView(getCurrentView());
        if( currentView.getElementsByClassName('mobi-vm-nav-bar').length > 0 ){
            contentHeight -= 40; //adjust for nav bar if exists
        }
        var contentNode = currentView.getElementsByClassName('mobi-vm-view-content')[0];
        if( contentNode ){
            contentNode.style.height = '' + contentHeight + 'px';
            console.log('set view content height to ' + contentHeight);
        }
        else
            console.error('ice.mobi.viewManager.refreshViewDimensions() cannot find content node for view = ' + currentView.id);
        var menuNode = document.getElementsByClassName('mobi-vm-menu')[0];
        if( menuNode ){
            menuNode.style.height = '' + (currentHeight - 45) + 'px';
            console.log('set menu height to ' + (currentHeight - 45));
        }
        else
            console.error('ice.mobi.viewManager.refreshViewDimensions() cannot find menu node');

    }

    function getTransitionFunctions(reverse){
        if( 'horizontal' == transitionType ){
            return [function(from,to){
                setTransform(to, 'translateX(' + (reverse ? '-' : '')
                        + window.innerWidth +    'px)');
            },function(from,to){
                setTransform(from, 'translateX(' + (reverse ? '100%' : '-100%') + ')');
                setTransform(to, 'translateX(0%)');
            }];
        }
        else if( 'vertical' == transitionType ){
            return [function(from,to){
                setTransform(to, 'translateY(' + (reverse ? '-' : '')
                        + window.innerWidth +    'px)');
            },function(from,to){
                setTransform(from, 'translateY(' + (reverse ? '100%' : '-100%') + ')');
                setTransform(to, 'translateY(0%)');
            }];
        }
        else if( 'flip' == transitionType ){
            return [function(from,to){
                setTransform(to, 'rotateY(' + (reverse ? '-' : '') + '180deg)');
            },function(from,to){
                setTransform(from, 'rotateY(' + (reverse ? '180deg' : '-180deg') + ')');
                setTransform(to, 'rotateY(0deg)');
            }];
        }
        else if( 'fade' == transitionType ){
            return [function(from,to){
                setOpacity(to, '0');
            },function(from,to){
                setOpacity(from, '0');
                setOpacity(to, '1');
            }];
        }
        else if( 'pageturn' == transitionType ){
            return [function(from,to){
                var parent = to.parentNode;
                parent.style.webkitPerspective = 1100;
                parent.style.webkitPerspectiveOrigin = '50% 50%';
                var fromMirror = document.createElement('div');
                fromMirror.id = '_' + from.id;
                fromMirror.style.width = '50%';
                fromMirror.style.height = '100%';
                fromMirror.style.position = 'absolute';
                fromMirror.style.top = '0px';
                fromMirror.style.right = '0px';
                fromMirror.style.webkitTransformStyle = 'preserve-3d';
                fromMirror.innerHTML = from.innerHTML;
                fromMirror.style.webkitTransform = 'rotateY(180deg)';
                to.parentNode.appendChild(fromMirror);
                from.style.display = 'none';
                parent.style.webkitTransitionProperty = '-webkit-transform';
                parent.style.webkitTransitionDuration = '1000ms';
                parent.style.webkitTransitionTimingFunction = 'ease';
                parent.style.webkitTransformOrigin = 'left';
                parent.style.webkitTransform = 'rotateY(-180deg)';
            },function(from,to){
                var fromMirror = document.getElementById('_'+from.id);
                var parent = to.parentNode;
                parent.removeChild(fromMirror);
                parent.style.webkitTransform = 'none';
                parent.style.webkitTransitionProperty = 'none';
                parent.style.webkitPerspective = 'none';
                parent.style.webkitPerspectiveOrigin = '';
            }];
        }
    }
    function transitionFunction(prop, toStart, from, toEnd){
        return {prop:prop, toStart: toStart, from: from, toEnd: toEnd};
    }
    function setTransitionDuration(elem, val){
        if( vendorPrefix )
            elem.style[''+ vendorPrefix + 'TransitionDuration'] = val;
        elem.style.transitionDuration = val;
    }
    function setTransform(elem, transform){
        elem.style.transitionDuration = transitionDuration;
        if( vendorPrefix ){
            elem.style[''+ vendorPrefix + 'TransitionDuration'] = transitionDuration;
            elem.style[''+ vendorPrefix + 'Transform'] = transform;
        }
        elem.style.transform = transform;
    }
    function setOpacity(elem, val){
        elem.style.transitionDuration = transitionDuration;
        elem.style.opacity = val;
    }
    function setTransitionEndListener(elem, f){
        if( vendorPrefix )
            elem.addEventListener(''+ vendorPrefix + 'TransitionEnd', f, false);
        elem.addEventListener('transitionEnd', f, false);
    }
    function removeTransitionEndListener(elem, f){
        if( vendorPrefix )
            elem.removeEventListener(''+ vendorPrefix + 'TransitionEnd', f, false);
        elem.removeEventListener('transitionEnd', f, false);
    }
    function updateViews(fromNode, toNode, reverse){
        console.log('updateViews() enter');
        if( supportsTransitions() ){
            var transitions = getTransitionFunctions(reverse);
            transitions[0](fromNode,toNode);
            toNode.setAttribute('data-selected', 'true');
            setTransitionDuration(toNode, '');
            setTimeout(transitionComplete, transitionDuration);
            setTimeout(function(){
                console.log('transition() for transition supported');
                transitions[1](fromNode,toNode);
            }, 0);
        }
        else{
            toNode.style.left = "100%";
            scrollTo(0, 1);
            toNode.setAttribute('data-selected', 'true');
            var percent = 100;
            transition();
            var timer = setInterval(transition, 0);

            function transition(){
                console.log('transition() for transition unsupported');
                percent -= 20;
                if (percent <= 0){
                    percent = 0;
                    clearInterval(timer);
                    transitionComplete();
                }
                fromNode.style.left = (reverse ? (100-percent) : (percent-100)) + "%";
                toNode.style.left = (reverse ? -percent : percent) + "%";
            }
        }

        function transitionComplete(){
            console.log('transitionComplete');
            if( fromNode )
                fromNode.removeAttribute('data-selected');
            checkTimer = setTimeout(refreshViewDimensions, 0);
            setTimeout(refreshView, 0, toNode);
            if( fromNode )
                removeTransitionEndListener(fromNode, transitionComplete);
        }
        console.log('updateViews() exit');
    }
    function refreshBackButton(toNode){
        console.log('refreshBackButton()');
        var headerNode = document.getElementsByClassName('mobi-vm-header')[0];
        var backButton = headerNode.children[1];
        var selected = getCurrentView();
        if (backButton){
            if( viewHistory.length == 1 ){
                backButton.style.display = "none";
                return;
            }
            else{
                var prev = viewHistory[viewHistory.length-2];
                if (prev ){
                    var prevView = getNodeForView(prev);
                    if( prevView ){
                        backButton.style.display = "inline";
                        var title = prevView.getAttribute('data-title');
                        backButton.innerHTML = title ? title : "Back";
                    }
                }
            }
        }
    }
    function refreshBackButtonAndViewDimensions(){
        var currentView = getNodeForView(getCurrentView());
        refreshBackButton(currentView);
        refreshViewDimensions();
    }
    function refreshView(toNode){
        console.log('refreshView()');
        var headerNode = document.getElementsByClassName('mobi-vm-header')[0];
        var titleNode = headerNode.firstChild;
        var title = toNode.getAttribute('data-title');
        if (title){
            titleNode.innerHTML = title;
        }
        refreshBackButton();

    }
    function viewHasNavBar(view){
        return view.getElementsByClassName('mobi-vm-nav-bar').length > 0;
    }

    im.viewManager = {
        showView: function(view){
            console.log('showView(' + view + ') current');
            var currentView = getCurrentView();
            if( view == currentView ){
                return;
            }
            var index = viewHistory.indexOf(view);
            var reverse = index != -1 ;
            if (reverse){
                viewHistory.splice(index);
            }
            var fromNode = getNodeForView(currentView);
            var toNode = getNodeForView(view);
            viewHistory.push(toNode.getAttribute('data-view'));
            if( toNode && fromNode ){
                setTimeout(updateViews, 0, fromNode, toNode, reverse);
            }
            else if( toNode ){
                toNode.setAttribute('data-selected', 'true');
            }
            document.getElementById("mobi_vm_selected").value = view;

            jsf.ajax.request(proxyFormId,event,{execute:'@form', render:'@all'});
            return false;
        },
        goBack: function(src){
            var goTo = viewHistory.slice(-2,-1)[0];
            if( goTo != undefined ){
                im.viewManager.showView(goTo);
            }
            else{
                console.error('ViewManager.goBack() invalid state history = ' + viewHistory);
            }
        },
        goToMenu: function(){
            im.viewManager.showView(menuId);
        },
        setState: function(transition, formId, vHistory){
            if( vHistory.length < 1 ){
                console.error('invalid empty history added to ViewManager.setState() aborting');
                return;
            }
            var view = vHistory[vHistory.length-1];
            transitionType = transition;
            proxyFormId = formId;
            viewHistory = vHistory;
            if( view != getCurrentView()){
                var toNode = getNodeForView(view);
                if( toNode ){
                    toNode.setAttribute('data-selected', 'true');
                    document.getElementById("mobi_vm_selected").value = view;
                }
            }
            refreshBackButtonAndViewDimensions();
            ice.mobi.addListener(window, 'resize', refreshViewDimensions);
            ice.mobi.addListener(window, 'orientationchange', refreshViewDimensions);
        }

    }
}(ice.mobi));

