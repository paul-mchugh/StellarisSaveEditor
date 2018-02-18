package info.paulmchugh.stellariseditor.fileparser;

public class KeyToken extends StringToken
{
	public KeyToken(String value)
	{
		super(value);
		type = TokenTypes.KEY;
	}
}
