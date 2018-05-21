package info.paulmchugh.stellariseditor.fileparser;



public class SaveFileToken
{
	protected TokenTypes type;
	
	public SaveFileToken(TokenTypes type)
	{
		this.type = type;
	}
	
	//gets the token type
	public TokenTypes getType()
	{
		return type;
	}
	
	
	
	
}
