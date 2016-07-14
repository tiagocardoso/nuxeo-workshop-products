package org.nuxeo.workshop.products.operation;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.workshop.products.ProductService;

/**
 *
 */
@Operation(id = UpdatePrice.ID, category = Constants.CAT_DOCUMENT, label = "TAUpdateProductPrice",
        description = "Describe here what your operation does.")
public class UpdatePrice {

    public static final String ID = "Document.UpdatePrice";

    @Context
    protected CoreSession session;

    @OperationMethod
    public DocumentModel run(DocumentModel doc) {
        updateProductPrice(doc);
        return doc;
    }

    @OperationMethod
    public DocumentModelList run(DocumentRefList docs) {
        DocumentModelListImpl result = new DocumentModelListImpl(
                (int) docs.totalSize());
        for (DocumentRef doc : docs) {
            result.add(run(session.getDocument(doc)));
        }
        return result;
    }

    protected void updateProductPrice(DocumentModel doc) {
        ProductService productService = Framework.getService(ProductService.class);
        doc.setPropertyValue("TAProduct:price", Float.valueOf(productService.computePrice(doc)).toString());
        session.saveDocument(doc);
    }
}
