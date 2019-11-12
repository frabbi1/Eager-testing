import testsmell.AbstractSmell;
import testsmell.TestFile;
import testsmell.TestSmellDetector;
import testsmelldetectorutils.FileSeparator;
import testsmelldetectorutils.Output;
import testsmelldetectorutils.OutputList;
import testsmelldetectorutils.ProjectChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) throws IOException {

    	
    	ProjectChooser pc = new ProjectChooser();
    	File[] files = pc.getFiles();
    	
    	System.out.println("processing...");
    	
    	FileSeparator fs = new FileSeparator();
    	ArrayList<File> testFiles = fs.getTestFilesList(files);
    	ArrayList<File> prodFiles = fs.getProductionFilesList(files);
    	
    	
    	
        TestSmellDetector testSmellDetector = TestSmellDetector.createTestSmellDetector();

        

        TestFile testFile;
        List<TestFile> testFileObjects = new ArrayList<>();
        
        for(File file : testFiles) {
    		testFile = new TestFile(file, prodFiles);
    		testFileObjects.add(testFile);
    	}

        TestFile tempFile;
        Boolean found = false;
        ArrayList<OutputList> outs = new ArrayList<OutputList>();
        
        FileWriter fw = new FileWriter(new File("eager-output.csv"));
        fw.write("\"Smell\",\"File Name\",\"Method Name\",\"Line\",\"Path\"\n");

        for (TestFile file : testFileObjects) {
        	

            tempFile = testSmellDetector.detectSmells(file,outs,fw);
            for (AbstractSmell smell : tempFile.getTestSmells()) {
            	if(smell.getHasSmell()) {
            		found = true;
            		
            		//showing output file by file
//            		outs = smell.getOutputList();
//            		new Output(outs);
//            		outs.clear();
            		//write output in file
            		
//            		outs.clear();
            		
            		
            	}
            	
              
            }
            
        }
        if(!found) {
        	JFrame frame = new JFrame();
        	JOptionPane.showMessageDialog(frame, "No Eager Test Smell Found");
        	fw.close();
        }
        else {
        	//showing output in a whole
        	new Output(outs);
        	fw.close();
        	
        }
        
        

        
    }


}
