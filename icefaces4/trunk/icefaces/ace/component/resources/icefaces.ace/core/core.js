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

ice.ace.ab = function(cfg) { ice.ace.AjaxRequest(cfg); };
ice.ace.locales = {};
ice.ace.PARTIAL_REQUEST_PARAM = "javax.faces.partial.ajax";
ice.ace.PARTIAL_UPDATE_PARAM = "javax.faces.partial.render";
ice.ace.PARTIAL_PROCESS_PARAM = "javax.faces.partial.execute";
ice.ace.PARTIAL_SOURCE_PARAM = "javax.faces.source";
ice.ace.BEHAVIOR_EVENT_PARAM = "javax.faces.behavior.event";
ice.ace.PARTIAL_EVENT_PARAM = "javax.faces.partial.event";
ice.ace.VIEW_STATE = "javax.faces.ViewState";

// Store minimal client states for component reinitialization (scroll position, etc.)
ice.ace.deadStates = {};

ice.ace.escapeClientId = function(id) {
    return "#" + id.replace(/:/g,"\\:");
};

ice.ace.instance = function(id) {
	var element = document.getElementById(id);
	if (element.widget) return element.widget;
	var lazy = ice.ace.lazy.registry[id];
	if (lazy) return lazy();
	return null;
}

ice.ace.destroy = function(id) {
    var elem = document.getElementById(id);
    if (elem.widget.destroy) {
        var deadState = elem.widget.destroy();
        if (deadState) {
            ice.ace.deadStates[id] = deadState;
        }
    }
    delete elem.widget;
}

ice.ace.lazy = function(name, args) {
    var clientId = args[0], // lazy requires clientId is first arg
        elem = document.getElementById(clientId);

    if (elem.widget == undefined) {
		delete ice.ace.lazy.registry[clientId];
        var component = ice.ace.create(name, args);
        return component;
    }
    else
        return elem.widget;
}

ice.ace.lazy.registry = {}; // store uninitialized lazy components

ice.ace.create = function(name, args) {
    // if first arg is clientId, registerComponent func will attach js instance to DOM
    var clientId = args[0], registerComponent = function(x){};
    var elem = document.getElementById(clientId);
    if (elem) registerComponent = function(component) {
        elem.widget = component;
    };

    // if dead state is available append to arguments
    if (ice.ace.deadStates[clientId]) {
        args.push(ice.ace.deadStates[clientId]);
        ice.ace.deadStates[clientId] = undefined;
    }

    if (ice.ace.jq.isFunction(ice.ace[name])) {
        // Use ECMAScript 5 to construct if available
        if (typeof Object.create == 'function') {
            var o = Object.create(ice.ace[name].prototype);
            ice.ace[name].apply(o, args);
            registerComponent(o);
            return o;
        } else {
            var temp = function(){}, // constructor-less duplicate class
                inst, ret;

            temp.prototype = ice.ace[name].prototype;
            inst = new temp; // init constructor-less class
            ret = ice.ace[name].apply(inst, args); // apply original constructor
            ret = Object(ret) === ret ? ret : inst;
            registerComponent(ret);
            return ret;
        }
    }
    else
        throw 'Missing resources for "' + name + '" component. ' +
                'See "http://www.icesoft.org/wiki/display/ICE/mandatoryResourceConfiguration" ' +
                'for more details.';
};

ice.ace.attachBehaviors = function(element, behaviors) {
    for (var event in behaviors)
        element.bind(event, function() { ice.ace.ab.call(element, behaviors[event]); });
};

ice.ace.eachCustomUpdate = function(responseXML, f) {
    var xmlDoc = responseXML.documentElement;
    var extensions = xmlDoc.getElementsByTagName("extension");

    for (var i = 0, l = extensions.length; i < l; i++) {
        var extension = extensions[i];
        if (extension.getAttributeNode('ice.customUpdate')) {
            f(extension.attributes.getNamedItem("id").nodeValue,
              extension.firstChild.data);
        }
    }
}

ice.ace.clearExecRender = function(options) {
    options['execute'] = undefined;
    options['render'] = undefined;
    return options;
}

ice.ace.extendAjaxArgs = function(callArguments, options) {
    // Return a modified copy of the original arguments instead of modifying the original.
    // The cb arguments, being a configured property of the component will live past this request.
    callArguments = ice.ace.util.clone(callArguments);

    // Premerge arrays of arguments supplied by the server
    if (callArguments instanceof Array) {
        var arrayArguments = callArguments;
        var callArguments = {};
        for (var x in arrayArguments) {
            callArguments = ice.ace.extendAjaxArgs(callArguments, arrayArguments[x]);
        }
    }

    var params     = options.params,
        execute    = options.execute,
        render     = options.render,
        source     = options.source,
        node       = options.node,
        event      = options.event,
        onstart    = options.onstart,
        onerror    = options.onerror,
        onsuccess  = options.onsuccess,
        oncomplete = options.oncomplete;

    if (params) {
        if (callArguments['params'])
            ice.ace.util.extend(callArguments['params'], params);
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

    if (source && !(callArguments['source'])) {
        callArguments['source'] = source;
    }

    if (event) {
        callArguments['event'] = event;
    }

    if (onstart) {
        if (callArguments['onstart']) {
            var existingStartCall = callArguments['onstart'];
            callArguments['onstart'] = function(xhr) {
                var ret = existingStartCall(xhr);
                return onstart(xhr) && ret;
            }
        } else {
            callArguments['onstart'] = onstart;
        }
    }

    if (onerror) {
        if (callArguments['onerror']) {
            var existingErrorCall = callArguments['onerror'];
            callArguments['onerror'] = function(xhr, status, error) {
                var ret = existingErrorCall(xhr, status, error);
                return onerror(xhr, status, error) && ret;
            }
        } else {
            callArguments['onerror'] = onerror;
        }
    }

    if (onsuccess) {
        if (callArguments['onsuccess']) {
            var existingSuccessCall = callArguments['onsuccess'];
            callArguments['onsuccess'] = function(data, status, xhr, args) {
                var ret = existingSuccessCall(data, status, xhr, args);
                return onsuccess(data, status, xhr, args) && ret;
            }
        } else {
            callArguments['onsuccess'] = onsuccess;
        }
    }

    if (oncomplete) {
        if (callArguments['oncomplete']) {
            var existingCompleteCall = callArguments['oncomplete'];
            callArguments['oncomplete'] = function(xhr, status, args) {
                var ret = existingCompleteCall(xhr, status, args);
                return oncomplete(xhr, status, args) && ret;
            }
        } else {
            callArguments['oncomplete'] = oncomplete;
        }
    }
    
    return callArguments;
}

ice.ace.AjaxRequest = function(cfg) {
    // If start events return false, cancel request
    if(cfg.onstart && !cfg.onstart.call(this, cfg))
       return;

    // Find the source DOM element
    var sourceElement;
    // If the source input is not a valid id ('@this', etc.), but an source node has been explicitly passed
    if (!document.getElementById(cfg.source) && cfg.node) {
        // Set the source element to the node
        sourceElement = cfg.node;
        // Set the source element id to the invalid source string as it will be used by the bridge as the source.
        sourceElement.id = cfg.source;
    } else
        // If the source input is a string attempt to find it as an id, else assume it is an element and assign it
        sourceElement = (typeof cfg.source == 'string') ? document.getElementById(cfg.source) : cfg.source;

    // If the attempts to find a source element from the input string failed, or there was no source input
    if (!sourceElement) {
        // Find form element to submit as event source
        var form = ice.ace.jq(ice.ace.escapeClientId(cfg.source)).parents('form:first');
        if(form.length == 0)
            form = ice.ace.jq(cfg.node).parents('form:first');
        if(form.length == 0)
            form = ice.ace.jq('form').eq(0);

        sourceElement = form.length ? form[0] : null;
    }

    // Defaults for execute and render
    var jsfExecute = cfg.execute || '@all';
    var jsfRender = cfg.render || '@all';

    // Create IF bridge request
    ice.fullSubmit(jsfExecute, jsfRender, null, sourceElement, function(parameter) {
        // Convert PF-style parameters to IF-style
        if(cfg.event) {
            parameter(ice.ace.BEHAVIOR_EVENT_PARAM, cfg.event);

            var domEvent = cfg.event;
            if(cfg.event == 'valueChange') {
                domEvent = 'change';
            } else if (cfg.event == 'action') {
                domEvent = 'click';
            }

            parameter(ice.ace.PARTIAL_EVENT_PARAM, domEvent);
        } else {
            parameter(cfg.source, cfg.source);
        }

        if(cfg.params) {
            var cfgParams = cfg.params;
            for(var p in cfgParams) {
                parameter(p, cfgParams[p]);
            }
        }
    }, function(onBeforeSubmit, onBeforeUpdate, onAfterUpdate, onNetworkError, onServerError) {
        // Define IF callbacks which map to PF/ace:ajax style callbacjs
        var context = {};
        onAfterUpdate(function(responseXML) {
            // Do onsuccess
            if (cfg.onsuccess && !cfg.onsuccess.call(context, responseXML, null /*status*/, null /*xhr*/))
                return;

            // Decode special customUpdate and callbackParam request portions
            ice.ace.AjaxResponse.call(context, responseXML);
        });

        if (cfg.oncomplete) {
            // Do oncomplete
            onAfterUpdate(function(responseXML) {
                cfg.oncomplete.call(context, null /*xhr*/, null /*status*/, context.args);
            });
        }

        if (cfg.onerror) {
            // Do onerror
            onNetworkError(function(responseCode, errorDescription) {
                cfg.onerror.call(context, null /*xhr*/, responseCode /*status*/, errorDescription /*error description*/)
            });
            onServerError(function(responseCode, responseText) {
                cfg.onerror.call(context, null /*xhr*/, responseCode /*status*/, responseText /*error description*/)
            });
        }
    });
};

ice.ace.AjaxResponse = function(responseXML) {
    var xmlDoc = responseXML.documentElement;
    var extensions = xmlDoc.getElementsByTagName("extension");

    this.args = {};
    for(var i = 0, l = extensions.length; i < l; i++) {
        var extension = extensions[i];
        if (extension.getAttributeNode('aceCallbackParam')) {
            var jsonObj = JSON.parse(extension.firstChild.data);
            for(var paramName in jsonObj) {
                if(paramName) {
                    this.args[paramName] = jsonObj[paramName];
                }
            }
        }
        if (extension.getAttributeNode('ice.customUpdate')) {
            var id = extension.attributes.getNamedItem("id").nodeValue;
            var content = extension.firstChild.data;
            this.updateElem(id, content);
        }
    }
};

ice.ace.AjaxResponse.updateElem = function(id, content) {
    ice.ace.jq(ice.ace.escapeClientId(id)).replaceWith(content);

    //Mobile
    if(ice.ace.jq.mobile) {
        var controls = ice.ace.jq(ice.ace.escapeClientId(id)).parent().find("input, textarea, select, button, ul");

        //input and textarea
        controls
                .filter("input, textarea")
                .not("[type='radio'], [type='checkbox'], [type='button'], [type='submit'], [type='reset'], [type='image'], [type='hidden']")
                .textinput();

        //lists
        controls.filter("[data-role='listview']").listview();

        //buttons
        controls.filter("button, [type='button'], [type='submit'], [type='reset'], [type='image']" ).button();

        //slider
        controls.filter("input, select")
                .filter("[data-role='slider'], [data-type='range']")
                .slider();

        //selects
        controls.filter("select:not([data-role='slider'])" ).selectmenu();
    }
};