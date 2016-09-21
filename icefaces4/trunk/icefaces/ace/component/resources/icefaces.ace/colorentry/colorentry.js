if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};

if (!ice.ace.ColorEntrys) ice.ace.ColorEntrys = {};

ice.ace.ColorEntry = function(id, cfg) {
    ice.ace.jq().ready(function() {
        this.id = id;
        this.jqId = ice.ace.escapeClientId(id);
        this.jq = ice.ace.jq(this.jqId);
        this.spanSelector = this.jqId + " > span";
        var options= cfg.options;

        ice.ace.ColorEntry.setResetValue(id);
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

        var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
        ice.ace.jq(input).hide();
        if (!ice.ace.ColorEntrys[id]) {
            ice.ace.ColorEntry.lazyInit(id, options);
        } else {
            // if instance was previously initialized, create right away and return
            ice.ace.jq(input).spectrum(options);
        }

        ice.onElementUpdate(id, function() {
           if (ice.ace.ColorEntrys[id]){
               console.log(" destroying spectrum for id="+id);
               var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
               ice.ace.jq(input).spectrum("destroy");
               ice.ace.ColorEntrys[id] = null;
           } ;
        });
    });
};

ice.ace.ColorEntry.setResetValue = function(id){
    if (typeof ice.ace.resetValues[id] == 'undefined') {
            var input = ice.ace.escapeClientId(id) + "_input";
    		if (input){
    			var initialValue = ice.ace.jq(input).val();
                console.log(" setting initialValue to="+initialValue);
    			ice.ace.setResetValue(id, initialValue);
            }
    }
};
ice.ace.ColorEntry.lazyInit = function(id, options){
    var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
    var inputSelector = (ice.ace.escapeClientId(id) + "_input");
    var inputVal;
    if (input){
        ice.ace.jq(input).css("display", "none");
        inputVal=ice.ace.jq(input).val();
    }
    var spanSelector = ice.ace.escapeClientId(id) + " > span";
    var parentSpan = ice.ace.jq(spanSelector);
    if (parentSpan.children('div.sp-replacer sp-light').length > 0){
        //may have to remove it?
    }  else {
        var replacerClass = "sp-replacer sp-light ";
        if (options.replacerClassName){
           replacerClass += options.replacerClassName;
        }
        ice.ace.jq(parentSpan).append("<div class='"+replacerClass+"'> <div class='sp-preview'>" +
            "<div class='sp-preview-inner' style='background-color:"+inputVal+"'></div></div><div class='sp-dd'>&#9660;</div></div>");
        // Event Binding with no submit.  Just inits and shows Spectrum
        ice.ace.jq(parentSpan).on("click", "div", function(e){
            e.stopPropagation();
            e.preventDefault();
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
        //no aria role to deal with
	}
    var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
    ice.ace.jq(input).spectrum("set", ice.ace.jq(input).val());
};

ice.ace.ColorEntry.clear = function(id, ariaEnabled){
    var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
    if (input){
        ice.ace.jq(input).val('');
        ice.ace.jq(input).spectrum("set", ice.ace.jq(input).val());
    }
}