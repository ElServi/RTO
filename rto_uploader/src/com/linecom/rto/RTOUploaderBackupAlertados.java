package com.linecom.rto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringTokenizer;

public class RTOUploaderBackupAlertados extends Thread {
	private  Connection m_Conexion = null;
	private  Properties m_PropFile = null;
	private  String ms_DirectorioCarga = null;
	private RTOTraza m_Traza = null;

	public RTOUploaderBackupAlertados(Connection p_Connection, Properties p_PropFile) {
		m_Conexion = p_Connection;
		m_PropFile = p_PropFile;
	}

	public void run() {		
		ms_DirectorioCarga= m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIOALERTADOS_DIR_CARGA);
		try {
			m_Traza = new RTOTraza(m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIOALERTADOS_DIR_LOG)
					+ "\\alertados.log");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(ms_DirectorioCarga);
		tratarFicherosExtraccion(ms_DirectorioCarga);		
	}
	
	private void tratarFicherosExtraccion(String string) {
		m_Traza.escribirInicioProceso();
		File directorioExtraccion = new File (ms_DirectorioCarga);
		int i;
		File[] listaFicheros = directorioExtraccion.listFiles();
		
		for (i=0; i < listaFicheros.length; i++) {
			 //System.out.println(ms_DirectorioCarga + "\\" +listaFicheros[i].getName());
			tratarCargaFichero(ms_DirectorioCarga + "\\" +listaFicheros[i].getName());
			System.out.println("----------------->Tratado: " + i);
		}
		m_Traza.escribirFinalProceso(i);
	}
	
	private void tratarCargaFichero(String p_sFileName) {
	    String fichero = p_sFileName;
	    String linea;
	    boolean l_bTodoOK = false;
	    FileReader fr = null;
	    
	    try {
	      fr = new FileReader(fichero);
	      BufferedReader br = new BufferedReader(fr);
	      
	       // Pasamos de las primera lineas
	      for(int i=0; i<1; i++) {
	    	  linea = br.readLine();
	      }
	    	  
	       // Procesamos el resto del archivo
	      while((linea = br.readLine()) != null) {
	    	  System.out.println(linea);
	    	  procesaLinea(linea);
	      }
	      fr.close();
	      l_bTodoOK = true;
	    }
	    catch(Exception e) {
	      System.out.println("Excepcion leyendo fichero "+ fichero + ": " + e + "\r\n");
	      e.printStackTrace();
	      
	      m_Traza.escribirError(fichero, e);
	      try {
	    	  fr.close();
	      }
	      catch(Exception ex) {
	    	  
	      }
	      l_bTodoOK = false;
	    }
	    copiaOK(p_sFileName,  l_bTodoOK);		
	}

	private void copiaOK(String p_sFileName, boolean p_bOK) {
		String l_sOrigen = p_sFileName;
		String l_sDestino = (p_bOK ? m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIOALERTADOS_DIR_CARGA_OK) + "\\"
				: m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIOALERTADOS_DIR_CARGA_NOK) + "\\") + (new File(p_sFileName).getName());

        try {
        	
        	Files.move(FileSystems.getDefault().getPath(l_sOrigen), 
        			FileSystems.getDefault().getPath(l_sDestino), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(e);
        }
	}
	
	private void procesaLinea(String linea) throws Exception {
		StringTokenizer st = new StringTokenizer(linea,";");
		String[] parts = linea.split(";");
				
		PreparedStatement l_PrepSentencia = null;
		
		 // Si viene la Fecha de Inicio Insertamos el registro de alerta
		if(parts.length >2 && !parts[0].contentEquals("") && !parts[1].contentEquals(""))
		{
			if(!parts[2].equals(""))
			{
				try {
					l_PrepSentencia = m_Conexion.prepareStatement(RTOConstants.SQL_INSERT_BACKUP_DIARIO_ALERTADO.replace("%TABLE_BACKUP_DIARIO_ALERTADOS%"
							, m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIOALERTADOS_TABLE)));
					l_PrepSentencia.setString(1, parts[0]);
					l_PrepSentencia.setString(2, parts[1]);
					l_PrepSentencia.setString(3, parts[0]);
					l_PrepSentencia.setString(4, parts[1]);
					l_PrepSentencia.setString(5, parts[2]);
					l_PrepSentencia.setString(6, parts.length !=4 ?  null: parts[3]);
					l_PrepSentencia.setString(7, parts[2]);
					l_PrepSentencia.setString(8, parts.length !=4 ?  null: parts[3]);
					l_PrepSentencia.setString(9, parts[0]);
					l_PrepSentencia.setString(10, parts[1]);
					
					l_PrepSentencia.executeUpdate();
				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw(e);
				}
			}
			 // Si NO viene la Fecha de Inicio Actualizamos registro existente
			else {
				try {
					l_PrepSentencia = m_Conexion.prepareStatement(RTOConstants.SQL_UPDATE_BACKUP_DIARIO_ALERTADO.replace("%TABLE_BACKUP_DIARIO_ALERTADOS%"
							, m_PropFile.getProperty(RTOConstants.PROP_BACKUPDIARIOALERTADOS_TABLE)));
					l_PrepSentencia.setString(1,  parts[0]);
					l_PrepSentencia.setString(2, parts[1]);
					l_PrepSentencia.setString(3, parts.length !=4 ?  null: parts[3]);
					l_PrepSentencia.setString(4, parts[0]);
					l_PrepSentencia.setString(5, parts[1]);
					
					l_PrepSentencia.executeUpdate();
			
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw(e);
				}
			}
		}
	}
}
