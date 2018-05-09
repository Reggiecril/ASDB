package polar;

import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

public class ComparisonData {
	public DefaultTableModel ComparisonHeaderModel = new DefaultTableModel();
	public DefaultTableModel ComparisonDifferentModel = new DefaultTableModel();
	public DefaultTableModel ComparisonChunkModel = new DefaultTableModel();

	/**
	 * a method which add data to Comparison Header table
	 */
	public void getHeaderData(int point) {
		// header table date
		String[] column= {"Time"};
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
		Object[] row = new Object[9];
		String[] columns = { "", "Heart", "Speed", "Cadence", "Altitude", "Power", "PI", "LPB", "RPB" };
		ComparisonDifferentModel.setColumnIdentifiers(columns);
		HashMap<String, Double> map = getPointData(point, data);
		row[0] = title;
		row[1] = map.get("Heart");
		row[2] = map.get("Speed");
		row[3] = map.get("Cadence");
		row[4] = map.get("Altitude");
		row[5] = map.get("Power");
		row[6] = map.get("PI");
		row[7] = map.get("LPB");
		row[8] = map.get("RPB");
		ComparisonDifferentModel.addRow(row);

	}

	/**
	 * a method which add data to table
	 */
	public void calculateDifferentData(int point, String title, Data firstData, Data secondData) {

		HashMap<String, Double> firstMap = getPointData(point, firstData);
		HashMap<String, Double> secondMap = getPointData(point, secondData);
		// calculate different
		double heart, speed, cadence, altitude, power, PI, LPB, RPB;
		heart = firstMap.get("Heart") - secondMap.get("Heart");
		speed = firstMap.get("Speed") - secondMap.get("Speed");
		cadence = firstMap.get("Cadence") - secondMap.get("Cadence");
		altitude = firstMap.get("Altitude") - secondMap.get("Altitude");
		power = firstMap.get("Power") - secondMap.get("Power");
		PI = firstMap.get("PI") - secondMap.get("PI");
		LPB = firstMap.get("LPB") - secondMap.get("LPB");
		RPB = firstMap.get("RPB") - secondMap.get("RPB");

		// header table date
		Object[] row = new Object[9];
		String[] columns = { "", "Heart", "Speed", "Cadence", "Altitude", "Power", "PI", "LPB", "RPB" };
		ComparisonDifferentModel.setColumnIdentifiers(columns);

		row[0] = title;
		row[1] = heart;
		row[2] = speed;
		row[3] = cadence;
		row[4] = altitude;
		row[5] = power;
		row[6] = PI;
		row[7] = LPB;
		row[8] = RPB;
		ComparisonDifferentModel.addRow(row);

	}

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
