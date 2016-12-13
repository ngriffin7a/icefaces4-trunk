if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};

ice.ace.ColorEntry = function(id, cfg) {
    console.log(" creating widget for id="+id);
    var behavior, altFieldVal;
    this.id = id;
    this.cfg = cfg;
    this.jqId = ice.ace.escapeClientId(id);
    this.jqElId =  this.jqId + '_input';
    this.jq = ice.ace.jq(this.jqElId);
 //   this.cfg.formId = this.jq.parents('form:first').attr('id');
    //i18n and l7n
  //  this.configureLocale();
    this.options = cfg;
    this.pickerFn = "colorpicker";
    if (this.options.color) {
        this.jq.value = this.options.color;
    }
    //Initialize colrEntry
    if(!this.cfg.disabled) {
        console.log(" creating colorpicker!");
        this.jq.colorpicker(this.cfg);
    }

//	ice.ace.setResetValue(this.id, this.getColor());

};

ice.ace.ColorEntry.instances = {}; // keep track of initialized instances

ice.ace.ColorEntry.prototype.configureLocale = function() {
    var localeSettings = ice.ace.locales[this.cfg.options.locale];

    if(localeSettings) {
        for(var setting in localeSettings) {
            this.cfg[setting] = localeSettings[setting];
        }
    }
};


ice.ace.ColorEntry.prototype.setColor = function(color) {
    this.jq.colorpicker('setColor', color);
};

ice.ace.ColorEntry.prototype.getColor= function() {
    return this.jq.colorpicker('getColor');
};

ice.ace.ColorEntry.prototype.enable = function() {
    this.jq.colorpicker('enable');
};

ice.ace.ColorEntry.prototype.disable = function() {
    this.jq.colorpicker('disable');
};

ice.ace.ColorEntry.prototype.destroy = function() {
    if (this.pickerFn) this.jq[this.pickerFn]("destroy");
    this.jq =  null;
};

ice.ace.ColorEntryInit = function( cfg) {
    ice.ace.jq().ready(function() {
        var options = cfg.options;
        var id =  options.id;
        var behaviors = cfg.behaviors || null;
        var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
        var hidden =  ice.ace.jq(ice.ace.escapeClientId(id) + "_hidden");
        var trigger=  null;
        var buttonClass="ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only";
   /*     ice.ace.jq.each(options, function(key, value) {
            console.log(key, value);
        });   */
        var showOn = options.showOn || "focus" ;
        var buttonText = options.buttonText || "";
        var buttonImage =  options.buttonImage || null;
        var buttonImageOnly = options.buttonImageOnly || false ;
        var initEltSet = ice.ace.jq();
        var inline = (options.inline) || false;
        if (inline && !options.parts){  //default inline component
            options.parts = ['preview', 'map', 'bar'];
            if (options.title){
                options.parts.push("header");
            }
            var colorFormat = options.colorFormat;
            if (colorFormat && colorFormat.indexOf("HEX") > -1){
                options.parts.push("hex");
            }
            options.part = {
                map: {size: 128},
                bar: {size: 128}
            } ;
            options.layout= {
                preview:    [0, 0, 1, 1],
                hex:        [1, 0, 1, 1],
                map:        [0, 1, 1, 1],
                bar:        [1, 1, 1, 1]
            };
            options.hsv=false;
            options.rgb = false;

        }
        /* add function callbacks to options */
        var okFn = function(event, color){ 
            var newColor = color.formatted;
             input.value = newColor;
             input.css({"border-left" : "12px solid", "border-color":newColor});
             console.log("val set to "+input.value+" format="+color.formatted);  
             if (behaviors && behaviors.change) { 
                 ice.ace.ab(behaviors.change); 
             } 
         } ;
        var selectFn = function(event, color){
            var colorFormatted = color.formatted;
            console.log(" color.formatted="+colorFormatted);
            hidden.value=colorFormatted;
            ice.ace.jq(ice.ace.escapeClientId(id) + "_hidden").val(colorFormatted);
            ice.ace.jq(ice.ace.escapeClientId(id) + "_hidden2").val(colorFormatted);
          //  console.log(" hidden val set to ="+hidden.value+" hidden2.val = "+ice.ace.jq(ice.ace.escapeClientId(id) + "_hidden2").val());
            if (behaviors && behaviors.change){
                ice.ace.ab(behaviors.change);
            }
        }
        if (inline){
            options.select = selectFn;
        }else {
            options.ok = okFn;
        }

        var create = function(){
            var widget=ice.ace.lazy("ColorEntry", [id,options]);
            ice.onElementUpdate(id, function(){
                   console.log(" colorentry onElementUpdate...destroy...");
                   widget.destroy();
                   initEltSet.remove();
            });
            ice.ace.ColorEntry.instances[id] = true;
            return widget;
        };
        var initAndShow = function(){
            console.log("initAndShow.....");
            if (ice.ace.instance(id)){
                ice.ace.instance(id).jq[ice.ace.instance(id).colorpicker]("show");
                   return;
            }
            if (trigger){
                trigger.remove();
            }
            create();
           // if(!ice.ace.instance(id).colorpicker)return;
            ice.ace.instance(id).jq[ice.ace.instance(id).colorpicker]("show");
        } ;
        if (inline){
            if (trigger) trigger.remove();
            return create();
        }


		// if instance was previously initialized, create right away and return
		if (ice.ace.ColorEntry.instances[id]) {
			create();
			return;
		}

        var buttonImageOnlyinputClass="cp-buttonImageOnly";
        if (buttonImageOnly){
            input.attr("class", buttonImageOnlyinputClass);
        }
        initEltSet = initEltSet.add(input);
        var spanSelector =  ice.ace.escapeClientId(id) ;
        var spanElement= ice.ace.jq(spanSelector);
       // if (!inline && button_both_all>=0) {
        if (!inline && ice.ace.jq.inArray(showOn, ["button","all", "both"]) >= 0) {
            console.log(" show on button and all...");
            if (buttonImageOnly){
                trigger =ice.ace.jq("<img />");
                trigger.attr("src", buttonImage);
                if (buttonText) {
                    trigger.attr("alt", buttonText);
                    trigger.attr("title", buttonText);
                }
            }  else {
                trigger=  ice.ace.jq('<button type="button"></button>').html(buttonImage == '' ? buttonText : ice.ace.jq('<img/>').attr(
                                    {src: buttonImage, alt: buttonText, title: buttonText}));
            }
            trigger.one("click", initAndShow);
            initEltSet = initEltSet.add(trigger);
            spanElement.append(trigger);
        }

        if (ice.ace.jq.inArray(showOn, ["focus","all", "both"]) >= 0) {
            setTimeout(function(){
                input.one("focus", initAndShow);
            }, 350);
          //  initEltSet = initEltSet.add(input);
        }

        ice.onElementUpdate(id, function() {
            // .remove cleans jQuery state unlike .unbind
            initEltSet.remove();
        });
    });
};

ice.ace.ColorEntry.clear = function(id, inFieldLabel, inFieldLabelStyleClass) {
    console.log(" clear");
	var instance = ice.ace.instanceNoLazyInit(id);
	if (instance) instance.setColor(null);
	var input = ice.ace.jq(ice.ace.escapeClientId(id + "_input"));
	if (inFieldLabel) {
		input.attr({name: id + "_label"});
		input.val(inFieldLabel);
		input.addClass(inFieldLabelStyleClass);
	} else {
		input.val('');
	}
};

ice.ace.ColorEntry.reset = function(id, inFieldLabel, inFieldLabelStyleClass) {
	var instance = ice.ace.instanceNoLazyInit(id);
	if (instance) {
		var value = ice.ace.resetValues[id];
		if (ice.ace.isSet(value)) instance.setColor(value);
	}
};
