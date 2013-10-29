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
    namespace.captureKeypress = function(id, keyMap) {
        var f = document.getElementById(id);
        f.onkeypress = function(ev) {
            var e = $event(ev, f);
            var element = triggeredBy(e);
            var type = toLowerCase(element.nodeName);
            if (type != 'textarea' && type != 'a') {
                //capture any key when default submit element is defined
                if (keyMap) {
                    var elementID = keyMap[keyCode(e)];
                    if (elementID) {
                        element = lookupElementById(elementID);
                        submit(ev || window.event, element);
                        return false;
                    }
                }
                if (isEnterKey(e)) {
                    //traverse ancestors to check if submit on enter is disabled at element's position in DOM
                    var cursor = element;
                    while (cursor && !cursor.submitOnEnter) {
                        cursor = cursor.parentNode;
                    }
                    if (cursor && cursor.submitOnEnter == 'disabled') {
                        //cancel submit on enter
                        return false;
                    } else {
                        submit(ev || window.event, element);
                        return false;
                    }
                }
                return true;
            }
        };
    };

    namespace.captureSubmit = function(id) {
        var f = document.getElementById(id);
        //hijack browser form submit, instead submit through an Ajax request
        f.nativeSubmit = f.submit;
        f.submit = function() {
            var theEvent = null;
            if (typeof(event) != 'undefined') {
                theEvent = event;
            } else if (window.event) {
                theEvent = window.event;
            } else {
                //very bizarre hack to extract parameters from high up
                //on the call stack to obtain the current Event on Firefox
                //this is sensitive to the depth of the call stack so
                //it may eventually be necessary to walk up rather than
                //test a specific caller
                //a
                var maybeCaller = null;
                maybeCaller = arguments.callee.caller.caller;
                if (null == maybeCaller) {
                    maybeCaller = arguments.callee.caller;
                }
                var maybeEvent = maybeCaller.arguments[0];
                if (typeof(maybeEvent.target) != 'undefined') {
                    theEvent = maybeEvent;
                }
            }
            submit(theEvent, f);
        };
        each(['onkeydown', 'onkeypress', 'onkeyup', 'onclick', 'ondblclick', 'onchange'], function(name) {
            f[name] = function(e) {
                var event = e || window.event;
                var element = event.target || event.srcElement;
                f.onsubmit = function() {
                    //fallback to using form as submitting element when the element was removed by a previous
                    //update and form.onsubmit callback is called directly (by application or third party library code)
                    if (element.name && !element.id) {
                        //verify that the id is not already in use
                        //second check is for IE9 which will lookup by name also when getElementById is invoked
                        var lookedUpElement = document.getElementById(element.name);
                        if (!lookedUpElement || !lookedUpElement.id) {
                            element.id = element.name;
                        }
                    }
                    var elementExists = document.getElementById(element.id);
                    submit(event, elementExists ? element : f);
                    f.onsubmit = null;
                    return false;
                };
            };
        });
    };
})();
