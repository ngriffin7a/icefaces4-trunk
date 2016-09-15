if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};


if (!ice.ace.ColorEntrys) ice.ace.ColorEntrys = {};

ice.ace.ColorEntry = function(id, cfg) {
    ice.ace.jq().ready(function() {
        console.log("colorEntry for id="+id);
        this.id = id;
        this.jqId = ice.ace.escapeClientId(id);
        this.jq = ice.ace.jq(this.jqId);
        this.spanSelector = this.jqId + " > span";
        var options= cfg.options;
     /*   var hideFn = function(){
           console.log(" in hide fn");
        } */
        var changeFn = function()
        {
            var t =  ice.ace.jq(input).spectrum("get");
            var valueFormat= cfg.options.preferredFormat;
            var convertValue = false;
            if (t && valueFormat)convertValue=true;
            if (convertValue && valueFormat=="hex"){
                t.toHex();
            } else if (convertValue && valueFormat == "hsl"){
                t.toHsl();
            } else if (convertValue && valueFormat == "rgb") {
                t.toRgbString();
            }else if (convertValue && valueFormat == "hsv"){
                t.toHsvString();
            } else if (convertValue){
                t.toHexString();
            }
            if (cfg.behaviors && cfg.behaviors.colorChange) {
                ice.ace.ab(cfg.behaviors.colorChange);
            }

        }
        options.change=changeFn;
  // not used yet...      options.hide=hideFn;

        var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
        ice.ace.jq(input).hide();
        var self = this;

        if (ice.ace.instance(id)) {
            //style it.
            ice.ace.ColorEntry.lazyInit(id, options);
        };

		// if instance was previously initialized, create right away and return
		if (ice.ace.ColorEntrys[id]) {
			//return;
		}

        ice.onElementUpdate(id, function() {
           if (ice.ace.ColorEntry[id]){
               console.log(" destroying spectrum for id="+id);
               var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
               ice.ace.jq(input).spectrum("destroy");
               ice.ace.ColorEntry[id] = null;
           } ;
        });
    });
};

ice.ace.ColorEntry.setResetValue = function(id){
    if (typeof ice.ace.resetValues[id] == 'undefined') {
            var jqId = ice.ace.escapeClientId(id) + "_input";
    		if (jqId){
    			var fieldSelector = jqId;
    			var initialValue = ice.ace.jq(fieldSelector).val();
    			if (initialValue) ice.ace.setResetValue(id, initialValue);
    		}
    	}
};
ice.ace.ColorEntry.lazyInit = function(id, options){
    var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
    var inputSelector = (ice.ace.escapeClientId(id) + "_input");
    var firstDivClass ="sp-replacer sp-light ";
    if (options.replacerClassName){
       firstDivClass = firstDivClass+options.replacerClassName;
    }
    var inputVal;
    if (input){
        ice.ace.jq(input).css("display", "none");
        inputVal=ice.ace.jq(input).val();
    }
    var spanSelector = ice.ace.escapeClientId(id) + " > span";
    var parentSpan = ice.ace.jq(spanSelector);
    if (parentSpan.children('div.sp-replacer sp-light').length > 0){
        //may have to remove it??
    }  else {
        ice.ace.jq(parentSpan).append("<div class='sp-replacer'> <div class='sp-preview'>" +
            "<div class='sp-preview-inner' style='background-color:"+inputVal+"'></div></div><div class='sp-dd'>&#9660;</div></div>");
        // Event Binding with no submit.  Just inits and shows Spectrum
        ice.ace.jq(parentSpan).on("click", "div", function(e){
            e.stopPropagation();
            e.preventDefault();
            console.log(" click on parentSpan");
            //remove old one and initialize and open spectrum
            if (ice.ace.jq(parentSpan).find("div.sp-replacer")){
                ice.ace.jq(parentSpan).find("div.sp-replacer").remove();
            }
            ice.ace.ColorEntrys[id] = ice.ace.jq(input).spectrum(options);
            ice.ace.jq(input).spectrum("show");
        }) ;
    }
} ;
ice.ace.ColorEntry.reset = function(id, ariaEnabled) {
	var value = ice.ace.resetValues[id];
	if (ice.ace.isSet(value)) {
        var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
        if (value) {
            ice.ace.jq(input).val(value);
        }
        //no aria role to deal with?
	}
};
