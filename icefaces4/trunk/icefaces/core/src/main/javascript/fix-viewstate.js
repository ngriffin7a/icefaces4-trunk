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
    function isComponentRendered(form) {
        return lookupNamedInputElement(form, 'javax.faces.encodedURL') ||
            lookupNamedInputElement(form, 'javax.faces.ViewState') ||
            lookupNamedInputElement(form, 'ice.window') ||
            lookupNamedInputElement(form, 'ice.view') ||
            (form.id && form[form.id] && form.id == form[form.id].value);
    }

    function ifViewStateUpdated(updates, callback) {
        var viewStateUpdate = detect(updates.getElementsByTagName('update'), function(update) {
            return contains(update.getAttribute('id'), 'javax.faces.ViewState');
        });

        if (viewStateUpdate) {
            callback(viewStateUpdate.firstChild.data);
        }
    }

    function collectUpdatedForms(updates, iterator) {
        each(updates.getElementsByTagName('update'), function(update) {
            var id = update.getAttribute('id');
            var e = lookupElementById(id);
            if (e) {
                if (toLowerCase(e.nodeName) == 'form') {
                    if (isComponentRendered(e)) {
                        iterator(e);//the form is the updated element
                    }
                } else {
                    //find all the forms in the update's markup, just in case the executed javascript moved forms
                    //around after the update was applied
                    var markup = join(collect(update.childNodes, function(cdata) {
                        return cdata.data;
                    }), '');
                    var formStartTags = markup.match(/\<form[^\<]*\>/g);
                    if (formStartTags) {
                        each(formStartTags, function(formStartTag) {
                            //find 'id' attribute in the form start tag
                            var match = formStartTag.match(/id="([\S]*?)"/im);
                            if (match && match[1]) {
                                var id = match[1];
                                var form = document.getElementById(id);
                                if (form && isComponentRendered(form)) {
                                    iterator(form);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //fix JSF issue: http://jira.icefaces.org/browse/ICE-5691
    namespace.onAfterUpdate(function(updates) {
        ifViewStateUpdated(updates, function(viewState) {
            collectUpdatedForms(updates, function(form) {
                //add hidden input field to the updated forms that don't have it
                try {
                    var viewStateElement = lookupViewStateElement(form);
                    if (viewStateElement.value != viewState) {
                        viewStateElement.value = viewState;
                    }
                } catch (ex) {
                    appendHiddenInputElement(form, 'javax.faces.ViewState', viewState, viewState);
                    debug(logger, 'append missing "javax.faces.ViewState" input element to form["' + form.id + '"]');
                }
            });
        });
    });

    //MyFaces uses a linked list of view state keys to track the changes in the view state -- the participating
    //forms need to have their view state key updated so that the next submit will work with the latest saved state
    //ICE-7188
    var formViewID;
    namespace.onBeforeSubmit(function(source) {
        formViewID = lookupNamedInputElement(formOf(source), 'ice.view').value;
    });
    namespace.onAfterUpdate(function(updates) {
        ifViewStateUpdated(updates, function(viewState) {
            //update only the forms that have the same viewID with the one used by the submitting form
            each(document.getElementsByTagName('form'), function(form) {
                var viewIDElement = lookupNamedInputElement(form, 'ice.view');
                var viewStateElement = lookupNamedInputElement(form, 'javax.faces.ViewState');
                if (viewStateElement && viewIDElement && viewIDElement.value == formViewID) {
                    viewStateElement.value = viewState;
                }
            });
        });
    });

    function fixViewState(id, viewState) {
        var form = lookupElementById(id);
        try {
            var viewStateElement = lookupViewStateElement(form);
            if (viewStateElement.value != viewState) {
                viewStateElement.value = viewState;
            }
        } catch (ex) {
            appendHiddenInputElement(form, 'javax.faces.ViewState', viewState, viewState);
        }
    }

    namespace.fixViewStates = function(formIds, viewState) {
        for( var i = 0; i < formIds.length; i++){
            fixViewState(formIds[i],viewState);
        }
    };
})();
