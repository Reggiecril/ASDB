package polarTest;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.data.time.TimeSeries;
import org.junit.jupiter.api.Test;

import polar.Data;
import polar.Polar;
public class DataUnitTest {
	//create a Data Object
	Data data=new Data();
	private static String REGEX = "\\[(.*?)\\]";
	/**
	 * 
	 * @throws IOException
	 */
	public DataUnitTest() throws IOException {
		//load file data
		file(data);
	}
	/**
	 * load test data from a file
	 * @param data
	 * @throws IOException
	 */
	public void file(Data data) throws IOException {
		File file = new File("/home/reggie/文档/ASDBExampleCycleComputerData.hrm");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String aline;
        int i=1;
        //load file data to TextArea
        while ((aline=br.readLine()) != null){
        	//collect date to HashMap
        	data.allMap.put(i, aline);
        	//collect header information
        	Pattern p=Pattern.compile(REGEX);
        	Matcher m=p.matcher(aline);
        	if(m.find()) {
        		//record the line number of each line.
        		data.headerMap.put(m.group(1),i);
        	}
        	i++;
        }
        fr.close();
        br.close();
	}
	/**
	 * test tableData
	 * @throws IOException
	 */
	@Test
	public void tableDataTest() throws IOException {
		data.tableData();
		assertEquals(8,data.dataModel.getRowCount());
		assertEquals("00:09:59.0",data.dataModel.getValueAt(0, 0));
		assertEquals("41",data.dataModel.getValueAt(0, 2));
	}
	/**
	 * test tableData(boolean)
	 * @throws IOException
	 */
	@Test
	public void tableDataBoolTest() throws IOException {
		data.tableData(false);
		assertEquals(8,data.dataModel.getRowCount());
		assertEquals("00:09:59.0",data.dataModel.getValueAt(0, 0));
		assertEquals("41",data.dataModel.getValueAt(0, 2));
	}
	/**
	 * test summaryData
	 * @throws IOException
	 */
	@Test
	public void summaryDataTest() throws IOException {
		data.summaryDate(true);
		assertEquals(1,data.summaryModel.getRowCount());
		assertEquals("25.0",data.summaryModel.getValueAt(0, 0));
		assertEquals(168,data.summaryModel.getValueAt(0, 6));
	}
	/**
	 * test summaryData(int,int)
	 * @throws IOException
	 */
	@Test
	public void summaryDataNumberTest() throws IOException {
		int[] i={0,1000,2000,3000,3500};
		for(int x=0;x<i.length;x++) {
			if(x!=0)
				data.summaryDate(i[x-1], i[x]);
		}
		assertEquals(4,data.summaryModel.getRowCount());
	}
	/**
	 * test chunkData
	 * @throws IOException
	 */
	@Test
	public void chunkDataTest() throws IOException {
		int[] i={0,1000,2000,3000};
		for(int x=0;x<i.length;x++) {
			if(x!=0)
				data.chunkData(i[x-1], i[x]);
		}
		assertEquals(3,data.chunkModel.getRowCount());
	}
	/**
	 * test HRData
	 * @throws IOException
	 */
	@Test
	public void HRDataTest() throws IOException {
		HashMap<String, double[]> map = new HashMap<String, double[]>();
		map=data.getHRData();
		assertEquals(6,map.size());
		assertEquals(3979,map.get("Heart").length);
	}
	/**
	 * test HRData(int,int)
	 * @throws IOException
	 */
	@Test
	public void HRDataNumberTest() throws IOException {
		HashMap<String, double[]> map = new HashMap<String, double[]>();
		map=data.getHRData(1000,3979);
		assertEquals(6,map.size());
		assertEquals(2979,map.get("Heart").length);
	}
	/**
	 * test IntTimes
	 * @throws IOException
	 */
	@Test
	public void IntTimesTest() throws IOException {
		String[][] intTime=data.getIntTimes();
		assertEquals(5,intTime.length);
	}
	/**
	 * test Trip
	 * @throws IOException
	 */
	@Test
	public void TripTest() throws IOException {
		HashMap<String, String> map = new HashMap<String, String>();
		map=data.getTrip();
		assertEquals(8,map.size());
		assertEquals("250",map.get("Distance"));
		assertEquals("0",map.get("Ascent"));
		assertEquals("3978",map.get("TotalTime"));
		assertEquals("309",map.get("AverageAltitude"));
	}
	/**
	 * test Params
	 * @throws IOException
	 */
	@Test
	public void ParamsTest() throws IOException {
		HashMap<String, String> map = new HashMap<String, String>();
		map=data.getParams();
		assertEquals(22,map.size());
		assertEquals("106",map.get("Version"));
		assertEquals("111111100",map.get("SMode"));
		assertEquals("01:06:18.9",map.get("Length"));
		assertEquals("15:46:20.0",map.get("StartTime"));
		assertEquals("0",map.get("StartDelay"));
	}
	/**
	 * test getHeaderData
	 * @throws IOException
	 */
	@Test
	public void HeaderDataTest() throws IOException {
		assertEquals(3979,data.getHeaderData("HRData").length);
		assertEquals(22,data.getHeaderData("Params").length);
	}
	/**
	 * test count
	 * @throws IOException
	 */
	@Test
	public void countTest() throws IOException {
		assertEquals(3979,data.count("HRData"));
		assertEquals(22,data.count("Params"));
	}
	/**
	 * test existPowerBalance
	 * @throws IOException
	 */
	@Test
	public void ifExistTest() throws IOException {
		assertEquals(true,data.ifExist("HRData"));
		assertEquals(false,data.ifExist("HRData1"));
		assertEquals(true,data.ifExist("Trip"));
	}
	/**
	 * test existPowerBalance
	 * @throws IOException
	 */
	@Test
	public void existPowerBalanceTest() throws IOException {
		boolean existPowerBalance=data.existPowerBalance();
		assertEquals(true,existPowerBalance);
	}
	/**
	 * test FTP
	 * @throws IOException
	 */
	@Test
	public void FTPTest() throws IOException {
		double FTP=data.getFTP();
		assertEquals(301,FTP);
	}
	/**
	 * test map in powerBalance
	 * @throws IOException
	 */
	@Test
	public void PowerBalanceTest() throws IOException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map=data.getPowerBalance(data.getHRData().get("PowerBalance"));
		 // Test size
		assertEquals(3,map.size());
		//Test isEmpty
		assertEquals(false,map.isEmpty());
		//Test value
		assertEquals(null,map.get("size"));
		assertEquals(15,map.get("PI"),1);
		assertEquals(52,map.get("LPB"),1);
		assertEquals(48,map.get("RPB"),1);
	}
	/**
	 * test NP
	 */
	@Test
	public void NPTest() {
		double NP=data.getNP(data.getHRData().get("Power"));
		assertEquals(232,NP);
	}
	/**
	 * test IF
	 */
	@Test
	public void IFTest() {
		double IF=data.getIF(300, 300);
		assertEquals(1,IF);
	}
	/**
	 * test TSS
	 */
	@Test
	public void TSSTest() {
		int TSS=data.getTSS(3000, 300,1,300);
		assertEquals(83,TSS);
	}
	
	/**
	 * test size of chart and isEmpty
	 * @throws IOException
	 */
	@Test
	public void ChartTest() throws IOException {
		HashMap<String, TimeSeries> map = new HashMap<String, TimeSeries>();
		map=data.getChartData();
		 // Test size
		assertEquals(5,map.size());
		
		assertEquals(false,map.isEmpty());
	}
	/**
	 * test correct of time
	 * @throws IOException
	 */
	@Test
	public void TimeTest() throws IOException {
		int Time=data.getTime();
		assertEquals(3979,Time);
	}
}
