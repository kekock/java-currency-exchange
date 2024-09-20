package Exceptions;

public class MissingFormFieldsException extends RuntimeException{
    public MissingFormFieldsException(){
        super("Missing Form Fields");
    }

    public MissingFormFieldsException(String message){
        super(message);
    }
}
