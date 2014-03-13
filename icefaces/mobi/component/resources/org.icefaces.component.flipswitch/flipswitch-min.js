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

if(!window.mobi){window.mobi={};}mobi.flipswitch={lastTime:0,init:function(a,g){if(g.transHack){var j=new Date().getTime();if((j-mobi.flipswitch.lastTime)<100){console.log("Double click suppression required");return;}mobi.flipswitch.lastTime=j;}this.id=a;this.cfg=g;this.flipperEl=g.elVal;this.event=g.event;var c=false;if(this.cfg.behaviors){c=true;}if(this.flipperEl){var f=this.flipperEl.className;var i="off";var b=this.flipperEl.children[0].className;var h=this.flipperEl.children[2].className;if(f.indexOf("-off ")>0){this.flipperEl.className="mobi-flipswitch mobi-flipswitch-on ui-widget";this.flipperEl.children[0].className="mobi-flipswitch-txt-on ui-button ui-corner-all ui-state-default ui-state-active";this.flipperEl.children[2].className="mobi-flipswitch-txt-off ui-button ui-corner-all ui-state-default";i=true;}else{this.flipperEl.className="mobi-flipswitch mobi-flipswitch-off ui-widget";this.flipperEl.children[0].className="mobi-flipswitch-txt-on ui-button ui-corner-all ui-state-default";this.flipperEl.children[2].className="mobi-flipswitch-txt-off ui-button ui-corner-all ui-state-default ui-state-active";i=false;}var e=this.id+"_hidden";var d=document.getElementById(e);if(d){d.value=i.toString();}if(c){if(this.cfg.behaviors.activate){ice.ace.ab(this.cfg.behaviors.activate);}}}}};