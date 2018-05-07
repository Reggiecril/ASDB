package PolarTest;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.data.time.TimeSeries;
import org.junit.jupiter.api.Test;

import Polar.Data;
import Polar.Polar;
class DataUnitTest {
	public DataUnitTest() throws IOException {
		Data data=new Data();
		file(data);
	}
	private static String REGEX = "\\[(.*?)\\]";
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
	@Test
	public void TimeTest() throws IOException {
		Data data=new Data();
		file(data);
		int Time=data.getTime();
		assertEquals(3979,Time);
	}
	@Test
	public void IFTest() {
		Data data=new Data();
		double IF=data.getIF(300, 300);
		assertEquals(1,IF);
	}
	@Test
	public void TSSTest() {
		Data data=new Data();
		int TSS=data.getTSS(3000, 300,1,300);
		assertEquals(83,TSS);
	}
	@Test
	public void ChartTest() throws IOException {
		Data data=new Data();
		file(data);
		HashMap<String, TimeSeries> map = new HashMap<String, TimeSeries>();
		map=data.getChartData();
		 // Test size
		assertEquals(5,map.size());
		
		assertEquals(false,map.isEmpty());
	}
	@Test
	public void PowerBalanceTest() throws IOException {
		Data data=new Data();
		file(data);
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

}
