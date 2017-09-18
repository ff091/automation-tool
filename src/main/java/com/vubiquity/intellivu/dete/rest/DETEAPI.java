/*******************************************************************************
 * VUBIQUITY PROPRIETARY, Copyright© 2016 VUBIQUITY.
 * UNPUBLISHED WORK, ALL RIGHTS RESERVED
 *
 * This software is the confidential and proprietary information of VUBIQUITY
 * ("Proprietary Information").  Any use, reproduction,
 * distribution or disclosure of the software or Proprietary Information,
 * in whole or in part, must comply with the terms of the license
 * agreement, nondisclosure agreement or contract entered into with
 * VUBIQUITY providing access to this software.
 *******************************************************************************/
package com.vubiquity.intellivu.dete.rest;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.DocumentType;

import com.accenture.dt.dete.ws.vump.schemas.AssetFeedRequest;

@RestController
@RequestMapping("/api/dete")
public class DETEAPI {
	private final Logger LOGGER = LoggerFactory.getLogger(DETEAPI.class);
	
//    @Autowired
//    private Jaxb2Marshaller deteMarshaller;
//    
//    @Bean(name = "deteMarshaller")
//    public Jaxb2Marshaller deteMarshaller() {
//        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//        marshaller.setPackagesToScan("com.accenture.dt.dete");
//        return marshaller;
//    }

	@RequestMapping(method = RequestMethod.POST, value = "/asset-feed", consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public String assetFeed(@RequestBody(required = true) String assetFeedRequestXml) throws Exception {
		InputStream in = IOUtils.toInputStream(assetFeedRequestXml, StandardCharsets.UTF_8);
        JAXBContext jaxbContext = JAXBContext.newInstance(com.accenture.dt.dete.ws.vump.schemas.ObjectFactory.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AssetFeedRequest assetFeedRequest = ((JAXBElement<AssetFeedRequest>) jaxbContext.createUnmarshaller().unmarshal(in)).getValue();
		
		if (assetFeedRequest != null) {
			LOGGER.info("Received asset feed {}", assetFeedRequest.getAsset().getId());

		} else {
			LOGGER.error("Parse error");
		}

		return createResponse("assetFeedResponse", true);
	}

	private String createResponse(String responseObject, boolean success) {
		StringBuilder builder = new StringBuilder();
		builder.append("<");
		builder.append(responseObject);
		builder.append(" xmlns=\"http://dete.dt.accenture.com/ws/vump/schemas\"><success>");
		builder.append(success);
		builder.append("</success></");
		builder.append(responseObject);
		builder.append(">");
		return builder.toString();
	}

}
