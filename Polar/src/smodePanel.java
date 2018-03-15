import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class smodePanel extends JPanel{
	Polar polar=new Polar();
	smodePanel(){
		//create a panel display SMode
		JPanel smodePanel=new JPanel();
		smodePanel.setPreferredSize(new Dimension(1200,30));
		smodePanel.setBackground(Color.WHITE);;
		
		smodePanel.add(new JLabel("Speed:"));
		smodePanel.add(new JLabel(polar.data.getSMODE().get("Speed")));
		smodePanel.add(new JLabel(" "));
		smodePanel.add(new JLabel("Cadence:"));
		smodePanel.add(new JLabel(polar.data.getSMODE().get("Cadence")));
		smodePanel.add(new JLabel(" "));
		System.out.println(polar.data.getParams());
		smodePanel.add(new JLabel("Altitude:"));
		smodePanel.add(new JLabel(polar.data.getSMODE().get("Altitude")));
		smodePanel.add(new JLabel(" "));
		
		smodePanel.add(new JLabel("Power:"));
		smodePanel.add(new JLabel(polar.data.getSMODE().get("Power")));
		smodePanel.add(new JLabel(" "));
		
		smodePanel.add(new JLabel("Power Left Right Balance:"));
		smodePanel.add(new JLabel(polar.data.getSMODE().get("Power Left Right Balance")));
		smodePanel.add(new JLabel(" "));
		
		smodePanel.add(new JLabel("Power Pedalling Index:"));
		smodePanel.add(new JLabel(polar.data.getSMODE().get("Power Pedalling Index")));
		smodePanel.add(new JLabel(" "));
		
		smodePanel.add(new JLabel("HR/CC data:"));
		smodePanel.add(new JLabel(polar.data.getSMODE().get("HR/CC data")));
		smodePanel.add(new JLabel(" "));
		
		smodePanel.add(new JLabel("US/Euro unit:"));
		smodePanel.add(new JLabel(polar.data.getSMODE().get("US/Euro unit")));
	}
	smodePanel(Data data){
		//create a panel display SMode
				JPanel smodePanel=new JPanel();
				smodePanel.setPreferredSize(new Dimension(1200,30));
				smodePanel.setBackground(Color.WHITE);;
				
				smodePanel.add(new JLabel("Speed:"));
				smodePanel.add(new JLabel(data.getSMODE().get("Speed")));
				smodePanel.add(new JLabel(" "));
				smodePanel.add(new JLabel("Cadence:"));
				smodePanel.add(new JLabel(data.getSMODE().get("Cadence")));
				smodePanel.add(new JLabel(" "));
				System.out.println(data.getParams());
				smodePanel.add(new JLabel("Altitude:"));
				smodePanel.add(new JLabel(data.getSMODE().get("Altitude")));
				smodePanel.add(new JLabel(" "));
				
				smodePanel.add(new JLabel("Power:"));
				smodePanel.add(new JLabel(data.getSMODE().get("Power")));
				smodePanel.add(new JLabel(" "));
				
				smodePanel.add(new JLabel("Power Left Right Balance:"));
				smodePanel.add(new JLabel(data.getSMODE().get("Power Left Right Balance")));
				smodePanel.add(new JLabel(" "));
				
				smodePanel.add(new JLabel("Power Pedalling Index:"));
				smodePanel.add(new JLabel(data.getSMODE().get("Power Pedalling Index")));
				smodePanel.add(new JLabel(" "));
				
				smodePanel.add(new JLabel("HR/CC data:"));
				smodePanel.add(new JLabel(data.getSMODE().get("HR/CC data")));
				smodePanel.add(new JLabel(" "));
				
				smodePanel.add(new JLabel("US/Euro unit:"));
				smodePanel.add(new JLabel(data.getSMODE().get("US/Euro unit")));
	}

}
