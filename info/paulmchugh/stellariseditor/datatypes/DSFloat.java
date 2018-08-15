package info.paulmchugh.stellariseditor.datatypes;

import java.util.Objects;

public class DSFloat implements SaveElement
{
	private double value;
	
	public DSFloat()
	{
		this(0);
	}
	
	public DSFloat(double value)
	{
		this.value = value;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public void setValue(double value)
	{
		this.value = value;
	}
	
	
	@Override
	public SaveElementTypes getElementType()
	{
		return SaveElementTypes.FLOATING_PT;
	}
	
	@Override
	public String getSaveRepresentation(int indents)
	{
		return Double.toString(value);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof DSFloat)) return false;
		DSFloat dsFloat = (DSFloat) o;
		return Double.compare(dsFloat.value, value) == 0;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(value);
	}
}
