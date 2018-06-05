package info.paulmchugh.stellariseditor.datatypes;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class NamedGroup implements StellarisGroup
{
	private Map<String, ArrayList<SaveElement>> keyValuePairs;
	private int count;
	
	public NamedGroup()
	{
		keyValuePairs = new TreeMap<>();
		count = 0;
	}
	
	@Override
	public int size()
	{
		return count;
	}
	
	@Override
	public SaveElementTypes getElementType()
	{
		return SaveElementTypes.NAMED_GROUP;
	}
	
	@Override
	public String getSaveRepresentation(int indents)
	{
		return null;
	}
}
