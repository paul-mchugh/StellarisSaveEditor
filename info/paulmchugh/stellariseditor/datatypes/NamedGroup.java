package info.paulmchugh.stellariseditor.datatypes;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class NamedGroup implements StellarisGroup
{
	private Map<String, ArrayList<SaveElement>> keyValueMappings;
	private ArrayList<String> keyPrintOrder;
	private int count;
	
	public NamedGroup()
	{
		keyValueMappings = new TreeMap<>();
		keyPrintOrder = new ArrayList<>();
		count = 0;
	}
	
	//inserts the KV pair as the last entry with this key
	public void insert(String key, SaveElement value)
	{
		//to insert the key as the last occurance of a key we need to check the size of the value array for our key
		//if the key doesn't exist yet then we can pass 0 b/c it will go in the fist position
		insert(key, keyValueMappings.containsKey(key) ? keyValueMappings.get(key).size() : 0, value);
	}
	
	public void insert(String key, int index, SaveElement value)
	{
		if (!keyValueMappings.containsKey(key))
		{
			//if an array doesn't exist for values that share this key then make it
			keyValueMappings.put(key, new ArrayList<>());
		}
		//insert the value in the list of values that share this key
		keyValueMappings.get(key).add(index, value);
		
		//increment count
		count++;
		
		//the keys should print in the order they were inserted by default, not alphebetical order.
		//keep track of the order the keys were inserted in.
		if (!keyPrintOrder.contains(key))
		{
			keyPrintOrder.add(key);
		}
	}
	
	//change the value of the first instance of key
	public void modify(String key, SaveElement value)
	{
		modify(key, 0, value);
	}
	
	//modify the value of the index occurance of key
	public void modify(String key, int index, SaveElement value)
	{
		keyValueMappings.get(key).set(index, value);
	}
	
	//remove the value of the first instance of key
	public void remove(String key, int index)
	{
		//if the key exists then we remove it
		if (keyValueMappings.containsKey(key))
		{
			keyValueMappings.get(key).remove(index);
			
			//update the count
			count--;
			
			//if key has no values then remove it from the mapping and the print order
			if (keyValueMappings.get(key).size()==0)
			{
				keyValueMappings.remove(key);
				keyPrintOrder.remove(key);
			}
		}
	}
	
	//remove all values associated with key
	public void removeAll(String key)
	{
		if (keyValueMappings.containsKey(key))
		{
			//decrement the count by the number values that are associated woth key
			count -= keyValueMappings.get(key).size();
			
			//remove key from both the mapping and the print order
			keyValueMappings.remove(key);
			keyPrintOrder.remove(key);
			
		}
	}
	
	//gets the vaue associated with the key
	public SaveElement get(String key)
	{
		return get(key, 0);
	}
	
	//gets a value based on its key and its index
	public SaveElement get(String key, int index)
	{
		return getAllValuesForKey(key).get(index);
	}
	
	//get all values for key
	public ArrayList<SaveElement> getAllValuesForKey(String key)
	{
		return new ArrayList<>(keyValueMappings.get(key));
	}
	
	//move the value at oldIndex to newIndex
	public void reorderValues(String key, int oldIndex, int newIndex)
	{
		//removes the value at old index and re inserts it at newIndex
		SaveElement value = keyValueMappings.get(key).remove(oldIndex);
		keyValueMappings.get(key).add(newIndex, value);
	}
	
	//the keyPrintOrder is used to print out the keys in the order they appear in the keyPrintOrder
	//if a keys that do not appear in the key print order will appear in alphebetical order after all keys that were in the print order
	//this method returns a copy of the key print order
	public ArrayList<String> getKeyPrintOrder()
	{
		return new ArrayList<>(keyPrintOrder);
	}
	
	//the keyPrintOrder is used to print out the keys in the order they appear in the keyPrintOrder
	//if a keys that do not appear in the key print order will appear in alphebetical order after all keys that were in the print order
	//this method sets the keyPrintOrder
	public void setKeyPrintOrder(ArrayList<String> keyPrintOrder)
	{
		if (keyPrintOrder != null)
		{
			this.keyPrintOrder = keyPrintOrder;
		}
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
	public String getSaveRepresentation(int indents) throws StateInvalidForSavingException
	{
		//if the group is empty then it can't be printed
		if (this.size()==0) throw new StateInvalidForSavingException("An UnnamedGroup is invalid for saving.", SaveFailureReason.EMPTY_NAMED_GROUP);
		
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
		result.append('{');
		result.append('\n');
		
		//first iterate through the keyPrintOrder and print all the children in the order they appear there
		for(String key : keyPrintOrder)
		{
			if (keyValueMappings.containsKey(key))
			{
				for (SaveElement value : keyValueMappings.get(key))
				{
					appendKVToStringBuilder(result, key, value, indentString, indents);
				}
			}
		}
		
		//then print all the remaining keys and values that are not on the printOrder in alphebetic order
		for (String key : keyValueMappings.keySet())
		{
			if (!keyPrintOrder.contains(key))
			{
				for (SaveElement value : keyValueMappings.get(key))
				{
					appendKVToStringBuilder(result, key, value, indentString, indents);
				}
			}
		}
		
		result.append(indentString);
		result.append('}');
		
		return result.toString();
	}
	
	private void appendKVToStringBuilder(StringBuilder builder, String key, SaveElement value, String indentString, int indents) throws StateInvalidForSavingException
	{
		builder.append(indentString);
		//the following line is a nightmare and needs further explanation, however it works
		//when the root is being printed we need its children to be at indent 0, so we pass -1 as the indent depth
		//for the root so its children will be at depth 0.  We normally just print this groups indent in (indentString)
		//then one more tab for the child's first line (the tab in the next line).  But, when we have an indent value
		//of -1 we need the child to have zero tabs.  We obviously can't have a -1 tabs in the indentString, so if we
		//are at -1 indents we don't apply the first line indent to make up for this.
		if(indents>=0) builder.append('\t');
		builder.append(key);
		builder.append('=');
		builder.append(value.getSaveRepresentation(indents + 1));
		builder.append('\n');
	}
}
