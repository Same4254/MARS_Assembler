package mars.venus;

import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;
import mars.*;
import mars.venus.editors.MARSTextEditingArea;
import mars.venus.editors.generic.GenericTextArea;
import mars.venus.editors.jeditsyntax.JEditBasedTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.undo.*;
import java.util.*;
import java.io.*;

/*
Copyright (c) 2003-2011,  Pete Sanderson and Kenneth Vollmar

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
 * Represents one file opened for editing.  Maintains required internal structures.
 * Before Mars 4.0, there was only one editor pane, a tab, and only one file could
 * be open at a time.  With 4.0 came the multifile (pane, tab) editor, and existing
 * duties were split between EditPane and the new EditTabbedPane class.
 *
 * @author Sanderson and Bumgarner
 */

public class EditPane extends JPanel implements Observer {

    RTextScrollPane scrollPane;
    RSyntaxTextArea textArea;

    private JLabel caretPositionLabel;
    private FileStatus fileStatus;
    private VenusUI mainUI;

    public EditPane(VenusUI appFrame) {
        super(new BorderLayout());
        mainUI = appFrame;

        // We want to be notified of editor font changes! See update() below.
        Globals.getSettings().addObserver(this);
        fileStatus = new FileStatus();

        initTextArea();
        initScrollPane();

        add(scrollPane, BorderLayout.CENTER);

        // If source code is modified, will set flag to trigger/request file save.
        // TODO: to implement with RSyntaxTextArea
//        textArea.getDocument().addDocumentListener(
//                new DocumentListener() {
//                    public void insertUpdate(DocumentEvent evt) {
//                        // IF statement added DPS 9-Aug-2011
//                        // This method is triggered when file contents added to document
//                        // upon opening, even though not edited by user.  The IF
//                        // statement will sense this situation and immediately return.
//                        if (FileStatus.get() == FileStatus.OPENING) {
//                            setFileStatus(FileStatus.NOT_EDITED);
//                            FileStatus.set(FileStatus.NOT_EDITED);
//                            return;
//                        }
//                        // End of 9-Aug-2011 modification.
//                        if (getFileStatus() == FileStatus.NEW_NOT_EDITED) {
//                            setFileStatus(FileStatus.NEW_EDITED);
//                        }
//                        if (getFileStatus() == FileStatus.NOT_EDITED) {
//                            setFileStatus(FileStatus.EDITED);
//                        }
//                        if (getFileStatus() == FileStatus.NEW_EDITED) {
//                            mainUI.editor.setTitle("", getFilename(), getFileStatus());
//                        } else {
//                            mainUI.editor.setTitle(getPathname(), getFilename(), getFileStatus());
//                        }
//
//                        FileStatus.setEdited(true);
//                        switch (FileStatus.get()) {
//                            case FileStatus.NEW_NOT_EDITED:
//                                FileStatus.set(FileStatus.NEW_EDITED);
//                                break;
//                            case FileStatus.NEW_EDITED:
//                                break;
//                            default:
//                                FileStatus.set(FileStatus.EDITED);
//                        }
//
//                        Globals.getGui().getMainPane().getExecutePane().clearPane(); // DPS 9-Aug-2011
//
//                    }
//
//                    public void removeUpdate(DocumentEvent evt) {
//                        this.insertUpdate(evt);
//                    }
//
//                    public void changedUpdate(DocumentEvent evt) {
//                        this.insertUpdate(evt);
//                    }
//                });

        setSourceCode("", false);

        JPanel editInfo = new JPanel(new BorderLayout());
        caretPositionLabel = new JLabel();
        caretPositionLabel.setToolTipText("Tracks the current position of the text editing cursor.");
        displayCaretPosition(new Point());
        editInfo.add(caretPositionLabel, BorderLayout.WEST);
        this.add(editInfo, BorderLayout.SOUTH);
    }

    private void initTextArea() {
        textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
        textArea.setFont(Globals.getSettings().getEditorFont());
        textArea.setCurrentLineHighlightColor(new Color(0xFFFAE3));
    }

    /**
     * Must be called after initTextArea()
     */
    private void initScrollPane() {
        scrollPane = new RTextScrollPane(textArea);
        scrollPane.getGutter().setLineNumberFont(Globals.getSettings().getEditorFont());
        scrollPane.getGutter().setLineNumberColor(new Color(0x999999));
        scrollPane.getGutter().setBackground(new Color(0xF0F0F0));
    }


    /**
     * For initializing the source code when opening an ASM file
     *
     * @param text        String containing text
     * @param editable set true if code is editable else false
     */
    public void setSourceCode(String text, boolean editable) {
        textArea.setText(text);
        textArea.setEditable(editable);
        textArea.setCaretPosition(0);
    }

    /**
     * Get rid of any accumulated undoable edits.  It is useful to call
     * this method after opening a file into the text area.  The
     * act of setting its text content upon reading the file will generate
     * an undoable edit.  Normally you don't want a freshly-opened file
     * to appear with its Undo action enabled.  But it will unless you
     * call this after setting the text.
     */
    public void discardAllUndoableEdits() {
        textArea.discardAllEdits();
    }



    /**
     * Calculate and return number of lines in source code text.
     */
    public int getSourceLineCount() {
        return textArea.getLineCount();
    }

    /**
     * Get source code text
     *
     * @return String containing source code
     */
    public String getSourceCode() {
        return textArea.getText();
    }


    /**
     * Set the editing status for this EditPane's associated document.
     * For the argument, use one of the constants from class FileStatus.
     *
     * @param fileStatus the status constant from class FileStatus
     */
    public void setFileStatus(int fileStatus) {
        this.fileStatus.setFileStatus(fileStatus);
    }


    /**
     * Get the editing status for this EditPane's associated document.
     * This will be one of the constants from class FileStatus.
     */
    public int getFileStatus() {
        return this.fileStatus.getFileStatus();
    }

    /**
     * Delegates to corresponding FileStatus method
     */
    public String getFilename() {
        return this.fileStatus.getFilename();
    }


    /**
     * Delegates to corresponding FileStatus method
     */
    public String getPathname() {
        return this.fileStatus.getPathname();
    }


    /**
     * Delegates to corresponding FileStatus method
     */
    public void setPathname(String pathname) {
        this.fileStatus.setPathname(pathname);
    }

    /**
     * Delegates to corresponding FileStatus method
     */
    public boolean hasUnsavedEdits() {
        return this.fileStatus.hasUnsavedEdits();
    }


    /**
     * Delegates to corresponding FileStatus method
     */
    public boolean isNew() {
        return this.fileStatus.isNew();
    }


    /**
     * Delegates to text area's requestFocusInWindow method.
     */
    public void tellEditingComponentToRequestFocusInWindow() {
        textArea.requestFocusInWindow();
    }


    /**
     * Delegates to corresponding FileStatus method
     */
    public void updateStaticFileStatus() {
        fileStatus.updateStaticFileStatus();
    }


    /**
     * get the manager in charge of Undo and Redo operations
     *
     * @return the UnDo manager
     */
    @Deprecated
    public UndoManager getUndoManager() {
        // TODO: cannot implement
        //return sourceCode.getUndoManager();
        return null;
    }

    //    TODO: implement
    //    Note: The following methods are invoked only when copy/cut/paste are
    //    used from the toolbar or menu or the defined menu Alt codes.
    //    When Ctrl-C, Ctrl-X or Ctrl-V are used, this code is NOT invoked but
    //    the operation works correctly!
    //    The "set visible" operations are used because clicking on the toolbar
    //    icon causes both the selection highlighting AND the blinking cursor
    //    to disappear! This does not happen when using menu selection or
    //    Ctrl-C/X/V

    /**
     * Copy selected text into clipboard
     */
    public void copyText() {
        textArea.copy();
        textArea.getCaret().setVisible(true);
    }

    /**
     * cut currently-selected text into clipboard
     */
    public void cutText() {
        textArea.cut();
        textArea.getCaret().setVisible(true);
    }

    /**
     * paste clipboard contents at cursor position
     */
    public void pasteText() {
        textArea.paste();
        textArea.getCaret().setVisible(true);
    }

    /**
     * select all text
     */
    public void selectAllText() {
        textArea.selectAll();
    }

    /**
     * Undo previous edit
     */
    public void undo() {
        textArea.undoLastAction();
    }

    /**
     * Redo previous edit
     */
    public void redo() {
        textArea.redoLastAction();
    }

    /**
     * Update state of Edit menu's Undo menu item.
     */
    @Deprecated
    public void updateUndoState() {
        // TODO: delete for bad practice
        mainUI.editUndoAction.updateUndoState();
    }

    /**
     * Update state of Edit menu's Redo menu item.
     */
    @Deprecated
    public void updateRedoState() {
        // TODO: delete for bad practice
        mainUI.editRedoAction.updateRedoState();
    }

    /**
     * Update the caret position label on the editor's border to
     * display the current line and column.  The position is given
     * as text stream offset and will be converted into line and column.
     *
     * @param pos Offset into the text stream of caret.
     */
    public void displayCaretPosition(int pos) {
        displayCaretPosition(convertStreamPositionToLineColumn(pos));
    }

    /**
     * Display cursor coordinates
     *
     * @param p Point object with x-y (column, line number) coordinates of cursor
     */
    public void displayCaretPosition(Point p) {
        caretPositionLabel.setText("Line: " + p.y + " Column: " + p.x);
    }

    /**
     * Given byte stream position in text being edited, calculate its column and line
     * number coordinates.
     *
     * @param position position of character
     * @return position Its column and line number coordinate as a Point.
     */
    public Point convertStreamPositionToLineColumn(int position) {
        String textStream = textArea.getText();
        int line = 1;
        int column = 1;
        for (int i = 0; i < position; i++) {
            if (textStream.charAt(i) == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
        }
        return new Point(column, line);
    }

    /**
     * Given line and column (position in the line) numbers, calculate
     * its byte stream position in text being edited.
     *
     * @param line   Line number in file (starts with 1)
     * @param column Position within that line (starts with 1)
     * @return corresponding stream position.  Returns -1 if there is no corresponding position.
     */
    public int convertLineColumnToStreamPosition(int line, int column) {
        String textStream = textArea.getText();
        int textLength = textStream.length();
        int textLine = 1;
        int textColumn = 1;
        for (int i = 0; i < textLength; i++) {
            if (textLine == line && textColumn == column) {
                return i;
            }
            if (textStream.charAt(i) == '\n') {
                textLine++;
                textColumn = 1;
            } else {
                textColumn++;
            }
        }
        return -1;
    }

    /**
     * Select the specified editor text line.  Lines are numbered starting with 1, consistent
     * with line numbers displayed by the editor.
     *
     * @param line The desired line number of this TextPane's text.  Numbering starts at 1, and
     *             nothing will happen if the parameter value is less than 1
     */
    public void selectLine(int line) {
        if (line > 0) {
            int lineStartPosition = convertLineColumnToStreamPosition(line, 1);
            int lineEndPosition = convertLineColumnToStreamPosition(line + 1, 1) - 1;
            if (lineEndPosition < 0) { // DPS 19 Sept 2012.  Happens if "line" is last line of file.

                lineEndPosition = textArea.getText().length() - 1;
            }
            if (lineStartPosition >= 0) {
                textArea.select(lineStartPosition, lineEndPosition);
                //sourceCode.setSelectionVisible(true);     // TODO: to implement with RSyntaxTextArea
            }
        }
    }


    /**
     * Select the specified editor text line.  Lines are numbered starting with 1, consistent
     * with line numbers displayed by the editor.
     *
     * @param line   The desired line number of this TextPane's text.  Numbering starts at 1, and
     *               nothing will happen if the parameter value is less than 1
     * @param column Desired column at which to place the cursor.
     */
    public void selectLine(int line, int column) {
        selectLine(line);
        // Made one attempt at setting cursor; didn't work but here's the attempt
        // (imagine using it in the one-parameter overloaded method above)
        //sourceCode.setCaretPosition(lineStartPosition+column-1);        
    }

    /**
     * Finds next occurrence of text in a forward search of a string. Search begins
     * at the current cursor location, and wraps around when the end of the string
     * is reached.
     *
     * @param find          the text to locate in the string
     * @param caseSensitive true if search is to be case-sensitive, false otherwise
     * @return TEXT_FOUND or TEXT_NOT_FOUND, depending on the result.
     */
    public int doFindText(String find, boolean caseSensitive) {
        // TODO: to implement with RSyntaxTextArea
        return 0;
        //return sourceCode.doFindText(find, caseSensitive);
    }

    /**
     * Finds and replaces next occurrence of text in a string in a forward search.
     * If cursor is initially at end
     * of matching selection, will immediately replace then find and select the
     * next occurrence if any.  Otherwise it performs a find operation.  The replace
     * can be undone with one undo operation.
     *
     * @param find          the text to locate in the string
     * @param replace       the text to replace the find text with - if the find text exists
     * @param caseSensitive true for case sensitive. false to ignore case
     * @return Returns TEXT_FOUND if not initially at end of selected match and matching
     * occurrence is found.  Returns TEXT_NOT_FOUND if the text is not matched.
     * Returns TEXT_REPLACED_NOT_FOUND_NEXT if replacement is successful but there are
     * no additional matches.  Returns TEXT_REPLACED_FOUND_NEXT if replacement is
     * successful and there is at least one additional match.
     */
    public int doReplace(String find, String replace, boolean caseSensitive) {
        // TODO: to implement with RSyntaxTextArea
        //return sourceCode.doReplace(find, replace, caseSensitive);
        return 0;
    }


    /**
     * Finds and replaces <B>ALL</B> occurrences of text in a string in a forward search.
     * All replacements are bundled into one CompoundEdit, so one Undo operation will
     * undo all of them.
     *
     * @param find          the text to locate in the string
     * @param replace       the text to replace the find text with - if the find text exists
     * @param caseSensitive true for case sensitive. false to ignore case
     * @return the number of occurrences that were matched and replaced.
     */
    public int doReplaceAll(String find, String replace, boolean caseSensitive) {
        // TODO: to implement with RSyntaxTextArea
        //return sourceCode.doReplaceAll(find, replace, caseSensitive);
        return 0;
    }


    /**
     * Update, if source code is visible, when Font setting changes.
     * This method is specified by the Observer interface.
     */
    public void update(Observable fontChanger, Object arg) {
        textArea.setFont(Globals.getSettings().getEditorFont());
        // TODO: implement remaining operations with RSyntaxTextArea
//        sourceCode.setLineHighlightEnabled(Globals.getSettings().getBooleanSetting(Settings.EDITOR_CURRENT_LINE_HIGHLIGHTING));
//        sourceCode.setCaretBlinkRate(Globals.getSettings().getCaretBlinkRate());
//        sourceCode.setTabSize(Globals.getSettings().getEditorTabSize());
//        sourceCode.updateSyntaxStyles();
        textArea.revalidate();
    }

}