/*******************************************************************************
 * Copyright (c) 2024 Owner.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Owner - initial API and implementation
 *
 *******************************************************************************/
package org.mdmi.transformation.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mdmi.rt.service.web.MdmiEngine;
import org.smooks.support.StreamUtils;

/**
 * @author Owner
 *
 */
public class TestTransformation {
	@BeforeAll
	public static void setEnvironment() {
		System.setProperty("mdmi.maps", "src/main/resources/maps");
		System.setProperty("LOGGING_LEVEL_MDMI", "TRACE");
	}

	private static byte[] readInputMessage() {
		try {
			return StreamUtils.readStream(new FileInputStream("input-message.edi"));
		} catch (IOException e) {
			e.printStackTrace();
			return "<no-message/>".getBytes();
		}
	}

	private static byte[] readInputMessage2() {
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

	private static byte[] readFHIRPAMessage() {
		try {
			return StreamUtils.readStream(new FileInputStream("PA-Approved-request.json"));
		} catch (IOException e) {
			e.printStackTrace();
			return "<no-message/>".getBytes();
		}
	}

	@Test
	void testEDI2FHIR() throws Exception {
		Path testPath = Paths.get("target/test-output/" + "testEDI2FHIR");
		if (!Files.exists(testPath)) {
			Files.createDirectories(testPath);
		}
		MdmiEngine mdmiEngine = new MdmiEngine();
		// mdmiEngine.mdmiSettings = new MDMISettings();
		String result = mdmiEngine.transformation(
			"X12.278", "FHIRR4JSON.MasterBundleReference", new String(readInputMessage()));
		Path path = Paths.get("target/test-output/testEDI2FHIR/testEDI2FHIR.json");
		byte[] strToBytes = result.getBytes();

		Files.write(path, strToBytes);
		System.err.println(result);

	}

	@Test
	void testEDI2FHIR2() throws Exception {
		Path testPath = Paths.get("target/test-output/" + "testEDI2FHIR2");
		if (!Files.exists(testPath)) {
			Files.createDirectories(testPath);
		}
		MdmiEngine mdmiEngine = new MdmiEngine();
		// mdmiEngine.mdmiSettings = new MDMISettings();
		String result = mdmiEngine.transformation(
			"X12.278", "FHIRR4JSON.MasterBundleReference", new String(readInputMessage2()));
		Path path = Paths.get("target/test-output/testEDI2FHIR2/testEDI2FHIR2.json");
		byte[] strToBytes = result.getBytes();

		Files.write(path, strToBytes);
		System.err.println(result);

	}

	@Test
	void testFHIR2EDI() throws Exception {
		Path testPath = Paths.get("target/test-output/" + "testFHIR2EDI");
		if (!Files.exists(testPath)) {
			Files.createDirectories(testPath);
		}
		MdmiEngine mdmiEngine = new MdmiEngine();
		// mdmiEngine.mdmiSettings = new MDMISettings();
		String result = mdmiEngine.transformation(
			"FHIRR4JSON.MasterBundleReference", "X12.278", new String(readFHIRMessage()));
		Path path = Paths.get("target/test-output/testFHIR2EDI/testFHIR2EDI.xml");
		byte[] strToBytes = result.getBytes();

		Files.write(path, strToBytes);
		System.err.println(result);

	}

	@Test
	void testLoopFIssue() throws Exception {
		Path testPath = Paths.get("target/test-output/" + "testFHIR2EDI");
		if (!Files.exists(testPath)) {
			Files.createDirectories(testPath);
		}
		MdmiEngine mdmiEngine = new MdmiEngine();
		// mdmiEngine.mdmiSettings = new MDMISettings();
		String result = mdmiEngine.transformation(
			"FHIRR4JSON.MasterBundleReference", "X12.278",
			new String(StreamUtils.readStream(new FileInputStream("input-fhir2.json"))));
		Path path = Paths.get("target/test-output/testFHIR2EDI/testFHIR2EDI.edi");
		byte[] strToBytes = result.getBytes();

		Files.write(path, strToBytes);
		System.err.println(result);

	}

	@Test
	void testFHIRPA2EDI() throws Exception {
		Path testPath = Paths.get("target/test-output/" + "testFHIR2EDI");
		if (!Files.exists(testPath)) {
			Files.createDirectories(testPath);
		}
		MdmiEngine mdmiEngine = new MdmiEngine();
		// mdmiEngine.mdmiSettings = new MDMISettings();
		String result = mdmiEngine.transformation(
			"FHIRR4JSON.MasterBundleReference", "X12.278", new String(readFHIRPAMessage()));
		Path path = Paths.get("target/test-output/testFHIR2EDI/PA-Approved-request.edi");
		byte[] strToBytes = result.getBytes();

		Files.write(path, strToBytes);
		System.err.println(result);

	}
}
