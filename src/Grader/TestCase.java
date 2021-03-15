package Grader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.json.JSONObject;

import mars.ErrorList;
import mars.MipsProgram;
import mars.ProcessingException;
import mars.mips.hardware.RegisterFile;

public class TestCase {
	public static final String POINT_VALUE_JSON_KEY = "PointValue";
	public static final String INPUT_REGISTERS_JSON_KEY = "InputRegisters";
	public static final String SOLUTION_REGISTERS_JSON_KEY = "SolutionRegisters";
	
	//TODO Replace with JSONObjects?
	
	// When the program runs, initilize these registers to these values
	private HashMap<String, Integer> inputRegisterValues;
	
	// Write down what the value is for each *tracked* register generated from the solution code 
	private HashMap<String, Integer> solutionRegisterValues;
	
	// When we run the solution code write down the value of each of these registers. 
	// This is what the student's code will be compared with
	private HashSet<String> trackedRegisters;
	
	private int pointValue;
	
	/**
	 * Creates a test case that is effectivelly empty.
	 *  
	 * What to do: 
	 * 	- Populate the initial register values
	 *  - Populate the tracked registers
	 *  - Generate the solution conditions (based on the tracked registers and the input to the code)
	 *  - Generate JSON object for this test case (if needed to write to file)
	 */
	public TestCase() {
		this.inputRegisterValues = new HashMap<String, Integer>();
		this.solutionRegisterValues = new HashMap<String, Integer>();
		
		this.trackedRegisters = new HashSet<String>();
		
		this.pointValue = 0;
	}
	
	/**
	 * Creates the test case as read from this JSONObject.
	 * Used for reading test cases from a file
	 *  
	 * @param jsonObject -> the JSONObject to read from
	 */
	public TestCase(JSONObject jsonObject) {
		this.inputRegisterValues = new HashMap<String, Integer>();
		this.solutionRegisterValues = new HashMap<String, Integer>();
		
		this.pointValue = jsonObject.getInt(POINT_VALUE_JSON_KEY);
		
		JSONObject inputRegistersObj = jsonObject.getJSONObject(INPUT_REGISTERS_JSON_KEY);
		for(String key : inputRegistersObj.keySet())
			inputRegisterValues.put(key, inputRegistersObj.getInt(key));
		
		JSONObject solutionRegisterObj = jsonObject.getJSONObject(SOLUTION_REGISTERS_JSON_KEY);
		for(String key : solutionRegisterObj.keySet()) {
			solutionRegisterValues.put(key, solutionRegisterObj.getInt(key));
			trackedRegisters.add(key);
		}
	}
	
	/**
	 * Generate a JSON object for this test case.
	 * This includes: 
	 *  - The point value
	 *  - The input values for the registers
	 *  - The register values from running the solution code (compute these before calling this function)
	 * 
	 * NOTE:
	 * 	- Generate the solution conditions before this!
	 * 
	 * USE:
	 * 	- Writing to a file mainly
	 * 
	 * @return -> A JSONObject with the above information
	 */
	public JSONObject generateJSONObject() {
		JSONObject obj = new JSONObject();
		
		obj.put(POINT_VALUE_JSON_KEY, pointValue);
		
		JSONObject inputRegisters = new JSONObject();
		for(Entry<String, Integer> e : inputRegisterValues.entrySet())
			inputRegisters.put(e.getKey(), e.getValue());
		
		obj.put(INPUT_REGISTERS_JSON_KEY, inputRegisters);
		
		JSONObject registersToCheck = new JSONObject();
		for(Entry<String, Integer> e : solutionRegisterValues.entrySet())
			registersToCheck.put(e.getKey(), e.getValue());
		
		obj.put(SOLUTION_REGISTERS_JSON_KEY, registersToCheck);
		
		return obj;
	}
	
	/**
	 * Take the file path to some code, assemble it, and run it.
	 * This does no assertions, it just runs the code.
	 * 
	 * TODO:
	 * 	- Error handling
	 * 	- Number of steps?
	 * 	- Start from main?
	 * 
	 * NOTE:
	 * 	- This will initialize the registers to their required values
	 * 
	 * USE:
	 * 	- Anytime code just needs to be ran, this handles getting it done
	 * 
	 * @param sourceFilePath
	 */
	private void runCode(String sourceFilePath) {
		MipsProgram program = new MipsProgram();
		
		try {
			program.readSource(sourceFilePath);
			program.tokenize();
			
			ArrayList<MipsProgram> list = new ArrayList<>();
			list.add(program);
			
			ErrorList warnings = program.assemble(list, true);
	        if (warnings != null && warnings.warningsOccurred()) {
	            System.out.println(warnings.generateWarningReport());
	        }
	        
	        for(Entry<String, Integer> e : inputRegisterValues.entrySet())
	        	RegisterFile.updateRegister(e.getKey(), e.getValue());
	        
	        RegisterFile.initializeProgramCounter(false);
	        
	        program.simulate(10);
		} catch (ProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public void generateSolutionConditions(String solutionCodeFilePath) {
		runCode(solutionCodeFilePath);
		
		solutionRegisterValues.clear();
		for(String registerName : trackedRegisters)
			solutionRegisterValues.put(registerName, RegisterFile.getUserRegister(registerName).getValue());
	}
	
	public int testStudentCode(String studentCodeFilePath) {
		runCode(studentCodeFilePath);
		
		for(Entry<String, Integer> e : solutionRegisterValues.entrySet()) {
			if(RegisterFile.getUserRegister(e.getKey()).getValue() != e.getValue()) {
				return 0;
			}
		}
		
		return pointValue;
	}

	public HashSet<String> getTrackedRegisters() { return trackedRegisters; }
	public HashMap<String, Integer> getInputRegisterValues() { return inputRegisterValues; }
	
	public int getPointValue() { return pointValue; }
	public void setPointValue(int pointValue) { this.pointValue = pointValue; }
}