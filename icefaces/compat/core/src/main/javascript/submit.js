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

var iceSubmitPartial;
var iceSubmit;
var formOf;

(function() {
    iceSubmitPartial = function(form, component, evt) {
        form = form || formOf(component);
        ice.submit(evt, component || form, function(parameter) {
            if (Ice.Menu != null && Ice.Menu.menuContext != null) {
                parameter('ice.menuContext', Ice.Menu.menuContext);
            }
            parameter('ice.submit.partial', true);
        });

        return false;
    };

    iceSubmit = function(form, component, evt) {
        form = form || formOf(component);
        var code;
        if (evt.keyCode) code = evt.keyCode;
        else if (evt.which) code = evt.which;
        if (code > 3) {
            if (code != 13) {
                return false;
            }
        }
        ice.submit(evt, component || form, function(parameter) {
            if (Ice.Menu != null && Ice.Menu.menuContext != null) {
                parameter('ice.menuContext', Ice.Menu.menuContext);
            }
        });

        return false;
    };

    formOf = function(element) {
        var parent = element.parentNode;
        while (parent) {
            if (parent.tagName && parent.tagName.toLowerCase() == 'form') return parent;
            parent = parent.parentNode;
        }

        throw 'Cannot find enclosing form.';
    };
})();
