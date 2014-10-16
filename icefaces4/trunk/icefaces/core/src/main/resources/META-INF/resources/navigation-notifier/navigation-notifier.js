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

    var PreviousPageUnloadTime = 'ice.previous-unload-timestamp';
    var PreviousPageTimestamp = 'ice.previous-timestamp';
    var PreviousPageURL = 'ice.previous-url';
    var TimestampKey = 'ice.timestamp'

    function now() {
        return (new Date()).getTime();
    }

    function clone(o) {
        var c = 'function' === typeof o.pop ? [] : {};
        for (var name in o) {
            if(o.hasOwnProperty(name)) {
                c[name] = o[name];
            }
        }

        return c;
    }

    function timestampHistoryPosition() {
        var state = window.history.state ? clone(window.history.state) : {};
        state[TimestampKey] = now();
        window.history.replaceState(state, null, location.href);
    }

    var pageLoadTime = now();
    var setupInvoked = false;
    ice.setupNavigationNotifier = function(id) {
        //skip setup if already invoked or HTML5 features are not present
        if (setupInvoked || !window.history.replaceState) return;

        if (window.history.state && window.history.state[TimestampKey]) {
            var pageTimestamp = Number(window.history.state[TimestampKey]);

            var previousPageTimestamp = Number(window.localStorage[PreviousPageTimestamp]);
            var previousPageUnloadTime = Number(window.localStorage[PreviousPageUnloadTime]);
            var previousURL = window.localStorage[PreviousPageURL];

            var isReload =
                (previousURL == document.location.href) && (previousPageTimestamp == pageTimestamp) && (pageLoadTime - previousPageUnloadTime < 700);
            if (!isReload) {
                //back/forward detected
                onLoad(window, function() {
                    ice.s(null, id);
                });
            }
        } else {
            //fresh load
            timestampHistoryPosition();
        }

        //update timestamp right before unloading page
        onUnload(window, function() {
            window.localStorage[PreviousPageUnloadTime] = String(now());
            window.localStorage[PreviousPageTimestamp] = window.history.state[TimestampKey];
            window.localStorage[PreviousPageURL] = document.location.href;
        });

        setupInvoked = true;
    };
})();
