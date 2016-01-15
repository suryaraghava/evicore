package com.evi.main.java;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadLoadCaseDocument {
	
	public LoadCaseBean getLookupCaseBean(Document doc){
		LoadCaseBean lcb = null;
		
		XPathFactory xFactory = XPathFactory.newInstance();
		XPath xpath = xFactory.newXPath();
		
		try {
			String StatusCode=xpath.evaluate("GetValidatedCaseForIVRResponse/GetValidatedCaseForIVRResult/MedicalReviewStatus",doc).trim();
			String AuthNum=xpath.evaluate("GetValidatedCaseForIVRResponse/GetValidatedCaseForIVRResult/AuthorizationId",doc).trim();
			String PatientName = xpath.evaluate("GetValidatedCaseForIVRResponse/GetValidatedCaseForIVRResult/Member/LastName",doc).trim();
			String PhysName = xpath.evaluate("GetValidatedCaseForIVRResponse/GetValidatedCaseForIVRResult/Physician/LastName",doc).trim();
			String FacilityName =xpath.evaluate("GetValidatedCaseForIVRResponse/GetValidatedCaseForIVRResult/Facility/FacilityName",doc).trim();
			String EffDate = xpath.evaluate("GetValidatedCaseForIVRResponse/GetValidatedCaseForIVRResult/EffectiveDate",doc).trim();
			String ExpDate = xpath.evaluate("GetValidatedCaseForIVRResponse/GetValidatedCaseForIVRResult/ExpirationDate",doc).trim();
			String FaxNum = xpath.evaluate("GetValidatedCaseForIVRResponse/GetValidatedCaseForIVRResult/Facility/Fax",doc).trim();
			XPathExpression expr = xpath.compile("GetValidatedCaseForIVRResponse/GetValidatedCaseForIVRResult/CptCodes");
			NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			 
			String NumOptCodes =""+nodes.getLength();
			
			List<CptCodesBean> listCptCodes = new ArrayList<CptCodesBean>();
			String desc = null;
			
			for(int i=0; i<=nodes.getLength();i++){
				CptCodesBean cptCodesBean = new CptCodesBean();
				Element el = (Element) nodes.item(i);
				NodeList children = el.getChildNodes();
				for (int k = 0; k < children.getLength(); k++) {
		            Node child = children.item(k);
		            if (child.getNodeType() != Node.TEXT_NODE) {
		                System.out.println("child tag: " + child.getNodeName());
		                if (child.getNodeName().equalsIgnoreCase("Description"))
		                     desc = child.getNodeValue();
		                cptCodesBean.setDescription(desc);
		                listCptCodes.add(cptCodesBean);
		            }
		        }
			}
			
			lcb = new LoadCaseBean();
			 
			lcb.setAuthNum(AuthNum);
			lcb.setCptCodes(listCptCodes);
			lcb.setEffDate(EffDate);
			lcb.setExpDate(ExpDate);
			lcb.setFacilityName(FacilityName);
			lcb.setFaxNum(FaxNum);
			lcb.setNumOptCodes(NumOptCodes);
			lcb.setPatientName(PatientName);
			lcb.setPhysName(PhysName);
			lcb.setStatusCode(StatusCode);
			

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			//logger.error("Exception occured :"+e);
		}	
		
		return lcb;
	}
	
}
