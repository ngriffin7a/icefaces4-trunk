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
    eval(ice.importFrom('ice.lib.functional'));
    eval(ice.importFrom('ice.lib.oo'));
    eval(ice.importFrom('ice.lib.window'));

    function now() {
        return (new Date()).getTime();
    }

    function updateTimestamp() {
        var state = window.history.state;
        if (!state) {
            state = {};
        }
        state.timestamp = now();
        window.history.replaceState(state, document.title, document.location.href);
    }

    var resourceLoadTimestamp = now();
    var setupInvoked = false;
    ice.setupNavigationNotifier = function(id) {
        //skip setup if already invoked or HTML5 features are not present
        if (setupInvoked || !window.history.replaceState) return;

        if (window.history.state && window.history.state.timestamp) {
            var timestamp = Number(window.history.state.timestamp);
            console.info('delta timestamp = ' + (resourceLoadTimestamp - timestamp));
            if (timestamp + 500 > resourceLoadTimestamp) {
                //page reloaded
            } else {
                //back/forward detected
                ice.s(null, id);
            }
            //update timestamp right before unloading page
            onUnload(window, updateTimestamp);
            updateTimestamp();
        } else {
            //fresh load
            updateTimestamp();
        }

        setupInvoked = true;
    };
})();
