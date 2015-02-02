Custom Modifications:

*.css
Modified occurrences of url() from using a plain path to a JSF resource expression path.

/ckeditor.mapping.js
Added this file to specify a mapping from canonical resource names to correct JSF resource URLs.
Modified mappings of *.md files to *.md.txt, so they have a known mime type

/ckeditor.js
/plugins/smiley/dialogs/smiley.js
/plugins/wsc/dialogs/wsc.js
/plugins/wsc/dialogs/wsc_ie.js
/plugins/about/dialogs/about.js
Modified paths to internal resources to invoke CKEDITOR.getUrl() first, in order to use correct JSF URLs.

/ckeditor.js
Line 778, added object existence conditions in detach() function to avoid NPEs
Line 904, set specific height when entering into source mode, in order to avoid collapsing the editable area
Line 236, disabled behaviour of automatically updating the underlying textarea when the form is submitted
Line 308, deleted code that removed the 'required' attribute from underlying textarea element

/samples
Removed all resources under /samples and the directory itself.

*.md
Renamed *.md files to *.md.txt, so they have a known mime type

/plugins/wsc/dialogs/wsc.js
/plugins/wsc/dialogs/wsc_ie.js
Replaced occurrences of the "http:" string for document.location.protocol, in order to correctly support SSL