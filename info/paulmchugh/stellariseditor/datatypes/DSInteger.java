package info.paulmchugh.stellariseditor.datatypes;

import java.util.Objects;

public class DSInteger implements SaveElement
{
	private long value;
	
	public DSInteger()
	{
		this(0);
	}
	
	public DSInteger(long value)
	{
		this.value = value;
	}
	
	public long getValue()
	{
		return value;
	}
	
	public void setValue(long value)
	{
		this.value = value;
	}
	
	@Override
	public SaveElementTypes getElementType()
	{
		return SaveElementTypes.INTEGER;
	}
	
	@Override
	public String getSaveRepresentation(int indents)
	{
		return Long.toString(value);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DSInteger integer = (DSInteger) o;
		return value == integer.value;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(value);
	}
}
