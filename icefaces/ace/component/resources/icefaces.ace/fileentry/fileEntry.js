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

if (!window['ice']) {
    window.ice = {};
}
if (!window['ice']['ace']) {
    window.ice.ace = {};
}

ice.ace.fileentry = {
    logAll : false,  // Set to true to enable more debugging to be logged

    consoleLog : function(important, msg) {
        if (window.console && window.console.log) {
            if (ice.ace.fileentry.logAll || important) {
                console.log(msg);
                //alert(msg);
            }
        }
    },

    onchange : function(event, id, multiple, autoUpload) {
        var elem, form, tableElem, tr, i;

        elem = ice.ace.fileentry.eventTarget(event);
        form = ice.ace.fileentry.formOf(elem);

        ice.ace.fileentry.consoleLog(false, "fileentry.onchange  id: " + id + "  multiple: " + multiple + "  autoUpload: " + autoUpload + "  elem: " + elem + "  form.id: " + form.id);

        if (multiple) {
            // Get the table
            tableElem = document.getElementById(id+'_multSelTbl');

            // Look for HTML5 files, and otherwise get the input value
            // Create the table rows
            ice.ace.fileentry.consoleLog(false, "fileentry.onchange  elem.name: " + elem.name + "  elem.id: " + elem.id);
            ice.ace.fileentry.consoleLog(false, "fileentry.onchange  elem.value: " + elem.value + "  elem.files: " + elem.files);
            if (elem.files) {
                for (i = 0; i < elem.files.length; i++) {
                    ice.ace.fileentry.consoleLog(false, "fileentry.onchange  " + i + "  name: " + elem.files[i].name + "  size: " + elem.files[i].size + "  type: " + elem.files[i].type);
                    tr = document.createElement("tr");
                    var prefix = elem.id + "_tr_";
                    tr.id = prefix+i;
                    tr.innerHTML = ("<td>"+elem.files[i].name+"</td><td>"+elem.files[i].size+"</td><td>"+elem.files[i].type+"</td>"
                            +((i>0)?"":"<td rowspan='"+elem.files.length+"'><button type='button' onclick='ice.ace.fileentry.cancelFileSelection(\""+elem.id+"\","+ice.ace.fileentry.arrayOfStrings(prefix,elem.files.length)+");'>Cancel</button></td>"));
                    tableElem.appendChild(tr);
                }
            } else {
                ice.ace.fileentry.consoleLog(false, "fileentry.onchange  value: " + elem.value);
				// alternative approach to support IE7 and IE8
				tbody = document.createElement("tbody");
                tr = document.createElement("tr");
				var trId = elem.id + "_tr";
                tr.setAttribute("id", trId);
				td1 = document.createElement("td");
				text1 = document.createTextNode(elem.value);
				td1.appendChild(text1);
				td2 = document.createElement("td");
				button = document.createElement("button");
				button.setAttribute("type", "button");
				var onclick = function() {
					ice.ace.fileentry.cancelFileSelection(elem.id,[trId]);
				};
				if (button.addEventListener) { 
					button.addEventListener('click', onclick, false); 
				} else if (button.attachEvent) { 
					button.attachEvent('onclick', onclick); 
				} else {
					button.onclick = "ice.ace.fileentry.cancelFileSelection('"+elem.id+"',['"+trId+"']);";
				}
				text2 = document.createTextNode("Cancel");
				button.appendChild(text2);
				td2.appendChild(button);
				tr.appendChild(td1);
				tr.appendChild(td2);
				tbody.appendChild(tr);
                tableElem.appendChild(tbody);
            }
        }

        if (autoUpload) {
            if (form) {
                form.onsubmit();
                form.submit();
            }
        } else if (multiple) {
            // Hide the current input and add another
            var newElem = elem.cloneNode(true);
            newElem.id = elem.id + "_1";
			newElem.value = '';
            elem.parentNode.appendChild(newElem);
            elem.style.cssText = "display:none;";
        }
    },

    eventTarget : function(event) {
        event = event || window.event;
        return(event.target || event.srcElement);
    },

    formOf : function(element) {
        var parent = element.parentNode;
        while (parent) {
            if (parent.tagName && parent.tagName.toLowerCase() == 'form') return parent;
            parent = parent.parentNode;
        }

        return null;
    },

    initiateSubmit : function(formId) {
        var form = document.getElementById(formId);
        form.onsubmit();
        form.submit();
    },

    clearMultipleSelectionTableRows : function(id) {
        var tableElem = document.getElementById(id+'_multSelTbl');
        if (tableElem) {
			while (tableElem.hasChildNodes()) {
				tableElem.removeChild(tableElem.lastChild);
			}
        }
    },

    cancelFileSelection : function(inputElemId, trIds) {
        var index, elem;
        elem = document.getElementById(inputElemId);
        if (elem) {
            elem.parentNode.removeChild(elem);
        }
        for (index = 0; index < trIds.length; index++) {
            elem = document.getElementById(trIds[index]);
            if (elem) {
                elem.parentNode.removeChild(elem);
            }
        }
    },

    arrayOfStrings : function(prefix, count) {
        var index, result = "[";
        for (index = 0; index < count; index++) {
            result = result + "\"" + prefix + index + "\"";
            if (index < count-1) {
                result = result + ","
            }
        }
        result = result + "]";
        return result;
    },

    iframeLoaded : function(context, id) {
        ice.ace.fileentry.consoleLog(false, "iframeLoaded()  begin  id: " + id);
        
        var i = document.getElementById(id);
        if ((typeof XMLDocument != "undefined") && i.contentDocument) {
            //var doc = i.contentDocument.document.XMLDocument ?
            //    i.contentDocument.document.XMLDocument :
            //    i.contentDocument.document;
            var d = i.contentDocument;
            if (d instanceof XMLDocument || "xmlEncoding" in d) {
                var serializer = new XMLSerializer();
                var responseText = serializer.serializeToString(d);
                //i.contentDocument.document.body?i.contentDocument.document.body.innerHTML:null;
                ice.ace.fileentry.response(d, responseText, context);
            }
        } else if (i.contentWindow &&
                   i.contentWindow.document) {
			var d = i.contentWindow.document.XMLDocument ?
                i.contentWindow.document.XMLDocument :
                i.contentWindow.document;
            var responseText;
            if (d.xml) {
                responseText = d.xml;
                //i.contentWindow.document.body?ii.contentWindow.document.body.innerHTML:null;
            } else {
                var serializer = new XMLSerializer();
                responseText = serializer.serializeToString(d);
            }
            ice.ace.fileentry.response(d, responseText, context);
        }
        
        ice.ace.fileentry.consoleLog(false, "iframeLoaded()  end");
	},
        
    response : function(responseXML, responseText, context) {
        ice.ace.fileentry.consoleLog(false, responseText);
        
        var request = {};
        request.status = 200;
        request.responseText = responseText;
        request.responseXML = responseXML;
        
        jsf.ajax.response(request, context);
    },
    
    captureFormOnsubmit : function(formId, iframeId, progressPushId, progressResourcePath) {
        var f = document.getElementById(formId);
        // To support portlets, as well as flagging FileEntry server code to
        // handle this multipart request, and not handle multipart requests
        // from other upload components which will omit this parameter.
        var encodedURL = f.elements['ice.fileEntry.encodedURL'];
        if(encodedURL){
            f.action = encodedURL.value;
        }

        if (progressPushId) {
            var regPushIds = [progressPushId];
            window.ice.push.register(regPushIds, function(pushedIds) {
                ice.ace.fileentry.onProgress(pushedIds, progressResourcePath);
            });
        }

        f.onsubmit = function(event) {
            ice.ace.fileentry.formOnsubmit(event, f, iframeId, progressPushId);
        };
    },
    
    formOnsubmit : function(event, formElem, iframeId, progressPushId) {
        ice.ace.fileentry.consoleLog(false, "formOnsubmit()  begin");

        // Set every fileEntry component in the form into the indeterminate
        // state, before progress notifications arrive, if icepush is present
        ice.ace.fileentry.setFormFileEntryStates(formElem, "uploading", true);

        var context = {};
        context.element = formElem;
        context.sourceid = formElem.id;
        context.formid = formElem.id;
        context.render = "@all";
        var context_execute = formElem.id; // Don't do "@all" or else FacesMessagePhaseListener doesn't work
        
        var oldEncoding;
        if (formElem.encoding) {
            oldEncoding = formElem.encoding;
            formElem.encoding = 'multipart/form-data';
        }
        else {
            oldEncoding = formElem.enctype;
            formElem.enctype = 'multipart/form-data';
        }
        
        var hSrc = ice.ace.fileentry.addHiddenInput(
            formElem, 'javax.faces.source', context.sourceid);
        var hParEx = ice.ace.fileentry.addHiddenInput(
            formElem, 'javax.faces.partial.execute', context_execute);
        var hParRend = ice.ace.fileentry.addHiddenInput(
            formElem, 'javax.faces.partial.render', context.render);
        var hParAjax = ice.ace.fileentry.addHiddenInput(
            formElem, 'javax.faces.partial.ajax', 'true');
        // Flag specifying javascript, to differentiate our non-javascript mode
        var hIceAjax = ice.ace.fileentry.addHiddenInput(
            formElem, 'ice.fileEntry.ajaxResponse', 'true');

        formElem.target = iframeId;
        var iframeElem = document.getElementById(iframeId);
        var iframeOnloadHandler = function() {
            ice.ace.fileentry.consoleLog(false, "onload()  begin");

            // Cleanup the form before proceeding
            if (formElem.encoding) {
                formElem.encoding = oldEncoding;
            } else {
                formElem.enctype = oldEncoding;
            }
            formElem.target = "_self";
            
            // This worked in FF, but not IE 8, so use explicit vars below
            // formElem.removeChild( formElem.elements['javax.faces.source'] );
            
            formElem.removeChild( hSrc );
            formElem.removeChild( hParEx );
            formElem.removeChild( hParRend );
            formElem.removeChild( hParAjax );
            formElem.removeChild( hIceAjax );

            // Set every fileEntry component in the form into the complete
            // state, which hides the progress
            ice.ace.fileentry.setFormFileEntryStates(formElem, "complete", false);

            ice.ace.fileentry.iframeLoaded(context, iframeId);
            
            // Cleanup the response iframe, after finished using it
            if (iframeElem.removeEventListener) {
                iframeElem.removeEventListener("load", iframeOnloadHandler, false);
            } else if (iframeElem.detachEvent) {
                iframeElem.detachEvent("onload", iframeOnloadHandler);
            }
            
            if (iframeElem.hasChildNodes()) {
                while (iframeElem.childNodes.length >= 1) {
                    iframeElem.removeChild(iframeElem.firstChild);
                }
            }

            if (progressPushId) {
                //POLL: Comment this section
                /*
                var unregPushIds = new Array(1);
                unregPushIds[0] = progressPushId;
                window.ice.push.deregister(unregPushIds);
                */
            }

            ice.ace.fileentry.consoleLog(false, "onload()  end");
        };
        if (iframeElem.addEventListener) {
            iframeElem.addEventListener("load",iframeOnloadHandler,false);
        }
        else if (iframeElem.attachEvent) {
            iframeElem.attachEvent("onload",iframeOnloadHandler);
        }

        /*
        //POLL: Uncomment this section
        var progressTimeout = function() {
            ice.ace.fileentry.onProgress(null, progressResourcePath);
            setTimeout(progressTimeout, 2000);
        };
        setTimeout(progressTimeout, 2000);
        */

        ice.ace.fileentry.consoleLog(false, "formOnsubmit()  end");
    },
        
    onProgress : function(pushIds, progressResourcePath) {
        ice.ace.fileentry.consoleLog(false, 'onProgress()  progressResourcePath: ' + progressResourcePath);

        window.ice.push.post(progressResourcePath, function(parameter) {}, function(statusCode, contentAsText, contentAsDOM) {
            ice.ace.fileentry.consoleLog(false, 'onProgress()  GET  contentAsText: ' + contentAsText);
            if (!contentAsText) {
                return;
            }
            var progressInfo = eval("(" + contentAsText + ")");
            var percent = progressInfo['percent'];
            var percentStr = percent + "%";
            var results = progressInfo['results'];
            var res;
            for (res in results) {
                var fileDiv = document.getElementById(results[res]);
                if (fileDiv) {
                    var outerDiv = fileDiv.childNodes[1];
                    if (outerDiv) {
                        if (outerDiv.className != "complete" && outerDiv.className != "inactive") {
                            var progDiv = outerDiv.firstChild.firstChild;
                            if (progDiv) {
                                if (percent != 100) {
                                    ice.ace.fileentry.removeStyleClass(progDiv, 'ui-corner-right');
                                } else {
                                    ice.ace.fileentry.ensureStyleClass(progDiv, 'ui-corner-right');
                                }
                                progDiv.style.width = percentStr;
                            }
                            outerDiv.className = "progress";
                            if (progDiv) {
                                ice.ace.fileentry.setOpacity(progDiv, 100);
                            }
                        }
                    }
                }
            }
        });
    },

    addHiddenInput : function(formElem, hiddenName, val) {
        var inputElem = document.createElement('input');
        inputElem.setAttribute('type','hidden');
        inputElem.setAttribute('name',hiddenName);
        inputElem.setAttribute('autocomplete', 'off');
        inputElem.setAttribute('value',val);
        formElem.appendChild(inputElem);
        return inputElem;
    },

    setFormFileEntryStates : function(formElem, className, pulse) {
        var fileEntryDivs = ice.ace.fileentry.getElementsByClass(
                "ice-file-entry",formElem,"div");
        var fileEntryLen = fileEntryDivs.length;
        var fileEntryIndex;
        for (fileEntryIndex = 0; fileEntryIndex < fileEntryLen; fileEntryIndex++) {
            var fileDiv = fileEntryDivs[fileEntryIndex];
            if (fileDiv) {
                var outerDiv = fileDiv.childNodes[1];
                if (outerDiv) {
                    var progDiv = outerDiv.firstChild.firstChild;
                    if (progDiv) {
                        progDiv.style.width = "100%";
                    }
                    outerDiv.className = className;
                    if (progDiv && pulse) {
                        ice.ace.fileentry.pulseElementUntilChangedStyleClass(0, progDiv, outerDiv, className);
                    }
                }
            }
        }
    },

    getElementsByClass : function(searchClass,node,tag) {
        var classElements = new Array();
        if (node == null) {
            node = document;
        }
        if (tag == null) {
            tag = '*';
        }
        var els = node.getElementsByTagName(tag);
        var elsLen = els.length;
        var pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)");
        for (i = 0, j = 0; i < elsLen; i++) {
            if ( pattern.test(els[i].className) ) {
                classElements[j] = els[i];
                j++;
            }
        }
        return classElements;
    },

    removeStyleClass : function(elem, sc) {
        var styleClasses = elem.className;
        var idx = styleClasses.indexOf(sc);
        if (idx != -1) {
            var theLeft = styleClasses.substring(0, idx);
            var theRight = styleClasses.substring(idx + sc.length, styleClasses.length);
            styleClasses = theLeft + theRight;
            elem.className = styleClasses;
        }
    },

    ensureStyleClass : function(elem, sc) {
        var styleClasses = elem.className;
        var idx = styleClasses.indexOf(sc);
        if (idx == -1) {
            styleClasses = styleClasses + ' ' + sc;
            elem.className = styleClasses;
        }
    },

    setOpacity : function(elem, opacityLevel) {
        var eStyle = elem.style;
        eStyle.opacity = opacityLevel / 100;
        eStyle.filter = 'alpha(opacity='+opacityLevel+')';
    },

    pulseElementUntilChangedStyleClass : function(pos, progDiv, outerDiv, className) {
        // Fade in from 20% to 80%, then fade out from 80% to 20%
        // Continue cycling the opacity on progDiv until outerDiv's className is not className anymore
        if (outerDiv.className != className) {
            return;
        }
        var minOpacity = 20;
        var maxOpacity = 80;
        var stepOpacity = 10;
        var millis = 100;

        var opacity;
        if (pos == 0) {
            opacity = minOpacity;
            pos = 1;
        }
        else if (pos > 0) {
            opacity = minOpacity + (pos * stepOpacity);
            if (opacity >= maxOpacity) {
                opacity = maxOpacity;
                pos = -1;
            } else {
                pos = pos + 1;
            }
        }
        else if (pos < 0) {
            opacity = maxOpacity + (pos * stepOpacity);
            if (opacity <= minOpacity) {
                opacity = minOpacity;
                pos = 1;
            } else {
                pos = pos - 1;
            }
        }
        ice.ace.fileentry.setOpacity(progDiv, opacity);
        setTimeout(function() {ice.ace.fileentry.pulseElementUntilChangedStyleClass(pos, progDiv, outerDiv, className);}, millis);
    },

    clearFileSelection : function(clientId) {
        ice.ace.fileentry.clearMultipleSelectionTableRows(clientId);
        var root = document.getElementById(clientId);
        if (root) {
            var fileEntryInputs = root.getElementsByTagName("input");
            var fileEntryInputsLen = fileEntryInputs.length;
            var fileEntryInputsIndex;
            for (fileEntryInputsIndex = 0; fileEntryInputsIndex < fileEntryInputsLen; fileEntryInputsIndex++) {
                var fileInput = fileEntryInputs[fileEntryInputsIndex];
                if (fileInput) {
                    if (fileEntryInputsIndex > 0) {
                        fileInput.parentNode.removeChild(fileInput);
                    } else {
						fileInput.value = '';
						fileInput.style.cssText = '';
					}
                }
            }
        }
    }
};
