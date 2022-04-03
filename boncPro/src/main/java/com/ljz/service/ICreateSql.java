package com.ljz.service;


public interface ICreateSql{

	String create(String[] tables, String dbType);

	String createSqlAndFile(String[] tables, String dbType);

	String insetDb(String[] tables, String dbType);

}
