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
	public void test1ValidToken() throws IOException, StellarisSaveFileParseException
	{
		InputStream instream = this.getClass().getResourceAsStream("/test/info/paulmchugh/stellariseditor/fileparser/resources/save_file_token_test_1.txt");
		
		//get the actual tokens
		Deque<SaveFileToken> actualTokenQueue = GamestateTokenizer.generateTokenQueueFromGamestate(instream);
		
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
}
