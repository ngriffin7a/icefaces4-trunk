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

var setFocus;
var applyFocus;
var currentFocus = '';
var monitorFocusChanges;
var restoreMonitorFocusChangesOnUpdate;

(function () {
    setFocus = function (id) {
        currentFocus = id ? id : '';
        debug(logger, 'persisted focus for element "' + id + '"');
    };

    function setCaretTo(element, pos) {
        if (element.createTextRange) {
            var range = element.createTextRange();
            range.move("character", pos);
            range.select();
        } else if (element.selectionStart || element.selectionStart == 0) {
            element.setSelectionRange(pos, pos);
        }
    }

    function setCaretAtTextEnd(element) {
        if ((toLowerCase(element.nodeName) == 'input' && element.type == 'text') || toLowerCase(element.nodeName) == 'textarea') {
            var text = element.value;
            setCaretTo(element, text.length);
        }
    }

    function isValidID(id) {
        return /^\w[\w\-\:]*$/.test(id);
    }

    var isIE = /MSIE/.test(navigator.userAgent);

    var focusOn = function (id) {
        runOnce(Delay(function () {
            if (id && isValidID(id)) {
                var e = document.getElementById(id);
                if (e && (e != document.activeElement)) {
                    setFocus(id);
                    if (e.focus) {
                        try {
                            var x = window.scrollX;
                            var y = window.scrollY;
                            e.focus();
                            //reset scroll position, Firefox will otherwise scroll to the focused element
                            window.scrollTo(x, y);
                        } catch (ex) {
                            //IE throws exception if element is invisible
                        } finally {
                            if (isIE) {
                                //IE sometimes requires that focus() be called again
                                try {
                                    e.focus();
                                } catch (ex2) {
                                    //IE throws exception if element is invisible
                                }
                            }
                            debug(logger, 'focused element "' + id + '"');
                            setCaretAtTextEnd(e);
                        }
                    }
                }
            }
        }, 100));
    };

    var focusStrategy = focusOn;
    //indirect reference to the currently used focus strategy
    applyFocus = function (id) {
        focusStrategy(id);
    };

    if (isIE) {
        var activeElement;
        //initialize activeElement if IE
        onLoad(window, function () {
            activeElement = document.activeElement;
        });

        //window.onblur in IE is triggered also when moving focus from window to an element inside the same window
        //to avoid bogus 'blur' events in IE the window.onblur behavior is simulated with the help of document.onfocusout
        //event handler
        var onBlur = function (callback) {
            registerElementListener(document, 'onfocusout', function () {
                if (activeElement == document.activeElement) {
                    callback();
                } else {
                    activeElement = document.activeElement;
                }
            });
        };

        var onFocus = function (callback) {
            registerElementListener(window, 'onfocus', callback);
        };

        //on window blur the ID of the focused element is just saved, not applied
        onBlur(function () {
            focusStrategy = setFocus;
        });

        onFocus(function () {
            focusStrategy = focusOn;
        });
    }

    function registerElementListener(element, eventType, listener) {
        var previousListener = element[eventType];
        if (previousListener) {
            element[eventType] = function (e) {
                var args = [e];
                //execute listeners so that 'this' variable points to the current element
                previousListener.apply(element, args);
                listener.apply(element, args);
            };
        } else {
            element[eventType] = listener;
        }
    }

    function saveCurrentFocus(e) {
        var evt = e || window.event;
        var element = evt.srcElement || evt.target;
        setFocus(element.id);
    }

    monitorFocusChanges = function(element) {
        if (element.attachEvent) {
            element.attachEvent('onfocusin', saveCurrentFocus);
        } else {
            element.addEventListener('focus', saveCurrentFocus, true);
            element.addEventListener('click', saveCurrentFocus, true);
        }
    };

    restoreMonitorFocusChangesOnUpdate = function(element) {
        var id = element.id;
        namespace.onAfterUpdate(function(updates) {
            if (detect(updates, function(update) {
                return update.getAttribute('id') == id;
            })) {
                //resume focus monitoring for the element that was updated
                monitorFocusChanges(lookupElementById(id));
            }
        });
    };
})();
