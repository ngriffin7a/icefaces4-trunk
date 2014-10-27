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

//view manager
(function(im){
    var viewHistory =  [];
    var transitionType = 'horizontal'; //horizontal, vertical
    var currentWidth = 0;
    var currentHeight = 0;
    var proxyFormId;
    var transitionDuration = 100;
    var vendor = (function () {
        if( !window.getComputedStyle ){
            return '';
        }
        var styles = window.getComputedStyle(document.documentElement, ''),
          pre = (Array.prototype.slice
            .call(styles)
            .join('')
            .match(/-(moz|webkit|ms)-/) || (styles.OLink === '' && ['', 'o'])
          )[1],
          dom = ('WebKit|Moz|MS|O').match(new RegExp('(' + pre + ')', 'i'))[1];
        return {
          dom: dom,
          lowercase: pre,
          css: '-' + pre + '-',
          js: pre[0].toUpperCase() + pre.substr(1)
        };
      })();
    var vendorPrefix = vendor.lowercase;

    function getCurrentView(){
        var views = document.querySelectorAll('.mobi-vm-view');
        for( var i = 0 ; i < views.length ; i++ ){
            var selected = views[i].getAttribute('data-selected');
            if( selected == "true" ){
                return views[i].getAttribute('data-view');
            }
        }
    }
    function getNodeForView(view){
        var views = document.querySelectorAll('.mobi-vm-view');
        for( var i = 0 ; i < views.length ; i++ ){
            var viewId = views[i].getAttribute('data-view');
            if( viewId ==  view ){
                return views[i];
            }
        }
    }
    function supportsTransitions(){
        var b = document.body || document.documentElement;
        var s = b.style;
        var p = 'transition';
        if(typeof s[p] == 'string') {return true; }

        var v = ['Moz', 'Webkit', 'Khtml', 'O', 'ms'];
        p = p.charAt(0).toUpperCase() + p.substr(1);
        for(var i=0; i<v.length; i++) {
          if(typeof s[v[i] + p] == 'string') { return true; }
        }
        return false;
    }
    function setOrientation(orient){
        document.body.setAttribute("data-orient", orient);
        if (orient == 'portrait'){
            document.body.classList.remove('landscape');
            document.body.classList.add('portrait');
        }
        else if (orient == 'landscape'){
            document.body.classList.remove('portrait');
            document.body.classList.add('landscape');
        }
        else{
            document.body.classList.remove('portrait');
            document.body.classList.remove('landscape');
        }
        setTimeout(scrollTo, 100, 0, 1);
    }

    function refreshViewDimensions() {
        setTimeout(function() {
            //console.log('refreshViewDimensions()');
             document.body.style.overflowY = 'hidden';

             if ((window.innerWidth != currentWidth) || (ice.mobi.windowHeight() != currentHeight)){
                currentWidth = window.innerWidth;
                currentHeight = ice.mobi.windowHeight();
                var orient = (currentWidth < currentHeight) ? 'portrait' : 'landscape';
                setOrientation(orient);
            }

            var contentHeight = currentHeight - 39; //adjust for header
            var currentView = getNodeForView(getCurrentView());
            if( currentView ){
                if( currentView.querySelectorAll('.mobi-vm-nav-bar').length > 0 ){
                    contentHeight -= 40; //adjust for nav bar if exists
                }
                var contentNode = currentView.querySelectorAll('.mobi-vm-view-content')[0];
                if( contentNode ){
                    contentNode.style.height = '' + contentHeight + 'px';
                    //ice.log.debug(ice.log, 'set view content height to ' + contentHeight);
                }
                else{
                    console.error('ice.mobi.viewManager.refreshViewDimensions() cannot find content node for view = ' + currentView.id);
                }
            }
            var menuNode = document.querySelector('.mobi-vm-menu');
            if( menuNode ){
                menuNode.children[0].style.height = '' + (currentHeight - 39) + 'px';
            }
            else
                console.error('ice.mobi.viewManager.refreshViewDimensions() cannot find menu node');

            var splashNode = document.querySelector('.mobi-vm-splash');
            if( splashNode ){
                splashNode.children[0].style.height = '' + (currentHeight - 39) + 'px';
            }
        }, 300);
    }
    
    function getTransitionFunctions(reverse){
        if( 'horizontal' == transitionType ){
            return [function(from,to){
                setTransform(to, 'translateX(' + (reverse ? '-' : '') 
                        + window.innerWidth +    'px)');
            },function(from,to){
                setTransform(from, 'translateX(' + (reverse ? '100%' : '-100%') + ')');
                setTransform(to, 'translateX(0%)');
            }];
        }
        else if( 'vertical' == transitionType ){
            return [function(from,to){
                setTransform(to, 'translateY(' + (reverse ? '-' : '') 
                        + window.innerWidth +    'px)');
            },function(from,to){
                setTransform(from, 'translateY(' + (reverse ? '100%' : '-100%') + ')');
                setTransform(to, 'translateY(0%)');
            }];
        }
        else if( 'flip' == transitionType ){
            return [function(from,to){
                setTransform(to, 'rotateY(' + (reverse ? '-' : '') + '180deg)');
            },function(from,to){
                setTransform(from, 'rotateY(' + (reverse ? '180deg' : '-180deg') + ')');
                setTransform(to, 'rotateY(0deg)');
            }];
        }
        else if( 'fade' == transitionType ){
            return [function(from,to){
				setTransform(to, 'none');
                setOpacity(to, '0');
            },function(from,to){
				setTransform(from, 'none');
                setOpacity(from, '0');
				setTransform(to, 'none');
                setOpacity(to, '1');
            }];
        }
        else if( 'pageturn' == transitionType ){
            return [function(from,to){
                var parent = to.parentNode;
                parent.style.webkitPerspective = 1100;
                parent.style.webkitPerspectiveOrigin = '50% 50%';
                var fromMirror = document.createElement('div');
                fromMirror.id = '_' + from.id;
                fromMirror.style.width = '50%';
                fromMirror.style.height = '100%';
                fromMirror.style.position = 'absolute';
                fromMirror.style.top = '0px';
                fromMirror.style.right = '0px';
                fromMirror.style.webkitTransformStyle = 'preserve-3d';
                fromMirror.innerHTML = from.innerHTML;
                fromMirror.style.webkitTransform = 'rotateY(180deg)';
                to.parentNode.appendChild(fromMirror);
                from.style.display = 'none';
                parent.style.webkitTransitionProperty = '-webkit-transform';
                parent.style.webkitTransitionDuration = '1000ms';
                parent.style.webkitTransitionTimingFunction = 'ease';
                parent.style.webkitTransformOrigin = 'left';
                parent.style.webkitTransform = 'rotateY(-180deg)';
            },function(from,to){
                var fromMirror = document.getElementById('_'+from.id);
                var parent = to.parentNode;
                parent.removeChild(fromMirror);
                parent.style.webkitTransform = 'none';
                parent.style.webkitTransitionProperty = 'none';
                parent.style.webkitPerspective = 'none';
                parent.style.webkitPerspectiveOrigin = '';
            }];
        }
    }
    function transitionFunction(prop, toStart, from, toEnd){
        return {prop:prop, toStart: toStart, from: from, toEnd: toEnd};
    }
    function setTransitionDuration(elem, val){
        if( vendorPrefix )
            elem.style[''+ vendorPrefix + 'TransitionDuration'] = val;
        elem.style.transitionDuration = val;
    }
    function setTransform(elem, transform){
        elem.style.transitionDuration = transitionDuration;
        if( vendorPrefix ){
            elem.style[''+ vendorPrefix + 'TransitionDuration'] = transitionDuration;
            elem.style[''+ vendorPrefix + 'Transform'] = transform;
        }
        elem.style.transform = transform;
    }
    function setOpacity(elem, val){
        elem.style.transitionDuration = transitionDuration;
        elem.style.opacity = val;
    }
    function setTransitionEndListener(elem, f){
        if( vendorPrefix )
            elem.addEventListener(''+ vendorPrefix + 'TransitionEnd', f, false);
        elem.addEventListener('transitionEnd', f, false);
    }
    function removeTransitionEndListener(elem, f){
        if( vendorPrefix )
            elem.removeEventListener(''+ vendorPrefix + 'TransitionEnd', f, false);
        elem.removeEventListener('transitionEnd', f, false);
    }
    function updateViews(fromNode, toNode, reverse){
        //ice.log.debug(ice.log, 'updateViews() enter');
        if( supportsTransitions() ){
            var transitions = getTransitionFunctions(reverse);
            transitions[0](fromNode,toNode);
            toNode.setAttribute('data-selected', 'true');
            setTransitionDuration(toNode, '');
            setTimeout(transitionComplete, transitionDuration);
            setTimeout(function(){
                //ice.log.debug(ice.log, 'transition() for transition supported');
                transitions[1](fromNode,toNode);
            }, 0);
        } 
        else{
            toNode.style.left = "100%";
            scrollTo(0, 1);
            toNode.setAttribute('data-selected', 'true');
            var percent = 100;
            transition();
            var timer = setInterval(transition, 0);

            function transition(){
                //ice.log.debug(ice.log, 'transition() for transition unsupported');
                percent -= 20;
                if (percent <= 0){
                    percent = 0;
                    clearInterval(timer);
                    transitionComplete();
                }
                fromNode.style.left = (reverse ? (100-percent) : (percent-100)) + "%"; 
                toNode.style.left = (reverse ? -percent : percent) + "%"; 
            }
        }
        
        function transitionComplete(){
            //ice.log.debug(ice.log, 'transitionComplete');
            if( fromNode )
                fromNode.removeAttribute('data-selected');
            checkTimer = setTimeout(refreshViewDimensions, 0);
            setTimeout(refreshView, 0, toNode);
            if( fromNode )
                removeTransitionEndListener(fromNode, transitionComplete);
        }
        //ice.log.debug(ice.log, 'updateViews() exit');
    }
    function refreshBackButton(toNode){
        //ice.log.debug(ice.log, 'refreshBackButton()');
        var headerNode = document.querySelectorAll('.mobi-vm-header')[0];
        var backButton = headerNode.children[1];
        
        var selected = getCurrentView();
        if (backButton){
            if( viewHistory.length == 1 ){
                backButton.style.display = "none";
                return;
            }
            else{
                var prev = viewHistory[viewHistory.length-2];
                if (prev ){
                    var prevView = getNodeForView(prev);
                    if( prevView ){
                        backButton.style.display = "inline";
                        var title = prevView.getAttribute('data-title'),
                            backButtonLabel = backButton.getAttribute('data-backbutton-label') == 'mobi-view' ? 
                                (title ? title : "Back") : backButton.getAttribute('data-backbutton-label'),
                            backButtonText = backButton.querySelector('.mobi-vm-back-text');
                        backButtonText.innerHTML = backButtonLabel;
                    }
                }
            }
        }
    }
    function refreshBackButtonAndViewDimensions(){
        var currentView = getNodeForView(getCurrentView());
        refreshBackButton(currentView);
        refreshViewDimensions();
    }
    function refreshView(toNode){
        //ice.log.debug(ice.log, 'refreshView()');
        var headerNode = document.querySelectorAll('.mobi-vm-header')[0];
        var titleNode = headerNode.firstChild;
        var title = toNode.getAttribute('data-title');
        if (title){
            titleNode.innerHTML = title;
        }
        refreshBackButton();
        
    }
    function viewHasNavBar(view){
        return view.querySelectorAll('.mobi-vm-nav-bar').length > 0;
    }
    
    function indexOfView(view){
        var views = document.querySelectorAll('.mobi-vm-view');
        for( var i = 0 ; i < views.length ; i++ ){
            if( views[i].getAttribute('data-view') === view ){
                return i;
            }
        }
        return -1;
    }

    function isClientSide(){
        var root = document.querySelector('.mobi-vm'),
            clientAttr = root ? root.getAttribute('data-clientside') : null;
        return clientAttr ? clientAttr == 'true' : false;
    }
    
    im.viewManager = {
        showView: function(view, event){
            //ice.log.debug(ice.log, 'showView(' + view + ') current');
            var currentView = getCurrentView();
            if( view == currentView ){
                return;
            }
            var views = document.querySelectorAll('.mobi-vm-view'),
                toNode = getNodeForView(view),
                toIndex = indexOfView(view),
                fromNode = getNodeForView(currentView),
                fromIndex = indexOfView(currentView);
            if (viewHistory.indexOf(view) > -1){
                viewHistory.splice(viewHistory.indexOf(view));
            }
            viewHistory.push(view);
            if( toNode && fromNode ){
                setTimeout(updateViews, 0, fromNode, toNode, toIndex < fromIndex);
            }
            else if( toNode ){
                toNode.setAttribute('data-selected', 'true');
            }
            document.getElementById("mobi_vm_selected").value = view;
            if( isClientSide() ){
                im.resizeAllContainers();
            }
            else{
                jsf.ajax.request(proxyFormId,event,{execute:'@form', render:'@all'});
            }
            
            return false;
        },
        goBack: function(event){
            var goTo = viewHistory.slice(-2,-1)[0];
            if( goTo != undefined ){
                im.viewManager.showView(goTo, event);
            }
            else{
                console.error('ViewManager.goBack() invalid state history = ' + viewHistory);
            }
        },
        setState: function(transition, formId, vHistory){
            if( vHistory.length < 1 ){
                console.error('invalid empty history added to ViewManager.setState() aborting');
                return;
            }
            var view = vHistory[vHistory.length-1],
                       currentView = getCurrentView();
            transitionType = transition;
            proxyFormId = formId;
            viewHistory = vHistory;
            if( !currentView && !view ){
                  document.querySelector('.mobi-vm-header').firstChild.innerHTML = document.querySelector('.mobi-vm').getAttribute('data-title');
                  document.querySelector('.mobi-vm-menu').setAttribute('data-selected', 'true');
            }
            else if( view != currentView){
                var toNode = getNodeForView(view);
                if( toNode ){
                    toNode.setAttribute('data-selected', 'true');
                    document.getElementById("mobi_vm_selected").value = view;
                }
            }
            
            refreshBackButtonAndViewDimensions();
            ice.mobi.addListener(window, 'resize', refreshViewDimensions);
            ice.mobi.addListener(window, 'orientationchange', refreshViewDimensions);
        }
        
    }
}(ice.mobi));
