package filemanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeGenerator {
    protected static CodeGenerator instance = null;
    protected static final String FORMAT = ".out";
    protected File outputFile;
    protected FileWriter fileWriter;
    private CodeGenerator() {
        instance = new CodeGenerator();
    }

    public static CodeGenerator getInstance() {
        if (instance == null)
            instance = new CodeGenerator();
        return instance;
    }
    public void setFileName(String fileName) throws IOException {
        outputFile = new File(fileName+FORMAT);
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        } else {
            outputFile.delete();
            outputFile.createNewFile();
        }
        fileWriter = new FileWriter(outputFile);
    }

    public void addLine(String lineToAdd) throws IOException {
        String lineWithJump= lineToAdd + '\n';
        fileWriter.write(lineWithJump);
    }
}
