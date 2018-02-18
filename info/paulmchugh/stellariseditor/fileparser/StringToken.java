package info.paulmchugh.stellariseditor.fileparser;

public class StringToken extends SaveFileToken
{
	String value;
	public StringToken(String value)
	{
		super(TokenTypes.STRING);
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
}