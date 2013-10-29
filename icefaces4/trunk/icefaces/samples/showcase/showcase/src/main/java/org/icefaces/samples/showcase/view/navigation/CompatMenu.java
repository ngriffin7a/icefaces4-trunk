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

package org.icefaces.samples.showcase.view.navigation;

import org.icefaces.samples.showcase.example.compat.autocomplete.AutocompleteBean;
import org.icefaces.samples.showcase.example.compat.border.BorderBean;
import org.icefaces.samples.showcase.example.compat.calendar.CalendarBean;
import org.icefaces.samples.showcase.example.compat.overview.IceSuiteOverviewBean;
import org.icefaces.samples.showcase.example.compat.chart.ChartBean;
import org.icefaces.samples.showcase.example.compat.collapsible.CollapsibleBean;
import org.icefaces.samples.showcase.example.compat.columns.ColumnsBean;
import org.icefaces.samples.showcase.example.compat.confirmation.ConfirmationBean;
import org.icefaces.samples.showcase.example.compat.connectionStatus.ConnectionStatus;
import org.icefaces.samples.showcase.example.compat.dataTable.DataTableBean;
import org.icefaces.samples.showcase.example.compat.divider.DividerBean;
import org.icefaces.samples.showcase.example.compat.dragdrop.DragDropBean;
import org.icefaces.samples.showcase.example.compat.effect.EffectBean;
import org.icefaces.samples.showcase.example.compat.eventphase.EventPhaseBean;
import org.icefaces.samples.showcase.example.compat.exporter.ExporterBean;
import org.icefaces.samples.showcase.example.compat.map.MapBean;
import org.icefaces.samples.showcase.example.compat.media.MediaBean;
import org.icefaces.samples.showcase.example.compat.menuBar.MenuBarBean;
import org.icefaces.samples.showcase.example.compat.menuPopup.MenuPopupBean;
import org.icefaces.samples.showcase.example.compat.outputResource.OutputResourceBean;
import org.icefaces.samples.showcase.example.compat.paginator.PaginatorBean;
import org.icefaces.samples.showcase.example.compat.popup.PopupBean;
import org.icefaces.samples.showcase.example.compat.positioned.PositionedBean;
import org.icefaces.samples.showcase.example.compat.progress.ProgressBean;
import org.icefaces.samples.showcase.example.compat.richtext.RichTextBean;
import org.icefaces.samples.showcase.example.compat.tab.TabBean;
import org.icefaces.samples.showcase.example.compat.tooltip.TooltipBean;
import org.icefaces.samples.showcase.example.compat.tree.TreeBean;
import org.icefaces.samples.showcase.example.compat.selector.SelectorBean;
import org.icefaces.samples.showcase.example.compat.series.SeriesBean;
import org.icefaces.samples.showcase.example.compat.stacking.StackingBean;
import org.icefaces.samples.showcase.example.compat.graphicimage.GraphicImageBean;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@Menu(
        title = "menu.compat.title", menuLinks = {
            @MenuLink(title = "menu.compat.iceSuiteOverview.title", isDefault = true, exampleBeanName = IceSuiteOverviewBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.columns.title", exampleBeanName = ColumnsBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.exporter.title", exampleBeanName = ExporterBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.paginator.title", exampleBeanName = PaginatorBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.dataTable.title", exampleBeanName = DataTableBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.map.title", exampleBeanName = MapBean.BEAN_NAME),
			@MenuLink(title = "menu.compat.graphicimage.title", exampleBeanName = GraphicImageBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.richtext.title", exampleBeanName = RichTextBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuBar.title", exampleBeanName = MenuBarBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.menuPopup.title", exampleBeanName = MenuPopupBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.chart.title", exampleBeanName = ChartBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.connectionStatus.title", exampleBeanName = ConnectionStatus.BEAN_NAME),
            @MenuLink(title = "menu.compat.media.title", exampleBeanName = MediaBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.progress.title", exampleBeanName = ProgressBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.outputResource.title", exampleBeanName = OutputResourceBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.border.title", exampleBeanName = BorderBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.collapsible.title", exampleBeanName = CollapsibleBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.confirmation.title", exampleBeanName = ConfirmationBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.divider.title", exampleBeanName = DividerBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.dragdrop.title", exampleBeanName = DragDropBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.effect.title", exampleBeanName = EffectBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.popup.title", exampleBeanName = PopupBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.positioned.title", exampleBeanName = PositionedBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.series.title", exampleBeanName = SeriesBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.stacking.title", exampleBeanName = StackingBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.tab.title", exampleBeanName = TabBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.tooltip.title", exampleBeanName = TooltipBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.selector.title", exampleBeanName = SelectorBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.calendar.title", exampleBeanName = CalendarBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.autocomplete.title", exampleBeanName = AutocompleteBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.eventphase.title", exampleBeanName = EventPhaseBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.tree.title", exampleBeanName = TreeBean.BEAN_NAME)
        })
@ManagedBean(name = CompatMenu.BEAN_NAME)
@ApplicationScoped
public class CompatMenu extends org.icefaces.samples.showcase.metadata.context.Menu<CompatMenu>
        implements Serializable {

    public static final String BEAN_NAME = "compatMenu";

    public CompatMenu() {
        super(CompatMenu.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getBeanName() {
        return BEAN_NAME;
    }
}
