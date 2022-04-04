package com.ljz.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ljz.model.DataInterface2procTmp;
import com.ljz.model.DataInterfaceColumnsTmp;
import com.ljz.model.DataInterfaceTmp;
import com.ljz.model.DataRvsdRecordTmp;

public class ImportInfo {
	
	private String dataSrcAbbr;
	
	private String batchNo;
	//接口修改数
	private int intUpdateNum;
	//接口新增数
	private int intInsertNum;
	//接口总数
	private int intAllNum;
	//算法修改数
	private int procUpdateNum;
	//算法新增数
	private int procInsertNum;
	//算法总数
	private int procAllNum;
	//字段修改数
	private int colUpdateNum;
	//字段新增数
	private int colInsertNum;
	//字段总数
	private int colAllNum;
	
	private List<DataInterfaceTmp> addList;
	
	private List<DataInterfaceTmp> editList;
	
	private List<DataInterfaceTmp> totalList;
	//无变化
	private List<DataInterfaceTmp> sameList;
	
	private List<DataInterface2procTmp> addProcList;
	
	private List<DataInterface2procTmp> editProcList;
	
	private List<DataInterfaceColumnsTmp> addColList;
	
	private List<DataInterfaceColumnsTmp> editColList;
	//需要建表语句的列表
	private List<String> needFileList = new ArrayList<String>();
	//需要物化的列表
	private List<String> needCreateList = new ArrayList<String>();
	
	DataRvsdRecordTmp dataRvsdRecordTmp;
	
	List<DataInterfaceTmp> dataInterfaceTmpList;
	
	List<DataInterfaceColumnsTmp> dataInterfaceColumnsTmpList;
	
	List<DataInterface2procTmp> dataInterface2procTmpList;
	//变更明细
	Map<String,Object> msgMap;
	//生成文件语句
	private String DMLInsert;
	
	private String DMLDeclare;
	
	private String rollBackSb;
	//生成文件路径
	private String DMLInsertPath;
	
	private String DMLFilePath;
	
	private String rollBackFilePath;
	//接口对应字段数
	List<Map<String, Object>> queryIntNum = new ArrayList<Map<String, Object>>();
	//s表列表
	List<Map<String, Object>> sdataList = new ArrayList<Map<String, Object>>();
	//o表列表
	List<Map<String, Object>> odataList = new ArrayList<Map<String, Object>>();

	public int getIntUpdateNum() {
		return intUpdateNum;
	}

	public void setIntUpdateNum(int intUpdateNum) {
		this.intUpdateNum = intUpdateNum;
	}

	public int getIntInsertNum() {
		return intInsertNum;
	}

	public void setIntInsertNum(int intInsertNum) {
		this.intInsertNum = intInsertNum;
	}

	public int getIntAllNum() {
		return intAllNum;
	}

	public void setIntAllNum(int intAllNum) {
		this.intAllNum = intAllNum;
	}

	public int getProcUpdateNum() {
		return procUpdateNum;
	}

	public void setProcUpdateNum(int procUpdateNum) {
		this.procUpdateNum = procUpdateNum;
	}

	public int getProcInsertNum() {
		return procInsertNum;
	}

	public void setProcInsertNum(int procInsertNum) {
		this.procInsertNum = procInsertNum;
	}

	public int getProcAllNum() {
		return procAllNum;
	}

	public void setProcAllNum(int procAllNum) {
		this.procAllNum = procAllNum;
	}

	public int getColUpdateNum() {
		return colUpdateNum;
	}

	public void setColUpdateNum(int colUpdateNum) {
		this.colUpdateNum = colUpdateNum;
	}

	public int getColInsertNum() {
		return colInsertNum;
	}

	public void setColInsertNum(int colInsertNum) {
		this.colInsertNum = colInsertNum;
	}

	public int getColAllNum() {
		return colAllNum;
	}

	public void setColAllNum(int colAllNum) {
		this.colAllNum = colAllNum;
	}

	public List<DataInterfaceTmp> getAddList() {
		return addList;
	}

	public void setAddList(List<DataInterfaceTmp> addList) {
		this.addList = addList;
	}

	public List<DataInterfaceTmp> getEditList() {
		return editList;
	}

	public void setEditList(List<DataInterfaceTmp> editList) {
		this.editList = editList;
	}

	public List<DataInterfaceTmp> getTotalList() {
		return totalList;
	}

	public void setTotalList(List<DataInterfaceTmp> totalList) {
		this.totalList = totalList;
	}

	public List<DataInterfaceTmp> getSameList() {
		return sameList;
	}

	public void setSameList(List<DataInterfaceTmp> sameList) {
		this.sameList = sameList;
	}

	public List<DataInterface2procTmp> getAddProcList() {
		return addProcList;
	}

	public void setAddProcList(List<DataInterface2procTmp> addProcList) {
		this.addProcList = addProcList;
	}

	public List<DataInterface2procTmp> getEditProcList() {
		return editProcList;
	}

	public void setEditProcList(List<DataInterface2procTmp> editProcList) {
		this.editProcList = editProcList;
	}

	public List<DataInterfaceColumnsTmp> getAddColList() {
		return addColList;
	}

	public void setAddColList(List<DataInterfaceColumnsTmp> addColList) {
		this.addColList = addColList;
	}

	public List<DataInterfaceColumnsTmp> getEditColList() {
		return editColList;
	}

	public void setEditColList(List<DataInterfaceColumnsTmp> editColList) {
		this.editColList = editColList;
	}

	public DataRvsdRecordTmp getDataRvsdRecordTmp() {
		return dataRvsdRecordTmp;
	}

	public void setDataRvsdRecordTmp(DataRvsdRecordTmp dataRvsdRecordTmp) {
		this.dataRvsdRecordTmp = dataRvsdRecordTmp;
	}

	public List<DataInterfaceTmp> getDataInterfaceTmpList() {
		return dataInterfaceTmpList;
	}

	public void setDataInterfaceTmpList(List<DataInterfaceTmp> dataInterfaceTmpList) {
		this.dataInterfaceTmpList = dataInterfaceTmpList;
	}

	public List<DataInterfaceColumnsTmp> getDataInterfaceColumnsTmpList() {
		return dataInterfaceColumnsTmpList;
	}

	public void setDataInterfaceColumnsTmpList(List<DataInterfaceColumnsTmp> dataInterfaceColumnsTmpList) {
		this.dataInterfaceColumnsTmpList = dataInterfaceColumnsTmpList;
	}

	public List<DataInterface2procTmp> getDataInterface2procTmpList() {
		return dataInterface2procTmpList;
	}

	public void setDataInterface2procTmpList(List<DataInterface2procTmp> dataInterface2procTmpList) {
		this.dataInterface2procTmpList = dataInterface2procTmpList;
	}

	public Map<String, Object> getMsgMap() {
		return msgMap;
	}

	public void setMsgMap(Map<String, Object> msgMap) {
		this.msgMap = msgMap;
	}

	public String getDMLInsert() {
		return DMLInsert;
	}

	public void setDMLInsert(String dMLInsert) {
		DMLInsert = dMLInsert;
	}

	public String getDMLDeclare() {
		return DMLDeclare;
	}

	public void setDMLDeclare(String dMLDeclare) {
		DMLDeclare = dMLDeclare;
	}

	public String getDMLInsertPath() {
		return DMLInsertPath;
	}

	public void setDMLInsertPath(String dMLInsertPath) {
		DMLInsertPath = dMLInsertPath;
	}

	public String getDMLFilePath() {
		return DMLFilePath;
	}

	public void setDMLFilePath(String dMLFilePath) {
		DMLFilePath = dMLFilePath;
	}

	

	public List<Map<String, Object>> getQueryIntNum() {
		return queryIntNum;
	}

	public void setQueryIntNum(List<Map<String, Object>> queryIntNum) {
		this.queryIntNum = queryIntNum;
	}

	public List<Map<String, Object>> getSdataList() {
		return sdataList;
	}

	public void setSdataList(List<Map<String, Object>> sdataList) {
		this.sdataList = sdataList;
	}

	public List<Map<String, Object>> getOdataList() {
		return odataList;
	}

	public void setOdataList(List<Map<String, Object>> odataList) {
		this.odataList = odataList;
	}

	public String getRollBackSb() {
		return rollBackSb;
	}

	public void setRollBackSb(String rollBackSb) {
		this.rollBackSb = rollBackSb;
	}

	public String getRollBackFilePath() {
		return rollBackFilePath;
	}

	public void setRollBackFilePath(String rollBackFilePath) {
		this.rollBackFilePath = rollBackFilePath;
	}

	public String getDataSrcAbbr() {
		return dataSrcAbbr;
	}

	public void setDataSrcAbbr(String dataSrcAbbr) {
		this.dataSrcAbbr = dataSrcAbbr;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public List<String> getNeedFileList() {
		return needFileList;
	}

	public void setNeedFileList(List<String> needFileList) {
		this.needFileList = needFileList;
	}

	public List<String> getNeedCreateList() {
		return needCreateList;
	}

	public void setNeedCreateList(List<String> needCreateList) {
		this.needCreateList = needCreateList;
	}

	
	
	

	
	
	
	
	

}
