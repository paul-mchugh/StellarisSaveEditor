package info.paulmchugh.stellariseditor.fileparser;

//Token Type is an enum that is used by SaveFileToken to describe what kind of token it is 
public enum TokenTypes
{
	ERROR,
	INTEGER,
	FLOATING_PT,
	STRING,
	KEY,
	EQUALS_SIGN,
	GROUP_OPEN,
	GROUP_CLOSE,
	GS_END
}