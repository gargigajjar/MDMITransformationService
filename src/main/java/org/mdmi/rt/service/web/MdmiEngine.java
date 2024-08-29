package org.mdmi.rt.service.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mdmi.core.Mdmi;
import org.mdmi.core.engine.MdmiUow;
import org.mdmi.core.engine.javascript.Utils;
import org.mdmi.core.engine.postprocessors.ConfigurablePostProcessor;
import org.mdmi.core.engine.preprocessors.ConfigurablePreProcessor;
import org.mdmi.core.engine.semanticprocessors.ConfigurableSemanticProcessor;
import org.mdmi.core.engine.terminology.FHIRTerminologyTransform;
import org.mdmi.core.runtime.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

public class MdmiEngine {

	// jakarta.annotation.PostConstruct thepost;

	// javax.annotation.PostConstruct the otherone;

	FHIRTerminologySettings terminologySettings = new FHIRTerminologySettings();

	static Boolean loaded = Boolean.FALSE;

	private String mapsFolders = "src/main/resources/maps";

	private HashMap<String, Properties> mapProperties = new HashMap<>();

	private HashMap<String, JSONObject> mapValues = new HashMap<>();

	private static Logger logger = LoggerFactory.getLogger(MdmiEngine.class);

	static List<Map<String, Object>> preprocessors = new ArrayList<Map<String, Object>>();

	static List<Map<String, Object>> postprocessors = new ArrayList<Map<String, Object>>();

	static List<Map<String, Object>> sourcesemanticprocessors = new ArrayList<Map<String, Object>>();

	static List<Map<String, Object>> targetsemanticprocessors = new ArrayList<Map<String, Object>>();

	static long lastModified;

	public static List<String> getFilesFromClasspathFolder() throws Exception {
		// Get the folder URL from the classpath
		// URL resource = ListFilesInClasspathFolder.class.getClassLoader().getResource(folderName);

		List<String> files = IOUtils.readLines(
			MdmiEngine.class.getClassLoader().getResourceAsStream("maps"), Charsets.UTF_8);

		// if (resource == null) {
		// throw new IllegalArgumentException("Folder not found on the classpath: " + folderName);
		// }
		//
		// // Convert the URL to a Path
		// Path path = Paths.get(resource.toURI());
		//
		// // List all files in the folder and collect their names as a List
		// try (Stream<Path> walk = Files.walk(path, 1)) { // Depth 1 to avoid subdirectories
		// return walk.filter(Files::isRegularFile).map(Path::getFileName).map(Path::toString).collect(
		// Collectors.toList());
		// }
		return files;
	}

	@SuppressWarnings("unchecked")
	private void loadMaps() throws Exception {

		// List<String> foo = getFilesFromClasspathFolder();

		// for (String s : foo) {
		// System.err.println(s);
		// }
		synchronized (this) {

			// MdmiUow.sourceFilter = mdmiSettings.getSourceFilterFlag();

			System.err.println("sourceFilter status" + MdmiUow.sourceFilter);

			if (loaded || lastModified == 0) {
				long currentModified = 0;
				for (String mapsFolder : Stream.of(mapsFolders.split(",", -1)).collect(Collectors.toList())) {

					Set<Path> folders = Files.find(
						Paths.get(mapsFolder), Integer.MAX_VALUE,
						(filePath, fileAttr) -> fileAttr.isDirectory()).collect(Collectors.toSet());

					for (Path folder : folders) {

						Set<File> maps2 = Stream.of(new File(folder.toString()).listFiles()).filter(
							file -> (!file.isDirectory() && file.toString().endsWith("mdmi"))).collect(
								Collectors.toSet());

						for (File map : maps2) {
							if (map.lastModified() > currentModified) {
								currentModified = map.lastModified();
							}

						}

					}
				}

				if (currentModified > lastModified) {
					loaded = false;
					mapProperties.clear();
					mapValues.clear();
					preprocessors.clear();
					postprocessors.clear();
					targetsemanticprocessors.clear();
					sourcesemanticprocessors.clear();
					lastModified = currentModified;

				}

			}

			if (loaded) {
				return;
			}

			FHIRTerminologyTransform.codeValues.clear();

			FHIRTerminologyTransform.processTerminology = true;

			FHIRTerminologyTransform.setFHIRTerminologyURL(terminologySettings.getUrl());

			FHIRTerminologyTransform.setUserName(terminologySettings.getUserName());

			FHIRTerminologyTransform.setPassword(terminologySettings.getPassword());

			for (String mapsFolder : Stream.of(mapsFolders.split(",", -1)).collect(Collectors.toList())) {

				Set<Path> folders = Files.find(
					Paths.get(mapsFolder), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isDirectory()).collect(
						Collectors.toSet());

				for (Path folder : folders) {

					Set<String> maps = Stream.of(new File(folder.toString()).listFiles()).filter(
						file -> (!file.isDirectory() && file.toString().endsWith("mdmi"))).map(File::getName).collect(
							Collectors.toSet());
					for (String map : maps) {
						System.err.println("Loading map  " + map);
						InputStream targetStream = new FileInputStream(folder.toString() + "/" + map);
						Mdmi.INSTANCE().getResolver().resolve(targetStream);
						System.err.println("Loaded map  " + map);
					}

					Path datatypemapsPath = Paths.get(folder.toString() + "/datatypemaps");
					if (Files.isDirectory(datatypemapsPath)) {
						Set<String> datatypemaps = Stream.of(
							new File(folder.toString() + "/datatypemaps").listFiles()).filter(
								file -> (!file.isDirectory() && file.toString().endsWith("js"))).map(
									File::getName).collect(Collectors.toSet());
						for (String datatypemap : datatypemaps) {
							System.err.println("Loading datatypemap  " + datatypemap);
							InputStream datatypetermStream = new FileInputStream(
								folder.toString() + "/datatypemaps/" + datatypemap);
							String mapsource = IOUtils.toString(datatypetermStream, StandardCharsets.UTF_8.name());

							for (String key : Mdmi.INSTANCE().getResolver().getMaps().keySet()) {
								if (key.startsWith(FilenameUtils.removeExtension(datatypemap))) {
									Mdmi.INSTANCE().getResolver().getMaps().get(key).datatypemappings = mapsource;
								}
							}
							System.err.println("Loaded datatypemap  " + datatypemap);
						}
					}

					Path termsPath = Paths.get(folder.toString() + "/terms");
					if (Files.isDirectory(termsPath)) {

						Set<String> valusets = Stream.of(new File(folder.toString() + "/terms").listFiles()).filter(
							file -> (!file.isDirectory() && file.toString().endsWith("json"))).map(
								File::getName).collect(Collectors.toSet());
						for (String valuset : valusets) {

							try {

								JSONParser parser = new JSONParser();

								Path file = Path.of(folder.toString() + "/terms/" + valuset);

								System.err.println("Loading valuset  " + valuset);

								String json = Files.readString(file, StandardCharsets.UTF_8);
								JSONObject jsonObject;

								jsonObject = (JSONObject) parser.parse(json);

								JSONObject expansion = (JSONObject) jsonObject.get("expansion");

								JSONArray contains = (JSONArray) expansion.get("contains");

								Utils.mapOfTransforms.put(FilenameUtils.removeExtension(valuset), new Properties());

								Consumer<JSONObject> getcode = new Consumer<JSONObject>() {

									@Override
									public void accept(JSONObject element) {
										Utils.mapOfTransforms.get(FilenameUtils.removeExtension(valuset)).put(
											element.get("code"), element.get("code"));

									}

								};
								contains.forEach(getcode);
							} catch (ParseException e) {
								logger.error(e.getMessage());
							}
						}

						Set<String> datatypeterms = Stream.of(
							new File(folder.toString() + "/terms").listFiles()).filter(
								file -> (!file.isDirectory() && file.toString().endsWith("properties"))).map(
									File::getName).collect(Collectors.toSet());
						for (String datatypeterm : datatypeterms) {
							System.err.println("Loading datatypeterm  " + datatypeterm);
							InputStream datatypetermStream = new FileInputStream(
								folder.toString() + "/terms/" + datatypeterm);
							Utils.mapOfTransforms.put(FilenameUtils.removeExtension(datatypeterm), new Properties());
							Utils.mapOfTransforms.get(FilenameUtils.removeExtension(datatypeterm)).load(
								datatypetermStream);

							System.err.println("Loaded map  " + datatypeterm);
						}

					}

					System.err.println("Check for processors.yml ");
					System.err.println("Looking for " + folder.toString() + "/" + "processors.yml");
					System.err.println("EXISTS " + Files.exists(Paths.get(folder.toString() + "/" + "processors.yml")));
					if (Files.exists(Paths.get(folder.toString() + "/" + "processors.yml"))) {
						System.err.println("Found processors.yml ");
						Yaml processorYaml = new Yaml();
						InputStream inputStream = new FileInputStream(folder.toString() + "/" + "processors.yml");
						Map<String, Object> obj = processorYaml.load(inputStream);

						if (obj.containsKey("preprocessors")) {
							preprocessors.add((Map<String, Object>) obj.get("preprocessors"));
						}
						if (obj.containsKey("postprocessors")) {
							postprocessors.add((Map<String, Object>) obj.get("postprocessors"));
						}

						if (obj.containsKey("sourcesemanticprocessors")) {
							sourcesemanticprocessors.add((Map<String, Object>) obj.get("sourcesemanticprocessors"));
						}

						if (obj.containsKey("targetsemanticprocessors")) {
							targetsemanticprocessors.add((Map<String, Object>) obj.get("targetsemanticprocessors"));
						}

					}

					Set<String> propertyFiles = Stream.of(new File(folder.toString()).listFiles()).filter(
						file -> (!file.isDirectory() && file.toString().endsWith("properties"))).map(
							File::getName).collect(Collectors.toSet());

					for (String propertyFile : propertyFiles) {
						System.err.println("Loading property  " + propertyFile);
						InputStream targetStream = new FileInputStream(folder.toString() + "/" + propertyFile);
						Properties properties = new Properties();
						properties.load(targetStream);
						mapProperties.put(FilenameUtils.removeExtension(propertyFile), properties);
						System.err.println("Loaded property  " + propertyFile);
					}

					Set<String> valuesFiles = Stream.of(new File(folder.toString()).listFiles()).filter(
						file -> (!file.isDirectory() && file.toString().endsWith("json"))).map(File::getName).collect(
							Collectors.toSet());

					for (String valuesFile : valuesFiles) {
						System.err.println("Loading property  " + valuesFile);

						// Files.readString()

						InputStream targetStream = new FileInputStream(folder.toString() + "/" + valuesFile);

						String body = IOUtils.toString(targetStream, StandardCharsets.UTF_8.name());

						JSONParser parser = new JSONParser();
						JSONObject jsonObject;
						try {
							jsonObject = (JSONObject) parser.parse(body);
							mapValues.put(FilenameUtils.removeExtension(valuesFile), jsonObject);
							System.err.println("Loaded property  " + valuesFile);
						} catch (ParseException e) {
							logger.error(e.getLocalizedMessage());
						}
					}

				}

			}

			loaded = Boolean.TRUE;

		}

	}

	private void reloadMaps() throws Exception {
		synchronized (this) {
			loaded = false;
			mapProperties.clear();
			preprocessors.clear();
			postprocessors.clear();
			targetsemanticprocessors.clear();
			sourcesemanticprocessors.clear();
			loadMaps();
		}
	}

	private Properties getMapProperties(String target) {
		for (String mapsFolder : Stream.of(mapsFolders.split(",", -1)).collect(Collectors.toList())) {
			if (!mapProperties.containsKey(target)) {
				Properties properties = new Properties();
				Path propertyFile = Paths.get(target + ".properties");

				if (Files.exists(propertyFile)) {
					try {
						properties.load(Files.newInputStream(propertyFile));
					} catch (IOException e) {
					}
				}
				Path valuesFile = Paths.get(target + ".json");
				if (Files.exists(valuesFile)) {
					try {
						properties.put("InitialValues", new String(Files.readAllBytes(valuesFile)));
					} catch (IOException e) {
					}
				}
				mapProperties.put(target, properties);
			}
		}
		return mapProperties.get(target);
	}

	String ediToXML(String message) throws Exception {
		String messageOut = "<DocumentRoot>" + EDIProcessor.transformEDI2XML(message.getBytes()) + "</DocumentRoot>";
		System.err.println(messageOut);
		return messageOut;

	}

	String xmlToEDI(String message) throws Exception {
		System.err.println(message);
		String messageOut = EDIProcessor.transformXML2EDI(
			message.replace("<DocumentRoot>", "").replace("</DocumentRoot>", "").replaceAll("[\\n\\r]", "").replaceAll(
				"999AaA999", " ").getBytes());
		System.err.println(messageOut);
		return messageOut;

	}

	public String transformation(String source, String target, String message) throws Exception {
		System.out.println("DEBUG Start transformation ");
		loadMaps();
		loadPreProcessors(Mdmi.INSTANCE());
		loadPostProcessors(Mdmi.INSTANCE());
		loadsourcesemanticprocessors(Mdmi.INSTANCE());
		loadTargetSemanticProcessors(Mdmi.INSTANCE());

		// MdmiUow.setSerializeSemanticModel(false);
		Mdmi.INSTANCE().getSourceSemanticModelProcessors().addSourceSemanticProcessor(new ProcessRelationships());
		getMapProperties(source);
		getMapProperties(target);

		if (source.startsWith("X12")) {
			message = ediToXML(message);
		}

		String result = RuntimeService.runTransformation(
			source, message.getBytes(), target, null, mapProperties.get(source), mapProperties.get(target),
			mapValues.get(source), mapValues.get(target));

		if (target.startsWith("X12")) {
			result = xmlToEDI(result);
		}

		return result;
	}

	private void loadPostProcessors(Mdmi instance) {

		instance.getPostProcessors().getPostProcessors().clear();
		if (postprocessors != null && (!postprocessors.isEmpty())) {

			for (Map<String, Object> p : postprocessors) {

				for (Object key : p.keySet()) {

					try {
						System.err.println("Adding postprocessors " + key);
						Class<?> clazz;
						clazz = Class.forName((String) ((Map) p.get(key)).get("class"));
						System.err.println("Loaded java postprocessors " + clazz.getCanonicalName());
						Constructor<?> ctors = clazz.getConstructors()[0];
						ConfigurablePostProcessor postProcessor = (ConfigurablePostProcessor) ctors.newInstance();
						postProcessor.setName((String) ((Map) p.get(key)).get("name"));
						postProcessor.setGroups((ArrayList<String>) ((Map) p.get(key)).get("groups"));
						postProcessor.setArguments(((Map) p.get(key)).get("arguments"));
						instance.getPostProcessors().addPostProcessor(postProcessor);
					} catch (Exception e) {
						logger.error("Error loading  PostProcessor " + key, e.getMessage());
						e.printStackTrace();
					}

				}
			}

		} else {
			System.err.println("No postprocessors registered");
		}

	}

	private void loadPreProcessors(Mdmi instance) {

		instance.getPreProcessors().getPreProcessors().clear();
		if (preprocessors != null && (!preprocessors.isEmpty())) {
			for (Map<String, Object> p : preprocessors) {
				for (Object key : p.keySet()) {
					try {
						System.err.println("Adding preprocessors " + key);
						Class<?> clazz;
						clazz = Class.forName((String) ((Map) p.get(key)).get("class"));
						System.err.println("Loaded java preprocessors " + clazz.getCanonicalName());
						Constructor<?> ctors = clazz.getConstructors()[0];
						ConfigurablePreProcessor preProcessor = (ConfigurablePreProcessor) ctors.newInstance();
						preProcessor.setName((String) ((Map) p.get(key)).get("name"));
						System.err.println(
							"Loaded java preprocessors groups " + ((Map) p.get(key)).get("groups").toString());
						preProcessor.setGroups((ArrayList<String>) ((Map) p.get(key)).get("groups"));
						preProcessor.setArguments(((Map) p.get(key)).get("arguments"));
						instance.getPreProcessors().addPreProcessor(preProcessor);
					} catch (Exception e) {
						logger.error("Error loading  PostProcessor " + key, e.getMessage());
						e.printStackTrace();
					}

				}
			}
		}

	}

	private void loadsourcesemanticprocessors(Mdmi instance) {
		instance.getSourceSemanticModelProcessors().getSourceSemanticProcessors().clear();
		if (sourcesemanticprocessors != null && !sourcesemanticprocessors.isEmpty()) {
			for (Map<String, Object> p : sourcesemanticprocessors) {
				for (Object key : p.keySet()) {
					try {
						Class<?> clazz;
						clazz = Class.forName((String) ((Map) p.get(key)).get("class"));
						Constructor<?> ctors = clazz.getConstructor(null);
						ConfigurableSemanticProcessor sourceSemanticProcessor = (ConfigurableSemanticProcessor) ctors.newInstance();
						sourceSemanticProcessor.setName((String) ((Map) p.get(key)).get("name"));
						sourceSemanticProcessor.setGroups((ArrayList<String>) ((Map) p.get(key)).get("groups"));
						sourceSemanticProcessor.setArguments(((Map) p.get(key)).get("arguments"));
						instance.getSourceSemanticModelProcessors().addSourceSemanticProcessor(sourceSemanticProcessor);
					} catch (Exception e) {
						logger.error("Error loading  sourceSemanticProcessor " + key, e.getMessage());
						e.printStackTrace();
					}

				}
			}
		}

	}

	private void loadTargetSemanticProcessors(Mdmi instance) {
		instance.getTargetSemanticModelProcessors().getTargetSemanticProcessors().clear();
		if (targetsemanticprocessors != null && !targetsemanticprocessors.isEmpty()) {
			for (Map<String, Object> p : targetsemanticprocessors) {
				for (Object key : p.keySet()) {
					try {
						Class<?> clazz;
						clazz = Class.forName((String) ((Map) p.get(key)).get("class"));
						Constructor<?> ctors = clazz.getConstructor(null);
						ConfigurableSemanticProcessor targetSemanticProcessor = (ConfigurableSemanticProcessor) ctors.newInstance();
						targetSemanticProcessor.setName((String) ((Map) p.get(key)).get("name"));
						targetSemanticProcessor.setGroups((ArrayList<String>) ((Map) p.get(key)).get("groups"));
						targetSemanticProcessor.setArguments(((Map) p.get(key)).get("arguments"));
						instance.getTargetSemanticModelProcessors().addTargetSemanticProcessor(targetSemanticProcessor);
					} catch (Exception e) {
						logger.error("Error loading  targetSemanticProcessor " + key, e.getMessage());
						e.printStackTrace();
					}

				}
			}
		}

	}

}
