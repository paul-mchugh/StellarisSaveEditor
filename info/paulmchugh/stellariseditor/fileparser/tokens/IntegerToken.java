package info.paulmchugh.stellariseditor.fileparser.tokens;

import java.util.Objects;

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
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		IntegerToken that = (IntegerToken) o;
		return value == that.value;
	}
	
	@Override
	public int hashCode()
	{
		
		return Objects.hash(super.hashCode(), value);
	}
}
