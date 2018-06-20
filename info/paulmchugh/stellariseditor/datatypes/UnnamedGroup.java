package info.paulmchugh.stellariseditor.datatypes;

import java.util.ArrayList;

public class UnnamedGroup<T extends SaveElement> extends ArrayList<T> implements StellarisGroup
{
	
	@Override
	public SaveElementTypes getElementType()
	{
		return SaveElementTypes.UNNAMED_GROUP;
	}
	
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
		boolean shouldAppendFinalIndent = false;
		
		//build the result string from the opening and closing group symbols and child elements
		StringBuilder result = new StringBuilder();
		result.append(indentString);
		result.append('{');
		result.append('\n');
		
		//apend all the children
		for(T childElement : this)
		{
			result.append(childElement.getSaveRepresentation(indents + 1));
			
			//if if this UnnamedGroup's children are also groups (of either kind) then they all get their own line
			if (childElement instanceof StellarisGroup)
			{
				result.append('\n');
				shouldAppendFinalIndent = true;
			}
			else
			{
				//if this UnnamedGroup's children are not groups then they get a space to seperate them
				result.append(' ');
				shouldAppendFinalIndent = false;
			}
		}
		
		if (shouldAppendFinalIndent) result.append(indentString);
		result.append('}');
		
		return result.toString();
	}
}
