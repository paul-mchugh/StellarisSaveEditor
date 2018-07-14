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
	public String getSaveRepresentation(int indents) throws StateInvalidForSavingException
	{
		//if the group is empty then it can't be printed
		if (this.size()==0) throw new StateInvalidForSavingException("An UnnamedGroup is invalid for saving.", SaveFailureReason.EMPTY_UNNAMED_GROUP);
		
		//each indent is one tab
		//create the string of indents to prepend the elements inside
		StringBuilder indentStringBuilder = new StringBuilder();
		for(int i = 0; i < indents; i++)
		{
			indentStringBuilder.append('\t');
		}
		String indentString = indentStringBuilder.toString();
		boolean childIsGroup = false;
		
		//build the result string from the opening and closing group symbols and child elements
		StringBuilder result = new StringBuilder();
		result.append('{');
		
		//append all the children
		for(T childElement : this)
		{
			//if if this UnnamedGroup's children are also groups (of either kind) then they all get their own line
			if (childElement instanceof StellarisGroup)
			{
				result.append('\n');
				result.append(indentString);
				result.append('\t');
				childIsGroup = true;
			}
			else
			{
				//if this UnnamedGroup's children are not groups then they get a space to seperate them
				result.append(' ');
				childIsGroup = false;
			}
			
			result.append(childElement.getSaveRepresentation(indents + 1));
			
		}
		
		//if the children are groups then we need to add a final new line and indent
		if (childIsGroup)
		{
			result.append('\n');
			result.append(indentString);
		}
		
		//closing parenthesis
		result.append('}');
		
		return result.toString();
	}
}
