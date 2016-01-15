package com.evi.main.elements;

import java.util.List;
import java.util.Properties;

import org.w3c.dom.Document;

import com.audium.server.session.DecisionElementData;
import com.audium.server.voiceElement.DecisionElementBase;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.ExitState;
import com.audium.server.voiceElement.Setting;
import com.audium.server.xml.DecisionElementConfig;
import com.evi.main.common.IVRConstants;
import com.evi.main.java.CptCodesBean;
import com.evi.main.java.LoadCaseBean;
import com.evi.main.java.ReadLoadCaseDocument;
import com.evi.main.utils.IVRUtils;

public class LookupCase extends DecisionElementBase implements ElementInterface{
	
	public String doDecision(String name, DecisionElementData decisionData){
		String statusFlag  = IVRConstants.error;
		try{
		DecisionElementConfig config = (DecisionElementConfig) decisionData.getDecisionElementConfig();
		
		String caseID = (String) config.getSettingValue(IVRConstants.caseID, decisionData);
		String taxID  = (String) config.getSettingValue(IVRConstants.taxID, decisionData);
		
		String applicationmode = (String) decisionData.getSessionData(IVRConstants.applicationMode);
		
		 String StatusCode=null;
		 String AuthNum=null;
		 String PatientName = null;
		 String PhysName = null;
		 String FacilityName =null;
		 String EffDate = null;
		 String ExpDate = null;
		 String FaxNum = null;
		 String NumOptCodes =null;
		 List<CptCodesBean> OptCodes =null;
		 
		 Document doc = null;
		
		 String caseId = IVRUtils.getSessionDataString(decisionData, IVRConstants.caseID);
		 
		if("test".equalsIgnoreCase(applicationmode)){
			Properties appProperties = (Properties) decisionData.getSessionData(IVRConstants.applicationProperties);
			StatusCode = IVRUtils.getPropertyValue(appProperties, IVRConstants.StatusCode);
			AuthNum = IVRUtils.getPropertyValue(appProperties, IVRConstants.AuthNum);
			PatientName = IVRUtils.getPropertyValue(appProperties, IVRConstants.PatientName);
			PhysName = IVRUtils.getPropertyValue(appProperties, IVRConstants.PhysName);
			FacilityName = IVRUtils.getPropertyValue(appProperties, IVRConstants.FacilityName);
			EffDate = IVRUtils.getPropertyValue(appProperties, IVRConstants.EffDate);
			ExpDate = IVRUtils.getPropertyValue(appProperties, IVRConstants.ExpDate);
			FaxNum = IVRUtils.getPropertyValue(appProperties, IVRConstants.FaxNum);
			NumOptCodes = IVRUtils.getPropertyValue(appProperties, IVRConstants.NumOptCodes);
			//OptCodes = IVRUtils.getPropertyValue(appProperties, IVRConstants.OptCodes);
		}
		else{
		if("stub".equalsIgnoreCase(applicationmode)){
			doc = IVRUtils.getDocument(caseId+"_res.xml");
		}
		else{
			
		}
		ReadLoadCaseDocument rLCD = new ReadLoadCaseDocument();
		LoadCaseBean lCB = rLCD.getLookupCaseBean(doc);
		
		StatusCode = lCB.getStatusCode();
		AuthNum = lCB.getAuthNum();
		PatientName = lCB.getPatientName();
		PhysName = lCB.getPhysName();
		FacilityName = lCB.getFacilityName();
		EffDate = lCB.getEffDate();
		ExpDate = lCB.getExpDate();
		FaxNum = lCB.getFaxNum();
		NumOptCodes = lCB.getNumOptCodes();
		OptCodes = lCB.getOptCodes();
		}
		
		
		IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.StatusCode, StatusCode);
		IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.AuthNum, AuthNum);
		IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.PatientName, PatientName);
		IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.PhysName, PhysName);
		IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.FacilityName, FacilityName);
		IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.EffDate, EffDate);
		IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.ExpDate, ExpDate);
		IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.FaxNum, FaxNum);
		IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.NumOptCodes, NumOptCodes);
		IVRUtils.setSessionDataAndLogAppLogAndLog4jList(decisionData, IVRConstants.OptCodes, OptCodes);
		
		IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.CaseNumberValid, "Yes");
		
		}
		catch(Exception e){
			
		}
		return statusFlag;
	}
	
	public String getElementName(){
		return "LookUpCase";
	}
	
	public String getDisplayFolderName(){
		return "Evicore";
	}
	
	public String getDescription(){
		return "LookUp Case";
	}
	
	public Setting[] getSettings() throws ElementException{
		Setting[] settingArray = new Setting[2];
		
		settingArray[0] = new Setting(
				IVRConstants.caseID,
				IVRConstants.caseID,
				IVRConstants.caseID,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		settingArray[1] = new Setting(
				IVRConstants.taxID,
				IVRConstants.taxID,
				IVRConstants.taxID,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
	
		
		return settingArray;
	}
	
	public ExitState[] getExitStates() throws ElementException{
		ExitState[] exitStateArray = new ExitState[2];
		exitStateArray[0] = new ExitState(IVRConstants.success,IVRConstants.success,IVRConstants.success);
		exitStateArray[1] = new ExitState(IVRConstants.error,IVRConstants.error,IVRConstants.error);
		
		return exitStateArray;
	}

}
