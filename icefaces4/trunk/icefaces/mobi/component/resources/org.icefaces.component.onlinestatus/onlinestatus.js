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

ice.mobi.onlineStatus = function(cfg) {
    var element = document.getElementById(cfg.id);
    var className = element.className;
    element.className = className ? (className + ' ' + cfg.onlineClass) : cfg.onlineClass;

    var removeOnlineCallback = ice.onOnline(function() {
        element.className = element.className.replace(cfg.offlineClass, cfg.onlineClass);
        cfg.onlineCallback(element);
    });
    var removeOfflineCallback = ice.onOffline(function() {
        element.className = element.className.replace(cfg.onlineClass, cfg.offlineClass);
        cfg.offlineCallback(element);
    });

    ice.onElementUpdate(cfg.id, function() {
        removeOfflineCallback();
        removeOnlineCallback();
    });
};