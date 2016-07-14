package org.nuxeo.workshop.products.operation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(FeaturesRunner.class)
@Features(AutomationFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy({"org.nuxeo.workshop.products.nuxeo-workshop-products-core", "studio.extensions.aaraujo-SANDBOX"})
public class TestUpdatePrice {

    @Inject
    protected CoreSession session;

    @Inject
    protected AutomationService automationService;


    @Test
    public void shouldCallWithDocument() throws OperationException {
        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        DocumentModel doc = session.createDocumentModel("/", "test", "TAProduct");
        doc.setPropertyValue("dc:title", "test product");
        doc.setPropertyValue("TAProduct:price", 56.09f);
        doc = session.createDocument(doc);
        ctx.setInput(doc);
        automationService.run(ctx, UpdatePrice.ID, params);
        assertEquals(57.09f, ((Double) doc.getPropertyValue("TAProduct:price")).floatValue(), 0.0f);

    }

    @Test
    public void shouldCallWithDocumentList() throws OperationException {
        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        DocumentModel doc = session.createDocumentModel("/", "test", "TAProduct");
        doc.setPropertyValue("dc:title", "test product");
        doc.setPropertyValue("TAProduct:price", 56.09f);
        doc = session.createDocument(doc);
        DocumentModel doc2 = session.createDocumentModel("/", "test2", "TAProduct");
        doc2.setPropertyValue("dc:title", "test product 2");
        doc2.setPropertyValue("TAProduct:price", 568.09f);
        doc2 = session.createDocument(doc2);
        DocumentModelList documentModelList = new DocumentModelListImpl();
        documentModelList.add(doc);
        documentModelList.add(doc2);
        ctx.setInput(documentModelList);
        automationService.run(ctx, UpdatePrice.ID, params);
        assertEquals(57.09f, ((Double) doc.getPropertyValue("TAProduct:price")).floatValue(), 0.0f);
        assertEquals(569.09f, ((Double) doc2.getPropertyValue("TAProduct:price")).floatValue(), 0.0f);
    }
}
