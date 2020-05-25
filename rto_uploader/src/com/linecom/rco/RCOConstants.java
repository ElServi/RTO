package com.linecom.rco;

public class RCOConstants {
	 // Parámetros de la base de datos
	public static final String PROP_RCO_BDD_SERVER = "bdd.rco.server";
	public static final String PROP_RCO_BDD_PORT = "bdd.rco.port";
	public static final String PROP_RCO_BDD_NAME = "bdd.rco.name";
	public static final String PROP_RCO_BDD_USER = "bdd.rco.user";
	public static final String PROP_RCO_BDD_PWD = "bdd.rco.password";
	
	 // Configuración de carga de STORTAGE_MENSUAL
	public static final String PROP_STORAGEMENSUAL_TABLE = "storage_mensual.bdd.table";
	public static final String PROP_STORAGEMENSUAL_DIR_CARGA = "storage_mensual.dir.carga";
	public static final String PROP_STORAGEMENSUAL_DIR_CARGA_OK = "storage_mensual.dir.carga.ok";
	public static final String PROP_STORAGEMENSUAL_DIR_CARGA_NOK = "storage_mensual.dir.carga.nok";
	public static final String PROP_STORAGEMENSUAL_DIR_LOG = "storage_mensual.dir.log";

	
	
	public static final String SQL_INSERT_STORAGE_MENSUAL = "INSERT INTO [dbo].[%TABLE_STORAGE_MENSUAL%]"
			+ "           ([FECHA]"
			+ "          ,[NOMBRE]"
			+ "          ,[MARCA]"
			+ "          ,[MODELO]"
			+ "          ,[ESTADO]"
			+ "          ,[TIPO]"
			+ "          ,[CAPACITY]"
			+ "          ,[PROYECTO]"
			+ "          ,[AREA]"
			+ "          ,[DATACENTER]"
			+ "          ,[ENTORNO]"
			+ "          ,[ITEM]"
			+ "          ,[RAW]"
			+ "          ,[NETO]"
			+ "          ,[USAGE]"
			+ "          ,[ASIGNADO]"
			+ "          ,[OCUPADO]"
			+ "          ,[DATO_NETO]"
			+ "          ,[DISPONIBLE]"
			+ "          ,[ASIGNADO_LIBRE]"
			+ "          ,[PORCENTAJE_LIBRE])"
			+ "    VALUES"
			+ "          (convert(datetime, ?,103)"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"			
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"			
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"			
			+ "          ,?"
			+ "          ,cast(? as decimal(5,2)))";

	public static final String SQL_INSERT_CAPACITY_STORAGE = "INSERT INTO [dbo].[%TABLE_STORAGE_MENSUAL%]"
			+ "           ([FECHA]"
			+ "          ,[NOMBRE]"
			+ "          ,[MARCA]"
			+ "          ,[MODELO]"
			+ "          ,[ESTADO]"
			+ "          ,[TIPO]"
			+ "          ,[CAPACITY]"
			+ "          ,[PROYECTO]"
			+ "          ,[AREA]"
			+ "          ,[DATACENTER]"
			+ "          ,[ENTORNO]"
			+ "          ,[ITEM]"
			+ "          ,[RAW]"
			+ "          ,[NETO]"
			+ "          ,[USAGE]"
			+ "          ,[ASIGNADO]"
			+ "          ,[OCUPADO]"
			+ "          ,[DATO_NETO]"
			+ "          ,[DISPONIBLE]"
			+ "          ,[ASIGNADO_LIBRE]"
			+ "          ,[PORCENTAJE_LIBRE])"
			+ "    VALUES"
			+ "          (convert(datetime, ?,103)"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"			
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"			
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"
			+ "          ,?"			
			+ "          ,?"
			+ "          ,cast(? as decimal(5,2)))";
}
