@echo off

rem ---------------------
rem | Setting all paths |
rem ---------------------

set SourcePath=..\java

set JavadocPath=..\..\docs

set TagletPath=..\..\target\classes
set ToolsPath=..\..\lib\tools.jar
set JexlPath=..\..\lib\commons-jexl-1.0.jar
set LoggingPath=..\..\lib\commons-logging.jar
set JUnitPath=..\..\lib\junit.jar

set ClassPath=".;%TagletPath%;%ToolsPath%;%JexlPath%;%LoggingPath%;%JUnitPath%"



rem ---------------
rem | Debug stuff |
rem ---------------

rem echo ClassPath: %ClassPath%



rem -------------------
rem | Generate javadoc |
rem --------------------

javadoc -source 1.4 -tagletpath %TagletPath% -taglet be.peopleware.taglet.contract.Registrar -taglet be.peopleware.taglet.team.Registrar -d %JavadocPath% -classpath %ClassPath% -sourcepath %SourcePath% -subpackages be.peopleware.taglet
