package org.mdmi.rt.service.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.mdmi.core.Mdmi;
import org.mdmi.core.engine.MdmiUow;
import org.mdmi.core.engine.postprocessors.ConfigurablePostProcessor;
import org.mdmi.core.engine.preprocessors.ConfigurablePreProcessor;
import org.mdmi.core.engine.terminology.FHIRTerminologyTransform;
import org.mdmi.core.runtime.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

@RestController
@RequestMapping("/mdmi/transformation")
public class MdmiEngine {

	@Autowired
	private ConfigurableApplicationContext applicationContext;

	@Autowired
	FHIRTerminologySettings terminologySettings;

	@Autowired
	ServletContext context;

	static Boolean loaded = Boolean.FALSE;

	@Value("#{systemProperties['mdmi.maps'] ?: '/maps'}")
	private String mapsFolders;

	private HashMap<String, Properties> mapProperties = new HashMap<>();

	private static Logger logger = LoggerFactory.getLogger(MdmiEngine.class);

	static List<Map<String, Object>> postprocessors = new ArrayList<Map<String, Object>>();

	static List<Map<String, Object>> preprocessors = new ArrayList<Map<String, Object>>();

	@SuppressWarnings("unchecked")
	private void loadMaps() throws IOException {
		synchronized (this) {
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
						InputStream targetStream = new FileInputStream(folder.toString() + "/" + map);
						Mdmi.INSTANCE().getResolver().resolve(targetStream);
					}
					if (Files.exists(Paths.get(folder.toString() + "/" + "processors.yml"))) {
						Yaml processorYaml = new Yaml();
						InputStream inputStream = new FileInputStream(folder.toString() + "/" + "processors.yml");
						Map<String, Object> obj = processorYaml.load(inputStream);
						postprocessors.add((Map<String, Object>) obj.get("postprocessors"));
						preprocessors.add((Map<String, Object>) obj.get("preprocessors"));
					}

				}

			}

			loaded = Boolean.TRUE;

		}

	}

	private void reloadMaps() throws IOException {
		synchronized (this) {
			loaded = false;
			mapProperties.clear();
			postprocessors.clear();
			preprocessors.clear();
			loadMaps();
		}
	}

	private Properties getMapProperties(String target) {
		for (String mapsFolder : Stream.of(mapsFolders.split(",", -1)).collect(Collectors.toList())) {
			if (!mapProperties.containsKey(target)) {
				Properties properties = new Properties();
				Path propertyFile = Paths.get(context.getRealPath(mapsFolder + "/" + target + ".properties"));
				if (Files.exists(propertyFile)) {
					try {
						properties.load(Files.newInputStream(propertyFile));
					} catch (IOException e) {
					}
				}
				Path valuesFile = Paths.get(context.getRealPath(mapsFolder + "/" + target + ".json"));
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

	@GetMapping
	public String get(HttpServletRequest req) throws Exception {
		loadMaps();
		loadPreProcessors(Mdmi.INSTANCE());
		loadPostProcessors(Mdmi.INSTANCE());
		return Mdmi.INSTANCE().getResolver().getEngineConfigurations();
	}

	@GetMapping(path = "reset")
	public String reset(HttpServletRequest req) throws Exception {
		reloadMaps();
		return Mdmi.INSTANCE().getResolver().getEngineConfigurations();
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public String transformation(@Context HttpServletRequest req, @RequestParam("source") String source,
			@RequestParam("target") String target, @RequestPart("message") MultipartFile uploadedInputStream)
			throws Exception {
		logger.debug("DEBUG Start transformation ");
		loadMaps();
		loadPreProcessors(Mdmi.INSTANCE());
		loadPostProcessors(Mdmi.INSTANCE());
		MdmiUow.setSerializeSemanticModel(false);
		Mdmi.INSTANCE().getSourceSemanticModelProcessors().addSourceSemanticProcessor(new ProcessRelationships());
		String result = RuntimeService.runTransformation(
			source, uploadedInputStream.getBytes(), target, null, getMapProperties(source), getMapProperties(target));
		return result;
	}

	@PostMapping(path = "byvalue", consumes = {
			MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
					MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public String transformation2(@Context HttpServletRequest req, @RequestParam("source") String source,
			@RequestParam("target") String target, @RequestBody String message) throws Exception {
		logger.debug("DEBUG Start transformation ");
		loadMaps();
		loadPreProcessors(Mdmi.INSTANCE());
		loadPostProcessors(Mdmi.INSTANCE());
		MdmiUow.setSerializeSemanticModel(false);
		Mdmi.INSTANCE().getSourceSemanticModelProcessors().addSourceSemanticProcessor(new ProcessRelationships());

		String result = RuntimeService.runTransformation(
			source, message.getBytes(), target, null, getMapProperties(source), getMapProperties(target));
		return result;
	}

	private void loadPostProcessors(Mdmi instance) {

		if (postprocessors != null) {

			for (Map<String, Object> p : postprocessors) {

				for (Object key : p.keySet()) {

					try {
						Class<?> clazz;
						clazz = Class.forName((String) ((Map) p.get(key)).get("class"));
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

		}

	}

	private void loadPreProcessors(Mdmi instance) {

		if (preprocessors != null) {
			for (Map<String, Object> p : preprocessors) {
				for (Object key : p.keySet()) {
					try {
						Class<?> clazz;
						clazz = Class.forName((String) ((Map) p.get(key)).get("class"));
						Constructor<?> ctors = clazz.getConstructors()[0];
						ConfigurablePreProcessor preProcessor = (ConfigurablePreProcessor) ctors.newInstance();
						preProcessor.setName((String) ((Map) p.get(key)).get("name"));
						preProcessor.setGroups((ArrayList<String>) ((Map) p.get(key)).get("groups"));
						preProcessor.setArguments(((Map) p.get(key)).get("arguments"));
						instance.getPreProcessors().addPreProcessor(preProcessor);
					} catch (Exception e) {
						logger.error("Error loading  PreProcessor " + key, e.getMessage());
						e.printStackTrace();
					}

				}
			}
		}

	}

}
