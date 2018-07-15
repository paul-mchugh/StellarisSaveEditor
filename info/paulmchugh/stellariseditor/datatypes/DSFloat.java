package info.paulmchugh.stellariseditor.datatypes;

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
}
