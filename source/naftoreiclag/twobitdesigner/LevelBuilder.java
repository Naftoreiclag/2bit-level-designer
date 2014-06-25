package naftoreiclag.twobitdesigner;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

// Format 0

public class LevelBuilder
{
	public static final byte format = 0x00;
	
	/*
	public static final byte empty0 = 0x0;
	public static final byte empty1 = 0x1;
	public static final byte empty2 = 0x2;
	public static final byte empty3 = 0x3;
	public static final byte foreground0 = 0x4;
	public static final byte foreground1 = 0x5;
	public static final byte foreground2 = 0x6;
	public static final byte foreground3 = 0x7;
	public static final byte unique = 0x8;
	public static final byte collision = 0xF;
	*/
	
	public static final byte empty0 = 0x0;
	public static final byte empty1 = 0x1;
	public static final byte empty2 = 0x2;
	public static final byte empty3 = 0x3;
	public static final byte empty4 = 0x4;
	public static final byte empty5 = 0x5;
	public static final byte empty6 = 0x6;
	public static final byte empty7 = 0x7;
	public static final byte decal = 0x8;
	public static final byte collision = 0x9;
	public static final byte foreground0 = 0xA;
	public static final byte foreground1 = 0xB;
	public static final byte foreground2 = 0xC;
	public static final byte[] empty = {empty0, empty1, empty2, empty3, empty4, empty5, empty6, empty7};
	public static final byte[] foreground = {foreground0, foreground1, foreground2};
	
	public static final int[] pallete = {0x000000, 0x333333, 0x555555, 0x777777, 0x999999, 0xBBBBBB, 0xDDDDDD, 0xFFFFFF};
	
	public final int twidth;
	public final int theight;
	
	private final byte[][] tileData;
	private final byte[][] pixelData;

	public int[] debug_counts = new int[8];
	
	private int numDecalTiles;
	
	public LevelBuilder(int twidth, int theight)
	{
		this.twidth = twidth;
		this.theight = theight;
		
		tileData = new byte[twidth][theight];
		for(int tx = 0; tx < twidth; ++ tx)
		{
			for(int ty = 0; ty < theight; ++ ty)
			{
				tileData[tx][ty] = empty0;
			}
		}
		
		pixelData = new byte[twidth << 3][theight << 3];
	}
	
	public void saveAsFile(String fileName) throws Exception
	{
		this.optimize();
		
		int numTiles = twidth * theight;
		int numBytesOfTiles = (numTiles >> 1) + (numTiles & 1);
		
		byte[] data = new byte[1 + 2 + numBytesOfTiles + (numDecalTiles * 24)];
		
		data[0] = format;
		data[1] = (byte) twidth;
		data[2] = (byte) theight;
		
		int position = 3;
		
		for(int ty = 0; ty < theight; ++ ty)
		{
			for(int tx = 0; tx < twidth; ++ tx)
			{
				// If the sum of ty and tx is even
				if(((ty + tx) & 1) == 0)
				{
					data[position] = (byte) (tileData[tx][ty] << 4);
					++ position;
				}
				
				// The sum of ty and tx is odd
				else
				{
					data[position] = (byte) (data[position] & tileData[tx][ty]);
				}
			}
		}

		for(int ty = 0; ty < theight; ++ ty)
		{
			for(int tx = 0; tx < twidth; ++ tx)
			{
				// If it is an empty tile
				if(tileData[tx][ty] <= decal)
				{
					// Don't save any decal data
					continue;
				}
				
				// Save decal data
				for(int py = 0; py < 8; ++ py)
				{
					data[position] = (byte) ((pixelData[0][py] << 5) + (pixelData[1][py] << 2) + (pixelData[2][py] >> 1));
					++ position;
					data[position] = (byte) ((pixelData[2][py] << 7) + (pixelData[3][py] << 4) + (pixelData[4][py] << 1) + (pixelData[4][py] >> 2));
					++ position;
					data[position] = (byte) ((pixelData[4][py] << 6) + (pixelData[5][py] << 3) + pixelData[6][py]);
					++ position;
				}
			}
		}
		
		System.out.println("Number of tiles: " + numTiles);
		System.out.println("Number of unique tiles: " + numDecalTiles);
		for(int i = 0; i < 8; ++ i)
		{
			System.out.println("Color " + i + " do of the pixels count " + debug_counts[i]);
		}
		DoNotInclude.printBytes(data);
		
		FileOutputStream fos;
		fos = new FileOutputStream(fileName);
		fos.write(data);
		fos.close();
		
		System.out.println("hello");
	}
	
	public void setPixelsFromImage(BufferedImage image)
	{
		for(int px = 0; px < twidth << 3; ++ px)
		{
			for(int py = 0; py < theight << 3; ++ py)
			{
				byte b = getByteFromRGB(image.getRGB(px, py));
				
				++ debug_counts[b];
				
				pixelData[px][py] = b;
			}
		}
	}
	
	public static byte getByteFromRGB(int rgb)
	{
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
	
	public void setPixel(int px, int py, byte color)
	{
		pixelData[px][py] = color;
	}
	
	public void setCollision(int tx, int ty) { tileData[tx][ty] = collision; }
	/*
	public void setForeground0(int tx, int ty) { tileData[tx][ty] = foreground0; }
	public void setForeground1(int tx, int ty) { tileData[tx][ty] = foreground1; }
	public void setForeground2(int tx, int ty) { tileData[tx][ty] = foreground2; }
	public void setForeground3(int tx, int ty) { tileData[tx][ty] = foreground3; }
	public void setForeground(int tx, int ty, int type) { tileData[tx][ty] = foreground[type]; }
	*/
	
	public void optimize()
	{
		numDecalTiles = 0;

		for(int ty = 0; ty < theight; ++ ty)
		{
			for(int tx = 0; tx < twidth; ++ tx)
			{
				// If it's already a special tile
				if(tileData[tx][ty] >= collision)
				{
					continue;
				}

				// Test for an empty tile
				byte color = pixelData[(tx << 3) + 7][(ty << 3) + 7];
				boolean isUnique = false;
				for(int px = 0; px < 8; ++ px)
				{
					for(int py = 0; py < 8; ++ py)
					{
						if(pixelData[(tx << 3) + px][(ty << 3) + py] != color)
						{
							isUnique = true;
							break;
						}
					}
					
					if(isUnique)
					{
						break;
					}
				}
				if(isUnique)
				{
					++ numDecalTiles;
					tileData[tx][ty] = decal;
					continue;
				}
				else
				{
					tileData[tx][ty] = empty[color];
				}
				
				// Other tests?
			}
		}
	}
}
