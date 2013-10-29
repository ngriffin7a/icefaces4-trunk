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

if (!window.console) {
    console = {};
    console.log = function() {
    };
}

var jsfErrorCallback = function handleError(error) {

    console.log("JSF error callback:");

    var type = error.type;
    console.log("type: " + type);

    var status = error.status;
    console.log("status: " + status);

    var description = error.description;
    console.log("description: " + description);

    var source = error.source;
    console.log("source: " + source);

    var errorName = error.errorName;
    console.log("error name: " + errorName);

    var errorMessage = error.errorMessage;
    console.log("error message: " + errorMessage);

    var responseCode = error.responseCode;
    console.log("response code: " + responseCode);

    var responseText = error.responseText;
    console.log("response text: " + responseText);

    var responseXML = error.responseXML;
    console.log("response xml: " + responseXML);

    //window.location.href = "./viewExpired.xhtml";

}

var iceErrorCallback = function iceHandleError(statusCode, responseTxt, responseDOM) {
    console.log("ICEfaces error callback:");
    console.log("response code: " + statusCode);
    console.log("response text: " + responseTxt);
    console.log("response xml: " + responseDOM);

    if (responseTxt.indexOf("ViewExpiredException") >= 0) {
        window.location.href = "./viewExpired.xhtml";
    }
}

if (ice) {
    document.body.configuration.disableDefaultErrorPopups = true;
    ice.onServerError(iceErrorCallback);
} else {
    jsf.ajax.addOnError(jsfErrorCallback);
}
