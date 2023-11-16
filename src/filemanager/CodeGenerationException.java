package filemanager;

public class CodeGenerationException extends Exception {
    public CodeGenerationException(String msg) {
        super("Error al general codigo : "+msg);
    }
}