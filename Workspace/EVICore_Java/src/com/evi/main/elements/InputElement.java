package com.evi.main.elements;


import java.util.HashMap;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.audium.core.vfc.VException;
import com.audium.core.vfc.VPreference;
import com.audium.core.vfc.form.VBuiltInField;
import com.audium.core.vfc.form.VForm;
import com.audium.core.vfc.util.VAction;
import com.audium.core.vfc.util.VGrammar;
import com.audium.core.vfc.util.VMain;
import com.audium.core.vfc.util.VProperty;
import com.audium.server.session.VoiceElementData;
import com.audium.server.voiceElement.AudioGroup;
import com.audium.server.voiceElement.AudiumElement;
import com.audium.server.voiceElement.ElementData;
import com.audium.server.voiceElement.ElementException;
import com.audium.server.voiceElement.ExitState;
import com.audium.server.voiceElement.Setting;
import com.audium.server.voiceElement.VoiceElementBase;
import com.audium.server.voiceElement.util.NoInputPrompt;
import com.audium.server.voiceElement.util.NoMatchPrompt;
import com.audium.server.voiceElement.util.VoiceElementUtil;
import com.audium.server.xml.VoiceElementConfig;


public class InputElement extends VoiceElementBase implements AudiumElement {

	private Logger logger = Logger.getLogger(InputElement.class.getName());

	private static String SPEECH_ONLY_AUDIO_GROUP = "SpeechOnly";
	private static String BOTH_AUDIO_GROUP = "Both";
	private static String DTMF_ONLY_AUDIO_GROUP = "DTMFOnly";

	private static String MAX_ERRORS_OPTION = "MaxErrors";
	private static String INPUT_TYPE_OPTION = "inputType";
	private static String DTMF_GRAMMAR_OPTION = "dtmfGrammar";
	private static String SPEECH_GRAMMAR_OPTION = "speechGrammar";
	private static String CONFIDENCE_OPTION = "confidence";
	private static String SLOT_OPTION = "grammarSlot";

	private static String OPTION_X_RETURN_EXIT_STATE = "optionX";
	private static String MAXERROR_RETURN_EXIT_STATE = "maxError";

	private static String CURRENT_COUNT = "currentCount";
	private static String EXIT_CONTINUE = "Continue";
	private static String EXIT_MAXERROR = "MaxError";
	private static String NI_COUNT_SESSION_VAR = "ConsolidatedErrorCount_NI";
	private static String NM_COUNT_SESSION_VAR = "ConsolidatedErrorCount_NM";
	private static String COUNT_ELEMENT_VAR = "count";
	
	private static String DEFAULT_CONFIDENCE = "0.40";
	
	private static String FIELD = "collectField";
	
	/**
	 * Returns the element name
	 * @return  The custom element name
	 */

	public String getElementName() {
		//Returns the element name
		return "InputElement";
	} 

	/**
	 * Returns the custom element description
	 * @return  The custom element description
	 */
	public String getDescription() {
		//Returns Description
		return "Create a menu that can function dynamically as voice or dtmf or both";
	}

	public String getDisplayFolderName() {
		//Returns Display Folder Name
		return "Evicore";
	}

	/**
	 * Returns the Exit States
	 * @return  The custom element Exit States as an array
	 * @exception throws Element Exception
	 */
	public ExitState[] getExitStates() throws ElementException {
		//Returns Exit States
		ExitState[] exitStateArray = new ExitState[2];
		exitStateArray[0] = new ExitState(OPTION_X_RETURN_EXIT_STATE,OPTION_X_RETURN_EXIT_STATE,"Indicates a menu option was chosen, check the value and utterance to find out which");
		exitStateArray[1] = new ExitState(MAXERROR_RETURN_EXIT_STATE,MAXERROR_RETURN_EXIT_STATE,"Maximum number of NI or NM inputs has been reached");
		
		return exitStateArray;		
	}


	/**
	 * Returns the element Settings
	 * @return  The custom element settings
	 * @exception throws Element Exception
	 */
	public Setting[] getSettings() throws ElementException {
		Setting[] settingArray = new Setting[6];			

		settingArray[0] =
				new Setting(
						INPUT_TYPE_OPTION,
						"Input type allowed",
						"Input allowed - DTMF, speech, or both",
						Setting.REQUIRED,
						Setting.SINGLE,
						Setting.SUBSTITUTION_ALLOWED,
						new String[]{"both","dtmf","speech"});		

		settingArray[0].setDefaultValue("both");
		
		settingArray[1] = 
				new Setting(
						MAX_ERRORS_OPTION,
						"Max NINM Errors",
						"Max Consolidated No Input and No Match count",
						Setting.REQUIRED,
						Setting.SINGLE,
						Setting.SUBSTITUTION_ALLOWED,
						Setting.INT);	

		settingArray[1].setDefaultValue("3");

		settingArray[2] = 
				new Setting(
						CONFIDENCE_OPTION,
						"Confidence",
						"Minimum confidence for successful recognition",
						Setting.OPTIONAL,
						Setting.SINGLE,
						Setting.SUBSTITUTION_ALLOWED,
						Setting.STRING);	

		settingArray[3] = 
				new Setting(
						SLOT_OPTION,
						"Slot Name",
						"Grammar slot name (must match slot defined in grammar)",
						Setting.OPTIONAL,
						Setting.SINGLE,
						Setting.SUBSTITUTION_ALLOWED,
						Setting.STRING);	
		
		settingArray[4] = 
				new Setting(
						DTMF_GRAMMAR_OPTION,
						"DTMF Grammar",
						"URL for DTMF grammar",
						Setting.OPTIONAL,
						Setting.SINGLE,
						Setting.SUBSTITUTION_ALLOWED,
						Setting.STRING);	

		settingArray[5] = 
				new Setting(
						SPEECH_GRAMMAR_OPTION,
						"Speech Grammar",
						"URL for Speech grammar",
						Setting.OPTIONAL,
						Setting.SINGLE,
						Setting.SUBSTITUTION_ALLOWED,
						Setting.STRING);	


		return settingArray;
	}

	/**
	 * Returns the Audio group display order
	 * @return  The audio group display order as array of strings
	 */
	public String[] getAudioGroupDisplayOrder() {
		String [] groups = new String[5];
		groups[0] = BOTH_AUDIO_GROUP;
		groups[1] = DTMF_ONLY_AUDIO_GROUP;
		groups[2] = SPEECH_ONLY_AUDIO_GROUP;
		groups[3] = "nomatch_audio_group";
		groups[4] = "noinput_audio_group";

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
		groupArray1[0] = new AudioGroup(BOTH_AUDIO_GROUP,"Both Audio","Audio to play when type is both",false,false);

		AudioGroup[] groupArray2 = new AudioGroup[1];
		groupArray2[0] = new AudioGroup(DTMF_ONLY_AUDIO_GROUP,"DTMF Only Audio","Audio to play when type is DTMF only",false,false);

		AudioGroup[] groupArray3 = new AudioGroup[1];
		groupArray3[0] = new AudioGroup(SPEECH_ONLY_AUDIO_GROUP,"Speech Only Audio","Audio to play when type is speech only",false,false);

		AudioGroup[] groupArray4 = new AudioGroup[1];
		groupArray4[0] = new AudioGroup(AudioGroup.NOMATCH);

		AudioGroup[] groupArray5 = new AudioGroup[1];
		groupArray5[0] = new AudioGroup(AudioGroup.NOINPUT);


		groups.put(BOTH_AUDIO_GROUP,groupArray1);
		groups.put(DTMF_ONLY_AUDIO_GROUP,groupArray2);
		groups.put(SPEECH_ONLY_AUDIO_GROUP,groupArray3);
		groups.put(groupArray4[0].getRealName(),groupArray4);
		groups.put(groupArray5[0].getRealName(),groupArray5);

		return groups;          
	}


	/**
	 * Returns the element Data
	 * 
	 * @return  The custom element Data as an array
	 * @exception throws Element Exception
	 */

	public ElementData[] getElementData() throws ElementException {
		ElementData [] data = new ElementData[3];
		data[0] = new ElementData(ElementData.VALUE); 
		data[1] = new ElementData(ElementData.CONFIDENCE);
		data[2] = new ElementData("utterance","Value entered");

		return data;
	}

	/**
	 * Functions pretty much as a regular menu from this point onward
	 * 
	 * @return  The custom element exit state
	 * @exception throws Element Exception
	 * * @exception throws VException
	 */
	public String addXmlBody(VMain vxml, Hashtable reqParameters, VoiceElementData md) 
			throws VException, ElementException {

		try {
			if (logger.isDebugEnabled())
				logger.debug("Start InputElement");

			
			String returnValue = null; 
			VoiceElementConfig config = md.getVoiceElementConfig();
			VPreference pref = md.getPreference();
			
			String inputType = config.getSettingValue(INPUT_TYPE_OPTION, md);

			// Get the submitted value, if any		
			String submitted = (String) reqParameters.get(FIELD);

			Integer errorCounter = ((Integer)md.getScratchData("errorCounter") != null ? (Integer)md.getScratchData("errorCounter") : 0);			
			Integer maxErrorCount = new Integer(config.getSettingValue(MAX_ERRORS_OPTION, md)); 
				
			String ninmType = null;
			if (logger.isDebugEnabled()) {
				logger.debug("Request Parameters: "+reqParameters.toString());
			}
			if (reqParameters.get("maxNoMatch")!=null || reqParameters.get("maxNoInput")!=null) {
				if (logger.isDebugEnabled()) {
					logger.debug("[InputElement] NINM path");
				}
				if (reqParameters.get("maxNoMatch") != null) {
					ninmType = "NM";
				} else {
					ninmType = "NI";
				}
				errorCounter++;
				if (errorCounter >= maxErrorCount) {
					return MAXERROR_RETURN_EXIT_STATE;
				} 
				else {
					md.setScratchData("errorCounter", errorCounter);
					// set submitted = null so we reconstruct the input form below
					submitted = null;
				}
			}
			
			if (submitted != null) { 
				md.setElementData("value",submitted);
				md.setElementData("confidence",(String)reqParameters.get("confidence"));
				md.addToLog("value", submitted);
				return OPTION_X_RETURN_EXIT_STATE;
			} 
			else {
				
				 // adds the confidence setting
	            VoiceElementUtil.addConfidenceProperty(vxml, inputType, config.getSettingValue(CONFIDENCE_OPTION, md), 
	            		DEFAULT_CONFIDENCE); 
	            
				// load the initial prompt				
				VForm form = VForm.getNew(pref, "start");
				VProperty props = form.getProperties();
				if (props == null)
					props = VProperty.getNew(pref);
								
				VBuiltInField field = null;
				
				VoiceElementConfig.AudioGroup errorPrompt = null;	
				if (ninmType != null) {
					if (ninmType.equals("NI")) {
						errorPrompt = config.getAudioGroup("noinput_audio_group",errorCounter); 
					} else {
						errorPrompt = config.getAudioGroup("nomatch_audio_group",errorCounter); 
					}
				}
				
				VoiceElementConfig.AudioGroup initialPrompt = null;							
				if (inputType != null && inputType.equals("speech")) {
					field = VBuiltInField.getNew(pref,VBuiltInField.GRAMMAR,FIELD,VBuiltInField.SPEECH);							
					initialPrompt = config.getAudioGroup(SPEECH_ONLY_AUDIO_GROUP,errorCounter+1); 
					props.add(VProperty.INPUT_MODES_ALLOWED,"voice");
				} else if (inputType != null && inputType.equals("dtmf")) {
					field = VBuiltInField.getNew(pref,VBuiltInField.GRAMMAR,FIELD,VBuiltInField.DTMF);
					initialPrompt = config.getAudioGroup(DTMF_ONLY_AUDIO_GROUP,errorCounter+1);					
					props.add(VProperty.INPUT_MODES_ALLOWED,"dtmf");
				} else {
					field = VBuiltInField.getNew(pref,VBuiltInField.GRAMMAR,FIELD,VBuiltInField.DTMF_SPEECH);
					initialPrompt = config.getAudioGroup(BOTH_AUDIO_GROUP,errorCounter+1); 
					props.add(VProperty.INPUT_MODES_ALLOWED,"dtmf voice");
				}
				if (errorPrompt != null) {
					initialPrompt.insertAudioItem(0,errorPrompt.getAudioItem(0));
				}
				
				// add in the grammars. Grammars define what the valid input is, and can be custom grammars
				// or builtin
				if (inputType != null && (inputType.equals("speech") || inputType.equals("both"))) {
					String grammarURL = config.getSettingValue(SPEECH_GRAMMAR_OPTION, md);
					if (grammarURL == null || grammarURL.length() == 0) {
						throw new ElementException("Speech Grammar is required when type is speech or both");
					}
					VGrammar speechGrammar = VGrammar.getNew(pref,true);
					speechGrammar.setSpeechSource(grammarURL);					
					field.setGrammar(speechGrammar);
				}
				if (inputType != null && (inputType.equals("dtmf") || inputType.equals("both"))) {
					String grammarURL = config.getSettingValue(DTMF_GRAMMAR_OPTION, md);
					if (grammarURL == null || grammarURL.length() == 0) {
						throw new ElementException("DTMF Grammar is required when type is DTMF or both");
					}
					VGrammar dtmfGrammar = VGrammar.getNew(pref,true);
					dtmfGrammar.setDtmfSource(grammarURL);				
					field.setGrammar(dtmfGrammar);
				}
				config.setAudioGroup(initialPrompt);
				
				String slotName = config.getSettingValue(SLOT_OPTION,md);
				if (slotName == null || slotName.length() == 0) {
					slotName = "out";
				}				
				field.setSlot(slotName);				
				
				form.setProperties(props);
				
				field.setSubmitThis(true);
				field.setOutsideSubmit(true);

				if (initialPrompt != null) {
					// Logs initial audio group				
					form.add(VoiceElementUtil.getInitialAudioGroupLogBlock(pref,null,"initial_audio_group",FIELD));
					field.setPromptCount(1, initialPrompt.constructAudio(md));
				}else{
					logger.error("[InputElement] An initial_audio_group is required for "+ md.getCurrentElement()+ ".");
					throw new ElementException("An initial_audio_group is required for "+ md.getCurrentElement()+ ".");
				}

				// nomatch and noinput events are set up, but they don't play the audios
				// the audios are added above before the re-prompt instead
				// NOMATCH
				NoMatchPrompt nomatch = new NoMatchPrompt(md, "nomatch_audio_group_dummy","max_nomatch_count",getSubmitURL(), 1);      
				field.add(nomatch.getEvent());
				//NOINPUT
				NoInputPrompt noinput = new NoInputPrompt(md, "nomatch_audio_group_dummy","max_noinput_count",getSubmitURL(), 1);
				field.add(noinput.getEvent());
				
				form.add(field);

				//SUBMIT ACTION
				VAction submitAction = VAction.getNew(pref,VAction.VARIABLE,"confidence",FIELD + "$.confidence",
						VAction.WITHOUT_QUOTES);
				submitAction.add(getSubmitURL(),VoiceElementBase.VXML_LOG_VARIABLE_NAME + " confidence");			

				// this action logs utterance, result, confidence, inputmode.
				// if there is a done prompt, this action also logs the done prompt.  if not, this action submits
				// the submitted value, confidence and the log variable to the Controller.
				VAction logAction = VoiceElementUtil.getUtteranceInteractionAssignment(FIELD + "$.utterance",
						FIELD + "$.inputmode",FIELD,FIELD + "$.confidence",pref);
				logAction.add(submitAction);

				field.add(logAction);				
				vxml.add(form);
				// drop down and return null below
			}
			return null;	
		}
		catch (Exception e) {
			// log any exception then throw it up so the default error node can catch it
			// and handle as appropriate for this IVR
			logger.error("Error executing custom menu element: ", e);
			if (!(e instanceof ElementException))
				throw new ElementException(e);
			else
				throw e;
		}
			
	}
}