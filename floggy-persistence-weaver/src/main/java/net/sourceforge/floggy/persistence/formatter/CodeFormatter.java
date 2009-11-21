/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.floggy.persistence.formatter;

import java.util.StringTokenizer;

public class CodeFormatter {

    public static String format(String source) {
    	if (source == null) {
    		throw new IllegalArgumentException();
    	}
	StringBuffer formatted = new StringBuffer();
	StringTokenizer tokenizer = new StringTokenizer(source, "\n");

	int tabIdent = 0;
	while (tokenizer.hasMoreTokens()) {
	    String line = tokenizer.nextToken();

	    int lineTabIdent = getTabIdent(line);
	    if (lineTabIdent <= 0) {
		tabIdent += lineTabIdent;
	    }

	    for (int i = 0; i < tabIdent; i++) {
		line = "    " + line;
	    }

	    formatted.append(line + "\n");

	    if (lineTabIdent > 0) {
		tabIdent += lineTabIdent;
	    }
	}

	return formatted.toString();
    }

    private static int getTabIdent(String line) {
	if (line == null) {
	    return 0;
	}

	int tabIdent = 0;
	char[] charArray = line.toCharArray();
	for (int i = 0; i < charArray.length; i++) {
	    if (charArray[i] == '{') {
		tabIdent++;
	    } else if (charArray[i] == '}') {
		tabIdent--;
	    }
	}

	return tabIdent;
    }

    protected CodeFormatter() {
    }
    
}
