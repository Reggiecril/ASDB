import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.regex.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

public class Polar extends JFrame implements ActionListener, ChartMouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame frame;
	JMenuBar menuBar;
	JMenu fileMenu, fileSpace, fileHelp;
	JMenuItem newMenuLoad, newMenuSave, newMenuExit, newMenuAbout;
	JButton button;
	FileDialog fd;
	File file;
	JComboBox<String> cb;
	JLabel body;
	JTable table = new JTable();
	JTable dataTable = new JTable();
	JTable summaryTable = new JTable();
	JTable chunkTable = new JTable();
	JPanel chunkPane = new JPanel();
	JTextField text = new JTextField(5);
	private static String REGEX = "\\[(.*?)\\]";
	Data data;
	private boolean speed;
	JTabbedPane tabbedPane = new JTabbedPane();
	DecimalFormat oneDecimal = new DecimalFormat("0.0");
	DecimalFormat twoDecimal = new DecimalFormat("0.00");
	private ChartPanel chartPanel;
	double px = 0.0, py = 0.0, prx = 0.0, pry = 0.0, chartpx = 0.0, chartpy = 0.0, chartX = 0.0, chartY = 0.0;
	private Point pointPress, pointRelease;
	int startPoint = 0, endPoint = 0;

	Polar() {
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public void GUI() {
		frame = new JFrame();
		Container contain = frame.getContentPane();
		// create menu bar
		menuBar = new JMenuBar();

		// File Menu in the menu bar
		fileMenu = new JMenu("File");
		fileMenu.setFont(new Font("sans-serif", Font.PLAIN, 20));
		menuBar.add(fileMenu);

		fileSpace = new JMenu("   ");
		menuBar.add(fileSpace);

		fileHelp = new JMenu("Help");
		fileHelp.setFont(new Font("sans-serif", Font.PLAIN, 20));
		menuBar.add(fileHelp);

		// files inside the menu bar(File ->...)
		newMenuLoad = new JMenuItem("Load", new ImageIcon("Icon/load.png"));
		newMenuLoad.setFont(new Font("sans-serif", Font.PLAIN, 20));
		fileMenu.add(newMenuLoad);
		newMenuLoad.addActionListener(this);

		newMenuSave = new JMenuItem("Save", new ImageIcon("Icon/Save.png"));
		newMenuSave.setFont(new Font("sans-serif", Font.PLAIN, 20));
		fileMenu.add(newMenuSave);
		newMenuSave.addActionListener(this);

		newMenuExit = new JMenuItem("Exit", new ImageIcon("Icon/Exit.png"));
		newMenuExit.setFont(new Font("sans-serif", Font.PLAIN, 20));
		fileMenu.add(newMenuExit);
		newMenuExit.addActionListener(this);
		// files inside the menu bar(About ->...)
		newMenuAbout = new JMenuItem("About", new ImageIcon("Icon/Help.png"));
		newMenuAbout.setFont(new Font("sans-serif", Font.PLAIN, 20));
		fileHelp.add(newMenuAbout);
		newMenuAbout.addActionListener(this);

		/// this is a panel in the north
		// create table panel
		JPanel tablePanel = new JPanel();
		tablePanel.setPreferredSize(new Dimension(1600, 380));

		// this is a panel which have some components and a table shows header of data.
		// create a header JPanel in tablePanel
		JPanel headerPanel = new JPanel();
		headerPanel.setPreferredSize(new Dimension(1600, 60));
		headerPanel.setBackground(Color.getHSBColor(0.0f, 0.0f, 93.33f));
		tablePanel.add(headerPanel);

		headerPanel.add(new JLabel("Header Table:"));
		headerPanel.add(new JLabel(" "));
		// date table
		table.setRowHeight(30);
		table.setBackground(Color.YELLOW);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(250, 50));
		headerPanel.add(scrollPane, BorderLayout.WEST);

		headerPanel.add(new JLabel("   "));
		headerPanel.add(new JLabel("Unit Selection:"));
		headerPanel.add(new JLabel(" "));
		// Create a ComboBox to display two type data by MPH and KM/H
		String[] speedItem = new String[] { "MILES", "KILOMETERS" };
		cb = new JComboBox<String>(speedItem);
		cb.setSelectedIndex(1);
		cb.setPreferredSize(new Dimension(150, 50));
		cb.addActionListener(new ActionListener() {
			/*
			 * when combobox select then display data by the text of selected. (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent event) {
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>) (event.getSource());
				String s = (String) cb.getSelectedItem();
				// empty the table
				resetBodyTable();
				resetSummaryTable();
				Polar polar = new Polar();
				// identify speed is KM/H or MPH
				if (s.equals("KILOMETERS")) {
					polar.setSpeed(true);
				} else {
					polar.setSpeed(false);
				}
				data.tableData(polar.isSpeed());
				data.summaryDate(polar.isSpeed());
				summaryTable.setModel(data.summaryModel);
				dataTable.setModel(data.dataModel);
			}
		});
		headerPanel.add(cb);

		headerPanel.add(new JLabel("   "));
		headerPanel.add(new JLabel("Chunk:"));
		headerPanel.add(new JLabel(" "));
		// create text
		
		text.setPreferredSize(new Dimension(100, 30));
		headerPanel.add(text);
		// create button
		JButton button = new JButton("Chunk");
		headerPanel.add(button);
		button.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	resetChunkTable();
		       if(startPoint<0)
		    	   startPoint=0;
		       else if(endPoint>data.getTime())
		    	   endPoint=data.getTime();
		       int index=Integer.valueOf(text.getText().toString());
		       int[]point=getChunkData(startPoint, endPoint,index);
		       for(int i=0;i<point.length;i++) {
		    	   if(i==0) {
		    		   data.chunkData(startPoint,point[i]);
		    	   }
		    	   else {
		    		   data.chunkData(point[i-1],point[i]);
		    	   }
		    	   System.out.println(point[i]);
		       }
		       chunkTable.setModel(data.chunkModel);
				
				
		       chunkPane.setVisible(true);
		    }});
		
		
		
		// create summary Panel in the table panel
		// create summary Panel
		JPanel summaryPane = new JPanel();
		summaryPane.setPreferredSize(new Dimension(1600,100));
		summaryPane.setBackground(Color.getHSBColor(0.0f, 0.0f, 93.33f));
		tablePanel.add(summaryPane);

		// create a JLabel in summaryPanel
		JLabel summary = new JLabel("Summary", SwingConstants.LEFT);
		summary.setFont(summary.getFont().deriveFont(16.0f));
		summary.setMaximumSize(new Dimension(1600, 18));
		summary.setPreferredSize(new Dimension(1600, 18));
		summary.setMinimumSize(new Dimension(1600, 18));
		summaryPane.add(summary, BorderLayout.NORTH);

		// create summary table
		summaryTable.setRowHeight(30);
		summaryTable.setBackground(Color.YELLOW);
		summaryTable.setPreferredScrollableViewportSize(summaryTable.getPreferredSize());
		JScrollPane scrollPane3 = new JScrollPane(summaryTable);
		scrollPane3.setPreferredSize(new Dimension(1600, 50));
		summaryPane.add(scrollPane3, BorderLayout.SOUTH);
		
		// create chunk Panel in the table panel
		// create chunk Panel
		
		chunkPane.setPreferredSize(new Dimension(1600, 200));
		chunkPane.setBackground(Color.getHSBColor(0.0f, 0.0f, 93.33f));
		chunkPane.setVisible(false);
		tablePanel.add(chunkPane);

		// create chunk date
		JLabel chunk = new JLabel("Chunk", SwingConstants.LEFT);
		chunk.setFont(chunk.getFont().deriveFont(16.0f));
		chunk.setMaximumSize(new Dimension(1600, 18));
		chunk.setPreferredSize(new Dimension(1600, 18));
		chunk.setMinimumSize(new Dimension(1600, 18));
		chunkPane.add(chunk, BorderLayout.NORTH);

		// create chunk table
		chunkTable.setRowHeight(30);
		chunkTable.setBackground(Color.YELLOW);
		chunkTable.setPreferredScrollableViewportSize(chunkTable.getPreferredSize());
		JScrollPane scrollPane4 = new JScrollPane(chunkTable);
		scrollPane4.setPreferredSize(new Dimension(1600, 180));
		chunkPane.add(scrollPane4, BorderLayout.SOUTH);
		
		
		
		
		
		/// this is a panel in south
		// create bodyPanel
		JPanel bodyPanel = new JPanel();
		bodyPanel.setBackground(Color.getHSBColor(0.0f, 0.0f, 93.33f));

		// create dataTable in body Panel
		dataTable.setRowHeight(30);
		dataTable.setPreferredScrollableViewportSize(dataTable.getPreferredSize());
		dataTable.setBackground(Color.GRAY);
		JScrollPane scrollPane1 = new JScrollPane(dataTable);
		scrollPane1.setPreferredSize(new Dimension(1200, 580));

		// create a panel display SMode
		JPanel smodePanel = new JPanel();
		smodePanel.setPreferredSize(new Dimension(1200, 30));
		smodePanel.setBackground(Color.WHITE);
		;

		smodePanel.add(new JLabel("Speed:"));
		smodePanel.add(new JLabel(getData().getSMODE().get("Speed")));
		smodePanel.add(new JLabel(" "));

		smodePanel.add(new JLabel("Cadence:"));
		smodePanel.add(new JLabel(getData().getSMODE().get("Cadence")));
		smodePanel.add(new JLabel(" "));

		smodePanel.add(new JLabel("Altitude:"));
		smodePanel.add(new JLabel(getData().getSMODE().get("Altitude")));
		smodePanel.add(new JLabel(" "));

		smodePanel.add(new JLabel("Power:"));
		smodePanel.add(new JLabel(getData().getSMODE().get("Power")));
		smodePanel.add(new JLabel(" "));

		smodePanel.add(new JLabel("Power Left Right Balance:"));
		smodePanel.add(new JLabel(getData().getSMODE().get("Power Left Right Balance")));
		smodePanel.add(new JLabel(" "));

		smodePanel.add(new JLabel("Power Pedalling Index:"));
		smodePanel.add(new JLabel(getData().getSMODE().get("Power Pedalling Index")));
		smodePanel.add(new JLabel(" "));

		smodePanel.add(new JLabel("HR/CC data:"));
		smodePanel.add(new JLabel(getData().getSMODE().get("HR/CC data")));
		smodePanel.add(new JLabel(" "));

		smodePanel.add(new JLabel("US/Euro unit:"));
		smodePanel.add(new JLabel(getData().getSMODE().get("US/Euro unit")));

		// panel in tabbedPane
		JPanel dataPanel = new JPanel();
		dataPanel.add(smodePanel, BorderLayout.NORTH);
		dataPanel.add(scrollPane1, BorderLayout.SOUTH);

		// create a tab Panel to display data and chart
		tabbedPane.setPreferredSize(new Dimension(1600, 600));
		tabbedPane.addTab("Data", dataPanel);
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		bodyPanel.add(tabbedPane);

		chartPanel = new ChartPanel(chart());
		chartPanel.addChartMouseListener(this);
		chartPanel.addMouseListener(new CustomListener());
		tabbedPane.addTab("Chart", chartPanel);
		// Display frame in the center of window
		contain.add(tablePanel, BorderLayout.NORTH);
		contain.add(bodyPanel, BorderLayout.SOUTH);

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setJMenuBar(menuBar);
		frame.setSize(1600, 1200);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem source = (JMenuItem) (e.getSource());
		// When click "Save"
		if (source.getText().equals("Save")) {
			fd = new FileDialog(frame, "Save", FileDialog.SAVE);
			fd.setVisible(true);
		}

		// When click "Load"
		else if (source.getText().equals("Load")) {
			resetBodyTable();
			resetSummaryTable();
			data.model.setRowCount(0);
			fd = new FileDialog(frame, "Open", FileDialog.LOAD);
			fd.setVisible(true); // create and display FileDialog.
			try {
				if ((fd.getDirectory() != null) && (fd.getFile() != null)) {
					// get the path and file name.
					file = new File(fd.getDirectory(), fd.getFile());
					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
					Data data = new Data();
					String aline;
					int i = 1;
					// load file data to TextArea
					while ((aline = br.readLine()) != null) {
						// collect date to HashMap
						data.allMap.put(i, aline);
						// collect header information
						Pattern p = Pattern.compile(REGEX);
						Matcher m = p.matcher(aline);
						if (m.find()) {
							// record the line number of each line.
							data.headerMap.put(m.group(1), i);
						}
						i++;
					}
					fr.close();
					br.close();
					Polar polar = new Polar();
					this.frame.setVisible(false);
					// add data to table and display then hide this frame.
					data.tableData();
					polar.dataTable.setModel(data.dataModel);
					polar.summaryTable.setModel(data.summaryModel);
					polar.table.setModel(data.model);
					polar.setData(data);
					frame.setVisible(false);

					polar.GUI();

				}

			} catch (IOException ioe) {

				System.out.println(ioe);
			}

		} else if (source.getText().equals("Exit")) {
			// exit application
			System.exit(-1);
		} else if (source.getText().equals("About")) {
			// show a messagebox.
			JOptionPane.showMessageDialog(newMenuAbout, "Welcome to Polar!" + "\n" + "                        :)");
		}
		if(e.getSource().equals(button)){
			 this.dispose();
			 resetBodyTable();
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

	void resetChunkTable() {
		data.chunkModel.setRowCount(0);
	}
	public boolean isSpeed() {
		return speed;
	}

	public void setSpeed(boolean speed) {
		this.speed = speed;
	}
	public int[] getChunkData(int startPoint,int endPoint,int index) {
		
		int different=endPoint-startPoint+1;
		int extra=different%index;
		int average=(different-extra)/index;
		int []eachPoint=new int[index];
		for(int i=0;i<index;i++) {
			if (extra == 0) {
				if (i == 0) {
					eachPoint[i] = startPoint + average - 1;
				} else {
					eachPoint[i] = eachPoint[i - 1] + average;
				}
			} else {
				if (i == 0) {
					eachPoint[i] = startPoint + average;
				} else {
					eachPoint[i]=eachPoint[i-1]+average+1;
				}
				extra--;
			}
		}
		return eachPoint;
	}

	public static void main(String[] args) {
		Polar polar = new Polar();
		polar.GUI();
	}
	

	/**
	 * add data to Chart
	 * 
	 * @param speed
	 * @return
	 */
	private XYDataset createDataset(String strings) {

		// Initialize

		TimeSeriesCollection tsc = new TimeSeriesCollection();

		tsc.addSeries(getData().getChartData().get(strings));
		XYDataset dataset = tsc;

		return dataset;
	}

	/**
	 * a whole chart to be returned
	 * 
	 * @param speed
	 * @return
	 */
	public JFreeChart chart() {
		XYDataset dataset = createDataset("Speed");

		// Create chart
		JFreeChart chart = ChartFactory.createTimeSeriesChart("Polar", // Chart title
				"Time", // X-Axis Label
				"", // Y-Axis Label
				dataset, true, true, false);
		// Assign it to the chart
		XYPlot plot = (XYPlot) chart.getPlot();
		// X-axis1
		DateAxis rangeAxis1 = new DateAxis("Times");
		rangeAxis1.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 5));
		rangeAxis1.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
		rangeAxis1.setLowerMargin(0.0001);
		rangeAxis1.setUpperMargin(0.0001);
		plot.setDomainAxis(0, rangeAxis1);
		// X-axis2
		NumberAxis rangeAxis2 = new NumberAxis("");
		rangeAxis2.setRange(0, getData().getTime());
		rangeAxis2.setTickUnit(new NumberTickUnit(300));
		rangeAxis2.setVisible(false);
		plot.setDomainAxis(1, rangeAxis2);

		// Y-axis1
		XYDataset dataset1 = createDataset("Speed");
		NumberAxis axis1 = new NumberAxis("");
		XYLineAndShapeRenderer r1 = new XYLineAndShapeRenderer();
		r1.setSeriesPaint(0, new Color(134, 179, 51));
		r1.setSeriesShapesVisible(0, false);
		axis1.setAutoRangeIncludesZero(false);
		axis1.setLabelPaint(new Color(134, 179, 51));
		axis1.setTickLabelPaint(new Color(134, 179, 51));
		axis1.setRange(0, 33);
		axis1.setTickUnit(new NumberTickUnit(11));
		plot.setRangeAxis(0, axis1);
		plot.mapDatasetToRangeAxis(3, 1);
		plot.setRangeAxisLocation(0, org.jfree.chart.axis.AxisLocation.BOTTOM_OR_LEFT);
		plot.setDataset(0, dataset1);
		plot.mapDatasetToRangeAxis(0, 0);
		plot.setRenderer(0, r1);
		// Y-axis2
		XYDataset dataset2 = createDataset("Cadence");
		NumberAxis axis2 = new NumberAxis("");
		XYLineAndShapeRenderer r2 = new XYLineAndShapeRenderer();
		r2.setSeriesPaint(0, Color.ORANGE);
		r2.setSeriesShapesVisible(0, false);
		axis2.setAutoRangeIncludesZero(false);
		axis2.setLabelPaint(Color.ORANGE);
		axis2.setTickLabelPaint(Color.ORANGE);
		axis2.setRange(0, 125);
		axis2.setTickUnit(new NumberTickUnit(25));
		plot.setRangeAxis(1, axis2);
		plot.setRangeAxisLocation(1, org.jfree.chart.axis.AxisLocation.BOTTOM_OR_LEFT);
		plot.setDataset(1, dataset2);
		plot.mapDatasetToRangeAxis(1, 1);
		plot.setRenderer(1, r2);
		// Y-axis3
		XYDataset dataset3 = createDataset("Altitude");
		NumberAxis axis3 = new NumberAxis("");
		XYLineAndShapeRenderer r3 = new XYLineAndShapeRenderer();
		r3.setSeriesPaint(0, Color.BLACK);
		r3.setSeriesShapesVisible(0, false);
		axis3.setAutoRangeIncludesZero(false);
		axis3.setLabelPaint(Color.BLACK);
		axis3.setTickLabelPaint(Color.BLACK);
		axis3.setRange(307, 314);
		axis3.setTickUnit(new NumberTickUnit(1));
		plot.setRangeAxis(2, axis3);
		plot.setRangeAxisLocation(2, org.jfree.chart.axis.AxisLocation.BOTTOM_OR_LEFT);
		plot.setDataset(2, dataset3);
		plot.mapDatasetToRangeAxis(2, 2);
		plot.setRenderer(2, r3);
		// Y-axis4
		XYDataset dataset4 = createDataset("Heart");
		NumberAxis axis4 = new NumberAxis("");
		XYLineAndShapeRenderer r4 = new XYLineAndShapeRenderer();
		r4.setSeriesPaint(0, new Color(254, 67, 101));
		r4.setSeriesShapesVisible(0, false);
		axis4.setAutoRangeIncludesZero(false);
		axis4.setLabelPaint(new Color(254, 67, 101));
		axis4.setTickLabelPaint(new Color(254, 67, 101));
		axis4.setRange(0, 200);
		axis4.setTickUnit(new NumberTickUnit(50));
		plot.setRangeAxis(3, axis4);
		plot.setRangeAxisLocation(3, org.jfree.chart.axis.AxisLocation.BOTTOM_OR_RIGHT);
		plot.setDataset(3, dataset4);
		plot.mapDatasetToRangeAxis(3, 3);
		plot.setRenderer(3, r4);
		// Y-axis5
		XYDataset dataset5 = createDataset("Power");
		XYLineAndShapeRenderer r5 = new XYLineAndShapeRenderer();
		r5.setSeriesPaint(0, new Color(164, 34, 168));
		r5.setSeriesShapesVisible(0, false);
		NumberAxis axis5 = new NumberAxis("");
		axis5.setAutoRangeIncludesZero(false);
		axis5.setLabelPaint(new Color(164, 34, 168));
		axis5.setTickLabelPaint(new Color(164, 34, 168));
		axis5.setRange(0, 650);
		axis5.setTickUnit(new NumberTickUnit(100));
		plot.setRangeAxis(4, axis5);
		plot.setRangeAxisLocation(4, org.jfree.chart.axis.AxisLocation.BOTTOM_OR_RIGHT);
		plot.setDataset(4, dataset5);
		plot.mapDatasetToRangeAxis(4, 4);
		plot.setRenderer(4, r5);
		return chart;
	}

	/**
	 * Receives chart mouse click events.
	 *
	 * @param event
	 *            the event.
	 */
	@Override
	public void chartMouseClicked(ChartMouseEvent event) {
		//ignore
	}

	@Override
	public void chartMouseMoved(ChartMouseEvent event) {

		int mouseX = event.getTrigger().getX();
		int mouseY = event.getTrigger().getY();
		pointPress = new Point(mouseX, mouseY);

	}

	public class CustomListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			px = e.getX();
			py = e.getY();
		}

		public void mouseReleased(MouseEvent e) {

			int x = 0, y = 0;
			prx = e.getX();
			pry = e.getY();
			if (prx - px < 0) {
				resetSummaryTable();
				data.summaryDate(isSpeed());
				summaryTable.setModel(data.summaryModel);
			} else if (prx - px == 0) {
				
			} else {
				resetSummaryTable();
				x = (int) (pointPress.getX() + (prx - px));
				y = (int) (pointPress.getY() + (pry - py));
				getMouse(pointPress);
				startPoint = (int) chartX;
				// calculate pointRelease
				pointRelease = new Point(x, y);
				getMouse(pointRelease);
				endPoint = (int) chartX;
				getData().summaryDate(startPoint, endPoint);
				summaryTable.setModel(data.summaryModel);
			}
		}

		public void getMouse(Point po) {
			Point2D p = chartPanel.translateScreenToJava2D(po);

			XYPlot plot = (XYPlot) chart().getPlot();
			ChartRenderingInfo info = chartPanel.getChartRenderingInfo();
			Rectangle2D dataArea = info.getPlotInfo().getDataArea();
			//
			ValueAxis domainAxis = plot.getDomainAxis(1);
			RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
			chartX = domainAxis.java2DToValue(p.getX(), dataArea, domainAxisEdge);
		}
	}
}
