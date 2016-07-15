/**
 *
 */

package org.nuxeo.workshop.products.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.workshop.products.adapter.TAProduct;

import java.io.Serializable;

/**
 * Code skeleton for a Seam bean that will manage a simple action.
 * This can be used to :
 * - do a navigation
 * - do some modification on the currentDocument (or other docs)
 * - create new documents
 * - send/retrive info from an external service
 * - ...
 */
@Name("productsAction")
@Scope(ScopeType.EVENT)
public class ProductsActionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(ProductsActionBean.class);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String name;

    public String location;

    @In(create = true, required = false)
    protected transient CoreSession documentManager;

    @In(create = true)
    protected NavigationContext navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = false)
    protected NuxeoPrincipal currentNuxeoPrincipal;

    @In(create = true)
    protected DocumentsListsManager documentsListsManager;

    public boolean accept() {
        return "TAProduct".equals(navigationContext.getCurrentDocument().getType());
    }

    public void createDistributor() {

        TAProduct product = navigationContext.getCurrentDocument().getAdapter(TAProduct.class);
        product.addDistributor(name, location);
        facesMessages.add(StatusMessage.Severity.INFO, "Added distributor: " + name + " - " + location);
        navigationContext.navigateToDocument(navigationContext.getCurrentDocument());
    }

}
