package filemanager;

import semantic.entities.ClassST;
import semantic.entities.ConstructorST;
import semantic.entities.MethodST;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeGenerator {
    protected static CodeGenerator instance = null;
    protected static final String FORMAT = ".out";
    protected static final String labelPrefix = "lbl";
    protected static final String labelPrefixName = "@";
    protected static final String labelPrefixMethod = labelPrefix+"Met";
    protected static final String labelPrefixConstructor = labelPrefix+"Constructor";
    protected static final String labelPrefixVT = labelPrefix+"VT";
    protected static final String heapLabel = "simple_heap_init";
    protected static final String mallocLabel = "simple_malloc";
    protected File outputFile;
    protected FileWriter fileWriter;

    public static CodeGenerator getInstance() {
        if (instance == null)
            instance = new CodeGenerator();
        return instance;
    }
    public void setFileName(String fileName) throws CodeGenerationException {
        outputFile = new File(fileName+FORMAT);
        try {
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            } else {
                outputFile.delete();
                outputFile.createNewFile();
            }
        } catch (IOException e) {
            throw new CodeGenerationException("No se pudo crear el archivo de salida");
        }
        try {
            fileWriter = new FileWriter(outputFile);
        } catch (IOException e) {
            throw new CodeGenerationException("No se pudo generar un escritor para el archivo "+e.getMessage());
        }
    }

    public void addLine(String lineToAdd) throws CodeGenerationException {
        String lineWithJump= lineToAdd + '\n';
        try {
            fileWriter.write(lineWithJump);
        } catch (IOException e) {
            throw new CodeGenerationException("No se pudo escribir en archivo para agregar la nueva linea "+e.getMessage());
        }
    }
    public static String generateLabelForMethod(MethodST method) {
        return labelPrefixMethod+method.getName()+labelPrefixName+method.getOwnerClass().getClassName();
    }

    public static String generateLabelForConstructor(ConstructorST constructor) {
        return labelPrefixConstructor+labelPrefixName+constructor.getOwnerClass().getClassName();
    }
    public static String generateLabelForVT(ClassST classST) {
        return labelPrefixVT+classST.getClassName();
    }
    public static String generateHeapRoutines() {
        return "simple_heap_init: " +
                "RET 0\t; Retorna inmediatamente\n" +
                "\n" +
                "simple_malloc: " +
                "LOADFP\t; Inicialización unidad\t\n" +
                "LOADSP\n" +
                "STOREFP ; Finaliza inicialización del RA\n" +
                "LOADHL\t; hl\n" +
                "DUP\t; hl\n" +
                "PUSH 1\t; 1\n" +
                "ADD\t; hl+1\n" +
                "STORE 4 ; Guarda el resultado (un puntero a la primer celda de la región de memoria)\n" +
                " LOAD 3\t; Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)\n" +
                "ADD\n" +
                "STOREHL ; Mueve el heap limit (hl). Expande el heap\n" +
                "STOREFP\n" +
                "RET 1\t; Retorna eliminando el parámetro\n";
    }
    public static String getLabelForHeap() {
        return heapLabel;
    }
    public static String getLabelForMalloc() {
        return mallocLabel;
    }
    public void closeFile() throws CodeGenerationException {
        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new CodeGenerationException("No se pudo cerrar el archivo escrito "+e.getMessage());
        }
    }
}
