package org.nuxeo.workshop.products;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.workshop.products.service.CountryTaxDescriptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductServiceImpl extends DefaultComponent implements ProductService {
    private static final String COUNTRY_TAX_XP_NAME = "countryTax";
    protected Map<String, Float> taxes = new ConcurrentHashMap<>();

    /**
     * Component activated notification.
     * Called when the component is activated. All component dependencies are resolved at that moment.
     * Use this method to initialize the component.
     *
     * @param context the component context.
     */
    @Override
    public void activate(ComponentContext context) {
        super.activate(context);
    }

    /**
     * Component deactivated notification.
     * Called before a component is unregistered.
     * Use this method to do cleanup if any and free any resources held by the component.
     *
     * @param context the component context.
     */
    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    /**
     * Application started notification.
     * Called after the application started.
     * You can do here any initialization that requires a working application
     * (all resolved bundles and components are active at that moment)
     *
     * @param context the component context. Use it to get the current bundle context
     * @throws Exception
     */
    @Override
    public void applicationStarted(ComponentContext context) {
        // do nothing by default. You can remove this method if not used.
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        if (!COUNTRY_TAX_XP_NAME.equals(extensionPoint)) {
            return;
        }
        CountryTaxDescriptor countryTax = (CountryTaxDescriptor) contribution;
        if (taxes.containsKey(countryTax.getCountry())) {
            taxes.remove(countryTax.getCountry());
        }
        taxes.put(countryTax.getCountry(), Float.valueOf(countryTax.getTax()));
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        if (!COUNTRY_TAX_XP_NAME.equals(extensionPoint)) {
            return;
        }
        CountryTaxDescriptor countryTax = (CountryTaxDescriptor) contribution;
        if (taxes.containsKey(countryTax.getCountry())) {
            taxes.remove(countryTax.getCountry());
        }
    }

    @Override
    public float computePrice(DocumentModel doc) {
        if (doc.getType().equals("TAProduct")) {
            return ((Double) doc.getPropertyValue("TAProduct:price")).floatValue() + 1.0f;
        }
        return 0;
    }

    @Override
    public float computePriceWithTax(DocumentModel doc, String country) {
        if (doc.getType().equals("TAProduct")) {
            float tax = 0.0f;
            if (taxes.containsKey(country)) {
                tax = taxes.get(country);
            }
            return ((Double) doc.getPropertyValue("TAProduct:price")).floatValue() * (1.0f + tax);
        }
        return 0;
    }


}
