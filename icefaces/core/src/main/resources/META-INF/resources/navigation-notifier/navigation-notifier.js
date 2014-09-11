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

    var PreviousPageUnloadTime = 'previous-unload-timestamp';
    var PreviousPageTimestamp = 'previous-timestamp';
    var PreviousPageURL = 'previous-url';

    function now() {
        return (new Date()).getTime();
    }

    function timestampHistoryPosition() {
        window.history.replaceState(now(), null, null);
    }

    var pageLoadTime = now();
    var setupInvoked = false;
    ice.setupNavigationNotifier = function(id) {
        //skip setup if already invoked or HTML5 features are not present
        if (setupInvoked || !window.history.replaceState) return;

        if (window.history.state) {
            var pageTimestamp = Number(window.history.state);

            var previousPageTimestamp = Number(window.localStorage[PreviousPageTimestamp]);
            var previousPageUnloadTime = Number(window.localStorage[PreviousPageUnloadTime]);
            var previousURL = window.localStorage[PreviousPageURL];

            var isReload =
                (previousURL == document.location.href) && (previousPageTimestamp == pageTimestamp) && (pageLoadTime - previousPageUnloadTime < 700);
            if (!isReload) {
                //back/forward detected
                ice.s(null, id);
            }
        } else {
            //fresh load
            timestampHistoryPosition();
        }

        //update timestamp right before unloading page
        onUnload(window, function() {
            window.localStorage[PreviousPageUnloadTime] = String(now());
            window.localStorage[PreviousPageTimestamp] = window.history.state;
            window.localStorage[PreviousPageURL] = document.location.href;
        });

        setupInvoked = true;
    };
})();
