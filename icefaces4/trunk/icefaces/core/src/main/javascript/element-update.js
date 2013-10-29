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

var switchToClientSideElementUpdateDetermination;

(function() {
    var elementUpdateListeners = [];
    namespace.onElementUpdate = function(id, callback) {
        var element = lookupElementById(id);
        if (element) {
            element['data-onElementUpdate'] = callback;
        } else {
            warn(logger, 'Cannot find element [' + id + '] to assign onElementUpdate callback.');
        }
    };

    function clientSideOnElementUpdate(id, callback) {
        var element = lookupElementById(id);
        var ancestorIDs = [];
        var cursor = element;
        while (cursor) {
            var cursorID;
            if (cursor == document.body) {
                cursorID = 'javax.faces.ViewBody';
            } else if (cursor == document.documentElement) {
                cursorID = 'javax.faces.ViewRoot';
            } else if (cursor == document.getElementsByTagName('head')[0]) {
                cursorID = 'javax.faces.ViewHead';
            } else {
                cursorID = cursor.id;
            }

            if (cursorID) {
                ancestorIDs.push(cursorID);
            }
            cursor = cursor.parentNode;
        }
        append(elementUpdateListeners, {identifier: id, handler: callback, ancestors: ('*' + ancestorIDs.join('*') + '*')});
        return removeCallbackCallback(elementUpdateListeners, function(c) {
            return id == c.id;
        });
    }

    switchToClientSideElementUpdateDetermination = function() {
        namespace.onElementUpdate = clientSideOnElementUpdate;
        // determine which elements are about to be removed by an update,
        // and clean them up while they're still in place
        namespace.onBeforeUpdate(function(updates) {
            each(updates.getElementsByTagName('update'), findAndNotifyUpdatedElements);
            each(updates.getElementsByTagName('delete'), findAndNotifyUpdatedElements);
        });
    };

    function findAndNotifyUpdatedElements(update) {
        var updatedElementId = update.getAttribute('id');
        if (contains(updatedElementId, 'javax.faces.ViewState')) {
            return;
        }
        var fvsTail = updatedElementId.substr(updatedElementId.length - 13);
        if ('_fixviewstate' === fvsTail) {
            return;
        }
        var updatedElement = lookupElementById(updatedElementId);
        if (updatedElement) {
            elementUpdateListeners = reject(elementUpdateListeners, function(idCallbackTuple) {
                var updated = contains(idCallbackTuple.ancestors, '*' + updatedElementId + '*');
                if (updated) {
                    var id = idCallbackTuple.identifier;
                    var element = lookupElementById(id);
                    //test if inner element still exists, sometimes client side code can remove DOM fragments
                    if (element) {
                        var callback = idCallbackTuple.handler;
                        try {
                            callback(element);
                        } catch (e) {
                            //ignore exception thrown in callback
                            //to make sure that the corresponding entry is removed from the list
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            });
        }
    }

    namespace.notifyOnElementUpdateCallbacks = function(ids) {
        each(ids, function(id) {
            var e = lookupElementById(id);
            if (e) {
                var callback = e['data-onElementUpdate'];
                if (callback) {
                    try {
                        callback(id);
                    } catch (ex) {
                        warn(logger, 'onElementUpdate callback for [' + id + '] failed to run properly', ex);
                    } finally {
                        e['data-onElementUpdate'] = null;
                    }
                }
            }
        });
    };

    namespace.notifyAllOnElementUpdateCallbacks = function() {
        var elements = document.body.getElementsByTagName('*');
        //use for loop for speed
        for (var i = 0, l = elements.length; i < l; i++) {
            var e = elements[i];
            var callback = e['data-onElementUpdate'];
            if (callback) {
                var id = e.id;
                try {
                    callback(id);
                } catch (ex) {
                    warn(logger, 'onElementUpdate callback for [' + id + '] failed to run properly', ex);
                } finally {
                    e['data-onElementUpdate'] = null;
                }
            }
        }
    };
})();