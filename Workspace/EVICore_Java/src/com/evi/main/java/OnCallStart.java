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
			            logger.debug("Properties read: "+props);
			        } catch (Exception e) {
			            logger.error("[OnCallStart] error opening servicingIvr.properties file",e);
			        }
			        String hostname = null;
			        try {
			            InetAddress addr = InetAddress.getLocalHost();        
			            hostname = addr.getHostName().toLowerCase();
			            
			            callStart.addToLog(IVRConstants.hostname,hostname);
			            callStart.setSessionData(IVRConstants.hostname, hostname);
			        } catch (UnknownHostException ue) {
			            logger.error("[OnCallStart] Error getting hostname",ue);
			            hostname="Default";
			        }
			        
			            appProperties.load(props);    
			            callStart.setSessionData("Application_Properties", appProperties);
			            
			            String audioPath = appProperties.getProperty(IVRConstants.audioPath);
			            
			            callStart.setSessionData(IVRConstants.audioPath, audioPath);
			            
			            
				}
				catch(Exception e){
					
				
				}
	}

}
