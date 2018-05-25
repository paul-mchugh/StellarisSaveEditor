package info.paulmchugh.stellariseditor.fileparser.tokens;

import java.util.Objects;

public class StringToken extends SaveFileToken
{
	private String value;
	public StringToken(String value)
	{
		super(TokenTypes.STRING);
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		StringToken that = (StringToken) o;
		return Objects.equals(value, that.value);
	}
	
	@Override
	public int hashCode()
	{
		
		return Objects.hash(super.hashCode(), value);
	}
}
