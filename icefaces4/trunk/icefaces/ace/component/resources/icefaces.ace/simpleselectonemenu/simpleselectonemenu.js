/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

ice.ace.SimpleSelectOneMenu = function(id, cfg) {
var $select = ice.ace.jq(ice.ace.escapeClientId(id)).find('select');
// dynamically set id to cause full markup updates that include this script when the select element changes
$select.attr('id', id + '_input');
$select.off('change').on('change', function(e){ e.stopPropagation(); if (cfg.behaviors && cfg.behaviors.change) ice.ace.ab(cfg.behaviors.change); });
$select.off('blur').on('blur', function(e){ e.stopPropagation(); if (cfg.behaviors && cfg.behaviors.blur) ice.ace.ab(cfg.behaviors.blur); });
};