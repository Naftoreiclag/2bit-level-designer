package naftoreiclag.twobitdesigner;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class MainPanel extends JPanel
{
	Aaaaaaaaa aaa;
	
	/*
	public void testComp(String fileName) throws Exception
	{
		image = ImageIO.read(new File(fileName + ".png"));
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		
		LevelBuilder b = new LevelBuilder(imageWidth >> 3, imageHeight >> 3);
		
		b.setPixelsFromImage(image);

		b.saveAsFileMethod1(fileName + "_compress_1_tiles.map");
		b.saveAsFileMethod2(fileName + "_compress_2_raw.map");
		b.saveAsFileMethod3(fileName + "_compress_3_strip.map");
		b.saveAsFileMethod4(fileName + "_compress_4_strip_optimized.map");
		b.saveAsFileMethod5(fileName + "_compress_5_strip_inconsiderate.map");
	}
	*/

	public BufferedImage image;
	
	public JLabel picLabel;
	public static class Aaaaaaaaa extends JPanel
	{
		public BufferedImage image;
		
		public Aaaaaaaaa() throws Exception
		{
			this.setSize(500, 500);
			image = LevelBuilder.debugImageMethod5("testimages/jellystatue2_compress_5_strip_inconsiderate.map");
		}
		
		@Override
		public void paint(Graphics g)
		{
			Graphics2D g2 = (Graphics2D) g;
			
			g2.setColor(Color.WHITE);
			g2.drawImage(image, 0, 0, null);
		}
	}
	
	public MainPanel() throws Exception
	{
		this.setSize(500, 500);
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		image = LevelBuilder.debugImageMethod5("testimages/jellystatue2_compress_5_strip_inconsiderate.map");
		
		picLabel = new JLabel(new ImageIcon(image));
		
		
		this.add(picLabel);
		
		/*
		aaa = new Aaaaaaaaa();
		
		JScrollPane scrollPane = new JScrollPane(aaa);
		
		this.add(scrollPane);
		*/
		
		/*
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter name of file minus file extension: ");
		String fileName = "park";//reader.nextLine();
		reader.close();
		*/
		/*
		testComp("testimages/bad");
		testComp("testimages/jellystatue2");
		testComp("testimages/ScrittlShip");
		testComp("testimages/park");
		
		imageReconstru1 = LevelBuilder.debugImageMethod3("testimages/park_compress_3_strip.map");
		imageReconstru2 = LevelBuilder.debugImageMethod5("testimages/park_compress_5_strip_inconsiderate.map");
		
		System.exit(0);
		*/
		
		/*
		File outputFile = new File(fileName + "_test.png");
		ImageIO.write(image, "png", outputFile);
		File outputFile2 = new File(fileName + "_test.bmp");
		ImageIO.write(image, "bmp", outputFile2);
		*/
		
		this.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e){ mMove(e); }
			@Override
			public void mouseMoved(MouseEvent e){ mMove(e); }
		});
		
		this.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e) { kPress(e); }
			@Override
			public void keyReleased(KeyEvent e) { kRelease(e); }
			@Override
			public void keyTyped(KeyEvent e) { }
		});
		
		this.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseClicked(MouseEvent e) { }
			@Override
			public void mouseEntered(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) { }
			@Override
			public void mousePressed(MouseEvent e) { mPress(e); }
			@Override
			public void mouseReleased(MouseEvent e) { mRelease(e); }
		});
	}
	private void mMove(MouseEvent e)
	{
	}
	private void mPress(MouseEvent e)
	{
	}
	private void mRelease(MouseEvent e)
	{
	}
	private void kPress(KeyEvent e)
	{
	}
	private void kRelease(KeyEvent e)
	{
	}
}
