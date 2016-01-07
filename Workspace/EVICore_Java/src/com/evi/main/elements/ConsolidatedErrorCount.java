package com.evi.main.elements;


import java.util.HashMap;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.audium.core.vfc.VException;
import com.audium.core.vfc.VPreference;
import com.audium.core.vfc.audio.VAudio;
import com.audium.core.vfc.form.VBlock;
import com.audium.core.vfc.form.VForm;
import com.audium.core.vfc.util.VAction;
import com.audium.core.vfc.util.VMain;
import com.audium.server.session.VoiceElementData;
import com.audium.server.voiceElement.AudioGroup;
import com.audium.server.voiceElement.AudiumElement;
import com.audium.server.voiceElement.ElementData;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ExitState;
import com.audium.server.voiceElement.Setting;
import com.audium.server.voiceElement.VoiceElementBase;
import com.audium.server.xml.VoiceElementConfig;


/**
 * 
 * Class to handle consolidated error count.
 * 
 * To use, place in the call flow after a voice (menu or input) element. Set that element to have max NI and NM
 * count of 1 with silence for the NI and NM audios. This element will handle counting to the max number of errors 
 * and will play the appropriate NI or NM audio. The Continue exit state should connect back to the same input
 * element for reprompting. The MaxError exit state should connect to whatever logic will execute when the
 * caller hits the max number of errors.
 */

public class ConsolidatedErrorCount extends VoiceElementBase implements AudiumElement {

	private Logger logger = Logger.getLogger(ConsolidatedErrorCount.class.getName());

	private static String NI_ERROR_AUDIO_NAME = "No Input";
	private static String NM_ERROR_AUDIO_NAME = "No Match";
	private static String MAXCOUNT = "maxCount";
	private static String CURRENT_COUNT = "currentCount";
	private static String EXIT_CONTINUE = "Continue";
	private static String EXIT_MAXERROR = "MaxError";
	private static String NI_COUNT_SESSION_VAR = "ConsolidatedErrorCount_NI";
	private static String NM_COUNT_SESSION_VAR = "ConsolidatedErrorCount_NM";
	private static String COUNT_ELEMENT_VAR = "count";

	public String getElementName()
	{
		return "ConsolidatedErrorCount";
	}

	
	public String getDescription() 
	{
		return "This element implements a consoldiated error count, containing the counter as well as audio files.";
	}

	
	public String getDisplayFolderName()
	{
		return "Evicore";
	}

	/**
	 * Returns the Exit States
	 * @return  The custom element Exit States as an arrya
	 * @exception throws Element Exception
	 */
	public ExitState[] getExitStates() throws ElementException {
		//Returns Exit States
		ExitState[] exitStateArray = new ExitState[2];


		exitStateArray[0] = new ExitState(EXIT_CONTINUE,EXIT_CONTINUE,"Continue Prompting");            
		exitStateArray[1] = new ExitState(EXIT_MAXERROR,EXIT_MAXERROR,"Max Errors Reached");          

		return exitStateArray;
	}


	/**
	 * Returns the element Settings
	 * @return  The custom element settings
	 * @exception throws Element Exception
	 */
	public Setting[] getSettings() throws ElementException {
		//returns settings
		Setting[] settingArray = new Setting[2];              

		settingArray[0] =
			new Setting(
					MAXCOUNT,
					"Max Error Count",
					"Max Error Count, the maximum number of errors allowed",
					Setting.REQUIRED,
					Setting.SINGLE,
					Setting.SUBSTITUTION_ALLOWED,
					Setting.INT);
		settingArray[1] =
			new Setting(
					CURRENT_COUNT,
					"Current Count",
					"Current Count, the current count going into this element",
					Setting.REQUIRED,
					Setting.SINGLE,
					Setting.SUBSTITUTION_ALLOWED,
					Setting.INT);



		// set setting defaults                         
		settingArray[0].setDefaultValue("3");
		settingArray[1].setDefaultValue("0");
		
		return settingArray;    

	}

	/**
	 * Returns the Aduio group display order
	 * @return  The audio group display order as array of strings
	 */
	public String[] getAudioGroupDisplayOrder() {
		String [] groups = new String[2];
		groups[0] = NI_ERROR_AUDIO_NAME;
		groups[1] = NM_ERROR_AUDIO_NAME;
		
		return groups;
	}

	/**
	 * Returns the Audio Groups
	 * @return  The HashMap of Audio Groups
	 * @exception throws Element Exception
	 */
	public HashMap getAudioGroups() throws ElementException {

		HashMap<String,AudioGroup[]> groups = new HashMap<String,AudioGroup[]>();
		AudioGroup[] groupArray1 = new AudioGroup[1];
		groupArray1[0] = new AudioGroup(NM_ERROR_AUDIO_NAME,"No Match Audio","Audio to play for a no match",false,false);

		AudioGroup[] groupArray2 = new AudioGroup[1];
		groupArray2[0] = new AudioGroup(NI_ERROR_AUDIO_NAME,"No Input Audio","Audio to play for a no input",false,false);
	
		groups.put(NM_ERROR_AUDIO_NAME,groupArray1);
		groups.put(NI_ERROR_AUDIO_NAME,groupArray2);
		
		return groups;          
	}


	/**
	 * Returns the element Data
	 * 
	 * @return  The custom element Data as an array
	 * @exception throws Element Exception
	 */

	public ElementData[] getElementData() throws ElementException {
		//Returns Element Data
		ElementData[] elementDataArray = new ElementData[1];

		elementDataArray[0] = new ElementData("count","Current count for this element");         

		return elementDataArray;            
	}

	/**
	 * The actual body of the element, executes the logic and returns the appropriate exit state
	 * 
	 * @return  The element exit state
	 * @exception throws Element Exception
	 * * @exception throws VException
	 */
	public String addXmlBody(
			VMain vxml,
			Hashtable reqParameters,
			VoiceElementData md)
	throws VException, ElementException {

		try {
			VoiceElementConfig config = md.getVoiceElementConfig();
			VPreference pref = md.getPreference();

			int maxCount = new Integer(config.getSettingValue(MAXCOUNT, md)); 
			int count = new Integer(config.getSettingValue(CURRENT_COUNT, md)); 
			
			// prepend the current element name to allow for multiple simultaneous counters to be running
			Integer nm = (Integer)md.getSessionData(md.getCurrentElement()+NM_COUNT_SESSION_VAR);
			Integer ni = (Integer)md.getSessionData(md.getCurrentElement()+NI_COUNT_SESSION_VAR);
			//if (logger.isDebugEnabled()) {
				System.out.println("max count: "+maxCount);
				System.out.println("ni fetched: "+nm+" nm fetched: "+ni);
				System.out.println("current count: "+count);
			//}

			// initialize NM if we've never set the session variable OR if the current count is 0
			if (nm == null || count == 0) {
				nm = 0;
			}
			if (ni == null || count == 0) {
				ni = 0;
			}
			
			
			// now that we're all set up, increment the total count
			count++;
			
			// use the previous element's exit state to determine if this is NM or NI 
			// and increment that counter also so we always play the right audio
			VoiceElementConfig.AudioGroup audioPrompt = null;
			String exitState = md.getExitStateHistory().get(md.getExitStateHistory().size()-1);              
			if (exitState.equals("max_noinput")) {
				ni++;                   
				audioPrompt = config.getAudioGroup(NI_ERROR_AUDIO_NAME,count);            
			} else {      
				// default to nomatch, allows us to also use NM to handle semantic failures
				nm++;             
				System.out.println("Fetching NM audio "+nm);
				audioPrompt = config.getAudioGroup(NM_ERROR_AUDIO_NAME,count); 				                
			} 
			
			if (audioPrompt == null) {
				audioPrompt=config.getAudioGroup(NM_ERROR_AUDIO_NAME,1);				
			} 

			
			md.setElementData(COUNT_ELEMENT_VAR,String.valueOf(count));
			
			// Configure the VXML form                
			VForm form = VForm.getNew(pref, "start");                               
			VBlock block = VBlock.getNew(pref, "play"); 
			VAudio vaudioList = VAudio.getNew(pref);
			vaudioList.add(audioPrompt.constructAudio(md));
			block.add(vaudioList);
			
			block.setSubmitThis(true);
			
			VAction submitAction = VAction.getNew(pref, VAction.SUBMIT, getSubmitURL());
			block.add(submitAction);
			form.add(block);        
			vxml.add(form);

			if (count < maxCount) {
				// store counts for the next iteration of this element
				md.setSessionData(md.getCurrentElement()+NM_COUNT_SESSION_VAR, Integer.valueOf(nm));
				md.setSessionData(md.getCurrentElement()+NI_COUNT_SESSION_VAR, Integer.valueOf(ni));
			
				return EXIT_CONTINUE;
			}

			// since we will return Max Error below, clear counts in case this element is used again
			md.setSessionData(md.getCurrentElement()+NM_COUNT_SESSION_VAR, Integer.valueOf(0));
			md.setSessionData(md.getCurrentElement()+NI_COUNT_SESSION_VAR, Integer.valueOf(0));
		
		} catch (ElementException elm) {
			throw elm;
		} catch (Exception ex) {
			throw new ElementException("Exception in ConsolidatedErrorCount",ex);
		}
		
		return EXIT_MAXERROR;    
	}
}