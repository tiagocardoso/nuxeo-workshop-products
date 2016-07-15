package org.nuxeo.workshop.products.adapter;


import org.nuxeo.ecm.collections.core.adapter.Collection;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.ecm.core.api.model.Property;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.workshop.products.ProductService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public DocumentModelList getVisuals() {
        DocumentModelList collectionMembers = new DocumentModelListImpl();
        CoreSession coreSession = doc.getCoreSession();
        collectionMembers.addAll(getVisualsRefs().stream()
                .map(coreSession::getDocument)
                .collect(Collectors.toList()));
        return collectionMembers;
    }

    public List<DocumentRef> getVisualsRefs() {
        Collection collection = doc.getAdapter(Collection.class);
        List<String> collectedIds = collection.getCollectedDocumentIds();
        List<DocumentRef> collectionMembers = new ArrayList<>();
        collectionMembers.addAll(collectedIds.stream()
                .map(IdRef::new)
                .collect(Collectors.toList()));
        return collectionMembers;
    }

    public void addDistributor(String name, String location) {
        List<Map<String, Serializable>> distributors = new ArrayList<>();

        Property distributorsProperty = doc.getProperty("TAProduct:distributor");
        for (Property distributorProperty: distributorsProperty) {
            Map<String, Serializable> distributor = new HashMap<>();
            distributor.put("name", distributorProperty.getValue("name"));
            distributor.put("location", distributorProperty.getValue("location"));

            distributors.add(distributor);
        }

        Map<String, Serializable> distributor = new HashMap<>();
        distributor.put("name", name);
        distributor.put("location", location);

        distributors.add(distributor);
        doc.setPropertyValue("TAProduct:distributor", (Serializable)distributors);
        doc.getCoreSession().saveDocument(doc);

    }

}
