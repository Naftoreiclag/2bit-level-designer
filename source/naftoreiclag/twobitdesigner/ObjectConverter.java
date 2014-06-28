package naftoreiclag.twobitdesigner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ObjectConverter
{
	String fileName;
	
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};
	
	public final int twidth;
	public final int theight;
	public final int pwidth;
	public final int pheight;
	
	public final int topPadding;
	public final int leftPadding;
	public final int bottomPadding;
	public final int rightPadding;
	
	private final boolean[][] collisionData;
	private final byte[][] pixelData;
	
	public ObjectConverter(String fileName)
	{
		BufferedImage image = null;
		try
		{
			image = ImageIO.read(new File(fileName + ".png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		
		if((imageWidth & 0x7) != 0 || (imageHeight & 0x7) != 0)
		{
			int crash = 1 / 0;
			System.out.println(crash);
		}
		
		twidth = imageWidth >> 3;
		theight = imageHeight >> 3;
		
		collisionData = new boolean[twidth][theight];

		int topPad = -1;
		for(int y = 0; y < imageHeight; ++ y)
		{
			for(int x = 0; x < imageWidth; ++ x)
			{
				// If alpha is opaque
				if((image.getRGB(x, y) & 0xff000000) != 0)
				{
					topPad = y;
				}
			}
		}
		if(topPad == -1)
		{
			int crash = 1 / 0;
			System.out.println(crash);
		}
		
		int leftPad = -1;
		for(int x = 0; x < imageWidth; ++ x)
		{
			for(int y = 0; y < imageHeight; ++ y)
			{
				// If alpha is opaque
				if((image.getRGB(x, y) & 0xff000000) != 0)
				{
					leftPad = x;
				}
			}
		}
		
		int bottomPad = -1;
		for(int y = 0; y < imageHeight; ++ y)
		{
			for(int x = 0; x < imageWidth; ++ x)
			{
				// If alpha is opaque
				if((image.getRGB(x, (imageHeight - y) - 1) & 0xff000000) != 0)
				{
					bottomPad = y;
				}
			}
		}

		
		int rightPad = -1;
		for(int x = 0; x < imageWidth; ++ x)
		{
			for(int y = 0; y < imageHeight; ++ y)
			{
				// If alpha is opaque
				if((image.getRGB((imageWidth - x) - 1, y) & 0xff000000) != 0)
				{
					rightPad = x;
				}
			}
		}
		
		//
		
		this.topPadding = topPad;
		this.leftPadding = leftPad;
		this.bottomPadding = bottomPad;
		this.rightPadding = rightPad;
		
		pwidth = ((twidth << 3) - leftPadding) - rightPadding;
		pheight = ((theight << 3) - topPadding) - bottomPadding;
		
		pixelData = new byte[pwidth][pheight];
		
		//
		
		for(int x = 0; x < pwidth; ++ x)
		{
			for(int y = 0; y < pheight; ++ y)
			{
				pixelData[x][y] = getByteFromRGB(image.getRGB(leftPadding + x, topPadding + y));
			}
		}
	}
	
	public static byte getByteFromRGB(int rgb)
	{
		if((rgb & 0xFF000000) == 0)
		{
			return 8;
		}
		
		rgb = rgb & 0x00FFFFFF;
		//System.out.println(Helper.hexCode(rgb));
		
		if(rgb < pallete[1])
		{
			return 0;
		}
		else if(rgb < pallete[2])
		{
			return 1;
		}
		else if(rgb < pallete[3])
		{
			return 2;
		}
		else if(rgb < pallete[4])
		{
			return 3;
		}
		else if(rgb < pallete[5])
		{
			return 4;
		}
		else if(rgb < pallete[6])
		{
			return 5;
		}
		else if(rgb < pallete[7])
		{
			return 6;
		}
		else
		{
			return 7;
		}
	}
}
