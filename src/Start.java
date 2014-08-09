/* IMAGE STEGANOGRAPHY : Start.java
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
 * >>Start.java is a basic splash screen code with no impact
 * on the functional purpose of the Simple Steganography program.
 * >>Start.java calls the constructor class from Login.java
 */

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JWindow;

public class Start extends JWindow implements Runnable{

	private static final long serialVersionUID = -1348168212720533419L;
	
	//Constructor to create an window of size equal to splash image
	Start() {
		setVisible(true);
		setSize(675,250);
		setLocationRelativeTo(null);
		
		Thread t = new Thread(this);
		t.start();
	} //end of Start() constructor
	
	public void run() {
		try {
			//Splash screen for 5 seconds
			Thread.sleep(5000);
			//Close splash screen image
			dispose();
			//Start the authentication process for using the application
			new Login();
		} catch (InterruptedException intEx) {
			System.out.print("Process interrupted abnormally!");
			System.exit(1);
		}
	} //end of run()
	
	//over-riding paint() method for displaying splash screen image
	public void paint(Graphics g) {
		Image splashScreen = null;
		try {
			splashScreen = ImageIO.read(getClass().getResource("images/start_splash.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Image splashScreen = Toolkit.getDefaultToolkit().getImage("images\\start_splash.jpg");
		//Displaying splash screen at the center position of the screen
		int xPosition = (this.getWidth() - splashScreen.getWidth(null)) / 2;
		int yPosition = (this.getHeight() - splashScreen.getHeight(null)) / 2;
		g.drawImage(splashScreen, xPosition, yPosition, this);
	} //end of paint()
		
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Start newInstance = new Start();
	} //end of main()
} //end of Start class
