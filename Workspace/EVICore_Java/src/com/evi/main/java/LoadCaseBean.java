package com.evi.main.java;

import java.util.List;

public class LoadCaseBean {
	
	 private String StatusCode;
	 private String AuthNum;
	 private String PatientName;
	 private String PhysName;
	 private String FacilityName;
	 private String EffDate;
	 private String ExpDate;
	 private String FaxNum;
	 private String NumOptCodes;
	 private List<CptCodesBean> cptCodes;
	 
	public String getStatusCode() {
		return StatusCode;
	}
	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}
	public String getAuthNum() {
		return AuthNum;
	}
	public void setAuthNum(String authNum) {
		AuthNum = authNum;
	}
	public String getPatientName() {
		return PatientName;
	}
	public void setPatientName(String patientName) {
		PatientName = patientName;
	}
	public String getPhysName() {
		return PhysName;
	}
	public void setPhysName(String physName) {
		PhysName = physName;
	}
	public String getFacilityName() {
		return FacilityName;
	}
	public void setFacilityName(String facilityName) {
		FacilityName = facilityName;
	}
	public String getEffDate() {
		return EffDate;
	}
	public void setEffDate(String effDate) {
		EffDate = effDate;
	}
	public String getExpDate() {
		return ExpDate;
	}
	public void setExpDate(String expDate) {
		ExpDate = expDate;
	}
	public String getFaxNum() {
		return FaxNum;
	}
	public void setFaxNum(String faxNum) {
		FaxNum = faxNum;
	}
	public String getNumOptCodes() {
		return NumOptCodes;
	}
	public void setNumOptCodes(String numOptCodes) {
		NumOptCodes = numOptCodes;
	}
	public List<CptCodesBean> getOptCodes() {
		return cptCodes;
	}
	public void setCptCodes(List<CptCodesBean> cptCodes) {
		this.cptCodes = cptCodes;
	}
	 
	 

}
