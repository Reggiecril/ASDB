import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.regex.*;

public class Polar extends JFrame implements ActionListener {
	JFrame frame;
	JMenuBar menuBar;
	JMenu fileMenu, fileSpace, fileHelp;
	JMenuItem newMenuLoad,newMenuSave,newMenuExit,newMenuAbout;
	JButton button;
	JTable table=new JTable();
	FileDialog fd;
	File file;
	TextArea textarea;
	DefaultTableModel model =new DefaultTableModel();
    HashMap<Integer,String> allMap = new HashMap<Integer,String>();
    HashMap<String,Integer> headerMap=new HashMap<String,Integer>();
    private static String REGEX = "\\[(.*?)\\]";
    
	Polar(){
		GUI();
	}
	public void GUI() {
		frame=new JFrame();
		Container contain = frame.getContentPane();
		//create menu bar
		menuBar = new JMenuBar();	
		
		//File Menu in the menu bar
		fileMenu = new JMenu("File");
		fileMenu.setFont(new Font("sans-serif", Font.PLAIN, 20));
		menuBar.add(fileMenu);

		fileSpace = new JMenu("   ");
		menuBar.add(fileSpace);
		
		fileHelp = new JMenu("Help");
		fileHelp.setFont(new Font("sans-serif", Font.PLAIN, 20));
		menuBar.add(fileHelp);

		//files inside the menu bar(File ->...) 
		newMenuLoad = new JMenuItem("Load",new ImageIcon("Icon/load.png"));
		newMenuLoad.setFont(new Font("sans-serif", Font.PLAIN, 20));
		fileMenu.add(newMenuLoad);
		newMenuLoad.addActionListener(this);
		
		newMenuSave = new JMenuItem("Save",new ImageIcon("Icon/Save.png"));
		newMenuSave.setFont(new Font("sans-serif", Font.PLAIN, 20));
		fileMenu.add(newMenuSave);
		newMenuSave.addActionListener(this);
		
		newMenuExit = new JMenuItem("Exit",new ImageIcon("Icon/Exit.png"));
		newMenuExit.setFont(new Font("sans-serif", Font.PLAIN, 20));
		fileMenu.add(newMenuExit);
		newMenuExit.addActionListener(this);
		//files inside the menu bar(About ->...) 
		newMenuAbout = new JMenuItem("About",new ImageIcon("Icon/Help.png"));
		newMenuAbout.setFont(new Font("sans-serif", Font.PLAIN, 20));
		fileHelp.add(newMenuAbout);
		newMenuAbout.addActionListener(this);
		//TextArea in Frame
		JPanel textPanel=new JPanel();
		textarea=new TextArea("",35,150,TextArea.SCROLLBARS_VERTICAL_ONLY);
		textarea.setEditable(false);
		textPanel.add(textarea);
		
		//set table in panel and design
		
		//table data
		String[] columns= {"Date","Start Time","Interval"};
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		table.setRowHeight(30);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		JScrollPane scrollPane=new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300,50));
		JPanel tablePanel = new JPanel();
		tablePanel.setPreferredSize(new Dimension(300,200));
		tablePanel.add(scrollPane);
		//Display frame in the center of window
		contain.add(textPanel, BorderLayout.SOUTH);
		contain.add(tablePanel,BorderLayout.WEST);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setJMenuBar(menuBar);
		frame.setSize(1200,800);
		frame.setVisible(true);	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	public void actionPerformed (ActionEvent e){
		JMenuItem source = (JMenuItem) (e.getSource());
		// When click "Save"
		if (source.getText().equals("Save")){
			fd = new FileDialog(frame,"Save",FileDialog.SAVE);
		    fd.setVisible(true); 
					
		}

		// When click "Load"
		else if (source.getText().equals("Load")){
			fd = new FileDialog(frame,"Open",FileDialog.LOAD);
            fd.setVisible(true);   //create and display FileDialog.
            try {   
            	if ((fd.getDirectory()!=null) && (fd.getFile()!=null)){
            	//get the path and file name.
            	file = new File(fd.getDirectory(),fd.getFile());
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                String aline;
                int i=1;
                //load file data to TextArea
                while ((aline=br.readLine()) != null){
                	//collect date to HashMap
                	allMap.put(i,aline);
                	//collect header information
                	Pattern p=Pattern.compile(REGEX);
                	Matcher m=p.matcher(aline);
                	if(m.find()) {
                		headerMap.put(m.group(1),i);
                	}
                	textarea.append(aline+"   LLines:"+i+"\n");
                	i++;
                }
                //get header data.
                String []data=getHeaderData("IntTimes");
                System.out.println((int)headerMap.get("IntTimes"));
                fr.close();
                br.close();
                Object []row=new Object[3];
                row[0]=getParams().get("Date");
                row[1]=getParams().get("StartTime");
                row[2]=getParams().get("Interval");
                model.addRow(row);
            	}
                
              }
            catch (IOException ioe){
                
                System.out.println(ioe);
              }	
	
			
		}else if(source.getText().equals("Exit")) {
			System.exit(-1);
		}else if(source.getText().equals("About")) {
			JOptionPane.showMessageDialog(newMenuAbout, "Welcome to Polar!"+"\n"+"                        :)");
		}
	}
	//get a data information array from Params
	public HashMap<String,String> getParams(){
		String []header=getHeaderData("Params");
		HashMap<String,String> map = new HashMap<String,String>();
		for(String line:header) {
			String lineData[]=line.split("=");
			map.put(lineData[0],lineData[1]);
		}
		
		return map;
	}
	//get lines of a header
	public String[] getHeaderData(String header) {
		try {
			//Initialize a string array
			int count=count(header);
			String [] date =new String[count];
			//get the header line number
			int j=(int)headerMap.get(header);
			int i=0;
			int c=0;
			//add line content to string array.
			for(i=j;i<1500000;i++) {
				String line=(String)allMap.get(i+1);
				//if line is null ,end
				if(line!=null) {
					//if line is empty, turn to next.
					if(line.trim().isEmpty()) continue;
					//if line is header, end
					Pattern p=Pattern.compile(REGEX);
		        	Matcher m=p.matcher(line);
		        	if(m.find()) {
		        		break;
		        	}
		        	date[c]=line;
		        	c++;
				}else {
					break;
				}
			}
			return date;
		}catch(NullPointerException e) {
			throw e;
		}
		
	}
	//count the number of lines from the part of header.
	public int count(String header) {
		//get the line number of header
		int j=(int)headerMap.get(header);
		int i=0;
		//start loop to get lines from header.
		for(i=j;i<1500000;i++) {
			String line=(String)allMap.get(i+1);
			//if the line is null, end it.
			if(line!=null) {
				//if a line is empty,turn to next loop
				if(line.trim().isEmpty()) continue;
				//if turn to next header,break the loop
				Pattern p=Pattern.compile(REGEX);
	        	Matcher m=p.matcher(line);
	        	if(m.find()) {
	        		i--;
	        		break;
	        	}
			}else {
				break;
			}
		}
		return i-j;
	}
	public static void main(String [] args) {
		Polar polar=new Polar();
	}
}
