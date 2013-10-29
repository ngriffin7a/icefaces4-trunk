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
(function ($, undefined) {

    var Message;
    ice.ace.Message = function (opts) {
        var effOpts = {};
        this.opts = opts;
        this.$msg = $(document.getElementById(opts.id + "_msg" + (opts.count || "")));
        if (opts.effect == "fade") {
            this.$msg.hide();
        } else if (opts.effect == "size") {
            effOpts.from = {width: 0, height: 0};
        }
        this.$msg.show(opts.effect, effOpts, opts.duration);
    };
    Message = ice.ace.Message;
    Message.prototype.initEffect = function () {
    };
    Message.prototype.changeEffect = function () {
    };
    Message.prototype.destroy = function () {
        this.$msg.remove();
    };

    Message.instances = {};
    Message.factory = function (opts) {
        $(function () {
            var id = opts.id,
                instances = Message.instances;
            instances[id] = new Message(opts);
//            instances[id][opts.event + "Effect"]();
            ice.onElementUpdate(id, function () {
                instances[id].destroy();
                instances[id] = null;
                delete instances[id];
            });
        });
    }

}(ice.ace.jq));
