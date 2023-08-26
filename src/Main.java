import filemanager.FileManager;
import filemanager.ImpFileManager;
import lexical.LexicalAnalyzer;
import lexical.LexicalException;

public class Main {
    static FileManager manager = new ImpFileManager();
    static String successfulExecutionMsg = "[SinErrores]";
    public static void notifyError(LexicalException e) {
        String arrowSpace = "";

        System.out.println("Error lexico en linea "+e.getLineNumber()+" columna "+e.getColumnNumber()+" : "+e.getInvalidLexeme()+" "+e.getDescription());
        System.out.println("Detalle: "+manager.getLine(e.getLineNumber()));

        for (int i = 0 ; i < manager.getCurrentColumnNumber()+7; i++) {
            arrowSpace = arrowSpace+" ";
        }

        System.out.println(arrowSpace + "^");
        System.out.println(e.getMessage());
    }

    public static void main(String[] args) {
        boolean errorOccur = false;
//        String filePath = args[0];
        try {
//            manager.openFile(filePath);
            manager.openFile("resources\\conErrores\\dummyLexError.java");
            LexicalAnalyzer analyzer = new LexicalAnalyzer(manager);
            while (!manager.isEOF()) {
                System.out.println(analyzer.nextToken().toString());
            }
            if (!errorOccur) {
                System.out.println(successfulExecutionMsg);
            }
            manager.closeCurrentFile();
        }
        catch (LexicalException e) {
            notifyError(e);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}