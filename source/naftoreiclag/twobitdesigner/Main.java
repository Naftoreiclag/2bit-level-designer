package naftoreiclag.twobitdesigner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

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
		
		JScrollPane scrollPane = new JScrollPane(m);

		this.add(scrollPane);
	}
	
	public static void main(String[] args) throws Exception
	{
		/*
		Main m = new Main();
		m.setVisible(true);
		*/
		ObjectConverter oc = new ObjectConverter("testimages/tree");
		oc.saveAsFileMethod1();
	}
}
