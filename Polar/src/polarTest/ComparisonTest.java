package polarTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.junit.jupiter.api.Test;

import polar.Comparison;
import polar.ComparisonData;
import polar.Data;

class ComparisonTest {
	Data firstData=new Data();
	Data secondData=new Data();
	Comparison comparison=new Comparison();
	ComparisonData CD=new ComparisonData();
	private static String REGEX = "\\[(.*?)\\]";
	/**
	 * load data to test
	 * @throws IOException
	 */
	public ComparisonTest() throws IOException {
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
	 * test resetHeaderTable
	 * @throws IOException
	 */
	@Test
	public void resetHeaderTableTest() throws IOException {
		comparison.resetHeaderTable();
		assertEquals(0,CD.ComparisonHeaderModel.getRowCount());
	}
	/**
	 * test resetDifferentTable
	 * @throws IOException
	 */
	@Test
	public void resetDifferentTableTest() throws IOException {
		comparison.resetDifferentTable();
		assertEquals(0,CD.ComparisonDifferentModel.getRowCount());
	}
	/**
	 * test resetChunkModel
	 * @throws IOException
	 */
	@Test
	public void resetChunkModelTest() throws IOException {
		comparison.resetChunkModel();
		assertEquals(0,CD.ComparisonChunkModel.getRowCount());
	}
	/**
	 * test getChunkData
	 * @throws IOException
	 */
	@Test
	public void getChunkDataTest() throws IOException {
		int[] each=comparison.getChunkData(comparison.getFirstData(), 4);
		assertEquals(4,each.length);
		assertEquals(994,each[0]);
		assertEquals(1989,each[1]);
		assertEquals(2984,each[2]);
		assertEquals(3979,each[3]);
	}
	/**
	 * test dataSet
	 * @throws IOException
	 */
	@Test
	public void dataSetTest() throws IOException {
		XYDataset dataset = comparison.createDataset("Speed",comparison.getFirstData());
		assertEquals(1,dataset.getSeriesCount());
	}
	/**
	 * test chart
	 * @throws IOException
	 */
	@Test
	public void chartTest() throws IOException {
		JFreeChart chart=comparison.chart(comparison.getFirstData(),"Polar");
		assertEquals("Polar",chart.getTitle().getText());
	}
}
