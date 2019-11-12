package testsmelldetectorutils;

import java.io.File;
import java.util.ArrayList;

public class FileSeparator {
	
	private ArrayList<File> testFileList = new ArrayList<File>();
	private ArrayList<File> prodFileList = new ArrayList<File>();
	
	public FileSeparator() {
		
	}
	
	public ArrayList<File> getTestFilesList(File[] files) {
		
		separateTestFile(files);
		return testFileList;
		
	}
	
	
	
	public ArrayList<File> getProductionFilesList(File[] files) {
		
		separateProdFile(files);
		return prodFileList;
		

	}
	
	public void separateTestFile(File[] files) {
		for (File file : files) {
	        if (file.isDirectory()) {
	        	separateTestFile(file.listFiles()); 
	        } else {
	        	if(file.getName().contains(".java") && (file.getName().contains("test") || file.getName().contains("Test"))) {
	        		
	        		testFileList.add(file);
	        	}
	            
	        }
	    }
	}
	
	public void separateProdFile(File[] files) {
		for (File file : files) {
	        if (file.isDirectory()) {
	            //System.out.println("Directory: " + file.getName());
	            separateProdFile(file.listFiles()); 
	        } else {
	        	if(file.getName().contains(".java") && !(file.getName().contains("test") || file.getName().contains("Test"))) {
	        		//System.out.println(file.getName());
	        		prodFileList.add(file);
	        	}
	            
	        }
	    }
	}

}
