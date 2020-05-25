package com.linecom.rto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.text.StyleContext.SmallAttributeSet;


public class RTOUploaderBackupDiario extends Thread {
	
	private  Connection m_Conexion = null;
	private  Properties m_PropFile = null;
	private  String ms_DirectorioCarga = null;
	private RTOTraza m_Traza = null;


	public RTOUploaderBackupDiario(Connection p_Connection, Properties p_PropFile) {
		m_Conexion = p_Connection;
		m_PropFile = p_PropFile;
	}
	
	public void run() {		
		ms_DirectorioCarga= m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIO_DIR_CARGA);
		 // Abrir archiuo de trazas
		try {
			m_Traza = new RTOTraza(m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIO_DIR_LOG)
					+ "\\backupdiario.log");
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
	    String fichero = p_sFileName;
	    String linea;
	    String s_UltimaLinea ="";
	    String s_FechaInsert = "";
	    String s_SentenciaUpdateFecha = "";
	    boolean l_bTodoOK = false;
	    
	    try {
	      FileReader fr = new FileReader(fichero);
	      BufferedReader br = new BufferedReader(fr);
	      
	       // Pasamos de las tres primeras lineas
	      for(int i=0; i<3; i++) {
	    	  linea = br.readLine();
	      }
	    	  
	       // Procesamos el resto del archivo
	      while((linea = br.readLine()) != null) {
	    	  if(linea.indexOf("Report generated on")<0) {
	    		  procesaLinea(linea);
	    	  }
	    	  else
	      	s_UltimaLinea= linea;
	      	
	      }
	      System.out.println("ultima: |" + s_UltimaLinea +"|");
	      s_FechaInsert = s_UltimaLinea.substring(21, s_UltimaLinea.length()-1);
	      System.out.println("ultima: |" + s_UltimaLinea +"| Fecha: |" + conversionFecha(s_FechaInsert) +"|");
	      actualizaFecha(conversionFecha(s_FechaInsert));
	      fr.close();
	      l_bTodoOK = true;
	    }
	    catch(Exception e) {
	      System.out.println("Excepcion leyendo fichero "+ fichero + ": " + e);
	      m_Traza.escribirError(fichero, e);
	    }
	    copiaOK(p_sFileName,  l_bTodoOK);		
	}

	private void copiaOK(String p_sFileName, boolean p_bOK) {
		String l_sOrigen = p_sFileName;
		String l_sDestino = (p_bOK ? m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIO_DIR_CARGA_OK) + "\\"
				: m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIO_DIR_CARGA_NOK) + "\\") + (new File(p_sFileName).getName());

        try {
        	
        	Files.move(FileSystems.getDefault().getPath(l_sOrigen), 
        			FileSystems.getDefault().getPath(l_sDestino), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(e);
        }

	}

	private void actualizaFecha(String conversionFecha) {
		PreparedStatement l_PrepSentencia = null;
		
		try {
			l_PrepSentencia = m_Conexion.prepareStatement(RTOConstants.SQL_UPDATE_FECHA.replace("%TABLE_BACKUP_DIARIO%"
					, m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIO_TABLE)));
			l_PrepSentencia.setString(1, conversionFecha);
			l_PrepSentencia.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private  String conversionFecha(String p_FechaInsert) {
		String l_sMon = "";
		String l_sMes = "";
		String l_sDia = "";
		String l_sAnyo = "";
		String l_sHora = "";
		String l_sMeridian = "";
		
		 // Sacamos la coma de la línea
		p_FechaInsert = p_FechaInsert.replaceFirst(",", "");
		StringTokenizer st = new StringTokenizer(p_FechaInsert," ");
		for(int i=0; st.hasMoreTokens(); i++) {
			switch (i) {
			case 0:
				l_sMon = st.nextToken();
				int l_iMes;
				for(l_iMes=0; l_iMes < RTOConstants.LIST_MON.length; l_iMes++) {
					if(l_sMon.equalsIgnoreCase(RTOConstants.LIST_MON[l_iMes])) {
						l_sMes = String.valueOf(l_iMes+1);
						break;
					}
				}
				break;
			case 1:
				l_sDia = st.nextToken();
				break;
			case 2:
				l_sAnyo = st.nextToken();
				break;
			case 3:
				l_sHora = st.nextToken();
				break;
			case 4:
				l_sMeridian = st.nextToken();
				break;
			default:
				break;
			}		
		}
		return l_sDia+"/"+l_sMes+"/"+l_sAnyo+" " + l_sHora + l_sMeridian;
	}


	private void procesaLinea(String linea) {
		String[] parts = linea.split(",");
		
		PreparedStatement l_PrepSentencia = null;
		
		
		try {
			l_PrepSentencia = m_Conexion.prepareStatement(RTOConstants.SQL_INSERT_BACKUP_DIARIO.replace("%TABLE_BACKUP_DIARIO%"
					, m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIO_TABLE)));
			l_PrepSentencia.setString(1, "1956-03-31 08:00:00");
			l_PrepSentencia.setString(2,  parts[0]);
			l_PrepSentencia.setString(3, parts[1]);
			l_PrepSentencia.setString(4, parts[2]);
			l_PrepSentencia.setInt(5, Integer.parseInt(parts[3]));
			l_PrepSentencia.setInt(6, Integer.parseInt(parts[4]));
			l_PrepSentencia.setInt(7, Integer.parseInt(parts[5]));
			l_PrepSentencia.setFloat(8, Float.parseFloat(parts[4])/Float.parseFloat(parts[3])*100);
			l_PrepSentencia.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
