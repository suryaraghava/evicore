/**
 * 
 * @author
 * 
 */
package com.evi.main.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.audium.server.session.APIBase;
import com.audium.server.session.DecisionElementData;
import com.audium.server.session.ElementAPI;

// TODO: Auto-generated Javadoc
/**
 * The Class IVRUtils.
 */
public class IVRUtils {
    
    /** The Constant logger. */
    private static final Logger logger = Logger.getLogger(IVRUtils.class.getName());
    
    /**
     * Gets the date format ddmmyyyy.
     *
     * @param dateParts the date parts
     * @return the date format ddmmyyyy
     */
    public static String getDateFormatDDMMYYYY(String[] dateParts){
    	String result = null;
    	try {
    		if( dateParts != null ){
    			int len = dateParts.length;
    			result = "";
    			String dd="";
    			String mm="";
    			String yyyy="";
    			for(int j=0; j<len; j++){
    				if( j==0 ){
    					yyyy = dateParts[j];
    				}else if( j== 1){
    					mm = dateParts[j];
    					int mon = Integer.parseInt(mm);
    					mm = String.format("%02d", mon);
    				} else {
    					dd = dateParts[j];
    					int date = Integer.parseInt(dd);
    					dd = String.format("%02d", date);
    				}
    			}
    			result = mm+""+dd+""+yyyy;
    			if(logger.isDebugEnabled()){
    				logger.debug("DateFormat in DDMMYYYY is"+result);
    			}
    		}
    	}catch(Exception ex){
    		logger.error("Exception while formatting the date", ex);
    	}
    	return result;
    }
    
    /**
     * Gets the property value.
     *
     * @param appProperties the app properties
     * @param propertyName the property name
     * @return the property value
     */
    public static String getPropertyValue(Properties appProperties, String propertyName){
        String hostname = null;
        boolean defaultHost = false;
        String propertyValue = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();        
            hostname = addr.getHostName().toLowerCase();
            if(logger.isDebugEnabled())
                logger.debug("[IVRUtils] Value of host name in "+hostname);
        } catch (UnknownHostException ue) {
            logger.error("[IVRUtils] Error getting hostname",ue);
            hostname="default";
            defaultHost = true;
        }
        
        if( !defaultHost ) {
            propertyValue = appProperties.getProperty(hostname + "_"+ propertyName);

            if( propertyValue == null ) {
                if(logger.isDebugEnabled())
                    logger.debug("[IVRUtils] Value of propertyName in "+propertyName+" appProperties:"+appProperties);
                propertyValue = appProperties.getProperty("default" + propertyName);
                
            }
        } else {
            propertyValue = appProperties.getProperty("default" + "_"+propertyName);
            
        }
        return propertyValue; 
    }
    
    /**
     * Gets the host name.
     *
     * @return the host name
     */
    public static String getHostName(){
		String hostname = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();        
            hostname = addr.getHostName().toLowerCase();
            
        } catch (UnknownHostException ue) {
            logger.error("[OnCallStart] Error getting hostname",ue);
            hostname="default";
        }
        return hostname;
	}
    
    /**
     * Log app log and log4j.
     *
     * @param elementName the element name
     * @param logNamePrefix the log name prefix
     * @param logValue the log value
     * @param data the data
     */
    public static void logAppLogAndLog4j(String elementName, String logNamePrefix, String logValue, APIBase data){
    	data.addToLog("["+elementName+"]"+logNamePrefix, logValue);
		if( logger.isDebugEnabled() )
			logger.debug(logNamePrefix+"->"+logValue);
    }
    
    public static void setSessionDataAndLogAppLogAndLog4j(APIBase data, String key, String value){
    	try {
    		if( isNotNullAndEmpty(value)) {
		    	data.setSessionData(key, value);
		    	logAppLogAndLog4j("", key, value, data);
    		} else {
    			logAppLogAndLog4j("", key, "Null or Empty", data);
    		}
    	} catch(Exception ex){
    		logger.error(ex);
    	}
    }
    
    /**
     * Removes the leading comma.
     *
     * @param str the str
     * @return the string
     */
    public static String removeLeadingComma(String str){
    	if( str != null && !str.isEmpty())
    		return str.substring(1, str.length()).trim();
    	else
    		return str;
    }
    
    /**
     * Insert call record.
     *
     * @param callID the call id
     * @param lang the lang
     * @param sourceIVR the source ivr
     * @return true, if successful
     */
    public static boolean insertCallRecord(String callID, String lang, String sourceIVR){
		boolean rslt = false;
		Connection connection = null;
	    PreparedStatement pStatement = null;
	    try
	    {
	      // the sql server driver string
	      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	    
	      // the sql server url
	      String url = "jdbc:sqlserver://VSTUCCEHDSA:1433;DatabaseName=IVRSurvey";
	      
	      // get the sql server database connection
	      connection = DriverManager.getConnection(url,"ivrsurvey", "F!agSt@rSv^");
	      String sql = "INSERT INTO IVRSurvey.dbo.IVRCallRecord(CallID, LangSelected, SourceIVR) VALUES (?,?,?)";
	      pStatement = connection.prepareStatement(sql);
	      pStatement.setString(1, callID);
	      pStatement.setString(2, lang);
	      pStatement.setString(3, sourceIVR);
	      int result = pStatement.executeUpdate();
	      logger.debug("Insert IVRCallRecord result is:"+result);
	      if( result > 0 ){
	    	  rslt = true;
	      }
	    }
	    catch (ClassNotFoundException e)
	    {
	    	logger.error("ClassNotFound Exception", e);
	      
	    }
	    catch (SQLException e)
	    {
	    	logger.error("ExceptionSQL Exception while inserting call record: "+callID, e);
	      
	    } finally {
	    	if( pStatement!= null ){
	    		try {
					pStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("Exception while closing prepared statement", e);
				}
	    	}
	    	if( connection != null )
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("Exception while closing connection", e);
				}
	    }
	    logger.debug("Result of IVR Call Record insert is:"+rslt);
	    return rslt;
	}
    
    /**
     * Gets the data source.
     *
     * @param s the s
     * @return the data source
     * @throws Exception the exception
     */
    public static DataSource getDataSource(String s) throws Exception {
		String errorMsg = null;
		DataSource datasource = null;
		InitialContext initialcontext = null;
		logger.debug("Entering getDataSource:"+s);
		try {
			initialcontext = new InitialContext();
		} catch (NamingException namingexception) {
			errorMsg = "NamingException occured: " + namingexception.getMessage();
			logger.error("Exception while getting initialContext", namingexception);
			return null;
		}
		Context context = null;
		try {
			context = (Context) initialcontext.lookup("java:comp/env");
		} catch (NamingException nex) {
			errorMsg = "NamingException occured: " + nex.getMessage();
			throw new Exception(errorMsg, nex);
		}
		try {
			datasource = (DataSource) context.lookup("jdbc/" + s);
		} catch (NamingException namingexception3) {
			try {
				datasource = (DataSource) initialcontext.lookup("java:/"
						+ s);
			} catch (NamingException nex) {
				errorMsg = "Problem looking up JNDI datasource: " + nex;
				throw new Exception(errorMsg, nex);
			}
		} catch(Exception ex){
			errorMsg = "Exception occured: " + ex.getMessage();
			throw new Exception(errorMsg, ex);
		}
		if( datasource == null ){
			errorMsg = "Problem lookingup JNDI datasource";
			throw new Exception(errorMsg);
		} else {
			return datasource;
		}
	}
    
    /**
     * Insert call record.
     *
     * @param dataSourceName the data source name
     * @param callID the call id
     * @param lang the lang
     * @param sourceIVR the source ivr
     * @return true, if successful
     */
    public static boolean insertCallRecord(String dataSourceName, String callID, String lang, String sourceIVR){
		Connection connection = null;
		boolean rslt = false;
		PreparedStatement pStatement = null;
		logger.debug("Entering insertCallRecord using DataSource:"+dataSourceName);
		try {
			connection = getJNDIConnection(dataSourceName);
		} catch (Exception e) {
			logger.error("Exception while inserting call record using direction database connection", e);
			return insertCallRecord(callID, lang, sourceIVR);
		}
		logger.debug("Got connection from datasource:"+dataSourceName);
		try {
			String sql = "INSERT INTO IVRSurvey.dbo.IVRCallRecord(CallID, LangSelected, SourceIVR) VALUES (?,?,?)";
		      pStatement = connection.prepareStatement(sql);
		      pStatement.setString(1, callID);
		      pStatement.setString(2, lang);
		      pStatement.setString(3, sourceIVR);
		      int result = pStatement.executeUpdate();
		      logger.debug("Insert IVRCallRecord result is:"+result);
		      if( result > 0 ){
		    	  rslt = true;
		    	  logger.debug("Inserted call record successfully- callId:"+callID);
		      } else {
		    	  logger.debug("Insert call record failed- callId:"+callID);
		      }
		      
		} catch (Exception e) {
			logger.error("Exception while inserting call details", e);
		} finally {
	    	if( pStatement!= null ){
	    		try {
					pStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("Exception while closing prepared statement", e);
				}
	    	}
	    	if( connection != null )
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("Exception while closing connection", e);
				}
	    }
		return rslt;
	}
	
	/**
	 * Gets the JNDI connection.
	 *
	 * @param strJNDIName the str jndi name
	 * @return the JNDI connection
	 * @throws Exception the exception
	 */
	public static Connection getJNDIConnection(String strJNDIName) throws Exception {
		DataSource datasource = null;
		Connection connection = null;
		logger.debug("Entering getJNDIConnection:"+strJNDIName);
		try {
			datasource = getDataSource(strJNDIName);
		} catch (NamingException nex) {
			throw new Exception("There was a problem looking up the JNDI datasource " + strJNDIName);
		} catch(Exception ex){
			throw new Exception(ex);
		}
		try {
			connection = datasource.getConnection();
		} catch (SQLException ex) {
			throw new Exception("SQLException occured: " + ex.getMessage());
		} catch (Exception ex) {
			throw new Exception("Exception occured: " + ex.getMessage());
		}
		return connection;		
	}
    
	public static String convertToSayItSmart(String value){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<value.length();i++){
			sb.append(value.charAt(i)).append(":::").append(value.charAt(i));
			if(i<(value.length()-1))
			sb.append("|||");
		}
		
		return sb.toString();
	}
	
	public static boolean isNotNullAndEmpty(String value){
		boolean rslt = false;
		if( value != null && !(value.isEmpty()) )
			rslt = true;
		return rslt;
	}
	
	public static String getSessionDataString(DecisionElementData data, String property){
		return (String) data.getSessionData(property);
	}
	
	public static String getCALLKEY(ElementAPI data){
		return (String) data.getSessionId();
	}
	
	public static String getTTSAddress(String strAddress){
		StringBuffer address = new StringBuffer();
		String streetNumber = "";
		String[] strSplit = strAddress.split(" ");
		if( strSplit != null ){
			if( strSplit.length > 0 ){
				streetNumber = strSplit[0];
			}
		}
		if( IVRUtils.isNotNullAndEmpty(streetNumber) ){
			strAddress = strAddress.replaceAll(streetNumber, "");
			streetNumber = streetNumber.replaceAll(".", "$0 ");
		}
		address.append(streetNumber).append(" ");
		address.append(strAddress);
		/*address.append("<![CDATA[<prosody rate=\"slow\">")
		.append("<say-as interpret-as=\"address\" format=\"us-state\">")
        .append(strAddress)
        .append("</say-as>")
        .append("</prosody>]]>");*/
		
		return address.toString();
	}
	
	public static Document getDocument(String fileName){
		Document doc = null;
		try {
			   // Setup the parser
			   DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			   //DocumentBuilder builder = builderFactory.newDocumentBuilder();
			   DocumentBuilder builder = builderFactory.newDocumentBuilder();

		        builder.setEntityResolver(new EntityResolver() {

		            @Override
		            public InputSource resolveEntity(String publicId, String systemId)
		                    throws SAXException, IOException {
		                logger.debug("Ignoring " + publicId + ", " + systemId);
		                return new InputSource(new StringReader(""));
		            }
		        });
			   // Read the XML file
			   File inputFile = new File(fileName);
			   InputStream inputStream = new FileInputStream(inputFile);
			   
			   // Parse the XML file   
			   doc = builder.parse(inputStream);
		}catch(Exception ex){
			logger.error("Exception in getting document", ex);
		}
		return doc;
	}
	
	public static Map<String, String> getMenuMap(String fileName){
		Map<String, String> resultMap = new HashMap<String, String>();
		Document doc = IVRUtils.getDocument(fileName);
		// Create an XPath instance
		XPath xPath = XPathFactory.newInstance().newXPath();
		String xPathExpression = "/call_flow/element_def/voice[contains(@class, 'com.audium.server.voiceElement.menu.MFoundation')]/static";
		NodeList titleList = null;
		try {
			titleList = (NodeList) xPath.compile(xPathExpression).evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			logger.error("exception in isMenu determination", e);
		}
		int size = titleList==null?0:titleList.getLength();
		String resultName = "";
		for ( int i = 0; i < size; i++ ) {
				resultName = titleList.item(i).getTextContent();
				resultName = resultName.replaceAll(".xml", "");
			   resultMap.put(resultName, resultName);
		}
		return resultMap;
	}
	
    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) throws Exception{
    	/*String b = ", asd,"
    		;
    	System.out.println(removeLeadingComma(b));
    	String[] a = {"2013", "3", "29"};
    	System.out.println(getDateFormatDDMMYYYY(a));*/
    	System.out.println(getTTSAddress("9746 RUSSIAN DRIVES"));
    	
    	String date = "January, 2034";
    	SimpleDateFormat sdf1 = new SimpleDateFormat("MMMM,yyyy");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("MMyyyy");
    	System.out.println(sdf2.format(sdf1.parse(date)));
    	
    	
    	String pattern1 = "MM/dd/yyyy";
		String pattern2 = "MMddyyyy";
	    SimpleDateFormat format1 = new SimpleDateFormat(pattern1);
	    SimpleDateFormat format2 = new SimpleDateFormat(pattern2);
	    String op = format2.format(format1.parse("11/01/2015"));
	    System.out.println(op);
	    
	    String pattern3 = "MMMM yyyy";
		String pattern4 = "MMyyyy";
	    SimpleDateFormat format3 = new SimpleDateFormat(pattern3);
	    SimpleDateFormat format4 = new SimpleDateFormat(pattern4);
	    System.out.println(format4.format(format3.parse("December 2032")));
    	//System.out.println();
    }
}