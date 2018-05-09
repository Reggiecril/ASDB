package polarTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import polar.Comparison;
import polar.ComparisonData;
import polar.Data;

class ComparisonDataTest {
	Data firstData=new Data();
	Data secondData=new Data();
	Comparison comparison=new Comparison();
	ComparisonData CD=new ComparisonData();
	private static String REGEX = "\\[(.*?)\\]";
	public ComparisonDataTest() throws IOException {
		// load file data
		file(firstData);
		comparison.setFirstData(firstData);
		// load file data
		file(secondData);
		comparison.setSecondData(secondData);
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
	 * test getHeaderData
	 * @throws IOException
	 */
	@Test
	public void getHeaderDataTest() throws IOException {
		CD.getHeaderData(100);
		assertEquals(1,CD.ComparisonHeaderModel.getRowCount());
	}
	/**
	 * test defaultHeaderData
	 * @throws IOException
	 */
	@Test
	public void defaultHeaderDataTest() throws IOException {
		CD.defaultHeaderData();
		assertEquals(1,CD.ComparisonHeaderModel.getRowCount());
	}
	/**
	 * test calculateDifferentData
	 * @throws IOException
	 */
	@Test
	public void getDifferentDataTest() throws IOException {
		CD.getDifferentData(100,"title",comparison.getFirstData());
		assertEquals(1,CD.ComparisonDifferentModel.getRowCount());
	}
	/**
	 * test calculateDifferentData
	 * @throws IOException
	 */
	@Test
	public void calculateDifferentDataTest() throws IOException {
		CD.calculateDifferentData(100,"title",comparison.getFirstData(),comparison.getSecondData());
		assertEquals(1,CD.ComparisonDifferentModel.getRowCount());
	}
	/**
	 * test chunkDifferentData
	 * @throws IOException
	 */
	@Test
	public void chunkDifferentDataTest() throws IOException {
		CD.chunkDifferentData(comparison.getFirstData(),comparison.getSecondData(),"title",1,1000,2000,1000,2000);
		assertEquals(1,CD.ComparisonChunkModel.getRowCount());
	}
	/**
	 * test getChunkMap
	 * @throws IOException
	 */
	@Test
	public void getChunkMapTest() throws IOException {
		HashMap<String, Double> map=CD.getChunkMap(comparison.getFirstData(),0,1000);
		assertEquals(16,map.size());
		assertEquals(Double.valueOf("16.0"),map.get("Average Speed"));
		assertEquals(Double.valueOf("191.0"),map.get("Average Power"));
	}
	/**
	 * test getPointData
	 * @throws IOException
	 */
	@Test
	public void getPointDataTest() throws IOException {
		HashMap<String, Double> map=CD.getPointData(2339,comparison.getFirstData());
		assertEquals(8,map.size());
		assertEquals(Double.valueOf("127.0"),map.get("Heart"));
	}
	/**
	 * test getAverage
	 * @throws IOException
	 */
	@Test
	public void getAverageTest() throws IOException {
		HashMap<String, Double> map=CD.getAverage(3979,comparison.getFirstData());
		assertEquals(5,map.size());
		assertEquals(Double.valueOf("25.0"),map.get("Distance"));
	}
	/**
	 * test getTime
	 * @throws IOException
	 */
	@Test
	public void getTimeTest() throws IOException {
		String each=CD.getTime(3979);
		assertEquals("01:06:19",each);
	}
}
