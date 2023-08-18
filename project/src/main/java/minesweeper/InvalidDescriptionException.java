package minesweeper;

/**
 * The exception when the scenario file does not have the 4 required lines
 * @author Andreas Kalavas
 */
public class InvalidDescriptionException extends Exception{
    /**
     * The constructor of the Exception
     * @param errorMessage The message of the exception
     */
    public InvalidDescriptionException(String errorMessage) {
        super(errorMessage);
    }
}
