/* SIMPLE STEGANOGRAPHY : Login.java
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
 * >>Login.java is a rudimentary authentication code with no impact
 * on the functional purpose of the Simple Steganography program.
 * >>Login.java calls the constructor class from SteganoMain.java
 * 
 * Login.java is a just a basic username and password authenticator
 * only created for the purpose of an example. It gives us an idea
 * about how and where an actual authentication process could be 
 * implemented in the program.
 */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


class Login extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = -3010597082691149848L;
	
	final JTextField nameInput, passInput;
	JLabel username, password;
	JButton submit;
	JPanel panel;
	
	Login() {
		//Create field for "Username:" input
		username = new JLabel();
		username.setText("USERNAME  :");
		username.setForeground(Color.WHITE);
		nameInput = new JTextField(15);
		
		//Create field for "Password:" input
		password = new JLabel();
		password.setText("PASSWORD :");
		password.setForeground(Color.WHITE);
		passInput = new JPasswordField(15);
		
		//SUBMIT button. User can also press ENTER on keyboard to submit credential details.
		submit = new JButton("SUBMIT");
		submit.setMnemonic(KeyEvent.VK_ENTER);
		getRootPane().setDefaultButton(submit);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel() {
			private static final long serialVersionUID = 7202207766947468085L;
			
			//setting background
			public void paintComponent(Graphics g) {
				Image background = Toolkit.getDefaultToolkit().getImage("images\\login_background.jpg");
		        g.drawImage(background,0,0,this);
			} //end of paintComponent()
		};
		
		//Creating the panel and adding all elements
		panel.setLayout(null);
		username.setBounds(150,125,150,50);
		nameInput.setBounds(230,135,150,30);
		password.setBounds(150,175,150,50);
		passInput.setBounds(230,185,150,30);
		submit.setBounds(230,230,150,30);
		panel.add(username);
		panel.add(nameInput);
		panel.add(password);
		panel.add(passInput);
		panel.add(submit);
		submit.addActionListener(this);
		add(panel);
		
		//modifying attributes of login window
		setResizable(false);
	    setSize(640, 480);
	    setVisible(true);
	    setLocation(350,100);
		setTitle("LOGIN FORM");
	} //end of Login() constructor
	
	public void actionPerformed(ActionEvent submitCredentials) {
		String name = nameInput.getText();
		String pass = passInput.getText();
		//Authentication process
		if (name.equals("admin") && pass.equals("password")) {
			SteganoMain page = new SteganoMain();
			dispose();
			page.setVisible(true);
		} else {
			System.out.println("enter the valid username and password");
			JOptionPane.showMessageDialog(this,"Incorrect login or password","Error",JOptionPane.ERROR_MESSAGE);
		}
	} //end of actionPerformed()

} //end of Login Class
