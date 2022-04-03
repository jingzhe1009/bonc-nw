package com.ljz.entity.datasource;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据源
 */
public class DataSource implements Serializable {

    private static final long serialVersionUID = -4377728998078641447L;

    private String dbId;//数据库链接标识

    private String dbAlias;//数据库链接别名，满足唯一性约束

    private String dbIp;//数据库链接IP

    private Integer dbPort;//数据库链接端口

    private String dbType;//数据库链接类型

    private String dbTypeName;//数据库链接类型名

    private String dbServiceName;//数据库服务名

    private String dbUsername;//数据库用户名

    private String dbPassword;//数据库密码

    private String isPool;//是否启用数据库连接池

    private Integer maxConnect;//最大连接数

    private Integer maxIdle;//最大活跃连接数

    private transient String createPerson;//创建人

    private transient Date createDate;//创建时间

    private transient String updatePerson;//修改人

    private transient Date updateDate;//修改时间

    private transient String userId;//租户

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getDbAlias() {
        return dbAlias;
    }

    public void setDbAlias(String dbAlias) {
        this.dbAlias = dbAlias;
    }

    public String getDbIp() {
        return dbIp;
    }

    public void setDbIp(String dbIp) {
        this.dbIp = dbIp;
    }

    public Integer getDbPort() {
        return dbPort;
    }

    public void setDbPort(Integer dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbTypeName() {
        return dbTypeName;
    }

    public void setDbTypeName(String dbTypeName) {
        this.dbTypeName = dbTypeName;
    }

    public String getDbServiceName() {
        return dbServiceName;
    }

    public void setDbServiceName(String dbServiceName) {
        this.dbServiceName = dbServiceName;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getIsPool() {
        return isPool;
    }

    public void setIsPool(String isPool) {
        this.isPool = isPool;
    }

    public Integer getMaxConnect() {
        return maxConnect;
    }

    public void setMaxConnect(Integer maxConnect) {
        this.maxConnect = maxConnect;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
