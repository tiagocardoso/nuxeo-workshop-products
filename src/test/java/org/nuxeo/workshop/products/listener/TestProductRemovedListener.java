package org.nuxeo.workshop.products.listener;

import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.collections.core.adapter.Collection;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.ecm.core.storage.sql.coremodel.SQLSession;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.workshop.products.test.ProductFeature;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.nuxeo.ecm.platform.query.api.AbstractPageProvider.log;
import static org.nuxeo.workshop.products.listener.ProductRemovedListener.REMOVED_VISUALS_FOLDER;

@RunWith(FeaturesRunner.class)
@Features({CoreFeature.class, PlatformFeature.class, ProductFeature.class})
public class TestProductRemovedListener {

    protected final List<String> events = Arrays.asList("documentRemoved");

    @Inject
    protected EventService s;

    @Inject
    protected CoreSession session;

    @Before
    public void setUp() {
        Framework.getProperties().setProperty(SQLSession.ALLOW_NEGATIVE_ACL_PROPERTY, "true");
    }

    @Test
    public void listenerRegistration() {
        EventListenerDescriptor listener = s.getEventListener("product-removed-listener");
        assertNotNull(listener);
    }

    @Test
    public void removedProduct() {
        DocumentModel product = session.createDocumentModel("/", "test", "TAProduct");
        product.setPropertyValue("dc:title", "test product");
        product.setPropertyValue("TAProduct:price", 56.09f);
        product = session.createDocument(product);
        DocumentModel visual = session.createDocumentModel("/", "test", "TAVisual");
        visual.setPropertyValue("dc:title", "test product");
        visual.setPropertyValue("TAProduct:price", 56.09f);
        visual = session.createDocument(visual);

        Collection collection = product.getAdapter(Collection.class);
        collection.addDocument(visual.getId());

        EventProducer eventProducer;
        try {
            eventProducer = Framework.getService(EventProducer.class);
        } catch (Exception e) {
            log.error("Cannot get EventProducer", e);
            return;
        }

        DocumentEventContext ctx = new DocumentEventContext(session, session.getPrincipal(), product);
        Event event = ctx.newEvent("productRemoved");
        eventProducer.fireEvent(event);
        s.waitForAsyncCompletion(); // not awaiting!!!
        PathRef documentRef = new PathRef("/default-domain/workspaces/" + REMOVED_VISUALS_FOLDER);
        /*
        assertTrue(session.exists(documentRef));
        DocumentModel visualFolder = session.getDocument(documentRef);
        assertEquals(visual.getPathAsString(), "/default-domain/workspaces" + REMOVED_VISUALS_FOLDER);
        ACP acp = visual.getACP();
        assertEquals(Access.DENY, acp.getAccess("members", SecurityConstants.READ));
        */
    }
}
