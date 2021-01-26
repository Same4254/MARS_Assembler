package mars.assembler; 

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

/**
 * Represents a MIPS program identifier to be stored in the symbol table.
 */
public class Symbol {

    public static final boolean TEXT_SYMBOL = false;
    public static final boolean DATA_SYMBOL = true;

    private final String name;
    private final boolean type;
    private int address;

    /**
     * @param name The name of the Symbol.
     * @param address The memory address that the Symbol refers to.
     * @param type The type of Symbol.
     */
    public Symbol(String name, int address, boolean type) {
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    /**
     * @return true if it's a DATA_SYMBOL, false if it's a TEXT_SYMBOL
     */
    public boolean getType() {
        return this.type;
    }

    public int getAddress() {
        return this.address;
    }

    /**
     * Sets/replaces the address of the the Symbol.
     *
     * @param newAddress The revised address of the Symbol.
     */
    public void setAddress(int newAddress) {
        this.address = newAddress;
    }

}