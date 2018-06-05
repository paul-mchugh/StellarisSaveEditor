package info.paulmchugh.stellariseditor.datatypes;

import java.util.ArrayList;

public class UnnamedGroup<T extends SaveElement> extends ArrayList<T> implements StellarisGroup
{
	
	@Override
	public String getSaveRepresentation(int indents)
	{
		//each indent is one tab
		//create the string of indents to prepend the elements inside
		StringBuilder indentStringBuilder = new StringBuilder();
		for(int i = 0; i < indents; i++)
		{
			indentStringBuilder.append('\t');
		}
		String indentString = indentStringBuilder.toString();
		
		//build the result string from the opening and closing group symbols and child elements
		StringBuilder result = new StringBuilder();
		result.append(indentString);
		result.append('{');
		
		//apend all the children
		for(T childElement : this)
		{
			result.append(childElement.getSaveRepresentation(indents + 1));
			result.append('\n');
		}
		
		result.append(indentString);
		result.append('}');
		
		return result.toString();
	}
}
