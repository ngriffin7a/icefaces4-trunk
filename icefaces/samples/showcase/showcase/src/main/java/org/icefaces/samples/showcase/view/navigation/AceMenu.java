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

import org.icefaces.samples.showcase.example.ace.accordionpanel.AccordionPanelBean;
import org.icefaces.samples.showcase.example.ace.animation.AnimationBean;
import org.icefaces.samples.showcase.example.ace.autocompleteentry.AutoCompleteEntryBean;
import org.icefaces.samples.showcase.example.ace.breadcrumbmenu.BreadcrumbMenuBean;
import org.icefaces.samples.showcase.example.ace.buttonGroup.ButtonGroupBean;
import org.icefaces.samples.showcase.example.ace.chart.ChartBean;
import org.icefaces.samples.showcase.example.ace.checkboxButton.CheckboxButtonBean;
import org.icefaces.samples.showcase.example.ace.combobox.ComboBoxBean;
import org.icefaces.samples.showcase.example.ace.confirmationdialog.ConfirmationDialogBean;
import org.icefaces.samples.showcase.example.ace.contextMenu.ContextMenuBean;
import org.icefaces.samples.showcase.example.ace.dataExporter.DataExporterBean;
import org.icefaces.samples.showcase.example.ace.dataTable.DataTableBean;
import org.icefaces.samples.showcase.example.ace.date.DateEntryBean;
import org.icefaces.samples.showcase.example.ace.dialog.DialogBean;
import org.icefaces.samples.showcase.example.ace.dragDrop.DragDropOverviewBean;
import org.icefaces.samples.showcase.example.ace.file.FileEntryBean;
import org.icefaces.samples.showcase.example.ace.gMap.MapBean;
import org.icefaces.samples.showcase.example.ace.linkButton.LinkButtonBean;
import org.icefaces.samples.showcase.example.ace.list.ListBean;
import org.icefaces.samples.showcase.example.ace.maskedEntry.MaskedEntryBean;
import org.icefaces.samples.showcase.example.ace.menu.MenuBean;
import org.icefaces.samples.showcase.example.ace.menuBar.MenuBarBean;
import org.icefaces.samples.showcase.example.ace.menuButton.MenuButtonBean;
import org.icefaces.samples.showcase.example.ace.message.MessageBean;
import org.icefaces.samples.showcase.example.ace.growlmessages.GrowlMessagesBean;
import org.icefaces.samples.showcase.example.ace.notificationpanel.NotificationPanelBean;
import org.icefaces.samples.showcase.example.ace.overview.AceSuiteOverviewBean;
import org.icefaces.samples.showcase.example.ace.panel.PanelBean;
import org.icefaces.samples.showcase.example.ace.printer.PrinterBean;
import org.icefaces.samples.showcase.example.ace.progressbar.ProgressBarBean;
import org.icefaces.samples.showcase.example.ace.pushButton.PushButtonBean;
import org.icefaces.samples.showcase.example.ace.radioButton.RadioButtonBean;
import org.icefaces.samples.showcase.example.ace.resizable.ResizableBean;
import org.icefaces.samples.showcase.example.ace.richtextentry.RichTextEntryBean;
import org.icefaces.samples.showcase.example.ace.selectmenu.SelectMenuBean;
import org.icefaces.samples.showcase.example.ace.simpleselectonemenu.SimpleSelectOneMenuBean;
import org.icefaces.samples.showcase.example.ace.slider.SliderBean;
import org.icefaces.samples.showcase.example.ace.submitMonitor.SubmitMonitorBean;
import org.icefaces.samples.showcase.example.ace.tab.TabSetBean;
import org.icefaces.samples.showcase.example.ace.textAreaEntry.TextAreaEntryBean;
import org.icefaces.samples.showcase.example.ace.textEntry.TextEntryBean;
import org.icefaces.samples.showcase.example.ace.tooltip.TooltipOverviewBean;
import org.icefaces.samples.showcase.example.ace.tree.TreeBean;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@Menu(
        title = "menu.ace.title", menuLinks = {
                @MenuLink(title = "menu.ace.aceSuiteOverview.title", isDefault = true, exampleBeanName = AceSuiteOverviewBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.accordionpanel.title", exampleBeanName = AccordionPanelBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.animation.title", exampleBeanName = AnimationBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.autocompleteentry.title", exampleBeanName = AutoCompleteEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.breadcrumbmenu.title", exampleBeanName = BreadcrumbMenuBean.BEAN_NAME),
				@MenuLink(title = "menu.ace.buttonGroup.title", exampleBeanName = ButtonGroupBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.chart.title", exampleBeanName = ChartBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.checkboxButton.title", exampleBeanName = CheckboxButtonBean.BEAN_NAME),
				@MenuLink(title = "menu.ace.combobox.title", exampleBeanName = ComboBoxBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.confirmationdialog.title", exampleBeanName = ConfirmationDialogBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.contextMenu.title", exampleBeanName = ContextMenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dataExporter.title", exampleBeanName = DataExporterBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dataTable.title", exampleBeanName = DataTableBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dateentry.title", exampleBeanName = DateEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dialog.title", exampleBeanName = DialogBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dragDrop.title", exampleBeanName = DragDropOverviewBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.fileentry.title", exampleBeanName = FileEntryBean.BEAN_NAME),
				@MenuLink(title = "menu.ace.gMap.title", exampleBeanName = MapBean.BEAN_NAME),
//@MenuLink(title = "menu.ace.logger.title", exampleBeanName = LoggerBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.growlmessages.title", exampleBeanName = GrowlMessagesBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.linkButton.title", exampleBeanName = LinkButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.list.title", exampleBeanName = ListBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.maskedEntry.title", exampleBeanName = MaskedEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.menu.title", exampleBeanName = MenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.menuBar.title", exampleBeanName = MenuBarBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.menuButton.title", exampleBeanName = MenuButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.message.title", exampleBeanName = MessageBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.notificationpanel.title", exampleBeanName = NotificationPanelBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.panel.title", exampleBeanName = PanelBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.printer.title", exampleBeanName = PrinterBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.progressbar.title", exampleBeanName = ProgressBarBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.pushButton.title", exampleBeanName = PushButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.radioButton.title", exampleBeanName = RadioButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.resizable.title", exampleBeanName = ResizableBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.richtextentry.title", exampleBeanName = RichTextEntryBean.BEAN_NAME),
				@MenuLink(title = "menu.ace.selectmenu.title", exampleBeanName = SelectMenuBean.BEAN_NAME),
				@MenuLink(title = "menu.ace.simpleselectonemenu.title", exampleBeanName = SimpleSelectOneMenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.slider.title", exampleBeanName = SliderBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.submitMonitor.title", exampleBeanName = SubmitMonitorBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tabSet.title", exampleBeanName = TabSetBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.textEntry.title", exampleBeanName = TextEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.textAreaEntry.title", exampleBeanName = TextAreaEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tooltip.title", exampleBeanName = TooltipOverviewBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tree.title", exampleBeanName = TreeBean.BEAN_NAME)
        })
@ManagedBean(name = AceMenu.BEAN_NAME)
@ApplicationScoped
public class AceMenu extends org.icefaces.samples.showcase.metadata.context.Menu<AceMenu>
        implements Serializable {

    public static final String BEAN_NAME = "aceMenu";

    public AceMenu() {
        super(AceMenu.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getBeanName() {
        return BEAN_NAME;
    }
}
