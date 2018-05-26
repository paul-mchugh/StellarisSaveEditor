package info.paulmchugh.stellariseditor.fileparser;

import info.paulmchugh.stellariseditor.fileparser.tokens.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;

public class GamestateTokenizer
{
	public static Deque<SaveFileToken> generateTokenQueueFromGamestate(InputStream gamestateInput) throws IOException, StellarisSaveFileParseException
	{
		Deque<SaveFileToken> tokens = new LinkedList<>();
		BufferedInputStream gamestate = new BufferedInputStream(gamestateInput);
		
		//information about the previous bytes in the stream
		int lineNo = 1;
		StringBuilder currentToken = new StringBuilder();
		boolean lastIsEscapeChar = false;
		boolean isQuotedString = false;
		
		
		//read input from the file until it is done
		while (true)
		{
			//mark the stream in case we don't want this character to be consumed
			gamestate.mark(1);
			//read the byte
			int input = gamestate.read();
			
			//the current character in the stream
			char currentChar = (char)input;
			//if the character was a newline then we need to increment the line number
			if (currentChar=='\n')lineNo++;
			
			//determine if the char that we just read means that the current token is complete if so then add the token to the end of the queue
			//if it is incomplete then we continue accumulating the token
			//we do different things if we are in a string
			boolean multiCharTokenComplete = false;
			
			if (input==-1&&!isQuotedString)multiCharTokenComplete=true;
			
			
			if (isQuotedString)
			{
				//in order to terminate a string you need an un-escaped quote
				if (currentChar=='"'&&!lastIsEscapeChar)
				{
					currentToken.append('"');
					multiCharTokenComplete = true;
				}
			}
			else
			{
				//anything less than or equal to 32 is whitespace and = and } also terminate tokens
				if (currentChar <= 32 || currentChar == '=' || currentChar == '}') multiCharTokenComplete = true;
			}
			
			//change the value of token complete back to false if the there is no token in the StringBuilder
			if (currentToken.length()==0)multiCharTokenComplete = false;
			
			//if the token is complete then we need to add it to the Queue
			if (multiCharTokenComplete)
			{
				//determine this token's type
				SaveFileToken token;
				if (!isQuotedString)
				{
					//WE ARE NOT IN A QUOTED STRING THIS IS EITHER A INTEGER, DOUBLE, OR KEY
					//isString denotes string that is wrapped by quotes keys are wrapped by quotes
					//some keys are not wrapped by quotes and therefore are not covered by isQuotedString
					token = getNumericTokenFromString(currentToken.toString());
					if (token.getType()==TokenTypes.ERROR)
					{
						//if the numeric token recognizer could not parse this string then it must be a key
						//if the token is after a EQUALS_SIGN then it can not be a key
						if (tokens.isEmpty()||tokens.peekLast().getType()!=TokenTypes.EQUALS_SIGN)
						{
							//NOT after an equals sign
							//THIS IS AN UNQUOTED KEY
							token = new KeyToken(currentToken.toString());
						}
						else
						{
							//key after an equals sign
							throw new StellarisSaveFileParseException("Unexpected token (" + currentToken.toString() + ") on line:"+ lineNo);
						}
					}
					tokens.addLast(token);
				}
				else
				{
					//WE ARE IN A QUOTED STRING THIS IS EITHER A QUOTED KEY OR A STRING
					//either way we can't tell yet because it is plausible that it is either a string in an array or a quoted key before an equals sign
					//once we have parsed the next token we can tell
					tokens.add(new StringToken(currentToken.substring(1,currentToken.length()-1)));
				}
				//we just added a token we need to re set everything
				currentToken = new StringBuilder();
				isQuotedString = false;
				lastIsEscapeChar = false;
				
				//if we just ended a token with a = or } then we need to reset the stream so that the char is not consumed
				if (currentChar == '=' || currentChar == '}') gamestate.reset();
				
				//if we reached the end of the file then we break out
				if (input==-1)break;
			}
			else if (input==-1)
			{
				//this IS the EOF, but it IS NOT the end of the end of a multichar token.
				//one of two things is true either this end came after a token that was already terminated or this is the end of an unterminated string
				//if this is after an already terminated token then we need to break out.  If it is an unterminated string then we throw an exception
				if (isQuotedString)
				{
					throw new StellarisSaveFileParseException("Illegal Token: Quoted object is terminated by EOF not an end qoute on line = " + lineNo);
				}
				else
				{
					break;
				}
			}
			else
			{
				//KEEP ON BUILDING THE CURRENT WORD OR PROCESS THE SINGLE CHAR TOKEN
				if ((currentChar == '=' || currentChar == '{' || currentChar == '}')&&!isQuotedString)
				{
					//single char token
					SaveFileToken lastToken = tokens.peekLast();
					if (lastToken!=null&&currentChar=='='&&(lastToken.getType()==TokenTypes.INTEGER||lastToken.getType()==TokenTypes.STRING))
					{
						//if this token is an equals sign token then the token before this one must be a key
						tokens.removeLast();
						
						if (lastToken.getType()==TokenTypes.STRING)
						{
							//must be a quoted key b/c all the unquoted keys were already made into key tokens
							//so we need to re-add the quotes
							tokens.addLast(new KeyToken("\"" + ((StringToken)lastToken).getValue() + "\""));
						}
						else
						{
							//convert the integer to a string an then add it back
							tokens.addLast(new KeyToken(Long.toString(((IntegerToken)lastToken).getValue())));
						}
					}
					
					//now the simple add tokens based of the character type part
					if (currentChar=='=')tokens.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
					else if (currentChar=='{')tokens.addLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
					else tokens.addLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));
					
				}
				else if (currentChar>32)
				{
					//part of multi-char token
					
					//if this char is part if a quoted string(includes quoted keys)
					if (isQuotedString)
					{
						if (lastIsEscapeChar)
						{
							//if the last char is a escape then we put in this char even if it is a special char
							currentToken.append(currentChar);
							lastIsEscapeChar = false;
						}
						else if (currentChar=='\\')
						{
							//if this char is a backslash then we record that the last char is a backslash and don't add anything
							lastIsEscapeChar = true;
						}
						else
						{
							//normal char just add it
							currentToken.append(currentChar);
						}
					}
					else
					{
						//if this char is part of an unquoted token
						
						//if this is the start of a token and the first char is a quote
						if (currentToken.length()==0&&currentChar=='"')
						{
							isQuotedString = true;
							currentToken.append(currentChar);
						}
						else
						{
							//this is an ordinary character that is in an unquoted token
							currentToken.append(currentChar);
						}
						
					}
				}
				else
				{
					//if we are in a quoted string then we need to add the whitespace
					if (isQuotedString)
					{
						currentToken.append(currentChar);
					}
				}
			}
			
		}
		
		//add the end of save file token
		tokens.addLast(new SaveFileToken(TokenTypes.GS_END));
		//return
		return tokens;
	}
	
	private static SaveFileToken getNumericTokenFromString(String token)
	{
		try
		{
			return new IntegerToken(Long.parseLong(token));
		}
		catch (NumberFormatException e)
		{
			try
			{
				return new FloatingPointToken(Double.parseDouble(token));
			}
			catch (NumberFormatException e2)
			{
				return new SaveFileToken(TokenTypes.ERROR);
			}
		}
		
	}
}
