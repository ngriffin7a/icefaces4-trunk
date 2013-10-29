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

var clearEventHandlersOnUnload;

(function() {
    function clearEventHandlers(element) {
        element.onkeypress = null;
        element.onmousedown = null;
        element.onmousemove = null;
        element.onmouseout = null;
        element.onmouseover = null;
        element.onclick = null;
        element.oncontextmenu = null;
        element.onchange = null;
        element.onfocus = null;
        element.onblur = null;
        element.submit = null;
        element.onsubmit = null;
        element.onkeydown = null;
        element.onkeyup = null;
        element.ondblclick = null;
        element.onfocusout = null;
    }

    clearEventHandlersOnUnload = function(container) {
        //clear the event handlers on the elements that will most likely create a memory leak
        onUnload(window, function() {
            container.configuration = null;

            each(['a', 'iframe'], function(type) {
                each(container.getElementsByTagName(type), clearEventHandlers);
            });

            each(container.getElementsByTagName('form'), function(form) {
                try {
                    form.submit = null;
                } catch (ex) {
                    //ignore exception thrown by IE, let the rest of cleanup proceed
                }
                form.onsubmit = null;
                var elements = form.elements;
                //IE returns 'undefined' when no from elements exist
                if (elements) {
                    each(elements, clearEventHandlers);
                }
            });
        });
    };

    //fix potential memory leaks by clearing up the event handlers that replaced elements have had
    namespace.onBeforeUpdate(function(updates) {
        each(updates.getElementsByTagName('update'), function(update) {
            var id = update.getAttribute('id');
            var e = lookupElementById(id);
            if (e) {
                clearEventHandlers(e);

                each(['a', 'iframe', 'input', 'select', 'button', 'textarea'], function(type) {
                    each(e.getElementsByTagName(type), clearEventHandlers);
                });

                each(e.getElementsByTagName('form'), function(form) {
                    clearEventHandlers(form);
                    form.submit = null;
                    form.onsubmit = null;
                });
            }
        });
    });
})();