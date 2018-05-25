package info.paulmchugh.stellariseditor.fileparser.tokens;

import java.util.Objects;

public class FloatingPointToken extends SaveFileToken
{
	private double value;
	
	//construct with value
	public FloatingPointToken(double value)
	{
		super(TokenTypes.FLOATING_PT);
		this.value = value;
	}
	
	public double getValue()
	{
		return value;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		FloatingPointToken that = (FloatingPointToken) o;
		return Double.compare(that.value, value) == 0;
	}
	
	@Override
	public int hashCode()
	{
		
		return Objects.hash(super.hashCode(), value);
	}
}
