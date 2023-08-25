package filemanager;

import java.io.*;
import java.nio.Buffer;

public class ImpFileManager implements FileManager{
    int currentLineNumber;
    int currentColumnNumber;
    String currentBufferedLine = " ";

    FileReader fileReader;
    BufferedReader bufferedReader;

    public ImpFileManager() {
    }
    public char getNextCharacter() {
        char nextChar;
            if (currentColumnNumber+1 == currentBufferedLine.length()) {
                currentColumnNumber = 0;
                readNewLine();
            } else {
                currentColumnNumber++;
            }
            if (currentBufferedLine == null) {
                nextChar = ' ';
            } else {
                //TODO fix this patch
                nextChar = currentBufferedLine.charAt(currentColumnNumber);
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

    public String getCurrentLine() { return currentBufferedLine; }
    public boolean isEOF() {
        boolean toReturn = false;
        if (currentBufferedLine == null)
            toReturn = true;
        return toReturn;
    }
    private void readNewLine() {
        try {
            currentBufferedLine = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        currentLineNumber++;
    }
}
