package com.evi.main.java;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.audium.server.AudiumException;
import com.audium.server.proxy.StartCallInterface;
import com.audium.server.session.CallStartAPI;
import com.evi.main.common.IVRConstants;
import com.evi.main.utils.IVRUtils;


public class OnCallStart implements StartCallInterface{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(OnCallStart.class.getName());
	
	@Override
	public void onStartCall(CallStartAPI callStart) throws AudiumException {
		
				Properties appProperties = new Properties();
				try{
					
					String propFileName = "/evicore.properties";
					InputStream props = null;        
			        try {            
			            props = OnCallStart.class.getResourceAsStream(propFileName);  
			            appProperties.load(props);  
			            logger.debug("Properties read: "+props);
			        } catch (Exception e) {
			            logger.error("[OnCallStart] error opening servicingIvr.properties file",e);
			        }
			              
			            callStart.setSessionData(IVRConstants.applicationProperties, appProperties);
			            
			            String audioPath = IVRUtils.getPropertyValue(appProperties, IVRConstants.audioPath);
			            IVRUtils.setSessionDataAndLogAppLogAndLog4j(callStart, IVRConstants.audioPath, audioPath);
			            
			            String applicationMode = IVRUtils.getPropertyValue(appProperties, IVRConstants.applicationMode);
			            IVRUtils.setSessionDataAndLogAppLogAndLog4j(callStart, IVRConstants.applicationMode, applicationMode);
			            
			            String midConfidence = IVRUtils.getPropertyValue(appProperties, IVRConstants.defaultMidConfidence);
			    		IVRUtils.setSessionDataAndLogAppLogAndLog4j(callStart, IVRConstants.defaultMidConfidence, midConfidence);
			            
			            if("test".equalsIgnoreCase(applicationMode)){
			            	IVRUtils.loadAllTestData(callStart,appProperties);
			            }
			            
				}
				catch(Exception e){
									
				}
	}
}
