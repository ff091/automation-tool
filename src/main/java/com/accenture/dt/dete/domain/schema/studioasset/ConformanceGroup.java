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


/**
 * <p>Java class for conformanceGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="conformanceGroup"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://dete.dt.accenture.com/domain/schema/digitalasset}asset"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="runTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="playlistSegment" type="{http://dete.dt.accenture.com/domain/schema/studioasset}playlistSegment" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="conformanceGroupAssociation" type="{http://dete.dt.accenture.com/domain/schema/studioasset}conformanceAssociation" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conformanceGroup", propOrder = {
    "runTime",
    "playlistSegment",
    "conformanceGroupAssociation"
})
public class ConformanceGroup
    extends Asset
{

    protected String runTime;
    protected List<PlaylistSegment> playlistSegment;
    protected List<ConformanceAssociation> conformanceGroupAssociation;

    /**
     * Gets the value of the runTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRunTime() {
        return runTime;
    }

    /**
     * Sets the value of the runTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRunTime(String value) {
        this.runTime = value;
    }

    /**
     * Gets the value of the playlistSegment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the playlistSegment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlaylistSegment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PlaylistSegment }
     * 
     * 
     */
    public List<PlaylistSegment> getPlaylistSegment() {
        if (playlistSegment == null) {
            playlistSegment = new ArrayList<PlaylistSegment>();
        }
        return this.playlistSegment;
    }

    /**
     * Gets the value of the conformanceGroupAssociation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the conformanceGroupAssociation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConformanceGroupAssociation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConformanceAssociation }
     * 
     * 
     */
    public List<ConformanceAssociation> getConformanceGroupAssociation() {
        if (conformanceGroupAssociation == null) {
            conformanceGroupAssociation = new ArrayList<ConformanceAssociation>();
        }
        return this.conformanceGroupAssociation;
    }

}
