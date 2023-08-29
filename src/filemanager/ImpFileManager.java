package filemanager;

import java.io.*;

public class ImpFileManager implements FileManager{
    int currentLineNumber;
    int currentColumnNumber;
    String currentBufferedLine = " ";
    boolean jumpedLine = false;
    boolean endOfFile = false;
    String path;
    FileReader fileReader;
    BufferedReader bufferedReader;

    public ImpFileManager() {
    }
    public char getNextCharacter() {
        char currentChar = ' ';
        if (jumpedLine) {
            currentLineNumber++;
            currentColumnNumber = 1;
            jumpedLine = false;
            currentBufferedLine = "";
        }
        try {
            currentChar = (char) bufferedReader.read();
            currentBufferedLine = currentBufferedLine + currentChar;
            if (currentChar == (char) -1)
                endOfFile = true;
            if (currentChar == '\n'){
                jumpedLine = true;
            } else {
                currentColumnNumber++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currentChar;

    }
    public void openFile(String path) throws FileNotFoundException {
        try {
            this.path = path;
            fileReader = new FileReader(path);
            bufferedReader = new BufferedReader(fileReader);

            currentLineNumber = 1;
            currentColumnNumber = 1;
        } catch (Exception e) {
            throw new FileNotFoundException("File "+path+" couldn't be found");
        }
    }

    public void closeCurrentFile() throws FileNotFoundException {
        try {
            fileReader.close();
            bufferedReader.close();
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

    public String getLine(int lineNumber) {
        String lineToReturn = "";
        try {
            FileReader auxiliaryFileReader = new FileReader(path);
            BufferedReader auxiliaryBuffer = new BufferedReader(auxiliaryFileReader);
            for (int i=1; i < lineNumber; i++) {
                auxiliaryBuffer.readLine();
            }
            lineToReturn = auxiliaryBuffer.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lineToReturn;
    }
    public boolean isEOF() {
        return endOfFile;
    }

}
