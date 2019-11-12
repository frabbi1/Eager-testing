package testsmell;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.apache.commons.lang3.StringUtils;
import testsmell.smell.*;
import testsmelldetectorutils.OutputList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestSmellDetector {

    private List<AbstractSmell> testSmells;

 
    public TestSmellDetector() {
        initializeSmells();
        
    }

    private void initializeSmells(){
        testSmells = new ArrayList<>();

        testSmells.add(new EagerTest());
     

    }


    public static TestSmellDetector createTestSmellDetector() {
        return new TestSmellDetector();
    }


    public List<String> getTestSmellNames() {
        return testSmells.stream().map(AbstractSmell::getSmellName).collect(Collectors.toList());
    }


    public TestFile detectSmells(TestFile testFile, ArrayList<OutputList> outs, FileWriter fw) throws IOException {
        CompilationUnit testFileCompilationUnit=null, productionFileCompilationUnit=null;
        FileInputStream testFileInputStream, productionFileInputStream;
        ArrayList<File> productionFiles;

        if(!StringUtils.isEmpty(testFile.getTestFilePath())) {
            testFileInputStream = new FileInputStream(testFile.getTestFilePath());
            testFileCompilationUnit = JavaParser.parse(testFileInputStream);
        }


        
        productionFiles = testFile.getProductionFiles();

        initializeSmells();
        for (AbstractSmell smell : testSmells) {
            try {
            	smell.setOutputList(outs, testFile.getTestFileName(), testFile.getTestFilePath(), fw);
                smell.runAnalysis(testFileCompilationUnit, productionFiles);
            } catch (FileNotFoundException e) {
                testFile.addSmell(null);
                continue;
            }
            testFile.addSmell(smell);
        }

        return testFile;

    }


}
