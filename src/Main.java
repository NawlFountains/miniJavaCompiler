import filemanager.FileManager;
import filemanager.ImpFileManager;

public class Main {
    public void notifyError() {

    }
    public static void main(String[] args) {
//        String filePath = args[1];
        FileManager manager = new ImpFileManager();
        try {
//            manager.openFile(filePath);
            manager.openFile("G:\\Other computers\\PC Casa\\UNS\\Cursando\\Compiladores\\Practica\\Proyecto\\miniJavaCompiler\\resources\\sinErrores\\lexSinErrores01.java");
            for (int i = 0; i < 16 ; i++) {

                char c = manager.getNextCharacter();
                System.out.println(c+" at line "+manager.getCurrentLineNumber()+" column "+manager.getCurrentColumnNumber());
            }
            manager.closeCurrentFile();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}