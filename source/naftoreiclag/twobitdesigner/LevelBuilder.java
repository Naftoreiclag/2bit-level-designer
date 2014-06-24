package naftoreiclag.twobitdesigner;

public class LevelBuilder
{
	public static final byte empty0 = 0x0;
	public static final byte empty1 = 0x1;
	public static final byte empty2 = 0x2;
	public static final byte empty3 = 0x3;
	public static final byte foreground0 = 0x4;
	public static final byte foreground1 = 0x5;
	public static final byte foreground2 = 0x6;
	public static final byte foreground3 = 0x7;
	public static final byte collision = 0xF;
	public static final byte[] empty = {empty0, empty1, empty2, empty3};
	public static final byte[] foreground = {foreground0, foreground1, foreground2, foreground3};
	
	public final int twidth;
	public final int theight;
	
	private final byte[][] tileData;
	private final byte[][] pixelData;
	
	public LevelBuilder(int twidth, int theight)
	{
		this.twidth = twidth;
		this.theight = theight;
		
		tileData = new byte[twidth][theight];
		pixelData = new byte[twidth << 3][theight << 3];
	}
	
	public void setPixel(int px, int py, byte color)
	{
		pixelData[px][py] = color;
	}
	
	public void setCollision(int tx, int ty) { tileData[tx][ty] = collision; }
	public void setForeground0(int tx, int ty) { tileData[tx][ty] = foreground0; }
	public void setForeground1(int tx, int ty) { tileData[tx][ty] = foreground1; }
	public void setForeground2(int tx, int ty) { tileData[tx][ty] = foreground2; }
	public void setForeground3(int tx, int ty) { tileData[tx][ty] = foreground3; }
	public void setForeground(int tx, int ty, int type) { tileData[tx][ty] = foreground[type]; }
	
	public void optimize()
	{
		for(int tx = 0; tx < twidth; ++ tx)
		{
			for(int ty = 0; ty < theight; ++ ty)
			{
				// If it's already a special tile
				if(tileData[tx][ty] == collision || 
					tileData[tx][ty] == foreground0 || 
					tileData[tx][ty] == foreground1 || 
					tileData[tx][ty] == foreground2 || 
					tileData[tx][ty] == foreground3)
				{
					continue;
				}

				// Test for an empty tile
				byte color = pixelData[(tx << 3) + 7][(ty << 3) + 7];
				boolean isSolidColor = true;
				for(int px = 0; px < 8; ++ px)
				{
					for(int py = 0; py < 8; ++ py)
					{
						if(pixelData[(tx << 3) + px][(ty << 3) + py] != color)
						{
							isSolidColor = false;
							break;
						}
					}
				}
				if(isSolidColor)
				{
					tileData[tx][ty] = empty[color];
					continue;
				}
				
				// Other tests?
			}
		}
	}
}
