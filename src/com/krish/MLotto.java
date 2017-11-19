package com.krish;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

public class MLotto implements Printable {
	final Component comp;
	static int noOfTickets = 8;
	static List<String> songsList = new LinkedList<String>();

	public static void main(String[] args) {
		String songsDir = "C:\\Krish\\Music\\AllTime";
		 File file = new File(songsDir);
		String[] fileList = file.list();
		for (String name : fileList) {
			//System.out.println(name);
			name = name.replace(".mp3", " ");
			songsList.add(name);
		}
		Collections.shuffle(songsList);
		/*try {
		FileWriter writer = new FileWriter("C:\\\\Krish\\\\Music\\\\AllTime\\1111.txt"); 
		for(String str: songsList) {
			writer.write(str);
			writer.write("\n");
		}
		writer.close();	
		}catch(Exception e) {
			e.printStackTrace();
		}*/
		generateTickets();
	}

	private static void generateTickets() {
		JFrame yourComponent = new JFrame();
		JTable table;
		// add the table to the frame
		JPanel mypannel = new JPanel();
		mypannel.setLayout(new FlowLayout());
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printme(yourComponent);
			}
		});
		mypannel.add(okButton);
		for (int i = 1; i <= noOfTickets; i++) {
			table = getTicketTable();
			mypannel.add(table);
			mypannel.add(Box.createRigidArea(new Dimension(5,80)));
		}
		yourComponent.add(mypannel);
		yourComponent.setTitle("Musical Lotto Ticket");
		yourComponent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		yourComponent.pack();
		yourComponent.setVisible(true);
		
	}

	private static void printme(JFrame yourComponent) {
		PrinterJob pjob = PrinterJob.getPrinterJob();
		PageFormat preformat = pjob.defaultPage();
		preformat.setOrientation(PageFormat.LANDSCAPE);
		PageFormat postformat = pjob.pageDialog(preformat);
		// If user does not hit cancel then print.
		if (preformat != postformat) {
			// Set print component
			pjob.setPrintable(new MLotto(yourComponent), postformat);
			if (pjob.printDialog()) {
				try {
					pjob.print();
				} catch (PrinterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public MLotto(Component comp) {
		this.comp = comp;
	}

	private static JTable getTicketTable() {

		// headers for the table
		String[] columns = new String[] { "", "", "", "", "" };

		// actual data for the table in a 2d array
		Object[][] data = getTicketData();

		// create table with data
		JTable table = new JTable(data, columns);
		table.getColumnModel().getColumn(0).setPreferredWidth(160);
		table.getColumnModel().getColumn(1).setPreferredWidth(160);
		table.getColumnModel().getColumn(2).setPreferredWidth(160);
		table.getColumnModel().getColumn(3).setPreferredWidth(160);
		table.getColumnModel().getColumn(4).setPreferredWidth(160);
		// table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}

	private static Object[][] getTicketData() {
				
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String ticketID  = dateFormat.format(new Date());
		
		Object[][] ticketData = new Object[][] {
				{ "Musical_Lotto", "Developed By:", "krishnabab@gmail.com", "TicketID:", ticketID },
				{ getOneRandomSong(), getOneRandomSong(), getOneRandomSong(), getOneRandomSong(), getOneRandomSong() },
				{ getOneRandomSong(), getOneRandomSong(), getOneRandomSong(), getOneRandomSong(), getOneRandomSong() },
				{ getOneRandomSong(), getOneRandomSong(), getOneRandomSong(), getOneRandomSong(), getOneRandomSong() }, };

		return ticketData;
	}

	private static String getOneRandomSong() {
		String song = songsList.get(0);
		songsList.remove(0);
		Collections.shuffle(songsList);
		return song;
	}

	@Override
	public int print(Graphics graphics, PageFormat format, int page_index) throws PrinterException {
		if (page_index > 0) {
			return Printable.NO_SUCH_PAGE;
		}

		// get the bounds of the component
		Dimension dim = comp.getSize();
		double cHeight = dim.getHeight();
		double cWidth = dim.getWidth();

		// get the bounds of the printable area
		double pHeight = format.getImageableHeight();
		double pWidth = format.getImageableWidth();

		double pXStart = format.getImageableX();
		double pYStart = format.getImageableY();

		double xRatio = pWidth / cWidth;
		double yRatio = pHeight / cHeight;

		Graphics2D g2 = (Graphics2D) graphics;
		g2.translate(pXStart, pYStart);
		g2.scale(xRatio, yRatio);
		comp.paint(g2);

		return Printable.PAGE_EXISTS;
	}
}
