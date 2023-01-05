/*******************************************************************************
 * Copyright (c) 2018 seanmuir.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mdmi.rt.service.web.Application;
import org.mdmi.rt.service.web.SBHA2XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@RunWith(SpringRunner.class)

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class MdmiEngineTest {

	SBHA2XML sb;

	@BeforeClass
	public static void setEnvironment() {
		System.setProperty("mdmi.maps", "src/test/resources/testmaps");
	}

	@Autowired
	private TestRestTemplate template;

	@Test
	public void testGetTransformations() {
		ResponseEntity<String> response = template.getForEntity("/mdmi/transformation", String.class);
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
		System.out.println(response.getBody());
	}

	public void hapiValidation(String result) throws Exception {

		// FhirContext ctx = FhirContext.forR4();
		// FhirValidator module = new FhirValidator(ctx);
		//
		// IValidatorModule module1 = new SchemaBaseValidator(ctx);
		// IValidatorModule module2 = new SchematronBaseValidator(ctx);
		// module.registerValidatorModule(module1);
		// module.registerValidatorModule(module2);
		//
		// ValidationResult valresult = module.validateWithResult(result);
		// if (valresult.isSuccessful() == false) {
		// for (SingleValidationMessage next : valresult.getMessages()) {
		// System.out.println(next.getLocationString() + " " + next.getMessage());
		// }
		// }

	}

	@Test
	public void testReset() throws Exception {

		Set<String> files = Stream.of(new File("src/test/resources/testreset").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		try {

			ResponseEntity<String> response = template.getForEntity("/mdmi/transformation", String.class);
			assertTrue(response.getStatusCode().equals(HttpStatus.OK));
			System.out.println(response.getBody());

			for (String fileLocation : files) {
				Path source = Paths.get(fileLocation);
				Path newdir = Paths.get("src/test/resources/testmaps");
				Files.copy(Paths.get(fileLocation), newdir.resolve(source.getFileName()));
			}

			ResponseEntity<String> resetResponse = template.getForEntity("/mdmi/transformation/reset", String.class);
			assertTrue(resetResponse.getStatusCode().equals(HttpStatus.OK));
			System.out.println(resetResponse.getBody());

			assertTrue((response.getBody().length() != resetResponse.getBody().length()));

		} finally {
			for (String fileLocation : files) {
				Path source = Paths.get(fileLocation);
				Path newdir = Paths.get("src/test/resources/testmaps");
				Files.deleteIfExists(newdir.resolve(source.getFileName()));
			}
		}
	}

	@Test
	public void testFHIR2CDA() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/PS/FHIR").listFiles()).filter(
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
				runTransformation("FHIRR4JSON.MasterBundle", "CDAR2.ContinuityOfCareDocument", document.get());
			}
		}
	}

	@Test
	public void testFHIR2CDA2() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/fhir").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation(
					"FHIRR4JSON.MasterBundle", "CDAR2.ContinuityOfCareDocument", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testCDA2FHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/test").listFiles()).filter(
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
				runTransformation("CCDAonFHIRJSON.CompositionBundle", "CDAR2.ContinuityOfCareDocument", document.get());
			}
		}
	}

	@Test
	public void testCDA2FHIR2() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/cda").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation(
					"CDAR2.ContinuityOfCareDocument", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testApex2FHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/apex").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation("APEX.Demo", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testPeraton() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/peraton/t6").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation("Legacy.Patient", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testMMIStoFHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/NJ/claim").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation("NJ.PROVIDER", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testMMIS_CLAIM_toFHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/NJ/claim").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation("NJ.Claim", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testSampletoFHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/Students").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation("CSV2XML.STUDENTS", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testCQL() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/cql").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		for (int count = 0; count < 10; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				runTransformation("JSON.CQLCovidSeverity", "JSON.RiskScore", document.get());
			}
		}
	}

	@Test
	public void testV2toFHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/v2").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation("HL7V2.ADTA01CONTENT", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testSBHA_AdministrativetoFHIR() throws Exception {
		Set<String> documents = Stream.of(
			new File("src/test/resources/samples/SBHA/Administrative").listFiles()).filter(
				file -> !file.isDirectory()).map(t -> {
					try {
						return t.getCanonicalPath();
					} catch (IOException e) {
						return "";
					}
				}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation("SBHA.Administrative", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testSBHA_DemographicstoFHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/SBHA/Demographics").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation("SBHA.Demographics", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testSBHA_DisciplinetoFHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/SBHA/Discipline").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation("SBHA.Discipline", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testSBHA_AttendancetoFHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/SBHA/Attendance").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation("SBHA.Attendance", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	private String runTransformation(String source, String target, String message) throws Exception {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("source", source);
		map.add("target", target);
		map.add("message", new FileSystemResource(Paths.get(message)));
		ResponseEntity<String> response = template.postForEntity("/mdmi/transformation", map, String.class);
		System.out.println(response.getStatusCode());
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
		// System.out.println(response.getBody());
		return response.getBody();
	}

	private void runTransformationWithValidation(String source, String target, String message) throws Exception {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
		map.add("source", source);
		map.add("target", target);
		map.add("message", new FileSystemResource(Paths.get(message)));
		ResponseEntity<String> response = template.postForEntity("/mdmi/transformation", map, String.class);
		System.out.println(response.getStatusCode());
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
		System.out.println(response.getBody());
		hapiValidation(response.getBody());
	}

	private String runTransformation2(String source, String target, String message) throws Exception {

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(message, headers);

		ResponseEntity<String> response = template.postForEntity(
			"/mdmi/transformation/byvalue?source=" + source + "&target=" + target, request, String.class);
		assertTrue(response.getStatusCode().equals(HttpStatus.OK));
		String value = response.getBody();
		assertFalse(StringUtils.isEmpty(value));
		System.out.println(value);
		return value;
	}

	@Test
	public void testCDA2FHIRByValue() throws Exception {
		runTransformation2("CDAR2.ContinuityOfCareDocument", "FHIRR4JSON.PortalBundle", "messageexample");
	}

	@Test
	public void testHSDS_LocationstoFHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/HSDS/Locations").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation(
					"HSDSJSON.LocationComplete", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testHSDS_OrganizationstoFHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/HSDS/Organizations").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation(
					"HSDSJSON.OrganizationComplete", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testHSDS_ServicestoFHIR() throws Exception {
		Set<String> documents = Stream.of(new File("src/test/resources/samples/HSDS/Services").listFiles()).filter(
			file -> !file.isDirectory()).map(t -> {
				try {
					return t.getCanonicalPath();
				} catch (IOException e) {
					return "";
				}
			}).collect(Collectors.toSet());

		File file = new File("src/test/resources/results/results.json");
		FileWriter fw = new FileWriter(file, false);

		for (int count = 0; count < 1; count++) {
			Optional<String> document = getRandom(documents);
			if (document.isPresent()) {
				String result = runTransformation(
					"HSDSJSON.ServicesComplete", "FHIRR4JSON.MasterBundle", document.get());
				fw.write(result);
			}
		}
		fw.close();
	}

	@Test
	public void testUniqueSENames() throws Exception {
		File file = new File("src/test/resources/testmaps/fhirR4MasterV2Bundle.mdmi");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// an instance of builder to parse the specified xml file
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getElementsByTagName("semanticElements");
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();

		for (int itr = 0; itr < nodeList.getLength(); itr++) {
			Node node = nodeList.item(itr);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				String seName = eElement.getAttribute("name").replaceAll("\\d", "");

				int temp;
				if (hmap.containsKey(seName)) {
					temp = hmap.get(seName) + 1;
					hmap.replace(seName, temp);
				} else {
					temp = 1;
					hmap.put(seName, temp);
				}
				System.out.println(seName + " :: " + (seName + temp));

				seName += temp;
				eElement.setAttribute("name", seName);

				Transformer tf = TransformerFactory.newInstance().newTransformer();
				tf.setOutputProperty(OutputKeys.INDENT, "yes");
				tf.setOutputProperty(OutputKeys.METHOD, "xml");
				tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

				DOMSource domSource = new DOMSource(doc);
				StreamResult sr = new StreamResult(new File("src/test/resources/testmaps/newMap.mdmi"));
				tf.transform(domSource, sr);

			}
		}
	}

	public static <E> Optional<E> getRandom(Collection<E> e) {
		return e.stream().skip((int) (e.size() * Math.random())).findFirst();
	}

}
