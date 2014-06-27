package naftoreiclag.twobitdesigner;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

	
	// column-inconsiderate row-based
	public void saveAsFileMethod5(String fileName) throws Exception
	{
		List<Byte> bites = new ArrayList<Byte>();
		
		int pheight = theight << 3;
		int pwidth = twidth << 3;
		
		bites.add(format);
		bites.add((byte) twidth);
		bites.add((byte) theight);

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
				
				if(color != pixelData[px][py] || size >= 31)
				{
					bites.add((byte) ((size << 3) + color));
					color = pixelData[px][py];
					size = 1;
				}
				else
				{
					++ size;
				}
			}
		}
		bites.add((byte) (color + (size << 3)));
		
		////////////////////

		byte[] data = new byte[bites.size()];
		for(int i = 0; i < bites.size(); ++ i)
		{
			data[i] = bites.get(i);
		}
		
		FileOutputStream fos;
		fos = new FileOutputStream(fileName);
		fos.write(data);
		fos.close();
	}
	
	// optimized row-based
	public void saveAsFileMethod4(String fileName) throws Exception
	{
		List<Byte> bites = new ArrayList<Byte>();
		
		int rowTypes = 0;
		
		int pheight = theight << 3;
		int pwidth = twidth << 3;
		
		bites.add(format);
		bites.add((byte) twidth);
		bites.add((byte) theight);

		System.out.println("Optimizations on row-based: ");
		for(int py = 0; py < pheight; ++ py)
		{
			List<Byte> rowBites = new ArrayList<Byte>();
			
			int size = 1;
			byte color = pixelData[0][0];
			for(int px = 0; px < pwidth; ++ px)
			{
				if(px == 0 && py == 0)
				{
					continue;
				}
				
				if(color != pixelData[px][py] || size >= 31)
				{
					rowBites.add((byte) (color + (size << 3)));
					color = pixelData[px][py];
					size = 0;
				}
				else
				{
					++ size;
				}
			}
			
			// Using the compression will be worse
			if(rowBites.size() > (pwidth >> 3) * 3)
			{
				System.out.println("Could have been optimized on row " + py);
				
				// Just save the plain data
				for(int tx = 0; tx < twidth; ++ tx)
				{
					int why = py;
					int ehx = (tx << 3);
					
					bites.add((byte) ((pixelData[ehx + 0][why] << 5) ^ (pixelData[ehx + 1][why] << 2) ^ (pixelData[ehx + 2][why] >> 1)));
					bites.add((byte) ((pixelData[ehx + 2][why] << 7) ^ (pixelData[ehx + 3][why] << 4) ^ (pixelData[ehx + 4][why] << 1) ^ (pixelData[ehx + 5][why] >> 2)));
					bites.add((byte) ((pixelData[ehx + 5][why] << 6) ^ (pixelData[ehx + 6][why] << 3) ^  pixelData[ehx + 7][why]));
				}
			}
			
			// Using the compression will be better
			else
			{
				rowBites.add((byte) (color + (size << 3)));
				
				//rowTypes = rowTypes | (0x1 << py);
				
				for(Byte b : rowBites)
				{
					bites.add(b);
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
		fos = new FileOutputStream(fileName);
		fos.write(data);
		fos.close();
	}
	
	// row-based
	public void saveAsFileMethod3(String fileName) throws Exception
	{
		List<Byte> bites = new ArrayList<Byte>();
		
		int pheight = theight << 3;
		int pwidth = twidth << 3;
		
		bites.add(format);
		bites.add((byte) twidth);
		bites.add((byte) theight);
		
		for(int py = 0; py < pheight; ++ py)
		{
			int size = 1;
			byte color = pixelData[0][py];
			for(int px = 1; px < pwidth; ++ px)
			{
				
				if(color != pixelData[px][py] || size >= 31)
				{
					bites.add((byte) ((size << 3) + color));
					color = pixelData[px][py];
					size = 1;
				}
				else
				{
					++ size;
				}
			}
			bites.add((byte) (color + (size << 3)));
		}
		
		////////////////////

		byte[] data = new byte[bites.size()];
		for(int i = 0; i < bites.size(); ++ i)
		{
			data[i] = bites.get(i);
		}
		
		FileOutputStream fos;
		fos = new FileOutputStream(fileName);
		fos.write(data);
		fos.close();
	}
	
	// raw
	public void saveAsFileMethod2(String fileName) throws Exception
	{
		byte[] data = new byte[(((twidth * theight) << 6) >> 3) * 3];
		
		int position = 0;
		
		for(int ty = 0; ty < theight; ++ ty)
		{
			for(int tx = 0; tx < twidth; ++ tx)
			{
				// Save decal data
				for(int py = 0; py < 8; ++ py)
				{
					int why = (ty << 3) + py;
					int ehx = (tx << 3);
					
					data[position ++] = (byte) ((pixelData[ehx + 0][why] << 5) ^ (pixelData[ehx + 1][why] << 2) ^ (pixelData[ehx + 2][why] >> 1));
					data[position ++] = (byte) ((pixelData[ehx + 2][why] << 7) ^ (pixelData[ehx + 3][why] << 4) ^ (pixelData[ehx + 4][why] << 1) ^ (pixelData[ehx + 5][why] >> 2));
					data[position ++] = (byte) ((pixelData[ehx + 5][why] << 6) ^ (pixelData[ehx + 6][why] << 3) ^  pixelData[ehx + 7][why]);
				}
			}
		}

		FileOutputStream fos;
		fos = new FileOutputStream(fileName);
		fos.write(data);
		fos.close();
	}
	
	public void saveAsFileMethod1(String fileName) throws Exception
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
					data[position ++] = (byte) (tileData[tx][ty] << 4);
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
				if(tileData[tx][ty] < decal)
				{
					// Don't save any decal data
					continue;
				}
				
				// Save decal data
				for(int py = 0; py < 8; ++ py)
				{
					int why = (ty << 3) + py;
					int ehx = (tx << 3);
					
					//ret[0] = (byte) ((data[0] << 5) ^ (data[1] << 2) ^ (data[2] >> 1));
					//ret[1] = (byte) ((data[2] << 7) ^ (data[3] << 4) ^ (data[4] << 1) ^ (data[5] >> 2));
					//ret[2] = (byte) ((data[5] << 6) ^ (data[6] << 3) ^  data[7]);
					
					data[position ++] = (byte) ((pixelData[ehx + 0][why] << 5) ^ (pixelData[ehx + 1][why] << 2) ^ (pixelData[ehx + 2][why] >> 1));
					data[position ++] = (byte) ((pixelData[ehx + 2][why] << 7) ^ (pixelData[ehx + 3][why] << 4) ^ (pixelData[ehx + 4][why] << 1) ^ (pixelData[ehx + 5][why] >> 2));
					data[position ++] = (byte) ((pixelData[ehx + 5][why] << 6) ^ (pixelData[ehx + 6][why] << 3) ^  pixelData[ehx + 7][why]);
				}
			}
		}
		
		System.out.println("Number of tiles: " + numTiles);
		System.out.println("Number of unique tiles: " + numDecalTiles);
		
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
		
		for(int i = 0; i < 8; ++ i)
		{
			System.out.println("Color " + i + " do of the pixels count " + debug_counts[i]);
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

	public static BufferedImage debugImageMethod3(String string) throws Exception
	{
		byte data[] = Files.readAllBytes(Paths.get(string));
		
		byte type = data[0];
		
		int twidth = data[1] & 0xff;
		int theight = data[2] & 0xff;
		
		int pwidth = twidth << 3;
		int pheight = theight << 3;
		
		BufferedImage ret = new BufferedImage(pwidth, pheight, BufferedImage.TYPE_INT_RGB);
		
		int ehx = 0;
		int why = 0;
		for(int i = 3; i < data.length; ++ i)
		{
			byte color = (byte) (data[i] & 0x07);
			int width = (data[i] & 0xff) >> 3;
			
			for(int x = 0; x < width; ++ x)
			{
				//System.out.println((ehx + x) + " " + why);
				
				ret.setRGB(ehx + x, why, pallete[color] | 0xFF000000);
			}
		
			ehx += width;
			
			if(ehx >= pwidth)
			{
				ehx = 0;
				++ why;
			}
		}
		
		return ret;
	}
	

	public static BufferedImage debugImageMethod5(String string) throws Exception
	{
		byte data[] = Files.readAllBytes(Paths.get(string));
		
		byte type = data[0];
		
		int twidth = data[1] & 0xff;
		int theight = data[2] & 0xff;
		
		System.err.println(twidth + ", " + theight);
		
		int pwidth = twidth << 3;
		int pheight = theight << 3;
		
		System.err.println(pwidth + ", " + pheight);
		
		BufferedImage ret = new BufferedImage(pwidth, pheight, BufferedImage.TYPE_INT_RGB);
		
		int ehx = 0;
		int why = 0;
		
		for(int i = 3; i < data.length; ++ i)
		{
			byte color = (byte) (data[i] & 0x07);
			int width = (data[i] & 0xff) >> 3;
		
			System.out.println("clr: " + color + " width: " + width);
			
			for(int x = 0; x < width; ++ x)
			{
				ret.setRGB(ehx ++, why, pallete[color] | 0xFF000000);

				if(ehx >= pwidth)
				{
					ehx = 0;
					++ why;
				}
			}
		}
		
		return ret;
	}
}
