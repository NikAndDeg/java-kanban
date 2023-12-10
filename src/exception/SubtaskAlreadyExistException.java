package exception;

public class SubtaskAlreadyExistException extends RuntimeException {
	public SubtaskAlreadyExistException(String message) {
		super(message);
	}
}
