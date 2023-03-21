/*******************************************************************************
 * Copyright (c) 2023 seanmuir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     seanmuir - initial API and implementation
 *
 *******************************************************************************/
package org.mdmi.rt.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mdmi.rt.service.web.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author seanmuir
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class XXXXyyy {

	@Test
	void test() {
		fail("Not yet implemented");
	}

	@BeforeClass
	public static void setEnvironment() {
		System.setProperty("mdmi.maps", "src/test/resources/docker/apex");
	}

	@BeforeAll
	public static void beforeClass() {
		System.setProperty("mdmi.maps", "src/test/resources/docker/apex");
	}

	@Before
	public void before() {
		System.setProperty("mdmi.maps", "src/test/resources/docker/apex");
	}

	@Test
	public void test2() {
		System.setProperty("mdmi.maps", "src/test/resources/docker/apex");
	}

	@After
	public void after() {
		System.setProperty("mdmi.maps", "src/test/resources/docker/apex");
	}

	@AfterClass
	public static void afterClass() {

		System.out.println("@AfterClass");
	}

	@Autowired
	private TestRestTemplate template;

	@Test
	public void testGetTransformations() {
		ResponseEntity<String> response = template.getForEntity("/mdmi/transformation", String.class);
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
		System.out.println(response.getBody());
	}

	private String runTransformation(String source, String target, String message) throws Exception {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("source", source);
		map.add("target", target);
		map.add("message", new FileSystemResource(Paths.get(message)));
		ResponseEntity<String> response = template.postForEntity("/mdmi/transformation", map, String.class);
		System.out.println(response.getStatusCode());
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
		Path sourcePath = Paths.get(message);
		String testName = FilenameUtils.removeExtension(sourcePath.getFileName().toString());

		Path testPath = Paths.get("target/test-output/" + testName);
		if (!Files.exists(testPath)) {
			Files.createDirectories(testPath);
		}

		Path path = Paths.get("target/test-output/" + testName + "/" + testName + "." + "json");
		byte[] strToBytes = response.getBody().getBytes();

		Files.write(path, strToBytes);

		// System.out.println(response.getBody());
		return response.getBody();
	}

	@Test
	public void testAPEX2FHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/apex").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				runTransformation("APEX.Massachusetts", "FHIRR4JSON.MasterBundle", document.get());
			}
		}
	}

	@Test
	public void testCDA2FHIR3() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/fhir").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				runTransformation("FHIRR4JSON.MasterBundle", "APEX.Massachusetts", document.get());
			}
		}
	}

	public static <E> Optional<E> getRandom(Collection<E> e) {
		return e.stream().skip((int) (e.size() * Math.random())).findFirst();
	}

}
