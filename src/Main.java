import filemanager.FileManager;
import filemanager.ImpFileManager;
import lexical.LexicalAnalyzer;
import lexical.LexicalException;
import syntax.SyntaxAnalyzer;
import syntax.SyntaxException;

public class Main {
    static FileManager manager = new ImpFileManager();
    static String successfulExecutionMsg = "[SinErrores]";
    public static void notifyLexicalError(LexicalException e) {
        String arrowSpace = "";

        System.out.println("Error lexico en linea "+e.getLineNumber()+" columna "+e.getColumnNumber()+" : "+e.getInvalidLexeme()+" "+e.getDescription());
        System.out.println("Detalle: "+manager.getLine(e.getLineNumber()));

        for (int i = 0 ; i < e.getColumnNumber()+8; i++) {
            arrowSpace = arrowSpace+" ";
        }

        System.out.println(arrowSpace + "^");
        System.out.println(e.getMessage());
    }
    public static void notifySyntacticalError(SyntaxException e) {
        System.out.println(e.getDescription()+"\n");
        System.out.println(e.getMessage());
    }

    public static void main(String[] args) {
        try {
            String filePath = args[0];
            manager = new ImpFileManager();
            manager.openFile(filePath);
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(manager);
            SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);
            syntaxAnalyzer.startAnalysis();
            System.out.println("\n" + successfulExecutionMsg);
            manager.closeCurrentFile();
        } catch (LexicalException e) {
            notifyLexicalError(e);
        } catch (SyntaxException e) {
            notifySyntacticalError(e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}