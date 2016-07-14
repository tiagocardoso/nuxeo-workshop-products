package org.nuxeo.workshop.products.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject("countryTax")
public class CountryTaxDescriptor {

    private static final Log log = LogFactory.getLog(CountryTaxDescriptor.class);


    public String getCountry() {
        return country;
    }

    public String getTax() {
        return tax;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    @XNode("@country")
    String country;

    @XNode("@tax")
    String tax;

}
