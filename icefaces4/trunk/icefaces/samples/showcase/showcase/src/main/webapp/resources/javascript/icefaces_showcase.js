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

function clone(o) {
    var c = 'function' === typeof o.pop ? [] : {};
    for (var name in o) {
        if(o.hasOwnProperty(name)) {
            c[name] = o[name];
        }
    }

    return c;
}

function restoreURL(e) {
    if (e.state && e.state.src) {
        location.href = e.state.src;
    }
}

function backButton() {
    if( window.history && window.history.pushState ){
        if (window.addEventListener) {
            window.addEventListener("popstate", restoreURL);
        } else {
            window.attachEvent("popstate", restoreURL);
        }
    }
}

if (window.addEventListener) {
    window.addEventListener('load', backButton, true);
} else {
    window.attachEvent('onload', backButton);
}

function updateAddressBarURL(grp, exp){
    var hypertextReference = '//' + location.host + location.pathname + '?grp=' + grp + '&exp=' + exp;
    if( window.history && window.history.pushState ){ //check if browser supports HTML5 history pushState
        var state;
        try {
            state = window.history.state ? clone(window.history.state) : {};
        } catch (ex) {
            state = {};
        }
        state.src = hypertextReference;
        history.pushState(state, null, hypertextReference); //set the current url, pass in a null state object
    }
}