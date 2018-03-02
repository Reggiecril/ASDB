import java.text.DecimalFormat;
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
	DefaultTableModel model =new DefaultTableModel();
	DefaultTableModel dataModel =new DefaultTableModel();
	DefaultTableModel summaryModel =new DefaultTableModel();
    HashMap<Integer,String> allMap = new HashMap<Integer,String>();
    HashMap<String,Integer> headerMap=new HashMap<String,Integer>();
    private static String REGEX = "\\[(.*?)\\]";
    Data(){
		
	}
	/**
	 * a method which add data to table
	 */
	public void tableData() {
		//header table date
        Object []row=new Object[3];
		String[] columns= {"Date","Start Time","Interval"};
		model.setColumnIdentifiers(columns);
        String []date=getParams().get("Date").split("");
        String date1=date[6]+date[7]+"/"+date[4]+date[5]+"/"+date[0]+date[1]+date[2]+date[3];
        row[0]=date1;
        row[1]=getParams().get("StartTime");
        row[2]=getParams().get("Interval");
        model.addRow(row);
        //table body data
        String [][]spl=getIntTimes();
		String[] columns1= {"Time","Speed(km/h)","Cadence(rpm)","Altitude","Heart rate","Heart Rate(MAX)","Power in watts"};
		dataModel.setColumnIdentifiers(columns1);
        for(int i=0;i<spl[0].length;i++) {
        	String []row1=spl[0][i].split("\t");
        	String []row2=spl[1][i].split("\t");
        	String []row3=spl[2][i].split("\t");
        	String []row4=spl[3][i].split("\t");
        	String []row5=spl[4][i].split("\t");
	        Object []dataRow=new Object[7];
	        float Speed=Integer.valueOf(row2[3]);
	        dataRow[0]=row1[0];
	        dataRow[1]=(Speed/128)*1.609;
	        dataRow[2]=row2[4];
	        dataRow[3]=row2[5];
	        dataRow[4]=row1[1];
	        dataRow[5]=row1[4];
	        dataRow[6]=row4[2];
	        dataModel.addRow(dataRow);
        }
        summaryDate(true);
		//create chart panel
       
		
	}
	
	/**
	 * re-write tableDate();
	 * this is only for table of body data.
	 * display data by different speed.
	 */
	public void tableData(boolean speed) {
		
        if(speed) {
	        //table body data
    		String[] columns1= {"Time","Speed(KM/H)","Cadence(RPM)","Altitude","Heart Rate","Heart Rate(MAX)","Power In Watts"};
    		dataModel.setColumnIdentifiers(columns1);
	        String [][]spl=getIntTimes();
	        for(int i=0;i<spl[0].length;i++) {
	        	String []row1=spl[0][i].split("\t");
	        	String []row2=spl[1][i].split("\t");
	        	String []row3=spl[2][i].split("\t");
	        	String []row4=spl[3][i].split("\t");
	        	String []row5=spl[4][i].split("\t");
		        Object []dataRow=new Object[7];
		        float Speed=Integer.valueOf(row2[3]);
		        dataRow[0]=row1[0];
		        dataRow[1]=(Speed/128)*1.609;
		        dataRow[2]=row2[4];
		        dataRow[3]=row2[5];
		        dataRow[4]=row1[1];
		        dataRow[5]=row1[4];
		        dataRow[6]=row4[2];
		        dataModel.addRow(dataRow);
	        }
        }else {
        	//table body data
    		String[] columns1= {"Time","Speed(mph)","Cadence(rpm)","Altitude","Heart rate","Power in watts"};
    		dataModel.setColumnIdentifiers(columns1);
	        String [][]spl=getIntTimes();
	        for(int i=0;i<spl[0].length;i++) {
	        	String []row1=spl[0][i].split("\t");
	        	String []row2=spl[1][i].split("\t");
	        	String []row3=spl[2][i].split("\t");
	        	String []row4=spl[3][i].split("\t");
	        	String []row5=spl[4][i].split("\t");
		        Object []dataRow=new Object[7];
		        float Speed=Integer.valueOf(row2[3]);
		        dataRow[0]=row1[0];
		        dataRow[1]=Speed/128;
		        dataRow[2]=row2[4];
		        dataRow[3]=row2[5];
		        dataRow[4]=row1[1];
		        dataRow[5]=row1[4];
		        dataRow[6]=row4[2];
		        dataModel.addRow(dataRow);
	        }
        }

	}
	/**
	 * get data of summary table
	 * @param speed
	 */
	public void summaryDate(boolean speed) {
        //summary body data
        String [][]spl=getIntTimes();
        if(speed) {
        	String[] columns1= {"Total distance covered","Average speed(KM/H)","Maximum speed(KM/H)","Average heart rate","Maximum heart rate","Minimum heart rate","Average power","Maximum power","Average altitude","Maximum altitude"};
        	summaryModel.setColumnIdentifiers(columns1);
        }else {
        	String[] columns1= {"Total distance covered","Average speed(MPH)","Maximum speed(MPH)","Average heart rate","Maximum heart rate","Minimum heart rate","Average power","Maximum power","Average altitude","Maximum altitude"};
        	summaryModel.setColumnIdentifiers(columns1);
        }
        //Initialize
        Object []dataRow=new Object[10];
        int sumHeart=0;
        int sumPower=0;
        int length=spl[0].length;
        int maxHeart=0;
        int minHeart=200;
        int maxPower=0;
        String [][]row1=new String[length][5];
        String [][]row2=new String[length][6];
        String [][]row3=new String[length][3];
        String [][]row4=new String[length][6];
        String [][]row5=new String[length][2];
        //loop to calculate 
        for(int i=0;i<length;i++) {
        	row1[i]=spl[0][i].split("\t");
        	row2[i]=spl[1][i].split("\t");
        	row3[i]=spl[2][i].split("\t");
        	row4[i]=spl[3][i].split("\t");
        	row5[i]=spl[4][i].split("\t");
        	sumHeart+=Integer.valueOf(row1[i][3]);
        	sumPower+=Integer.valueOf(row4[i][2]);
        	//loop to select a max heart rate
        	if(maxHeart<Integer.valueOf(row1[i][4])) {
        		maxHeart=Integer.valueOf(row1[i][4]);
        	}
        	//loop to select a min heart rate
        	if(minHeart>Integer.valueOf(row1[i][2])) {
        		minHeart=Integer.valueOf(row1[i][2]);
        	}        	
        	//loop to select a max power
        	if(maxPower<Integer.valueOf(row4[i][2])) {
        		maxPower=Integer.valueOf(row4[i][2]);
        	}
        }
    	
        //Total distance
        dataRow[0]=getTrip().get("Distance");
        //Initialize Average speed
        double averageSpeed=Integer.valueOf(getTrip().get("AverageSpeed"));
        //Initialize Maximum speed
        double maximumSpeed=Integer.valueOf(getTrip().get("MaximumSpeed"));
        if(speed) {
            //Average speed
            dataRow[1]=(averageSpeed/128)*1.609;
            //Maximum speed
            dataRow[2]=(maximumSpeed/128)*1.609;
        }else {
            //Average speed
            dataRow[1]=averageSpeed/128;
            //Maximum speed
            dataRow[2]=maximumSpeed/128;
        }
        //Average heart rate
        dataRow[3]=sumHeart/length;
        //Maximum heart rate
        dataRow[4]=maxHeart;
        //Minimum heart rate
        dataRow[5]=minHeart;
        //Average power
        dataRow[6]=sumPower/length;
        //Maximum power
        dataRow[7]=maxPower;
        //Average altitude
        dataRow[8]=getTrip().get("AverageAltitude");
        //Maximum altitude
        dataRow[9]=getTrip().get("MaximumAltitude");

        summaryModel.addRow(dataRow);
        
	}
	/**
	 * get the data of SMODE
	 * @return
	 */
	public HashMap<String,String> getSMODE(){
		HashMap<String,String> map=new HashMap<String,String>();
		//get the line of SMODE and spilt it
		String []smode=getParams().get("SMode").split("");
	
		//add data into speed
		if(smode[0].equals("0"))
			map.put("Speed", "off");
		else
			map.put("Speed", "on");
		//add data into cadence
		if(smode[1].equals("0"))
			map.put("Cadence", "off");
		else
			map.put("Cadence", "on");
		//add data into altitude
		if(smode[2].equals("0"))
			map.put("Altitude", "off");
		else
			map.put("Altitude", "on");
		//add data into Power
		if(smode[3].equals("0"))
			map.put("Power", "off");
		else
			map.put("Power", "on");
		//add data into Power Left Right Balance
		if(smode[4].equals("0"))
			map.put("Power Left Right Balance", "off");
		else
			map.put("Power Left Right Balance", "on");
		//add data into Power Pedaling Index
		if(smode[5].equals("0"))
			map.put("Power Pedalling Index", "off");
		else
			map.put("Power Pedalling Index", "on");
		//add data into HR/CC data
		if(smode[6].equals("0"))
			map.put("HR/CC data", "HR data only,");
		else
			map.put("HR/CC data", "HR + cycling data");
		//add data into US/Euro unit
		if(smode[7].equals("0"))
			map.put("US/Euro unit", "Euro");
		else
			map.put("US/Euro unit", "US");
		return map;
	}
	/**
	 * get the data from 'IntTimes',then named every elements.
	 * @return
	 */
	public String[][] getIntTimes(){
		String []header=getHeaderData("IntTimes");
		HashMap<String,String[]> map=new HashMap<String,String[]>();
		int j=0;
		String[][]spl=new String[5][count("IntTimes")/5];
		for(int i=0;i<count("IntTimes");i+=5) {
			spl[0][j]=header[i];
			spl[1][j]=header[i+1];
			spl[2][j]=header[i+2];
			spl[3][j]=header[i+3];
			spl[4][j]=header[i+4];
			j++;
		}
		return spl;
	}
	/**
	 * get the data from 'Trip', then named each number
	 * @return
	 */
	public HashMap<String,String> getTrip(){
		String []header=getHeaderData("Trip");
		HashMap<String,String> map=new HashMap<String,String>();
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
	 * @return
	 */
	public HashMap<String,String> getParams(){
		String []header=getHeaderData("Params");
		HashMap<String,String> map = new HashMap<String,String>();
		for(String line:header) {
			String lineData[]=line.split("=");
			map.put(lineData[0],lineData[1]);
		}
		
		return map;
	}
	
	/**
	 * get lines of a header
	 * @param header
	 * @return
	 */
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
	/**
	 * count the number of lines from the part of header.
	 * @param header
	 * @return
	 */
	public int count(String header) {
		//get the line number of header
		int j=(int)headerMap.get(header);
		int i=0;
		int count;
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
		if(header=="Trip")
			count=i-j+1;
		else
			count=i-j;
		return count;
	}
	
	
	
	
	///for chart
	///for chart
	///for chart
	/**
	 * add data to Chart
	 * @param speed
	 * @return
	 */
	private XYDataset createDataset() {
		//get data from header[IntTimes]
        String [][]spl=getIntTimes();
        
        //Initialize
        int length=spl[0].length;
        String [][]row1=new String[length][5];
        String [][]row2=new String[length][6];
        String [][]row3=new String[length][3];
        String [][]row4=new String[length][6];
        String [][]row5=new String[length][2];
        String []dataTime=new String[length];
        double []dataSpeed=new double[length];
        double []dataCadence=new double[length];
        double []dataAltitude=new double[length];
        double []dataHeart=new double[length];
        double []dataPower=new double[length];
        //Divide into 5 parts
        for(int i=0;i<length;i++) {
        	row1[i]=spl[0][i].split("\t");
        	row2[i]=spl[1][i].split("\t");
        	row3[i]=spl[2][i].split("\t");
        	row4[i]=spl[3][i].split("\t");
        	row5[i]=spl[4][i].split("\t");
        	dataTime[i]=row1[i][0];
        	dataSpeed[i]=Double.valueOf(row2[i][3])/128*1.609*10;
        	dataCadence[i]=Double.valueOf(row2[i][4]);
        	dataAltitude[i]=Double.valueOf(row2[i][5]);
        	dataHeart[i]=Double.valueOf(row1[i][1]);
        	dataPower[i]=Double.valueOf(row4[i][2]);
        }
        TimeSeriesCollection tsc= new TimeSeriesCollection();
        tsc.addSeries(getChartData("Speed(mph)",length,dataSpeed,dataTime));
        tsc.addSeries(getChartData("Cadence(rpm)",length,dataCadence,dataTime));
        tsc.addSeries(getChartData("Altitude(ft)",length,dataAltitude,dataTime));
        tsc.addSeries(getChartData("Heart(bpm)",length,dataHeart,dataTime));
        tsc.addSeries(getChartData("Power(W)",length,dataPower,dataTime));
       
        XYDataset dataset =tsc;
        
	    return dataset;
	  }
	//get series 
	public TimeSeries getChartData(String title,int length,double []itemData,String []itemTime){
		TimeSeries series = new TimeSeries(title);
		String []startTime=itemTime[0].split(":");
		int hour=Integer.valueOf(startTime[0]);
		int minute=Integer.valueOf(startTime[1]);
		double s=Double.valueOf(startTime[2]);
		int second=(int)s;
	    Second current = new Second(second,minute,hour,1,1,2018);
		//generate plot chart
        for(int j=0;j<length;j++) {
        	//only run before (length-1)
        	if(j==length-1) {
        		double nowTime=itemData[j];
        		series.add(current, new Double(nowTime)); 
        		break;
        	}else {
        		int timeDifference=getTime(itemTime[j+1])-getTime(itemTime[j]);
        		//calculate difference between now time and next time
	        	double Difference=itemData[j+1]-itemData[j];
	        	//get now time speed
	        	double nowTime=itemData[j];
	        	//when the difference is less than 0,display it decline slowly.
	        	//when the difference is more than 0, display it rise slowly.
	        	if(Difference<0) {
	        		//make Difference to positive
	        		Difference=0-Difference;
	        		//get index of rise or decline
	        		double value=Difference/timeDifference;
	        		for (int i = 0; i < timeDifference; i++) {
		 		    	try {
		 		    		nowTime = nowTime -value+(Math.random()-0.50)*5.0;
		 		    		if(nowTime<0) {
		 		    			nowTime=0;
		 		    		}
		 		    		series.add(current, new Double(nowTime) );                 
		 		            current = (Second) current.next(); 
		 		         } catch (SeriesException e) {
		 		            System.err.println("Error adding to series");
		 		         }
		        	 }
	        	}else {
	        		double con=Difference/timeDifference;
	        		for (int i = 0; i < timeDifference; i++) {
		 		    	try {
		 		    		nowTime = nowTime +con+ (Math.random()-0.50)*5.0;
		 		    		if(nowTime<0) {
		 		    			nowTime=0;
		 		    		}
		 		    		series.add(current, new Double(nowTime) );                 
		 		            current = ( Second ) current.next(); 
		 		         } catch (SeriesException e) {
		 		            System.err.println("Error adding to series");
		 		         }
		        	 }
	        	}
        	}
        }
        return series;
	}
	//calculate time to second and return to Integer
	public int getTime(String time) {
		int calculate=0;
		String []timeRow=time.split(":");
		double row2=Double.valueOf(timeRow[2]);
		calculate=Integer.valueOf(timeRow[0])*3600+Integer.valueOf(timeRow[1])*60+(int)row2;
		return calculate;
	}
	/**
	 * a whole chart to be returned
	 * @param speed
	 * @return
	 */
	public JFreeChart chart() {
		XYDataset dataset = createDataset();  
		
        // Create chart
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Polar", // Chart title
            "Time", // X-Axis Label
            "Number", // Y-Axis Label
            dataset,
            true,
            false,
            false
            );
       
		// Assign it to the chart
		XYPlot plot = (XYPlot) chart.getPlot();
		DateAxis rangeAxis = new DateAxis("Times");
        rangeAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 5));
        rangeAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
        plot.setDomainAxis(rangeAxis);
        
        return chart;
	}
}
