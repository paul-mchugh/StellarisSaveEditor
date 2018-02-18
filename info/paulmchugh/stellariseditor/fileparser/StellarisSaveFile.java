package info.paulmchugh.stellariseditor.fileparser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class StellarisSaveFile
{
	public StellarisSaveFile(File saveFile) throws IOException
	{
		//open the save file as a zip file
		ZipFile saveReader = new ZipFile(saveFile);
		
		//get the gamestate entry
		//gamestate is the name of the zip entry that stores stellaris's game data
		//there is also an entry called meta that stores meta-data that is displayed when you are choosing the save you want to play
		ZipEntry gamestateEntry = saveReader.getEntry("gamestate");
		
		//start reading the gamestate entry
		InputStream gamestate = saveReader.getInputStream(gamestateEntry);
		
		try
		{
			Queue<SaveFileToken> tokens = SaveFileToken.generateTokenQueueFromGamestate(gamestate);
			tokens.peek();
		}
		catch (StellarisSaveFileParseException|IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//close the file
		saveReader.close();
	}
}
