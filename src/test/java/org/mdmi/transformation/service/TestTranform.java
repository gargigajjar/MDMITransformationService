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
 * @author seanmuir
 *
 */
class TestTransform {

	@BeforeAll
	public static void setEnvironment() {
		System.setProperty("mdmi.maps", "src/main/resources/maps");
		System.setProperty("LOGGING_LEVEL_MDMI", "TRACE");
	}

	private static byte[] readInputMessage() {
		try {
			return StreamUtils.readStream(new FileInputStream("LoopF.edi"));
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

	/*
	 * @Test
	 * void testFHIR2EDI() throws Exception {
	 * MdmiEngine mdmiEngine = new MdmiEngine();
	 * // mdmiEngine.mdmiSettings = new MDMISettings();
	 * String result = mdmiEngine.transformation(
	 * "FHIRR4JSON.MasterBundleReference", "X12.278", new String(readFHIRMessage()));
	 * System.err.println(result);
	 *
	 * }
	 */
}