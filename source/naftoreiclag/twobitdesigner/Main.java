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

		System.out.println("lofewjgfioew");
		MainPanel m = new MainPanel();
		
		this.add(m);
	}
	
	public static void main(String[] args) throws Exception
	{
		
		System.out.println("lofewjgfioew");
		
		Main m = new Main();
		m.setVisible(true);
	}
}
