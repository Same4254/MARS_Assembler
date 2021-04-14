package Grader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONTokener;

public class JSONFileIO {
	public static void writeJSONArray(String filePath, JSONArray arr) {
		writeJSONArray(new File(filePath), arr);
	}
	
	public static void writeJSONArray(File file, JSONArray arr) {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(arr.toString(4));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONArray readJSONArray(File file) {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		JSONTokener tokener = new JSONTokener(fr);
		JSONArray obj = new JSONArray(tokener);
		
		try {
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public static JSONArray readJSONObject(String filePath) {
		File file = new File(filePath);
		return readJSONArray(file);
	}
}
