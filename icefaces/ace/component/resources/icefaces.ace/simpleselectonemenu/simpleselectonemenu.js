ice.ace.SimpleSelectOneMenu = function(id, cfg) {
var $select = ice.ace.jq(ice.ace.escapeClientId(id)).find('select');
// dynamically set id to cause full markup updates that include this script when the select element changes
$select.attr('id', id + '_input');
$select.off('change').on('change', function(e){ e.stopPropagation(); if (cfg.behaviors && cfg.behaviors.change) ice.ace.ab(cfg.behaviors.change); });
$select.off('blur').on('blur', function(e){ e.stopPropagation(); if (cfg.behaviors && cfg.behaviors.blur) ice.ace.ab(cfg.behaviors.blur); });
};