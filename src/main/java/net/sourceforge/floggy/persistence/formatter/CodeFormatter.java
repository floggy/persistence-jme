package net.sourceforge.floggy.persistence.formatter;

import java.util.StringTokenizer;

public class CodeFormatter {

    public static String format(String source) {
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

}
