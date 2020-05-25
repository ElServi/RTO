package com.linecom.rto;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RTOTraza extends FileOutputStream {

	public RTOTraza(String name) throws FileNotFoundException {
		super(name,true);
		// TODO Auto-generated constructor stub
	}
	
	public void escribirInicioProceso()
	{
		String s_Text = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		s_Text = "*Inicio de proceso ["+ dateFormat.format(date) +"]\r\n";
		try {
			super.write(s_Text.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void escribirFinalProceso(int i_NumeroFicheros)
	{
		String s_Text = "";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		s_Text = "-Fin de proceso ["+ dateFormat.format(date) +"] - Ficheros tratados: " + i_NumeroFicheros + "\r\n";
		try {
			super.write(s_Text.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void escribirError(String psFichero, Exception e) {
		String s_Text = "[ERROR]" + psFichero + " | " + e.getMessage() + "\r\n" + getStackTrace(e) + "\r\n";
		try {
			super.write(s_Text.getBytes());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static String getStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String sStackTrace = sw.toString(); // stack trace as a string
		return sStackTrace;
	}
}
