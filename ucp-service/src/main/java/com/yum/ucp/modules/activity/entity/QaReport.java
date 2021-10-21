/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.entity;

import org.hibernate.validator.constraints.Length;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 测试报告Entity
 * @author Zachary
 * @version 2019-08-15
 */
public class QaReport extends DataEntity<QaReport> {
	
	private static final long serialVersionUID = 1L;
	private String verif;		// 核销是否通过
	private String actId;		// 活动ID
	private String verifRemark;		// 核销备注
	private String emerge;		// 展现是否通过
	private String emergeRemark;		// 展现备注
	private String eatinRemark;		// 堂食备注
	private String mobileRemark;		// 手机点餐备注
	private String outwardRemark;		// 外送备注
	private String takeoutRemark;		// 外带备注
	private String kioskRemark;		// kiosk备注
	private String silverSecondRemark;		// 银二代不满足条件报错备注
	private String posRemark;		// POS备注
	private String invoiceRemark;		// 发票备注
	private String otherRemark;		// 其他备注
	
	public QaReport() {
		super();
	}

	public QaReport(String id){
		super(id);
	}

	@Length(min=0, max=1, message="核销是否通过长度必须介于 0 和 1 之间")
	public String getVerif() {
		return verif;
	}

	public void setVerif(String verif) {
		this.verif = verif;
	}
	
	@Length(min=0, max=75, message="活动ID长度必须介于 0 和 75 之间")
	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}
	
	@Length(min=0, max=1000, message="核销备注长度必须介于 0 和 1000 之间")
	public String getVerifRemark() {
		return verifRemark;
	}

	public void setVerifRemark(String verifRemark) {
		this.verifRemark = verifRemark;
	}
	
	@Length(min=0, max=1, message="展现是否通过长度必须介于 0 和 1 之间")
	public String getEmerge() {
		return emerge;
	}

	public void setEmerge(String emerge) {
		this.emerge = emerge;
	}
	
	@Length(min=0, max=1000, message="展现备注长度必须介于 0 和 1000 之间")
	public String getEmergeRemark() {
		return emergeRemark;
	}

	public void setEmergeRemark(String emergeRemark) {
		this.emergeRemark = emergeRemark;
	}
	
	@Length(min=0, max=1000, message="堂食备注长度必须介于 0 和 1000 之间")
	public String getEatinRemark() {
		return eatinRemark;
	}

	public void setEatinRemark(String eatinRemark) {
		this.eatinRemark = eatinRemark;
	}
	
	@Length(min=0, max=1000, message="手机点餐备注长度必须介于 0 和 1000 之间")
	public String getMobileRemark() {
		return mobileRemark;
	}

	public void setMobileRemark(String mobileRemark) {
		this.mobileRemark = mobileRemark;
	}
	
	@Length(min=0, max=255, message="外送备注长度必须介于 0 和 255 之间")
	public String getOutwardRemark() {
		return outwardRemark;
	}

	public void setOutwardRemark(String outwardRemark) {
		this.outwardRemark = outwardRemark;
	}
	
	@Length(min=0, max=255, message="外带备注长度必须介于 0 和 255 之间")
	public String getTakeoutRemark() {
		return takeoutRemark;
	}

	public void setTakeoutRemark(String takeoutRemark) {
		this.takeoutRemark = takeoutRemark;
	}
	
	@Length(min=0, max=255, message="kiosk备注长度必须介于 0 和 255 之间")
	public String getKioskRemark() {
		return kioskRemark;
	}

	public void setKioskRemark(String kioskRemark) {
		this.kioskRemark = kioskRemark;
	}
	
	@Length(min=0, max=255, message="银二代不满足条件报错备注长度必须介于 0 和 255 之间")
	public String getSilverSecondRemark() {
		return silverSecondRemark;
	}

	public void setSilverSecondRemark(String silverSecondRemark) {
		this.silverSecondRemark = silverSecondRemark;
	}
	
	@Length(min=0, max=255, message="POS备注长度必须介于 0 和 255 之间")
	public String getPosRemark() {
		return posRemark;
	}

	public void setPosRemark(String posRemark) {
		this.posRemark = posRemark;
	}
	
	@Length(min=0, max=255, message="发票备注长度必须介于 0 和 255 之间")
	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
	}
	
	@Length(min=0, max=255, message="其他备注长度必须介于 0 和 255 之间")
	public String getOtherRemark() {
		return otherRemark;
	}

	public void setOtherRemark(String otherRemark) {
		this.otherRemark = otherRemark;
	}
	
}