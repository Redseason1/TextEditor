package TextEditor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
public class TextEditor extends JFrame implements ActionListener {
    JTextArea textArea;
    JScrollPane scrollPane;
    JLabel fontLabel;
    JSpinner fontSizeSpinner;
    JButton fontColorButton;
    JComboBox<String> fontBox;
 
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    public TextEditor(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Bro text Editor");
        this.setSize(500, 500);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
  
        textArea = new JTextArea();
        textArea.setLineWrap(true);  //字串太長會自動依照版面大小換行
        textArea.setWrapStyleWord(true);  //同setLineWrap()方法，但沒有setLineWrap()方法無法執行
        textArea.setFont(new Font("Arial",Font.PLAIN,20));
  
        scrollPane = new JScrollPane(textArea);  //捲動面板
        scrollPane.setPreferredSize(new Dimension(450,450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);  //設定捲動面板會自動垂直捲動
  
        fontLabel = new JLabel("Font: ");
  
        fontSizeSpinner = new JSpinner();  //字型大小
        fontSizeSpinner.setPreferredSize(new Dimension(50,25));
        fontSizeSpinner.setValue(20);  //設定字型預設值

        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {  //設定更改字型大小
                textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizeSpinner.getValue())); 
            }  
        });
  
        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);
  
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();  //可用字型集合
  
        fontBox = new JComboBox<>(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");
  
        // ------ menubar ------
  
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");
   
        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);
   
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
  
        // ------ /menubar ------
   
        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.setVisible(true);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) { 
        if(e.getSource()==fontColorButton) {
            //JColorChooser colorChooser = new JColorChooser();
   
            Color color = JColorChooser.showDialog(null, "Choose a color", Color.black);
   
            textArea.setForeground(color);  //設定更改字型顏色
        }
  
        if(e.getSource()==fontBox) {  //設定更改字型
            textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
        }
  
        if(e.getSource()==openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));  //設定預設路徑；"."意指此編輯器的預設路徑
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");  //設定預設資料檔案
            fileChooser.setFileFilter(filter);
   
            int response = fileChooser.showOpenDialog(null);
   
            if(response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());  //選擇的路徑
                Scanner fileIn = null;
    
                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()) {
                        while(fileIn.hasNextLine()) {  //當load檔有下一行時
                            String line = fileIn.nextLine()+"\n";  //在需要換行時設定換行
                            textArea.append(line);  //放進textArea裡
                        }
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                finally {
                    fileIn.close();
                }
            }   
        }

        if(e.getSource()==saveItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
   
            int response = fileChooser.showSaveDialog(null);
   
            if(response == JFileChooser.APPROVE_OPTION) {
                File file;
                PrintWriter fileOut = null;
    
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());  //選擇的路徑
                try {
                    fileOut = new PrintWriter(file);  //將save的路徑給fileOut
                    fileOut.println(textArea.getText()); //textArea裡的文字印到fileOut並save
                }
                catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                finally {
                    fileOut.close();
                }   
            }
        }

        if(e.getSource()==exitItem) {
            System.exit(0);
        }  
    }
}
