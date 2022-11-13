package com.baeldung.service;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import com.baeldung.wsdl.GetCountryRequest;
import com.baeldung.wsdl.GetCountryResponse;
import com.baeldung.wsdl.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class CountryClient extends WebServiceGatewaySupport {

	private final static QName QName = new QName("http://www.baeldung.com/springsoap/gen", "name");
	private static final Logger log = LoggerFactory.getLogger(CountryClient.class);

	public GetCountryResponse getCountry(String country) {

		ObjectFactory factory = new ObjectFactory();
		GetCountryRequest value = factory.createGetCountryRequest();

		JAXBElement<GetCountryRequest> request = new JAXBElement<>(QName, GetCountryRequest.class, null, value);

		log.info("getName()  - " + request.getName());
		log.info("getValue() - " + request.getValue().getName());
		log.info("Requesting location for " + country);

		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance(GetCountryRequest.class);
			Marshaller m = context.createMarshaller();

			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML

			StringWriter sw = new StringWriter();
			m.marshal(value, sw);
			String xmlString = sw.toString();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}


		return (GetCountryResponse) getWebServiceTemplate()
			.marshalSendAndReceive("http://localhost:8080/ws", request,
				new SoapActionCallback("http://www.baeldung.com/springsoap/gen/getCountryRequest"));

	}

}
