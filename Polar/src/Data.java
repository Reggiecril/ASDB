import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.util.RelativeDateFormat;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.DateRange;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class Data {
	DefaultTableModel model = new DefaultTableModel();
	DefaultTableModel dataModel = new DefaultTableModel();
	DefaultTableModel summaryModel = new DefaultTableModel();
	HashMap<Integer, String> allMap = new HashMap<Integer, String>();
	HashMap<String, Integer> headerMap = new HashMap<String, Integer>();
	private static String REGEX = "\\[(.*?)\\]";
	DecimalFormat df = new DecimalFormat("0.00");

	Data() {

	}

	/**
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * ADD TABLE DATA
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * ==========================================================================================================
	 */

	/**
	 * a method which add data to table
	 */
	public void tableData() {
		getNP();
		// header table date
		Object[] row = new Object[3];
		String[] columns = { "Date", "Start Time", "Interval" };
		model.setColumnIdentifiers(columns);
		String[] date = getParams().get("Date").split("");
		String date1 = date[6] + date[7] + "/" + date[4] + date[5] + "/" + date[0] + date[1] + date[2] + date[3];
		row[0] = date1;
		row[1] = getParams().get("StartTime");
		row[2] = getParams().get("Interval");
		model.addRow(row);
		// table body data
		String[][] spl = getIntTimes();
		String[] columns1 = { "Time", "Speed(km/h)", "Cadence(rpm)", "Altitude", "Heart rate", "Heart Rate(MAX)",
				"Power in watts" };
		dataModel.setColumnIdentifiers(columns1);
		for (int i = 0; i < spl[0].length; i++) {
			String[] row1 = spl[0][i].split("\t");
			String[] row2 = spl[1][i].split("\t");
			String[] row3 = spl[2][i].split("\t");
			String[] row4 = spl[3][i].split("\t");
			String[] row5 = spl[4][i].split("\t");
			Object[] dataRow = new Object[7];
			float Speed = Integer.valueOf(row2[3]);
			dataRow[0] = row1[0];
			dataRow[1] = Math.round(Speed / 10 * 1.609);
			dataRow[2] = row2[4];
			dataRow[3] = row2[5];
			dataRow[4] = row1[1];
			dataRow[5] = row1[4];
			dataRow[6] = row4[2];
			dataModel.addRow(dataRow);
		}
		summaryDate(true);
		// create chart panel

	}

	/**
	 * re-write tableDate(); this is only for table of body data. display data by
	 * different speed.
	 */
	public void tableData(boolean speed) {

		if (speed) {
			// table body data
			String[] columns1 = { "Time", "Speed(KM/H)", "Cadence(RPM)", "Altitude", "Heart Rate", "Heart Rate(MAX)",
					"Power In Watts" };
			dataModel.setColumnIdentifiers(columns1);
			String[][] spl = getIntTimes();
			for (int i = 0; i < spl[0].length; i++) {
				String[] row1 = spl[0][i].split("\t");
				String[] row2 = spl[1][i].split("\t");
				String[] row3 = spl[2][i].split("\t");
				String[] row4 = spl[3][i].split("\t");
				String[] row5 = spl[4][i].split("\t");
				Object[] dataRow = new Object[7];
				float Speed = Integer.valueOf(row2[3]);
				dataRow[0] = row1[0];
				dataRow[1] = Math.round(Speed / 10 * 1.609);
				dataRow[2] = row2[4];
				dataRow[3] = row2[5];
				dataRow[4] = row1[1];
				dataRow[5] = row1[4];
				dataRow[6] = row4[2];
				dataModel.addRow(dataRow);
			}
		} else {
			// table body data
			String[] columns1 = { "Time", "Speed(mph)", "Cadence(rpm)", "Altitude", "Heart rate", "Power in watts" };
			dataModel.setColumnIdentifiers(columns1);
			String[][] spl = getIntTimes();
			for (int i = 0; i < spl[0].length; i++) {
				String[] row1 = spl[0][i].split("\t");
				String[] row2 = spl[1][i].split("\t");
				String[] row3 = spl[2][i].split("\t");
				String[] row4 = spl[3][i].split("\t");
				String[] row5 = spl[4][i].split("\t");
				Object[] dataRow = new Object[7];
				float Speed = Integer.valueOf(row2[3]);
				dataRow[0] = row1[0];
				dataRow[1] = Math.round(Speed / 10);
				dataRow[2] = row2[4];
				dataRow[3] = row2[5];
				dataRow[4] = row1[1];
				dataRow[5] = row1[4];
				dataRow[6] = row4[2];
				dataModel.addRow(dataRow);
			}
		}

	}

	/**
	 * get data of summary table
	 * 
	 * @param speed
	 */
	public void summaryDate(boolean speed) {
		// summary body data
		if (speed) {
			String[] columns1 = { "Total distance covered", "Average speed(KM/H)", "Maximum speed(KM/H)",
					"Average heart rate", "Maximum heart rate", "Minimum heart rate", "Average power", "Maximum power",
					"Average altitude", "Maximum altitude" };
			summaryModel.setColumnIdentifiers(columns1);
		} else {
			String[] columns1 = { "Total distance covered", "Average speed(MPH)", "Maximum speed(MPH)",
					"Average heart rate", "Maximum heart rate", "Minimum heart rate", "Average power", "Maximum power",
					"Average altitude", "Maximum altitude" };
			summaryModel.setColumnIdentifiers(columns1);
		}
		// Initialize
		Object[] dataRow = new Object[10];
		int sumHeart = 0;
		int sumPower = 0;
		int sumSpeed = 0;
		int maxSpeed = 0;
		int maxHeart = 0;
		int minHeart = 200;
		int maxPower = 0;
		int sumAltitude = 0;
		int maxAltitude = 0;
		double[] speedData = getHRData().get("Speed");
		double[] heartData = getHRData().get("Heart");
		double[] powerData = getHRData().get("Power");
		double[] altitudeData = getHRData().get("Altitude");
		int length = speedData.length;
		// loop to calculate
		for (int i = 0; i < length; i++) {

			sumSpeed += (int) speedData[i];
			sumHeart += (int) heartData[i];
			sumPower += (int) powerData[i];
			sumAltitude += (int) altitudeData[i];
			// loop to select a max speed rate
			if (maxSpeed < (int) speedData[i]) {
				maxSpeed = (int) speedData[i];
			}
			// loop to select a max altitude rate
			if (maxAltitude < (int) altitudeData[i]) {
				maxAltitude = (int) altitudeData[i];
			}
			// loop to select a max heart rate
			if (maxHeart < (int) heartData[i]) {
				maxHeart = (int) heartData[i];
			}
			// loop to select a min heart rate
			if (minHeart > (int) heartData[i]) {
				minHeart = (int) heartData[i];
			}
			// loop to select a max power
			if (maxPower < (int) powerData[i]) {
				maxPower = (int) powerData[i];
			}
		}

		// Total distance
		if (speed) {
			dataRow[0] = getTrip().get("Distance");
		} else {
			dataRow[0] = Math.round(Integer.valueOf(getTrip().get("Distance")) / 1.609);
		}
		// Initialize Average speed
		double averageSpeed = sumSpeed / length;
		// Initialize Maximum speed
		double maximumSpeed = maxSpeed;
		if (speed) {
			// Average speed
			dataRow[1] = Math.round((averageSpeed / 10) * 0.62);
			// Maximum speed
			dataRow[2] = Math.round((maximumSpeed / 10) * 0.62);
		} else {
			// Average speed
			dataRow[1] = averageSpeed / 10;
			// Maximum speed
			dataRow[2] = maximumSpeed / 10;
		}
		// Average heart rate
		dataRow[3] = Math.round(sumHeart / length);
		// Maximum heart rate
		dataRow[4] = maxHeart;
		// Minimum heart rate
		dataRow[5] = minHeart;
		// Average power
		dataRow[6] = Math.round(sumPower / length);
		// Maximum power
		dataRow[7] = maxPower;
		// Average altitude
		dataRow[8] = Math.round(sumAltitude / length);
		// Maximum altitude
		dataRow[9] = maxAltitude;

		summaryModel.addRow(dataRow);

	}

	/**
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * GET DATA FROM FILE
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * ==========================================================================================================
	 */

	/**
	 * get the data of SMODE
	 * 
	 * @return
	 */
	public HashMap<String, String> getSMODE() {
		HashMap<String, String> map = new HashMap<String, String>();
		// get the line of SMODE and spilt it
		String[] smode = getParams().get("SMode").split("");

		// add data into speed
		if (smode[0].equals("0"))
			map.put("Speed", "off");
		else
			map.put("Speed", "on");
		// add data into cadence
		if (smode[1].equals("0"))
			map.put("Cadence", "off");
		else
			map.put("Cadence", "on");
		// add data into altitude
		if (smode[2].equals("0"))
			map.put("Altitude", "off");
		else
			map.put("Altitude", "on");
		// add data into Power
		if (smode[3].equals("0"))
			map.put("Power", "off");
		else
			map.put("Power", "on");
		// add data into Power Left Right Balance
		if (smode[4].equals("0"))
			map.put("Power Left Right Balance", "off");
		else
			map.put("Power Left Right Balance", "on");
		// add data into Power Pedaling Index
		if (smode[5].equals("0"))
			map.put("Power Pedalling Index", "off");
		else
			map.put("Power Pedalling Index", "on");
		// add data into HR/CC data
		if (smode[6].equals("0"))
			map.put("HR/CC data", "HR data only,");
		else
			map.put("HR/CC data", "HR + cycling data");
		// add data into US/Euro unit
		if (smode[7].equals("0"))
			map.put("US/Euro unit", "Euro");
		else
			map.put("US/Euro unit", "US");
		return map;
	}

	/**
	 * get the data from 'HRData',spilt to Heart,Speed,Cadence,Altitude,Power.
	 * 
	 * @return
	 */
	public HashMap<String, double[]> getHRData() {
		String[] header = getHeaderData("HRData");
		HashMap<String, double[]> map = new HashMap<String, double[]>();
		double[] heart = new double[header.length];
		double[] speed = new double[header.length];
		double[] cadence = new double[header.length];
		double[] altitude = new double[header.length];
		double[] power = new double[header.length];
		double[] powerBalance = new double[header.length];
		for (int i = 0; i < header.length; i++) {
			String[] line = header[i].split("\t");
			heart[i] = Double.valueOf(line[0]);
			speed[i] = Double.valueOf(line[1]);
			cadence[i] = Double.valueOf(line[2]);
			altitude[i] = Double.valueOf(line[3]);
			power[i] = Double.valueOf(line[4]);
			powerBalance[i] = Double.valueOf(line[5]);
		}
		map.put("Heart", heart);
		map.put("Speed", speed);
		map.put("Cadence", cadence);
		map.put("Altitude", altitude);
		map.put("Power", power);
		map.put("PowerBalance", powerBalance);
		return map;
	}

	/**
	 * get the data from 'IntTimes',then named every elements.
	 * 
	 * @return
	 */
	public String[][] getIntTimes() {
		String[] header = getHeaderData("IntTimes");
		int j = 0;
		String[][] spl = new String[5][count("IntTimes") / 5];
		for (int i = 0; i < count("IntTimes"); i += 5) {
			spl[0][j] = header[i];
			spl[1][j] = header[i + 1];
			spl[2][j] = header[i + 2];
			spl[3][j] = header[i + 3];
			spl[4][j] = header[i + 4];
			j++;
		}
		return spl;
	}

	/**
	 * get the data from 'Trip', then named each number
	 * 
	 * @return
	 */
	public HashMap<String, String> getTrip() {
		String[] header = getHeaderData("Trip");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Distance", header[0]);
		map.put("Ascent", header[1]);
		map.put("TotalTime", header[2]);
		map.put("AverageAltitude", header[3]);
		map.put("MaximumAltitude", header[4]);
		map.put("AverageSpeed", header[5]);
		map.put("MaximumSpeed", header[6]);
		map.put("Odometer", header[7]);
		return map;
	}

	/**
	 * get a data information array from Params
	 * 
	 * @return
	 */
	public HashMap<String, String> getParams() {
		String[] header = getHeaderData("Params");
		HashMap<String, String> map = new HashMap<String, String>();
		for (String line : header) {
			String lineData[] = line.split("=");
			map.put(lineData[0], lineData[1]);
		}

		return map;
	}

	/**
	 * get lines of a header
	 * 
	 * @param header
	 * @return
	 */
	public String[] getHeaderData(String header) {
		try {
			// Initialize a string array
			int count = count(header);
			String[] date = new String[count];
			// get the header line number
			int j = (int) headerMap.get(header);
			int i = 0;
			int c = 0;
			// add line content to string array.
			for (i = j; i < 1500000; i++) {
				String line = (String) allMap.get(i + 1);
				// if line is null ,end
				if (line != null) {
					// if line is empty, turn to next.
					if (line.trim().isEmpty())
						continue;
					// if line is header, end
					Pattern p = Pattern.compile(REGEX);
					Matcher m = p.matcher(line);
					if (m.find()) {
						break;
					}
					date[c] = line;
					c++;
				} else {
					break;
				}
			}
			return date;
		} catch (NullPointerException e) {
			throw e;
		}

	}

	/**
	 * count the number of lines from the part of header.
	 * 
	 * @param header
	 * @return
	 */
	public int count(String header) {
		// get the line number of header
		int j = (int) headerMap.get(header);
		int i = 0;
		int count;
		// start loop to get lines from header.
		for (i = j; i < 1500000; i++) {
			String line = (String) allMap.get(i + 1);
			// if the line is null, end it.
			if (line != null) {
				// if a line is empty,turn to next loop
				if (line.trim().isEmpty())
					continue;
				// if turn to next header,break the loop
				Pattern p = Pattern.compile(REGEX);
				Matcher m = p.matcher(line);
				if (m.find()) {
					i--;
					break;
				}
			} else {
				break;
			}
		}
		if (header == "Trip")
			count = i - j + 1;
		else
			count = i - j;
		return count;
	}

	/**
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * Calculation
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * ==========================================================================================================
	 */
	public int getFTP() {
		int ftp=301;
		return ftp;
	}
	/**
	 * calculate PowerBalance
	 * 
	 * @return
	 */
	public HashMap<String, Integer> getPowerBalance() {
		// load "PowerBalance" data
		double[] powerBalance = getHRData().get("PowerBalance");
		HashMap<String, Integer> map=new HashMap<String, Integer>();
		int totalLPB = 0,totalPI = 0;
		for (int i = 0; i < powerBalance.length; i++) {
			//calculate PI
			int PI=(int)(powerBalance[i]-(powerBalance[i]%256))/256;
			//calculate LPB
			int LPB=(int)powerBalance[i]%256;
			//calculate total
			totalPI+=PI;
			totalLPB+=LPB;
		}
		//calculate average
		int averageLPB=(int)totalLPB/powerBalance.length;
		int averagePI=(int)totalPI/powerBalance.length;
		//put PI,LPB,RPB into map
		map.put("PI", averagePI);
		map.put("LPB", averageLPB);
		map.put("RPB",1-averageLPB);
		return map;
	}


	/**
	 * calculate NP
	 * 
	 * @return
	 */
	public int getNP() {
		// load "power" data
		double[] power = getHRData().get("Power");
		// init total fourth power
		double FourthPower = 0;
		for (int i = 0; i < power.length; i++) {
			// stop program before 30s
			if (i + 29 < power.length) {
				double total = 0;
				// get total amount of each 30s interval
				for (int x = 0; x < 30; x++) {
					total += power[i + x];
				}
				double TotalFour = Math.pow(total / 30, 4);
				// calculate total fourth power
				FourthPower += TotalFour;
			}
		}
		// calculate total average
		double TotalAverage = FourthPower / (power.length - 29);
		// get fourth root of average
		int result = (int) Math.round(Math.pow(TotalAverage, 0.25));
		double a=232;
		double b=301;
		double f=(a/b);
		 DecimalFormat df = new DecimalFormat("#");
	        df.setMaximumFractionDigits(2);
	        System.out.printf(df.format(f));
		return result;
	}
	/**
	 * calculate IF
	 * 
	 * @return
	 */
	public double getIF() {
		int NP=getNP();
		int FTP=getFTP();
		double IF=NP/FTP;
		return IF;
	}

	/**
	 * calculate TSS
	 * 
	 * @return
	 */
	public double getTSS() {
		double TSS=0;
		
		return TSS;
	}



//	public void getFTP() {
//		double[] power = getHRData().get("Power");
//		double total=0;
//		for (int i = 0; i < power.length; i++) {
//			if(i<1800) {
//				total+=power[i];
//			}
//		}
//		double average=total/1800;
//		System.out.println(average);
//	}
	/**
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * CHART
	 * ==========================================================================================================
	 * ==========================================================================================================
	 * ==========================================================================================================
	 */
	/// for chart
	/// for chart
	/// for chart
	/**
	 * add data to Chart
	 * 
	 * @param speed
	 * @return
	 */
	private XYDataset createDataset(String strings) {

		// Initialize

		TimeSeriesCollection tsc = new TimeSeriesCollection();

		tsc.addSeries(getChartData().get(strings));
		XYDataset dataset = tsc;

		return dataset;
	}

	// get series
	public HashMap<String, TimeSeries> getChartData() {
		HashMap<String, TimeSeries> map = new HashMap<String, TimeSeries>();
		final TimeSeries series1 = new TimeSeries("Speed");

		TimeSeries series2 = new TimeSeries("Cadence");
		TimeSeries series3 = new TimeSeries("Altitude");
		TimeSeries series4 = new TimeSeries("Heart");
		TimeSeries series5 = new TimeSeries("Power");

		double[] dataSpeed = getHRData().get("Speed");
		double[] dataCadence = getHRData().get("Cadence");
		double[] dataAltitude = getHRData().get("Altitude");
		double[] dataHeart = getHRData().get("Heart");
		double[] dataPower = getHRData().get("Power");

		Second current = new Second(0, 0, 0, 1, 1, 2018);
		for (double nowTime : dataSpeed) {
			series1.add(current, new Double((nowTime / 10) * 0.62));
			current = (Second) current.next();
		}
		Second current1 = new Second(0, 0, 0, 1, 1, 2018);
		for (double nowTime : dataCadence) {
			series2.add(current1, new Double(nowTime));
			current1 = (Second) current1.next();
		}
		Second current2 = new Second(0, 0, 0, 1, 1, 2018);
		for (double nowTime : dataAltitude) {
			series3.add(current2, new Double(nowTime));
			current2 = (Second) current2.next();
		}
		Second current3 = new Second(0, 0, 0, 1, 1, 2018);
		for (double nowTime : dataHeart) {
			series4.add(current3, new Double(nowTime));
			current3 = (Second) current3.next();
		}
		Second current4 = new Second(0, 0, 0, 1, 1, 2018);
		for (double nowTime : dataPower) {
			series5.add(current4, new Double(nowTime));
			current4 = (Second) current4.next();
		}
		map.put("Speed", series1);
		map.put("Cadence", series2);
		map.put("Altitude", series3);
		map.put("Heart", series4);
		map.put("Power", series5);
		return map;
	}

	// calculate time to second and return to Integer
	public int getTime(String time) {
		int calculate = 0;
		String[] timeRow = time.split(":");
		double row2 = Double.valueOf(timeRow[2]);
		calculate = Integer.valueOf(timeRow[0]) * 3600 + Integer.valueOf(timeRow[1]) * 60 + (int) row2;
		return calculate;
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
				dataset, true, false, false);
		// Assign it to the chart
		XYPlot plot = (XYPlot) chart.getPlot();

		DateAxis rangeAxis = new DateAxis("Times");
		rangeAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 5));
		rangeAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
		plot.setDomainAxis(rangeAxis);
		// axis1
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
		// axis2
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
		// axis3
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
		// axis4
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
		// axis5
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
}
