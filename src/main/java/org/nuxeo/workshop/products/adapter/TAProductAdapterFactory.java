package org.nuxeo.workshop.products.adapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

public class TAProductAdapterFactory implements DocumentAdapterFactory {

    private static final Log log = LogFactory.getLog(TAProductAdapterFactory.class);

    public Object getAdapter(DocumentModel doc, Class cls) {
        return new TAProduct(doc);
    }

}
