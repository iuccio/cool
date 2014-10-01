package it.cnr.cool.service.modelDesigner;

import static org.junit.Assert.assertTrue;
import it.cnr.cool.cmis.model.ModelPropertiesIds;
import it.cnr.cool.cmis.service.JaxBHelper;
import it.cnr.cool.service.util.AlfrescoDocument;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.alfresco.model.dictionary._1.Model;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/cool-model-test-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class ModelDesignerServiceTest {

	@Autowired
	private ModelDesignerService modelDesignerService;
	@Autowired
	private JaxBHelper jaxBHelper;
	@Autowired
	private Service service;
	private Session cmisSession;
	private String version;
	private String nodeRefModel;
	private String suffisso;
	final static String MODEL_NAME = "modelTest";
	final static String TEMPLATE_NAME = "testTemplate";
	final Date data = new Date();
	private Document template = null;
	private static String nameAspect;

	@Before
	public void createModel() {
		suffisso = "" + data.getTime();

		cmisSession = service.getAdminSession();
		String xml = null;
		try {
			xml = IOUtils
					.toString(getClass().getResourceAsStream("/model.xml"));
		} catch (IOException e) {
			System.err
					.println("Errore nel caricamento del contenuto del modello dal File System");
		}
		Map<String, Object> resp;
		xml = xml.replace("test", suffisso);

		resp = modelDesignerService.updateModel(cmisSession, xml, MODEL_NAME
				+ suffisso, nodeRefModel, false, null);
		assertTrue(resp.get("status").equals("ok"));
		nodeRefModel = ((String) resp.get("nodeRefModel")).split(";")[0];
		version = ((String) resp.get("nodeRefModel")).split(";")[1];
		modelDesignerService.activateModel(cmisSession, nodeRefModel + ";"
				+ version, true);
	}

	@After
	public void deleteModel() {
		// il template solitamente è null perché viene creato solo nel test
		// testUpdateModelGenerateTemplate
		if (template != null) {
			// rimuovo l'aspect
			Map<String, Object> properties = new HashMap<String, Object>();
			List<Object> aspects = template.getProperty(
					PropertyIds.SECONDARY_OBJECT_TYPE_IDS).getValues();
			aspects.remove(nameAspect);
			properties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, aspects);
			template.updateProperties(properties);

			template.delete(true);
		}
		Map<String, Object> resp = modelDesignerService.deleteModel(
				cmisSession, nodeRefModel);
		assertTrue(((String) resp.get("status")).equals("ok"));
	}

	@Test
	public void testActivateModel() {
		Boolean active = false;
		Map<String, Object> resp = modelDesignerService.activateModel(
				cmisSession, nodeRefModel, active);
		assertTrue(((String) resp.get("status")).equals("disactivate"));
		assertTrue(cmisSession.getObject(nodeRefModel)
				.getPropertyValue(ModelPropertiesIds.MODEL_ACTIVE.value())
				.equals(active));
		active = true;
		resp = modelDesignerService.activateModel(cmisSession, nodeRefModel,
				active);
		assertTrue(((String) resp.get("status")).equals("activate"));
		assertTrue(cmisSession.getObject(nodeRefModel)
				.getPropertyValue(ModelPropertiesIds.MODEL_ACTIVE.value())
				.equals(active));
	}

	@Test
	public void testDeleteProperty() {
		// cancello la property "test:toDelete"
		Map<String, Object> resp = modelDesignerService.deleteProperty(
				cmisSession, nodeRefModel, suffisso + ":document", suffisso
						+ ":toDelete");
		assertTrue(((String) resp.get("status")).equals("ok"));
		Document doc = (Document) cmisSession.getObject(nodeRefModel);

		Model modello;
		try {
			modello = jaxBHelper.unmarshal(
					new StreamSource(doc.getContentStream().getStream()),
					Model.class, false).getValue();
			// controlllo che, dopo la cancellazione della property del primo
			// type definito nel model.xml, ne sia rimasta solo una
			assertTrue(modello.getTypes().getType().get(0).getProperties()
					.getProperty().size() == 1);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetDocs() {
		List<AlfrescoDocument> docs = modelDesignerService.getDocsByPath(
				cmisSession, nodeRefModel);
		assertTrue(docs.size() == 0);

		docs = modelDesignerService.getDocsByTypeName(cmisSession, suffisso
				+ ":document");
		assertTrue(docs.size() == 0);
	}

	@Test
	public void testInvalidUpdate() throws IOException {
		String invalidXml = IOUtils.toString(getClass().getResourceAsStream(
				"/invalidModel.xml"));
		invalidXml = invalidXml.replace("test", suffisso);
		// provo una modifica NON consentita (eliminazione del type)
		Map<String, Object> resp = modelDesignerService.updateModel(
				cmisSession, invalidXml, ModelDesignerServiceTest.MODEL_NAME
						+ suffisso, nodeRefModel, false, null);
		assertTrue(resp.get("status").equals("ko"));
		assertTrue(((String) resp.get("message"))
				.contains("Failed to validate model update - found deleted TYPE '{http://www.cnr.it/model/"
						+ suffisso + "}document'"));
	}

	@Test
	public void testUpdateModel() throws IOException {
		String updateXml = IOUtils.toString(getClass().getResourceAsStream(
				"/updateModel.xml"));

		updateXml = updateXml.replace("test", suffisso);
		// provo una modifica consentita
		Map<String, Object> resp = modelDesignerService.updateModel(
				cmisSession, updateXml, ModelDesignerServiceTest.MODEL_NAME
						+ data.getTime(), nodeRefModel, false, null);
		assertTrue(resp.get("status").equals("ok"));
	}

	@Test
	public void testUpdateModelGenerateTemplate() throws IOException {
		String updateXml = IOUtils.toString(getClass().getResourceAsStream(
				"/updateModel.xml"));

		updateXml = updateXml.replace("test", suffisso);
		// provo una modifica consentita
		Map<String, Object> resp = modelDesignerService.updateModel(
				cmisSession, updateXml, ModelDesignerServiceTest.MODEL_NAME
						+ data.getTime(), nodeRefModel, true, TEMPLATE_NAME);

		assertTrue(resp.get("status").equals("ok"));

		template = (Document) cmisSession
				.getObjectByPath(modelDesignerService.nodeTemplatesPath + "/"
						+ TEMPLATE_NAME);

		nameAspect = "P:" + suffisso + ":aspect";
		assertTrue(template.getProperty(PropertyIds.SECONDARY_OBJECT_TYPE_IDS)
				.getValues().contains(nameAspect));
	}
}