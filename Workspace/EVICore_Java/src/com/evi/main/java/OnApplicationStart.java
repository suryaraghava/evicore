package com.evi.main.java;

import java.net.URL;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.audium.server.AudiumException;
import com.audium.server.global.ApplicationStartAPI;
import com.audium.server.proxy.StartApplicationInterface;

/**
 * The Class OnApplicationStart.
 *
 * @author kavunuri
 */
public class OnApplicationStart implements StartApplicationInterface{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(OnApplicationStart.class
			.getName());
	
	/* (non-Javadoc)
	 * @see com.audium.server.proxy.StartApplicationInterface#onStartApplication(com.audium.server.global.ApplicationStartAPI)
	 */
	@Override
	public void onStartApplication(ApplicationStartAPI appStart)
			throws AudiumException {
		// TODO Auto-generated method stub
		
		 
			//initial log4j from log4j.properties file first
			try {

				URL url = OnApplicationStart.class.getResource("/log4j.xml");
				DOMConfigurator.configure(url);

				logger.info("eviCore application started");

			} catch (Exception e) {

				// init RootLogger as default
				logger = Logger.getRootLogger();

				logger.addAppender(new ConsoleAppender());
				
			} finally {
				logger.debug("Exiting Application Start");
			}

		}
	}



