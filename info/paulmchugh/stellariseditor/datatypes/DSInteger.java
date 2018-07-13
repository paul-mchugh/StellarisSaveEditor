package info.paulmchugh.stellariseditor.datatypes;

public class DSInteger implements SaveElement
{
	private long value;
	
	public DSInteger()
	{
		value = 0;
	}
	
	public DSInteger(int value)
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
}
