import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;

public class Data {
	DefaultTableModel model =new DefaultTableModel();
	DefaultTableModel dataModel =new DefaultTableModel();
    HashMap<Integer,String> allMap = new HashMap<Integer,String>();
    HashMap<String,Integer> headerMap=new HashMap<String,Integer>();
    private static String REGEX = "\\[(.*?)\\]";
    Data(){
		
	}
	/*
	 * a method which add data to table
	 */
	public void tableData() {
		//table date
        Object []row=new Object[3];
        String []date=getParams().get("Date").split("");
        String date1=date[6]+date[7]+"/"+date[4]+date[5]+"/"+date[0]+date[1]+date[2]+date[3];
        row[0]=date1;
        row[1]=getParams().get("StartTime");
        row[2]=getParams().get("Interval");
        model.addRow(row);
        //table body data
        String [][]spl=getIntTimes();
        for(int i=0;i<spl[0].length;i++) {
        	String []row1=spl[0][i].split("\t");
        	String []row2=spl[1][i].split("\t");
        	String []row3=spl[2][i].split("\t");
        	String []row4=spl[3][i].split("\t");
        	String []row5=spl[4][i].split("\t");
	        Object []dataRow=new Object[6];
	        float Speed=Integer.valueOf(row2[3]);
	        dataRow[0]=row1[0];
	        dataRow[1]=(Speed/128)*1.609;
	        dataRow[2]=row2[4];
	        dataRow[3]=row2[5];
	        dataRow[4]=row1[1];
	        dataRow[5]=row4[2];
	        dataModel.addRow(dataRow);
        }
        
	}
	/*
	 * a method which add data to table
	 */
	public void tableData(boolean speed) {
        if(speed) {
	        //table body data
	        String [][]spl=getIntTimes();
	        for(int i=0;i<spl[0].length;i++) {
	        	String []row1=spl[0][i].split("\t");
	        	String []row2=spl[1][i].split("\t");
	        	String []row3=spl[2][i].split("\t");
	        	String []row4=spl[3][i].split("\t");
	        	String []row5=spl[4][i].split("\t");
		        Object []dataRow=new Object[6];
		        float Speed=Integer.valueOf(row2[3]);
		        dataRow[0]=row1[0];
		        dataRow[1]=(Speed/128)*1.609;
		        dataRow[2]=row2[4];
		        dataRow[3]=row2[5];
		        dataRow[4]=row1[1];
		        dataRow[5]=row4[2];
		        dataModel.addRow(dataRow);
	        }
        }else {
        	//table body data
	        String [][]spl=getIntTimes();
	        for(int i=0;i<spl[0].length;i++) {
	        	String []row1=spl[0][i].split("\t");
	        	String []row2=spl[1][i].split("\t");
	        	String []row3=spl[2][i].split("\t");
	        	String []row4=spl[3][i].split("\t");
	        	String []row5=spl[4][i].split("\t");
		        Object []dataRow=new Object[6];
		        float Speed=Integer.valueOf(row2[3]);
		        dataRow[0]=row1[0];
		        dataRow[1]=Speed/128;
		        dataRow[2]=row2[4];
		        dataRow[3]=row2[5];
		        dataRow[4]=row1[1];
		        dataRow[5]=row4[2];
		        dataModel.addRow(dataRow);
	        }
        }

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
