package Grader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;

public class TestCaseCollectionFile {
	private File jsonFile;
	private ArrayList<TestCase> testCases;
	
	public TestCaseCollectionFile(File jsonFile) {
		this.jsonFile = jsonFile;
		
		testCases = new ArrayList<TestCase>();
		
		if(jsonFile.exists()) {
			JSONArray testCasesJSONArray = JSONFileIO.readJSONArray(jsonFile);
			for(int i = 0; i < testCasesJSONArray.length(); i++)
				testCases.add(new TestCase(this, testCasesJSONArray.getJSONObject(i)));
		} else {
			try {
				jsonFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void save() {
		JSONArray array = new JSONArray(testCases.size());
		
		for(int i = 0; i < testCases.size(); i++) {
			array.put(i, testCases.get(i).generateJSONObject());
		}
		
		JSONFileIO.writeJSONArray(jsonFile, array);
	}
	
	public ArrayList<TestCase> getTestCases() { return testCases; }
}
