package org.nuxeo.workshop.products;

import org.nuxeo.ecm.core.api.DocumentModel;

public interface ProductService {

 public float computePrice(DocumentModel doc);

}
