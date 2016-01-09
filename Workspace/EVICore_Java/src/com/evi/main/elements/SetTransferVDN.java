package com.evi.main.elements;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.audium.server.session.ActionElementData;
import com.audium.server.voiceElement.ActionElementBase;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.Setting;
import com.audium.server.xml.ActionElementConfig;
import com.evi.main.common.IVRConstants;
import com.evi.main.utils.IVRUtils;

public class SetTransferVDN extends ActionElementBase implements ElementInterface{
	private static Logger logger = Logger.getLogger(SetTransferVDN.class);
	
	public void doAction(String name, ActionElementData actionData){
		try{
			ActionElementConfig config = actionData.getActionElementConfig();
			
			String vdn = (String) config.getSettingValue(IVRConstants.transferType, actionData);
			
			logger.debug("Name: "+vdn);
			
			Properties prop = (Properties) actionData.getSessionData(IVRConstants.applicationProperties);
			
			String transferVDN = IVRUtils.getPropertyValue(prop, vdn);
			
			IVRUtils.setSessionDataAndLogAppLogAndLog4j(actionData, IVRConstants.transferVDN, transferVDN);
			
			}
			catch(Exception e){
				logger.error("Exception",e);
			}
	}
	
	public String getElementName(){
		return "SetTransferVDN";
	}
	
	public String getDisplayFolderName(){
		return "Evicore";
	}
	
	public String getDescription(){
		return "SetTransferVDN";
	}
	
	public Setting[] getSettings() throws ElementException{
		Setting[] settingArray = new Setting[1];
		
		settingArray[0] = new Setting(
				IVRConstants.transferType,
				IVRConstants.transferType,
				IVRConstants.transferType,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				new String[]{IVRConstants.mainMenuVDN,IVRConstants.agentReqVDN,IVRConstants.disconnectVDN,IVRConstants.errorVDN,
						IVRConstants.nurseLineVDN,IVRConstants.surveyVDN,IVRConstants.timeoutVDN});
		
		return settingArray;
	}

}
