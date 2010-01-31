/**
 *
 * <p>Title: TestParser.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004 Sun Microsystems, Inc.</p>
 * <p>Company: Sun Microsystems, Inc</p>
 * @author Jeff Kesselman
 * @version 1.0
 */
package com.worldwizards.util.parse;

import java.io.IOException;
import java.io.Reader;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * <p>
 * Title: TestParser.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004 Sun Microsystems, Inc.
 * </p>
 * <p>
 * Company: Sun Microsystems, Inc
 * </p>
 * 
 * @author Jeff Kesselman
 * @version 1.0
 */
public class TextParser {
	enum TokenState {
		WhiteSpace, Integer, Float, String, Operator
	};

	String whitespace = " \t\r\n";

	String extendedStrChars = "_";

	Reader textIn;

	private int lineCount = 1;

	private int charCount = 0;

	private boolean pushedBack = false;

	private int pushedBackChar;

	private boolean done = false;

	public TextParser(Reader input) {
		textIn = input;
	}

	public void setWhitespace(String ws) {
		whitespace = ws;
	}

	// scanner
	private String nextToken() throws IOException {
		StringBuffer token = new StringBuffer();
		TokenState state = TokenState.WhiteSpace;
		while (!done) {
			int nextChar = getNextChar();
			charCount++;
			if (nextChar == -1){
				charCount--;
				return token.toString();
			} else if (nextChar == '\n') {
				charCount = 0;
				lineCount++;
			} else if (nextChar == '-') {
				switch (state) {
				case WhiteSpace:
					token.append((char) nextChar);
					state = TokenState.Integer;
					break;
				case Integer:
					pushBack(nextChar);
					return token.toString();
				case String:
					token.append((char) nextChar);
					break;
				case Operator:
					pushBack(nextChar);
					return token.toString();
				case Float:
					pushBack(nextChar);
					return token.toString();
				}
			} else if (nextChar == '.') {
				switch (state) {
				case WhiteSpace:
					token.append((char) nextChar);
					state = TokenState.Operator;
					break;
				case Integer:
					token.append((char) nextChar);
					state = TokenState.Float;
					break;
				case String:
					token.append((char) nextChar);
					break;
				case Operator:
					pushBack(nextChar);
					return token.toString();
				case Float:
					pushBack(nextChar);
					return token.toString();
				}
			} else if (whitespace.indexOf(nextChar) != -1) { // white space
				switch (state) {
				case WhiteSpace:
					// nop
					break;
				case Integer:
					return token.toString();
				case String:
					return token.toString();
				case Operator:
					return token.toString();
				case Float:
					return token.toString();
				}
			} else if (Character.isLetter(nextChar)
					|| (extendedStrChars.indexOf(nextChar) != -1)) {
				switch (state) {
				case WhiteSpace:
					token.append((char) nextChar);
					state = TokenState.String;
					break;
				case Integer:
					token.append((char) nextChar);
					state = TokenState.String;
					break;
				case String:
					token.append((char) nextChar);
					break;
				case Operator:
					pushBack(nextChar);
					return token.toString();
				}
			} else if (Character.isDigit(nextChar)) {
				switch (state) {
				case WhiteSpace:
					token.append((char) nextChar);
					state = TokenState.Integer;
					break;
				case Integer:
					token.append((char) nextChar);
					break;
				case Float:
					token.append((char) nextChar);
					break;
				case String:
					token.append((char) nextChar);
					break;
				case Operator:
					pushBack(nextChar);
					return token.toString();
				}
			} else { // operator
				switch (state) {
				case WhiteSpace:
					token.append((char) nextChar);
					state = TokenState.Operator;
					break;
				case Integer:
					pushBack(nextChar);
					return token.toString();
				case Float:
					pushBack(nextChar);
					return token.toString();
				case String:
					pushBack(nextChar);
					return token.toString();
				case Operator:
					token.append((char) nextChar);
					break;
				}
			}
		}
		if (token.length() > 0) {
			return token.toString();
		} else {
			return null;
		}
	}

	private void pushBack(int ch) {
		pushedBack = true;
		pushedBackChar = ch;
		charCount--;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	private int getNextChar() throws IOException {
		if (pushedBack) {
			pushedBack = false;
			return pushedBackChar;
		}
		return textIn.read();
	}

	public int expectStrings(String[] tokens) throws IOException,
			TokenNotFoundException {
		String token = nextToken();
		if (token == null) {
			throw new EndOfInputException();
		}
		for (int i = 0; i < tokens.length; i++) {
			if (token.equals(tokens[i])) {
				return i;
			}
		}
		throw new TokenNotFoundException(token, tokens, charCount
				- token.length(), lineCount);
	}

	public void expectString(String tok) throws IOException,
			TokenNotFoundException {
		String token = nextToken();
		if (!tok.equals(token)) {
			throw new TokenNotFoundException(token, new String[] { tok },
					charCount - token.length(), lineCount);
		}
	}

	public int getInteger() throws IOException {
		String token = nextToken();
		return Integer.parseInt(token);
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public String getString() throws IOException {
		String token = nextToken();
		return token;
	}

	/**
	 * @return
	 * @throws IOException
	 */
	public float getFloat() throws IOException {
		String token = nextToken();
		return Float.parseFloat(token);
	}

	public boolean hasMore() throws IOException {
		if (pushedBack){
			return true;
		} else {
			int nxt = getNextChar();
			if (nxt==-1){
				return false;
			} else {
				pushBack(nxt);
				return true;
			}
		}
	}

}
