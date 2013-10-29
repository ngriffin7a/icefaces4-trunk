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

var singleSubmitExecuteThis;
var singleSubmitExecuteThisRenderThis;
var submit;
var fullSubmit;
var singleSubmit;

(function() {
    function idOrElement(e) {
        return isString(e) ? document.getElementById(e) : e;
    }

    function formOf(element) {
        return toLowerCase(element.nodeName) == 'form' ? element : enclosingForm(element);
    }

    function isAjaxDisabled(formID, element) {
        var elementID = element.id;
        //lookup element that contains the list of elements that have AJAX submission disabled
        var disablingMarker = document.getElementById(formID + ":ajaxDisabled");
        return disablingMarker && contains(split(trim(disablingMarker.value), ' '), elementID);
    }

    function standardFormSerialization(element) {
        return configurationOf(element).standardFormSerialization;
    }

    function serializeEventToOptions(event, options) {
        var collectingQuery = object(function(method) {
            method(addNameValue, function(self, name, value) {
                options[name] = value;
            });
        });
        serializeOn(event, collectingQuery);
    }

    function encodedURLOf(form) {
        return form['javax.faces.encodedURL'] ? form['javax.faces.encodedURL'].value : form.action;
    }

    function serializeAdditionalParameters(additionalParameters, options) {
        if (additionalParameters) {
            additionalParameters(function(name, value) {
                options[name] = value;
            });
        }
    }

    //ICE-7456: When using singleSubmit, MyFaces does not include the
    //          element.name as it does when f:ajax is used. So here
    //          we add it if it's a valid value and not there already.
    //          The strategy is similar to what Mojarra already does.
    function fixExecuteParameter(execute, element) {
        if (execute && element.name && element.id) {
            var execIds = execute.split(' ');
            for (var i = 0, size = execIds.length; i < size; i++) {
                if (execIds[i] == element.name) {
                    return execute;
                }
            }
            execute = execute + ' ' + element.name;
        }
        return execute;
    }

    singleSubmit = function(execute, render, event, element, additionalParameters, callbacks) {
        var viewID = viewIDOf(element);
        var form = document.getElementById(singleSubmitFormID(viewID));
        var clonedElements = [];
        try {
            var clonedElement = form.appendChild(element.cloneNode(true));
            append(clonedElements, clonedElement);

            var tagName = toLowerCase(element.nodeName);
            //copy state which IE won't copy during cloning
            if (tagName == 'input') {
                if (element.type == 'radio') {
                    clonedElement.checked = element.checked;
                    execute = fixExecuteParameter(execute, element);
                }
                if (element.type == 'checkbox') {
                    clonedElement.checked = element.checked;
                    //copy the rest of checkboxes with the same name and their state
                    var name = element.name;
                    each(element.form.elements, function(checkbox) {
                        if (checkbox.name == name && checkbox != element) {
                            var checkboxClone = form.appendChild(checkbox.cloneNode(true));
                            append(clonedElements, checkboxClone);
                            checkboxClone.checked = checkbox.checked;
                        }
                    });
                    execute = fixExecuteParameter(execute, element);
                }
            } else if (tagName == 'select') {
                var clonedOptions = clonedElement.options;
                each(element.options, function(option, i) {
                    clonedOptions[i].selected = option.selected;
                });
            } else if (tagName == 'textarea') {
                clonedElement.value = element.value;
            }

            event = event || null;
            var onBeforeSubmitListeners = [];
            var onBeforeUpdateListeners = [];
            var onAfterUpdateListeners = [];
            var onNetworkErrorListeners = [];
            var onServerErrorListeners = [];
            if (callbacks) {
                callbacks(
                    curry(append, onBeforeSubmitListeners),
                    curry(append, onBeforeUpdateListeners),
                    curry(append, onAfterUpdateListeners),
                    curry(append, onNetworkErrorListeners),
                    curry(append, onServerErrorListeners)
                );
            }
            if (deltaSubmit(element)) {
                append(onAfterUpdateListeners, recalculateFormPreviousParameters(element, form));
            }

            var requestScopedSubmitEventBroadcaster = submitEventBroadcaster(onBeforeSubmitListeners, onBeforeUpdateListeners, onAfterUpdateListeners);
            var requestScopedSubmitErrorBroadcaster = submitErrorBroadcaster(onNetworkErrorListeners, onServerErrorListeners);
            var options = {
                execute: execute,
                render: render,
                onevent: requestScopedSubmitEventBroadcaster,
                onerror: requestScopedSubmitErrorBroadcaster,
                'ice.window': namespace.window,
                'ice.view': viewID,
                'ice.focus': currentFocus
            };
            var decoratedEvent = $event(event, element);

            if (isKeyEvent(decoratedEvent) && isEnterKey(decoratedEvent)) {
                cancelBubbling(decoratedEvent);
                cancelDefaultAction(decoratedEvent);
            }

            serializeEventToOptions(decoratedEvent, options);
            serializeAdditionalParameters(additionalParameters, options);

            debug(logger, join([
                'partial submit to ' + encodedURLOf(form),
                'javax.faces.execute: ' + execute,
                'javax.faces.render: ' + render,
                'javax.faces.source: ' + element.id,
                'view ID: ' + viewID,
                'event type: ' + type(decoratedEvent)
            ], '\n'));
            namespace.submitFunction(clonedElement, event, options);
        } catch (e) {
            debug(logger, "singleSubmit failed " + e);
        } finally {
            if (window.myfaces) {
                //myfaces queue does not serialize
                //until the request is sent, so we must delay
                append(onAfterUpdateListeners, function() {
                    each(clonedElements, function(c) {
                        form.removeChild(c);
                    });
                });
            } else {
                each(clonedElements, function(c) {
                    form.removeChild(c);
                });
            }
        }
    }

    singleSubmitExecuteThis = function(event, idorelement, additionalParameters, callbacks) {
        var element = idOrElement(idorelement);
        if (standardFormSerialization(element)) {
            return fullSubmit('@this', '@all', event, element, function(p) {
                p('ice.submit.type', 'ice.se');
                p('ice.submit.serialization', 'form');
                if (additionalParameters) additionalParameters(p);
            }, callbacks);
        } else {
            return singleSubmit('@this', '@all', event, element, function(p) {
                p('ice.submit.type', 'ice.se');
                p('ice.submit.serialization', 'element');
                if (additionalParameters) additionalParameters(p);
            }, callbacks);
        }
    };

    singleSubmitExecuteThisRenderThis = function(event, idorelement, additionalParameters, callbacks) {
        var element = idOrElement(idorelement);
        if (standardFormSerialization(element)) {
            return fullSubmit('@this', '@this', event, element, function(p) {
                p('ice.submit.type', 'ice.ser');
                p('ice.submit.serialization', 'form');
                if (additionalParameters) additionalParameters(p);
            }, callbacks);
        } else {
            return singleSubmit('@this', '@this', event, element, function(p) {
                p('ice.submit.type', 'ice.ser');
                p('ice.submit.serialization', 'element');
                if (additionalParameters) additionalParameters(p);
            }, callbacks);
        }
    };

    var addPrefix = 'patch+';
    var removePrefix = 'patch-';

    function extractTarget(e) {
        if (!e) {
            return null;
        }
        return (e.currentTarget) ? e.currentTarget :
            ( (e.target) ? e.target : e.srcElement );
    }

    function isFormElement(e) {
        var type = toLowerCase(e.nodeName);
        return (type == 'input' && (e.name != 'javax.faces.ViewState')) ||
            type == 'select' ||
            type == 'textarea';
    }

    function recalculateFormPreviousParameters(element, form) {
        return function(updates) {
            var updatedFragments = inject(updates.getElementsByTagName('update'), [], function(result, update) {
                var id = update.getAttribute('id');
                if (contains(id, 'javax.faces.ViewState') || endsWith(id, '_fixviewstate')) {
                    return result;
                } else {
                    return append(result, lookupElementById(id));
                }
            });
            var updatedForms = inject(updatedFragments, [ form ] , function(result, e) {
                if (isFormElement(e) && not(contains(result, e.form))) {
                    append(result, e.form);
                } else {
                    each(e.getElementsByTagName('form'), function(form) {
                        append(result, form);
                    });
                }

                return result;
            });

            each(updatedForms, function(form) {
                debug(logger, 'recalculate initial parameters for updated form["' + form.id + '"]');
                form.previousParameters = HashSet(jsf.getViewState(form).split('&'));
            });
        };
    }

    fullSubmit = function(execute, render, event, element, additionalParameters, callbacks) {
        var f = null;
        var extractedElement = extractTarget(event);
        var eventElement = (extractedElement) ? extractedElement :
            triggeredBy($event(event, element));
        if (eventElement && (eventElement.tagName) &&
            (toLowerCase(eventElement.tagName) == "form")) {
            eventElement = element;
        }
        if (toLowerCase(element.tagName) == "form") {
            f = element;
        } else {
            f = formOf(element);
        }
        var formID = f.id;
        //if the element has ajaxDisabled or any parent has ajaxDisabled,
        //then ajax is disabled
        var ajaxIsDisabled = false;
        var ancestor = eventElement;
        while (null != ancestor) {
            if ((ancestor.tagName) &&
                (toLowerCase(ancestor.tagName) == "form")) {
                break;
            }
            if (isAjaxDisabled(formID, ancestor)) {
                ajaxIsDisabled = true;
                break;
            }
            ancestor = ancestor.parentNode;
        }
        if (ajaxIsDisabled) {
            //use native submit function saved by namespace.captureSubmit
            if (f && f.nativeSubmit) {
                var fakeClick = document.createElement("input");
                fakeClick.setAttribute("type", "hidden");
                fakeClick.setAttribute("name", eventElement.name);
                fakeClick.setAttribute("value", eventElement.value);
                fakeClick.setAttribute("autocomplete", "off");
                f.appendChild(fakeClick);
                f.nativeSubmit();
                f.removeChild(fakeClick);
            }
        } else {
            event = event || null;

            var onBeforeSubmitListeners = [];
            var onBeforeUpdateListeners = [];
            var onAfterUpdateListeners = [];
            var onNetworkErrorListeners = [];
            var onServerErrorListeners = [];
            if (callbacks) {
                callbacks(
                    curry(append, onBeforeSubmitListeners),
                    curry(append, onBeforeUpdateListeners),
                    curry(append, onAfterUpdateListeners),
                    curry(append, onNetworkErrorListeners),
                    curry(append, onServerErrorListeners)
                );
            }
            var viewID = viewIDOf(element);
            var requestScopedSubmitEventBroadcaster = submitEventBroadcaster(onBeforeSubmitListeners, onBeforeUpdateListeners, onAfterUpdateListeners);
            var requestScopedSubmitErrorBroadcaster = submitErrorBroadcaster(onNetworkErrorListeners, onServerErrorListeners);
            var options = {
                execute: execute,
                render: render,
                onevent: requestScopedSubmitEventBroadcaster,
                onerror: requestScopedSubmitErrorBroadcaster,
                'ice.window': namespace.window,
                'ice.view': viewID,
                'ice.focus': currentFocus};

            var decoratedEvent = $event(event, element);

            if (isKeyEvent(decoratedEvent) && isEnterKey(decoratedEvent)) {
                cancelBubbling(decoratedEvent);
                cancelDefaultAction(decoratedEvent);
            }

            try {
                serializeEventToOptions(decoratedEvent, options);
            } catch (e) {
                debug(logger, "Unable to serialize event " + e);
            }
            serializeAdditionalParameters(additionalParameters, options);

            var form = formOf(element);
            var isDeltaSubmit = deltaSubmit(element);

            debug(logger, join([
                (isDeltaSubmit ? 'delta ' : '') + 'full submit to ' + encodedURLOf(form),
                'javax.faces.execute: ' + execute,
                'javax.faces.render: ' + render,
                'javax.faces.source: ' + element.id,
                'view ID: ' + viewID,
                'event type: ' + type(decoratedEvent)
            ], '\n'));

            if (isDeltaSubmit) {
                append(onAfterUpdateListeners, recalculateFormPreviousParameters(element, f));

                var previousParameters = form.previousParameters || HashSet();
                var currentParameters = HashSet(jsf.getViewState(form).split('&'));
                var addedParameters = complement(currentParameters, previousParameters);
                var removedParameters = complement(previousParameters, currentParameters);
                //form.previousParameters = currentParameters;
                function splitStringParameter(f) {
                    return function(p) {
                        var parameter = split(p, '=');
                        f(decodeURIComponent(parameter[0]), decodeURIComponent(parameter[1]));
                    };
                }

                var deltaSubmitForm = document.getElementById(singleSubmitFormID(viewID));
                var appendedElements = [];

                var clonedElement;
                if (toLowerCase(element.nodeName) == 'form') {
                    //forms cannot be nested, we create a hidden input element to replace it
                    clonedElement = document.createElement('input');
                    clonedElement.setAttribute('id', element.id);
                    clonedElement.setAttribute('name', element.id);
                    clonedElement.setAttribute('value', element.id);
                    clonedElement.setAttribute('type', 'hidden');
                    clonedElement.setAttribute('autocomplete', 'off');
                } else {
                    clonedElement = element.cloneNode(true);
                    //copy the value for textareas since some browsers (Chrome 22 and Firefox 16) are to lazy to do it
                    clonedElement.value = element.value;
                }

                append(appendedElements, deltaSubmitForm.appendChild(clonedElement));

                function createHiddenInputInDeltaSubmitForm(name, value) {
                    append(appendedElements, appendHiddenInputElement(deltaSubmitForm, name, value));
                }

                try {
                    createHiddenInputInDeltaSubmitForm('ice.deltasubmit.form', form.id);
                    createHiddenInputInDeltaSubmitForm(form.id, form.id);
                    each(addedParameters, splitStringParameter(function(name, value) {
                        createHiddenInputInDeltaSubmitForm(addPrefix + name, value);
                    }));
                    each(removedParameters, splitStringParameter(function(name, value) {
                        createHiddenInputInDeltaSubmitForm(removePrefix + name, value);
                    }));

                    namespace.submitFunction(clonedElement, event, options);
                } finally {
                    each(appendedElements, function(element) {
                        deltaSubmitForm.removeChild(element);
                    });
                }
            } else {
                namespace.submitFunction(element, event, options);
            }
        }
    };

    submit = function(event, element, additionalParameters, callbacks) {
        return fullSubmit('@all', '@all', event, idOrElement(element), function(p) {
            p('ice.submit.type', 'ice.s');
            p('ice.submit.serialization', 'form');
            if (additionalParameters) additionalParameters(p);
        }, callbacks);
    };
})();
