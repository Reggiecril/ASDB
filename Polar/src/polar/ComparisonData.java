package polar;

import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class ComparisonData {
	public DefaultTableModel ComparisonHeaderModel = new DefaultTableModel();
	public DefaultTableModel ComparisonDifferentModel = new DefaultTableModel();
	public DefaultTableModel ComparisonChunkModel = new DefaultTableModel();
	public ComparisonData() {
		
	}
	/**
	 * a method which add data to Comparison Header table
	 */
	public void getHeaderData(int point) {
		// header table date
		String[] column = { "Time" };
		ComparisonHeaderModel.setColumnIdentifiers(column);
		Object[] row = new Object[1];
		row[0] = getTime(point);
		ComparisonHeaderModel.addRow(row);
	}

	/**
	 * a method which add data to Comparison Header table
	 */
	public void defaultHeaderData() {
		// header table date
		ComparisonHeaderModel.addColumn("Time");
		Object[] row = new Object[1];
		row[0] = "00:00:00";
		ComparisonHeaderModel.addRow(row);
	}

	/**
	 * a method which add data to table
	 */
	public void getDifferentData(int point, String title, Data data) {

		// header table date
		Object[] row = new Object[14];
		String[] columns = { "", "Distance", "Average Speed", "Average Cadence", "Average Altitude", "Average Power",
				"Heart", "Speed", "Cadence", "Altitude", "Power", "PI", "LPB", "RPB" };
		ComparisonDifferentModel.setColumnIdentifiers(columns);
		HashMap<String, Double> map = getPointData(point, data);
		HashMap<String, Double> averageMap = getAverage(point, data);
		row[0] = title;
		row[1] = averageMap.get("Distance");
		row[2] = averageMap.get("Average Speed");
		row[3] = averageMap.get("Average Cadence");
		row[4] = averageMap.get("Average Altitude");
		row[5] = averageMap.get("Average Power");
		row[6] = map.get("Heart");
		row[7] = map.get("Speed");
		row[8] = map.get("Cadence");
		row[9] = map.get("Altitude");
		row[10] = map.get("Power");
		row[11] = map.get("PI");
		row[12] = map.get("LPB");
		row[13] = map.get("RPB");
		ComparisonDifferentModel.addRow(row);

	}

	/**
	 * a method which add data to table
	 */
	public void calculateDifferentData(int point, String title, Data firstData, Data secondData) {

		HashMap<String, Double> firstMap = getPointData(point, firstData);
		HashMap<String, Double> secondMap = getPointData(point, secondData);
		HashMap<String, Double> firstAverageMap = getAverage(point, firstData);
		HashMap<String, Double> secondAverageMap = getAverage(point, secondData);
		// calculate different
		double distance, averageSpeed, averageCadence, averageAltitude, averagePower, heart, speed, cadence, altitude,
				power, PI, LPB, RPB;
		distance = firstAverageMap.get("Distance") - secondAverageMap.get("Distance");
		averageSpeed = firstAverageMap.get("Average Speed") - secondAverageMap.get("Average Speed");
		averageCadence = firstAverageMap.get("Average Cadence") - secondAverageMap.get("Average Cadence");
		averageAltitude = firstAverageMap.get("Average Altitude") - secondAverageMap.get("Average Altitude");
		averagePower = firstAverageMap.get("Average Power") - secondAverageMap.get("Average Power");
		heart = firstMap.get("Heart") - secondMap.get("Heart");
		speed = firstMap.get("Speed") - secondMap.get("Speed");
		cadence = firstMap.get("Cadence") - secondMap.get("Cadence");
		altitude = firstMap.get("Altitude") - secondMap.get("Altitude");
		power = firstMap.get("Power") - secondMap.get("Power");
		PI = firstMap.get("PI") - secondMap.get("PI");
		LPB = firstMap.get("LPB") - secondMap.get("LPB");
		RPB = firstMap.get("RPB") - secondMap.get("RPB");

		// header table date
		Object[] row = new Object[14];
		String[] columns = { "", "Distance", "Average Speed", "Average Cadence", "Average Altitude", "Average Power",
				"Heart", "Speed", "Cadence", "Altitude", "Power", "PI", "LPB", "RPB" };
		ComparisonDifferentModel.setColumnIdentifiers(columns);

		row[0] = title;
		row[1] = distance;
		row[2] = averageSpeed;
		row[3] = averageCadence;
		row[4] = averageAltitude;
		row[5] = averagePower;
		row[6] = heart;
		row[7] = speed;
		row[8] = cadence;
		row[9] = altitude;
		row[10] = power;
		row[11] = PI;
		row[12] = LPB;
		row[13] = RPB;
		ComparisonDifferentModel.addRow(row);

	}

	/**
	 * add a empty row to different model
	 */
	public void addEmptyRowDifferent() {
		String[] columns = { "", "Section", "Start Time", "End Time", "Distance covered", "Average speed(KM/H)",
				"Maximum speed(KM/H)", "Average heart rate", "Maximum heart rate", "Minimum heart rate",
				"Average power", "Maximum power", "Average altitude", "Maximum altitude", "PI",
				"Power Balance(LPB/RPB)", "NP", "IF", "TSS" };
		ComparisonChunkModel.setColumnIdentifiers(columns);
		Object[] row = new Object[19];
		for (Object x : row)
			x = "";
		ComparisonChunkModel.addRow(row);
	}
/**
 * chunk data from two object
 * @param data
 * @param title
 * @param section
 * @param number1
 * @param number2
 */
	public void chunkData(Data data, String title, int section, int number1, int number2) {
		HashMap<String, Double> map = getChunkMap(data, number1, number2);
		// chunk data
		String[] columns1 = { "", "Section", "Start Time", "End Time", "Distance covered", "Average speed(KM/H)",
				"Maximum speed(KM/H)", "Average heart rate", "Maximum heart rate", "Minimum heart rate",
				"Average power", "Maximum power", "Average altitude", "Maximum altitude", "PI",
				"Power Balance(LPB/RPB)", "NP", "IF", "TSS" };
		ComparisonChunkModel.setColumnIdentifiers(columns1);
		// Initialize
		Object[] chunkRow = new Object[19];
		// title
		chunkRow[0] = title;
		// section
		chunkRow[1] = section+1;
		// start time
		chunkRow[2] = getTime(number1);
		// end time
		chunkRow[3] =getTime(number2);
		// Total distance
		chunkRow[4] = map.get("Distance");

		// Average speed
		chunkRow[5] = map.get("Average Speed");
		// Maximum speed
		chunkRow[6] = map.get("Maximum Speed");

		// Average heart rate
		chunkRow[7] = map.get("Average Heart Rate");
		// Maximum heart rate
		chunkRow[8] = map.get("Maximum Heart Rate");
		// Minimum heart rate
		chunkRow[9] = map.get("Minimum Heart Rate");
		// Average power
		chunkRow[10] = map.get("Average Power");
		// Maximum power
		chunkRow[11] = map.get("Maximum Power");
		// Average altitude
		chunkRow[12] = map.get("Average Altitude");
		// Maximum altitude
		chunkRow[13] = map.get("Maximum Altitude");
		if (data.existPowerBalance()) {
			// PI
			chunkRow[14] = map.get("PI");
			// Power Balance

			chunkRow[15] = map.get("LPB")+"/"+map.get("RPB");

		} else {
			// PI
			chunkRow[14] = 0;
			// Power Balance
			chunkRow[15] = 0+"/"+0;
		}
		// NP
		chunkRow[16] = map.get("NP");
		// IF
		chunkRow[17] = map.get("IF");
		// TSS
		chunkRow[18] = map.get("TSS");
		ComparisonChunkModel.addRow(chunkRow);
	}

	public void chunkDifferentData(Data firstData, Data secondData, String title, int section,int firstStart,int firstEnd,int secondStart,int secondEnd) {
		HashMap<String, Double> firstMap = getChunkMap(firstData, firstStart, firstEnd);
		HashMap<String, Double> secondMap = getChunkMap(secondData, secondStart, secondEnd);
		// chunk data
		String[] columns1 = { "", "Section", "Start Time", "End Time", "Distance covered", "Average speed(KM/H)",
				"Maximum speed(KM/H)", "Average heart rate", "Maximum heart rate", "Minimum heart rate",
				"Average power", "Maximum power", "Average altitude", "Maximum altitude", "PI",
				"Power Balance(LPB/RPB)", "NP", "IF", "TSS" };
		ComparisonChunkModel.setColumnIdentifiers(columns1);
		// Initialize
		Object[] chunkRow = new Object[19];
		// title
		chunkRow[0] = "Different";
		// section
		chunkRow[1] = section+1;
		// start time
		chunkRow[2] = "";
		// end time
		chunkRow[3] = "";
		// Total distance
		chunkRow[4] = firstMap.get("Distance") - secondMap.get("Distance");

		// Average speed
		chunkRow[5] = firstMap.get("Average Speed")-secondMap.get("Average Speed");
		// Maximum speed
		chunkRow[6] = (((double) (firstMap.get("Maximum Speed")) - (double) (secondMap.get("Maximum Speed"))));

		// Average heart rate
		chunkRow[7] = (((double) (firstMap.get("Average Heart Rate"))
				- (double) (secondMap.get("Average Heart Rate"))));
		// Maximum heart rate
		chunkRow[8] = (((double) (firstMap.get("Maximum Heart Rate"))
				- (double) (secondMap.get("Maximum Heart Rate"))));
		// Minimum heart rate
		chunkRow[9] = (((double) (firstMap.get("Minimum Heart Rate"))
				- (double) (secondMap.get("Minimum Heart Rate"))));
		// Average power
		chunkRow[10] = (((double) (firstMap.get("Average Power")) - (double) (secondMap.get("Average Power"))));
		// Maximum power
		chunkRow[11] = (((double) (firstMap.get("Maximum Power")) - (double) (secondMap.get("Maximum Power"))));
		// Average altitude
		chunkRow[12] = (((double) (firstMap.get("Average Altitude")) - (double) (secondMap.get("Average Altitude"))));
		// Maximum altitude
		chunkRow[13] = (((double) (firstMap.get("Maximum Altitude")) - (double) (secondMap.get("Maximum Altitude"))));

		// PI
		chunkRow[14] = (((double) (firstMap.get("PI")) - (double) (secondMap.get("PI"))));
		// Power Balance
		double LPB=firstMap.get("LPB")-secondMap.get("LPB");
		double RPB=firstMap.get("RPB")-secondMap.get("RPB");
		chunkRow[15] = LPB+"/"+RPB;

		// NP
		chunkRow[16] = (((double) (firstMap.get("NP")) - (double) (secondMap.get("NP"))));
		// IF
		chunkRow[17] = (((double) (firstMap.get("IF")) - (double) (secondMap.get("IF"))));
		// TSS
		chunkRow[18] = (((double) (firstMap.get("TSS")) - (double) (secondMap.get("TSS"))));
		ComparisonChunkModel.addRow(chunkRow);
	}

	/**
	 * put the chunk data to map
	 * 
	 * @param speed
	 */
	public HashMap<String, Double> getChunkMap(Data data, int number1, int number2) {
		HashMap<String, Double> map = new HashMap<String, Double>();

		int sumHeart = 0;
		int sumPower = 0;
		int sumSpeed = 0;
		int maxSpeed = 0;
		int maxHeart = 0;
		int minHeart = 200;
		int maxPower = 0;
		int sumAltitude = 0;
		int maxAltitude = 0;

		// protect if point is o,o
		if (number1 < 0)
			number1 = 0;
		else if (number2 > data.getTime())
			number2 = data.getTime();
		String[] header = data.getHeaderData("HRData");
		if (data.getParams().get("Interval") != "1") {
			int interval = Integer.valueOf(data.getParams().get("Interval"));
			String[] header1 = new String[header.length * interval];
			for (int i = 0, length = header.length; i < length; i++) {
				for (int j = 0; j < interval; j++)
					header1[i * interval + j] = header[i];
			}
			header = header1;
		}
		int length = number2 - number1 + 1;
		double[] heartData = new double[length];
		double[] speedData = new double[length];
		double[] cadenceData = new double[length];
		double[] altitudeData = new double[length];
		double[] powerData = new double[length];
		double[] powerBalanceData = new double[length];
		int j = 0;
		int a = 0;
		for (String x : header) {
			if (j >= number1 && j <= number2) {
				String[] line = x.split("\t");
				heartData[a] = Double.valueOf(line[0]);
				speedData[a] = Double.valueOf(line[1]);
				cadenceData[a] = Double.valueOf(line[2]);
				altitudeData[a] = Double.valueOf(line[3]);
				powerData[a] = Double.valueOf(line[4]);
				if (line.length == 5) {
					powerBalanceData = null;
				} else {
					powerBalanceData[a] = Double.valueOf(line[5]);
				}
				a++;
			}
			j++;
		}

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
		// Initialize Average speed
		double averageSpeed = sumSpeed / length;
		// Initialize Maximum speed
		double maximumSpeed = maxSpeed;

		double distance = (averageSpeed / 10) * length / 3600;

		// Total distance
		map.put("Distance", distance);

		// Average speed
		map.put("Average Speed",Double.valueOf( Math.round((averageSpeed / 10) * 0.62)));
		// Maximum speed
		map.put("Maximum Speed", Double.valueOf(Math.round((maximumSpeed / 10) * 0.62)));

		// Average heart rate
		map.put("Average Heart Rate", Double.valueOf(Math.round(sumHeart / length)));
		// Maximum heart rate
		map.put("Maximum Heart Rate", Double.valueOf(maxHeart));
		// Minimum heart rate
		map.put("Minimum Heart Rate", Double.valueOf(minHeart));
		// Average power
		map.put("Average Power", Double.valueOf(Math.round(sumPower / length)));
		// Maximum power
		map.put("Maximum Power", Double.valueOf(maxPower));
		// Average altitude
		map.put("Average Altitude", Double.valueOf(Math.round(sumAltitude / length)));
		// Maximum altitude
		map.put("Maximum Altitude", Double.valueOf(maxAltitude));
		if (data.existPowerBalance()) {
			// PI
			int PI = data.getPowerBalance(powerBalanceData).get("PI");
			map.put("PI", Double.valueOf(PI));
			// Power Balance
			map.put("LPB", Double.valueOf(data.getPowerBalance(powerBalanceData).get("LPB")));
			map.put("RPB", Double.valueOf(data.getPowerBalance(powerBalanceData).get("RPB")));

		} else {
			// PI
			map.put("PI", 0.0);
			// Power Balance
			map.put("LPB",0.0);
			map.put("RPB", 0.0);
		}
		// NP
		int NP = data.getNP(powerData);
		map.put("NP", Double.valueOf(NP));
		// IF
		double IF = data.getIF(NP, data.getFTP());
		map.put("IF", Double.valueOf(IF));
		// TSS
		map.put("TSS", Double.valueOf(data.getTSS(data.getTime(), NP, IF, data.getFTP())));

		return map;

	}
/**
 * get point position and get data
 * @param point
 * @param data
 * @return
 */
	public HashMap<String, Double> getPointData(int point, Data data) {
		HashMap<String, Double> map = new HashMap<String, Double>();

		if (point > data.getTime()) {
			map.put("Heart", 0.0);
			map.put("Speed", 0.0);
			map.put("Cadence", 0.0);
			map.put("Altitude", 0.0);
			map.put("Power", 0.0);
			map.put("PI", 0.0);
			map.put("LPB", 0.0);
			map.put("RPB", 0.0);
		} else if (point < 0) {
			map.put("Heart", 0.0);
			map.put("Speed", 0.0);
			map.put("Cadence", 0.0);
			map.put("Altitude", 0.0);
			map.put("Power", 0.0);
			map.put("PI", 0.0);
			map.put("LPB", 0.0);
			map.put("RPB", 0.0);
		} else {
			String[] header = data.getHeaderData("HRData");
			if (data.getParams().get("Interval") != "1") {
				int interval = Integer.valueOf(data.getParams().get("Interval"));
				String[] header1 = new String[header.length * interval];
				for (int i = 0, length = header.length; i < length; i++) {
					for (int j = 0; j < interval; j++)
						header1[i * interval + j] = header[i];
				}
				header = header1;
			}
			double heart, speed, cadence, altitude, power, powerBalance;
			String[] line = header[point].split("\t");
			heart = Double.valueOf(line[0]);
			speed = Double.valueOf(line[1]);
			cadence = Double.valueOf(line[2]);
			altitude = Double.valueOf(line[3]);
			power = Double.valueOf(line[4]);
			if (line.length == 5) {
				powerBalance = 0;
			} else {
				powerBalance = Double.valueOf(line[5]);
			}

			map.put("Heart", heart);
			map.put("Speed", speed);
			map.put("Cadence", cadence);
			map.put("Altitude", altitude);
			map.put("Power", power);
			map.put("PI", (double) (data.getPowerBalance(powerBalance).get("PI")));
			map.put("LPB", (double) (data.getPowerBalance(powerBalance).get("LPB")));
			map.put("RPB", (double) (data.getPowerBalance(powerBalance).get("RPB")));
		}
		return map;
	}
/**
 * calculate average and put them to map
 * @param point
 * @param data
 * @return
 */
	public HashMap<String, Double> getAverage(int point, Data data) {
		HashMap<String, Double> map = new HashMap<String, Double>();
		String[] header = data.getHeaderData("HRData");
		if (data.getParams().get("Interval") != "1") {
			int interval = Integer.valueOf(data.getParams().get("Interval"));
			String[] header1 = new String[header.length * interval];
			for (int i = 0, length = header.length; i < length; i++) {
				for (int j = 0; j < interval; j++)
					header1[i * interval + j] = header[i];
			}
			header = header1;
		}
		double speed = 0, cadence = 0, altitude = 0, power = 0;
		double totalSpeed = 0, totalCadence = 0, totalAltitude = 0, totalPower = 0;
		double distance = 0, averageSpeed = 0, averageCadence = 0, averageAltitude = 0, averagePower = 0;
		for (int i = 0; i < point; i++) {

			if (i > data.getTime() - 1) {
				speed = 0;
				cadence = 0;
				altitude = 0;
				power = 0;
			} else if (point < 0) {
				speed = 0;
				cadence = 0;
				altitude = 0;
				power = 0;
			} else {
				String[] line = header[i].split("\t");
				speed = Double.valueOf(line[1]);
				cadence = Double.valueOf(line[2]);
				altitude = Double.valueOf(line[3]);
				power = Double.valueOf(line[4]);
			}
			totalSpeed += speed;
			totalCadence += cadence;
			totalAltitude += altitude;
			totalPower += power;
		}
		averageSpeed = totalSpeed / point / 10;
		averageCadence = totalCadence / point;
		averageAltitude = totalAltitude / point;
		averagePower = totalPower / point;
		distance = averageSpeed * point / 3600;
		map.put("Distance", (double) (Math.round(distance * 1000) / 1000));
		map.put("Average Speed", (double) (Math.round(averageSpeed * 1000) / 1000));
		map.put("Average Cadence", (double) (Math.round(averageCadence * 1000) / 1000));
		map.put("Average Altitude", (double) (Math.round(averageAltitude * 1000) / 1000));
		map.put("Average Power", (double) (Math.round(averagePower * 1000) / 1000));

		return map;

	}
/**
 * calculate time of the point position
 * @param point
 * @return
 */
	public String getTime(int point) {
		int hour, minute, second;
		second = (point % 3600) % 60;
		minute = ((point % 3600) - second) / 60;
		hour = (point - point % 3600) / 3600;
		String stringSecond, stringMinute, stringHour;
		// second
		if (second < 10)
			stringSecond = "0" + second;
		else
			stringSecond = String.valueOf(second);
		// minute
		if (minute < 10)
			stringMinute = "0" + minute;
		else
			stringMinute = String.valueOf(minute);
		// hour
		if (hour < 10)
			stringHour = "0" + hour;
		else
			stringHour = String.valueOf(hour);
		return stringHour + ":" + stringMinute + ":" + stringSecond;
	}
}
