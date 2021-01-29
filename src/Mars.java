import java.awt.Color;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.demo.DemoPrefs;

import mars.Globals;
import mars.Settings;

/*
Copyright (c) 2003-2006,  Pete Sanderson and Kenneth Vollmar

Developed by Pete Sanderson (psanderson@otterbein.edu)
and Kenneth Vollmar (kenvollmar@missouristate.edu)

Permission is hereby granted, free of charge, to any person obtaining 
a copy of this software and associated documentation files (the 
"Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, 
distribute, sublicense, and/or sell copies of the Software, and to 
permit persons to whom the Software is furnished to do so, subject 
to the following conditions:

The above copyright notice and this permission notice shall be 
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR 
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION 
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

(MIT license, http://www.opensource.org/licenses/mit-license.html)
 */

public class Mars {
    public static void main(String[] args) {
    	Globals.initialize(true);
    	
    	//Initialize to some state... The init functions below handle loading the previously selected theme
    	try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	
    	DemoPrefs.init("mars");
		DemoPrefs.initLaf(args);
    	
    	//These could work as the alternate row color
//    	UIManager.put("Table.alternateRowColor", UIManager.get("ToolBar.background"));
//    	UIManager.put("Table.alternateRowColor", UIManager.get("TabbedPane.light"));
//    	UIManager.put("Table.alternateRowColor", UIManager.get("TabbedPane.shadow"));
//    	UIManager.put("Table.alternateRowColor", UIManager.get("TabbedPane.underlineColor"));
//    	UIManager.put("Table.alternateRowColor", UIManager.get("Table.gridColor"));
    	
//    	UIManager.put("Table.alternateRowColor", UIManager.get("Table.background"));
//    	
//    	UIManager.put("Table.showCellFocusIndicator", false);
//    	
//    	UIManager.put("Table.showHorizontalLines", true);
//    	UIManager.put("Table.showVerticalLines", true);
    	
        new mars.MarsLaunch(args);
    }
}

