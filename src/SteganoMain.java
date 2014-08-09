/* IMAGE STEGANOGRAPHY : SteganoMain.java
 * 
 * This source code form is protected under GPL v2.0.
 * To read more about the license, follow the link:
 * http://www.gnu.org/licenses/gpl-2.0.html
 * 
 * Created by Manik Singh <maniksingh92@live.com> as a project
 * during the summer training.
 * 
 * Initiating date : 7-JUL-2014
 * 
 * >> SteganoMain.java is the point where the Steganography process
 * actually begins
 * >> SteganoMain.java calls an object from SteganoImgProcess.java
 * in order to perform the bit operations on the image, to encode
 * or decode a message from the image.
 * 
 *  
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
//import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class SteganoMain extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 642140547550428960L;
	
	//Creating different Swing objects for the GUI
	JLabel displayOutput;
	JFileChooser inputFile, outputSaveAs, txtFile;
	JLabel imageLabel, textLabel, outputLocationLabel;
	JTextField inputLocation, outputLocation;
	JButton inputBrowse, outputBrowse, resetButton, createButton, saveButton, txtButton;
	JTextArea text;
	JPanel panel;
	MenuItem encodeDropDown, decodeDropDown, exitDropDown;
	Menu fileMenu, exitMenu;
	MenuBar mb;
	FileFilter allImages = new FileNameExtensionFilter("All Images", ImageIO.getReaderFileSuffixes());
	FileFilter pngImages = new FileNameExtensionFilter("PNG Images", "png");
	FileFilter txtFiles  = new FileNameExtensionFilter("TXT Files", "txt");
	
	//Creating different objects for processing the image file
	File activeFile, outputFile, activeTxtFile;
	Image inputImg, outputImg;
	BufferedImage img, imgOut;
	int imgCols, imgRows;
	
	//true = encoding process, false = decoding process.
	//switcher affects the FileFilter which will be applied to the JFileChooser
	boolean switcher = true;
	
	//This object is responsible for calling functions which will actually
	//encode text into image file and save it, or extract text from encoded image
	SteganoImgProcess processingObject = new SteganoImgProcess();
	
	//Start of SteganoMain() constructor
	SteganoMain() {
		
		try
		{
		  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){}
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		
		//Used to display output image which has been encoded with the hidden text
		displayOutput = new JLabel("");
		displayOutput.setBounds(20, 20, 100, 100);
		
		//The following components are for GUI purposes, and are self explanatory
		imageLabel = new JLabel();
		imageLabel.setForeground(Color.WHITE);
		imageLabel.setText("Image:");
		imageLabel.setBounds(30, 20, 100, 30);
		inputLocation = new JTextField(30);
		inputLocation.setEditable(false);
		inputLocation.setBounds(30, 50, 220, 30);
		inputBrowse = new JButton("Choose");
		inputBrowse.addActionListener(this);
		inputBrowse.setBounds(260, 50, 80, 30);
		
		textLabel = new JLabel();
		textLabel.setText("Input text to be encoded:");
		textLabel.setForeground(Color.WHITE);
		textLabel.setBounds(30, 90, 200, 30);
		text = new JTextArea();
		text.setBounds(30, 120, 310, 140);
		
		txtButton = new JButton("or, Choose content from a file");
		txtButton.addActionListener(this);
		txtButton.setBounds(30, 260, 310, 30);
		txtButton.setVisible(true);
		
		outputLocationLabel = new JLabel();
		outputLocationLabel.setText("Destination File:");
		outputLocationLabel.setBounds(30, 300, 200, 30);
		outputLocationLabel.setForeground(Color.WHITE);
		outputLocation = new JTextField(30);
		outputLocation.setBounds(30, 330, 220, 30);
		outputBrowse = new JButton("Browse");
		outputBrowse.addActionListener(this);
		outputBrowse.setBounds(260, 330, 80, 30);
		
		resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		resetButton.setBounds(30, 380, 150, 30);
		createButton = new JButton("Encode");
		createButton.addActionListener(this);
		createButton.setBounds(190, 380, 150, 30);
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		saveButton.setBounds(190, 380, 150, 30);
		saveButton.setVisible(false);
				
		panel = new JPanel() {
			
			private static final long serialVersionUID = 8104705757223466700L;
			
			//Set background image
			public void paintComponent(Graphics g) {
				Image background = null;
				try {
					background = ImageIO.read(getClass().getResource("images/login_background.jpg"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				g.drawImage(background,0,0,this);
			}
		};
		panel.setLayout(null);
		panel.add(displayOutput);
		panel.add(imageLabel);
		panel.add(inputLocation);
		panel.add(inputBrowse);
		panel.add(textLabel);
		panel.add(text);
		panel.add(outputLocation);
		panel.add(outputLocationLabel);
		panel.add(outputBrowse);
		panel.add(resetButton);
		panel.add(createButton);
		panel.add(saveButton);
		panel.add(txtButton);
		
		setTitle("Steganography - Encode");
		add(panel);
		setVisible(true);
		setResizable(false);
		setSize(370,480);
		setLocation(350,100);
		
		encodeDropDown = new MenuItem("Encode");
        encodeDropDown.addActionListener(this);
        decodeDropDown = new MenuItem("Decode");
        decodeDropDown.addActionListener(this);
        fileMenu = new Menu("File");
        fileMenu.add(encodeDropDown);
        fileMenu.add(decodeDropDown);
		
        exitDropDown = new MenuItem("Exit Program");
        exitDropDown.addActionListener(this);
        exitMenu = new Menu("Exit");
        exitMenu.add(exitDropDown);
        
        mb = new MenuBar();
        mb.add(fileMenu);
        mb.add(exitMenu);
        setMenuBar(mb);
			
	} //end of SteganoMain() constructor

	public void actionPerformed(ActionEvent mouseClick) {
		
		if (mouseClick.getSource() == exitDropDown) { //Drop down option for exiting program
			System.out.println("Exiting the software.");
			System.exit(0);
			
		} else if (mouseClick.getSource() == resetButton) { //button to set all inputs to null
			displayOutput.setIcon(null);
			inputLocation.setText(null);
			outputLocation.setText(null);
			text.setText(null);
			
		} else if (mouseClick.getSource() == encodeDropDown) { //Drop down option for initializing encoding process
			resetButton.doClick(); //call resetButton action
			createButton.setVisible(true); //making button for encoding process visible
			saveButton.setVisible(false); //hiding button for decoding process
			setTitle("Steganography - Encode");
			textLabel.setText("Input text to be encoded:");
			switcher = true; //set switcher value to denote encoding
			text.setBounds(30, 120, 310, 140);
			txtButton.setVisible(true);
			
		} else if (mouseClick.getSource() == decodeDropDown) { //Drop down option for initializing decoding process
			resetButton.doClick(); //call resetButton action
			createButton.setVisible(false); //hiding encoding process button
			saveButton.setVisible(true); //making button for decoding process visible
			setTitle("Steganography - Decode");
			textLabel.setText("Decoded text:");
			switcher = false; //set switcher value to denote decoding
			text.setBounds(30, 120, 310, 170);
			txtButton.setVisible(false);
			
		} else if (mouseClick.getSource() == inputBrowse) { //Process for selecting image file for encoding or decoding
			if(inputFile == null) {
				inputFile = new JFileChooser();
			}
			
			//modifying File types which can be chosen by inputFile
			inputFile.setAcceptAllFileFilterUsed(false);
			if(switcher) { //if switcher denotes encoding process
				inputFile.removeChoosableFileFilter(pngImages); //removing restriction to choose just .PNG images
				inputFile.addChoosableFileFilter(allImages); //adding ability to choose any type of source image
			} else { //if switcher denotes decoding process
				inputFile.removeChoosableFileFilter(allImages); //removing ability to choose any type of image
				inputFile.addChoosableFileFilter(pngImages); //adding restriction to choose just .PNG images
			}
			
			//Displaying dialog box to choose appropriate file
			int openFile = inputFile.showDialog(null, "Open file");
			if (openFile == JFileChooser.APPROVE_OPTION) {
				activeFile = inputFile.getSelectedFile();
				inputLocation.setText(activeFile.getAbsolutePath());
				outputSaveAs = new JFileChooser(activeFile);
			}
			
		} else if (mouseClick.getSource() == outputBrowse) { //Process for selecting output file for encoded image or decoded text file
			if(outputSaveAs == null) {
				outputSaveAs = new JFileChooser();
			}
			
			//modifying File types which can be chosen by outputSaveAs
			outputSaveAs.setAcceptAllFileFilterUsed(false);
			if(switcher) { //if switcher denotes encoding process
				outputSaveAs.removeChoosableFileFilter(txtFiles); //removing option for choosing .TXT file
				outputSaveAs.addChoosableFileFilter(pngImages); //adding restriction to choose just .PNG images for decoding
				
			} else { //if switcher denotes decoding process
				outputSaveAs.removeChoosableFileFilter(pngImages); //removing restriction to choose just .PNG images
				outputSaveAs.addChoosableFileFilter(txtFiles); //adding option for choosing output .TXT file for decoded text
			}
			
			//Displaying dialog box to choose appropriate file
			int openDirectory = outputSaveAs.showDialog(null, "Save As");
			if (openDirectory == JFileChooser.APPROVE_OPTION) {
				outputFile = outputSaveAs.getSelectedFile();
				if(switcher) { //if switcher denotes encoding process
					outputLocation.setText(outputFile.getAbsolutePath() + ".png"); //append .png extension to filename
				} else { //if switcher denotes decoding process
					outputLocation.setText(outputFile.getAbsolutePath() + ".txt"); //append .txt extension to filename
				}
			}
		} else if(mouseClick.getSource() == txtButton) { //Process for selecting message input from file
			if(txtFile == null) {
				txtFile = new JFileChooser();
			}
			
			int openText = txtFile.showDialog(null, "Open file");
			if (openText == JFileChooser.APPROVE_OPTION) {
				
				activeTxtFile = txtFile.getSelectedFile();
				try {
					text.setText(new Scanner( activeTxtFile ).useDelimiter("\\A").next()); //get character format of file
				} catch (FileNotFoundException e) {
					System.out.println("Text file not found!");
					e.printStackTrace();
				}
			}
			
		} else if (mouseClick.getSource() == createButton) { //Process for encoding text into source image
			try {
				img = ImageIO.read(new File(activeFile.getAbsolutePath())); //load source image into BufferedImage img
				imgOut = ImageIO.read(new File(activeFile.getAbsolutePath())); //create duplicate of source image into which message will be encoded
																			   //in order to prevent overwriting of source image
			} catch (IOException e) {
				System.out.println("error reading file!");
				e.printStackTrace();
			}
			imgCols = img.getWidth();
		    imgRows = img.getHeight();
		    
		    //Run the encode() method from SteganoImgProcess object for actually encoding text into image
		    boolean success = processingObject.encode(img, imgOut, imgCols, imgRows, text.getText(), outputLocation.getText());
		    
		    if(success) {
		    	JOptionPane.showMessageDialog(this,"Successfully created image", "Success",JOptionPane.INFORMATION_MESSAGE);
		    } else {
		    	JOptionPane.showMessageDialog(this,"Problem in saving file!", "Error",JOptionPane.ERROR_MESSAGE);
		    }
		    //Displaying output image file on panel for the user
		    /*Image im = Toolkit.getDefaultToolkit().getImage(outputFile.getAbsolutePath() + ".png");
		    ImageIcon ioc=new ImageIcon(im.getScaledInstance(displayOutput.getWidth(), displayOutput.getHeight(),Image.SCALE_SMOOTH));
		    displayOutput.setIcon(ioc);
            displayOutput.setVisible(true);*/
            
		} else if (mouseClick.getSource() == saveButton) { //Process for decoding text from source .PNG file
			try {
				img = ImageIO.read(new File(activeFile.getAbsolutePath())); //load source image into BufferedImage img for decoding
			} catch (IOException e) {
				System.out.println("error reading file!");
				e.printStackTrace();
			}
			imgCols = img.getWidth();
		    imgRows = img.getHeight();
		    
		    //Run the decode() method from SteganoImgProcess object for actually decoding text from image
		    String decodedMsg = processingObject.decode(img, imgCols, imgRows); //Store decoded message
		    
		    if(decodedMsg == null) { //If image file was not encoded with this software
		    	JOptionPane.showMessageDialog(this,"Incorrect file or corrupted message", "Error",JOptionPane.ERROR_MESSAGE);
		    	resetButton.doClick();
		    } else {
		    	text.setText(decodedMsg); //Display decoded message on panel
		    	try {
		    		//Create .TXT file with the decoded message
					PrintWriter out = new PrintWriter(outputLocation.getText());
					out.print(decodedMsg);
					out.close();
					JOptionPane.showMessageDialog(this,"Successfully created TXT file", "Success",JOptionPane.INFORMATION_MESSAGE);
				} catch (FileNotFoundException e) {
					System.out.println("text file could not be saved.");
					e.printStackTrace();
				} 
		    }
		}
	} //end of actionPerformed() method
}
