package mars.venus;

import mars.*;
import mars.venus.editors.MARSTextEditingArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import javax.swing.undo.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class EditPane extends JPanel implements Observer {

    private RTextScrollPane scrollPane;
    private RSyntaxTextArea textArea;

    private boolean caretPositionEnabled;

    //private JLabel caretPositionLabel;
    private FileStatus fileStatus;
    private VenusUI mainUI;



    public EditPane(VenusUI appFrame) {
        super(new BorderLayout());

        caretPositionEnabled = true;
        mainUI = appFrame;

        // We want to be notified of editor font changes! See update() below.
        Globals.getSettings().addObserver(this);
        fileStatus = new FileStatus();

        initTextArea();
        initScrollPane();

        add(scrollPane, BorderLayout.CENTER);



        // If source code is modified, will set flag to trigger/request file save.
//        textArea.getDocument().addDocumentListener(
//                new DocumentListener() {
//                    @Override
//                    public void insertUpdate(DocumentEvent e) {
//                        switch (FileStatus.get()) {
//                            case FileStatus.OPENING: FileStatus.set(FileStatus.NOT_EDITED); break;
//                            case FileStatus.NEW_NOT_EDITED: FileStatus.set(FileStatus.NEW_EDITED); break;
//                            default: FileStatus.set(FileStatus.EDITED);
//                        }
//                        FileStatus.setEdited(true);
//
//                        //    TODO: to implement but not in this way
//                        //    if (getFileStatus() == FileStatus.NEW_EDITED) {
//                        //        mainUI.editor.setTitle("", getFilename(), getFileStatus());
//                        //    } else {
//                        //        mainUI.editor.setTitle(getPathname(), getFilename(), getFileStatus());
//                        //    }
//                        //    Globals.getGui().getMainPane().getExecutePane().clearPane();
//
//                        System.out.println("insert update");
//                    }
//
//                    @Override
//                    public void removeUpdate(DocumentEvent e) {
//                        insertUpdate(e);
//                        System.out.println("remove update");
//                    }
//
//                    @Override
//                    public void changedUpdate(DocumentEvent e) {
//                        insertUpdate(e);
//                        System.out.println("changed update");
//                    }
//                }
//        );

        setSourceCode("", false);

        JPanel editInfo = new JPanel(new BorderLayout());
//        caretPositionLabel = new JLabel();
//        caretPositionLabel.setToolTipText("Tracks the current position of the text editing cursor.");
        displayCaretPosition(new Point());
//        editInfo.add(caretPositionLabel, BorderLayout.WEST);
        this.add(editInfo, BorderLayout.SOUTH);
    }

    public void showCaretPosition(boolean enabled) {
        caretPositionEnabled = enabled;
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
        textArea.discardAllEdits();
        textArea.setCaretPosition(0);
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

    public void copyToClipboard() {
        textArea.copy();

        // The "set visible" operation is used because clicking on the toolbar
        // icon causes the blinking cursor to disappear!
        // This does not happen when using menu selection or Ctrl-C/X/V
        textArea.getCaret().setVisible(true);
    }

    public void cutToClipboard() {
        textArea.cut();

        // The "set visible" operation is used because clicking on the toolbar
        // icon causes the blinking cursor to disappear!
        // This does not happen when using menu selection or Ctrl-C/X/V
        textArea.getCaret().setVisible(true);
    }

    public void pasteFromClipboard() {
        textArea.paste();

        // The "set visible" operation is used because clicking on the toolbar
        // icon causes the blinking cursor to disappear!
        // This does not happen when using menu selection or Ctrl-C/X/V
        textArea.getCaret().setVisible(true);
    }

    public void selectAllText() {
        textArea.selectAll();
    }

    public boolean canRedo() {
        return textArea.canRedo();
    }

    public boolean canUndo() {
        return textArea.canUndo();
    }

    public void undo() {
        textArea.undoLastAction();
    }

    public void redo() {
        textArea.redoLastAction();
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
//        caretPositionLabel.setText("Line: " + p.y + " Column: " + p.x);
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
     * @return true if text was found, false otherwise
     */
    public boolean findText(String find, boolean caseSensitive) {
        SearchContext context = new SearchContext();
        context.setSearchFor(find);
        context.setSearchForward(true);
        context.setMatchCase(caseSensitive);
        context.setWholeWord(false);

        return SearchEngine.find(textArea, context).wasFound();
    }

    /**
     * Finds and replaces next occurrence of text in a string in a forward search.
     * If cursor is initially at end
     * of matching selection, will immediately replace then find and select the
     * next occurrence if any.  Otherwise it performs a find operation.
     *
     * @param find          the text to locate in the string
     * @param replace       the text to replace the find text with - if the find text exists
     * @param caseSensitive true for case sensitive. false to ignore case
     * @return Returns true if text was replaced, false otherwise
     */
    // TODO: for some reason it doesn't work so well
    public boolean replace(String find, String replace, boolean caseSensitive) {
        SearchContext context = new SearchContext();
        context.setSearchFor(find);
        context.setReplaceWith(replace);
        context.setSearchForward(true);
        context.setMatchCase(caseSensitive);
        context.setWholeWord(false);

        return SearchEngine.replace(textArea, context).wasFound();
    }


    /**
     * Replaces all occurrences of text in a string.
     *
     * @param find          the text to locate in the string
     * @param replace       the text to replace the find text with - if the find text exists
     * @param caseSensitive true for case sensitive. false to ignore case
     * @return the number of occurrences that were matched and replaced.
     */
    public int replaceAll(String find, String replace, boolean caseSensitive) {
        SearchContext context = new SearchContext();
        context.setSearchFor(find);
        context.setReplaceWith(replace);
        context.setMatchCase(caseSensitive);
        context.setWholeWord(false);

        return SearchEngine.replaceAll(textArea, context).getCount();
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