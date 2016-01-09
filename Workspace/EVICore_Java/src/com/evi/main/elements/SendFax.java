package com.evi.main.elements;

import com.audium.server.session.DecisionElementData;
import com.audium.server.voiceElement.DecisionElementBase;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.ExitState;
import com.audium.server.voiceElement.Setting;
import com.evi.main.common.IVRConstants;

public class SendFax extends DecisionElementBase implements ElementInterface{
	
	public String doDecision(String name, DecisionElementData decisionData){
		String statusFlag  = IVRConstants.error;
		
		return statusFlag;
	}
	
	public String getElementName(){
		return "SendFax";
	}
	
	public String getDisplayFolderName(){
		return "Evicore";
	}
	
	public String getDescription(){
		return "Send Fax";
	}
	
	public Setting[] getSettings() throws ElementException{
		Setting[] settingArray = new Setting[1];
		
		settingArray[0] = new Setting(
				IVRConstants.faxNumber,
				IVRConstants.faxNumber,
				IVRConstants.faxNumber,
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
