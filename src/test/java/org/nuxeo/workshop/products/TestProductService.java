package org.nuxeo.workshop.products;

import com.google.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(FeaturesRunner.class)
@Features({ CoreFeature.class, PlatformFeature.class })
@Deploy({ "org.nuxeo.workshop.products.nuxeo-workshop-products-core", "studio.extensions.aaraujo-SANDBOX"})
public class TestProductService {

    @Inject
    protected ProductService productService;

    @Inject
    protected CoreSession session;

    @Test
    public void testService() {
        assertNotNull(productService);
    }

    @Test
    public void testComputePrice() {
        DocumentModel doc = session.createDocumentModel("/", "test", "TAProduct");
        doc.setPropertyValue("dc:title", "test product");
        doc.setPropertyValue("TAProduct:price", 56.09);
        doc = session.createDocument(doc);
        assertEquals(productService.computePrice(doc), ((Double) doc.getPropertyValue("TAProduct:price")).floatValue(), 0.0f);
    }
}
