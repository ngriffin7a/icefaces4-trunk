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

    //Client behaviors and input skinning
  /*  if(this.options.popup) {
        if(this.options.behaviors) {
            ice.ace.attachBehaviors(this.jq, this.options.behaviors);
        } */

        //Visuals
   /*     if(this.options.popup && this.options.theme != false) {
            ice.ace.util.bindHoverFocusStyle(this.jq);
        }
        behavior = this.cfg && this.cfg.behaviors && this.cfg.behaviors.valueChange;
    }   */
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
        var trigger = null;
   /*     ice.ace.jq.each(options, function(key, value) {
            console.log(key, value);
        });   */
        var showOn = options.showOn || 'both';
        var buttonText = options.buttonText || null;
        var buttonImage = options.buttonImage || '\/colorpicker\/javax.faces.resource\/colorentry\/ui-colorpicker.png.jsf?ln=icefaces.ace&v=4_2_0_161018';
        var buttonImageOnly = options.buttonImageOnly || false ;
        var initEltSet = ice.ace.jq();
        var inline = (options.parts == 'inline') || false;

        /* add function callbacks to options */
        var okFn = function(event, color){ 
             input.value = color.formatted;
             console.log("val set to "+input.value+" format="+color.formatted);  
             if (behaviors && behaviors.change) { 
                 ice.ace.ab(behaviors.change); 
             } 
         } ;

        options.ok = okFn; 

        var create = function(){
            if (!ice.ace.ColorEntry.instances[id]){
                var widget=ice.ace.lazy("ColorEntry", [id,options]);
            }else {
                var widget = ice.ace.create("ColorEntry", [id, options]);
            }

            ice.onElementUpdate(id, function(){
                   console.log(" colorentry onElementUpdate...destroy...");
                   widget.destroy();
                   initEltSet.remove();
            });
            ice.ace.ColorEntry.instances[id] = true;
            return widget;
        };
        var initAndShow = function(){
            if (ice.ace.instance(id)){
                ice.ace.instance(id).jq[ice.ace.instance(id).colorpicker]("show");
                   return;
            }
            if (trigger){
                trigger.remove();
            }
            create();
            if(!ice.ace.instance(id).colorpicker)return;
            ice.ace.instance(id).jq[ice.ace.instance(id).colorpicker]("show");
        } ;
        if (inline){
            if (trigger) trigger.remove();
            return create();
        }
        if (!options.popup) {
            create();
            return;
        }

		// if instance was previously initialized, create right away and return
		if (ice.ace.ColorEntry.instances[id]) {
			create();
			return;
		}

		ice.ace.lazy.registry[id] = function() {
			if (trigger) trigger.remove();
			return create();
		};

        initEltSet = initEltSet.add(input);

        if (!inline && ice.ace.jq.inArray(showOn, ["button","all"]) >= 0) {
            console.log(" show on button and all..." );
            trigger = buttonImageOnly ?
                ice.ace.jq('<img/>').addClass(triggerClass).
                    attr({ src: buttonImage, alt: buttonText, title: buttonText }) :
                ice.ace.jq('<button type="button"></button>').addClass(triggerClass).
                    html(buttonImage == '' ? buttonText : ice.ace.jq('<img/>').attr(
                    { src:buttonImage, alt:buttonText, title:buttonText }));
           // input[isRTL ? 'before' : 'after'](trigger);
            trigger.one("click", initAndShow);
            initEltSet = initEltSet.add(trigger);
        }
        if (ice.ace.jq.inArray(showOn, ["focus","all"]) >= 0) {
            setTimeout(function(){
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
