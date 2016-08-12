if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};

//if (!ice.ace.Colorpicker.registry)  ice.ace.Colorpicker.registry = {};

if (!ice.ace.ColorPickers) ice.ace.Colorpickers = {};

ice.ace.ColorPicker = function(id, cfg) {
    ice.ace.jq().ready(function() {
        var pickerId = ice.ace.escapeClientId(id);
        this.jq = ice.ace.jq(pickerId);
        var options= cfg.options;
        var changeFn = function(){console.log(" change fn")} ;
        if (cfg.behaviors) {
            var changeFnvals = cfg.behaviors;
            changeFn = function()
            {
                var self = this;
              /*  self.jq.on('change', function () {
                    console.log(" change event!!!!!"); */
                    ice.ace.ab(cfg.behaviors.change);
               // });
            }
            options.change=changeFn;
        }


        var input = ice.ace.jq(ice.ace.escapeClientId(id) + "_input");
        var colorVal = options.color || "#f00";
        var self = this;

        console.log(" color is ="+colorVal);
        if (ice.ace.instance(id)) {
            ice.ace.jq(input).spectrum(options);
        };

		// if instance was previously initialized, create right away and return
		if (ice.ace.Colorpickers[id]) {
			//return;
		}


        ice.onElementUpdate(id, function() {
            // .remove cleans jQuery state unlike .unbind
            initEltSet.remove();
        });
    });
};

ice.ace.ColorPicker.prototype.attachBehaviors = function() {
    console.log(" attachBehaviors");
	var self = this;
    self.jq.on('change', function () {
        console.log(" change event!!!!!") ;
        ice.ace.ab(behavior);
    });
};