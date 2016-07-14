package org.nuxeo.workshop.products.operation;

import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.DocumentRefList;
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
    public void run(DocumentModel doc) {
        updateProductPrice(doc);
    }

    @OperationMethod
    public void run(DocumentRefList docs) {
        for (DocumentRef doc : docs) {
            run(session.getDocument(doc));
        }
    }

    protected void updateProductPrice(DocumentModel doc) {
        ProductService productService = Framework.getService(ProductService.class);
        doc.setPropertyValue("TAProduct:price", Float.valueOf(productService.computePrice(doc)).toString());
        session.saveDocument(doc);
    }
}
