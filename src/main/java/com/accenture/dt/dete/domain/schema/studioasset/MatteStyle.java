//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.07.12 at 09:24:55 PM ICT 
//


package com.accenture.dt.dete.domain.schema.studioasset;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for matteStyle.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="matteStyle"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="LETTERBOX"/&gt;
 *     &lt;enumeration value="MATTED"/&gt;
 *     &lt;enumeration value="PILLARBOX"/&gt;
 *     &lt;enumeration value="SIDEMATTED"/&gt;
 *     &lt;enumeration value="HYBRID"/&gt;
 *     &lt;enumeration value="FULLFRAME"/&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "matteStyle")
@XmlEnum
public enum MatteStyle {

    LETTERBOX,
    MATTED,
    PILLARBOX,
    SIDEMATTED,
    HYBRID,
    FULLFRAME,
    UNKNOWN;

    public String value() {
        return name();
    }

    public static MatteStyle fromValue(String v) {
        return valueOf(v);
    }

}
