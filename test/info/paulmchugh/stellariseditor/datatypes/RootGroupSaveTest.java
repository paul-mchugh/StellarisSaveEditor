package test.info.paulmchugh.stellariseditor.datatypes;

import info.paulmchugh.stellariseditor.datatypes.*;
import org.junit.Assert;
import org.junit.Test;

public class RootGroupSaveTest
{
	@Test
	public void test1_print() throws StateInvalidForSavingException
	{
		String expectedString = "a_float=42.7\na_named_group={\n\tan_int=1337\n\ta_string=\"hello\"\n\tsubgroup={\n\t\tanything=\"\"\n\t}\n}\nan_unnamed_group={ 1000 2012}\n";
		RootGroup rootGroup = new RootGroup();
		
		rootGroup.insert("a_float", new DSFloat(42.7));
		
		NamedGroup childNamedGroup = new NamedGroup();
		childNamedGroup.insert("an_int", new DSInteger(1337));
		childNamedGroup.insert("a_string", new DSString("hello"));
		NamedGroup subChildNG = new NamedGroup();
		subChildNG.insert("anything", new DSString());
		childNamedGroup.insert("subgroup", subChildNG);
		rootGroup.insert("a_named_group", childNamedGroup);
		
		UnnamedGroup<DSInteger> childUnnamedGroupOfInts = new UnnamedGroup<>();
		childUnnamedGroupOfInts.add(new DSInteger(1000));
		childUnnamedGroupOfInts.add(new DSInteger(2012));
		rootGroup.insert("an_unnamed_group",childUnnamedGroupOfInts);
		
		String actualString = rootGroup.getSaveRepresentation();
		Assert.assertEquals(expectedString,actualString);
	}
	
	@Test
	public void test2_rootGroupNotRoot()
	{
		NamedGroup group = new NamedGroup();
		
		RootGroup illegalChildRoot = new RootGroup();
		illegalChildRoot.insert("an_int", new DSInteger(5));
		
		//rootGroups can't be inserted into other groups
		group.insert("illegal_root", illegalChildRoot);
		
		try
		{
			group.getSaveRepresentation(0);
			Assert.fail("No StateInvalidForSavingException thrown after attempted printing of group that illegally has a child that is a root group.");
		}
		catch (StateInvalidForSavingException e)
		{
			Assert.assertEquals(SaveFailureReason.ROOT_GROUP_IS_A_CHILD, e.getSaveFailReason());
		}
		
	}
}
