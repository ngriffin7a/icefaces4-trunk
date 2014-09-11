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

import org.icefaces.samples.showcase.example.ace.ajax.AjaxBean;
import org.icefaces.samples.showcase.example.ace.accordionpanel.AccordionPanelBean;
import org.icefaces.samples.showcase.example.ace.accordionpanel.AccordionPanelDynamicBean;
import org.icefaces.samples.showcase.example.ace.accordionpanel.AccordionPanelEffectBean;
import org.icefaces.samples.showcase.example.ace.animation.AnimationBean;
import org.icefaces.samples.showcase.example.ace.audioPlayer.AudioPlayerBean;
import org.icefaces.samples.showcase.example.ace.autocompleteentry.*;
import org.icefaces.samples.showcase.example.ace.breadcrumbmenu.BreadcrumbMenuBean;
import org.icefaces.samples.showcase.example.ace.buttonGroup.ButtonGroupBean;
import org.icefaces.samples.showcase.example.ace.chart.*;
import org.icefaces.samples.showcase.example.ace.checkboxButton.CheckboxButtonBean;
import org.icefaces.samples.showcase.example.ace.checkboxButton.CheckboxButtonCustomBean;
import org.icefaces.samples.showcase.example.ace.combobox.*;
import org.icefaces.samples.showcase.example.ace.confirmationdialog.ConfirmationDialogBean;
import org.icefaces.samples.showcase.example.ace.confirmationdialog.ConfirmationDialogEffectBean;
import org.icefaces.samples.showcase.example.ace.confirmationdialog.ConfirmationDialogModalBean;
import org.icefaces.samples.showcase.example.ace.contextMenu.*;
import org.icefaces.samples.showcase.example.ace.dataExporter.*;
import org.icefaces.samples.showcase.example.ace.dataTable.*;
import org.icefaces.samples.showcase.example.ace.cellEditor.CellEditorBean;
import org.icefaces.samples.showcase.example.ace.columnGroup.ColumnGroupBean;
import org.icefaces.samples.showcase.example.ace.rowExpansion.RowExpansionBean;
import org.icefaces.samples.showcase.example.ace.tableConfigPanel.TableConfigPanelBean;
import org.icefaces.samples.showcase.example.ace.date.*;
import org.icefaces.samples.showcase.example.ace.dialog.DialogBean;
import org.icefaces.samples.showcase.example.ace.dialog.DialogEffectsAndSizeBean;
import org.icefaces.samples.showcase.example.ace.dialog.ModalDialogBean;
import org.icefaces.samples.showcase.example.ace.dragDrop.DataTableIntegrationBean;
import org.icefaces.samples.showcase.example.ace.dragDrop.DragDropOverviewBean;
import org.icefaces.samples.showcase.example.ace.dragDrop.DraggableOverviewBean;
import org.icefaces.samples.showcase.example.ace.dynamicResource.DynamicResourceBean;
import org.icefaces.samples.showcase.example.ace.file.FileEntryBean;
import org.icefaces.samples.showcase.example.ace.file.FileEntryCallbackBean;
import org.icefaces.samples.showcase.example.ace.file.FileEntryListenerBean;
import org.icefaces.samples.showcase.example.ace.file.FileEntryValidationOptionsBean;
import org.icefaces.samples.showcase.example.ace.gMap.*;
import org.icefaces.samples.showcase.example.ace.graphicImage.GraphicImageBean;
import org.icefaces.samples.showcase.example.ace.linkButton.LinkButtonBean;
import org.icefaces.samples.showcase.example.ace.list.*;
import org.icefaces.samples.showcase.example.ace.maskedEntry.MaskedEntryBean;
import org.icefaces.samples.showcase.example.ace.maskedEntry.MaskedIndicatorBean;
import org.icefaces.samples.showcase.example.ace.maskedEntry.MaskedLabelBean;
import org.icefaces.samples.showcase.example.ace.maskedEntry.MaskedReqStyleBean;
import org.icefaces.samples.showcase.example.ace.menu.*;
import org.icefaces.samples.showcase.example.ace.menuBar.*;
import org.icefaces.samples.showcase.example.ace.menuButton.MenuButtonBean;
import org.icefaces.samples.showcase.example.ace.menuSeparator.MenuSeparatorBean;
import org.icefaces.samples.showcase.example.ace.multiColumnSubmenu.MultiColumnSubmenuBean;
import org.icefaces.samples.showcase.example.ace.message.MessageBean;
import org.icefaces.samples.showcase.example.ace.messages.MessagesBean;
import org.icefaces.samples.showcase.example.ace.growlmessages.GrowlMessagesBean;
import org.icefaces.samples.showcase.example.ace.notificationpanel.NotificationPanelBean;
import org.icefaces.samples.showcase.example.ace.notificationpanel.NotificationPanelClientBean;
import org.icefaces.samples.showcase.example.ace.overview.AceSuiteOverviewBean;
import org.icefaces.samples.showcase.example.ace.panel.*;
import org.icefaces.samples.showcase.example.ace.printer.PrinterBean;
import org.icefaces.samples.showcase.example.ace.progressbar.*;
import org.icefaces.samples.showcase.example.ace.pushButton.PushButtonBean;
import org.icefaces.samples.showcase.example.ace.qrcode.QrcodeBean;
import org.icefaces.samples.showcase.example.ace.radioButton.RadioButtonBean;
import org.icefaces.samples.showcase.example.ace.radioButton.RadioButtonCustomBean;
import org.icefaces.samples.showcase.example.ace.resizable.ResizableBean;
import org.icefaces.samples.showcase.example.ace.resizable.ResizeListenerBean;
import org.icefaces.samples.showcase.example.ace.richtextentry.RichTextEntryBean;
import org.icefaces.samples.showcase.example.ace.selectmenu.*;
import org.icefaces.samples.showcase.example.ace.simpleselectonemenu.SimpleSelectOneMenuBean;
import org.icefaces.samples.showcase.example.ace.simpleselectonemenu.SimpleSelectOneMenuIndicatorBean;
import org.icefaces.samples.showcase.example.ace.simpleselectonemenu.SimpleSelectOneMenuLabelBean;
import org.icefaces.samples.showcase.example.ace.simpleselectonemenu.SimpleSelectOneMenuReqStyleBean;
import org.icefaces.samples.showcase.example.ace.slider.SliderAsyncInputBean;
import org.icefaces.samples.showcase.example.ace.slider.SliderBean;
import org.icefaces.samples.showcase.example.ace.slider.SliderListener;
import org.icefaces.samples.showcase.example.ace.slider.SliderSubmitionExample;
import org.icefaces.samples.showcase.example.ace.splitpane.SplitPaneBean;
import org.icefaces.samples.showcase.example.ace.submitMonitor.SubmitMonitorBean;
import org.icefaces.samples.showcase.example.ace.tab.TabClientSideBean;
import org.icefaces.samples.showcase.example.ace.tab.TabProxyBean;
import org.icefaces.samples.showcase.example.ace.tab.TabServerSideBean;
import org.icefaces.samples.showcase.example.ace.tab.TabSetBean;
import org.icefaces.samples.showcase.example.ace.textAreaEntry.TextAreaEntryBean;
import org.icefaces.samples.showcase.example.ace.textAreaEntry.TextAreaEntryIndicatorBean;
import org.icefaces.samples.showcase.example.ace.textAreaEntry.TextAreaEntryLabelBean;
import org.icefaces.samples.showcase.example.ace.textAreaEntry.TextAreaEntryReqStyleBean;
import org.icefaces.samples.showcase.example.ace.textEntry.*;
import org.icefaces.samples.showcase.example.ace.themeSelect.ThemeSelectBean;
import org.icefaces.samples.showcase.example.ace.tooltip.DelegateTooltipBean;
import org.icefaces.samples.showcase.example.ace.tooltip.GlobalTooltipBean;
import org.icefaces.samples.showcase.example.ace.tooltip.TooltipOverviewBean;
import org.icefaces.samples.showcase.example.ace.tree.*;
import org.icefaces.samples.showcase.example.ace.videoPlayer.VideoPlayerBean;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.SearchSelectItem;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@Menu(
        title = "menu.ace.title",
        menuLinks = {
                @MenuLink(title = "menu.ace.aceSuiteOverview.title", isDefault = true, exampleBeanName = AceSuiteOverviewBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.ajax.title", exampleBeanName = AjaxBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.submitMonitor.title", exampleBeanName = SubmitMonitorBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.autocompleteentry.title", exampleBeanName = AutoCompleteEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dateentry.title", exampleBeanName = DateEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.maskedEntry.title", exampleBeanName = MaskedEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.richtextentry.title", exampleBeanName = RichTextEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.slider.title", exampleBeanName = SliderBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.textEntry.title", exampleBeanName = TextEntryBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.textAreaEntry.title", exampleBeanName = TextAreaEntryBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.buttonGroup.title", exampleBeanName = ButtonGroupBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.checkboxButton.title", exampleBeanName = CheckboxButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.linkButton.title", exampleBeanName = LinkButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.pushButton.title", exampleBeanName = PushButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.radioButton.title", exampleBeanName = RadioButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.menuButton.title", exampleBeanName = MenuButtonBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.breadcrumbmenu.title", exampleBeanName = BreadcrumbMenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.contextMenu.title", exampleBeanName = ContextMenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.menu.title", exampleBeanName = MenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.menuBar.title", exampleBeanName = MenuBarBean.BEAN_NAME),
				@MenuLink(title = "menu.ace.menuSeparator.title", exampleBeanName = MenuSeparatorBean.BEAN_NAME),
				@MenuLink(title = "menu.ace.multiColumnSubmenu.title", exampleBeanName = MultiColumnSubmenuBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.selectmenu.title", exampleBeanName = SelectMenuBean.BEAN_NAME),
    			@MenuLink(title = "menu.ace.simpleselectonemenu.title", exampleBeanName = SimpleSelectOneMenuBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.combobox.title", exampleBeanName = ComboBoxBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.list.title", exampleBeanName = ListBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.dialog.title", exampleBeanName = DialogBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.confirmationdialog.title", exampleBeanName = ConfirmationDialogBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.notificationpanel.title", exampleBeanName = NotificationPanelBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tooltip.title", exampleBeanName = TooltipOverviewBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.accordionpanel.title", exampleBeanName = AccordionPanelBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.panel.title", exampleBeanName = PanelBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.splitpane.title", exampleBeanName = SplitPaneBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tabSet.title", exampleBeanName = TabSetBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tree.title", exampleBeanName = TreeBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.dynamicResource.title", exampleBeanName = DynamicResourceBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.fileentry.title", exampleBeanName = FileEntryBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.message.title", exampleBeanName = MessageBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.messages.title", exampleBeanName = MessagesBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.growlmessages.title", exampleBeanName = GrowlMessagesBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.cellEditor.title", exampleBeanName = CellEditorBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.columnGroup.title", exampleBeanName = ColumnGroupBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dataTable.title", exampleBeanName = DataTableBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dataExporter.title", exampleBeanName = DataExporterBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.rowExpansion.title", exampleBeanName = RowExpansionBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.tableConfigPanel.title", exampleBeanName = TableConfigPanelBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.chart.title", exampleBeanName = ChartBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.gMap.title", exampleBeanName = MapBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.audioPlayer.title", exampleBeanName = AudioPlayerBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.graphicImage.title", exampleBeanName = GraphicImageBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.videoPlayer.title", exampleBeanName = VideoPlayerBean.BEAN_NAME),

                @MenuLink(title = "menu.ace.animation.title", exampleBeanName = AnimationBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.dragDrop.title", exampleBeanName = DragDropOverviewBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.printer.title", exampleBeanName = PrinterBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.progressbar.title", exampleBeanName = ProgressBarBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.qrcode.title", exampleBeanName = QrcodeBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.resizable.title", exampleBeanName = ResizableBean.BEAN_NAME),
                @MenuLink(title = "menu.ace.themeSelect.title", exampleBeanName = ThemeSelectBean.BEAN_NAME)},
        searchSelectItems = {
                @SearchSelectItem(labelTag = "menu.ace.aceSuiteOverview.title", labelExample = "menu.ace.aceSuiteOverview.subMenu.main", value = AceSuiteOverviewBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.submitMonitor.title", labelExample = "menu.ace.submitMonitor.subMenu.main", value = SubmitMonitorBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.autocompleteentry.title", labelExample = "menu.ace.autocompleteentry.subMenu.main", value = AutoCompleteEntryBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.autocompleteentry.title", labelExample = "menu.ace.autocompleteentry.subMenu.select", value = AutoCompleteEntrySelectBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.autocompleteentry.title", labelExample = "menu.ace.autocompleteentry.subMenu.facet", value = AutoCompleteEntryFacetBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.autocompleteentry.title", labelExample = "menu.ace.autocompleteentry.subMenu.match", value = AutoCompleteEntryMatchBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.autocompleteentry.title", labelExample = "menu.ace.autocompleteentry.subMenu.rows", value = AutoCompleteEntryRowsBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.autocompleteentry.title", labelExample = "menu.ace.autocompleteentry.subMenu.lazy", value = AutoCompleteEntryLazyBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.autocompleteentry.title", labelExample = "menu.ace.autocompleteentry.subMenu.label", value = AutoCompleteEntryLabelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.autocompleteentry.title", labelExample = "menu.ace.autocompleteentry.subMenu.indicator", value = AutoCompleteEntryIndicatorBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.autocompleteentry.title", labelExample = "menu.ace.autocompleteentry.subMenu.reqStyle", value = AutoCompleteEntryReqStyleBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.main", value = DateEntryBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.popup", value = DatePopupBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.timeentry", value = DateTimeBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.ajax", value = DateAjaxBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.pages", value = DatePagesBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.minmax", value = DateMinMaxBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.navigator", value = DateNavigatorBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.label", value = DateLabelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.indicator", value = DateIndicatorBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.reqStyle", value = DateReqStyleBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dateentry.title", labelExample = "menu.ace.dateentry.subMenu.locale", value = DateLocaleBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.maskedEntry.title", labelExample = "menu.ace.maskedEntry.subMenu.main", value = MaskedEntryBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.maskedEntry.title", labelExample = "menu.ace.maskedEntry.subMenu.label", value = MaskedLabelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.maskedEntry.title", labelExample = "menu.ace.maskedEntry.subMenu.indicator", value = MaskedIndicatorBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.maskedEntry.title", labelExample = "menu.ace.maskedEntry.subMenu.reqStyle", value = MaskedReqStyleBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.richtextentry.title", labelExample = "menu.ace.richtextentry.subMenu.main", value = RichTextEntryBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.slider.title", labelExample = "menu.ace.slider.subMenu.main", value = SliderBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.slider.title", labelExample = "menu.ace.slider.subMenu.asyncinput", value = SliderAsyncInputBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.slider.title", labelExample = "menu.ace.slider.subMenu.listener", value = SliderListener.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.slider.title", labelExample = "menu.ace.slider.subMenu.submitionExample", value = SliderSubmitionExample.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.textEntry.title", labelExample = "menu.ace.textEntry.subMenu.main", value = TextEntryBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.textEntry.title", labelExample = "menu.ace.textEntry.subMenu.autotab", value = TextEntryAutotabBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.textEntry.title", labelExample = "menu.ace.textEntry.subMenu.label", value = TextEntryLabelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.textEntry.title", labelExample = "menu.ace.textEntry.subMenu.indicator", value = TextEntryIndicatorBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.textEntry.title", labelExample = "menu.ace.textEntry.subMenu.reqStyle", value = TextEntryReqStyleBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.textAreaEntry.title", labelExample = "menu.ace.textAreaEntry.subMenu.main", value = TextAreaEntryBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.textAreaEntry.title", labelExample = "menu.ace.textAreaEntry.subMenu.label", value = TextAreaEntryLabelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.textAreaEntry.title", labelExample = "menu.ace.textAreaEntry.subMenu.indicator", value = TextAreaEntryIndicatorBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.textAreaEntry.title", labelExample = "menu.ace.textAreaEntry.subMenu.reqStyle", value = TextAreaEntryReqStyleBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.buttonGroup.title", labelExample = "menu.ace.buttonGroup.subMenu.main", value = ButtonGroupBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.checkboxButton.title", labelExample = "menu.ace.checkboxButton.subMenu.main", value = CheckboxButtonBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.checkboxButton.title", labelExample = "menu.ace.checkboxButton.subMenu.custom", value = CheckboxButtonCustomBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.linkButton.title", labelExample = "menu.ace.linkButton.subMenu.main", value = LinkButtonBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.pushButton.title", labelExample = "menu.ace.pushButton.subMenu.main", value = PushButtonBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.radioButton.title", labelExample = "menu.ace.radioButton.subMenu.main", value = RadioButtonBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.radioButton.title", labelExample = "menu.ace.radioButton.subMenu.custom", value = RadioButtonCustomBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.menuButton.title", labelExample = "menu.ace.menuButton.subMenu.main", value = MenuButtonBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.breadcrumbmenu.title", labelExample = "menu.ace.breadcrumbmenu.submenu.main", value = BreadcrumbMenuBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.contextMenu.title", labelExample = "menu.ace.contextMenu.subMenu.main", value = ContextMenuBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.contextMenu.title", labelExample = "menu.ace.contextMenu.subMenu.component", value = ContextMenuComponent.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.contextMenu.title", labelExample = "menu.ace.contextMenu.subMenu.table", value = ContextMenuTable.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.contextMenu.title", labelExample = "menu.ace.contextMenu.subMenu.effect", value = ContextMenuEffect.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.contextMenu.title", labelExample = "menu.ace.contextMenu.subMenu.multicolumn", value = ContextMenuMultiColumn.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.contextMenu.title", labelExample = "menu.ace.contextMenu.subMenu.delegate", value = ContextMenuDelegate.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.menu.title", labelExample = "menu.ace.menu.subMenu.main", value = MenuBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.menu.title", labelExample = "menu.ace.menu.subMenu.type", value = MenuType.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.menu.title", labelExample = "menu.ace.menu.subMenu.events", value = MenuEvents.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.menu.title", labelExample = "menu.ace.menu.subMenu.effect", value = MenuEffect.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.menu.title", labelExample = "menu.ace.menu.subMenu.display", value = MenuDisplay.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.menuBar.title", labelExample = "menu.ace.menuBar.subMenu.main", value = MenuBarBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.menuBar.title", labelExample = "menu.ace.menuBar.subMenu.effect", value = MenuBarEffect.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.menuBar.title", labelExample = "menu.ace.menuBar.subMenu.click", value = MenuBarClick.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.menuBar.title", labelExample = "menu.ace.menuBar.subMenu.dynamic", value = MenuBarDynamic.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.menuBar.title", labelExample = "menu.ace.menuBar.subMenu.multicolumn", value = MenuBarMultiColumn.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.selectmenu.title", labelExample = "menu.ace.selectmenu.subMenu.main", value = SelectMenuBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.selectmenu.title", labelExample = "menu.ace.selectmenu.subMenu.facet", value = SelectMenuFacetBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.selectmenu.title", labelExample = "menu.ace.selectmenu.subMenu.label", value = SelectMenuLabelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.selectmenu.title", labelExample = "menu.ace.selectmenu.subMenu.indicator", value = SelectMenuIndicatorBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.selectmenu.title", labelExample = "menu.ace.selectmenu.subMenu.reqStyle", value = SelectMenuReqStyleBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.simpleselectonemenu.title", labelExample = "menu.ace.simpleselectonemenu.subMenu.main", value = SimpleSelectOneMenuBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.simpleselectonemenu.title", labelExample = "menu.ace.simpleselectonemenu.subMenu.label", value = SimpleSelectOneMenuLabelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.simpleselectonemenu.title", labelExample = "menu.ace.simpleselectonemenu.subMenu.indicator", value = SimpleSelectOneMenuIndicatorBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.simpleselectonemenu.title", labelExample = "menu.ace.simpleselectonemenu.subMenu.reqStyle", value = SimpleSelectOneMenuReqStyleBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.combobox.title", labelExample = "menu.ace.combobox.subMenu.main", value = ComboBoxBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.combobox.title", labelExample = "menu.ace.combobox.subMenu.facet", value = ComboBoxFacetBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.combobox.title", labelExample = "menu.ace.combobox.subMenu.filtering", value = ComboBoxFilteringBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.combobox.title", labelExample = "menu.ace.combobox.subMenu.label", value = ComboBoxLabelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.combobox.title", labelExample = "menu.ace.combobox.subMenu.indicator", value = ComboBoxIndicatorBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.combobox.title", labelExample = "menu.ace.combobox.subMenu.reqStyle", value = ComboBoxReqStyleBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.main", value = ListBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.selection", value = ListSelectionBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.selectionAjax", value = ListSelectionAjaxBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.selectionMini", value = ListSelectionMiniBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.reordering", value = ListReorderBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.reorderingAjax", value = ListReorderAjaxBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.drag", value = ListDragBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.dual", value = ListDualBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.multi", value = ListMultiBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.block", value = ListBlockBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.list.title", labelExample = "menu.ace.list.subMenu.blockComplex", value = ListBlockComplexBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.dialog.title", labelExample = "menu.ace.dialog.subMenu.main", value = DialogBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dialog.title", labelExample = "menu.ace.dialog.subMenu.effectsAndSize", value = DialogEffectsAndSizeBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dialog.title", labelExample = "menu.ace.dialog.subMenu.modalDialog", value = ModalDialogBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.confirmationdialog.title", labelExample = "menu.ace.confirmationdialog.subMenu.main", value = ConfirmationDialogBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.confirmationdialog.title", labelExample = "menu.ace.confirmationdialog.subMenu.modal", value = ConfirmationDialogModalBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.confirmationdialog.title", labelExample = "menu.ace.confirmationdialog.subMenu.effect", value = ConfirmationDialogEffectBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.notificationpanel.title", labelExample = "menu.ace.notificationpanel.subMenu.main", value = NotificationPanelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.notificationpanel.title", labelExample = "menu.ace.notificationpanel.subMenu.clientSide", value = NotificationPanelClientBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.tooltip.title", labelExample = "menu.ace.tooltip.subMenu.main", value = TooltipOverviewBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.tooltip.title", labelExample = "menu.ace.tooltip.subMenu.globalTooltip", value = GlobalTooltipBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.tooltip.title", labelExample = "menu.ace.tooltip.subMenu.delegateTooltip", value = DelegateTooltipBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.accordionpanel.title", labelExample = "menu.ace.accordionpanel.subMenu.main", value = AccordionPanelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.accordionpanel.title", labelExample = "menu.ace.accordionpanel.subMenu.dynamic", value = AccordionPanelDynamicBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.accordionpanel.title", labelExample = "menu.ace.accordionpanel.subMenu.effect", value = AccordionPanelEffectBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.panel.title", labelExample = "menu.ace.panel.subMenu.main", value = PanelBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.panel.title", labelExample = "menu.ace.panel.subMenu.header", value = PanelHeader.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.panel.title", labelExample = "menu.ace.panel.subMenu.toggle", value = PanelToggle.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.panel.title", labelExample = "menu.ace.panel.subMenu.close", value = PanelClose.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.panel.title", labelExample = "menu.ace.panel.subMenu.listener", value = PanelListener.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.panel.title", labelExample = "menu.ace.panel.subMenu.menu", value = PanelMenu.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.splitpane.title", labelExample = "menu.ace.splitpane.subMenu.main", value = SplitPaneBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.tabSet.title", labelExample = "menu.ace.tabSet.subMenu.main", value = TabSetBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.tabSet.title", labelExample = "menu.ace.tabSet.subMenu.clientSide", value = TabClientSideBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.tabSet.title", labelExample = "menu.ace.tabSet.subMenu.serverSide", value = TabServerSideBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.tabSet.title", labelExample = "menu.ace.tabSet.subMenu.proxy", value = TabProxyBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.tree.title", labelExample = "menu.ace.tree.subMenu.main", value = TreeBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.tree.title", labelExample = "menu.ace.tree.subMenu.lazy", value = TreeLazyBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.tree.title", labelExample = "menu.ace.tree.subMenu.client", value = TreeClientBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.tree.title", labelExample = "menu.ace.tree.subMenu.reorder", value = TreeReorderBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.tree.title", labelExample = "menu.ace.tree.subMenu.selection", value = TreeSelectionBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.fileentry.title", labelExample = "menu.ace.fileentry.subMenu.main", value = FileEntryBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.fileentry.title", labelExample = "menu.ace.fileentry.subMenu.listener", value = FileEntryListenerBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.fileentry.title", labelExample = "menu.ace.fileentry.subMenu.validation", value = FileEntryValidationOptionsBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.fileentry.title", labelExample = "menu.ace.fileentry.subMenu.callback", value = FileEntryCallbackBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.message.title", labelExample = "menu.ace.message.subMenu.main", value = MessageBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.growlmessages.title", labelExample = "menu.ace.growlmessages.subMenu.main", value = GrowlMessagesBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.main", value = DataTableBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.click", value = DataTableClick.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.columnReordering", value = DataTableColumnReordering.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.columnResizing", value = DataTableColumnResizing.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.stackable", value = DataTableStackable.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.dynamicColumns", value = DataTableDynamicColumns.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.filtering", value = DataTableFiltering.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.find", value = DataTableFind.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.grouping", value = DataTableGrouping.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.lazyLoading", value = DataTableLazyLoading.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.multiRowHeader", value = DataTableMultiRowHeader.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.paginator", value = DataTablePaginator.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.panelexpansion", value = DataTablePanelExpansion.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.pinning", value = DataTablePinning.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.rowexpansion", value = DataTableRowExpansion.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.listener", value = DataTableListener.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.rowstate", value = DataTableRowState.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.rowEditing", value = DataTableRowEditing.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.selector", value = DataTableSelector.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.scrolling", value = DataTableScrolling.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.sorting", value = DataTableSorting.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.configpanel", value = DataTableConfigPanel.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataTable.title", labelExample = "menu.ace.dataTable.subMenu.configpaneladvanced", value = DataTableConfigPanelAdvanced.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.dataExporter.title", labelExample = "menu.ace.dataExporter.subMenu.main", value = DataExporterBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataExporter.title", labelExample = "menu.ace.dataExporter.subMenu.columns", value = DataExporterColumns.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataExporter.title", labelExample = "menu.ace.dataExporter.subMenu.rows", value = DataExporterRows.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataExporter.title", labelExample = "menu.ace.dataExporter.subMenu.excludeFromExport", value = ExcludeFromExport.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.dataExporter.title", labelExample = "menu.ace.dataExporter.subMenu.custom", value = DataExporterCustom.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.main", value = ChartBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.bar", value = ChartBarBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.bubble", value = ChartBubbleBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.candlestick", value = ChartCandlestickBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.donut", value = ChartDonutBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.gauge", value = ChartGaugeBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.line", value = ChartLineBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.pie", value = ChartPieBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.combined", value = ChartCombinedBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.color", value = ChartColorBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.dynamic", value = ChartDynamicBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.chart.title", labelExample = "menu.ace.chart.subMenu.export", value = ChartExportBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.gMap.title", labelExample = "menu.ace.gMap.subMenu.overview", value = MapBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.gMap.title", labelExample = "menu.ace.gMap.subMenu.autocomplete", value = MapAutocompleteBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.gMap.title", labelExample = "menu.ace.gMap.subMenu.control", value = MapControlBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.gMap.title", labelExample = "menu.ace.gMap.subMenu.direction", value = MapDirectionBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.gMap.title", labelExample = "menu.ace.gMap.subMenu.event", value = MapEventBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.gMap.title", labelExample = "menu.ace.gMap.subMenu.infowindow", value = MapInfoWindowBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.gMap.title", labelExample = "menu.ace.gMap.subMenu.layer", value = MapLayerBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.gMap.title", labelExample = "menu.ace.gMap.subMenu.marker", value = MapMarkerBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.gMap.title", labelExample = "menu.ace.gMap.subMenu.options", value = MapOptionsBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.gMap.title", labelExample = "menu.ace.gMap.subMenu.overlay", value = MapOverlayBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.animation.title", labelExample = "menu.ace.animation.subMenu.main", value = AnimationBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.maskedEntry.title", labelExample = "menu.ace.dragDrop.subMenu.main", value = DragDropOverviewBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.maskedEntry.title", labelExample = "menu.ace.dragDrop.subMenu.draggable", value = DraggableOverviewBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.maskedEntry.title", labelExample = "menu.ace.dragDrop.subMenu.dataTableIntegration", value = DataTableIntegrationBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.printer.title", labelExample = "menu.ace.printer.subMenu.main", value = PrinterBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.progressbar.title", labelExample = "menu.ace.progressbar.subMenu.main", value = ProgressBarBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.progressbar.title", labelExample = "menu.ace.progressbar.subMenu.polling", value = ProgressBarPolling.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.progressbar.title", labelExample = "menu.ace.progressbar.subMenu.push", value = ProgressBarPush.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.progressbar.title", labelExample = "menu.ace.progressbar.subMenu.client", value = ProgressBarClient.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.progressbar.title", labelExample = "menu.ace.progressbar.subMenu.clientAndServer", value = ProgressBarClientAndServer.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.qrcode.title", labelExample = "menu.ace.qrcode.subMenu.main", value = QrcodeBean.BEAN_NAME),

                @SearchSelectItem(labelTag = "menu.ace.maskedEntry.title", labelExample = "menu.ace.resizable.subMenu.main", value = ResizableBean.BEAN_NAME),
                @SearchSelectItem(labelTag = "menu.ace.maskedEntry.title", labelExample = "menu.ace.resizable.subMenu.resizeListener", value = ResizeListenerBean.BEAN_NAME)
        }
)
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
