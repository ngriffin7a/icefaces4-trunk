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
    this.options = cfg;
    if (this.options.color) { 
        this.jq.value = this.options.color; 
    } 
    //create or update colrEntry 
    if(!this.cfg.disabled) { 
        this.jq.colorpicker(this.cfg); 
    }  
    ice.ace.ColorEntry.instances[id] = this;
    ice.ace.setResetValue(this.id, cfg.color);  
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
    if (this.jq && this.jq.colorpicker){
        this.jq.colorpicker("destroy");
    }
    this.jq =  null;
};

ice.ace.ColorEntryInit = function( cfg) {
    ice.ace.jq().ready(function() {
        var options = cfg.options;
        var INPUT_STYLE_CLASS = "ui-inputfield ui-widget ui-state-default ui-corner-all ui-colorpicker-input";
        var INPUT_EMPTY_STYLE_CLASS = "ui-inputfield ui-widget ui-state-default ui-corner-all";
        var id = options.id;
        var behaviors = cfg.behaviors || null;
        var positionId = ice.ace.escapeClientId(id);
        var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
        var hidden = ice.ace.jq(ice.ace.escapeClientId(id) + "_hidden");
        var trigger = null;
        var previousColor=options.color || "";
        var colorFormat = options.colorFormat;
        var allowEmpty = options.showNoneButton;
        var showOn = options.showOn || "focus";
        var buttonText = options.buttonText || "";
        var buttonImage = options.buttonImage || null;
        var buttonImageOnly = options.buttonImageOnly || false;
        var myposition={
            of: positionId
        }
        if (buttonImageOnly){
            options.position=myposition;
        }
        var initEltSet = ice.ace.jq();
        var inline = (options.inline) || false;
        if (inline && !options.parts) {  //default inline component
            options.parts = ['preview', 'map', 'bar'];
            if (options.title) {
                options.parts.push("header");
            }

            if (colorFormat && colorFormat.indexOf("HEX") > -1) {
                options.parts.push("hex");
            }
            options.part = {
                map: {size: 128},
                bar: {size: 128}
            };
            options.layout = {
                preview: [0, 0, 1, 1],
                hex: [1, 0, 1, 1],
                map: [0, 1, 1, 1],
                bar: [1, 1, 1, 1]
            };
            options.hsv = false;
            options.rgb = false;

        }

        if (options.colorFormat){
            if (options.colorFormat == 'HEX3') {
                options.colorFormat = ['HEX3', 'HEX']
            }
            if (options.colorFormat == 'EXACT') {
                options.colorFormat = ['EXACT', 'NAME']
            }
            if (options.colorFormat == 'HSL') {
               options.colorFormat = ['HSL', 'HSLA']
            }
        }
        /* add function callbacks to options */
        function getHexValue(newColor) {
            var hexField = ice.ace.jq('.ui-colorpicker-hex-input');
            if (hexField) {
                newColor = "#" + hexField.val();
            }
            return newColor;
        } ;
        function setColorBar(color, emptyColor) {
            var newColor = color.formatted;
            var colorObj = color;
            if (allowEmpty && !color.formatted) {
                input.attr('class', INPUT_EMPTY_STYLE_CLASS);
                return;
            } else if (colorFormat.indexOf("HEX") > -1) {
                newColor = "#" + newColor;
            } else if (colorFormat.indexOf("HSL")> -1){
                newColor = getHexValue(newColor);
                var hiddenHexField =ice.ace.jq(ice.ace.escapeClientId(id + "_hiddenHex"));
                if (hiddenHexField){
                    hiddenHexField.attr('value',newColor);
                }
            }
            if (color.formatted) {
                input.attr('class',INPUT_STYLE_CLASS);
            }
            input.value = color.formatted;
            input.css({"border-left-color": newColor});
            var borderRule = "border-left-color: " + newColor + " !important";
            input.attr('style', borderRule);
        };

        var okFn = function(event, color){ 
         //   console.log(" OK fn");
            if (!color.formatted || color.formatted=="false"){
                return;
            }
            var emptyColor="#f2eaea";
            ice.ace.setResetValue(this.id, color.formatted);  
            if (allowEmpty){
                emptyColor = input.css("background-color") ;
            }
            setColorBar(color, emptyColor);
            if (behaviors && behaviors.change) { 
                var inputChange = behaviors.change;
                ice.ace.ab(inputChange); 
            } 
         } ;
        var cancelFn = function(event, color){
          //  console.log(" cancel fn");
            if (!ice.ace.resetValues[id]){
                input.attr('class', INPUT_EMPTY_STYLE_CLASS);
            } else {
                color.formatted=ice.ace.resetValues[id];
                setColorBar(color, ice.ace.resetValues[id])
            }
        } ;
        var selectFn = function(event, color){
        //    console.log(" Select function");
            if (!color.formatted || color.formatted=="false"){
                console.log(" The current widget does not support the color format of "+colorFormat);
                return;
            }
            var colorFormatted = color.formatted;
            if (!inline){  //set
                var emptyColor = input.css("background-color") ;
                setColorBar(color, emptyColor);
                return;
            }
            ice.ace.setResetValue(this.id, color.formatted);  
            hidden.value = colorFormatted;
            ice.ace.jq(ice.ace.escapeClientId(id) + "_hidden").val(colorFormatted);
            ice.ace.jq(ice.ace.escapeClientId(id) + "_hidden2").val(colorFormatted);
            if (behaviors && behaviors.change) {
                ice.ace.ab(behaviors.change);
            }
        };
        if (!inline){
            options.ok = okFn;
        }
        options.select = selectFn;
        options.cancel=cancelFn;

        var create = function(){
          //  console.log(" create.....");
            var widget=ice.ace.create("ColorEntry", [id,options]);
            ice.onElementUpdate(id, function(){
                //   console.log(" colorentry onElementUpdate...destroy...");
                   widget.destroy();
                   initEltSet.remove();
            });
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
                ice.ace.instance(id).jq[ice.ace.instance(id).colorpicker]("show");
            }
        } ;
        if (inline){
            if (trigger) trigger.remove();
            return create();
        }

		// if instance was previously initialized, create right away and return
		if (ice.ace.ColorEntry.instances[id]) {
            var widget = ice.ace.ColorEntry.instances[id];
             if (widget){
             //    console.log(" destroying existing widget");
                 widget.destroy();
             } /* else {
                 console.log(" no widget to destroy....");
             } */
            create();
			return;
		}
      //  console.log(" rest of stuff is for non  inline");
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
