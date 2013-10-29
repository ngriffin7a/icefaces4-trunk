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

var setupDefaultIndicators;
(function () {
    var off = operator();

    function PopupIndicator(message, description, panel) {
        var backPanel = panel();
        var messageContainer = document.body.appendChild(document.createElement('div'));
        messageContainer.className = 'ice-status-indicator';
        var messageContainerStyle = messageContainer.style;
        messageContainerStyle.position = 'absolute';
        messageContainerStyle.textAlign = 'center';
        messageContainerStyle.zIndex = '28001';
        messageContainerStyle.color = 'black';
        messageContainerStyle.backgroundColor = 'white';
        messageContainerStyle.paddingLeft = '10px';
        messageContainerStyle.paddingRight = '10px';
        messageContainerStyle.paddingTop = '15px';
        messageContainerStyle.paddingBottom = '15px';
        messageContainerStyle.borderBottomColor = 'gray';
        messageContainerStyle.borderRightColor = 'gray';
        messageContainerStyle.borderTopColor = 'silver';
        messageContainerStyle.borderLeftColor = 'silver';
        messageContainerStyle.borderWidth = '2px';
        messageContainerStyle.borderStyle = 'solid';

        var messageElement = messageContainer.appendChild(document.createElement('div'));
        messageElement.appendChild(document.createTextNode(message));
        messageElement.className = 'ice-status-indicator-message';
        var messageElementStyle = messageElement.style;
        messageElementStyle.textAlign = 'left';
        messageElementStyle.fontSize = '14px';
        messageElementStyle.fontSize = '14px';
        messageElementStyle.fontWeight = 'bold';

        var descriptionElement = messageElement.appendChild(document.createElement('div'));
        descriptionElement.innerHTML = description;
        descriptionElement.className = 'ice-status-indicator-description';
        var descriptionElementStyle = descriptionElement.style;
        descriptionElementStyle.fontSize = '11px';
        descriptionElementStyle.marginTop = '7px';
        descriptionElementStyle.marginBottom = '7px';
        descriptionElementStyle.fontWeight = 'normal';

        var resize = function () {
            messageContainerStyle.left = ((window.width() - messageContainer.clientWidth) / 2) + 'px';
            messageContainerStyle.top = ((window.height() - messageContainer.clientHeight) / 2) + 'px';
        };
        resize();
        var clearOnResize = onResize(window, resize);

        return object(function (method) {
            method(off, function (self) {
                if (messageContainer) {
                    try {
                        document.body.removeChild(messageContainer);
                        clearOnResize();
                        off(backPanel);
                    } finally {
                        messageContainer = null;
                    }
                }
            });
        });
    }

    function BackgroundOverlay(container) {
        return function () {
            var overlay = container.ownerDocument.createElement('iframe');
            overlay.setAttribute('src', 'about:blank');
            overlay.setAttribute('frameborder', '0');
            overlay.className = 'ice-status-indicator-overlay';
            var overlayStyle = overlay.style;
            overlayStyle.top = '0';
            overlayStyle.left = '0';
            container.appendChild(overlay);

            var resize = container.tagName.toLowerCase() == 'body' ?
                function () {
                    overlayStyle.width = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth) + 'px';
                    overlayStyle.height = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight) + 'px';
                } :
                function () {
                    overlayStyle.width = container.offsetWidth + 'px';
                    overlayStyle.height = container.offsetHeight + 'px';
                };
            resize();
            var clearOnResize = onResize(window, resize)

            return object(function (method) {
                method(off, function (self) {
                    if (overlay) {
                        try {
                            container.removeChild(overlay);
                            clearOnResize();
                        } finally {
                            overlay = null;
                        }
                    }
                });
            });
        }
    }

    function extractTagContent(tag, html) {
        var start = new RegExp('\<' + tag + '[^\<]*\>', 'g').exec(html);
        var end = new RegExp('\<\/' + tag + '\>', 'g').exec(html);
        var tagWithContent = html.substring(start.index, end.index + end[0].length);
        return tagWithContent.substring(tagWithContent.indexOf('>') + 1, tagWithContent.lastIndexOf('<'));
    }

    setupDefaultIndicators = function (container, configuration) {
        var overlay = BackgroundOverlay(container);
        var beforeSessionExpiryIndicator = object(function (method) {
            method(off, noop);
        });

        function showIndicators() {
            return !(namespace.disableDefaultErrorPopups || configuration.disableDefaultErrorPopups);
        }

        namespace.onServerError(function (code, html, xmlContent) {
            if (showIndicators()) {
                //test if server error message is formatted in XML
                var message;
                var description;
                if (xmlContent) {
                    message = xmlContent.getElementsByTagName("error-message")[0].firstChild.nodeValue;
                    description = xmlContent.getElementsByTagName("error-name")[0].firstChild.nodeValue;
                } else {
                    message = extractTagContent('title', html);
                    description = extractTagContent('body', html);
                }
                PopupIndicator(message, description, overlay);
            }
        });

        namespace.onNetworkError(function () {
            if (showIndicators()) {
                PopupIndicator("Network Connection Interrupted", "Reload this page to try to reconnect.", overlay);
            }
        });

        namespace.onSessionExpiry(function () {
            if (showIndicators()) {
                off(beforeSessionExpiryIndicator);
                PopupIndicator("User Session Expired", "Reload this page to start a new user session.", overlay);
            }
        });

        namespace.onBeforeSessionExpiry(function (time) {
            if (showIndicators()) {
                beforeSessionExpiryIndicator = PopupIndicator("User Session is about to expire in " + time + " seconds.", "Reload this page to keep your current user session.", overlay);
            }
        });
    }
})();
