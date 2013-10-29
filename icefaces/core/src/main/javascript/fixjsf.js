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
    function extractTagContent(tag, html) {
        var start = new RegExp('\<' + tag + '[^\<]*\>', 'g').exec(html);
        var end = new RegExp('\<\/' + tag + '\>', 'g').exec(html);
        if (start && end && start.index && end.index) {
            var tagWithContent = html.substring(start.index, end.index + end[0].length);
            return tagWithContent.substring(tagWithContent.indexOf('>') + 1, tagWithContent.lastIndexOf('<'));
        } else {
            return '';
        }
    }

    function extractAttributeValue(html, name, defaultValue) {
        var re = new RegExp(name + '="([\\S]*?)"', 'im');
        var result = html.match(re);
        return result ? result[1] : defaultValue;
    }

    function extractSrcAttribute(html) {
        return extractAttributeValue(html, 'src');
    }

    function unescapeHtml(text) {
        if (text) {
            var temp = document.createElement("div");
            temp.innerHTML = text;
            var result = temp.firstChild.data;
            temp.removeChild(temp.firstChild);
            return result;
        } else {
            return text;
        }
    }

    function stripPathParameters(url) {
        try {
            //see if the URL has path parameters
            var semicolonPosition = indexOf(url, ';');
            var newURL = substring(url, 0, semicolonPosition);
            //see if the URL has a query string
            try {
                var questionPosition = indexOf(url, '?');
                return newURL + substring(url, questionPosition, url.length);
            } catch (e) {
                return newURL;
            }
        } catch (e) {
            return url;
        }
    }

    var client = Client();

    //fix for ICE-6481, effective only when the document found in the DOM update is not valid XML
    var scriptElementMatcher = /<script[^>]*>([\S\s]*?)<\/script>/igm;

    function extractAndEvaluateScripts(content) {
        var scriptTags = content.match(scriptElementMatcher);
        if (scriptTags) {
            var scripts = collect(scriptTags, function(script) {
                var src = extractSrcAttribute(script);
                var code;
                if (src) {
                    src = stripPathParameters(unescapeHtml(src));
                    if (contains(scriptRefs, src)) {
                        code = '';
                    } else {
                        getSynchronously(client, src, noop, noop, function(response) {
                            code = contentAsText(response);
                        });
                        append(scriptRefs, src);
                    }
                } else {
                    code = unescapeHtml(extractTagContent('script', script));
                }
                return code;
            });

            //select only non empty scripts
            each(select(scripts, identity), namespace.globalEval);
        }
    }

    var linkElementMatcher = /<link[^>]*>/igm;

    function extractAndAppendStyles(content) {
        var linkTags = content.match(linkElementMatcher);
        if (linkTags) {
            var newLinkRefs = collect(select(linkTags,
                function(linkTag) {
                    return extractAttributeValue(linkTag, 'type') == 'text/css';
            }), function(linkTag) {
                    return replace(extractAttributeValue(linkTag, 'href'), '&amp;', '&');
            });

            var headElement = document.getElementsByTagName("head")[0];

            var addedLinkRefs = complement(newLinkRefs, linkRefs);
            each(addedLinkRefs, function(src) {
                var code;
                getSynchronously(client, src, noop, noop, function(response) {
                    code = contentAsText(response);
                });

                var styleElement = document.createElement('style');
                styleElement.type = 'text/css';
                headElement.appendChild(styleElement);
                if (styleElement.styleSheet) {   // IE
                    styleElement.styleSheet.cssText = code;
                } else {                // the world
                    var textNode = document.createTextNode(code);
                    styleElement.appendChild(textNode);
                }

                //remove text nodes added to avoid memory usage increase
                //headElement.removeChild(styleElement);
            });

            linkRefs = newLinkRefs;
        }
    }

    //remember loaded script references
    var scriptRefs = [];
    var linkRefs = [];

    function createResourceMatching(attribute) {
        return function(result, s) {
            var src = s.getAttribute(attribute);
            if (src) {
                append(result, stripPathParameters(src));
            }
            return result;
        };
    }

    onLoad(window, function() {
        var scriptElements = document.documentElement.getElementsByTagName('script');
        inject(scriptElements, scriptRefs, createResourceMatching('src'));
    });

    onLoad(window, function() {
        var linkElements = document.documentElement.getElementsByTagName('link');
        inject(linkElements, linkRefs, createResourceMatching('href'));
    });

    function findViewRootUpdate(content) {
        return detect(content.getElementsByTagName('update'), function(update) {
            return update.getAttribute('id') == 'javax.faces.ViewRoot';
        });
    }

    onLoad(window, function() {
        //clear the flag, only DOM updates are supposed to update it
        document.documentElement.isHeadUpdateSuccessful = null;
    });

    namespace.onBeforeUpdate(function(content) {
        var headUpdate = detect(content.getElementsByTagName('extension'), function(update) {
            return update.getAttribute('type') == 'javax.faces.ViewHead';
        });

        var originalDocumentWrite = document.write;
        //disable document.write function since we don't evaluate the code while HTML is parsed
        //by disabling the function we avoid page reloads caused by out of context document.write invocations
        document.write = noop;

        if (headUpdate) {
            var innerContent = headUpdate.firstChild.data;
            extractAndEvaluateScripts(innerContent);
            extractAndAppendStyles(innerContent);
            document.title = extractTagContent('title', innerContent);
        }
        //restore original function
        document.write = originalDocumentWrite;
    });

    namespace.onAfterUpdate(function(content) {
        var rootUpdate = findViewRootUpdate(content);

        //isHeadUpdateSuccessful property is set when a script element rendered in the head is properly evaluated
        if (rootUpdate && !document.documentElement.isHeadUpdateSuccessful) {
            var headContent = extractTagContent('head', rootUpdate.firstChild.data);
            extractAndEvaluateScripts(headContent);
            extractAndAppendStyles(headContent);
        } else {
            //clear the flag for the next update
            document.documentElement.isHeadUpdateSuccessful = null;
        }

        //fix for ICE-6916
        if (rootUpdate) {
            document.title = extractTagContent('title', rootUpdate.firstChild.data);
        }
    });

    //ICE-7129 -- remove included iframes before updating the element to avoid a hard crash in IE8
    namespace.onBeforeUpdate(function(updates) {
        each(updates.getElementsByTagName('update'), function(update) {
            var id = update.getAttribute('id');
            var updatedElement = lookupElementById(id);
            if (updatedElement) {
                each(updatedElement.getElementsByTagName('iframe'), function(iframe) {
                    if (iframe && iframe.parentNode) {
                        iframe.parentNode.removeChild(iframe);
                    }
                });
            }
        });
    });


    if (!/MSIE/.test(navigator.userAgent)) {
        namespace.onBeforeUpdate(function(content) {
            var rootUpdate = findViewRootUpdate(content);
            //move configuration to the 'html' element to allow the rest of the callbacks to find it once the update is applied
            if (rootUpdate) {
                var configuration = document.body.configuration;
                if (configuration) {
                    document.documentElement.configuration = configuration;
                }
            }
        });
    }

    //ICE-7916 -- fix IE6 flicker
    try {
        document.execCommand("BackgroundImageCache", false, true);
    } catch(err) {
    }
})();
