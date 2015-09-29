package org.icefaces.samples.showcase.view.menu.legacy;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.icefaces.samples.showcase.view.menu.CategoryGroup;
import org.icefaces.samples.showcase.view.menu.ComponentGroup;
import org.icefaces.samples.showcase.view.menu.Demo;
import org.icefaces.samples.showcase.view.menu.DemoSource;
import org.icefaces.samples.showcase.view.menu.ShowcaseMenu;
import org.icefaces.samples.showcase.view.menu.UserMenuState;

/**
 * Class designed to convert old URL links to the new menu structure
 * The menu system was changed in 2015 from custom annotations to a single unified class (ICE-10787)
 * As part of this the URLs for "grp" and "exp" bookmarkability are different
 * But we still reference the old URLs in many documents, forum posts, etc.
 * This class will convert an old legacy URL to the new proper demo
 * For example:
 *  ?grp=aceMenu&exp=textEntryBean
 * Becomes
 *  ?grp=ace:textEntry&exp=Overview
 */
public class LegacyUrlMapper {
    private static final Logger logger = Logger.getLogger(LegacyUrlMapper.class.toString());
	
	private static final String JAVA_SUFFIX = ".java";
	
	public static UserMenuState convert(List<CategoryGroup> currentMenu, UserMenuState currentState) {
		boolean foundMatch = false;
		String beanName = null;
		
		CategoryGroup loopCategory = null;
		for (int i = 0; i < currentMenu.size(); i++) {
			loopCategory = currentMenu.get(i);
			
			for (ComponentGroup loopComponent : loopCategory.getComponents()) {
				if (loopComponent.getHasDemos()) {
					for (Demo loopDemo : loopComponent.getDemos()) {
						if (loopDemo.getHasSources()) {
							beanName = null;
							for (DemoSource loopSource : loopDemo.getSources()) {
								if (loopSource.getType() == DemoSource.Type.JAVA) {
									if (loopSource.getName().toLowerCase().contains(JAVA_SUFFIX)) {
										// Trim the source name (such as TextEntryBean.java) to a lowercase textentrybean to compare to the lowercase "exp" param value
										beanName = loopSource.getName().toLowerCase().substring(0, loopSource.getName().toLowerCase().indexOf(JAVA_SUFFIX)).trim();
										
										if (beanName.equals(currentState.getParamDemo().toLowerCase().trim())) {
											currentState.setSelectedComponent(loopComponent);
											currentState.setSelectedDemo(loopDemo);
											currentState.setActiveIndex(i);
											
											foundMatch = true;
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		if (foundMatch) {
			logger.info("Converted a legacy URL from " + ShowcaseMenu.URL_PARAM_GROUP + "=" + currentState.getParamComponent() + "&" + ShowcaseMenu.URL_PARAM_DEMO + "=" + currentState.getParamDemo() +
				    " to '" + currentState.getSelectedComponent().getName() + " " + currentState.getSelectedDemo().getName() + "'.");
		}
		else {
			logger.log(Level.WARNING, "Could not find a current match for legacy URL " + ShowcaseMenu.URL_PARAM_GROUP + "=" + currentState.getParamComponent() + "&" + ShowcaseMenu.URL_PARAM_DEMO + "=" + currentState.getParamDemo() + ".");
		}
		
		return currentState;
	}
}
