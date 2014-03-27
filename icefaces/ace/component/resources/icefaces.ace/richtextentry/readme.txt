Custom Modifications:

*.css
Modified occurrences of url() from using a plain path to a JSF resource expression path.

ckeditor.mapping.js
Added this file to specify a mapping from canonical resource names to correct JSF resource URLs.

/plugins/smiley/dialogs/smiley.js
/plugins/wsc/dialogs/wsc.js
/plugins/wsc/dialogs/wsc_ie.js
Modified paths to internal resources to invoke CKEDITOR.getUrl() first, in order to use correct JSF URLs.

/samples
Removed all resources under /samples and the directory itself.