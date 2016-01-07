package com.evi.main.elements;

import org.apache.log4j.Logger;

import com.audium.server.session.DecisionElementData;
import com.audium.server.voiceElement.DecisionElementBase;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.ExitState;
import com.audium.server.voiceElement.Setting;
import com.audium.server.xml.DecisionElementConfig;
import com.evi.main.common.IVRConstants;

public class DecisionWith7Exits extends DecisionElementBase implements ElementInterface{
	private static Logger logger = Logger.getLogger(DecisionWith7Exits.class);
	
	public String doDecision(String name, DecisionElementData decisionData){
		String flag = IVRConstants.error;
		try{
		DecisionElementConfig config = decisionData.getDecisionElementConfig();
		
		String sessionValue = (String) config.getSettingValue(IVRConstants.SessionValue, decisionData);
		String value = (String) decisionData.getSessionData(sessionValue);
		String comparevalue1 = (String) config.getSettingValue(IVRConstants.comparevalue1, decisionData);
		String comparevalue2 = (String) config.getSettingValue(IVRConstants.comparevalue2, decisionData);
		String comparevalue3 = (String) config.getSettingValue(IVRConstants.comparevalue3, decisionData);
		String comparevalue4 = (String) config.getSettingValue(IVRConstants.comparevalue4, decisionData);
		String comparevalue5 = (String) config.getSettingValue(IVRConstants.comparevalue5, decisionData);
		String comparevalue6 = (String) config.getSettingValue(IVRConstants.comparevalue6, decisionData);
		String comparevalue7 = (String) config.getSettingValue(IVRConstants.comparevalue7, decisionData);
		
		logger.debug("value: "+value);
		logger.debug("Comp1: "+comparevalue1);
		logger.debug("Comp2: "+comparevalue2);
		
		if(null!=value){
			if(value.equalsIgnoreCase(comparevalue1)){
				return IVRConstants.comparevalue1;
			}
			
			else if(value.equalsIgnoreCase(comparevalue2)){
				return IVRConstants.comparevalue2;
			}
			else if(value.equalsIgnoreCase(comparevalue3)){
				return IVRConstants.comparevalue3;
			}
			else if(value.equalsIgnoreCase(comparevalue4)){
				return IVRConstants.comparevalue4;
			}
			else if(value.equalsIgnoreCase(comparevalue5)){
				return IVRConstants.comparevalue5;
			}
			else if(value.equalsIgnoreCase(comparevalue6)){
				return IVRConstants.comparevalue6;
			}
			else if(value.equalsIgnoreCase(comparevalue7)){
				return IVRConstants.comparevalue7;
			}
			else{
				return IVRConstants.novalue;
			}
		}
		else{
			return IVRConstants.novalue;
		}
		}
		catch(Exception e){
			logger.error("Exception",e);
		}
		return flag;
	}
	
	public String getElementName(){
		return "DecisionWith7Exits";
	}
	
	public String getDisplayFolderName(){
		return "Evicore";
	}
	
	public String getDescription(){
		return "Decision to compare Session data";
	}
	
	public Setting[] getSettings() throws ElementException{
		Setting[] settingArray = new Setting[8];
		
		settingArray[0] = new Setting(
				IVRConstants.SessionValue,
				IVRConstants.SessionValue,
				"value",
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		settingArray[1] = new Setting(
				IVRConstants.comparevalue1,
				IVRConstants.comparevalue1,
				IVRConstants.comparevalue1,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		settingArray[2] = new Setting(
				IVRConstants.comparevalue2,
				IVRConstants.comparevalue2,
				IVRConstants.comparevalue2,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		settingArray[3] = new Setting(
				IVRConstants.comparevalue3,
				IVRConstants.comparevalue3,
				IVRConstants.comparevalue3,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		settingArray[4] = new Setting(
				IVRConstants.comparevalue4,
				IVRConstants.comparevalue4,
				IVRConstants.comparevalue4,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		settingArray[5] = new Setting(
				IVRConstants.comparevalue5,
				IVRConstants.comparevalue5,
				IVRConstants.comparevalue5,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		settingArray[6] = new Setting(
				IVRConstants.comparevalue6,
				IVRConstants.comparevalue6,
				IVRConstants.comparevalue6,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		settingArray[7] = new Setting(
				IVRConstants.comparevalue7,
				IVRConstants.comparevalue7,
				IVRConstants.comparevalue7,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		return settingArray;
	}
	
	public ExitState[] getExitStates() throws ElementException{
		ExitState[] exitStateArray = new ExitState[9];
		
		exitStateArray[0] = new ExitState(IVRConstants.comparevalue1,IVRConstants.comparevalue1,IVRConstants.comparevalue1);
		exitStateArray[1] = new ExitState(IVRConstants.comparevalue2,IVRConstants.comparevalue2,IVRConstants.comparevalue2);
		exitStateArray[2] = new ExitState(IVRConstants.comparevalue3,IVRConstants.comparevalue3,IVRConstants.comparevalue3);
		exitStateArray[3] = new ExitState(IVRConstants.comparevalue4,IVRConstants.comparevalue4,IVRConstants.comparevalue4);
		exitStateArray[4] = new ExitState(IVRConstants.comparevalue5,IVRConstants.comparevalue5,IVRConstants.comparevalue5);
		exitStateArray[5] = new ExitState(IVRConstants.comparevalue6,IVRConstants.comparevalue6,IVRConstants.comparevalue6);
		exitStateArray[6] = new ExitState(IVRConstants.comparevalue7,IVRConstants.comparevalue7,IVRConstants.comparevalue7);
		exitStateArray[7] = new ExitState(IVRConstants.novalue,IVRConstants.novalue,IVRConstants.novalue);
		exitStateArray[8] = new ExitState(IVRConstants.error,IVRConstants.error,IVRConstants.error);
		
		return exitStateArray;
	}

}
