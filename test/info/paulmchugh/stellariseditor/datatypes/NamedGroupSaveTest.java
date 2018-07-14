package test.info.paulmchugh.stellariseditor.datatypes;

import info.paulmchugh.stellariseditor.datatypes.DSInteger;
import info.paulmchugh.stellariseditor.datatypes.NamedGroup;
import org.junit.Assert;
import org.junit.Test;

public class NamedGroupSaveTest
{
	@Test
	public void testPrintGroup1()
	{
		//just a named group with an int inside (that is l33t)
		String expectedString = "{\n\tan_int=1337\n}";
		NamedGroup group = new NamedGroup();
		
		group.insert("an_int", new DSInteger(1337));
		
		String actualString = group.getSaveRepresentation(0);
		
		Assert.assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testPrintGroup2()
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
}
