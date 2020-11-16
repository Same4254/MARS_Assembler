package mars.venus;

import mars.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class EditPane extends JPanel implements Observer {

    private RTextScrollPane scrollPane;
    private RSyntaxTextArea textArea;

    private FileStatus fileStatus;
    private VenusUI mainUI;

    // TODO: implement caret position

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
    public boolean replace(String find, String replace, boolean caseSensitive) {
        // TODO: for some reason it doesn't work so well

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
        // TODO: update highlight enabled/disabled
        // TODO: update caret blink rate
        // TODO: update tab size
        // TODO: update syntax style
        textArea.revalidate();
    }

}