package org.nuxeo.workshop.products.listener;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.ACP;
import org.nuxeo.ecm.core.api.security.impl.ACPImpl;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.PostCommitFilteringEventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.workshop.products.adapter.TAProduct;

import java.util.Arrays;
import java.util.List;

public class ProductRemovedListener implements PostCommitFilteringEventListener {

    public static String REMOVED_VISUALS_FOLDER = "deprecated_visuals";
    protected final List<String> handled = Arrays.asList("productRemoved");


    @Override
    public void handleEvent(EventBundle events) {
        for (Event event : events) {
            if (acceptEvent(event)) {
                handleEvent(event);
            }
        }
    }

    @Override
    public boolean acceptEvent(Event event) {
        return handled.contains(event.getName());
    }


    public void handleEvent(Event event) {
        EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
            return;
        }

        DocumentEventContext docCtx = (DocumentEventContext) ctx;
        DocumentModel doc = docCtx.getSourceDocument();

        if ("TAProduct".compareTo(doc.getType()) != 0) {
            return;
        }

        TAProduct product = doc.getAdapter(TAProduct.class);
        CoreSession coreSession = doc.getCoreSession();
        DocumentModel folder;
        PathRef documentRef = new PathRef("/default-domain/workspaces/" + REMOVED_VISUALS_FOLDER);
        if (coreSession.exists(documentRef)) {
            folder = coreSession.getDocument(documentRef);
        } else {
            folder = coreSession.createDocumentModel("/default-domain/workspaces", REMOVED_VISUALS_FOLDER, "Folder");
            ACP acp = doc.getACP() != null ? doc.getACP() : new ACPImpl();

            ACE ace = ACE.builder("group1", "READ").isGranted(false).build();
            boolean permissionChanged = false;
            permissionChanged = acp.addACE(ACL.LOCAL_ACL, ace) || permissionChanged;
            if (permissionChanged) {
                doc.setACP(acp, true);
            }
        }
        coreSession.move(product.getVisualsRefs(), folder.getRef());


    }
}
