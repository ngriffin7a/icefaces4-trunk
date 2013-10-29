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

if (!window.ice['ace']) {
    window.ice.ace = {};
}
if (!ice.ace.Charts) ice.ace.Charts = {};
ice.ace.Chart = function (id, data, cfg) {
    var self = this;
    this.id = id;
    this.jqId = ice.ace.escapeClientId(this.id);
    this.cfg  = cfg;
    this.behaviors = cfg.behaviors;
    this.chart_region = ice.ace.jq(this.jqId+'_chart');


    // Clear existing ace plot instance.
    if (ice.ace.Charts[id]) {
        if (ice.ace.Charts[id].plot)
            ice.ace.Charts[id].plot.destroy();
        else // clean up error message that is probably present if plot is not present
            this.chart_region.html('');
    }

    ice.ace.jq.jqplot.config.catchErrors = true;
    ice.ace.jq.jqplot.config.errorBorder = '1px solid #aaaaaa';
    this.plot = ice.ace.jq.jqplot(this.jqId.substring(1)+'_chart', data, cfg);
    ice.ace.Charts[id] = self;
    var $this = ice.ace.jq(this.jqId);

    if (cfg.handlePointClick)
        $this.off("jqplotDataClick").on(
                "jqplotDataClick",
                function(e, seriesIndex, pointIndex, data) {
                    self.handlePointClick.call(self, e, seriesIndex, pointIndex, data);
                }
        );

    if (cfg.behaviors && cfg.behaviors.mouseOutData) {
        $this.off("jqplotDataHighlight").on(
                "jqplotDataHighlight",
                function() {
                    ice.ace.ab(self.behaviors.mouseOutData);
                }
        );
    }

    if (cfg.behaviors && cfg.behaviors.mouseInData) {
        $this.off("jqplotDataUnhighlight").on(
                "jqplotDataUnhighlight",
                function() {
                    ice.ace.ab(self.behaviors.mouseInData);
                }
        );
    }

    if (cfg.behaviors && cfg.behaviors.showHighlighter) {
        $this.off("jqplotHighlighterHighlight").on(
                "jqplotHighlighterHighlight",
                function() {
                    ice.ace.ab(self.behaviors.showHighlighter);
                }
        )
    }

    if (cfg.behaviors && cfg.behaviors.hideHighlighter) {
        $this.off("jqplotHighlighterUnhighlight").on(
                "jqplotHighlighterUnhighlight",
                function() {
                    ice.ace.ab(self.behaviors.hideHighlighter);
                }
        )
    }

    $this.off("jqplotDragStart").on(
            "jqplotDragStart",
            function(e, seriesIndex, pointIndex, data) {
                self.handleDragStart.call(self, e, seriesIndex, pointIndex, data);
            }
    );

    $this.off("jqplotDragStop").on(
            "jqplotDragStop",
            function(e, seriesIndex, pointIndex, data) {
                self.handleDragStop.call(self, e, seriesIndex, pointIndex, data);
            }
    );

    var _self = this,
        replotWhenVis = function () {
            if (!_self.chart_region.is(':hidden')) {
                _self.plot.replot();
            } else
                setTimeout(replotWhenVis, 400);
        };

    if (!this.cfg.disableHiddenInit && this.chart_region.is(':hidden')) {
        setTimeout(replotWhenVis, 400);
    }
};


ice.ace.Chart.prototype.handleDragStart = function(e, seriesIndex, pointIndex, data) {
    var toCategoryValue = function(indexVal, axis) { return (axis.ticks[Math.round(indexVal) - 1]); };

    var options = {
        source: this.id,
        execute: '@this',
        render: (this.behaviors && this.behaviors.dragStart && this.behaviors.dragStart.render) ? '' : '@none'
    };

    var params = {};
    // Persist point drag info for stop event.
    this.dragXAxis = this.plot.series[seriesIndex]._xaxis;
    this.dragYAxis = this.plot.series[seriesIndex]._yaxis;
    this.dragSeriesIndex = seriesIndex;
    this.dragPointIndex = pointIndex;

    options.params = params;

    var pointData = this.plot.series[seriesIndex]._plotData[pointIndex].slice(),
        xValueIsCategory = this.dragXAxis.renderer instanceof ice.ace.jq.jqplot.CategoryAxisRenderer,
        yValueIsCategory = this.dragYAxis.renderer instanceof ice.ace.jq.jqplot.CategoryAxisRenderer;

    if (xValueIsCategory)
        pointData[0] = toCategoryValue(pointData[0], this.dragXAxis);

    if (yValueIsCategory)
        pointData[1] = toCategoryValue(pointData[1], this.dragYAxis);

    // Record start value
    this.dragStartValue = pointData;

    // Call behaviours
    if (this.behaviors)
        if (this.behaviors.dragStart) {
            ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.dragStart,options));
            return;
        }
};

ice.ace.Chart.prototype.handleDragStop = function(e, seriesIndex, pointIndex, data) {
    var toCategoryValue = function(indexVal, axis) { return (axis.ticks[Math.round(indexVal) - 1]); };

    var options = {
        source: this.id,
        execute: '@this',
        render: (this.behaviors && this.behaviors.dragStop && this.behaviors.dragStop.render) ? '' : '@none' };

    var params = {};

    var pointData = [pointIndex[this.dragXAxis.name],pointIndex[this.dragYAxis.name]],
        xValueIsCategory = this.dragXAxis.renderer instanceof ice.ace.jq.jqplot.CategoryAxisRenderer,
        yValueIsCategory = this.dragYAxis.renderer instanceof ice.ace.jq.jqplot.CategoryAxisRenderer;

    if (xValueIsCategory)
        pointData[0] = toCategoryValue(pointData[0], this.dragXAxis);

    if (yValueIsCategory)
        pointData[1] = toCategoryValue(pointData[1], this.dragYAxis);

    var pointDataDiffers = pointData[0] != this.dragStartValue[0] ||
            pointData[1] != this.dragStartValue[1];

    if (pointDataDiffers) {
       var dragRecord = [];
       dragRecord.push([[pointData[0], pointData[1]], this.dragSeriesIndex, this.dragPointIndex]);
       params[this.id+"_drag"] = JSON.stringify(dragRecord);
    }

    options.params = params;

    if (this.behaviors)
        if (this.behaviors.dragStop) {
            ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.dragStop,options));
            return;
        }

    if (pointDataDiffers)
        ice.ace.AjaxRequest(options);
};

ice.ace.Chart.prototype.handlePointClick = function(e, seriesIndex, pointIndex, data) {
    var options = {
            source: this.id,
            execute: '@this',
            render: '@this'
        };

    var params = {};
    params[this.id+'_selection'] = data;
    options.params = params;

    if (this.behaviors)
        if (this.behaviors.click) {
            ice.ace.ab(ice.ace.extendAjaxArgs(this.behaviors.click, options));
            return;
        }

    ice.ace.AjaxRequest(options);
}

ice.ace.Chart.prototype.downloadAsImage = function() {
    this.chart_region.jqplotSaveImage();
}

ice.ace.Chart.prototype.exportToImage = function(img) {
    ice.ace.jq(img).attr('src',
        this.chart_region.jqplotToImageStr());
}

ice.ace.Chart.prototype.exportToServer = function() {
    var options = {
            source: this.id,
            execute: '@this',
            render: '@this'
        };

    var params = {};
    params[this.id+'_export'] = this.chart_region.jqplotToImageStr();
    options.params = params;

    ice.ace.AjaxRequest(options);
}
