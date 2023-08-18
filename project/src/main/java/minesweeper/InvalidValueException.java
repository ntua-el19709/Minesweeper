package minesweeper;

/**
 * The exception when the values of the scenario file are not correct
 * @author Andreas Kalavas
 */
public class InvalidValueException extends Exception{
    /**
     * The constructor of the Exception
     * @param errorMessage The message of the exception
     */
    public InvalidValueException(String errorMessage) {
        super(errorMessage);
    }
}
