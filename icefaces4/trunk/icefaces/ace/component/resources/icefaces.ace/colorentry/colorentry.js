if (!window['ice']) window.ice = {};
if (!window.ice['ace']) window.ice.ace = {};

//if (!ice.ace.ColorEntry.registry)  ice.ace.ColorEntry.registry = {};

if (!ice.ace.ColorEntrys) ice.ace.ColorEntrys = {};

ice.ace.ColorEntry = function(id, cfg) {
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
        if (options.color){
            var colorVal = options.color ;
            console.log(" color is ="+colorVal);
        }
        var self = this;

        if (ice.ace.instance(id)) {
            ice.ace.jq(input).spectrum(options);
        };

		// if instance was previously initialized, create right away and return
		if (ice.ace.ColorEntrys[id]) {
			//return;
		}


        ice.onElementUpdate(id, function() {
            // .remove cleans jQuery state unlike .unbind
            initEltSet.remove();
        });
    });
};

ice.ace.ColorEntry.prototype.attachBehaviors = function() {
    console.log(" attachBehaviors");
	var self = this;
    self.jq.on('change', function () {
        console.log(" change event!!!!!") ;
        ice.ace.ab(behavior);
    });
};