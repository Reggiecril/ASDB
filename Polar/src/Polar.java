import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.regex.*;

import org.jfree.chart.ChartFactory;  
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;  
import org.jfree.chart.JFreeChart;  
import org.jfree.chart.axis.CategoryAxis;  
import org.jfree.chart.axis.ValueAxis;  
import org.jfree.chart.plot.CategoryPlot;  
import org.jfree.chart.plot.PlotOrientation;  
import org.jfree.chart.title.TextTitle;  
import org.jfree.data.category.CategoryDataset;  
import org.jfree.data.category.DefaultCategoryDataset;  

public class Polar extends JFrame implements ActionListener {
	JFrame frame;
	JMenuBar menuBar;
	JMenu fileMenu, fileSpace, fileHelp;
	JMenuItem newMenuLoad,newMenuSave,newMenuExit,newMenuAbout;
	JButton button;
	FileDialog fd;
	File file;
	JComboBox<String> cb;
	JLabel body;
	JTable table=new JTable();
	JTable dataTable=new JTable();
	JTable summaryTable=new JTable();
    private static String REGEX = "\\[(.*?)\\]";
	Data data=new Data();
	private boolean speed;

	ChartPanel chartPanel;
	Polar(){
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
		
		
		///this is a panel in the north
		//create table panel
		JPanel tablePanel = new JPanel();
		tablePanel.setPreferredSize(new Dimension(1200,200));
		
		//this is a panel which have some components and a table shows header of data.
		//create a header JPanel in tablePanel
		JPanel headerPanel = new JPanel();
		headerPanel.setPreferredSize(new Dimension(1200,60));
		headerPanel.setBackground(Color.getHSBColor(0.0f, 0.0f, 93.33f));
		tablePanel.add(headerPanel);
		
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
				resetBodyTable();
				resetSummaryTable();
				Polar polar=new Polar();
				
				if(s.equals("KM/H")) {
					polar.setSpeed(true);
					data.tableData(polar.isSpeed());
					data.summaryDate(polar.isSpeed());
				}else {
					polar.setSpeed(false);
					data.tableData(polar.isSpeed());
					data.summaryDate(polar.isSpeed());
				}
				summaryTable.setModel(data.summaryModel);
				dataTable.setModel(data.dataModel);
			}
		});
		headerPanel.add(cb);
		//create summary Panel in the table panel
		//create summary Panel
		JPanel summaryPane=new JPanel();
		summaryPane.setPreferredSize(new Dimension(1200,150));
		tablePanel.add(summaryPane);
		//create a JLabel in summaryPanel
		JLabel summary=new JLabel("Summary",SwingConstants.CENTER);
		summary.setFont (summary.getFont ().deriveFont (28.0f));
		summary.setMaximumSize(new Dimension(1200,50));
		summary.setPreferredSize(new Dimension(1200,50));
		summary.setMinimumSize(new Dimension(1200,50));
		summaryPane.add(summary,BorderLayout.NORTH);
		//create summary table
		summaryTable.setRowHeight(30);
		summaryTable.setBackground(Color.YELLOW);
		summaryTable.setPreferredScrollableViewportSize(summaryTable.getPreferredSize());
		JScrollPane scrollPane3=new JScrollPane(summaryTable);
		scrollPane3.setPreferredSize(new Dimension(1200,80));
		summaryPane.add(scrollPane3,BorderLayout.SOUTH);
		//date table
		table.setRowHeight(30);
		table.setBackground(Color.YELLOW);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		JScrollPane scrollPane=new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(250,50));
		headerPanel.add(scrollPane,BorderLayout.WEST);
		
		///this is a panel in south
		//create bodyPanel
		JPanel bodyPanel=new JPanel();
		bodyPanel.setBackground(Color.getHSBColor(0.0f, 0.0f, 93.33f));
		
		//create dataTable in body Panel
		dataTable.setRowHeight(30);
		dataTable.setPreferredScrollableViewportSize(dataTable.getPreferredSize());
		dataTable.setBackground(Color.GRAY);
		JScrollPane scrollPane1=new JScrollPane(dataTable);
		scrollPane1.setPreferredSize(new Dimension(1200,480));

		//create a tab Panel to display data and chart
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setPreferredSize(new Dimension(1200,490));
		tabbedPane.addTab("Data", scrollPane1);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	
		tabbedPane.addTab("Chart", chartPanel);
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
		bodyPanel.add(tabbedPane);
        
		//Display frame in the center of window
		contain.add(tablePanel,BorderLayout.NORTH);
		contain.add(bodyPanel,BorderLayout.SOUTH);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setJMenuBar(menuBar);
		frame.setSize(1200,800);
		frame.setVisible(true);	
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
			resetBodyTable();
			resetSummaryTable();
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
                summaryTable.setModel(data.summaryModel);
                table.setModel(data.model);
                chartPanel=new ChartPanel(data.chart(isSpeed()));
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
	void resetBodyTable() {
		 data.dataModel.setRowCount(0);
	}
	void resetSummaryTable() {
		 data.summaryModel.setRowCount(0);
	}
	void resetTable() {
		 data.model.setRowCount(0);
	}
	public boolean isSpeed() {
		return speed;
	}
	public void setSpeed(boolean speed) {
		this.speed = speed;
	}
	public static void main(String [] args) {
		Polar polar=new Polar();
		polar.GUI();
	}
}

