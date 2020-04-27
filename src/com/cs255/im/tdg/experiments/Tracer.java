package com.cs255.im.tdg.experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Tracer {
	File f;
	FileWriter fw;
	BufferedWriter bw;
	
	@SuppressWarnings("deprecation")
	public Tracer(String filePath, int d1, int d2, int d3, float threshold, int seedsize) throws IOException
	{
		Date d = new Date();
		String fileName = filePath+"/output_"+d1 + "_" + d2+ "_" + d3 + "_" +threshold+"_"+seedsize+ "_"+ Calendar.getInstance().getTimeInMillis()+".txt";
		f = new File(fileName);
		fw = new FileWriter(f);
		bw = new BufferedWriter(fw);
		System.out.println("Trace File : " + fileName);
	}
	public void trace(String s)
	{
		trace(s,true);
	}
	public void trace(String s, boolean newLine)
	{
		try {
			bw.write(s);
			if(newLine)
				bw.newLine();
			bw.flush();
		}
		catch(Exception e)
		{
			System.out.println("Exiting...Some exception in tracer.." + e);
			destroy();
		}
	}
	
	
	public void destroy()
	{
		
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
