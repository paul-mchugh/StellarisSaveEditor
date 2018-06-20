package info.paulmchugh.stellariseditor.datatypes;

public interface SaveElement
{
	SaveElementTypes getElementType();
	
	//gets the string representation of this element that is suitable for writing to a decompressed .sav file
	//the first line of the element's description should not be indented
	//all subsequent lines should be indented by at least as many tabs as the parameter indents specifies
	//if an element can be represented by a single line then it should not be indented
	String getSaveRepresentation(int indents);
}
