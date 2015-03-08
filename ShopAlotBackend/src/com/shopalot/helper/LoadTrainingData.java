package com.shopalot.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class LoadTrainingData {
	public static Map<Integer[],String> getDataSet(String filename){
		
			Map<Integer[],String> map = new HashMap<Integer[],String>();
			try {
				CSVReader csvReader = new CSVReader(new FileReader(new File(filename)));
				List<String[]> data = csvReader.readAll();
				for (int i = 0; i < data.size(); i++) {
					Integer[] singals= new Integer[5];
					for (int j = 0; j < singals.length; j++) {
						singals[j]=Integer.parseInt(data.get(i)[j]);
					}
					map.put(singals, data.get(i)[5]);//location name
				}
				csvReader.close();
				return map;
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}
}
