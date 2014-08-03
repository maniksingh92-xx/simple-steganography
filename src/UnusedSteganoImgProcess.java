/* SIMPLE STEGANOGRAPHY : UnusedSteganoImgProcess.java
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
 * >> UnusedSteganoImgProcess.java is an unfinished attempt
 * at encoding data in the two LSB of each red, green and
 * blue. This would effectively allow for larger amount of
 * text to be encoded inside an image.
 * 
 * 
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.imageio.ImageIO;

public class UnusedSteganoImgProcess {
	
	void encode(BufferedImage input, BufferedImage output, int width, int height, String msg, String outputName) {
		
		String message = msg + "!";
		int msgLength = message.length();
		char currentChar;
		int count = 0;
		
		int[] twoBitMessage = new int[4 * msgLength];
		
		for(int i =0; i < msgLength ; i++) {
			currentChar = message.charAt(i);
			twoBitMessage[4*i + 0] = (currentChar >> 6) & 0x3;
			twoBitMessage[4*i + 1] = (currentChar >> 4) & 0x3;
			twoBitMessage[4*i + 2] = (currentChar >> 2) & 0x3;
			twoBitMessage[4*i + 3] = (currentChar) & 0x3;
		}
		
		
		int pixel, alpha, red, green, blue, aOut, rOut, gOut, bOut, pixOut;
		loop: for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				if(count < 4*msgLength) {
					pixel = input.getRGB(i, j);
					
					alpha = (pixel >> 24) & 0xff;
					red = (pixel >> 16) & 0xff;
					green = (pixel >> 8) & 0xff;
					blue = (pixel >> 0) & 0xff;
					
					System.out.println("al" + alpha);
					System.out.println(red);
					System.out.println(green);
					System.out.println(blue);
					
					rOut = (red & 0xfc) | twoBitMessage[count++];
					if(count == 4*msgLength) {
						break loop;
					}
					gOut = (green & 0xfc) | twoBitMessage[count++];
					if(count == 4*msgLength) {
						break loop;
					}
					bOut = (blue & 0xfc) | twoBitMessage[count++];
					aOut = alpha;
					
					System.out.println(rOut);
					System.out.println(gOut);
					System.out.println(bOut);
					System.out.println(aOut);
					
					pixOut = (aOut << 24) | (rOut << 16) | (gOut << 8) | (bOut);
					
					System.out.println("ths " + pixOut);
					
					output.setRGB(i, j, pixOut);
					
				} else {
					break loop;
				}
			}
			
		}
		
		try {
			ImageIO.write(output, "png", new File(outputName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	String decode(BufferedImage input, int width, int height) {
		
		StringBuffer decodedMsg = new StringBuffer();
		Deque<Integer> listChar = new ArrayDeque<Integer>();
		
		int pixel, red, green, blue, rOut, gOut, bOut, charOut;
		loop: for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				pixel = input.getRGB(i, j);
				
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel >> 0) & 0xff;
				
				rOut = red & 0x03;
				gOut = green & 0x03;
				bOut = blue & 0x03;
				
				listChar.add(rOut);
				listChar.add(gOut);
				listChar.add(bOut);
				
				if(listChar.size() >=4) {
					charOut = (listChar.pop() << 6) | (listChar.pop() << 4) | (listChar.pop() << 2) | listChar.pop() ;
					if((char)charOut == '!') {
						break loop;
					} else {
						decodedMsg.append((char)charOut);
					}
				}
			}
			
		}
		
		String outputMsg = new String(decodedMsg);
		
		return outputMsg;
		
	}
}