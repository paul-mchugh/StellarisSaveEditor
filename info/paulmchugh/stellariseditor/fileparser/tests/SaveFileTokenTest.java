package info.paulmchugh.stellariseditor.fileparser.tests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.*;

import info.paulmchugh.stellariseditor.fileparser.FloatingPointToken;
import info.paulmchugh.stellariseditor.fileparser.IntegerToken;
import info.paulmchugh.stellariseditor.fileparser.KeyToken;
import info.paulmchugh.stellariseditor.fileparser.SaveFileToken;
import info.paulmchugh.stellariseditor.fileparser.StellarisSaveFileParseException;
import info.paulmchugh.stellariseditor.fileparser.StringToken;
import info.paulmchugh.stellariseditor.fileparser.TokenTypes;

public class SaveFileTokenTest
{
	@Test
	public void test1() throws IOException, StellarisSaveFileParseException
	{
		//construct the queue of tokens addLast
		Deque<SaveFileToken> expectedTokens = new LinkedList<SaveFileToken>();
		//what is expected to be in the file
		expectedTokens.addLast(new KeyToken("hello"));
		expectedTokens.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokens.addLast(new IntegerToken(13));
		
		expectedTokens.addLast(new KeyToken("\"letters space\""));
		expectedTokens.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokens.addLast(new IntegerToken(42));
		
		expectedTokens.addLast(new KeyToken("0"));
		expectedTokens.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokens.addLast(new FloatingPointToken(123.123));
		
		expectedTokens.addLast(new KeyToken("a_group"));
		expectedTokens.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokens.addLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		//4 34 3 165 51 111 525 121
		expectedTokens.addLast(new IntegerToken(4));
		expectedTokens.addLast(new IntegerToken(34));
		expectedTokens.addLast(new IntegerToken(3));
		expectedTokens.addLast(new IntegerToken(165));
		expectedTokens.addLast(new IntegerToken(51));
		expectedTokens.addLast(new IntegerToken(111));
		expectedTokens.addLast(new IntegerToken(525));
		expectedTokens.addLast(new IntegerToken(121));
		
		expectedTokens.addLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));
		
		expectedTokens.addLast(new KeyToken("339"));
		expectedTokens.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		expectedTokens.addLast(new IntegerToken(47));
		
		expectedTokens.addLast(new SaveFileToken(TokenTypes.GS_END));
		
		ZipFile saveReader = new ZipFile(new File("src\\info\\paulmchugh\\stellariseditor\\fileparser\\tests\\resources\\save_file_token_test_1.sav"));
		
		//get the gamestate entry
		//gamestate is the name of the zip entry that stores stellaris's game data
		//there is also an entry called meta that stores meta-data that is displayed when you are choosing the save you want to play
		ZipEntry gamestateEntry = saveReader.getEntry("gamestate");
		
		//start reading the gamestate entry
		InputStream gamestate = saveReader.getInputStream(gamestateEntry);
		
		Deque<SaveFileToken> actualTokens = SaveFileToken.generateTokenQueueFromGamestate(gamestate);
		
		saveReader.close();
		
		//make sure that the expected tokens size equals the size of the actual tokens queue
		Assert.assertEquals(expectedTokens.size(), actualTokens.size());
		
		//compare the tokens
		while (!actualTokens.isEmpty())
		{
			SaveFileToken expected = expectedTokens.pollFirst();
			SaveFileToken actual   = actualTokens.pollFirst();
			Assert.assertEquals(expected.getType(), actual.getType());
			if (expected.getType()==TokenTypes.INTEGER)
			{
				Assert.assertEquals(((IntegerToken)expected).getValue(), ((IntegerToken)actual).getValue());
			}
			else if (expected.getType()==TokenTypes.FLOATING_PT)
			{
				Assert.assertEquals(((FloatingPointToken)expected).getValue(), ((FloatingPointToken)actual).getValue(), 0.001);
			}
			else if (expected.getType()==TokenTypes.STRING)
			{
				Assert.assertEquals(((StringToken)expected).getValue(), ((StringToken)actual).getValue());
			}
		}
		
	}
}
