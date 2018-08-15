package info.paulmchugh.stellariseditor.datatypes;

import java.util.Objects;

public class DSString implements SaveElement
{
	private String value;
	
	public DSString()
	{
		this("");
	}
	
	public DSString(String value)
	{
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value!=null ? value : "";
	}
	
	@Override
	public SaveElementTypes getElementType()
	{
		return SaveElementTypes.STRING;
	}
	
	@Override
	public String getSaveRepresentation(int indents)
	{
		return "\""+value+"\"";
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DSString dsString = (DSString) o;
		return Objects.equals(value, dsString.value);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(value);
	}
}
