ICEsoft custom modifications

* jquery.qtip-2.0.0.js	replaced occurrences of 'jQuery' for ice.ace.jq at the top
* jquery.qtip-2.0.0.js	at line 187 added ui-widget-class to outer div
* jquery.qtip-2.0.0.js	removed line 188 to avoid applying ui-widget-content class to inner div
* jquery.qtip-2.0.0.css	added custom extensions at the bottom
* jquery.qtip-2.0.0.css	removed max-width
* jquery.qtip-2.0.0.js	ICE-8699 added logic to only register the window mouseout event handler when the tooltip is shown and unregister it when the tooltip is hidden
* jquery.qtip-2.0.0.js	ICE-8905 Add ARIA role and attributes to Tooltip
* jquery.qtip-2.0.0.js	ICE-8974 recalculated position in ie7 browsers to display tooltip as intended