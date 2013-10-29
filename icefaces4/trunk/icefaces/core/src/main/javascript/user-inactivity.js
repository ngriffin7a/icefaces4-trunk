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
    var userInactivityListeners = [];
    var notifiedUserInactivityListeners = [];
    var isUserInactivityMonitorStarted = false;

    namespace.onUserInactivity = function(timeout, idleUserCallback, activeUserCallback) {
        if (!isUserInactivityMonitorStarted) {
            isUserInactivityMonitorStarted = true;
            observeUserInactivity();
        }
        var tuple = {interval: (timeout * 1000), idleCallback: idleUserCallback, activeCallback: activeUserCallback};
        append(userInactivityListeners, tuple);
        return removeCallbackCallback(userInactivityListeners, detectByReference(tuple));
    };

    function observeUserInactivity() {
        var userActivityMonitor = Delay(function() {
            var now = (new Date).getTime();
            var additionalNotifiedListeners = select(userInactivityListeners, function(tuple) {
                var interval = tuple.interval;
                var runCallback = now > lastActivityTime + interval;
                if (runCallback) {
                    var callback = tuple.idleCallback;
                    try {
                        callback();
                    } catch (ex) {
                        warn(logger, 'onUserInactivity idle user callback failed to run', ex);
                    }
                }

                return runCallback;
            });
            userInactivityListeners = complement(userInactivityListeners, additionalNotifiedListeners);
            notifiedUserInactivityListeners = concatenate(notifiedUserInactivityListeners, additionalNotifiedListeners);
        }, 3 * 1000);//poll every 3 seconds
        run(userActivityMonitor);

        var stopActivityMonitor = curry(stop, userActivityMonitor);
        namespace.onSessionExpiry(stopActivityMonitor);
        namespace.onNetworkError(stopActivityMonitor);
        namespace.onServerError(stopActivityMonitor);
        namespace.onUnload(stopActivityMonitor);

        var lastActivityTime = (new Date).getTime();
        function resetUserInactivity() {
            lastActivityTime = (new Date).getTime();
            if (notEmpty(notifiedUserInactivityListeners)) {
                each(notifiedUserInactivityListeners, function(tuple) {
                    var callback = tuple.activeCallback;
                    if (callback) {
                        try {
                            callback();
                        } catch (ex) {
                            warn(logger, 'onUserInactivity active user callback failed to run', ex);
                        }
                    }
                });
                userInactivityListeners = concatenate(userInactivityListeners, notifiedUserInactivityListeners);
                notifiedUserInactivityListeners = [];
            }
        }
        registerListener('keydown', document, resetUserInactivity);
        registerListener('mouseover', document, resetUserInactivity);
    }
})();