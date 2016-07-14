package org.nuxeo.workshop.products.test;

import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.TransactionalFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.SimpleFeature;

@Deploy({
        "org.nuxeo.workshop.products.nuxeo-workshop-products-core",
        "studio.extensions.aaraujo-SANDBOX",
        "org.nuxeo.ecm.platform.collections.core"
})
@Features({CoreFeature.class, org.nuxeo.ecm.platform.test.PlatformFeature.class,  TransactionalFeature.class, })
public class ProductFeature extends SimpleFeature {
}