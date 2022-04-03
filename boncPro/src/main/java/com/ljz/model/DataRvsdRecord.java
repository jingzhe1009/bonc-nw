package com.ljz.model;

import java.io.Serializable;
import java.util.Date;

public class DataRvsdRecord implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String needVrsnNbr;

    private String exptSeqNbr;
	
    private String dataSrcAbbr;
    
    private String chgPsn;

	private String exctPsn;

	private String corrIntfStdVrsn;

	private String intfDscr;

	private String sDate;

	private String eDate;

	private String fileName;

	public String getNeedVrsnNbr() {
		return needVrsnNbr;
	}

	public void setNeedVrsnNbr(String needVrsnNbr) {
		this.needVrsnNbr = needVrsnNbr;
	}

	public String getExptSeqNbr() {
		return exptSeqNbr;
	}

	public void setExptSeqNbr(String exptSeqNbr) {
		this.exptSeqNbr = exptSeqNbr;
	}

	public String getDataSrcAbbr() {
		return dataSrcAbbr;
	}

	public void setDataSrcAbbr(String dataSrcAbbr) {
		this.dataSrcAbbr = dataSrcAbbr;
	}

	public String getChgPsn() {
		return chgPsn;
	}

	public void setChgPsn(String chgPsn) {
		this.chgPsn = chgPsn;
	}

	public String getExctPsn() {
		return exctPsn;
	}

	public void setExctPsn(String exctPsn) {
		this.exctPsn = exctPsn;
	}

	public String getCorrIntfStdVrsn() {
		return corrIntfStdVrsn;
	}

	public void setCorrIntfStdVrsn(String corrIntfStdVrsn) {
		this.corrIntfStdVrsn = corrIntfStdVrsn;
	}

	public String getIntfDscr() {
		return intfDscr;
	}

	public void setIntfDscr(String intfDscr) {
		this.intfDscr = intfDscr;
	}

	public String getsDate() {
		return sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public String geteDate() {
		return eDate;
	}

	public void seteDate(String eDate) {
		this.eDate = eDate;
	}



	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "DataRvsdRecord{" +
				"needVrsnNbr='" + needVrsnNbr + '\'' +
				", exptSeqNbr='" + exptSeqNbr + '\'' +
				", dataSrcAbbr='" + dataSrcAbbr + '\'' +
				", chgPsn='" + chgPsn + '\'' +
				", exctPsn='" + exctPsn + '\'' +
				", corrIntfStdVrsn='" + corrIntfStdVrsn + '\'' +
				", intfDscr='" + intfDscr + '\'' +
				", sDate='" + sDate + '\'' +
				", eDate='" + eDate + '\'' +
				'}';
	}
}