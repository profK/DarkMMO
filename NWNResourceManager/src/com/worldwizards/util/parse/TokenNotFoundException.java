/**
 *
 * <p>Title: TokenNotFoundException.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004 Sun Microsystems, Inc.</p>
 * <p>Company: Sun Microsystems, Inc</p>
 * @author Jeff Kesselman
 * @version 1.0
 */
package com.worldwizards.util.parse;

/**
 *
 * <p>Title: TokenNotFoundException.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004 Sun Microsystems, Inc.</p>
 * <p>Company: Sun Microsystems, Inc</p>
 * @author Jeff Kesselman
 * @version 1.0
 */
public class TokenNotFoundException extends Exception {
	String tokenFound;
	String[] tokensWanted;
	int charpos;
	int linepos;	
	private String message;
	/**
	 * @param token
	 * @param tokens
	 * @param charCount
	 * @param charCount2
	 * @param lineCount
	 */
	public TokenNotFoundException(String token, String[] tokens, int charCount, int lineCount) {
		tokenFound = token;
		tokensWanted = tokens;
		charpos = charCount;
		linepos = lineCount;	
		StringBuffer msg = new StringBuffer("Found ");
		msg.append(token);
		msg.append(" expected [");
		for(int i=0;i<tokens.length;i++){
			msg.append(tokens[i]);
			if (i<tokens.length-1){
				msg.append(",");
			}
		}
		msg.append("] at line ");
		msg.append(linepos);
		msg.append(", char ");
		msg.append(charpos);
		message = msg.toString();
	}
	
	public String getMessage(){
		return message;
	}
	
}
