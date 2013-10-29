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
    var beforeSessionExpiryListeners = [];

    namespace.onBeforeSessionExpiry = function(callback) {
        append(beforeSessionExpiryListeners, callback);
        return removeCallbackCallback(beforeSessionExpiryListeners, detectByReference(callback));
    };

    var beforeSessionExpiryTimeoutBomb = object(function(method) {
        method(stop, noop);
    });
    var sessionExpiryTimeoutBomb = object(function(method) {
        method(stop, noop);
    });

    namespace.resetSessionExpiryTimeout = function(deltaTime, timeLeft) {
        stop(beforeSessionExpiryTimeoutBomb);
        stop(sessionExpiryTimeoutBomb);
        var timeInSeconds = Math.round(timeLeft / 1000);
        beforeSessionExpiryTimeoutBomb = runOnce(Delay(function() {
            broadcast(beforeSessionExpiryListeners, [timeInSeconds]);

            runOnce(Delay(function() {
                sessionExpiryTimeoutBomb = broadcast(sessionExpiryListeners);
            }, timeLeft));
        }, deltaTime));
    };
})();