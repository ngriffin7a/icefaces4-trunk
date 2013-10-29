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

(function() {
    var off = operator();

    function Overlay(element) {
        var container = element || document.body;
        var overlay = container.ownerDocument.createElement('iframe');
        overlay.setAttribute('src', 'about:blank');
        overlay.setAttribute('frameborder', '0');
        overlay.className = 'ice-blockui-overlay';
        var overlayStyle = overlay.style;
        overlayStyle.top = '0';
        overlayStyle.left = '0';

        if (container.tagName.toLowerCase() == 'body') {
            overlayStyle.width = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth) + 'px';
            overlayStyle.height = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight) + 'px';
        } else {
            overlayStyle.width = container.offsetWidth + 'px';
            overlayStyle.height = container.offsetHeight + 'px';
        }

        container.appendChild(overlay);

        return object(function(method) {
            method(off, function(self) {
                if (overlay) {
                    try {
                        container.removeChild(overlay);
                    } catch (e) {
                        //ignore, the overlay does not match the document after a html/body level update
                    }
                }
            });
        });
    }

    //in the future this function can be modified to read configuration set per component container element 
    function isBlockUIEnabled(source) {
        return configurationOf(source).blockUIOnSubmit;
    }

    function eventSink(element) {
        return function(e) {
            var ev = $event(e, element);
            var evenType = type(ev);
            var triggeringElement = triggeredBy(ev);
            var capturingElement = capturedBy(ev);
            cancel(ev);
            debug(logger, 'event [type: ' + evenType +
                ', triggered by: ' + (identifier(triggeringElement) || triggeringElement) +
                ', captured in: ' + (identifier(capturingElement) || capturingElement) + '] was discarded.');
        }
    }

    function isBlurEvent() {
        var c = arguments.callee.caller;
        while (c) {
            if (c == namespace.fullSubmit) {
                var eventArgument = c.arguments[2];
                if (eventArgument)  {
                    return eventArgument.type == 'blur';
                } else {
                    return false;
                }
            }
            c = c.arguments.callee.caller;
        }

        return false;
    }

    var stopBlockingUI = noop;
    namespace.onBeforeSubmit(function(source, isClientRequest) {
        //Only block the UI for client-initiated requests (not push requests)
        //do not block submit triggered by the 'blur' event -- most probably it is issued by a partial submits
        if (isClientRequest && isBlockUIEnabled(source) && not(isBlurEvent())) {
            debug(logger, 'blocking UI');
            var blockUIOverlay = Overlay();
            var rollbacks = inject(['input', 'select', 'textarea', 'button', 'a'], [], function(result, type) {
                return concatenate(result, asArray(collect(document.body.getElementsByTagName(type), function(e) {
                    var sink = eventSink(e);
                    var onkeypress = e.onkeypress;
                    var onkeyup = e.onkeyup;
                    var onkeydown = e.onkeydown;
                    var onclick = e.onclick;
                    e.onkeypress = sink;
                    e.onkeyup = sink;
                    e.onkeydown = sink;
                    e.onclick = sink;

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
                })));
            });

            stopBlockingUI = function() {
                broadcast(rollbacks);
                off(blockUIOverlay);
                debug(logger, 'unblocked UI');
            };
        } else {
            stopBlockingUI = noop;
        }
    });

    namespace.onBeforeUpdate(function() {
        stopBlockingUI();
    });
})();
