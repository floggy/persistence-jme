#!/bin/sh
# ----------------------------------------------------------------------------
#  Copyright 2006 Floggy Open Source Group
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Floggy Start Up Batch script
#
# Required ENV vars:
# ------------------
#   JAVA_HOME - location of a JDK home dir
#
# ----------------------------------------------------------------------------


FLOGGY_CMD_LINE_ARGS=""
while [ "$1" != "" ] ; do

  FLOGGY_CMD_LINE_ARGS="$FLOGGY_CMD_LINE_ARGS $1"
  shift

done

# OS specific support.  $var _must_ be set to either true or false.
cygwin=false;
darwin=false;
mingw=false;
case "`uname`" in
  CYGWIN*) cygwin=true;;
  MINGW*) mingw=true;;
  Darwin*) darwin=true 
           if [ -z "$JAVA_VERSION" ] ; then
             JAVA_VERSION="CurrentJDK"
           else
             echo "Using Java version: $JAVA_VERSION"
           fi
           if [ -z "$JAVA_HOME" ] ; then
             JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/${JAVA_VERSION}/Home
           fi
           ;;
esac

if [ -z "$JAVA_HOME" ] ; then
  if [ -r /etc/gentoo-release ] ; then
    JAVA_HOME=`java-config --jre-home`
  fi
fi

if [ -z "$FLOGGY_HOME" ] ; then
  ## resolve links - $0 may be a link to Floggy's home
  PRG="$0"

  # need this for relative symlinks
  while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
      PRG="$link"
    else
      PRG="`dirname "$PRG"`/$link"
    fi
  done

  saveddir=`pwd`

  FLOGGY_HOME=`dirname "$PRG"`/..

  # make it fully qualified
  FLOGGY_HOME=`cd "$FLOGGY_HOME" && pwd`

  cd "$saveddir"
  # echo Using Floggy at $FLOGGY_HOME
fi

# For Cygwin, ensure paths are in UNIX format before anything is touched
if $cygwin ; then
  [ -n "$FLOGGY_HOME" ] &&
    FLOGGY_HOME=`cygpath --unix "$FLOGGY_HOME"`
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] &&
    CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
fi

# For Migwn, ensure paths are in UNIX format before anything is touched
if $mingw ; then
  [ -n "$FLOGGY_HOME" ] &&
    FLOGGY_HOME="`(cd "$FLOGGY_HOME"; pwd)`"
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME="`(cd "$JAVA_HOME"; pwd)`"
  # TODO classpath?
fi

if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=java
  fi
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi

if [ -z "$JAVA_HOME" ] ; then
  echo "Warning: JAVA_HOME environment variable is not set."
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$FLOGGY_HOME" ] &&
    FLOGGY_HOME=`cygpath --path --windows "$FLOGGY_HOME"`
  [ -n "$JAVA_HOME" ] &&
    JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$HOME" ] &&
    HOME=`cygpath --path --windows "$HOME"`
fi

cp=$FLOGGY_HOME/lib/floggy-persistence-weaver.jar:$FLOGGY_HOME/lib/floggy-persistence-framework.jar:$FLOGGY_HOME/lib/commons-io.jar:$FLOGGY_HOME/lib/javassist.jar:$FLOGGY_HOME/lib/commons-logging.jar

exec "$JAVACMD" \
  -classpath "$cp" \
  net.sourceforge.floggy.persistence.Main $FLOGGY_CMD_LINE_ARGS


