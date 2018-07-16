package info.paulmchugh.stellariseditor.datatypes;

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
}
