//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.07.12 at 09:24:55 PM ICT 
//


package com.accenture.dt.dete.domain.schema.studioasset;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.accenture.dt.dete.domain.schema.digitalasset.Asset;
import com.accenture.dt.dete.domain.schema.digitalasset.Rendition;


/**
 * <p>Java class for editedVersion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="editedVersion"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://dete.dt.accenture.com/domain/schema/digitalasset}rendition"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="eidr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="asset" type="{http://dete.dt.accenture.com/domain/schema/digitalasset}asset" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "editedVersion", propOrder = {
    "type",
    "eidr",
    "asset"
})
public class EditedVersion
    extends Rendition
{

    protected String type;
    protected String eidr;
    protected List<Asset> asset;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the eidr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEidr() {
        return eidr;
    }

    /**
     * Sets the value of the eidr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEidr(String value) {
        this.eidr = value;
    }

    /**
     * Gets the value of the asset property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the asset property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAsset().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Asset }
     * 
     * 
     */
    public List<Asset> getAsset() {
        if (asset == null) {
            asset = new ArrayList<Asset>();
        }
        return this.asset;
    }

}
