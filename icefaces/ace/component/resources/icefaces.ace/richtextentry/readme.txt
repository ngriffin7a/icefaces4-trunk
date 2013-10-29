Custom Modifications:

ICE-8130	ckeditor/plugins/wsc/dialogs/wsc.js
Spellcheck plug-in was modified to (1) use correctly the URL mapping implemented to access resources in JSF, and to (2) supply absolute paths for the resources that will be loaded by the external site (spellchecker.net) iframe.

ICE-8715	(all CSS files)
Replaced the contents of url() expressions to contain EL resource expressions instead of simple relative paths.

ICE-9184	ckeditor/ckeditor.js
Line 96, getData() was modified to check for object 'N' and to exit function if it's not there.

ICE-9374	ckeditor/ckeditor.js
Line 22, added function call to get contents.css using the mapping, instead of trying to use the standard URL

ICE-9387	ckeditor/ckeditor.js
Line 80, modified loading of preview.html to use the mapping function to access it via the right URL