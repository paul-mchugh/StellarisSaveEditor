package test.info.paulmchugh.stellariseditor.datatypes;

import info.paulmchugh.stellariseditor.datatypes.DSInteger;
import info.paulmchugh.stellariseditor.datatypes.SaveFailureReason;
import info.paulmchugh.stellariseditor.datatypes.StateInvalidForSavingException;
import info.paulmchugh.stellariseditor.datatypes.UnnamedGroup;
import org.junit.Assert;
import org.junit.Test;

public class UnnamedGroupSaveTest
{
	@Test
	public void test1_nonGroupChildren() throws StateInvalidForSavingException
	{
		//a bunch of numbers in a unnamed group
		
		String expectedString = "{ 213421 234234 546456 867671 562433 987885}";
		
		UnnamedGroup<DSInteger> numbers = new UnnamedGroup<>();
		
		numbers.add(new DSInteger(213421));
		numbers.add(new DSInteger(234234));
		numbers.add(new DSInteger(546456));
		numbers.add(new DSInteger(867671));
		numbers.add(new DSInteger(562433));
		numbers.add(new DSInteger(987885));
		
		String actualString = numbers.getSaveRepresentation(0);
		
		Assert.assertEquals(expectedString, actualString);
	}
	
	@Test
	public void test2_groupChildren() throws StateInvalidForSavingException
	{
		//a group with other groups as children
		
		String expectedString = "{\n\t{ 1}\n\t{\n\t\t{ 1337}\n\t}\n\t{ 3}\n\t{ 4}\n}";
		
		UnnamedGroup<UnnamedGroup> parent	= new UnnamedGroup<>();
		UnnamedGroup<DSInteger> child1		= new UnnamedGroup<>();
		UnnamedGroup<UnnamedGroup> child2		= new UnnamedGroup<>();
		UnnamedGroup<DSInteger> child3		= new UnnamedGroup<>();
		UnnamedGroup<DSInteger> child4		= new UnnamedGroup<>();
		UnnamedGroup<DSInteger> subgroup	= new UnnamedGroup<>();
		
		parent.add(child1);
		parent.add(child2);
		parent.add(child3);
		parent.add(child4);
		subgroup.add(new DSInteger(1337));
		
		child1.add(new DSInteger(1));
		child2.add(subgroup);
		child3.add(new DSInteger(3));
		child4.add(new DSInteger(4));
		
		String actualString = parent.getSaveRepresentation(0);
		
		Assert.assertEquals(expectedString, actualString);
	}
	
	@Test
	public void test3_emptyGroupTest()
	{
		UnnamedGroup<DSInteger> group= new UnnamedGroup<>();
		
		try
		{
			group.getSaveRepresentation(0);
			Assert.fail("No StateInvalidForSavingException thrown after empty UnnamedGroup save was attempted.");
		}
		catch (StateInvalidForSavingException e)
		{
			Assert.assertEquals(SaveFailureReason.EMPTY_UNNAMED_GROUP, e.getSaveFailReason());
		}
	}
}
