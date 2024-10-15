package exceptions;

public class MissingFormFieldsException extends RuntimeException{
    public MissingFormFieldsException(){
        super();
    }

    public MissingFormFieldsException(String message){
        super(message);
    }
}
