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

(function () {
    var userAgent = navigator.userAgent.toLowerCase();

    function userAgentContains(s) {
        return userAgent.indexOf(s) >= 0;
    }

    ice.browser = {
        isIE: function () {
            return userAgentContains('msie');
        },
        isEdge: function () {
            return userAgentContains('trident');
        },
        isSafari: function () {
            return userAgentContains('safari') && !userAgentContains('chrome') && !userAgentContains('mobile safari');
        },
        isFirefox: function () {
            return userAgentContains('firefox');
        },
        isChrome: function () {
            return userAgentContains('chrome');
        },
        majorVersion: function () {
            try {
                if (ice.browser.isFirefox()) {
                    return Number(navigator.userAgent.match(/Firefox[\/\s](\d+)\.\d+/)[1]);
                } else if (ice.browser.isSafari()) {
                    return Number(navigator.userAgent.match(/Version\/\s*(\d+)\.\d+\.\d+ Safari/)[1]);
                } else if (ice.browser.isChrome()) {
                    return Number(navigator.userAgent.match(/Chrome\/(\d+)\.\d+/)[1]);
                } else if (ice.browser.isIE()) {
                    return Number(navigator.userAgent.match(/MSIE (\d+)\.\d+;/)[1]);
                } else if (ice.browser.isEdge()) {
                    return Number(navigator.userAgent.match(/Trident.*rv[ :]*(\d+)\.\d+/)[1]);
                } else {
                    return 0;
                }
            } catch (ex) {
                return 0;
            }
        },
        minorVersion: function () {
            try {
                if (ice.browser.isFirefox()) {
                    return Number(navigator.userAgent.match(/Firefox[\/\s]\d+\.(\d+)/)[1]);
                } else if (ice.browser.isSafari()) {
                    return Number(navigator.userAgent.match(/Version\/\s*\d+\.(\d+)\.*Safari/)[1]);
                } else if (ice.browser.isChrome()) {
                    return Number(navigator.userAgent.match(/Chrome\/\d+\.(\d+)/)[1]);
                } else if (ice.browser.isIE()) {
                    return Number(navigator.userAgent.match(/MSIE \d+\.(\d+);/)[1]);
                } else if (ice.browser.isEdge()) {
                    return Number(navigator.userAgent.match(/Trident.*rv[ :]*\d+\.(\d+)/)[1]);
                } else {
                    return 0;
                }
            } catch (ex) {
                return 0;
            }
        },

        os: {
            isWindows: function () {
                return userAgentContains('windows')
            },
            isOSX: function () {
                return userAgentContains('macintosh')
            },
            isLinux: function () {
                return userAgentContains('linux')
            },
            isAndroid: function () {
                return userAgentContains('android')
            },
            isiOS: function () {
                return userAgentContains('ipod') || userAgentContains('ipad') || userAgentContains('iphone');
            }
        },

        localStorageAvailable: function () {
            var workingLocalStorage = false;
            if (window.localStorage) {
                var key = 'testLocalStorage';
                var value = String(Math.random());
                try {
                    window.localStorage[key] = value;
                    workingLocalStorage = window.localStorage[key] == value;
                } finally {
                    window.localStorage.removeItem(key);
                }
            }
            return workingLocalStorage;
        },
        cookiesEnabled: function () {
            return navigator.cookieEnabled;
        },
        isOnline: function () {
            return navigator.onLine;
        }
    }
})();
