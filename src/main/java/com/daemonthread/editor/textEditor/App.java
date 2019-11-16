package com.daemonthread.editor.textEditor;

import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class App extends JFrame
{
    public JFrame mainFrame; //Outer Frame
    public JMenuBar topMenuBar; //Top Menu Bar

    public JLabel statusLabel; // Status Label to show errors and messages
    public JTextArea textArea; // Main Text Area

    boolean saved; // Check if file saved or not;
    boolean newFileFlag; // Check if new File is created or not
    String fileName; // Store the name of the file opened/created
    
    String applicationTitle = "JEDIT";

    File fileRef; // Store the file
    JFileChooser fileChooser;


    App() {

        saved = false;
        newFileFlag = true;
        fileName = "Untitled";

        fileRef = new File(fileName);

        fileChooser = new JFileChooser();

        //Add Supported Extensions
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Java Source Files (*.java)", "java"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("C++ Source Files (*.cpp)", "cpp"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("C Source Files (*.c)", "c"));

        //Set the Current Folder as intital Path to choose from
        fileChooser.setCurrentDirectory(new File("."));

        try {

            // Set the Look and Feel of the window
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.metal.MetalLookAndFeel");

            //Set the theme to OCEAN
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Prepare the rest of the GUI
        prepareGUI();
    }   
    
    
    boolean isSaved() {
        return saved;
    }

    void setSave(boolean saved) {
        this.saved = saved;
    }

    String getFileName() {
        return fileName;
    }

    void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //DESIGN the basic layout
    private void prepareGUI() {
        
        //Creating the main Frame
        mainFrame = new JFrame("Text Editor");
        mainFrame.setSize(600, 600);
        
        //Create top menu bar
        topMenuBar = new JMenuBar();
        
        //Call MenuBar
        showMenu();

        statusLabel = new JLabel("", JLabel.CENTER);

        //Call TextArea
        showTextArea();
        
        // ! TEST OUT DIFFERENT LAYOUT (BOXSET will probably work right)
        mainFrame.setJMenuBar(topMenuBar);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
        
    }

    /** MENU AREA */
    private void showMenu() {
        //Create Menu
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu help = new JMenu("Help");

        //Add Menu to Menu Bar
        topMenuBar.add(file);
        topMenuBar.add(edit);
        topMenuBar.add(help);

        //Create MenuItem for FILE
        JMenuItem newFile = new JMenuItem("New");
        newFile.setMnemonic(KeyEvent.VK_N);
        newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newFile.setActionCommand("New");

        JMenuItem open = new JMenuItem("Open");
        open.setMnemonic(KeyEvent.VK_O);
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        open.setActionCommand("Open");

        JMenuItem save = new JMenuItem("Save");
        save.setMnemonic(KeyEvent.VK_S);
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        save.setActionCommand("Save");

        JMenuItem saveAs = new JMenuItem("Save As");
        saveAs.setActionCommand("SaveAs");

        JMenuItem exit = new JMenuItem("Exit");
        exit.setMnemonic(KeyEvent.VK_W);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        exit.setActionCommand("Exit");

        //Add Menu items to FILE Menu
        file.add(newFile);
        file.add(open);
        file.add(save);
        file.add(saveAs);       
        file.add(exit);

        //Create menuItems for EDIT
        JMenuItem undo = new JMenuItem("Undo");
        undo.setMnemonic(KeyEvent.VK_Z);
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        undo.setActionCommand("Undo");

        JMenuItem cut = new JMenuItem("Cut");
        cut.setMnemonic(KeyEvent.VK_X);
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cut.setActionCommand("Cut");

        JMenuItem copy = new JMenuItem("Copy");
        copy.setMnemonic(KeyEvent.VK_C);
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copy.setActionCommand("Copy");

        JMenuItem paste = new JMenuItem("Paste");
        paste.setMnemonic(KeyEvent.VK_V);
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        paste.setActionCommand("Paste");

        //Add menu item to EDIT
        edit.add(undo);
        edit.add(copy);
        edit.add(cut);
        edit.add(paste);

        //Set MenuItemListener
        MenuItemListener menuItemListener = new MenuItemListener();
        newFile.addActionListener(menuItemListener);
        open.addActionListener(menuItemListener);
        save.addActionListener(menuItemListener);        
        exit.addActionListener(menuItemListener);
        undo.addActionListener(menuItemListener);
        cut.addActionListener(menuItemListener);
        copy.addActionListener(menuItemListener);
        paste.addActionListener(menuItemListener);

        MenuItemKeyListener menuItemKeyListener = new MenuItemKeyListener();
        newFile.addMenuKeyListener(menuItemKeyListener);
        open.addMenuKeyListener(menuItemKeyListener);
        exit.addMenuKeyListener(menuItemKeyListener);
        save.addMenuKeyListener(menuItemKeyListener);

        mainFrame.setVisible(true);
    }
    
    /** SETTING TEXT AREA */
    private void showTextArea() {
        textArea = new JTextArea(1500, 1500);

        //Setting Tab Size to 4        
        textArea.setTabSize(4);
        textArea.setFont(new Font("Serif",Font.PLAIN,20));

        mainFrame.add(textArea);
        mainFrame.setVisible(true);
    }
    
    /**SAVE FILE */
    boolean saveFile(File temp) {
        FileWriter fout = null;
        
        try{
            fout = new FileWriter(temp);
            fout.write(textArea.getText());
        } catch (IOException e) {
            updateStatus(temp, false);
            return false;
        } finally {
            try {
                fout.close();
            } catch (IOException e) {
            }
        }

        updateStatus(temp, true);
        return true;
    }

    boolean saveFile() {
        if (!newFileFlag) {
            return saveFile(fileRef);
        }
        return saveAsFile();
    }

    /** SAVE AS OPERATION */
    boolean saveAsFile() {
        File temp = null;
        fileChooser.setDialogTitle("Save As ");
        fileChooser.setApproveButtonText("Save Now");
        fileChooser.setApproveButtonToolTipText("Click to save now");

        do {
            // If the user doesnt save the file then exit
            if (fileChooser.showSaveDialog(mainFrame) != JFileChooser.APPROVE_OPTION) {
                return false;
            }
            
            //Open the Dialog to save the file
            temp = fileChooser.getSelectedFile();

            //File already exists in the location
            if (!temp.exists()) {
                break;
            }

            if(JOptionPane.showConfirmDialog(mainFrame,"<html>"+temp.getPath()+"already exists.<br>Do you want to replace it?<html>",
                "Save As", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                break;
            }
        } while (true);

        return saveFile(temp);
    }

    /**OPEN A FILE FROM THE AND APPEND TEXT */
    boolean openFile(File temp) {
        FileInputStream fin = null;
        BufferedReader din = null;

        try  
        {  
            fin=new FileInputStream(temp);  
            din = new BufferedReader(new InputStreamReader(fin));
            
            String str = " ";
            
            while(str!=null)  
            {  
                str=din.readLine();  
                if (str == null) {
                    break;
                }
                textArea.append(str+"\n");  
            }  
        
        } catch (IOException e) {
            updateStatus(temp, false);
            return false;
        }  
        finally  
        {
            try {
                din.close();
                fin.close();
            } catch (IOException e) {
            }
        }  
        updateStatus(temp,true);  
        textArea.setCaretPosition(0);
        
        return true;  
    }      

    /** OPEN A FILE */
    void openFile()  
    {
        if (!confirmSave())
            return;
        fileChooser.setDialogTitle("Open File...");
        fileChooser.setApproveButtonText("Open this");
        fileChooser.setApproveButtonMnemonic(KeyEvent.VK_O);
        fileChooser.setApproveButtonToolTipText("Click me to open the selected file.!");

        File temp = null;
        do {
            if (fileChooser.showOpenDialog(mainFrame) != JFileChooser.APPROVE_OPTION)
                return;
            temp = fileChooser.getSelectedFile();

            if (temp.exists())
                break;

            JOptionPane.showMessageDialog(mainFrame,
                    "<html>" + temp.getName() + "<br>file not found.<br>"
                            + "Please verify the correct file name was given.<html>",
                    "Open", JOptionPane.INFORMATION_MESSAGE);

        } while (true);

        textArea.setText("");

        if (!openFile(temp)) {
            fileName = "Untitled";
            saved = true;
            mainFrame.setTitle(fileName + " - " + applicationTitle);
        }
        if (!temp.canWrite()) {
            newFileFlag = true;
        }

    }
    
    /**UPDATE THE CURRENT STATUS OF THE FILE */
    void updateStatus(File temp,boolean saved)  
    {
        if (saved) {
            this.saved = true;

            fileName = new String(temp.getName());

            if (!temp.canWrite()) {
                fileName += "(Read only)";
                newFileFlag = true;
            }

            fileRef = temp;

            mainFrame.setTitle(fileName + " - " + applicationTitle);
            statusLabel.setText("File : " + temp.getPath() + " saved/opened successfully.");

            newFileFlag = false;
        } else {
            statusLabel.setText("Failed to save/open : " + temp.getPath());
        }
    }
    
    /**CONFIRM IF FILE SAVED OR NOT */
    boolean confirmSave()  
    {
        String strMsg = "<html>The text in the " + fileName + " file has been changed.<br>"
                + "Do you want to save the changes?<html>";

        if (!saved) {
            int x = JOptionPane.showConfirmDialog(mainFrame, strMsg, applicationTitle,
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (x == JOptionPane.CANCEL_OPTION) {
                return false;
            }
            if (x == JOptionPane.YES_OPTION && !saveAsFile()) {
                return false;
            }
        }
        return true;
    }
    
    /**CREATE NEW FILE */
    void newFile()  
    {  
        if (!confirmSave()) {
            return;
        }
        
        textArea.setText("");
         
        fileName=new String("Untitled");  
        fileRef = new File(fileName);
        
        saved=true;  
        newFileFlag = true;
        
        mainFrame.setTitle(fileName+" - "+applicationTitle);  
    }  

    /** DRIVER CODE */
    public static void main( String[] args )
    {
        App editor = new App();   

    }
    
    /**MENU LISTENER TO LISTEN TO RECEIVE MENU ITEM PRESS EVENTS */
    class MenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            if (actionCommand.equals("Exit")) {
                if (confirmSave()) {
                    System.exit(0);
                }
            } else if (actionCommand.equals("New")) {
                newFile();
            } else if (actionCommand.equals("Open")) {
                openFile();            
            } else if (actionCommand.equals("Save")) {
                saveFile();
            } else if (actionCommand.equals("SaveAs")) {
                saveAsFile();
            } else if (actionCommand.equals("Cut")) {
                textArea.cut();
            } else if (actionCommand.equals("Copy")) {
                textArea.copy();
            } else if (actionCommand.equals("Paste")) {
                textArea.paste();
            } 

        }
    }

    /**LISTENS TO KEY PRESSES */
    class MenuItemKeyListener implements MenuKeyListener {
        public void menuKeyTyped(MenuKeyEvent e) {            
            char ch = e.getKeyChar();
            if (ch == 'E' || ch == 'e') {
                System.exit(0);
            }
        }
        public void menuKeyPressed(MenuKeyEvent e) {
            char ch = e.getKeyChar();
            if (ch == 'E' || ch == 'e') {
                System.exit(0);
            }
        }
        public void menuKeyReleased(MenuKeyEvent e) {
            char key = e.getKeyChar();
            if (key == 'E' || key == 'e') {
                System.exit(0);
            }else{
                System.out.println(e.getKeyChar() + " pressed!");
            }
        }
    }
}
