package info.paulmchugh.stellariseditor.datatypes;

public class RootGroup extends NamedGroup
{
	public String getSaveRepresentation() throws StateInvalidForSavingException
	{
		return getSaveRepresentation(-1);
	}
	
	@Override
	public String getSaveRepresentation(int indents) throws StateInvalidForSavingException
	{
		//the indents value passed in must be less than 0 otherwise that means that this is not the root and
		//somehow this RootGroup got inserted into another group
		if (indents>=0) throw new StateInvalidForSavingException("A RootGroup is the child of another group. It is nested within " + (indents+1) + " groups.",
				SaveFailureReason.ROOT_GROUP_IS_A_CHILD);
		
		String saveRepWithBrackets = super.getSaveRepresentation(-1);
		return saveRepWithBrackets.substring(2, saveRepWithBrackets.length()-1);//remove brackets from the superclass result
	}
}
