/* 
* Original Code Copyright Prime Technology.
* Subsequent Code Modifications Copyright 2011-2012 ICEsoft Technologies Canada Corp. (c)
* 
* Licensed under the Apache License, Version 2.0 (the "License"); 
* you may not use this file except in compliance with the License. 
* You may obtain a copy of the License at 
* 
* http://www.apache.org/licenses/LICENSE-2.0 
* 
* Unless required by applicable law or agreed to in writing, software 
* distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
* See the License for the specific language governing permissions and 
* limitations under the License. 
* 
* NOTE THIS CODE HAS BEEN MODIFIED FROM ORIGINAL FORM 
* 
* Subsequent Code Modifications have been made and contributed by ICEsoft Technologies Canada Corp. (c). 
* 
* Code Modification 1: Integrated with ICEfaces Advanced Component Environment. 
* Contributors: ICEsoft Technologies Canada Corp. (c) 
* 
* Code Modification 2: [ADD BRIEF DESCRIPTION HERE] 
* Contributors: ______________________ 
* Contributors: ______________________ 
* 
*/

ice.ace.ProgressBar = function(id, cfg) {
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);

    if(this.cfg.usePolling) {
        this.cfg.formId = ice.ace.jq(this.jqId).parents('form:first').attr('id');
    }

    this.qObj = ice.ace.jq(this.jqId);
    this.qObj.progressbar(this.cfg);
//    if (this.cfg.hasChangeListener) {
        this.qObj.bind('progressbarchange', this, this.changeListener);
//    }
}

ice.ace.ProgressBar.prototype.setValue = function(value) {
    ice.ace.jq(this.jqId).progressbar('value', value);
}

ice.ace.ProgressBar.prototype.getValue  = function() {
    return ice.ace.jq(this.jqId).progressbar('value');
}

ice.ace.ProgressBar.prototype.start = function() {
    var _self = this;
	
    if(this.cfg.usePolling) {
		
        this.progressPoll = setInterval(function() {
            var options = {
                source: _self.id,
                execute: _self.id,
                render: "@none",
                formId: _self.cfg.formId,
                async: true,
                oncomplete: function(xhr, status, args) {
                    var value = args[_self.id + '_value'];
                    _self.setValue(value);

                    //trigger close listener
                    if(value === 100) {
                        _self.fireCompleteEvent();
                    }
                }
            };

            ice.ace.AjaxRequest(options);
            
        }, this.cfg.pollingInterval);
    }
}

ice.ace.ProgressBar.prototype.fireCompleteEvent = function() {
    var completeListener = this.cfg && this.cfg.behaviors && this.cfg.behaviors.complete;
    clearInterval(this.progressPoll);

    var options = {
        source: this.id,
        execute: this.id,
        render: "@none",
        formId: this.cfg.formId,
        async: true
    };

    var params = {};
    params[this.id + '_complete'] = true;

    options.params = params;
	    
    if (completeListener) {
        options.params[this.id] = this.id; // also triggers listener, if any
        ice.ace.ab(ice.ace.extendAjaxArgs(
                completeListener,
                ice.ace.clearExecRender(options)
        ));
    } else ice.ace.AjaxRequest(options);
}

ice.ace.ProgressBar.prototype.changeListener = function(ev, ui) {
    var data = ev.data, id = data.id;
    var cfg = data.cfg;
    var changeListener = cfg && cfg.behaviors && cfg.behaviors.change;
    var options = {
        source: id,
        execute: id,
        render: "@none",
        formId: cfg.formId,
        async: true
    };

    var params = {};
    params[id + '_change'] = true;
    params[id + '_value'] = ui.value;
    params[id + '_percentage'] = ui.percentage;

    options.params = params;

    if (changeListener) {
        options.params[this.id] = this.id; // also triggers listener, if any
        ice.ace.ab(ice.ace.extendAjaxArgs(
                changeListener,
                ice.ace.clearExecRender(options)
        ));
    } else ice.ace.AjaxRequest(options);
};

ice.ace.ProgressBar.prototype.cancel = function() {
    var cancelListener = this.cfg && this.cfg.behaviors && this.cfg.behaviors.cancel;
    clearInterval(this.progressPoll);
    var _self = this;

    var options = {
        source: this.id,
        execute: this.id,
        render: "@none",
        formId: this.cfg.formId,
        async: true,
        oncomplete:function(xhr, status, args) {
            _self.setValue(0);
        }
    };

    var params = {};
    params[this.id + '_cancel'] = true;

    options.params = params;

    if (cancelListener) {
        options.params[this.id] = this.id; // also triggers listener, if any
        ice.ace.ab(ice.ace.extendAjaxArgs(
                cancelListener,
                ice.ace.clearExecRender(options)
        ));
        this.setValue(0);
    } else ice.ace.AjaxRequest(options);
}
