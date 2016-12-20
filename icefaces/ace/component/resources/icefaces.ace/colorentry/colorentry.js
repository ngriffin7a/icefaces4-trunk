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
    // i18n and l7n   //  this.configureLocale(); 
    this.options = cfg;
    this.pickerFn = "colorpicker"; 
    if (this.options.color) { 
        this.jq.value = this.options.color; 
    } 
    //create or update colrEntry 
    if(!this.cfg.disabled) { 
        ice.ace.ColorEntry.instances[id] = this.jq.colorpicker(this.cfg); 
        if (this.cfg.title){ 
            this.jq.colorpicker("option", "showOptions", {title: this.cfg.title}); 
        } 
    }  
    ice.ace.setResetValue(this.id, this.getColor());  
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
        var INPUT_STYLE_CLASS = "ui-inputfield ui-widget ui-state-default ui-corner-all ui-colorpicker-input";
        var INPUT_EMPTY_STYLE_CLASS ="ui-inputfield ui-widget ui-state-default ui-corner-all";
        var id =  options.id;
        var behaviors = cfg.behaviors || null;
        var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
        var hidden =  ice.ace.jq(ice.ace.escapeClientId(id) + "_hidden");
        var trigger=  null;
        var colorFormat = options.colorFormat;
        var allowEmpty = options.showNoneButton;
        console.log(" allowEmpty!");
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
            var emptyColor="#f2eaea";
            if (allowEmpty){
                emptyColor = input.css("background-color") ;
            }
            else if (!color.formatted || color.formatted=="false"){
                console.log("OK has been pressed but "+colorFormat+" color format is not supported in this widget");
                return;
            }
            var newColor = color.formatted;
            if (allowEmpty  && !color.formatted){
                newColor = emptyColor;
                input.attr('css', INPUT_EMPTY_STYLE_CLASS) ;
            }else if (colorFormat.indexOf("HEX")>-1){
                newColor="#"+ newColor;
            }
            if (color.formatted){
                input.attr('css', INPUT_STYLE_CLASS) ;
            }
            input.value = color.formatted;

            console.log(" ok fn newColor="+newColor);
            input.css({"border-left-color":newColor});
            var borderRule = "border-left-color: "+newColor+" !important";
            input.attr('style', borderRule);
            if (behaviors && behaviors.change) { 
                ice.ace.ab(behaviors.change); 
            } 
         } ;
        var selectFn = function(event, color){
            if (!color.Formatted){
                console.log(" The current widget does not support the color format of "+colorFormat);
            }
            var colorFormatted = color.formatted;
            console.log(" color.formatted="+colorFormatted);
            hidden.value = colorFormatted;
            ice.ace.jq(ice.ace.escapeClientId(id) + "_hidden").val(colorFormatted);
            ice.ace.jq(ice.ace.escapeClientId(id) + "_hidden2").val(colorFormatted);
            if (behaviors && behaviors.change){
                ice.ace.ab(behaviors.change);
            }
        };
        if (inline){
            options.select = selectFn;
        }else {
            options.ok = okFn;
          //  options.select = selectFn;
        }

        var create = function(){
            console.log(" create.....");
            var widget=ice.ace.create("ColorEntry", [id,options]);
            ice.onElementUpdate(id, function(){
                //   console.log(" colorentry onElementUpdate...destroy...");
                   widget.destroy();
                   initEltSet.remove();
            });
          //  ice.ace.ColorEntry.instances[id] = true;
            return widget;
        };
        var initAndShow = function(){
            console.log("initAndShow.....");
            if (trigger){
                trigger.remove();
            }
            create();
            if ( ice.ace.instance(id).colorpicker){
                console.log(" showing......");
                ice.ace.instance(id).jq[ice.ace.instance(id).colorpicker]("show");
            }
        };
        var initButtonAndShow = function(){

            if (trigger){
                console.log(" removing trigger");
                trigger.remove();
            }
            create();
            if (ice.ace.instance(id).colorpicker){
                console.log("showing");
                ice.ace.instance(id).jq[ice.ace.instance(id).colorpicker]("show");
            }else {
                console.log(" no ice.ace.instance of colorpicker for this id");
            }
        } ;
        if (inline){
            if (trigger) trigger.remove();
            return create();
        }

		// if instance was previously initialized, create right away and return
		if (ice.ace.ColorEntry.instances[id]) {
            console.log(" widget previously initialized, so create right away and return!");
            create();
			return;
		}
        console.log(" rest of stuff is for non  inline");
        var buttonImageOnlyinputClass="cp-buttonImageOnly";
        if (buttonImageOnly){
            input.attr("class", buttonImageOnlyinputClass);
        }
        initEltSet = initEltSet.add(input);
        var spanSelector =  ice.ace.escapeClientId(id) ;
        var spanElement= ice.ace.jq(spanSelector);
        if (!inline && ice.ace.jq.inArray(showOn, ["button","all", "both"]) >= 0) {
            if (buttonImageOnly) {
                trigger = ice.ace.jq("<img />");
                trigger.attr("src", buttonImage);
                if (buttonText) {
                    trigger.attr("alt", buttonText);
                    trigger.attr("title", buttonText);
                }
                setTimeout(function(){
                    trigger.one("click", initButtonAndShow());
                }, 350);
                initEltSet = initEltSet.add(trigger);
            }
        }

        if (ice.ace.jq.inArray(showOn, ["focus","all", "both"]) >= 0) {
            setTimeout(function(){
             //   console.log(" within setTimeout....");
                input.one("focus", initAndShow);
            }, 350);
            initEltSet = initEltSet.add(input);
        }

        ice.onElementUpdate(id, function() {
            // .remove cleans jQuery state unlike .unbind
            initEltSet.remove();
        });
    });
};

ice.ace.ColorEntry.clear = function(id, inFieldLabel, inFieldLabelStyleClass) {
   // console.log(" clear");
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
