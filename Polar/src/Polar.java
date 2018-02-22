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
	FileDialog fd;
	File file;
	JComboBox<String> cb;
	JTable table=new JTable();
	JTable dataTable=new JTable();
    private static String REGEX = "\\[(.*?)\\]";
	Data data=new Data();
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
		//create table panel
		JPanel tablePanel = new JPanel();
		tablePanel.setPreferredSize(new Dimension(1200,400));
		//Create a ComboBox to display two type data by MPH and KM/H
		String []speedItem=new String[] {"MPH","KM/H"};
		cb=new JComboBox<String>(speedItem);
		cb.setSelectedIndex(1);
		cb.setPreferredSize(new Dimension(150,50));
		cb.addActionListener(new ActionListener() {
			/*
			 * when combobox select then display data by the text of selected.
			 * (non-Javadoc)
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			public void actionPerformed(ActionEvent event) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb=(JComboBox<String>)(event.getSource());
				String s=(String)cb.getSelectedItem();
				//empty the table
				resetTable();
				
				if(s.equals("KM/H")) {
					data.tableData(true);
				}else {
					data.tableData(false);
				}
				dataTable.setModel(data.dataModel);
			}
		});
		tablePanel.add(cb);
		//date table
		String[] columns= {"Date","Start Time","Interval"};
		data.model.setColumnIdentifiers(columns);
		table.setRowHeight(30);
		table.setModel(data.model);
		table.setBackground(Color.YELLOW);
		table.setForeground(Color.blue);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		JScrollPane scrollPane=new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(250,50));
		tablePanel.add(scrollPane,BorderLayout.WEST);
		//body data table
		dataTable.setRowHeight(30);
		dataTable.setPreferredScrollableViewportSize(dataTable.getPreferredSize());
		JScrollPane scrollPane1=new JScrollPane(dataTable);
		scrollPane1.setPreferredSize(new Dimension(1200,200));
		tablePanel.add(scrollPane1);
		
		//Display frame in the center of window
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
			resetTable();
			data.model.setRowCount(0);
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
                	data.allMap.put(i,aline);
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
                data.tableData();
                
                dataTable.setModel(data.dataModel);
            	}
                
              }
            catch (IOException ioe){
                
                System.out.println(ioe);
              }	
	
			
		}else if(source.getText().equals("Exit")) {
			//exit application
			System.exit(-1);
		}else if(source.getText().equals("About")) {
			//show a messagebox.
			JOptionPane.showMessageDialog(newMenuAbout, "Welcome to Polar!"+"\n"+"                        :)");
		}
	}
	void resetTable() {
		 data.dataModel.setRowCount(0);
	}
	public static void main(String [] args) {
		Polar polar=new Polar();
	}
}
