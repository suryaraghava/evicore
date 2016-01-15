package com.evi.main.elements;

import java.util.List;

import com.audium.server.session.DecisionElementData;
import com.audium.server.voiceElement.DecisionElementBase;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.ExitState;
import com.audium.server.voiceElement.Setting;
import com.audium.server.xml.DecisionElementConfig;
import com.evi.main.common.IVRConstants;
import com.evi.main.java.CptCodesBean;
import com.evi.main.utils.IVRUtils;

public class GetCPTCodes extends DecisionElementBase implements ElementInterface{
	
	public String doDecision(String name, DecisionElementData decisionData){
		String flag = IVRConstants.error;
		try{
		DecisionElementConfig config = (DecisionElementConfig) decisionData.getDecisionElementConfig();
		
		String currentCount = (String) config.getSettingValue(IVRConstants.currentCount, decisionData);
		
		String NumOptCodes = (String) IVRUtils.getSessionDataString(decisionData, IVRConstants.NumOptCodes);
		
		Integer int_NumOptCodes = Integer.valueOf(NumOptCodes);
		Integer int_currentCode = Integer.valueOf(currentCount);
		
		List<CptCodesBean> cptCodesBean = (List<CptCodesBean>) decisionData.getSessionData(IVRConstants.OptCodes);
		
		if(int_NumOptCodes>int_currentCode){
			String optCodesDesc = cptCodesBean.get(int_currentCode).getDescription();
			IVRUtils.setSessionDataAndLogAppLogAndLog4j(decisionData, IVRConstants.optCodeDesc, optCodesDesc);
			flag = IVRConstants.hasMoreCPTCodes;
		}
		else{
			flag = IVRConstants.hasNoMoreCPTCodes;
		}
				
		}
		catch(Exception e){
			
		}
		return flag;
	}
	
	public String getElementName(){
		return "GetCPTCodes";
	}
	
	public String getDisplayFolderName(){
		return "Evicore";
	}
	
	public String getDescription(){
		return "GetCPTCodes";
	}
	
	public Setting[] getSettings() throws ElementException{
		Setting[] settingArray = new Setting[1];
		
		settingArray[0] = new Setting(
				IVRConstants.currentCount,
				IVRConstants.currentCount,
				IVRConstants.currentCount,
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);
		
		return settingArray;
	}
	
	public ExitState[] getExitStates() throws ElementException{
		ExitState[] exitStateArray = new ExitState[3];
		exitStateArray[0] = new ExitState(IVRConstants.hasNoMoreCPTCodes,IVRConstants.hasNoMoreCPTCodes,IVRConstants.hasNoMoreCPTCodes);
		exitStateArray[1] = new ExitState(IVRConstants.error,IVRConstants.error,IVRConstants.error);
		exitStateArray[2] = new ExitState(IVRConstants.hasMoreCPTCodes,IVRConstants.hasMoreCPTCodes,IVRConstants.hasMoreCPTCodes);
		
		return exitStateArray;
	}

}
