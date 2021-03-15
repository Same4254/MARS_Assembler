package Grader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONFileIO {
	public static void writeJSONObject(String filePath, JSONObject obj) {
		try {
			FileWriter writer = new FileWriter(filePath);
			writer.write(obj.toString(4));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static JSONObject readJSONObject(String filePath) {
		File f = new File(filePath);
		FileReader fr = null;
		try {
			fr = new FileReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		JSONTokener tokener = new JSONTokener(fr);
		JSONObject obj = new JSONObject(tokener);
		
		try {
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
}
