package com.linecom.rto;

public class RTOConstants {
	 // Parámetros de la base de datos
	public static final String PROP_RTO_BDD_SERVER = "bdd.rto.server";
	public static final String PROP_RTO_BDD_PORT = "bdd.rto.port";
	public static final String PROP_RTO_BDD_NAME = "bdd.rto.name";
	public static final String PROP_RTO_BDD_USER = "bdd.rto.user";
	public static final String PROP_RTO_BDD_PWD = "bdd.rto.password";
	
	 // Configuración de carga de BACKUP_DIARIO
	public static final String PROP_BACKUPDIARIO_TABLE = "backup_diario.bdd.table";
	public static final String PROP_BACKUPDIARIO_DIR_CARGA = "backup_diario.dir.carga";
	public static final String PROP_BACKUPDIARIO_DIR_CARGA_OK = "backup_diario.dir.carga.ok";
	public static final String PROP_BACKUPDIARIO_DIR_CARGA_NOK = "backup_diario.dir.carga.nok";
	public static final String PROP_BACKUPDIARIO_DIR_LOG = "backup_diario.dir.log";

	 // Configuración de carga de BACKUP_DIARIO_ALERTADOS
	public static final String PROP_BACKUPDIARIOALERTADOS_TABLE = "backup_diario_alertados.bdd.table";
	public static final String PROP_BACKUPDIARIOALERTADOS_DIR_CARGA = "backup_diario_alertados.dir.carga";
	public static final String PROP_BACKUPDIARIOALERTADOS_DIR_CARGA_OK = "backup_diario_alertados.dir.carga.ok";
	public static final String PROP_BACKUPDIARIOALERTADOS_DIR_CARGA_NOK = "backup_diario_alertados.dir.carga.nok";
	public static final String PROP_BACKUPDIARIOALERTADOS_DIR_LOG = "backup_diario_alertados.dir.log";
	
	 // Listado de meses Mon
	public static final String[] LIST_MON = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	 // Sentencia SQL para insertar Backup_Diario
	public static final String SQL_INSERT_BACKUP_DIARIO = "INSERT INTO [dbo].[%TABLE_BACKUP_DIARIO%]"
			+ "           ([FECHA]"
			+ "          ,[MASTER_SERVER]"
			+ "          ,[CABINA]"
			+ "          ,[TIPO]"
			+ "          ,[NETO_GB]"
			+ "          ,[USADO_GB]"
			+ "          ,[LIBRE_GB]"
			+ "          ,[PORCENTAJE_OCUPADO])"
			+ "    VALUES"
			+ "          (convert(datetime, ?,20)"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"			
			+ "          ,cast(? as decimal(5,2)))";
	
	public static final String SQL_UPDATE_FECHA = "UPDATE [dbo].[%TABLE_BACKUP_DIARIO%]"
			+ "   SET [FECHA] = convert(datetime,?,  103)"
			+ "   WHERE FECHA = cast('31/03/1956 8:00:00' as datetime)";
	
	 // Sentencia SQL para insertar Backup_Diario
/*	public static final String SQL_INSERT_BACKUP_DIARIO_ALERTADO="INSERT INTO [dbo].[%TABLE_BACKUP_DIARIO_ALERTADOS%]" 
			+ "           ([MASTER_SEVER]" 
			+ "           ,[CABINA]" 
			+ "           ,[FECHA_INICIO_ALERTA]" 
			+ "           ,[FECHA_FIN_ALERTA])" 
			+ "     VALUES" 
			+ "           (?" 
			+ "           ,?" 
			+ "           ,?" 
			+ "           ,?)";
	*/
	public static final String SQL_INSERT_BACKUP_DIARIO_ALERTADO ="IF NOT EXISTS ("
			+ "SELECT MASTER_SERVER, CABINA FROM %TABLE_BACKUP_DIARIO_ALERTADOS% WHERE MASTER_SERVER=?"
			+ "		    AND CABINA=?)"
			+ "INSERT INTO [dbo].[%TABLE_BACKUP_DIARIO_ALERTADOS%]"
			+ "           ([MASTER_SERVER]"
			+ "           ,[CABINA]"
			+ "           ,[FECHA_INICIO_ALERTA]"
			+ "           ,[FECHA_FIN_ALERTA])"
			+ "     VALUES"
			+ "           (?"
			+ "           ,?"
			+ "           ,convert(datetime,?,  103)"
			+ "           ,convert(datetime,?,  103))"
			+ " ELSE"
			+ " UPDATE [dbo].[%TABLE_BACKUP_DIARIO_ALERTADOS%]"
			+ "   SET [FECHA_INICIO_ALERTA] = convert(datetime,?,  103)"
			+ "      ,[FECHA_FIN_ALERTA] = convert(datetime,?,  103)"
			+ " WHERE MASTER_SERVER= ?"
			+ "		    AND CABINA=?";
	
	public static final String SQL_UPDATE_BACKUP_DIARIO_ALERTADO ="IF EXISTS ("
			+ " SELECT MASTER_SERVER, CABINA FROM BACKUP_DIARIO_ALERTADOS_PRV WHERE MASTER_SERVER=?"
			+ "		    AND CABINA=?)"
			+ " UPDATE [dbo].[BACKUP_DIARIO_ALERTADOS_PRV]"
			+ "   SET [FECHA_FIN_ALERTA] = ?"
			+ " WHERE MASTER_SERVER=?"
			+ "		    AND CABINA=?";
						
}
