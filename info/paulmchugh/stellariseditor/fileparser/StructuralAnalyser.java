package info.paulmchugh.stellariseditor.fileparser;

import info.paulmchugh.stellariseditor.datatypes.*;
import info.paulmchugh.stellariseditor.fileparser.tokens.*;

import java.util.Deque;

@SuppressWarnings({"ConstantConditions", "StatementWithEmptyBody"})
public class StructuralAnalyser
{
	//file ::= kvpair {kvpair} END
	public static RootGroup analyseTokenQueue(Deque<SaveFileToken> tokenQueue) throws StellarisSaveFileParseException
	{
		//RootGroup that all the children will be added to
		RootGroup resultGroup = new RootGroup();
		//check to make sure that there is at least one KVPair in the root group
		if (!parseKVPair(tokenQueue,resultGroup))
		{
			throw new StellarisSaveFileParseException("file contains no KVPairs in root group (file is empty)");
		}
		//parse and add the rest of the KVPairs
		while (parseKVPair(tokenQueue, resultGroup));
		//check for the gamestate_end (GS_END) token
		if (tokenQueue.poll().getType()!=TokenTypes.GS_END) throw new StellarisSaveFileParseException("Token queue doesn't end with GS_END. Submit a bug report.");
		
		return resultGroup;
	}
	
	//kvpair ::= KEY EQUALS value
	//returns true if there was a kvpair to parse false otherwise
	private static boolean parseKVPair(Deque<SaveFileToken> tokenQueue, NamedGroup parent) throws StellarisSaveFileParseException
	{
		KeyToken key;
		SaveElement value;
		
		//get the key if there is no key then stop and tell the calling function
		if (tokenQueue.peek().getType()!=TokenTypes.KEY) return false;
		key = (KeyToken) tokenQueue.poll();
		
		//check for an equals token
		if (tokenQueue.poll().getType()!=TokenTypes.EQUALS_SIGN) throw new StellarisSaveFileParseException("Expected EQUALS_SIGN as second token in KVPair");
		
		//get the value
		value = parseValue(tokenQueue);
		
		//insert child into parent
		parent.insert(key.getValue(),value);
		
		return true;
	}
	
	//value ::= INT | FLOAT | STRING | ngroup | ungroup
	private static SaveElement parseValue(Deque<SaveFileToken> tokenQueue) throws StellarisSaveFileParseException
	{
		SaveFileToken firstToken = tokenQueue.poll();
		switch (firstToken.getType())
		{
			//simple values
			case INTEGER:
				return new DSInteger(((IntegerToken)firstToken).getValue());
			case FLOATING_PT:
				return new DSFloat(((FloatingPointToken)firstToken).getValue());
			case STRING:
				return new DSString(((StringToken)firstToken).getValue());
			//errors
			default:
			case KEY:
			case EQUALS_SIGN:
			case GROUP_CLOSE:
			case GS_END:
			case ERROR:
				throw new StellarisSaveFileParseException("Illegal token discovered as value.");
			//the value is a group -most difficult part
			case GROUP_OPEN:
			{
				//put back the group open token
				tokenQueue.addFirst(firstToken);
				//This parser is loosely a single look ahead recursive descent parser.  This is the one exception.
				//When the parser encounters an GROUP_OPEN token it is impossible to tell if the group is named or not.
				//So we cheat and look ahead by 2 tokens so we can determine if it is named or not.
				if (isNextGroupNamed(tokenQueue))
				{
					return parseNamedGroup(tokenQueue);
				}
				else
				{
					return parseUnnamedGroup(tokenQueue);
				}
			}
		}
	}
	
	//for determining if a value is a named or unnamed group
	private static boolean isNextGroupNamed(Deque<SaveFileToken> tokenQueue) throws StellarisSaveFileParseException
	{
		//get the relevant tokens
		SaveFileToken groupOpenToken = tokenQueue.poll();
		SaveFileToken potentialKey    = tokenQueue.poll();
		SaveFileToken potentialEquals = tokenQueue.poll();
		//store if their types are consistent witht he start of a named group
		boolean firstIsGroupOpen = groupOpenToken.getType()==TokenTypes.GROUP_OPEN;
		boolean secondIsKey      = potentialKey.getType()==TokenTypes.KEY;
		boolean thirdIsEquals    = potentialEquals.getType()==TokenTypes.EQUALS_SIGN;
		//restore the tokenQueue
		tokenQueue.push(potentialEquals);
		tokenQueue.push(potentialKey);
		tokenQueue.push(groupOpenToken);
		//the first token must be a GROUP_OPEN
		if (!firstIsGroupOpen) throw new StellarisSaveFileParseException("Expected group did not start with GROUP_OPEN symbol \'{\'");
		//If we have a key and no equals or no key and an equals then the group is malformed
		if (secondIsKey^thirdIsEquals) throw new StellarisSaveFileParseException("Malformed group not consistent with named or unnamed groups.");
		//secondIsKey and thirdIsEquals are equal at this point so just return one of them
		return secondIsKey;
	}
	
	private static NamedGroup parseNamedGroup(Deque<SaveFileToken> tokenQueue) throws StellarisSaveFileParseException
	{
		NamedGroup thisGroup = new NamedGroup();
		
		//check for GROUP_OPEN
		if (tokenQueue.poll().getType()!=TokenTypes.GROUP_OPEN) throw new StellarisSaveFileParseException("Named group doesn't start with GROUP_OPEN.");
		
		//check to make sure that there is at least one KVPair in the current group
		if (!parseKVPair(tokenQueue,thisGroup))
		{
			throw new StellarisSaveFileParseException("Named group contains no child elements.");
		}
		//parse and add the rest of the KVPairs
		while (parseKVPair(tokenQueue,thisGroup));
		
		//check for GROUP_CLOSE
		if (tokenQueue.poll().getType()!=TokenTypes.GROUP_CLOSE) throw new StellarisSaveFileParseException("Named group doesn't start with GROUP_CLOSE.");
		
		return thisGroup;
	}
	
	@SuppressWarnings("unchecked")
	private static UnnamedGroup parseUnnamedGroup(Deque<SaveFileToken> tokenQueue) throws StellarisSaveFileParseException
	{
		//check to see if the first element is GROUP_OPEN
		if (tokenQueue.poll().getType()!=TokenTypes.GROUP_OPEN) throw new StellarisSaveFileParseException("Unnamed group doesn't start with GROUP_OPEN.");
		
		//determine unnamed group's type
		TokenTypes internalTokenType = tokenQueue.peek().getType();
		SaveElementTypes childrenType;
		
		//determine the type of the groups children
		switch (internalTokenType)
		{
			case INTEGER:
				childrenType = SaveElementTypes.INTEGER;
				break;
			case FLOATING_PT:
				childrenType = SaveElementTypes.FLOATING_PT;
				break;
			case STRING:
				childrenType = SaveElementTypes.STRING;
				break;
			case GROUP_OPEN:
				childrenType = (isNextGroupNamed(tokenQueue) ? SaveElementTypes.NAMED_GROUP : SaveElementTypes.UNNAMED_GROUP);
				break;
			default:
			case KEY:
			case EQUALS_SIGN:
			case ERROR:
			case GROUP_CLOSE:
			case GS_END:
				throw new StellarisSaveFileParseException("Unnamed group has illegal internal tokens");
		}
		
		//parse the children based off the known type of the groups children
		UnnamedGroup element;
		
		switch (childrenType)
		{
			case NAMED_GROUP:
				element = new UnnamedGroup<NamedGroup>();
				while (tokenQueue.peek().getType()==TokenTypes.GROUP_OPEN)
				{
					element.add(parseNamedGroup(tokenQueue));
				}
				break;
			case UNNAMED_GROUP:
				element = new UnnamedGroup<UnnamedGroup>();
				while (tokenQueue.peek().getType()==TokenTypes.GROUP_OPEN)
				{
					element.add(parseUnnamedGroup(tokenQueue));
				}
				break;
			case INTEGER:
				element = new UnnamedGroup<DSInteger>();
				while (tokenQueue.peek().getType() == TokenTypes.INTEGER)
				{
					element.add(new DSInteger(((IntegerToken) tokenQueue.poll()).getValue()));
				}
				break;
			case FLOATING_PT:
				element = new UnnamedGroup<DSFloat>();
				while (tokenQueue.peek().getType() == TokenTypes.FLOATING_PT)
				{
					element.add(new DSFloat(((FloatingPointToken) tokenQueue.poll()).getValue()));
				}
				break;
			case STRING:
				element = new UnnamedGroup<DSString>();
				while (tokenQueue.peek().getType() == TokenTypes.STRING)
				{
					element.add(new DSString(((StringToken) tokenQueue.poll()).getValue()));
				}
				break;
			default:
				throw new StellarisSaveFileParseException("Fatal Error in Structural Analyzer. Submit a bug report with the file you tried to parse");
		}
		
		//check for the GROUP_CLOSE
		if (tokenQueue.poll().getType()!=TokenTypes.GROUP_CLOSE) throw new StellarisSaveFileParseException("Unnamed Groups must be closed by a GROUP_CLOSE.");
		
		return element;
	}
}
