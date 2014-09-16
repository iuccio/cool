//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.05.20 at 05:17:49 PM CEST 
//


package it.cnr.cool.mail.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value"
})
@XmlRootElement(name = "Impronta")
public class Impronta {

    @XmlAttribute(name = "algoritmo")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String algoritmo;
    @XmlAttribute(name = "codifica")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String codifica;
    @XmlValue
    protected String value;

    /**
     * Gets the value of the algoritmo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgoritmo() {
        if (algoritmo == null) {
            return "SHA-1";
        } else {
            return algoritmo;
        }
    }

    /**
     * Sets the value of the algoritmo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgoritmo(String value) {
        this.algoritmo = value;
    }

    /**
     * Gets the value of the codifica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodifica() {
        if (codifica == null) {
            return "base64";
        } else {
            return codifica;
        }
    }

    /**
     * Sets the value of the codifica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodifica(String value) {
        this.codifica = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getvalue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setvalue(String value) {
        this.value = value;
    }

}
