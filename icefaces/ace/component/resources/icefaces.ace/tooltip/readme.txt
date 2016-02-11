Updated to qtip 2.2

Custom fixes in .js file:
* replaced occurrences of jQuery for ice.ace.jq
* removed 'else' keyword when registering plugin with jQuery
* commented out aria-atomic attributes, which were causing Javascript errors on IE7
* removed mechanism that assigned new numeric ids to tooltip instances and instead added call to destroy the previous instance with the same id

Custom fixes in .css file:
* added custom extensions
* added rounded corners in custom extensions
* removed max width
* removed font size
* added box shadow

