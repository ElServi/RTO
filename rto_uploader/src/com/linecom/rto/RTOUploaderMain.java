package com.linecom.rto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.linecom.rco.RCOConstants;
import com.linecom.rco.RCOUploaderStorageMensual;

public class RTOUploaderMain {
	private static Connection m_Conexion = null;
	private static Properties m_PropFile = null;
	private static String m_sConfigDir = System.getProperty("cfg.dir");
	private static RTOUploaderBackupDiario m_BackupDiarioProces= null;
	private static RTOUploaderBackupAlertados m_BackupAlertadosProces= null;
	private static RCOUploaderStorageMensual m_StorageMensualProces = null;
	

	public static void main(String[] args) {
		System.out.println(m_sConfigDir);
		m_PropFile = cargaPropiedadesRTO();
		abrirConexionBDDRTO();
		m_BackupDiarioProces = new RTOUploaderBackupDiario(m_Conexion, m_PropFile);
		m_BackupDiarioProces.run();
		try {
			m_Conexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		m_PropFile = cargaPropiedadesRCO();
		abrirConexionBDDRCO();
		m_StorageMensualProces = new RCOUploaderStorageMensual(m_Conexion, m_PropFile);
		m_StorageMensualProces.run();
		try {
			m_Conexion.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private static Properties cargaPropiedadesRTO() {
		Properties prop = new Properties();
		InputStream is = null;
			
		try {
			is = new FileInputStream(m_sConfigDir + "/rto_uploader.properties");
			prop.load(is);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		return prop;
	}

	private static Properties cargaPropiedadesRCO() {
		Properties prop = new Properties();
		InputStream is = null;
			
		try {
			is = new FileInputStream(m_sConfigDir + "/rco_uploader.properties");
			prop.load(is);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		return prop;
	}

	private static void abrirConexionBDDRTO() {
    	m_Conexion = null;
        String connectionUrl =
        	"jdbc:sqlserver://"+ m_PropFile.getProperty(RTOConstants.PROP_RTO_BDD_SERVER)
        		+":"+m_PropFile.getProperty(RTOConstants.PROP_RTO_BDD_PORT)+";"
        		+ "database="+m_PropFile.getProperty(RTOConstants.PROP_RTO_BDD_NAME)+";"
        		+ "user="+m_PropFile.getProperty(RTOConstants.PROP_RTO_BDD_USER)+";"
        		+ "password="+m_PropFile.getProperty(RTOConstants.PROP_RTO_BDD_PWD)+";"
        		+ "encrypt=true;"
        		+ "trustServerCertificate=true;"
        		+ "loginTimeout=30;";
        
        System.out.println("conexión: "+connectionUrl);

        try {
        	m_Conexion = DriverManager.getConnection(connectionUrl);
        	System.out.println("Conexión correcta");
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
        	m_Conexion = null;
        }
     }
	
	private static void abrirConexionBDDRCO() {
    	m_Conexion = null;
        String connectionUrl =
        	"jdbc:sqlserver://"+ m_PropFile.getProperty(RCOConstants.PROP_RCO_BDD_SERVER)
        		+":"+m_PropFile.getProperty(RCOConstants.PROP_RCO_BDD_PORT)+";"
        		+ "database="+m_PropFile.getProperty(RCOConstants.PROP_RCO_BDD_NAME)+";"
        		+ "user="+m_PropFile.getProperty(RCOConstants.PROP_RCO_BDD_USER)+";"
        		+ "password="+m_PropFile.getProperty(RCOConstants.PROP_RCO_BDD_PWD)+";"
        		+ "encrypt=true;"
        		+ "trustServerCertificate=true;"
        		+ "loginTimeout=30;";
        
        System.out.println("conexión: "+connectionUrl);

        try {
        	m_Conexion = DriverManager.getConnection(connectionUrl);
        	System.out.println("Conexión correcta");
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
        	m_Conexion = null;
        }
     }


}
