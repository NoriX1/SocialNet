package services.exceptions;

public class NoUserFoundInDBException extends RuntimeException{
    public NoUserFoundInDBException(String s)
    {
        super(s);
    }
}
