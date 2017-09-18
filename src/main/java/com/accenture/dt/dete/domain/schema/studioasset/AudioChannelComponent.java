//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.07.12 at 09:24:55 PM ICT 
//


package com.accenture.dt.dete.domain.schema.studioasset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import com.accenture.dt.dete.domain.schema.digitalasset.Component;


/**
 * <p>Java class for audioChannelComponent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="audioChannelComponent"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://dete.dt.accenture.com/domain/schema/digitalasset}component"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="channelPosition" type="{http://dete.dt.accenture.com/domain/schema/studioasset}channelPosition" minOccurs="0"/&gt;
 *         &lt;element name="channelNumber" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="fileChannelNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sourceChannelNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "audioChannelComponent", propOrder = {
    "channelPosition",
    "channelNumber",
    "fileChannelNumber",
    "description",
    "sourceChannelNumber"
})
public class AudioChannelComponent
    extends Component
{

    @XmlSchemaType(name = "string")
    protected ChannelPosition channelPosition;
    protected int channelNumber;
    protected Integer fileChannelNumber;
    protected String description;
    protected Integer sourceChannelNumber;

    /**
     * Gets the value of the channelPosition property.
     * 
     * @return
     *     possible object is
     *     {@link ChannelPosition }
     *     
     */
    public ChannelPosition getChannelPosition() {
        return channelPosition;
    }

    /**
     * Sets the value of the channelPosition property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChannelPosition }
     *     
     */
    public void setChannelPosition(ChannelPosition value) {
        this.channelPosition = value;
    }

    /**
     * Gets the value of the channelNumber property.
     * 
     */
    public int getChannelNumber() {
        return channelNumber;
    }

    /**
     * Sets the value of the channelNumber property.
     * 
     */
    public void setChannelNumber(int value) {
        this.channelNumber = value;
    }

    /**
     * Gets the value of the fileChannelNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFileChannelNumber() {
        return fileChannelNumber;
    }

    /**
     * Sets the value of the fileChannelNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFileChannelNumber(Integer value) {
        this.fileChannelNumber = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the sourceChannelNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSourceChannelNumber() {
        return sourceChannelNumber;
    }

    /**
     * Sets the value of the sourceChannelNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSourceChannelNumber(Integer value) {
        this.sourceChannelNumber = value;
    }

}
