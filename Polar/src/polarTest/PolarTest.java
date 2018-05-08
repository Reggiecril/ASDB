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

import polar.Data;
import polar.Polar;

public class PolarTest {

	//create a Data Object
		Data data=new Data();
		Polar polar=new Polar();
		private static String REGEX = "\\[(.*?)\\]";
		/**
		 * 
		 * @throws IOException
		 */
		public PolarTest() throws IOException {
			//load file data
			file(data);
			polar.setData(data);
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
		 * test resetBodyTable
		 * @throws IOException
		 */
		@Test
		public void resetBodyTableTest() throws IOException {
			polar.resetBodyTable();
			assertEquals(0,data.dataModel.getRowCount());
		}
		/**
		 * test resetBodyTable
		 * @throws IOException
		 */
		@Test
		public void resetSummaryTable() throws IOException {
			polar.resetSummaryTable();
			assertEquals(0,data.summaryModel.getRowCount());
		}
		/**
		 * test resetBodyTable
		 * @throws IOException
		 */
		@Test
		public void resetTable() throws IOException {
			polar.resetTable();
			assertEquals(0,data.model.getRowCount());
		}
		/**
		 * test resetBodyTable
		 * @throws IOException
		 */
		@Test
		public void resetChunkTable() throws IOException {
			polar.resetChunkTable();
			assertEquals(0,data.chunkModel.getRowCount());
		}
		/**
		 * test getChunkData
		 * @throws IOException
		 */
		@Test
		public void getChunkDataTest() throws IOException {
			int[] each=polar.getChunkData(200, 3000, 4);
			assertEquals(4,each.length);
			assertEquals(900,each[0]);
			assertEquals(1600,each[1]);
			assertEquals(2300,each[2]);
			assertEquals(3000,each[3]);
		}
		/**
		 * test dataSet
		 * @throws IOException
		 */
		@Test
		public void dataSetTest() throws IOException {
			XYDataset dataset = polar.createDataset("Speed");
			assertEquals(1,dataset.getSeriesCount());
		}
		/**
		 * test chart
		 * @throws IOException
		 */
		@Test
		public void chartTest() throws IOException {
			JFreeChart chart=polar.chart();
			assertEquals("Polar",chart.getTitle().getText());
		}
		/**
		 * test getData
		 * @throws IOException
		 */
		@Test
		public void getDataTest() throws IOException {
			
			assertEquals(301,polar.getData().getFTP());
		}
}
