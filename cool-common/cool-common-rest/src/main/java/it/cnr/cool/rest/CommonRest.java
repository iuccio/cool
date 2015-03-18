package it.cnr.cool.rest;

import it.cnr.cool.cmis.service.CMISService;
import it.cnr.cool.cmis.service.CacheService;
import it.cnr.cool.cmis.service.VersionService;
import it.cnr.cool.rest.util.Util;
import it.cnr.cool.security.service.impl.alfresco.CMISUser;
import it.cnr.cool.service.PageService;
import it.cnr.cool.util.StringUtil;
import org.apache.chemistry.opencmis.client.bindings.spi.BindingSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import java.util.HashMap;
import java.util.Map;

@Path("common")
@Component
@Produces(MediaType.APPLICATION_JSON)
public class CommonRest {

	private static final String FTL = "/surf/webscripts/js/common.get.json.ftl";

	@Autowired
	private PageService pageService;

	@Autowired
	private VersionService versionService;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private CMISService cmisService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CommonRest.class);

	@GET
	public Response foo(@Context HttpServletRequest req) {

		ResponseBuilder rb;
		try {

			Map<String, Object> model = new HashMap<String, Object>();

			model.put("artifact_version", versionService.getVersion());

			CMISUser user = cmisService.getCMISUserFromSession(req);

			BindingSession bindingSession = cmisService
					.getCurrentBindingSession(req);

			model.put("caches", cacheService.getCaches(user, bindingSession));

			model.put("cmisDateFormat", StringUtil.CMIS_DATEFORMAT);

			Map<String, Object> context = pageService.getContext(user);

			model.put("context", context);

			String json = Util.processTemplate(model, FTL);
			LOGGER.debug(json);
			rb = Response.ok(json);
            CacheControl cacheControl = new CacheControl();
            cacheControl.setNoCache(true);
            rb.cacheControl(cacheControl);
		} catch (Exception e) {
			LOGGER.error("unable to process common json", e);
			rb = Response.status(Status.INTERNAL_SERVER_ERROR);
		}

		return rb.build();

	}

	@DELETE
	public void delete(@Context HttpServletRequest req, @QueryParam("authortiyName") String authortiyName) {
		BindingSession bindingSession = cmisService
				.getCurrentBindingSession(req);
		if (authortiyName != null) {
			if (authortiyName.startsWith("GROUP_"))
				cacheService.clearGroupCache(authortiyName, bindingSession);
			else
				cacheService.clearCache(authortiyName);
		}
	}
}
