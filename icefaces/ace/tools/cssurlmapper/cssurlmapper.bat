@echo off

set CMD_LINE_ARGS=%1
if ""%1""=="""" goto doneStart
shift
:setupArgs
if ""%1""=="""" goto doneStart
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setupArgs
:doneStart

java -Djava.ext.dirs=lib;dist org.icefaces.ace.util.cssurlmapper.Main %CMD_LINE_ARGS%
