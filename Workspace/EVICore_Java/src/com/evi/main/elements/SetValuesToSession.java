package com.evi.main.elements;

import org.apache.log4j.Logger;

import com.audium.server.session.ActionElementData;
import com.audium.server.voiceElement.ActionElementBase;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.Setting;
import com.audium.server.xml.ActionElementConfig;
import com.evi.main.common.IVRConstants;

public class SetValuesToSession extends ActionElementBase implements ElementInterface{
	private static Logger logger = Logger.getLogger(SetValuesToSession.class);
	
	public void doAction(String name, ActionElementData actionData){
		try{
		ActionElementConfig config = actionData.getActionElementConfig();
		
		String sessionName = (String) config.getSettingValue(IVRConstants.SessionName, actionData);
		String SessionValue = (String) config.getSettingValue(IVRConstants.SessionValue, actionData);
		
		logger.debug("Name: "+sessionName);
		logger.debug("Value: "+SessionValue);
		
		
		}
		catch(Exception e){
			logger.error("Exception",e);
		}
		
	}
	
	public String getElementName(){
		return "SetValuesToSession";
	}
	
	public String getDisplayFolderName(){
		return "Evicore";
	}
	
	public String getDescription(){
		return "SetValuesToSession";
	}
	
	public Setting[] getSettings() throws ElementException{
		Setting[] settingArray = new Setting[2];
		
		settingArray[0] = new Setting(
				IVRConstants.SessionName,
				IVRConstants.SessionName,
				"value",
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		settingArray[1] = new Setting(
				IVRConstants.SessionValue,
				IVRConstants.SessionValue,
				IVRConstants.SessionValue,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		
		
		
		
		return settingArray;
	}
	
	

}
