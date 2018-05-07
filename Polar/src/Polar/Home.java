package Polar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartPanel;

public class Home extends JFrame{
	private static String REGEX = "\\[(.*?)\\]";
	Home(){
	}
	
	public void GUI() {
		JFrame frame=new JFrame();
		//create a panel ,and add some component in it.
		JPanel centerPanel=new JPanel();
		//display a label in panel.
		JLabel timerLabel = new JLabel("Please Load A File!", SwingConstants.CENTER);
		timerLabel.setFont (timerLabel.getFont ().deriveFont (38.0f));
		centerPanel.add(timerLabel);
		//display a button and can load the frame which is Polar
		JButton button=new JButton("Load");
		button.setPreferredSize(new Dimension(150,50));
		//add action to this button can load data and show the Polar frame
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e){
				FileDialog fd = new FileDialog(frame,"Open",FileDialog.LOAD);
	            fd.setVisible(true);   //create and display FileDialog.
	            try {   
	            	if ((fd.getDirectory()!=null) && (fd.getFile()!=null)){
	            	//get the path and file name.
	            	File file = new File(fd.getDirectory(),fd.getFile());
	                FileReader fr = new FileReader(file);
	                BufferedReader br = new BufferedReader(fr);
	                Data data=new Data();
	                Polar polar=new Polar();
	                String aline;
	                int i=1;
	                //load file data to TextArea
	                while ((aline=br.readLine()) != null){
	                	//collect date to HashMap
	                	data.allMap.put(i, aline);
	                	//collect header information
	                	Pattern p=Pattern.compile(REGEX);
	                	Matcher m=p.matcher(aline);
	                	if(m.find()) {
	                		//record the line number of each line.
	                		data.headerMap.put(m.group(1),i);
	                	}
	                	i++;
	                }
	                fr.close();
	                br.close();
	                //add data to table and display then hide this frame.
	                data.tableData();
	                polar.dataTable.setModel(data.dataModel);
	                polar.summaryTable.setModel(data.summaryModel);
	                polar.table.setModel(data.model);
	                polar.setData(data);
	                frame.setVisible(false);
					polar.GUI();
	                
	            	}
	                
	              }
	            catch (IOException ioe){
	                ioe.printStackTrace();
	              }	
		
			}
		});
		centerPanel.add(button,BorderLayout.SOUTH);
		//add the panel to box,make it center
        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(Box.createVerticalGlue());
        box.add(centerPanel);     
        box.add(Box.createVerticalGlue());
		
		//frame 
		frame.add(box);
		frame.pack();
		frame.setSize(1200,800);
		frame.setVisible(true);	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}
	public static void main(String[] args) {
		Home home = new Home();
		home.GUI();
	}

}
