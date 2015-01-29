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

if (!window.ice) {
    window.ice = new Object;
}

if (!window.ice.icefaces) {
    //the eval needs to be located outside the main closure to allow proper minification from YUI compressor
    window.ice.globalEval = function(src) {
        if (window.execScript) {
            window.execScript(src);
        } else {
            (function() {
                window.eval.call(window, src);
            })();
        }
    };

    (function(namespace) {
        namespace.icefaces = true;
        namespace.configuration = new Object();
        namespace.disableDefaultErrorPopups = false;
        //define primitive submit function to allow overriding it later in special environments
        namespace.submitFunction = jsf.ajax.request;

        function detectByReference(ref) {
            return function(o) {
                return o == ref;
            };
        }

        function removeCallbackCallback(callbackList, detector) {
            return function removeCallback() {
                var temp = reject(callbackList, detector);
                empty(callbackList);
                each(temp, curry(append, callbackList));
            }
        }

        var sessionExpiryListeners = [];
        namespace.onSessionExpiry = function(callback) {
            append(sessionExpiryListeners, callback);
            return removeCallbackCallback(sessionExpiryListeners, detectByReference(callback));
        };

        var networkErrorListeners = [];
        namespace.onNetworkError = function(callback) {
            append(networkErrorListeners, callback);
            return removeCallbackCallback(networkErrorListeners, detectByReference(callback));
        };

        var serverErrorListeners = [];
        namespace.onServerError = function(callback) {
            append(serverErrorListeners, callback);
            return removeCallbackCallback(serverErrorListeners, detectByReference(callback));
        };

        var viewDisposedListeners = [];
        namespace.onViewDisposal = function(callback) {
            append(viewDisposedListeners, callback);
            return removeCallbackCallback(viewDisposedListeners, detectByReference(callback));
        };

        var beforeSubmitListeners = [];
        namespace.onBeforeSubmit = function(callback) {
            append(beforeSubmitListeners, callback);
            return removeCallbackCallback(beforeSubmitListeners, detectByReference(callback));
        };
        //alias for onBeforeSubmit
        //deprecated
        namespace.onSubmitSend = namespace.onBeforeSubmit;

        var beforeUpdateListeners = [];
        namespace.onBeforeUpdate = function(callback) {
            append(beforeUpdateListeners, callback);
            return removeCallbackCallback(beforeUpdateListeners, detectByReference(callback));
        };
        //alias for onBeforeUpdate
        //deprecated
        namespace.onSubmitResponse = namespace.onBeforeUpdate;

        var afterUpdateListeners = [];
        namespace.onAfterUpdate = function(callback) {
            append(afterUpdateListeners, callback);
            return removeCallbackCallback(afterUpdateListeners, detectByReference(callback));
        };

        function configurationOf(element) {
            var result = configurationOfImpl(element);
            if (result) {
                return result;
            }
            result = configurationOfImpl(document.getElementById(element.id));
            if (result) {
                return result;
            }
            debug(logger, 'configuration not found for ' + element.nodeName);
            return {};
        }

        function configurationOfImpl(element) {
            configParent = detect(parents(element),
                function(e) {
                    if (null != e) {
                        return e.configuration;
                    }
                    return {};
                });
            if (null != configParent) {
                return configParent.configuration;
            }
            return null;
        }

        function deltaSubmit(element) {
            return configurationOf(element).deltaSubmit;
        }

        function viewIDOf(element) {
            return configurationOf(element).viewID;
        }

        function formOf(element) {
            try {
                return formOfNode(element);
            } catch (e) {
                //page update may have occurred
                return formOfNode(document.getElementById(element.id));
            }
        }

        function formOfNode(element) {
            return toLowerCase(element.nodeName) == 'form' ? element : enclosingForm(element);
        }

        function lookupElementById(id) {
            var e;
            if (id == 'javax.faces.ViewRoot') {
                e = document.documentElement;
            } else if (id == 'javax.faces.ViewBody') {
                e = document.body;
            } else {
                try {
                    e = document.getElementById(id);
                } catch (e) {
                    //element not found, error thrown only in IE
                }
            }

            return e;
        }

        //function used to safely retrieve named element -- form[name] sometimes fails in IE
        function lookupNamedInputElement(form, name) {
            var e = form[name];

            if (!e) {
                e = detect(form.getElementsByTagName('input'), function(input) {
                    return input.name && input.name == name;
                });
            }

            return e;
        }

        //function used to safely retrieve ViewState key element
        function lookupViewStateElement(form) {
            return detect(form.getElementsByTagName('input'), function(input) {
                return input.name && endsWith(input.name, 'javax.faces.ViewState');
            }, function() {
                throw 'cannot find javax.faces.ViewState input element';
            });
        }

        function lookupViewState(element) {
            return lookupViewStateElement(element).value;
        }

        function retrieveUpdateFormID(viewID) {
            return 'v' + viewID.replace(':', '-') + '-retrieve-update';
        }

        function singleSubmitFormID(viewID) {
            return 'v' + viewID.replace(':', '-') + '-single-submit';
        }

        eval(ice.importFrom('ice.lib.functional'));
        eval(ice.importFrom('ice.lib.oo'));
        eval(ice.importFrom('ice.lib.collection'));
        eval(ice.importFrom('ice.lib.hashtable'));
        eval(ice.importFrom('ice.lib.string'));
        eval(ice.importFrom('ice.lib.delay'));
        eval(ice.importFrom('ice.lib.window'));
        eval(ice.importFrom('ice.lib.event'));
        eval(ice.importFrom('ice.lib.element'));
        eval(ice.importFrom('ice.lib.logger'));
        eval(ice.importFrom('ice.lib.query'));
        eval(ice.importFrom('ice.lib.http'));

        namespace.onLoad = curry(onLoad, window);
        namespace.onBeforeUnload = curry(onBeforeUnload, window);
        namespace.onUnload = curry(onUnload, window);

        var handler = window.console && window.console.log ? ConsoleLogHandler(debug) : WindowLogHandler(debug, window.location.href);
        var logger = Logger([ 'window' ], handler);
        namespace.log = logger;
        namespace.log.debug = debug;
        namespace.log.info = info;
        namespace.log.warn = warn;
        namespace.log.error = error;
        namespace.log.childLogger = childLogger;

        //include focus.js
        namespace.setFocus = setFocus;
        namespace.sf = setFocus;
        namespace.applyFocus = applyFocus;
        namespace.af = applyFocus;

        function appendHiddenInputElement(form, name, value, defaultValue) {
            var hiddenInput = document.createElement('input');
            hiddenInput.setAttribute('name', name);
            hiddenInput.setAttribute('value', value);
            hiddenInput.setAttribute('type', 'hidden');
            hiddenInput.setAttribute('autocomplete', 'off');
            if (defaultValue) {
                hiddenInput.defaultValue = defaultValue;
            }
            form.appendChild(hiddenInput);
            return hiddenInput;
        }

        function appendViewStateInputElement(form, viewState) {
            var prefix = configurationOf(form).parameterPrefix;
            appendHiddenInputElement(form, prefix + 'javax.faces.ViewState', viewState, viewState);
        }

        var viewIDs = [];

        function requestForUpdates(viewID) {
            append(viewIDs, viewID);
            var formID = retrieveUpdateFormID(viewID);
            var form = lookupElementById(formID);
            var parameterPrefix = configurationOf(form).parameterPrefix;

            //form is missing after navigating to a non-icefaces page
            if (form) {
                try {
                    debug(logger, 'picking updates for view ' + viewID);
                    var options = {
                        'com.sun.faces.namingContainerId': parameterPrefix,
                        'ice.submit.type': 'ice.push',
                        'ice.view': viewID,
                        'ice.window': namespace.window,
                        render: '@all'
                    };
                    jsf.ajax.request(form, null, options);
                } catch (e) {
                    warn(logger, 'failed to pick updates', e);
                }
            }
        }

        function retrieveUpdate(viewID) {
            //delay issuing the request until no submit event is in progress
            var delayedUpdates = function()  {
                if (eventInProgress)  {
                    setTimeout(delayedUpdates, 20);
                } else {
                    requestForUpdates(viewID);
                }
            };
            return delayedUpdates;
        }

        var client = Client();

        function disposeWindow(viewID) {
            return function() {
                var form = lookupElementById(singleSubmitFormID(viewID));
                //form is missing after navigating to a non-icefaces page
                if (form) {
                    var prefix = configurationOf(form).parameterPrefix;
                    try {
                        //dispose is the final operation on this page, so no harm
                        //in modifying the action to remove CDI conversation id
                        var encodedURLElement = lookupNamedInputElement(form, 'javax.faces.encodedURL');
                        var url = encodedURLElement ? encodedURLElement.value : form.action;
                        form.action = url.replace(/(\?|&)cid=[0-9]+/, "$1");
                        debug(logger, 'dispose window and associated views ' + viewIDs);
                        postSynchronously(client, form.action, function(query) {
                            addNameValue(query, prefix + 'ice.submit.type', 'ice.dispose.window');
                            addNameValue(query, prefix + 'ice.window', namespace.window);
                            addNameValue(query, prefix + 'javax.faces.ViewState', lookupViewState(form));
                            each(viewIDs, curry(addNameValue, query, 'ice.view'));
                        }, FormPost, noop);
                    } catch (e) {
                        warn(logger, 'failed to notify window disposal', e);
                    }
                }
            };
        }

        function sessionExpired() {
            //stop requesting updates
            requestForUpdates = noop;
            //deregister pushIds to stop blocking connection, if ICEpush is present
            if (namespace.push) {
                each(viewIDs, namespace.push.deregister);
            }
            //notify listeners
            broadcast(sessionExpiryListeners);
        }

        function containsXMLData(doc) {
            //test if document contains XML data since IE will return a XML document for dropped connection
            return doc && doc.documentElement;
        }

        function containsHTMLData(doc) {
            return doc.documentElement.nodeName == 'html';
        }

        //define function to be wired as submit callback into JSF bridge
        function submitEventBroadcaster(perRequestOnBeforeSubmitListeners, perRequestOnBeforeUpdateListeners, perRequestOnAfterUpdateListeners) {
            perRequestOnBeforeSubmitListeners = perRequestOnBeforeSubmitListeners || [];
            perRequestOnBeforeUpdateListeners = perRequestOnBeforeUpdateListeners || [];
            perRequestOnAfterUpdateListeners = perRequestOnAfterUpdateListeners || [];
            // Cache iceEnabled for 'success' event as submitElement may be detached from configParent
            var viewID;
            return function(submitEvent) {
                var submitElement = submitEvent.source;
                //if we have the submit element and the view ID set (ICEfaces render enabled) then the callbacks are invoked
                if (submitElement) {
                    //re-lookup submit element, sometimes in IE the queued requests are not serialized properly and the
                    //submit element might have been replaced by another response
                    submitElement = lookupElementById(submitElement.id);
                    try {
                        viewID = viewIDOf(submitElement);
                    } catch (e) {
                        //ignore failure to traverse parents when trying to find the configured viewID
                        //assume that the traversal is done on a replaced DOM fragment
                    }

                    if (viewID) {
                        switch (submitEvent.status) {
                            case 'begin':
                                //Include parameter indicating if submission was triggered by client
                                var isUserInitiatedRequest = false;
                                if (submitElement.id != retrieveUpdateFormID(viewIDOf(submitElement))) {
                                    isUserInitiatedRequest = true;
                                }
                                broadcast(perRequestOnBeforeSubmitListeners, [ submitElement, isUserInitiatedRequest ]);
                                break;
                            case 'complete':
                                var xmlContent = submitEvent.responseXML;
                                if (containsXMLData(xmlContent)) {
                                    if (containsHTMLData(xmlContent)) {
                                        //reload page when html markup is received instead of the partial update
                                        document.location = document.location.href;
                                    } else {
                                        broadcast(perRequestOnBeforeUpdateListeners, [ xmlContent, submitElement ]);
                                    }
                                } else {
                                    warn(logger, 'the response does not contain XML data');
                                    if (configurationOf(submitElement).reloadOnUpdateFailure) {
                                        warn(logger, 'reloading page ...');
                                        document.location = document.location.href;
                                    }
                                }
                                break;
                            case 'success':
                                var xmlContent = submitEvent.responseXML;
                                broadcast(perRequestOnAfterUpdateListeners, [ xmlContent, submitElement ]);
                                break;
                        }
                    }
                } else {
                    warn(logger, 'Source element is undefined, cannot determine if this view is ICEfaces enabled.')
                }
            };
        }

        //define function to be wired as error callback into JSF bridge
        function submitErrorBroadcaster(perRequestNetworkErrorListeners, perRequestServerErrorListeners, sessionExpiredListener) {
            perRequestNetworkErrorListeners = perRequestNetworkErrorListeners || [];
            perRequestServerErrorListeners = perRequestServerErrorListeners || [];
            return function(e) {
                if (e.status == 'serverError') {
                    var xmlContent = e.responseXML;
                    if (containsXMLData(xmlContent) && sessionExpiredListener) {
                        var errorName = xmlContent.getElementsByTagName("error-name")[0].firstChild.nodeValue;
                        if (errorName && contains(errorName, 'org.icefaces.application.SessionExpiredException')) {
                            info(logger, 'received session expired message');
                            sessionExpiredListener();
                            return;
                        }
                    }

                    info(logger, 'received error message [code: ' + e.responseCode + ']: ' + e.responseText);
                    broadcast(perRequestServerErrorListeners, [ e.responseCode, e.responseText, containsXMLData(xmlContent) ? xmlContent : null]);
                } else if (e.status == 'httpError') {
                    warn(logger, 'HTTP error [code: ' + e.responseCode + ']: ' + e.description + '\n' + e.responseText);
                    broadcast(perRequestNetworkErrorListeners, [ e.responseCode, e.description]);
                } else {
                    //If the error falls through the other conditions, just log it.
                    error(logger, 'Error [status: ' + e.status + ' code: ' + e.responseCode + ']: ' + e.description + '\n' + e.responseText);
                }
            };
        }

        //setup page scoped submit event broadcasters
        namespace.submitCallback = submitEventBroadcaster(beforeSubmitListeners, beforeUpdateListeners, afterUpdateListeners);
        jsf.ajax.addOnEvent(namespace.submitCallback);
        namespace.errorCallback = submitErrorBroadcaster(networkErrorListeners, serverErrorListeners, sessionExpired);
        jsf.ajax.addOnError(namespace.errorCallback);

        var eventInProgress;
        //setup submit error logging
        function logReceivedUpdates(e) {
            if ('begin' == e.status)  {
                eventInProgress = e;
            } else {
                eventInProgress = null;
            }
            if ('success' == e.status) {
                var xmlContent = e.responseXML;
                var updates = xmlContent.documentElement.firstChild.childNodes;
                var updateDescriptions = collect(updates, function(update) {
                    var id = update.getAttribute('id');
                    var updateType = update.nodeName;
                    var detail = updateType + (id ? '["' + id + '"]' : '');
                    //will require special case for insert operation
                    if ('update' == updateType) {
                        detail += ': ' + substring(update.firstChild.data, 0, 40) + '....';
                    } else if ('insert' == updateType) {
                        var location = update.firstChild.getAttribute('id');
                        var text = update.firstChild.firstChild.data;
                        detail += ': ' + update.firstChild.nodeName + ' ' + location + ': ' + substring(text, 0, 40) + '....';
                    } else if ('eval' == updateType) {
                        detail += ': ' + substring(update.firstChild.data, 0, 40) + '....';
                    }
                    return detail;
                });
                debug(logger, 'applied updates >>\n' + join(updateDescriptions, '\n'));
            }
        }

        jsf.ajax.addOnEvent(logReceivedUpdates);

        //include submit.js
        //include blockui.js
        namespace.se = singleSubmitExecuteThis;
        namespace.ser = singleSubmitExecuteThisRenderThis;
        namespace.submit = submit;
        namespace.s = submit;
        namespace.sef = submitExecuteForm;
        namespace.fullSubmit = fullSubmit;

        namespace.ajaxRefresh = function(viewID) {
            viewID = viewID || (document.body.configuration ? document.body.configuration.viewID : null);
            if (!viewID) {
                throw 'viewID parameter required';
            }
            var c = configurationOf(lookupElementById(retrieveUpdateFormID(viewID)));
            //cache retrieve update callback
            if (!c.ajaxRefresh) {
                c.ajaxRefresh = retrieveUpdate(viewID);
            }
            c.ajaxRefresh();
        };

        namespace.setupBridge = function(setupID, viewID, windowID, configuration) {
            var container = document.getElementById(setupID).parentNode;
            container.setupCount = container.setupCount ? (container.setupCount + 1) : 1;

            if (container.setupCount == 1) {
                container.configuration = configuration;
                container.configuration.viewID = viewID;
                namespace.window = windowID;
                if (configuration.sendDisposeWindow) {
                    onBeforeUnload(window, disposeWindow(viewID));
                }
                if (configuration.focusManaged) {
                    monitorFocusChanges(container);
                    restoreMonitorFocusChangesOnUpdate(container);
                }
                if (configuration.clientSideElementUpdateDetermination) {
                    switchToClientSideElementUpdateDetermination();
                }
                if (configuration.blockUIOnSubmit) {
                    onBeforeUnload(window, function() {
                        startBlockingUI();
                    });
                }
                setupDefaultIndicators(container, configuration);
                clearEventHandlersOnUnload(container);
            }
        };

        namespace.setupPush = function(viewID) {
            var retrieveViewUpdate = retrieveUpdate(viewID);
            ice.push.register([viewID], retrieveViewUpdate);
            ice.onBlockingConnectionReEstablished(retrieveViewUpdate);
        };

        namespace.unsetupPush = function(viewID) {
            ice.push.deregister([viewID]);
        };

        namespace.setupRefresh = function(viewID, interval, duration, id) {
            var times = duration < 0 ? null : Math.floor(duration / interval);
            var requestUpdate = retrieveUpdate(viewID);
            var delay = Delay(requestUpdate, interval);
            run(delay, times);
            var stopDelay = curry(stop, delay);
            namespace.onSessionExpiry(stopDelay);
            namespace.onNetworkError(stopDelay);
            namespace.onServerError(stopDelay);
            namespace.onUnload(stopDelay);
            namespace.onElementUpdate(id, stopDelay);
        };

        namespace.calculateInitialParameters = function(id) {
            var f = document.getElementById(id);
            f.previousParameters = HashSet(jsf.getViewState(f).split('&'));
        };

        //clear network listeners just before reloading or navigating away to avoid falsely notified errors
        onBeforeUnload(window, function() {
            empty(networkErrorListeners);
        });

        onKeyPress(document, function(ev) {
            var e = $event(ev);
            if (isEscKey(e)) cancelDefaultAction(e);
        });

        //include session-expiry-warning.js
        //include clear-callbacks.js
        //include capture-submit.js
        //include fix-viewstate.js
        //include element-update.js
        //include element-remove.js
        //include user-inactivity.js
        //include status.js
        //include offline.js
        //include fixjsf.js
    })(window.ice);
}

