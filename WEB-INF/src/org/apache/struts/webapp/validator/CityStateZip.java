/*
 * The Apache Software License, Version 1.1
 */

package org.apache.struts.webapp.validator;

/**
 * Just used to provide an indexed properties example.
 *
 * @author David Wintefeldt
*/
public class CityStateZip implements java.io.Serializable {

    private String sCity = null;
    private String sStateProv = null;
    private String[] sZipPostal = new String[3];

    public String getCity() {
       return sCity;	
    }
    
    public void setCity(String sCity) {
       	this.sCity = sCity;
    }

    public String getStateProv() {
       return sStateProv;	
    }
    
    public void setStateProv(String sStateProv) {
       	this.sStateProv = sStateProv;
    }

    public String getZipPostal(int index) {
       return sZipPostal[index];	
    }
    
    public void setZipPostal(int index, String value) {
       	this.sZipPostal[index] = value;
    }

}
