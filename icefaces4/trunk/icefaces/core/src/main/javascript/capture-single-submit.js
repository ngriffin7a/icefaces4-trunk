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
    namespace.enableSingleSubmit = function(id, useBlur) {
        var f = document.getElementById(id);
        f.singleSubmit = 'enabled';
        function submitForm(ev) {
            //need to investigate why $event did not provide type
            var eType;
            if (window.event) {
                eType = window.event.type;
            } else {
                eType = ev.type;
            }
            if (0 == eType.indexOf("on")) {
                //strip "on" from front of event name
                eType = eType.substr(2);
            }
            var e = $event(ev, f);
            var element = triggeredBy(e);

            //traverse ancestors to check if single submit is disabled at element's position in DOM
            var cursor = element;
            while (cursor && !cursor.singleSubmit) {
                cursor = cursor.parentNode;
            }
            if (cursor.singleSubmit == 'disabled') {
                //single submit is disabled
                return;
            }

            var elementType = element.type;
            if (!elementType) {
                return;
            }
            elementType = toLowerCase(elementType);

            if (elementType == 'image') {
                return;
            }
            if (elementType == 'submit') {
                return;
            }
            if ((null == element.id) || ("" == element.id)) {
                return;
            }

            var isText = ( (elementType == "text") ||
                (elementType == "password") ||
                (elementType == "textarea") );
            if (isText) {

                //click events should not trigger text box submit
                //blur events are mostly redundant with change events
                if ((eType == "click") || !useBlur && eType == "blur") {
                    return;
                }

                //focusout is required for older IE versions
                //only check for the value changing if we are not relying on blur
                if (!useBlur && eType == "focusout") {

                    if (element.value == element.previousTextValue) {
                        return;
                    }

                    element.previousTextValue = element.value;
                }
            }

            //Single selects (handled differently than multiple selects)
            if (elementType == ("select-one")) {

                // Most browsers provide a change event which should
                // be enough to trigger submission. If not...
                if (eType != 'change') {

                    //Pre IE9 sends a couple of click events when pulling down the list.  We need to
                    //ignore the first one.
                    if (eType == 'click' && element.selectedIndex <= 0 && !element.previouslySelected) {

                        //debug(logger, 'ignore first IE click');
                        element.previouslySelected = element.selectedIndex;
                        return;
                    }

                    //Determine if the actual selection has changed.
                    if (element.selectedIndex == element.previouslySelected) {
                        return;
                    }

                    element.previouslySelected = element.selectedIndex;
                }
            }

            //Multiple selects (handled differently than single selects)
            if (elementType == ("select-multiple")) {

                // Most browsers provide a change event which should
                // be enough to trigger submission. If not...
                if (eType != 'change') {

                    //Most browsers provide direct access to the element.selectedOptions but
                    //for pre IE9 browsers, we need to iterate through and see how many are
                    //selected.
                    var numberOfSelected = 0;
                    for (var i = 0; i < element.options.length; i++) {
                        if (element.options[i].selected) {
                            numberOfSelected++;
                        }
                    }

                    //If the number of items selected is the same and the index of the selected item is the same
                    //then there is no change and no submission required.
                    if (numberOfSelected == element.previousNumberOfSelected &&
                        element.selectedIndex == element.previouslySelected) {
                        return;
                    }

                    //Record the index or number of options for future comparisons.
                    element.previouslySelected = element.selectedIndex;
                    element.previousNumberOfSelected = numberOfSelected;
                }
            }

            ice.setFocus(null);
            ice.se(e, element);
        }

        if (f.addEventListener) {
            //events for most browsers
            //use the event capture listener rather than the bubble listener if submitOnBlur=true
            //or the blur events will never arrive (default is false)
            f.addEventListener('blur', submitForm, useBlur);
            f.addEventListener('change', submitForm, false);
        } else {
            //events for IE
            f.attachEvent('onfocusout', submitForm);
            f.attachEvent('onclick', submitForm);
        }
    };

    namespace.cancelSingleSubmit = function(id) {
        if (typeof id != "string") return;
        var f = document.getElementById(id);
        if (!f) return;

        var cancelBubble = function (event) {
            event = event || window.event;
            if (event.stopPropagation) event.stopPropagation();
            event.cancelBubble = true;
        };

        if (f.addEventListener) {
            f.addEventListener('blur', cancelBubble, false);
            f.addEventListener('change', cancelBubble, false);
        } else {
            f.attachEvent('onfocusout', cancelBubble);
            f.attachEvent('onclick', cancelBubble);
        }
    };

    namespace.disableSingleSubmit = function(id) {
        var element = lookupElementById(id);
        element.singleSubmit = 'disabled';
    };
})();