package filemanager;

import java.io.EOFException;
import java.io.FileNotFoundException;

public interface FileManager {
    char getNextCharacter() throws EOFException;
    void selectCurrentFile(String path) throws FileNotFoundException;
    int currentLineNumber();
}
