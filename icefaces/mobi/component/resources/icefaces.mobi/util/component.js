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

String.prototype.trim = function () {
    return this.replace(/^\s*/, "").replace(/\s*$/, "");
};

ice.yui3 = {
    y : null,
    getY: function() {
        return ice.yui3.y;
    },
    modules: {},
    use :function(callback) {
        if (ice.yui3.y == null) {
			var Yui = ice.yui3.getNewInstance();
			Yui.use('loader', 'oop', 'event-custom', 'attribute', 'base',
                    'event', 'dom', 'node', 'event-delegate',
                    // load modules required by the animation library
                    'anim', 'plugin', 'pluginhost',
                    // Specified by animation-v2.js
                    //'json',
                    function(Y) {
                ice.yui3.y = Y;
				
				// make Y.one support ':'s in IDs
				var _one = ice.yui3.y.one;
				ice.yui3.y.one = function(id) { 
					if (ice.yui3.y.Lang.isString(id)) {
						id = id.replace(/:/g, '\\:');
					}
					return _one(id);
				}
				
                callback(ice.yui3.y);
            });
        } else {
            callback(ice.yui3.y);
        }
    },
    loadModule: function(module) {
        if (!this.modules[module])
            this.modules[module] = module;
    },
    getModules: function() {
        var modules = '';
        for (module in this.modules)
            modules += module + ',';
        return modules.substring(0, modules.length - 1);
    },
    //basePathPattern: /^.*\/+(.*)\/javax\.faces\.resource.*yui\/yui-min\.js(.*)\?ln=(.*)?$/,
	basePathPattern: /^(.*)javax\.faces\.resource([\/\=])yui\/yui-min\.js(.*)$/,
	basePathPatternPortlets: /^(.*)javax\.faces\.resource([\/\=])yui%2Fyui-min\.js(.*)$/,
	libraryPattern: /^(.*[\_\-\.\&\?])ln\=yui\/[0-9a-zA-Z_\.]+(.*)$/,
	libraryPatternPortlets: /^(.*[\_\-\.\&\?])ln\=yui%2F[0-9a-zA-Z_\.]+(.*)$/,
	getBasePath: function(pattern) {
		var nodes, i, src, match;
		nodes = document.getElementsByTagName('script') || [];
		for (i = 0; i < nodes.length; i++) {
			src = nodes[i].src;
			if (src) {
				match = src.match(pattern);
				if (match) {
					return match;
				}
			}
		}
		return null;
    },
	yui3Base: '',
	yui2in3Base: '',
	facesServletExtension: '',
	yui3TrailingPath: '',
	yui2in3TrailingPath: '',
	getNewInstance: function() { // Private, only for use()
		if (!(ice.yui3.yui3Base && ice.yui3.yui2in3Base)) {
			var isPortlet = false;
			var whole = ice.yui3.getBasePath(ice.yui3.basePathPattern);
			if (!whole) {
				whole = ice.yui3.getBasePath(ice.yui3.basePathPatternPortlets);
				isPortlet = true;
			}
			var library;
			if (isPortlet) {
				library = whole[3].match(ice.yui3.libraryPatternPortlets);
			} else {
				library = whole[3].match(ice.yui3.libraryPattern);
			}
			ice.yui3.yui3Base = whole[1] + "javax.faces.resource" + whole[2];
			ice.yui3.yui2in3Base = whole[1] + "javax.faces.resource" + whole[2];
			ice.yui3.yui3TrailingPath = whole[3];
			ice.yui3.yui2in3TrailingPath = library[1] + "ln=yui/2in3" + library[2];
		}

        //filter:"raw"
		var Y = YUI({combine: false, base: ice.yui3.yui3Base,
			groups: {
				yui2: {
				    combine: false,
					base: ice.yui3.yui2in3Base,
					patterns:  {
						'yui2-': {
							configFn: function(me) {
								if (/-skin|reset|fonts|grids|base/.test(me.name)) {
									me.type = 'css';
									me.path = me.path.replace(/\.js/, '.css');
								}
							}
						}
					}
				}
			}
		});
		
		// create URLs with the Faces Servlet extension at the end (e.g. '.jsf')
		var oldUrlFn = Y.Loader.prototype._url;
		Y.Loader.prototype._url = function(path, name, base) {
			var trailingPath;
			if (name.indexOf('yui2-') == -1) {
				trailingPath = ice.yui3.yui3TrailingPath;
			} else {
				trailingPath = ice.yui3.yui2in3TrailingPath;
			}
			return oldUrlFn.call(this, path, name, base) + trailingPath;
		};		
		
		return Y;
	}
};


var JSContext = function(clientId) {
    this.clientId = clientId
};
JSContext.list = {};
JSContext.prototype = {
    setComponent:function(component) {
        this.component = component;
    },

    getComponent:function() {
        return this.component;
    },

    setJSProps:function(props) {
        this.jsProps = props;
    },

    getJSProps:function() {
        return this.jsProps;
    },
    setJSFProps:function(props) {
        this.jsfProps = props;
    },

    getJSFProps:function() {
        return this.jsfProps;
    },

    isAttached:function() {
        return document.getElementById(this.clientId)['JSContext'];
    }
};

ice.ace.updateProperties = function(clientId, jsProps, jsfProps, events, lib) {
	ice.ace.getInstance(clientId, function(yuiComp) {
		// TODO Why is yuiComp undefined
		if (!yuiComp) {
			return;
		}
		for (prop in jsProps) {
			var propValue = yuiComp.get(prop);
			if (propValue != jsProps[prop]) {
				yuiComp.set(prop, jsProps[prop]);
			}
		}
	}, lib, jsProps, jsfProps);
};

ice.ace.getInstance = function(clientId, callback, lib, jsProps, jsfProps) {
    var component = document.getElementById(clientId);
    //could be either new component, or part of the DOM diff
    var context = this.getJSContext(clientId);
    if (!context || (context && !context.isAttached())) {
        context = this.createContext(clientId);
        context.setJSProps(jsProps);
        context.setJSFProps(jsfProps);
        lib.initialize(clientId, jsProps, jsfProps, function(YUIJS) {
            context.setComponent(YUIJS);
            callback(context.getComponent());
        });
    } else {
        context = this.getJSContext(clientId);
        context.setJSProps(jsProps);
        context.setJSFProps(jsfProps);
        callback(context.getComponent());
    }
};

ice.ace.getJSContext =  function(clientId) {
    var component = document.getElementById(clientId);
    if (component) {
        if (component['JSContext'])
            return component['JSContext'];
        //else
            //return JSContext[clientId];
    }
    return null;
};

ice.ace.createContext = function(clientId) {
    var component = document.getElementById(clientId);
    component['JSContext'] = new JSContext(clientId);
    JSContext[clientId] = component['JSContext'];
    return component['JSContext'];
};

ice.ace.clientState = {
    set: function(clientId, state) {
        this.getStateHolder()[clientId] = state;
    },

    get: function(clientId) {
        return this.getStateHolder()[clientId];
    },

    has: function(clientId) {
        return (this.getStateHolder()[clientId] != null);
    },

    getStateHolder: function () {
        if (!window.document['sparkle_clientState']) {
            window.document['sparkle_clientState'] = {};
        }
        return window.document['sparkle_clientState'];
    }
};

// this seems repetitive ... changed the handful of places these functions
// were used as a member of ice.yui3
//for (props in ice.ace) {
//    ice.yui3[props] = ice.ace[props];
//}