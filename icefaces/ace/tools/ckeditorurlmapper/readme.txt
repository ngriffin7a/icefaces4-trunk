What is this?

This is a tool that does two things to the original CKEditor resources:
1. Creates a javascript file containing a mapping function from canonical 
resource names to correct EL resource expressions.
2. Replaces all occurrences of url() expressions in CSS files from 
relative paths to EL resource expressions.

How to run?

1. Open CKEditorUrlMapper.java and make sure all the constants that appear at 
the top have correct values, especially LIB_ABSOLUTE_PATH. Use forward slashes
for directory paths and also make sure that they end with a forward slash too.
2. Open the build.xml file in this directory and make sure that the paths 
specified there seem correct for the current file hierarchy of the ICEfaces
project.
3. Compile tool by typing 'ant' on the command line.
4. Run tool by typing 'ant run' on the command line.

When to run?

This tool is meant to be run only when upgrading to a new version of CKEditor, 
since it assumes that all urls in the resources will be regular relative
paths and not EL resource expressions or something else.

For the same reasons, this tool is not meant to be run at every build or 
periodically. It shouldn't be run if a previous run already modified the
resources

Other notes

* Nothing outside of the CKEditor root directory gets modified. The CSS files
are replaced in place, and the mapping Javascript file is added to the 
CKEditor root directory.

* This tool makes some assumptions about the way the CKEditor resources are
organized, regarding relative paths. If the new version of CKEditor 
diverges substantially from the way it has been organized in the past,
the algorithm to modify the CSS resources and create the mapping Javascript
file might need to be modified to satisfy the new requirements.
