package testsmelldetectorutils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class ProjectChooser {
	
	private String path = "";
	
	public ProjectChooser() {
		JFileChooser chooser = new JFileChooser();
	    chooser.setDialogTitle("choosertitle");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    
	    chooser.setAcceptAllFileFilterUsed(false);

	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	    	this.path = chooser.getSelectedFile().getAbsolutePath();
	    	//System.out.println(chooser.getSelectedFile().getAbsolutePath());
	    } else {
	      System.out.println("No Selection ");
	      System.exit(0);
	    }
	    
	    //File[] files = new File(path).listFiles();
	    //showFiles(files);
	    
	    
		
	}
	
	private void showFiles(File[] files) {
	    for (File file : files) {
	        if (file.isDirectory()) {
	            System.out.println("Directory: " + file.getName());
	            showFiles(file.listFiles()); // Calls same method again.
	        } else {
	        	if(file.getName().contains(".java")) {
	        		System.out.println("File: " + file.getName());
	        		System.out.println(file.getAbsolutePath());
	        		System.out.println();
	        	}
	            
	        }
	    }
	}
	
	public File[] getFiles() {
		File[] files = new File(this.path).listFiles();
		return files;
	}

}
