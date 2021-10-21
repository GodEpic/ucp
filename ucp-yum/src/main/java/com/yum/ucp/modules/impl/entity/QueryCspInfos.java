package com.yum.ucp.modules.impl.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/13.
 */
public class QueryCspInfos implements Serializable {
    private String id;
    private String incidentNo;                //事件编号
    private String incidentDateStr;          //事件发生时间, 2018-01-15 16:00
    private String status;                                   //事件状态, 见附录：INCIDENT_STATUS
    private String statusDesc;                       //事件状态的描述
    private String type;                                    //事件类型,见附录：INCIDENT_TYPE
    private String typeDesc;                               //事件类型描述
    private String source;                             //来源编号
    private String sourceId;                                //来源系统里唯一标识该事件的ID,如果存在
    private String sourceUserCode;                       //各业务系统自己usercode,如KFC系统的user code等等.非必填
    private String businessUnitId;                   //品牌编号,来自主数据
    private String businessUnitName;                    //品牌名
    private String marketId;                             //市场ID
    private String marketName;                        //市场名
    private String stateId;                               //省份ID
    private String stateName;                             //省份名
    private String locationId;                           //地点ID
    private String locationName;                           //地点名
    private String deptId;                            //餐厅ID
    private String deptNo;                            //餐厅编号
    private String deptName;       //餐厅名字
    private String customerName;                       //投诉客户名字
    private String customerTelephone;             //投诉客户电话
    private String incidentDetails;           //投诉详情
    private String diningWay;                           //就餐方式编号, 见附录：INCIDENT_DINING_WAY
    private String diningWayDesc;                     //就餐方式描述
    private String orderDetails;           //订单详情
    private String imgList;         //投诉照片
    private String additionParams;
    private String tagid;
    private String imgUrls;

    private String token;//存储云的token

    public QueryCspInfos() {
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncidentNo() {
        return incidentNo;
    }

    public void setIncidentNo(String incidentNo) {
        this.incidentNo = incidentNo;
    }

    public String getIncidentDateStr() {
        return incidentDateStr;
    }

    public void setIncidentDateStr(String incidentDateStr) {
        this.incidentDateStr = incidentDateStr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceUserCode() {
        return sourceUserCode;
    }

    public void setSourceUserCode(String sourceUserCode) {
        this.sourceUserCode = sourceUserCode;
    }

    public String getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(String businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public String getBusinessUnitName() {
        return businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTelephone() {
        return customerTelephone;
    }

    public void setCustomerTelephone(String customerTelephone) {
        this.customerTelephone = customerTelephone;
    }

    public String getIncidentDetails() {
        return incidentDetails;
    }

    public void setIncidentDetails(String incidentDetails) {
        this.incidentDetails = incidentDetails;
    }

    public String getDiningWay() {
        return diningWay;
    }

    public void setDiningWay(String diningWay) {
        this.diningWay = diningWay;
    }

    public String getDiningWayDesc() {
        return diningWayDesc;
    }

    public void setDiningWayDesc(String diningWayDesc) {
        this.diningWayDesc = diningWayDesc;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getImgList() {
        return imgList;
    }

    public void setImgList(String imgList) {
        this.imgList = imgList;
    }

    public String getAdditionParams() {
        return additionParams;
    }

    public void setAdditionParams(String additionParams) {
        this.additionParams = additionParams;
    }
}
