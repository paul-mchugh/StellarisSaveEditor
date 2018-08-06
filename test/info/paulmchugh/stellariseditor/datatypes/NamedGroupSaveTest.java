package test.info.paulmchugh.stellariseditor.datatypes;

import info.paulmchugh.stellariseditor.datatypes.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class NamedGroupSaveTest
{
	@Test
	public void testPrintGroup1() throws StateInvalidForSavingException
	{
		//just a named group with an int inside (that is l33t)
		String expectedString = "{\n\tan_int=1337\n}";
		NamedGroup group = new NamedGroup();
		
		group.insert("an_int", new DSInteger(1337));
		
		String actualString = group.getSaveRepresentation(0);
		
		Assert.assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testPrintGroup2() throws StateInvalidForSavingException
	{
		//named group in named group
		String expectedString = "{\n\tinside_grp={\n\t\tinside_int=9000\n\t}\n\tlook_an_int=1000\n}";
		
		NamedGroup outer = new NamedGroup();
		NamedGroup inner = new NamedGroup();
		
		outer.insert("inside_grp", inner);
		outer.insert("look_an_int", new DSInteger(1000));
		inner.insert("inside_int", new DSInteger(9000));
		
		String actualString = outer.getSaveRepresentation(0);
		
		Assert.assertEquals(expectedString, actualString);
	}
	
	@Test
	public void test3_emptyGroupTest()
	{
		NamedGroup group = new NamedGroup();
		
		try
		{
			group.getSaveRepresentation(0);
			Assert.fail("No StateInvalidForSavingException thrown after empty NamedGroup save was attempted.");
		}
		catch (StateInvalidForSavingException e)
		{
			Assert.assertEquals(SaveFailureReason.EMPTY_NAMED_GROUP, e.getSaveFailReason());
		}
	}
	
	@Test
	public void test4_quotedKeys() throws StateInvalidForSavingException
	{
		String expectedString = "{\n\ti_am_a_string=\"a string\"\n\ti_am_a_float=2.9\n\t\"i am a quoted key and int\"=5\n\t\"i am a qk and string\"=\"hi\"\n}";
		
		NamedGroup group = new NamedGroup();
		
		group.insert("i_am_a_string", new DSString("a string"));
		group.insert("i_am_a_float", new DSFloat(2.9));
		group.insert("\"i am a quoted key and int\"", new DSInteger(5));
		group.insert("\"i am a qk and string\"", new DSString("hi"));
		
		String actualString = group.getSaveRepresentation(0);
		
		Assert.assertEquals(expectedString, actualString);
	}
	
	@Test
	public void test5_printOrder() throws StateInvalidForSavingException
	{
		NamedGroup group = new NamedGroup();
		
		group.insert("c_key", new DSInteger(5));
		group.insert("b_key", new DSInteger(5));
		group.insert("a_key", new DSInteger(5));
		
		//test that the result is correct
		String expectedFirst = "{\n\tc_key=5\n\tb_key=5\n\ta_key=5\n}";
		String actualFirst = group.getSaveRepresentation(0);
		Assert.assertEquals(expectedFirst,actualFirst);
		
		//test that the print order is correct
		ArrayList<String> expectedFirstOrder = new ArrayList<>();
		expectedFirstOrder.add("c_key");
		expectedFirstOrder.add("b_key");
		expectedFirstOrder.add("a_key");
		ArrayList<String> actualFirstOrder  = group.getKeyPrintOrder();
		Assert.assertEquals(expectedFirstOrder, actualFirstOrder);
		
		//clear the print order
		group.setKeyPrintOrder(new ArrayList<>());
		
		//test to see if it is printing in default alphebetical order
		String expectedSecond = "{\n\ta_key=5\n\tb_key=5\n\tc_key=5\n}";
		String actualSecond = group.getSaveRepresentation(0);
		Assert.assertEquals(expectedSecond, actualSecond);
		
	}
}
