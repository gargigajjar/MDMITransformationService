/*******************************************************************************
 * Copyright (c) 2024 seanmuir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     seanmuir - initial API and implementation
 *
 *******************************************************************************/
package org.mdmi.transformation.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.Test;
import org.mdmi.rt.service.web.EDIProcessor;
import org.smooks.Smooks;
import org.smooks.api.ExecutionContext;
import org.smooks.engine.DefaultApplicationContextBuilder;
import org.smooks.io.payload.StringResult;
import org.smooks.support.StreamUtils;
import org.xml.sax.SAXException;

/**
 * @author seanmuir
 *
 */
class TestConversion {

	private static byte[] readInputMessage() {
		try {
			return StreamUtils.readStream(new FileInputStream("input-message.edi"));
		} catch (IOException e) {
			e.printStackTrace();
			return "<no-message/>".getBytes();
		}
	}

	private static byte[] readFHIRMessage() {
		try {
			return StreamUtils.readStream(new FileInputStream("input-fhir.json"));
		} catch (IOException e) {
			e.printStackTrace();
			return "<no-message/>".getBytes();
		}
	}

	@Test
	void testEDI2XML() throws IOException, SAXException {
		Smooks smooks = new Smooks(
			new DefaultApplicationContextBuilder().withClassLoader(EDIProcessor.class.getClassLoader()).build());
		smooks.addResourceConfigs("smile-smooks-parser-config.xml");

		try {

			Path testPath = Paths.get("target/test-output/" + "EDI2XML");
			if (!Files.exists(testPath)) {
				Files.createDirectories(testPath);
			}
			// Create an exec context - no profiles....
			ExecutionContext executionContext = smooks.createExecutionContext();

			StringResult result = new StringResult();

			// Configure the execution context to generate a report...
			// executionContext.getContentDeliveryRuntime().addExecutionEventListener(new HtmlReportGenerator("target/report/report.html",
			// executionContext.getApplicationContext()));

			// Filter the input message to the outputWriter, using the execution context...
			smooks.filterSource(
				executionContext, new StreamSource(new ByteArrayInputStream(readInputMessage())), result);

			Path path = Paths.get("target/test-output/EDI2XML/x12.xml");
			byte[] strToBytes = result.getResult().getBytes();

			Files.write(path, strToBytes);

			System.out.println(result.getResult());

		} finally {
			smooks.close();
		}
	}

	/*
	 * @Test
	 * void testJavaEDI2XML() throws IOException, ParserConfigurationException, TransformerException {
	 *
	 * String inputData = "ST*278*0001*005010X217~" + "BHT*0007*11*200300114000001*20050501*1400*18~" + "";
	 * Path testPath = Paths.get("target/test-output/java/javaConversion");
	 * if (!Files.exists(testPath)) {
	 * Files.createDirectories(testPath);
	 * }
	 * inputData = sanitizeInputData(inputData);
	 *
	 * // Create an empty XML document
	 * DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	 * DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 * Document doc = docBuilder.newDocument();
	 *
	 * // Root element
	 * Element rootElement = doc.createElement("Segments");
	 * doc.appendChild(rootElement);
	 *
	 * // Split the input data by tilde (~) to get each segment
	 * String[] segments = inputData.split("~");
	 *
	 * // Process each segment
	 * for (String segment : segments) {
	 * if (segment.trim().isEmpty())
	 * continue; // Skip empty lines
	 *
	 * // Split the segment by asterisk (*) to get each part
	 * String[] elements = segment.split("\\*");
	 *
	 * // Use the first element as the tag name (e.g., HL, NM1)
	 * if (elements.length > 0) {
	 * Element segmentElement = doc.createElement(elements[0]);
	 * rootElement.appendChild(segmentElement);
	 *
	 * // Add remaining elements as sub-elements
	 * for (int i = 1; i < elements.length; i++) {
	 * Element element = doc.createElement("Element" + i);
	 * element.appendChild(doc.createTextNode(elements[i]));
	 * segmentElement.appendChild(element);
	 * }
	 * }
	 * }
	 *
	 * try (OutputStream outputStream = Files.newOutputStream(testPath)) {
	 * StreamResult result = new StreamResult(outputStream); // StreamResult with OutputStream
	 *
	 * // Write the content into an XML file
	 * TransformerFactory transformerFactory = TransformerFactory.newInstance();
	 * Transformer transformer = transformerFactory.newTransformer();
	 * transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	 * DOMSource source = new DOMSource(doc);
	 *
	 * transformer.transform(source, result);
	 *
	 * System.out.println("File converted to XML successfully!");
	 * }
	 *
	 * }
	 *
	 * private static String sanitizeInputData(String input) {
	 * // Regular expression to match any illegal XML characters
	 * return input.replaceAll("[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD]", "");
	 * }
	 */
}
