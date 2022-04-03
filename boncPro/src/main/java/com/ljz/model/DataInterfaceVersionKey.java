package com.ljz.model;

import java.io.Serializable;

public class DataInterfaceVersionKey implements Serializable {
    private String needVrsnNbr;

    private String dataSrcAbbr;

    private String dataInterfaceNo;

    private String dataInterfaceName;

    private static final long serialVersionUID = 1L;

    public String getNeedVrsnNbr() {
        return needVrsnNbr;
    }

    public void setNeedVrsnNbr(String needVrsnNbr) {
        this.needVrsnNbr = needVrsnNbr == null ? null : needVrsnNbr.trim();
    }

    public String getDataSrcAbbr() {
        return dataSrcAbbr;
    }

    public void setDataSrcAbbr(String dataSrcAbbr) {
        this.dataSrcAbbr = dataSrcAbbr == null ? null : dataSrcAbbr.trim();
    }

    public String getDataInterfaceNo() {
        return dataInterfaceNo;
    }

    public void setDataInterfaceNo(String dataInterfaceNo) {
        this.dataInterfaceNo = dataInterfaceNo == null ? null : dataInterfaceNo.trim();
    }

    public String getDataInterfaceName() {
        return dataInterfaceName;
    }

    public void setDataInterfaceName(String dataInterfaceName) {
        this.dataInterfaceName = dataInterfaceName == null ? null : dataInterfaceName.trim();
    }
}