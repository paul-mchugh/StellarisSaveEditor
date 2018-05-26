package test.info.paulmchugh.stellariseditor.fileparser;

import info.paulmchugh.stellariseditor.fileparser.GamestateTokenizer;
import info.paulmchugh.stellariseditor.fileparser.StellarisSaveFileParseException;
import info.paulmchugh.stellariseditor.fileparser.tokens.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;


public class GamestateTokenizerTest
{
	@Test
	public void test1ValidFile() throws IOException, StellarisSaveFileParseException
	{
		//get the actual tokens
		Deque<SaveFileToken> actualTokenQueue = tokenQueueFromTestFile("/test/info/paulmchugh/stellariseditor/fileparser/resources/save_file_token_test_1.txt");
		
		//build a token queue w/ the expected tokens
		Deque<SaveFileToken> expectedTokenQueue = new LinkedList<>();
		expectedTokenQueue.offerLast(new KeyToken("hello"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokenQueue.offerLast(new IntegerToken(13));
		expectedTokenQueue.offerLast(new KeyToken("\"letters space\""));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokenQueue.offerLast(new IntegerToken(42));
		expectedTokenQueue.offerLast(new KeyToken("0"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokenQueue.offerLast(new FloatingPointToken(123.123));
		expectedTokenQueue.offerLast(new KeyToken("a_group"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		expectedTokenQueue.offerLast(new IntegerToken(4));
		expectedTokenQueue.offerLast(new IntegerToken(34));
		expectedTokenQueue.offerLast(new IntegerToken(3));
		expectedTokenQueue.offerLast(new IntegerToken(165));
		expectedTokenQueue.offerLast(new IntegerToken(51));
		expectedTokenQueue.offerLast(new IntegerToken(111));
		expectedTokenQueue.offerLast(new IntegerToken(525));
		expectedTokenQueue.offerLast(new IntegerToken(121));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));
		expectedTokenQueue.offerLast(new KeyToken("339"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokenQueue.offerLast(new IntegerToken(47));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GS_END));
		
		//do the test
		Assert.assertEquals(expectedTokenQueue, actualTokenQueue);
		
	}
	
	@Test
	public void test2ValidFile() throws  IOException, StellarisSaveFileParseException
	{
		//get the actual tokens
		Deque<SaveFileToken> actualTokenQueue = tokenQueueFromTestFile("/test/info/paulmchugh/stellariseditor/fileparser/resources/save_file_token_test_2.txt");
		
		//build a token queue w/ the expected tokens
		Deque<SaveFileToken> expectedTokenQueue = new LinkedList<>();
		expectedTokenQueue.offerLast(new KeyToken("outer_group"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		expectedTokenQueue.offerLast(new KeyToken("inner_array"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		expectedTokenQueue.offerLast(new StringToken("one"));
		expectedTokenQueue.offerLast(new StringToken("two"));
		expectedTokenQueue.offerLast(new StringToken("\\three"));
		expectedTokenQueue.offerLast(new StringToken("four\\"));
		expectedTokenQueue.offerLast(new StringToken("five"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));
		expectedTokenQueue.offerLast(new KeyToken("0"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		expectedTokenQueue.offerLast(new IntegerToken(1));
		expectedTokenQueue.offerLast(new IntegerToken(2));
		expectedTokenQueue.offerLast(new IntegerToken(3));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));
		expectedTokenQueue.offerLast(new KeyToken("1"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokenQueue.offerLast(new StringToken("word"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GS_END));
		
		//do the test
		Assert.assertEquals(expectedTokenQueue, actualTokenQueue);
	}
	
	//tests for improperly terminated strings
	@Test
	public void test3InvalidFile() throws IOException
	{
		try
		{
			//save_file_token_test_3.txt contains a StringToken at the end with no terminator quote
			tokenQueueFromTestFile("/test/info/paulmchugh/stellariseditor/fileparser/resources/save_file_token_test_3.txt");
			Assert.fail("Was supposed to throw exception on invalid file but didn't");
		}
		catch (StellarisSaveFileParseException e)
		{
			//was supposed to fail
		}
	}
	
	//tests for improperly terminated strings
	@Test
	public void test4ValidFileStringsTest() throws IOException, StellarisSaveFileParseException
	{
		//get the actual tokens
		Deque<SaveFileToken> actualTokenQueue = tokenQueueFromTestFile("/test/info/paulmchugh/stellariseditor/fileparser/resources/save_file_token_test_4.txt");
		
		//build a token queue w/ the expected tokens
		Deque<SaveFileToken> expectedTokenQueue = new LinkedList<>();
		expectedTokenQueue.offerLast(new KeyToken("group_with_strings"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		expectedTokenQueue.offerLast(new StringToken("\\"));
		expectedTokenQueue.offerLast(new StringToken("\""));
		expectedTokenQueue.offerLast(new StringToken("{"));
		expectedTokenQueue.offerLast(new StringToken("="));
		expectedTokenQueue.offerLast(new StringToken("}"));
		expectedTokenQueue.offerLast(new StringToken("strings with spaces"));
		expectedTokenQueue.offerLast(new StringToken("ordinary_string"));
		expectedTokenQueue.offerLast(new StringToken("string with lots of potentially problematic components \\ \" { = } done"));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));
		expectedTokenQueue.offerLast(new SaveFileToken(TokenTypes.GS_END));
		
		//do the test
		Assert.assertEquals(expectedTokenQueue, actualTokenQueue);
	}
	
	private static Deque<SaveFileToken> tokenQueueFromTestFile(String path) throws IOException, StellarisSaveFileParseException
	{
		//get the instream from the uncompressed resource file
		InputStream instream = GamestateTokenizerTest.class.getResourceAsStream(path);
		
		
		//return the tokenized input stream
		return GamestateTokenizer.generateTokenQueueFromGamestate(instream);
	}
}
