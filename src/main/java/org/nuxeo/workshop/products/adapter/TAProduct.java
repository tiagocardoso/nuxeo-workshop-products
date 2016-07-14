package org.nuxeo.workshop.products.adapter;


import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.workshop.products.ProductService;

public class TAProduct {

    protected DocumentModel doc;

    public TAProduct(DocumentModel doc) {
        this.doc = doc;
    }

    public float getPrice() {
        return getProductService().computePrice(doc);
    }

    public float getPriceWithTax(String country) {
        return getProductService().computePriceWithTax(doc, country);
    }

    protected ProductService getProductService() {
        return Framework.getService(ProductService.class);
    }
}
