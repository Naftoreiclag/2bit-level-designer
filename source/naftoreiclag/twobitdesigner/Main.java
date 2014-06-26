package naftoreiclag.twobitdesigner;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Main extends JFrame
{
	private Main() throws Exception
	{
		super("Pig Collision Demo");
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);

		DoNotInclude.printBytes(DoNotInclude.encodeTest(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07}));
		
		MainPanel m = new MainPanel();
		
		
		
		this.add(m);
	}
	
	public static void main(String[] args) throws Exception
	{
		
		
		Main m = new Main();
		m.setVisible(true);
	}
}
