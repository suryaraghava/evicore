package com.evi.main.elements;

import org.apache.log4j.Logger;

import com.audium.server.session.DecisionElementData;
import com.audium.server.voiceElement.DecisionElementBase;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ElementInterface;
import com.audium.server.voiceElement.ExitState;
import com.audium.server.voiceElement.Setting;
import com.audium.server.xml.DecisionElementConfig;

public class IsNullorEmpty extends DecisionElementBase implements ElementInterface{

	private static Logger logger = Logger.getLogger(IsNullorEmpty.class);
	
	public String doDecision(String name, DecisionElementData data){
		String statusFlag = "Yes";

		try{
			DecisionElementConfig config = data.getDecisionElementConfig();

			String sessionValue = (String) config.getSettingValue("SessionValue", data);
			String value = (String) data.getSessionData(sessionValue);
			if( value != null && !(value.isEmpty()) )
				statusFlag = "No";
		}
		catch(Exception e){
			logger.error("Exception",e);
		}
		return statusFlag;
	}

	public String getElementName(){
		return "IsNullorEmpty";
	}

	public String getDisplayFolderName(){
		return "Santosh";
	}

	public String getDescription(){
		return "Decision to check if Session data is null or empty";
	}

	public Setting[] getSettings() throws ElementException{
		Setting[] settingArray = new Setting[1];

		settingArray[0] = new Setting(
				"SessionValue",
				"SessionValue",
				"value",
				Setting.REQUIRED,
				Setting.SINGLE,
				Setting.SUBSTITUTION_ALLOWED,
				Setting.STRING);

		return settingArray;
	}

	public ExitState[] getExitStates() throws ElementException{
		ExitState[] exitStateArray = new ExitState[2];

		exitStateArray[0] = new ExitState("Yes","Yes","Yes");
		exitStateArray[1] = new ExitState("No","No","No");

		return exitStateArray;
	}

}
