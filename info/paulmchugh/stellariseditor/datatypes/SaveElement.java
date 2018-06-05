package info.paulmchugh.stellariseditor.datatypes;

public interface SaveElement
{
	SaveElementTypes getElementType();
	String getSaveRepresentation(int indents);//gets the string representation of this element that is suitable for writing to a decompressed .sav file
}
