/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
ice.ace.GrowlMessages = function (id, cfg) {
    var jq = ice.ace.jq, msgs = cfg.msgs,
        i, opts;

    jq.jGrowl.defaults.pool = cfg.pool;
    jq.jGrowl.defaults.closer = cfg.closer;
    jq.jGrowl.defaults.openDuration = cfg.openDuration;
    jq.jGrowl.defaults.closeDuration = cfg.closeDuration;
    jq.jGrowl.defaults.closeTemplate = "<span class='ui-icon ui-icon-circle-close'></span>";
    jq.jGrowl.defaults.closerTemplate = "<div>close all</div>";

    opts = {header: cfg.header, group: cfg.group, position: cfg.position, glue: cfg.glue, life: cfg.life};

    for (i = 0; i < msgs.length; i++) {
        opts.icon = msgs[i].icon;
        opts.themeState = msgs[i].state;
        opts.sticky = msgs[i].sticky;
        jq.jGrowl(msgs[i].text, opts);
    }

    ice.onElementUpdate(id, function () {
        var instance = jq('#jGrowl').data('jGrowl.instance');
        instance && instance.shutdown();
        jq('#jGrowl').remove();
    });
};
