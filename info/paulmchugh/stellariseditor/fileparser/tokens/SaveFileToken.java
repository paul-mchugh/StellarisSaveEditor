package info.paulmchugh.stellariseditor.fileparser.tokens;


import java.util.Objects;

public class SaveFileToken
{
	protected TokenTypes type;
	
	public SaveFileToken(TokenTypes type)
	{
		this.type = type;
	}
	
	//gets the token type
	public TokenTypes getType()
	{
		return type;
	}
	
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SaveFileToken that = (SaveFileToken) o;
		return type == that.type;
	}
	
	@Override
	public int hashCode()
	{
		
		return Objects.hash(type);
	}
}
