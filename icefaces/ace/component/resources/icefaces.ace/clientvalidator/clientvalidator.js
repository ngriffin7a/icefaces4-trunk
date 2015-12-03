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
    function formOf(id) {
        var cursor = document.getElementById(id);
        while (cursor) {
            if (cursor.nodeName.toLowerCase() == 'form') {
                return cursor;
            }
            cursor = cursor.parentNode;
        }

        return null;
    }

    function isParent(parent, element) {
        var cursor = element;
        while (cursor) {
            if (cursor == parent) {
                return true;
            }
            cursor = cursor.parentNode;
        }

        return false;
    }

    function cleanupMessages(elements) {
        for (var i = 0, l = elements.length; i < l; i++) {
            var element = elements[i];
            element.className = element.className.replace(' ui-state-error', '');
            var cleanupValidationMessage = element.cleanupValidationMessage;
            if (cleanupValidationMessage) {
                try {
                    cleanupValidationMessage();
                } catch (ex) {
                    //the node is cleared a second time
                }
            }
        }
    }

    window.addEventListener('load', function () {
        var old = ice.fullSubmit;
        ice.fullSubmit = function (execute, render, event, element, additionalParameters, callbacks) {
            var form = formOf(element.id);
            var jqForm = ice.ace.jq(form);
            var isValidForm = jqForm.valid();
            var validationResult = jqForm.validate();
            var validElements = validationResult.validElements();
            if (validElements) {
                cleanupMessages(validElements);
            }

            var skipValidation = false;
            var invalidElements = validationResult.invalidElements();
            for (var j = 0, k = invalidElements.length; j < k; j++) {
                var invalidElement = invalidElements[j];
                if (isParent(element, invalidElement) && invalidElement.immediate) {
                    skipValidation = true;
                    cleanupMessages(invalidElements);
                    break;
                }
            }

            if (skipValidation || (form && isValidForm)) {
                old(execute, render, event, element, additionalParameters, callbacks);
            }
        };
    });

    function clientValidationMessageFor(id, text) {
        return function (parameter, element) {
            var messageId = id + '_msg';
            var container = document.getElementById(messageId);
            if (container.childNodes.length == 0) {
                element.className += ' ui-state-error';
                container.className = 'ui-widget ui-corner-all ui-state-error';
                var icon = container.appendChild(document.createElement('span'));
                icon.className = 'ui-faces-message-icon';
                var iconAlert = icon.appendChild(document.createElement('span'));
                iconAlert.className = 'ui-icon ui-icon-alert';
                var message = container.appendChild(document.createElement('span'));
                message.className = 'ui-faces-message-text';
                message.appendChild(document.createTextNode(text));

                element.cleanupValidationMessage = function () {
                    container.className = '';
                    container.removeChild(icon);
                    container.removeChild(message);
                };
            } else {
                container.childNodes[1].innerHTML = text;
            }

            return '';
        }
    }

    function clientValidationMessagesFor(id, text) {
        return function (parameter, element) {
            var messageId = id + '_msg_' + element.id;
            var container = document.getElementById(id);
            var node = document.getElementById(messageId);
            if (!node) {
                element.className += ' ui-state-error';
                node = container.appendChild(document.createElement('div'));
                node.id = messageId;
                node.className = 'ui-corner-all ui-state-error';
                var icon = node.appendChild(document.createElement('span'));
                icon.className = 'ui-icon ui-icon-alert';
                node.appendChild(document.createTextNode(text));

                element.cleanupValidationMessage = function () {
                    container.removeChild(node);
                };
            } else {
                node.childNodes[1].textContent = text;
            }

            return '';
        }
    }

    function clientValidationGrowlMessagesFor(id, messageText, config) {
        return function (parameter, element) {
            config.msgs = [{text: messageText, icon: 'alert', state: 'error', sticky: false}];

            ice.ace.GrowlMessages(id, config);
            element.cleanupValidationMessage = function () {
            };
        }
    }

    ice.ace.setupClientValidation = function (id, rule, config, messageType, message, immediate) {
        var form = formOf(id);
        if (!form.enabledValidation) {
            ice.ace.jq(ice.ace.escapeClientId(form.id)).validate().settings.showErrors = function(){};
            form.enabledValidation = true;
        }

        function setup() {
            var selector = ice.ace.escapeClientId(id);

            ice.ace.jq(selector).rules('remove', rule);

            var ruleConfig = {};
            ruleConfig[rule] = config;
            var messageCreator = {};
            if (messageType.aceMessage) {
                messageCreator[rule] = clientValidationMessageFor(messageType.id, message);
            } else if (messageType.aceMessages) {
                messageCreator[rule] = clientValidationMessagesFor(messageType.id, message);
            } else if (messageType.aceGrowlMessages) {
                messageCreator[rule] = clientValidationGrowlMessagesFor(messageType.id, message, messageType.configuration);
            }
            ruleConfig['messages'] = messageCreator;

            ice.ace.jq(selector).rules('add', ruleConfig);
            document.getElementById(id).immediate = immediate;
        }
        ice.onElementUpdate(id, setup);
        setup();
    }
})();