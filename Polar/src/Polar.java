import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.*;

public class Polar implements ActionListener {
	JFrame frame;
	JMenuBar menuBar;
	JMenu fileMenu, fileSpace, fileHelp;
	JMenuItem newMenuLoad,newMenuSave,newMenuExit,newMenuAbout;
	JButton button;
	FileDialog fd;
	File file;
	TextArea textarea,textarea1;
    private static String REGEX = "\\[(.*?)\\]";
	Polar(){
		
	}
	public void GUI() {
		frame=new JFrame();

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
		//Textarea in Frame
		JPanel text=new JPanel();
		textarea=new TextArea(10,100);
		textarea1=new TextArea(30,100);
		text.add(textarea);
		text.add(textarea1);
		frame.add(text);
		
		//Display frame in the center of window
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setJMenuBar(menuBar);
		frame.setSize(800,600);
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
            fd.setVisible(true);   //create and display filedialog.
            try {   
            	if ((fd.getDirectory()!=null) && (fd.getFile()!=null)){
            	//get the path and file name.
            	file = new File(fd.getDirectory(),fd.getFile());
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String aline;
                int i=1;
                //load file data to textarea
                while ((aline=br.readLine()) != null){
                	Pattern p=Pattern.compile(REGEX);
                	Matcher m=p.matcher(aline);
                	if(m.find()) {
                		textarea.append(m.group(1)+"   LLines:"+i+"\n");
                	}
                	textarea1.append(aline+"   Lines"+i+"\n");
                	i++;
                }
                fr.close();
                br.close();}
                
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
	public static void main(String [] args) {
		Polar polar=new Polar();
		polar.GUI();
	}
}
