package polar;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView.TableRow;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

public class Comparison extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Data firstData, secondData;
	public ChartPanel firstChartPanel, secondChartPanel;
	public JFreeChart firstChart, secondChart;
	public JTable headerTable = new JTable();
	public JTable differentTable = new JTable();
	public ComparisonData CD = new ComparisonData();
	public Crosshair xCrosshair;
	JTextField text = new JTextField(5);

	public Comparison(Data firstData, Data secondData) {
		this.setFirstData(firstData);
		this.setSecondData(secondData);
	}

	public void GUI() {
		JFrame frame = new JFrame();
		Container contain = frame.getContentPane();
		contain.setLayout(new BoxLayout(contain, BoxLayout.Y_AXIS));

		// two chart Panel
		firstChart = chart(getFirstData(), "Origin Chart");
		firstChartPanel = new ChartPanel(firstChart);
		firstChartPanel.setSize(new Dimension(1200, 300));
		firstChartPanel.setMaximumSize(new Dimension(1200, 300));
		firstChartPanel.setPreferredSize(new Dimension(1200, 300));
		firstChartPanel.setMinimumSize(new Dimension(1200, 300));
		firstChartPanel.addChartMouseListener(new FirstChartListener());
		CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
		this.xCrosshair = new Crosshair(Double.NaN, Color.GRAY, new BasicStroke(0f));
		this.xCrosshair.setLabelVisible(true);
		crosshairOverlay.addDomainCrosshair(xCrosshair);
		firstChartPanel.addOverlay(crosshairOverlay);

		secondChart = chart(getSecondData(), "Comparison Chart");
		secondChartPanel = new ChartPanel(chart(getSecondData(), "Comparison Chart"));
		secondChartPanel.setSize(new Dimension(1200, 300));
		secondChartPanel.setMaximumSize(new Dimension(1200, 300));
		secondChartPanel.setPreferredSize(new Dimension(1200, 300));
		secondChartPanel.setMinimumSize(new Dimension(1200, 300));
		secondChartPanel.addChartMouseListener(new SecondChartListener());
		secondChartPanel.addOverlay(crosshairOverlay);

		JPanel functionPanel = new JPanel();
		functionPanel.setSize(1200, 50);
		functionPanel.setMaximumSize(new Dimension(1200, 50));
		functionPanel.setPreferredSize(new Dimension(1200, 50));
		functionPanel.setMinimumSize(new Dimension(1200, 50));
		functionPanel.setLayout(new FlowLayout());

		// date table
		JPanel tablePanel = new JPanel();

		headerTable.setRowHeight(30);
		headerTable.setBackground(Color.YELLOW);
		headerTable.setPreferredScrollableViewportSize(headerTable.getPreferredSize());
		JScrollPane scrollPane = new JScrollPane(headerTable);
		scrollPane.setPreferredSize(new Dimension(250, 50));
		functionPanel.add(scrollPane);

		functionPanel.add(new JLabel("   "));
		functionPanel.add(new JLabel("Chunk:"));
		functionPanel.add(new JLabel(" "));
		// create text

		text.setPreferredSize(new Dimension(100, 30));
		functionPanel.add(text);
		// create button
		JButton button = new JButton("Chunk");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (text.getText().isEmpty()) {
					JOptionPane.showMessageDialog(frame,
							"Please enter a chunk number!" + "\n" + "                        :)");
				} else {
					differentTable.setVisible(true);
					resetDifferentTable();
					resetChunkModel();
					int index = Integer.valueOf(text.getText().toString());
					int[] firstDataArray = getChunkData(getFirstData(), index);
					int[] secondDataArray = getChunkData(getSecondData(), index);
					for (int i = 0; i < index; i++) {
						System.out.println("i:" + firstDataArray[i] + " index:" + index);
						if (i == 0) {
							CD.chunkData(getFirstData(), "Origin", i, 0, firstDataArray[i]);
							CD.chunkData(getSecondData(), "Comparison", i, 0, secondDataArray[i]);
							CD.chunkDifferentData(getFirstData(), getSecondData(), "Different", i, 0, firstDataArray[i],
									0, secondDataArray[i]);
							CD.addEmptyRowDifferent();
						} else {
							CD.chunkData(getFirstData(), "Origin", i, firstDataArray[i - 1], firstDataArray[i]);
							CD.chunkData(getSecondData(), "Comparison", i, secondDataArray[i - 1], secondDataArray[i]);
							CD.chunkDifferentData(getFirstData(), getSecondData(), "Different", i,
									firstDataArray[i - 1], firstDataArray[i], secondDataArray[i - 1],
									secondDataArray[i]);
							CD.addEmptyRowDifferent();
						}

					}
					differentTable.setModel(CD.ComparisonChunkModel);
				}

			}

		});
		functionPanel.add(button);

		firstChartPanel.addOverlay(crosshairOverlay);
		tablePanel.setSize(1200, 350);
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setMaximumSize(new Dimension(1200, 350));
		tablePanel.setPreferredSize(new Dimension(1200, 350));
		tablePanel.setMinimumSize(new Dimension(1200, 350));
		// create table
		differentTable.setVisible(false);
		differentTable.setRowHeight(30);
		differentTable.setBackground(Color.YELLOW);
		differentTable.setPreferredScrollableViewportSize(differentTable.getPreferredSize());
		JScrollPane differentPanel = new JScrollPane(differentTable);
		differentPanel.setPreferredSize(new Dimension(1200, 350));
		tablePanel.add(differentPanel, BorderLayout.CENTER);

		JPanel firstPanel = new JPanel();
		firstPanel.setSize(1200, 300);
		firstPanel.setMaximumSize(new Dimension(1200, 300));
		firstPanel.setPreferredSize(new Dimension(1200, 300));
		firstPanel.setMinimumSize(new Dimension(1200, 300));
		firstPanel.add(firstChartPanel);

		JPanel secondPanel = new JPanel();
		secondPanel.setSize(1200, 300);
		secondPanel.setMaximumSize(new Dimension(1200, 300));
		secondPanel.setPreferredSize(new Dimension(1200, 300));
		secondPanel.setMinimumSize(new Dimension(1200, 300));
		secondPanel.add(secondChartPanel);

		// Display frame in the center of window
		contain.add(functionPanel);
		contain.add(tablePanel);
		contain.add(firstPanel);
		contain.add(secondPanel);
		// frame
		frame.pack();
		frame.setSize(1200, 1000);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	public Data getFirstData() {
		return firstData;
	}

	public void setFirstData(Data firstData) {
		this.firstData = firstData;
	}

	public Data getSecondData() {
		return secondData;
	}

	public void setSecondData(Data secondData) {
		this.secondData = secondData;
	}

	public void resetHeaderTable() {
		CD.ComparisonHeaderModel.setRowCount(0);
	}

	public void resetDifferentTable() {
		CD.ComparisonDifferentModel.setRowCount(0);
	}

	public void resetChunkModel() {
		CD.ComparisonChunkModel.setRowCount(0);
	}

	public int[] getChunkData(Data data, int index) {

		int different = data.getTime() + 1;
		int extra = different % index;
		int average = (different - extra) / index;
		int[] eachPoint = new int[index];
		for (int i = 0; i < index; i++) {
			if (extra == 0) {
				if (i == 0) {
					eachPoint[i] = 0 + average - 1;
				} else {
					eachPoint[i] = eachPoint[i - 1] + average;
				}
			} else {
				if (i == 0) {
					eachPoint[i] = 0 + average;
				} else {
					eachPoint[i] = eachPoint[i - 1] + average + 1;
				}
				extra--;
			}
		}
		return eachPoint;
	}

	public class FirstChartListener implements ChartMouseListener {

		@Override
		public void chartMouseClicked(ChartMouseEvent event) {

		}

		@Override
		public void chartMouseMoved(ChartMouseEvent event) {
			differentTable.setVisible(true);
			resetHeaderTable();
			resetDifferentTable();
			resetChunkModel();
			int mouseX = event.getTrigger().getX();
			int mouseY = event.getTrigger().getY();

			Point2D p = firstChartPanel.translateScreenToJava2D(new Point(mouseX, mouseY));

			XYPlot plot = (XYPlot) firstChart.getPlot();
			ChartRenderingInfo info = firstChartPanel.getChartRenderingInfo();
			Rectangle2D dataArea = info.getPlotInfo().getDataArea();
			//
			ValueAxis domainAxis = plot.getDomainAxis(1);
			RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
			double chartX = domainAxis.java2DToValue(p.getX(), dataArea, domainAxisEdge);
			int point = (int) chartX;
			CD.getHeaderData(point);
			headerTable.setModel(CD.ComparisonHeaderModel);
			CD.getDifferentData(point, "Origin Chart", getFirstData());
			CD.getDifferentData(point, "Comparison Chart", getSecondData());
			CD.calculateDifferentData(point, "Different", getFirstData(), getSecondData());
			differentTable.setModel(CD.ComparisonDifferentModel);

			Rectangle2D Area = firstChartPanel.getScreenDataArea();
			ValueAxis xAxis = plot.getDomainAxis();
			double x = xAxis.java2DToValue(event.getTrigger().getX(), Area, RectangleEdge.BOTTOM);
			xCrosshair.setValue(x);

		}

	}

	public class SecondChartListener implements ChartMouseListener {

		@Override
		public void chartMouseClicked(ChartMouseEvent event) {

		}

		@Override
		public void chartMouseMoved(ChartMouseEvent event) {
			differentTable.setVisible(true);
			resetHeaderTable();
			resetDifferentTable();
			resetChunkModel();
			int mouseX = event.getTrigger().getX();
			int mouseY = event.getTrigger().getY();

			Point2D p = secondChartPanel.translateScreenToJava2D(new Point(mouseX, mouseY));

			XYPlot plot = (XYPlot) secondChart.getPlot();
			ChartRenderingInfo info = secondChartPanel.getChartRenderingInfo();
			Rectangle2D dataArea = info.getPlotInfo().getDataArea();
			//
			ValueAxis domainAxis = plot.getDomainAxis(1);
			RectangleEdge domainAxisEdge = plot.getDomainAxisEdge();
			double chartX = domainAxis.java2DToValue(p.getX(), dataArea, domainAxisEdge);
			int point = (int) chartX;
			CD.getHeaderData(point);
			headerTable.setModel(CD.ComparisonHeaderModel);
			CD.getDifferentData(point, "Origin Chart", getFirstData());
			CD.getDifferentData(point, "Comparison Chart", getSecondData());
			CD.calculateDifferentData(point, "Different", getFirstData(), getSecondData());
			differentTable.setModel(CD.ComparisonDifferentModel);

			Rectangle2D Area = firstChartPanel.getScreenDataArea();
			ValueAxis xAxis = plot.getDomainAxis();
			double x = xAxis.java2DToValue(event.getTrigger().getX(), Area, RectangleEdge.BOTTOM);
			xCrosshair.setValue(x);
		}

	}

	/**
	 * add data to Chart
	 * 
	 * @param speed
	 * @return
	 */
	public XYDataset createDataset(String strings, Data data) {

		// Initialize

		TimeSeriesCollection tsc = new TimeSeriesCollection();

		tsc.addSeries(data.getChartData().get(strings));
		XYDataset dataset = tsc;

		return dataset;
	}

	/**
	 * a whole chart to be returned
	 * 
	 * @param speed
	 * @return
	 */
	public JFreeChart chart(Data data, String title) {
		XYDataset dataset = createDataset("Speed", data);

		// Create chart
		JFreeChart chart = ChartFactory.createTimeSeriesChart(title, // Chart title
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
		rangeAxis2.setRange(0, data.getTime());
		rangeAxis2.setTickUnit(new NumberTickUnit(300));
		rangeAxis2.setVisible(true);
		plot.setDomainAxis(1, rangeAxis2);

		// Y-axis1
		XYDataset dataset1 = createDataset("Speed", data);
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
		XYDataset dataset2 = createDataset("Cadence", data);
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
		XYDataset dataset3 = createDataset("Altitude", data);
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
		XYDataset dataset4 = createDataset("Heart", data);
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
		XYDataset dataset5 = createDataset("Power", data);
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
}
