@REM
@REM Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
@REM
@REM Licensed under the Apache License, Version 2.0 (the "License");
@REM you may not use this file except in compliance with the License.
@REM You may obtain a copy of the License at
@REM
@REM http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing, software
@REM distributed under the License is distributed on an "AS IS" BASIS,
@REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM See the License for the specific language governing permissions and
@REM limitations under the License.
@REM

@ECHO off

SET ERROR_CODE=0

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM ==== START VALIDATION ====
IF NOT "%JAVA_HOME%" == "" GOTO OkJHome

ECHO.
ECHO ERROR: JAVA_HOME not found in your environment.
ECHO Please set the JAVA_HOME variable in your environment to match the
ECHO location of your Java installation
ECHO.
GOTO error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" GOTO chkFloggyHome

ECHO.
ECHO ERROR: JAVA_HOME is set to an invalid directory.
ECHO JAVA_HOME = %JAVA_HOME%
ECHO Please set the JAVA_HOME variable in your environment to match the
ECHO location of your Java installation
ECHO.
GOTO error

:chkFloggyHome
if not "%FLOGGY_HOME%"=="" GOTO valFloggyHome

if "%OS%"=="Windows_NT" SET FLOGGY_HOME=%~dp0\..
if not "%FLOGGY_HOME%"=="" GOTO valMHome

ECHO.
ECHO ERROR: FLOGGY_HOME not found in your environment.
ECHO Please set the FLOGGY_HOME variable in your environment to match the
ECHO location of the Floggy installation
ECHO.
GOTO error

:valFloggyHome
if exist "%FLOGGY_HOME%\bin\fpw.bat" GOTO init

ECHO.
ECHO ERROR: FLOGGY_HOME is set to an invalid directory.
ECHO FLOGGY_HOME = %FLOGGY_HOME%
ECHO Please set the FLOGGY_HOME variable in your environment to match the
ECHO location of the Floggy installation
ECHO.
GOTO error
@REM ==== END VALIDATION ====

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" GOTO Win9xArg

@REM -- 4NT shell
if "%eval[2+2]" == "4" GOTO 4NTArgs

@REM -- Regular WinNT shell
SET FLOGGY_CMD_LINE_ARGS=%*
GOTO endInit

@REM The 4NT Shell from jp software
:4NTArgs
SET FLOGGY_CMD_LINE_ARGS=%$
GOTO endInit

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
SET FLOGGY_CMD_LINE_ARGS=
:Win9xApp
IF %1a==a GOTO endInit
SET FLOGGY_CMD_LINE_ARGS=%FLOGGY_CMD_LINE_ARGS% %1
shift
GOTO Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit

SET cp=%FLOGGY_HOME%\lib\floggy-persistence-weaver.jar;%FLOGGY_HOME%\lib\floggy-persistence-framework.jar;%FLOGGY_HOME%\lib\commons-io.jar;%FLOGGY_HOME%\lib\javassist.jar;%FLOGGY_HOME%\lib\commons-logging.jar;

"%JAVA_HOME%"\bin\java.exe -cp %cp% net.sourceforge.floggy.persistence.Main %FLOGGY_CMD_LINE_ARGS%


:error
IF "%OS%"=="Windows_NT" @ENDLOCAL
SET ERROR_CODE=1

:end
EXIT /B %ERROR_CODE%