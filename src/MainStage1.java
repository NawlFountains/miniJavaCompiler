import filemanager.FileManager;
import filemanager.ImpFileManager;
import lexical.LexicalAnalyzer;
import lexical.LexicalException;

public class MainStage1 {
    static FileManager manager = new ImpFileManager();
    static String successfulExecutionMsg = "[SinErrores]";
    public static void notifyError(LexicalException e) {
        String arrowSpace = "";

        System.out.println("Error lexico en linea "+e.getLineNumber()+" columna "+e.getColumnNumber()+" : "+e.getInvalidLexeme()+" "+e.getDescription());
        System.out.println("Detalle: "+manager.getLine(e.getLineNumber()));

        for (int i = 0 ; i < e.getColumnNumber()+7; i++) {
            arrowSpace = arrowSpace+" ";
        }

        System.out.println(arrowSpace + "^");
        System.out.println(e.getMessage());
    }

    public static void main(String[] args) {
        try {
                String filePath = args[0];
                boolean errorOccur = false;
                manager = new ImpFileManager();
                manager.openFile(filePath);
                LexicalAnalyzer analyzer = new LexicalAnalyzer(manager);
                while (!manager.isEOF()) {
                    try {
                        System.out.println(analyzer.nextToken().toString());
                    } catch (LexicalException e) {
                        notifyError(e);
                        errorOccur = true;
                    }
                }
                if (!errorOccur) {
                    System.out.println("\n"+successfulExecutionMsg);
                } else {
                    errorOccur = false;
                }
                manager.closeCurrentFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}