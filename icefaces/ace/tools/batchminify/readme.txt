What is this?
A simple tool that invokes Google's closure-compiler for multiple files, using just one process, in order to avoid starting a new VM for every file.

How to run?
This is meant to be run from a build script. The tool only takes one argument and that is a list of the source files to process, separated by semicolons.

Other notes
* The output files will be written to the same directory as the source files.
* The name of the minified output file is hard-coded in the class itself, which is basically the same name as the source file with an added suffix.

