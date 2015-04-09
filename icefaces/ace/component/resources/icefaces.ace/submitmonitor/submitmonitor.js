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

(function() {
    var consoleLog = function(msg) {
        if (window.console) {
            //console.log(msg);
        }
    };

    var broadcast = function(funcs, args) {
        args = args || [];

        for (var i in funcs) {
            if (!isNaN(parseInt(i))) {
                funcs[i].apply(funcs[i],args);
            }
        }
    };

    var fold = function(items, initialValue, injector) {
        var tally = initialValue;
        var size = items.length;
        for (var i = 0; i < size; i++) {
            tally = injector(tally, items[i]);
        }

        return tally;
    };

    var isParentElement = function(parent, child) {
        var cursor = child;
        while (cursor) {
            if (parent == cursor) {
                return true;
            } else {
                cursor = cursor.parentNode;
            }
        }

        return false;
    };

    var isSessionExpired = function(xmlContent) {
        var sessionExpired = false;
        if (xmlContent && xmlContent.documentElement) {
            var errorNames = xmlContent.getElementsByTagName("error-name");
            if (errorNames && errorNames.length > 0) {
                var errorName = errorNames[0].firstChild.nodeValue;
                if (errorName && errorName.indexOf('org.icefaces.application.SessionExpiredException') >= 0) {
                    sessionExpired = true;
                }
            }
        }
        return sessionExpired;
    };

    function Overlay(cfg, container, whenShownFunc) {
        // If the request is processed before our timeouts for adding the
        // elements, then we need to never add them.
        var addElements = true;
        var addDelay = 250;

        var overlay = document.createElement('div');
        overlay.className = 'ui-widget-overlay';
        overlay.style.cssText = 'position: absolute; z-index: 28000; zoom: 1; display: none;';
        consoleLog('Overlay  Built overlay');
        if (container == document.body) {
            container.appendChild(overlay);
        } else {
            container.parentNode.appendChild(overlay);
        }
        consoleLog('Overlay  Added overlay');

        var cloneToRemove;
        var revertElem;
        var revertZIndex;
        if (cfg.autoCenter) {
            cloneToRemove = ice.ace.jq(ice.ace.escapeClientId(cfg.id)+"_display").clone(false,true);
            cloneToRemove.attr('id', cfg.id + '_clone');
            cloneToRemove.addClass('clone ui-panel ui-widget-content ui-corner-all');
            cloneToRemove.css('z-index', '28001');
            cloneToRemove.css('display', 'none');
            cloneToRemove.children().addClass('ui-panel-titlebar ui-widget-header ui-corner-all');
            consoleLog('Overlay  autoCenter  built clone');
            if (container == document.body) {
                cloneToRemove.appendTo(container);
                cloneToRemove.css('position', 'fixed');
                consoleLog('Overlay  autoCenter  added clone over body');
            } else {
                cloneToRemove.appendTo(container.parentNode);
                cloneToRemove.css('position', 'absolute');
                consoleLog('Overlay  autoCenter  added clone over other');
            }
        } else {
            revertElem = ice.ace.jq(ice.ace.escapeClientId(cfg.id)+"_display");
            consoleLog('Overlay  !autoCenter  found revert');
        }

        setTimeout(function() {
            consoleLog('Overlay  setTimeout to add overlay / clone / revert  addElements: ' + addElements);
            if (whenShownFunc) {
                whenShownFunc();
                whenShownFunc = null;
            }
            if (!addElements) {
                return;
            }
            if (revertElem) {
                revertZIndex = revertElem.css('z-index');
                revertElem.css('z-index', '28001');
                revertElem.css('display', '');
                consoleLog('Overlay  setTimeout  showed revert');
            }
            if (overlay) {
                var overlayWidth = 0, overlayHeight = 0;
                if (container == document.body) {
                    overlayWidth = Math.max(document.documentElement.scrollWidth,
                        Math.max(document.body.scrollWidth, document.body.parentNode.offsetWidth));
                    overlayHeight = Math.max(document.documentElement.scrollHeight,
                        Math.max(document.body.scrollHeight, document.body.parentNode.offsetHeight));
                } else {
                    overlayWidth = container.offsetWidth;
                    overlayHeight = container.offsetHeight;
                }
                var x = container.offsetTop;
                var y = container.offsetLeft;
                overlay.style.cssText = 'top: ' + x + 'px; left: ' + y + 'px; width: '+overlayWidth+'px; height: '+overlayHeight+'px; position: absolute; z-index: 28000; zoom: 1;';
                if (container != document.body) {
                    ice.ace.jq(overlay).position({
                        my: 'left top',
                        at: 'left top',
                        of: container,
                        collision: 'none'});
                }
                consoleLog('Overlay  setTimeout  showed and positioned overlay');
            }
            if (cloneToRemove) {
                cloneToRemove.css('display', '');
                if (container == document.body) {
                    cloneToRemove.position({
                        my: 'center center',
                        at: 'center center',
                        of: window,
                        collision: 'fit'});
                    consoleLog('Overlay  setTimeout  showed and positioned clone over body');
                } else {
                    cloneToRemove.position({
                        my: 'center center',
                        at: 'center center',
                        of: container,
                        collision: 'fit'});
                    consoleLog('Overlay  setTimeout  showed and positioned clone over other');
                }
            }
        }, addDelay);

        return function() {
            consoleLog('Overlay  function to cleanup overlay and clone  addElements(sets false): ' + addElements);
            addElements = false;
            if (overlay) {
                try {
                    overlay.parentNode.removeChild(overlay);
                } catch (e) { //ignore, the overlay does not match the document after a html/body level update
                }
            }
            if (cloneToRemove) {
                try {
                    cloneToRemove.remove();
                } catch (e) { //ignore, the cloneToRemove does not match the document after a html/body level update
                }
            }
            if (revertElem) {
                try {
                    revertElem.css('z-index', revertZIndex);
                    revertElem.css('display', 'none');
                } catch (e) { //ignore, the cloneToRemove does not match the document after a html/body level update
                }
            }
        };
    }

    var anticipationStrings = ['unanticipated', 'anticipated', 'commenced'];
    var UNANTICIPATED = 0, ANTICIPATED = 1, COMMENCED = 2;
    var anticipatePossibleSecondSubmit = UNANTICIPATED;

    var NOOP = function () {
        consoleLog('stopBlockingUI NOOP');
    };
    consoleLog('stopBlockingUI = NOOP  from  init');

    if (!ice.ace) ice.ace = {};

    var uniqueCounter = 0;
    var beforeSubmit = [];
    var beforeUpdate = [];
    var cleanBeforeSubmit = [];
    var cleanBeforeUpdate = [];
    var elementMonitorMapping = {};

    ice.ace.SubmitMonitor = function (id, cfg) {
        var jqId = ice.ace.escapeClientId(cfg.id);
        var uniqueId = uniqueCounter++;
        var stopBlockingUI = NOOP;

        //cleanup previous mapping for this submit monitor (in case of partial update/re-configure)
        var mapping = {};
        for (var p in elementMonitorMapping) {
            if (elementMonitorMapping.hasOwnProperty(p)) {
                var v = elementMonitorMapping[p];
                if (v != id) {
                    mapping[p] = v;
                }
            }
        }
        elementMonitorMapping = mapping;

        var monitoredElementIDs = cfg.monitorFor;
        if (monitoredElementIDs) {
            var ids = monitoredElementIDs.split(" ");
            for (var i = 0, l = ids.length; i < l; i++) {
                elementMonitorMapping[ids[i]] = id;
            }
        } else {
            elementMonitorMapping['body'] = id;
        }

        function isMonitoringElement(source) {
            if (!source) {
                return false;
            }
            var cursor = source;
            while (cursor) {
                var elementID = cursor == document.body ? 'body' : cursor.id;
                if (elementID) {
                    var monitorID = elementMonitorMapping[elementID];
                    if (monitorID) {
                        //execute this monitor only when the first element encountered (while traversing the ancestors)
                        //has a corresponding monitor that matches the current one
                        return monitorID == id;
                    }
                }
                cursor = cursor.parentNode;
            }

            return false;
        }

        function getBlockUIProperty() {
            return (cfg.blockUI == undefined) ? '@all' : cfg.blockUI;
        }

        function isBlockUIEnabled() {
            return (getBlockUIProperty() != '@none');
        }

        function isBlockUITypeAmenableToCombining() {
            var rawBlockUI = getBlockUIProperty();
            return ( (rawBlockUI != '@source') && (rawBlockUI != '@none') );
        }

        function resolveBlockUIElement(source) {
            var rawBlockUI = getBlockUIProperty();
            if (rawBlockUI == '@all') {
                return document.body;
            } else if (rawBlockUI == '@source') {
                return source;
            } else if (rawBlockUI == '@none') {
                return null;
            } else {
                var elem = ice.ace.jq(ice.ace.escapeClientId(rawBlockUI));
                if (elem && elem.length > 0) {
                    return elem[0];
                }
                return null;
            }
        }

        function eventSink(element) {
            return function(e) {
				e = e || window.event;
                var eventType = ( (e.type != undefined && e.type != null) ? e.type : null );
                var triggeringElement = e.srcElement ? e.srcElement : e.target;
                var capturingElement = element;
                if (e.stopPropagation) {
                    e.stopPropagation();
                } else {
                    e.cancelBubble = true;
                }
                consoleLog('Monitor '+uniqueId+'>'+jqId+'  event [type: ' + eventType +
                        ', triggered by: ' + (triggeringElement.id || triggeringElement) +
                        ', captured in: ' + (capturingElement.id || capturingElement) + '] was discarded.');
                return false;
            }
        }

        var allStates = ['idle', 'active', 'serverError', 'networkError', 'sessionExpired'];
        var IDLE = 0, ACTIVE = 1, SERVER_ERROR = 2, NETWORK_ERROR = 3, SESSION_EXPIRED = 4;
        var currentState = IDLE;

        var changeState = function(state) {
            currentState = state;
            consoleLog('Monitor '+uniqueId+'>'+jqId+'  changeState: ' + state + ' : ' + allStates[state]);
            ice.ace.jq(jqId+'_display > div.ice-sub-mon-mid').hide().filter('.'+allStates[state]).show();
            ice.ace.jq(jqId+'_clone > div.ice-sub-mon-mid').hide().filter('.'+allStates[state]).show();
        };


        var begunApplicableToThis = false;

        var doOverlayIfBlockingUI = function(source) {
            //Only block the UI for client-initiated requests (not push requests)
            if (isBlockUIEnabled()) {
                consoleLog('Monitor '+uniqueId+'>'+jqId+'  doOverlayIfBlockingUI  Blocking UI');

                var overlayShown = false;
                var overlayShownFunc = function() {
                    overlayShown = true;
                };
                var eventSinkFirstClickCount = 0;
                function eventSinkFirstClick(firstSubmitSource, element, originalOnclick, regularSink) {
                    return function(e) {
                        consoleLog('Monitor '+uniqueId+'>'+jqId+'  eventSinkFirstClick()  overlayShown: ' + overlayShown + '  eventSinkFirstClickCount: ' + eventSinkFirstClickCount);
                        if (overlayShown) {
                            consoleLog('eventSinkFirstClick()  overlay shown');
                            return regularSink(e);
                        }
                        if (eventSinkFirstClickCount > 0) {
                            consoleLog('eventSinkFirstClick()  not first click');
                            return regularSink(e);
                        }
                        eventSinkFirstClickCount++;

						e = e || window.event;
                        var triggeringElement = ( (e.srcElement != undefined && e.srcElement != null) ? e.srcElement : e.target);
                        consoleLog('event [type: ' + e.type +
                                ', triggered by: ' + (triggeringElement.id || triggeringElement) +
                                ', captured in: ' + (element.id || element) + ']');
                        consoleLog('first submit element: ' + (firstSubmitSource.id || firstSubmitSource));
                        if ((firstSubmitSource == triggeringElement) || (firstSubmitSource == element)) {
                            consoleLog('eventSinkFirstClick()  clicked on same element as first submit');
                            return regularSink(e);
                        }

                        consoleLog('eventSinkFirstClick()  calling original onclick');
                        // Might not be an onclick directly on that element, it might
                        // have to bubble up
                        anticipatePossibleSecondSubmit = ANTICIPATED;
                        if (originalOnclick) {
                            return originalOnclick.call(element, e);
                        }
                    }
                }

                consoleLog('Monitor '+uniqueId+'>'+jqId+'  doOverlayIfBlockingUI  after eventSinkFirstClick');
                var overlayContainerElem = resolveBlockUIElement(source);
                consoleLog('Monitor '+uniqueId+'>'+jqId+'  doOverlayIfBlockingUI  overlayContainerElem: ' + overlayContainerElem);
                var blockUIOverlay = Overlay(cfg, overlayContainerElem, overlayShownFunc);
                overlayShownFunc = null;
                var rollbacks = fold(['input', 'select', 'textarea', 'button', 'a'], [], function(result, type) {
                    return result.concat(
                            ice.ace.jq.map(overlayContainerElem.getElementsByTagName(type), function(e) {
                        var sink = eventSink(e);
                        var onkeypress = e.onkeypress;
                        var onkeyup = e.onkeyup;
                        var onkeydown = e.onkeydown;
                        var onclick = e.onclick;
                        var sinkClick = eventSinkFirstClick(source, e, onclick, sink);
                        e.onkeypress = sink;
                        e.onkeyup = sink;
                        e.onkeydown = sink;
                        e.onclick = sinkClick;

                        return function() {
                            try {
                                e.onkeypress = onkeypress;
                                e.onkeyup = onkeyup;
                                e.onkeydown = onkeydown;
                                e.onclick = onclick;
                            } catch (ex) {
                                //don't fail if element is not present anymore
                            }
                        };
                    })
                    );
                });

                stopBlockingUI = function() {
                    broadcast(rollbacks);
                    if (blockUIOverlay) {
                        blockUIOverlay();
                        blockUIOverlay = null;
                    }
                    stopBlockingUI = NOOP;
                    consoleLog('Monitor '+uniqueId+'>'+jqId+'  Unblocked UI');
                };
            } else {
                consoleLog('Monitor '+uniqueId+'>'+jqId+'  stopBlockingUI = NOOP  from  else of isBlockUIEnabled()');
                stopBlockingUI = NOOP;
            }
        };

        var CLEANUP_UNNECESSARY = 0, CLEANUP_PENDING = 1, CLEANUP_ACKNOWLEDGED = 2;
        var cleanup = CLEANUP_UNNECESSARY;

        function handleCleanup(isBeforeSubmit) {
            if (cleanup == CLEANUP_ACKNOWLEDGED) {
                consoleLog('Monitor '+uniqueId+'>'+jqId+'  handleCleanup  DEAD');
                return true;
            } else if (cleanup == CLEANUP_PENDING) {
                cleanup = CLEANUP_ACKNOWLEDGED;
                consoleLog('Monitor '+uniqueId+'>'+jqId+'  handleCleanup  CLEANUP PENDING -> ACKNOWLEDGED');
                setTimeout(function() {
                    consoleLog('Monitor '+uniqueId+'>'+jqId+'  handleCleanup  setTimeout cleanup');
                    if (cleanBeforeSubmit[uniqueId]) {
                        cleanBeforeSubmit[uniqueId]();
                        cleanBeforeSubmit[uniqueId] = null;
                        beforeSubmit[uniqueId] = null;
                    }
                    if (cleanBeforeUpdate[uniqueId]) {
                        cleanBeforeUpdate[uniqueId]();
                        cleanBeforeUpdate[uniqueId] = null;
                        beforeUpdate[uniqueId] = null;
                    }
                    if (cleanServerError) {
                        cleanServerError();
                        cleanServerError = null;
                    }
                    if (cleanNetworkError) {
                        cleanNetworkError();
                        cleanNetworkError = null;
                    }
                }, 270);
                return isBeforeSubmit;
            }
            return false;
        }

        //override the primitive submit function with one that will block sub-sequent calls
        var lock = false;
        var originalSubmitFunction = ice.submitFunction;
        ice.submitFunction = function(element, event, options) {
            var blockedContainer = resolveBlockUIElement(element);
            if (!blockedContainer || (isParentElement(blockedContainer, element) && !lock)) {
                lock = true;
                var originalOnEvent = options.onevent;
                options.onevent = function(submitEvent) {
                    if (submitEvent.status == 'success') {
                        lock = false;
                    }
                    if (originalOnEvent) {
                        originalOnEvent(submitEvent);
                    }
                };
                originalSubmitFunction(element, event, options);
            }
        };

        consoleLog('Monitor '+uniqueId+'>'+jqId+'  Register onElementUpdate: '+cfg.id+'_script');

        window.ice.onElementUpdate(cfg.id+'_script', function() {
            cleanup = CLEANUP_PENDING;
            //revert to the original (overridden) submit function
            //there can be multiple levels when more than on submit monitor is on the page
            ice.submitFunction = originalSubmitFunction;
            consoleLog('Monitor '+uniqueId+'>'+jqId+'  onElementUpdate  -> CLEANUP_PENDING');
        });

        beforeSubmit.push(function(source, isClientRequest) {
            if (handleCleanup(true)) {
                return;
            }
            if (!isClientRequest) {
                return;
            }
            if (!isMonitoringElement(source)) {
                consoleLog('Monitor '+uniqueId+'>'+jqId+'  onBeforeSubmit()  NOT monitoring source: ' + source + '  id: ' + source.id);
                return;
            }
            begunApplicableToThis = true;

            consoleLog('Monitor '+uniqueId+'>'+jqId+'  onBeforeSubmit()  IS  monitoring source: ' + source + '  id: ' + source.id);
            consoleLog('Monitor '+uniqueId+'>'+jqId+'  onBeforeSubmit()  ' + anticipationStrings[anticipatePossibleSecondSubmit]);
            if (isBlockUITypeAmenableToCombining() && (anticipatePossibleSecondSubmit == ANTICIPATED)) {
                consoleLog('onBeforeSubmit()  anticipated -> commenced');
                anticipatePossibleSecondSubmit = COMMENCED;
            } else {
                consoleLog('onBeforeSubmit()  regular');
                changeState(ACTIVE);
                doOverlayIfBlockingUI(source);
            }
        });

        var whenUpdate = function(xmlContent, source) {
            consoleLog('Monitor '+uniqueId+'>'+jqId+'  whenUpdate()  stopping');
            anticipatePossibleSecondSubmit = UNANTICIPATED;
            stopBlockingUI();
            changeState(IDLE);
        };

        beforeUpdate.push(function(xmlContent, source) {
            if (handleCleanup(false)) {
                return;
            }
            // Can't use isMonitoringElement(source) here since source is from
            // before the update, so doesn't necessarily exist any more, nor
            // a new component with the same id.
            if (!begunApplicableToThis) {
                consoleLog('Monitor '+uniqueId+'>'+jqId+'  onBeforeUpdate()  NOT begunApplicableToThis for source: ' + source + '  id: ' + source.id);
                return;
            }
            begunApplicableToThis = false;
            consoleLog('Monitor '+uniqueId+'>'+jqId+'  onBeforeUpdate()  IS  begunApplicableToThis for source: ' + source + '  id: ' + source.id);

            consoleLog('Monitor '+uniqueId+'>'+jqId+'  onBeforeUpdate()  ' + anticipationStrings[anticipatePossibleSecondSubmit]);
            if (isBlockUITypeAmenableToCombining() && (anticipatePossibleSecondSubmit == ANTICIPATED)) {
                setTimeout(function() {
                    consoleLog('Monitor '+uniqueId+'>'+jqId+'  onBeforeUpdate()  DELAYED  ' + anticipationStrings[anticipatePossibleSecondSubmit]);
                    if (anticipatePossibleSecondSubmit != COMMENCED) {
                        whenUpdate(xmlContent, source);
                    }
                }, 260);
            } else if (isSessionExpired(xmlContent)) {
                consoleLog('Monitor '+uniqueId+'>'+jqId+'  onBeforeUpdate()  isSessionExpired');
                anticipatePossibleSecondSubmit = UNANTICIPATED;
                changeState(SESSION_EXPIRED);
            } else {
                whenUpdate(xmlContent, source);
            }
        });

        var cleanServerError = window.ice.onServerError(function() {
            if (handleCleanup(false)) {
                return;
            }
            anticipatePossibleSecondSubmit = UNANTICIPATED;
            changeState(SERVER_ERROR);
        });

        var cleanNetworkError = window.ice.onNetworkError(function() {
            if (handleCleanup(false)) {
                return;
            }
            anticipatePossibleSecondSubmit = UNANTICIPATED;
            changeState(NETWORK_ERROR);
        });

        changeState(IDLE);
    };

    window.ice.onBeforeSubmit(function(source, isClientRequest) {
        for (var i = 0, l = beforeSubmit.length; i < l; i++) {
            var callback = beforeSubmit[i];
            if (callback) {
                cleanBeforeSubmit[i] = callback(source, isClientRequest);
            }
        }
    });

    window.ice.onBeforeUpdate(function(xmlContent, source) {
        for (var l = beforeUpdate.length, i = l - 1; i > -1; i--) {
            var callback = beforeUpdate[i];
            if (callback) {
                cleanBeforeUpdate[i] = callback(xmlContent, source);
            }
        }
    });
})();