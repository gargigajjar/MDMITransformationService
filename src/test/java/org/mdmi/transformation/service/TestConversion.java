/*******************************************************************************
 * Copyright (c) 2024 MDIX, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompany this distribution and is available at
 * https\://www.apache.org/licenses/LICENSE-2.0.
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
			return StreamUtils.readStream(new FileInputStream("PA-Approved-request man edit 1112.edi"));
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

	private static byte[] readXMLMessage() {
		try {
			return StreamUtils.readStream(new FileInputStream("input-x12.xml"));
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

	@Test
	void testXML2EDI() throws IOException, SAXException {
		Smooks smooks = new Smooks(
			new DefaultApplicationContextBuilder().withClassLoader(EDIProcessor.class.getClassLoader()).build());
		smooks.addResourceConfigs("smile-smooks-unparser-config.xml");

		try {

			Path testPath = Paths.get("target/test-output/" + "XML2EDI");
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
			smooks.filterSource(executionContext, new StreamSource(new ByteArrayInputStream(readXMLMessage())), result);

			Path path = Paths.get("target/test-output/XML2EDI/x12.edi");
			byte[] strToBytes = result.getResult().getBytes();

			Files.write(path, strToBytes);

			System.out.println(result.getResult());

		} finally {
			smooks.close();
		}
	}

}
