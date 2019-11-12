package testsmell;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestFile {
    private String app, testFilePath, productionFilePath;
    private List<AbstractSmell> testSmells;
    //--
    private File testFile;
    private ArrayList<File> prodFiles;

    public String getApp() {
        return app;
    }
    
    public ArrayList<File> getProductionFiles(){
    	return this.prodFiles;
    }

    public String getProductionFilePath() {
        return productionFilePath;
    }

    public String getTestFilePath() {
        return testFilePath;
    }

    public List<AbstractSmell> getTestSmells() {
        return testSmells;
    }

    public boolean getHasProductionFile() {
        return ((productionFilePath != null && !productionFilePath.isEmpty()));
    }

    public TestFile(File testFile, ArrayList<File> prodFiles) {
        this.testFile = testFile;
        this.prodFiles = prodFiles;
        this.testFilePath = this.testFile.getAbsolutePath();
        this.app = testFile.getPath();
        this.testSmells = new ArrayList<>();
        
    }

    public void addSmell(AbstractSmell smell) {
        testSmells.add(smell);
    }

   

    public String getTestFileName(){
        int lastIndex = testFilePath.lastIndexOf("\\");
        return testFilePath.substring(lastIndex+1,testFilePath.length());
    }

    public String getTestFileNameWithoutExtension(){
        int lastIndex = getTestFileName().lastIndexOf(".");
        return getTestFileName().substring(0,lastIndex);
    }

}