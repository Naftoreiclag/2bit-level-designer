package naftoreiclag.twobitdesigner;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ObjectConverter
{
	public final String fileName;
	
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};
	
	public final int twidth;
	public final int theight;
	public final int pwidth;
	public final int pheight;
	
	public final int originX;
	public final int originY;
	
	public final int topPadding;
	public final int leftPadding;
	public final int bottomPadding;
	public final int rightPadding;
	
	public static final byte format = 0x00;
	
	private final boolean[][] collisionData;
	private final byte[][] pixelData;
	
	public ObjectConverter(String fileName)
	{
		this.fileName = fileName;
		
		BufferedImage imageMain = null;
		try
		{
			imageMain = ImageIO.read(new File(fileName + ".png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		BufferedImage imageCollision = null;
		try
		{
			imageCollision = ImageIO.read(new File(fileName + "_collision.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		int imageWidth = imageMain.getWidth();
		int imageHeight = imageMain.getHeight();
		
		if((imageWidth & 0x7) != 0 || (imageHeight & 0x7) != 0)
		{
			int crash = 1 / 0;
			System.out.println(crash);
		}
		
		twidth = imageWidth >> 3;
		theight = imageHeight >> 3;
		
		collisionData = new boolean[twidth][theight];
		
		int originX = 0;
		int originY = 0;
		for(int y = 0; y < theight; ++ y)
		{
			for(int x = 0; x < twidth; ++ x)
			{
				if((imageCollision.getRGB(x << 3, y << 3) & 0xFFFF0000) == 0xFFFF0000)
				{
					collisionData[x][y] = true;
				}
				if((imageCollision.getRGB(x << 3, y << 3) & 0xFF00FF00) == 0xFF00FF00)
				{
					originX = x;
					originY = y;
				}
			}
		}
		this.originX = originX;
		this.originY = originY;

		int topPad = -1;
		for(int y = 0; y < imageHeight; ++ y)
		{
			for(int x = 0; x < imageWidth; ++ x)
			{
				// If alpha is opaque
				if((imageMain.getRGB(x, y) & 0xff000000) != 0)
				{
					topPad = y;
					break;
				}
			}
			if(topPad != -1)
			{
				break;
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
				if((imageMain.getRGB(x, y) & 0xff000000) != 0)
				{
					leftPad = x;
					break;
				}
			}
			if(leftPad != -1)
			{
				break;
			}
		}
		
		int bottomPad = -1;
		for(int y = 0; y < imageHeight; ++ y)
		{
			for(int x = 0; x < imageWidth; ++ x)
			{
				// If alpha is opaque
				if((imageMain.getRGB(x, (imageHeight - y) - 1) & 0xff000000) != 0)
				{
					bottomPad = y;
					break;
				}
			}
			if(bottomPad != -1)
			{
				break;
			}
		}

		
		int rightPad = -1;
		for(int x = 0; x < imageWidth; ++ x)
		{
			for(int y = 0; y < imageHeight; ++ y)
			{
				// If alpha is opaque
				if((imageMain.getRGB((imageWidth - x) - 1, y) & 0xff000000) != 0)
				{
					rightPad = x;
					break;
				}
			}
			if(rightPad != -1)
			{
				break;
			}
		}
		
		//
		
		System.out.println("t: " + topPad + " l: " + leftPad + " b: " + bottomPad + " r: " + rightPad);
		
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
				pixelData[x][y] = getByteFromRGB(imageMain.getRGB(leftPadding + x, topPadding + y));
			}
		}
	}
	
	public void saveAsFileMethod2() throws Exception
	{
		List<Byte> bites = new ArrayList<Byte>();
		
		bites.add((byte) ObjectConverter.format);
		bites.add((byte) twidth);
		bites.add((byte) theight);
		bites.add((byte) originX);
		bites.add((byte) originY);
		bites.add((byte) ((leftPadding << 3) + topPadding));
		bites.add((byte) ((rightPadding << 3) + bottomPadding));

		// Collision Data
		
		int position = 0;
		byte buildAByte = 0;
		for(int ty = 0; ty < theight; ++ ty)
		{
			for(int tx = 0; tx < twidth; ++ tx)
			{
				if(collisionData[tx][ty])
				{
					buildAByte = (byte) (buildAByte | (1 << position));
				}
				
				++ position;
				
				if(position == 8)
				{
					bites.add(buildAByte);
					
					position = 0;
				}
			}
		}
		
		// Color Data
		
		byte color = pixelData[0][0];
		int size = 1;
		
		for(int py = 0; py < pheight; ++ py)
		{
			for(int px = 0; px < pwidth; ++ px)
			{
				if(px == 0 && py == 0)
				{
					continue;
				}
				
				if(color == 8)
				{
					if(color != pixelData[px][py] || size >= 127)
					{
						bites.add((byte) (size + 0x80));
						color = pixelData[px][py];
						size = 1;
						
						continue;
					}
				}
				else
				{
					if(color != pixelData[px][py] || size >= 15)
					{
						bites.add((byte) ((size << 3) + color));
						color = pixelData[px][py];
						size = 1;
						
						continue;
					}
				}
				
				++ size;
			}
		}
		if(color == 8)
		{
			bites.add((byte) (size + 0x80));
		}
		else
		{
			bites.add((byte) ((size << 3) + color));
		}
		
		// Writing

		byte[] data = new byte[bites.size()];
		for(int i = 0; i < bites.size(); ++ i)
		{
			data[i] = bites.get(i);
		}
		
		FileOutputStream fos;
		fos = new FileOutputStream(fileName + "_object_2");
		fos.write(data);
		fos.close();
		System.out.println("saved" + bites.size() + fileName + "_object_2");
	}
	
	// column-inconsiderate row-based
	/*
	public void saveAsFileMethod1() throws Exception
	{
		List<Byte> bites = new ArrayList<Byte>();
		
		bites.add((byte) 0);
		bites.add((byte) twidth);
		bites.add((byte) theight);

		byte color = 0;
		for(int py = 0; py < pheight; ++ py)
		{
			for(int px = 0; px < pwidth; ++ px)
			{
				if(pixelData[px][py] != 8)
				{
					color = pixelData[px][py];
				}
			}
		}
		int size = 1;
		
		for(int py = 0; py < pheight; ++ py)
		{
			for(int px = 0; px < pwidth; ++ px)
			{
				if(px == 0 && py == 0)
				{
					continue;
				}
				
				if(size >= 31)
				{
					bites.add((byte) ((size << 3) + color));
					
					if(pixelData[px][py] != 8)
					{
						color = pixelData[px][py];
					}
					size = 1;
					
					continue;
				}
				
				if(pixelData[px][py] == 8)
				{
					++ size;
					continue;
				}
				
				if(color != pixelData[px][py])
				{
					bites.add((byte) ((size << 3) + color));
					color = pixelData[px][py];
					size = 1;
					
					continue;
				}
				
				++ size;
			}
		}
		bites.add((byte) (color + (size << 3)));
		
		int position = 0;
		byte buildAByte = 0;
		for(int ty = 0; ty < theight; ++ ty)
		{
			for(int tx = 0; tx < twidth; ++ tx)
			{
				if(collisionData[tx][ty])
				{
					buildAByte = (byte) (buildAByte | (1 << position));
				}
				
				++ position;
				
				if(position == 8)
				{
					bites.add(buildAByte);
					
					position = 0;
				}
			}
		}
		
		////////////////////

		byte[] data = new byte[bites.size()];
		for(int i = 0; i < bites.size(); ++ i)
		{
			data[i] = bites.get(i);
		}
		
		FileOutputStream fos;
		fos = new FileOutputStream(fileName + "_object_1");
		fos.write(data);
		fos.close();
		System.out.println("saved" + bites.size() + fileName + "_object_1");
	}
	*/
	
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
