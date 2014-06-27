package naftoreiclag.twobitdesigner;

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
		
		MainPanel m = new MainPanel();

		this.add(m);
	}
	
	public static void main(String[] args) throws Exception
	{
		Main m = new Main();
		m.setVisible(true);
	}
}
