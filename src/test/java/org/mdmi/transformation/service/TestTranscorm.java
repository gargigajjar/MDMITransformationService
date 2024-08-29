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

import org.junit.jupiter.api.Test;
import org.mdmi.rt.service.web.MdmiEngine;
import org.smooks.support.StreamUtils;

/**
 * @author seanmuir
 *
 */
class TestTranscorm {

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
	void testEDI2FHIR() throws Exception {
		MdmiEngine mdmiEngine = new MdmiEngine();
		// mdmiEngine.mdmiSettings = new MDMISettings();
		String result = mdmiEngine.transformation(
			"X12.278", "FHIRR4JSON.MasterBundleReference", new String(readInputMessage()));
		System.err.println(result);

	}

	@Test
	void testFHIR2EDI() throws Exception {
		MdmiEngine mdmiEngine = new MdmiEngine();
		// mdmiEngine.mdmiSettings = new MDMISettings();
		String result = mdmiEngine.transformation(
			"FHIRR4JSON.MasterBundleReference", "X12.278", new String(readFHIRMessage()));
		System.err.println(result);

	}

}
