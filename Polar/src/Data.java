import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;

public class Data {
	DefaultTableModel model =new DefaultTableModel();
	DefaultTableModel dataModel =new DefaultTableModel();
	DefaultTableModel summaryModel =new DefaultTableModel();
    HashMap<Integer,String> allMap = new HashMap<Integer,String>();
    HashMap<String,Integer> headerMap=new HashMap<String,Integer>();
    private static String REGEX = "\\[(.*?)\\]";
    Data(){
		
	}
	/*
	 * a method which add data to table
	 */
	public void tableData() {
		//header table date
        Object []row=new Object[3];
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
	}
	/*
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
        for(int i=0;i<length;i++) {
        	row1[i]=spl[0][i].split("\t");
        	row2[i]=spl[1][i].split("\t");
        	row3[i]=spl[2][i].split("\t");
        	row4[i]=spl[3][i].split("\t");
        	row5[i]=spl[4][i].split("\t");
        	sumHeart+=Integer.valueOf(row1[i][3]);
        	sumPower+=Integer.valueOf(row4[i][2]);
        	if(maxHeart<Integer.valueOf(row1[i][4])) {
        		maxHeart=Integer.valueOf(row1[i][4]);
        	}
        	if(minHeart>Integer.valueOf(row1[i][2])) {
        		minHeart=Integer.valueOf(row1[i][2]);
        	}
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
}
