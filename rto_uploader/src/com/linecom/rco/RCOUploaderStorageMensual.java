package com.linecom.rco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import com.linecom.rto.RTOTraza;

public class RCOUploaderStorageMensual {
	private  Connection m_Conexion = null;
	private  Properties m_PropFile = null;
	private  String ms_DirectorioCarga = null;
	private RTOTraza m_Traza = null;

	
	public RCOUploaderStorageMensual(Connection p_Connection, Properties p_PropFile) {
		m_Conexion = p_Connection;
		m_PropFile = p_PropFile;
	}

	public void run() {		
		ms_DirectorioCarga= m_PropFile.getProperty(RCOConstants.PROP_STORAGEMENSUAL_DIR_CARGA);
		 // Abrir archiuo de trazas
		try {
			m_Traza = new RTOTraza(m_PropFile.getProperty(RCOConstants.PROP_STORAGEMENSUAL_DIR_LOG)
					+ "\\storagemensual.log");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(ms_DirectorioCarga);
		tratarFicherosExtraccion();
	}
	
	private void tratarFicherosExtraccion() {
		m_Traza.escribirInicioProceso();
		File directorioExtraccion = new File (ms_DirectorioCarga);
		int i;
		
		File[] listaFicheros = directorioExtraccion.listFiles();
		
		for (i=0; i < listaFicheros.length; i++) {
			System.out.println(ms_DirectorioCarga + "\\" +listaFicheros[i].getName());
			tratarCargaFichero(ms_DirectorioCarga + "\\" +listaFicheros[i].getName());
			System.out.println("----------------->Tratado: " + i);
		}
		m_Traza.escribirFinalProceso(i);
	}	

	private void tratarCargaFichero(String p_sFileName) {
	    boolean l_bTodoOK = false;
		 // SI comienza por "CApacity Storage"
		if(p_sFileName.indexOf("Capacity Storage")>=0) {
			tratarCapacityStorage(p_sFileName);
		}
		else {
			tratarDatosHistoricos(p_sFileName);
		}
	}

	private void tratarDatosHistoricos(String p_sFileName) {
		String linea;
		try {
			FileReader fr = new FileReader(p_sFileName);
			BufferedReader br = new BufferedReader(fr);
		       // Pasamos de la cabecera
		    for(int i=0; i<1; i++) {
		    	linea = br.readLine();
		    }
		    
		     // Procesamos el resto del archivo
		    while((linea = br.readLine()) != null) {
		    	procesaLineaDatosHistoricos(linea);
		    }
		}
		catch(Exception e) {
			
		}

		System.out.println("DATOSHistoricos: " + p_sFileName);
		// TODO Auto-generated method stub
		
	}

	private void tratarCapacityStorage(String p_sFileName) {
		String l_sFechaArchivo = p_sFileName.substring(p_sFileName.lastIndexOf("Capacity Storage")+17,p_sFileName.lastIndexOf("Capacity Storage")+24).replace('-', '/');
		// TODO Auto-generated method stub
		System.out.println("CAPACITY Storage: " + p_sFileName);
		String linea;
		try {
			FileReader fr = new FileReader(p_sFileName);
			BufferedReader br = new BufferedReader(fr);
		       // Pasamos de la cabecera
		    for(int i=0; i<1; i++) {
		    	linea = br.readLine();
		    }
		    
		     // Procesamos el resto del archivo
		    while((linea = br.readLine()) != null) {
		    	procesaLineaDatosCapacity(linea, l_sFechaArchivo);
		    }
		}
		catch(Exception e) {
			
		}

		System.out.println("DATOSHistoricos: " + p_sFileName);
		// TODO Auto-generated method stub
		
		
		
	}
	
	private void procesaLineaDatosCapacity(String linea, String p_sFecha) {
		String[] parts = linea.split("\\;",-1);
		
		PreparedStatement l_PrepSentencia = null;
		System.out.println(parts.length);
		System.out.println(linea);
		// String l_sFechaFichero =  ("01/" + p_sFecha).replace('/', '-');
		String l_sFechaFichero =  "01/" + p_sFecha;
		String l_sNum =null;
		
		try {
			l_PrepSentencia = m_Conexion.prepareStatement(RCOConstants.SQL_INSERT_STORAGE_MENSUAL.replace("%TABLE_STORAGE_MENSUAL%"
					, m_PropFile.getProperty(RCOConstants.PROP_STORAGEMENSUAL_TABLE)));
			l_PrepSentencia.setString(1, l_sFechaFichero);
			l_PrepSentencia.setString(2, parts[0]);
			l_PrepSentencia.setString(3, parts[1]);
			l_PrepSentencia.setString(4, parts[2]);
			l_PrepSentencia.setString(5, parts[3]);
			l_PrepSentencia.setString(6, parts[4]);
			l_PrepSentencia.setString(7, parts[5]);
			l_PrepSentencia.setString(8, parts[6]);
			l_PrepSentencia.setString(9, parts[7]);
			l_PrepSentencia.setString(10, parts[8]);
			l_PrepSentencia.setString(11, parts[9]);
			l_PrepSentencia.setString(12, parts[10]);
			l_PrepSentencia.setNull(13, java.sql.Types.FLOAT); // l_PrepSentencia.setFloat(13,null);
			tratarNumerico(parts[11],l_PrepSentencia, 14);
			l_PrepSentencia.setNull(15, java.sql.Types.FLOAT); // l_PrepSentencia.setFloat(15,null);
			tratarNumerico(parts[12],l_PrepSentencia, 16);
			tratarNumerico(parts[13],l_PrepSentencia, 17);
			tratarNumerico(parts[14],l_PrepSentencia, 18);
			tratarNumerico(parts[15],l_PrepSentencia, 19);
			tratarNumerico(parts[16],l_PrepSentencia, 20);
			tratarNumerico(parts[17],l_PrepSentencia, 21);
			//l_PrepSentencia.setFloat(21, Float.parseFloat(parts[4])/Float.parseFloat(parts[3])*100);
			l_PrepSentencia.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

	private void procesaLineaDatosHistoricos(String linea) {
		String[] parts = linea.split("\\;",-1);
		
		PreparedStatement l_PrepSentencia = null;
		//System.out.println(parts.length);
		//System.out.println(linea);
		
		
		try {
			l_PrepSentencia = m_Conexion.prepareStatement(RCOConstants.SQL_INSERT_STORAGE_MENSUAL.replace("%TABLE_STORAGE_MENSUAL%"
					, m_PropFile.getProperty(RCOConstants.PROP_STORAGEMENSUAL_TABLE)));
			l_PrepSentencia.setString(1, parts[0].toString());
			l_PrepSentencia.setString(2, parts[1]);
			l_PrepSentencia.setString(3, parts[2]);
			l_PrepSentencia.setString(4, parts[3]);
			l_PrepSentencia.setString(5, parts[4]);
			l_PrepSentencia.setString(6, parts[5]);
			l_PrepSentencia.setString(7, parts[6]);
			l_PrepSentencia.setString(8, parts[7]);
			l_PrepSentencia.setString(9, parts[8]);
			l_PrepSentencia.setString(10, parts[9]);
			l_PrepSentencia.setString(11, parts[10]);
			l_PrepSentencia.setString(12, parts[11]);
			l_PrepSentencia.setFloat(13, parts[12].length()==0?0:Float.parseFloat(parts[12].replace(',','.')));
			l_PrepSentencia.setFloat(14, parts[13].length()==0?0:Float.parseFloat(parts[13].replace(',','.')));
			l_PrepSentencia.setFloat(15, parts[14].length()==0?0:Float.parseFloat(parts[14].replace(',','.')));
			l_PrepSentencia.setFloat(16, parts[15].length()==0?0:Float.parseFloat(parts[15].replace(',','.')));
			l_PrepSentencia.setFloat(17, parts[16].length()==0?0:Float.parseFloat(parts[16].replace(',','.')));
			l_PrepSentencia.setFloat(18, parts[17].length()==0?0:Float.parseFloat(parts[17].replace(',','.')));
			l_PrepSentencia.setFloat(19, parts[18].length()==0?0:Float.parseFloat(parts[18].replace(',','.')));
			l_PrepSentencia.setFloat(20, parts[19].length()==0?0:Float.parseFloat(parts[19].replace(',','.')));
			l_PrepSentencia.setFloat(21, parts[20].length()==0?0:Float.parseFloat(parts[20].replace(',','.'))*100);
			//l_PrepSentencia.setFloat(21, Float.parseFloat(parts[4])/Float.parseFloat(parts[3])*100);
			l_PrepSentencia.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void tratarNumerico(String p_sParam, PreparedStatement p_Statement, int index)
	{
		String l_sReturn = p_sParam;
		if(p_sParam.length()==0)
			l_sReturn = "0";
		if(p_sParam.indexOf("#N/A")!=-1)
			l_sReturn = null;
		if(p_sParam.trim().contentEquals("-"))
			l_sReturn = null;

		l_sReturn=l_sReturn==null? null:l_sReturn.replace(".","").replace(',','.').replace("%", "");
		
		try {
			if(l_sReturn==null)
				p_Statement.setNull(index, java.sql.Types.FLOAT);
			else
				p_Statement.setFloat(index, Float.parseFloat(l_sReturn));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}
