package test.info.paulmchugh.stellariseditor.fileparser;

import info.paulmchugh.stellariseditor.datatypes.*;
import info.paulmchugh.stellariseditor.fileparser.StellarisSaveFileParseException;
import info.paulmchugh.stellariseditor.fileparser.StructuralAnalyser;
import info.paulmchugh.stellariseditor.fileparser.tokens.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Deque;
import java.util.LinkedList;

public class StructuralAnalyzerTest
{
	
	@Test
	public void test1_sa() throws StellarisSaveFileParseException
	{
		//create expectedGroup
		RootGroup expectedGroup = new RootGroup();
		expectedGroup.insert("an_int", new DSInteger(1337));
		expectedGroup.insert("a_float", new DSFloat(1991.865));
		expectedGroup.insert("a_string", new DSString("hello"));
		NamedGroup namedInternalGroup = new NamedGroup();
		namedInternalGroup.insert("internal_key", new DSInteger(1000));
		NamedGroup groupInGroup = new NamedGroup();
		groupInGroup.insert("two_deep", new DSFloat(20.02));
		namedInternalGroup.insert("group_in_group", groupInGroup);
		expectedGroup.insert("a_named_group", namedInternalGroup);
		UnnamedGroup<DSInteger> integerGroup = new UnnamedGroup<>();
		integerGroup.add(new DSInteger(1000));
		integerGroup.add(new DSInteger(1001));
		integerGroup.add(new DSInteger(1002));
		integerGroup.add(new DSInteger(1003));
		expectedGroup.insert("an_unnamed_group", integerGroup);
		
		//create and populate testQueue
		Deque<SaveFileToken> testQueue = new LinkedList<>();
		testQueue.addLast(new KeyToken("an_int"));//an_int=1337
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new IntegerToken(1337));
		testQueue.addLast(new KeyToken("a_float"));//a_float=1991.865
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new FloatingPointToken(1991.865));
		testQueue.addLast(new KeyToken("a_string"));//a_string="hello"
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new StringToken("hello"));
		testQueue.addLast(new KeyToken("a_named_group"));//a_named_group={
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		testQueue.addLast(new KeyToken("internal_key"));//internal_key=1000
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new IntegerToken(1000));
		testQueue.addLast(new KeyToken("group_in_group"));//group_in_group={
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		testQueue.addLast(new KeyToken("two_deep"));//two_deep=20.02
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new FloatingPointToken(20.02));
		testQueue.addLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));//end of group_in_group
		testQueue.addLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));//end of a_named_group
		testQueue.addLast(new KeyToken("an_unnamed_group"));//an_unnamed_group={
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		testQueue.addLast(new IntegerToken(1000));
		testQueue.addLast(new IntegerToken(1001));
		testQueue.addLast(new IntegerToken(1002));
		testQueue.addLast(new IntegerToken(1003));
		testQueue.addLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));
		testQueue.addLast(new SaveFileToken(TokenTypes.GS_END));
		
		RootGroup actualGroup = StructuralAnalyser.analyseTokenQueue(testQueue);
		Assert.assertEquals(expectedGroup, actualGroup);
	}
	
	@Test
	public void test2_KVPairTest() throws StellarisSaveFileParseException
	{
		RootGroup expectedGroup = new RootGroup();
		expectedGroup.insert("int_key", new DSInteger(100));
		expectedGroup.insert("int_key", new DSInteger(100));
		expectedGroup.insert("int_key", new DSInteger(101));
		
		Deque<SaveFileToken> testQueue = new LinkedList<>();
		testQueue.addLast(new KeyToken("int_key"));
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new IntegerToken(100));
		testQueue.addLast(new KeyToken("int_key"));
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new IntegerToken(100));
		testQueue.addLast(new KeyToken("int_key"));
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new IntegerToken(101));
		testQueue.addLast(new SaveFileToken(TokenTypes.GS_END));
		RootGroup actualGroup = StructuralAnalyser.analyseTokenQueue(testQueue);
		
		Assert.assertEquals(expectedGroup, actualGroup);
	}
	
	@Test
	public void test3_NestedGroupTest() throws StellarisSaveFileParseException
	{
		RootGroup expectedGroup = new RootGroup();
		NamedGroup childGroup = new NamedGroup();
		NamedGroup groupInGroup = new NamedGroup();
		groupInGroup.insert("dummy", new DSFloat(10.01));
		childGroup.insert("group_in_group", groupInGroup);
		expectedGroup.insert("top_child", childGroup);
		
		Deque<SaveFileToken> testQueue = new LinkedList<>();
		testQueue.addLast(new KeyToken("top_child"));
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		testQueue.addLast(new KeyToken("group_in_group"));
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new SaveFileToken(TokenTypes.GROUP_OPEN));
		testQueue.addLast(new KeyToken("dummy"));
		testQueue.addLast(new SaveFileToken(TokenTypes.EQUALS_SIGN));
		testQueue.addLast(new FloatingPointToken(10.01));
		testQueue.addLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));
		testQueue.addLast(new SaveFileToken(TokenTypes.GROUP_CLOSE));
		testQueue.addLast(new SaveFileToken(TokenTypes.GS_END));
		RootGroup actualGroup = StructuralAnalyser.analyseTokenQueue(testQueue);
		
		Assert.assertEquals(expectedGroup, actualGroup);
	}
	
	
}
