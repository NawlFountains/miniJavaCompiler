import filemanager.CodeGenerator;
import filemanager.FileManager;
import filemanager.ImpFileManager;
import lexical.LexicalAnalyzer;
import lexical.LexicalException;
import lexical.SemanticException;
import semantic.SymbolTable;
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
    public static void notifySemanticError(SemanticException e) {
        System.out.println("Error semantico en linea "+e.getLineNumber()+": "+e.getDescription()+"\n");
        System.out.println(e.getMessage());
    }

    public static void main(String[] args) {
        try {
//            String filePath = args[0];
            String filePath = "D:/Users/nahue/UNS/Cursando/Compiladores/Practica/Proyecto/miniJavaCompiler/resources/codeGenerationTests/gen-01.java";
            manager = new ImpFileManager();
            manager.openFile(filePath);
            SymbolTable.getInstance().resetTable();
            LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(manager);
            SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);
            syntaxAnalyzer.startAnalysis();
            SymbolTable.getInstance().checkDeclarations();
            SymbolTable.getInstance().consolidate();
            SymbolTable.getInstance().checkSentences();
            System.out.println("Compilacion Exitosa");
            System.out.println("\n" + successfulExecutionMsg);
            System.out.println("Generacion de codigo");
            CodeGenerator.getInstance().setFileName(filePath.substring(0,filePath.length()-5));
            SymbolTable.getInstance().generateCode();
            CodeGenerator.getInstance().closeFile();
            manager.closeCurrentFile();
        } catch (LexicalException e) {
            notifyLexicalError(e);
        } catch (SyntaxException e) {
            notifySyntacticalError(e);
        } catch (SemanticException e) {
            notifySemanticError(e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
    }
}