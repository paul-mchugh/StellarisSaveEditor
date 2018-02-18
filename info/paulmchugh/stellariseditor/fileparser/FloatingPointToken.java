package info.paulmchugh.stellariseditor.fileparser;

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
}
