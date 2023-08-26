package filemanager;

import java.io.FileNotFoundException;

public interface FileManager {
    char getNextCharacter();
    void openFile(String path) throws FileNotFoundException;
    void closeCurrentFile() throws FileNotFoundException;
    int getCurrentLineNumber();
    int getCurrentColumnNumber();
    String getLine(int lineNumber);
    boolean isEOF();
}
