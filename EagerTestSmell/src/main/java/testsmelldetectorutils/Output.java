package testsmelldetectorutils;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame; 
import javax.swing.JScrollPane; 
import javax.swing.JTable; 

public class Output {
	JFrame f; 

	JTable j; 
	
	public Output(ArrayList<OutputList> ol) {
		f = new JFrame(); 
		f.setTitle("Output"); 
		
		int len = ol.size();
		String[][] data = new String[len][5];
		
		for(int i=0; i<len; i++) {
			for(int j=0; j<5; j++) {
				if(j==0) data[i][j] = ol.get(i).getSmell();
				if(j==1) data[i][j] = ol.get(i).getFn();
				if(j==2) data[i][j] = ol.get(i).getMn();
				if(j==3) data[i][j] = ol.get(i).getLine();
				if(j==4) data[i][j] = ol.get(i).getPath();
			}
		}

		

		String[] columnNames = { "Smell", "File Name", "Method Name", "Line No", "Path" }; 


		j = new JTable(data, columnNames); 
		
		j.setBounds(50, 50, 700, 400); 
		j.setFont(new Font("Serif", Font.CENTER_BASELINE, 16));
		j.setRowHeight(30);


		JScrollPane sp = new JScrollPane(j); 
		f.add(sp); 

		f.setSize(1350, 700); 

		f.setVisible(true); 
	}
}
