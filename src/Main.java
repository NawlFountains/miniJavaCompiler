import filemanager.FileManager;
import filemanager.ImpFileManager;
import lexical.LexicalAnalyzer;

public class Main {
    public void notifyError() {

    }
    public static void main(String[] args) {
//        String filePath = args[1];
        FileManager manager = new ImpFileManager();
        try {
//            manager.openFile(filePath);
            manager.openFile("G:\\Other computers\\PC Casa\\UNS\\Cursando\\Compiladores\\Practica\\Proyecto\\miniJavaCompiler\\resources\\sinErrores\\lexSinErrores01.java");
//            LexicalAnalyzer analyzer = new LexicalAnalyzer(manager);
            while (!manager.isEOF()) {
                System.out.println(manager.getNextCharacter());
//                System.out.println(analyzer.nextToken().toString());
            }
            manager.closeCurrentFile();
        } catch (Exception e) {
            System.out.println("Exception found :"+e.getMessage());
        }

    }

}