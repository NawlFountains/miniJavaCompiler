package filemanager;

import java.io.*;
import java.nio.Buffer;

public class ImpFileManager implements FileManager{
    int currentLineNumber;
    int currentColumnNumber;
    String currentBufferedLine;

    FileReader fileReader;
    BufferedReader bufferedReader;

    public ImpFileManager() {
    }
    public char getNextCharacter() throws EOFException {
        char nextChar;
        try {
            if (currentColumnNumber+1 == currentBufferedLine.length()) {
                currentColumnNumber = 0;
                readNewLine();
            } else {
                currentColumnNumber++;
            }
            nextChar = currentBufferedLine.charAt(currentColumnNumber);
        } catch (Exception e) {
            throw new EOFException("Unexpected End of file");
        }
        return nextChar;
    }

    public void openFile(String path) throws FileNotFoundException {
        try {
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);

            readNewLine();
            currentLineNumber = 0;
            currentColumnNumber = 0;
        } catch (Exception e) {
            throw new FileNotFoundException("File "+path+" couldn't be found");
        }
    }

    public void closeCurrentFile() throws FileNotFoundException {
        try {
            fileReader.close();
        } catch (Exception e) {
            throw new FileNotFoundException("Couldn't close current file");
        }
    }

    public int getCurrentLineNumber() {
        return currentLineNumber;
    }

    public int getCurrentColumnNumber() {
        return currentColumnNumber;
    }

    private void readNewLine() throws IOException {
        currentBufferedLine = bufferedReader.readLine();
        currentLineNumber++;
    }
}
