package info.paulmchugh.stellariseditor.datatypes;

public class StateInvalidForSavingException extends Exception
{
	SaveFailureReason reason = SaveFailureReason.UNKNOWN_ERROR;
	
	public StateInvalidForSavingException(String message, SaveFailureReason reason)
	{
		super(message);
		this.reason = reason;
	}
	
	public SaveFailureReason getSaveFailReason()
	{
		return reason;
	}
}
