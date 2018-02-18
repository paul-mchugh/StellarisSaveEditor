package info.paulmchugh.stellariseditor.fileparser;

public class IntegerToken extends SaveFileToken
{
	private long value;
	
	//construct the token with its value
	public IntegerToken(long value)
	{
		super(TokenTypes.INTEGER);
		this.value = value;
	}
	
	public long getValue()
	{
		return value;
	}
}
