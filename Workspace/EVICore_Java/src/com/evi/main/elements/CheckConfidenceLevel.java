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

public class CheckConfidenceLevel extends DecisionElementBase implements ElementInterface{
	private static Logger logger = Logger.getLogger(CheckConfidenceLevel.class);
	@Override
	public String doDecision(String name, DecisionElementData decisionData)
			{
		String statusFlag = IVRConstants.error;
		try{
		DecisionElementConfig config = decisionData.getDecisionElementConfig();
		Integer defaultConfidenceValue = Integer.valueOf((String) config.getSettingValue(IVRConstants.defaultMidConfidence, decisionData));
		Integer formConfidence = Integer.valueOf((String) config.getSettingValue(IVRConstants.formConfidence, decisionData));
		
		if(formConfidence<defaultConfidenceValue){
			statusFlag = IVRConstants.lowConfidence;
		}
		else{
			statusFlag = IVRConstants.highConfidence;
		}
		}
		catch(Exception e){
			logger.error("Exception",e);
		}
		return statusFlag;
	}
	
	public String getElementName(){
		return "CheckConfidenceLevel";
	}
	
	public String getDisplayFolderName(){
		return "Santosh";
	}
	
	public String getDescription(){
		return "Decision to CheckConfidenceLevel";
	}
	
	public Setting[] getSettings() throws ElementException{
		Setting[] settingArray = new Setting[2];
		
		settingArray[0] = new Setting(
				IVRConstants.defaultMidConfidence,
				IVRConstants.defaultMidConfidence,
				"value",
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		settingArray[1] = new Setting(
				IVRConstants.formConfidence,
				IVRConstants.formConfidence,
				IVRConstants.formConfidence,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		return settingArray;
	}
	
	public ExitState[] getExitStates() throws ElementException{
		ExitState[] exitStateArray = new ExitState[3];
		
		exitStateArray[0] = new ExitState(IVRConstants.highConfidence,IVRConstants.highConfidence,IVRConstants.highConfidence);
		exitStateArray[1] = new ExitState(IVRConstants.lowConfidence,IVRConstants.lowConfidence,IVRConstants.lowConfidence);
		exitStateArray[2] = new ExitState(IVRConstants.error,IVRConstants.error,IVRConstants.error);
		
		return exitStateArray;
	}

}
