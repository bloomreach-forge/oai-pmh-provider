
package org.onehippo.forge.oaipmh.provider.model.oai.edurep.lom;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementRefs;
import jakarta.xml.bind.annotation.XmlMixed;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for technicalType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="technicalType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsmd_v1p2}format" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsmd_v1p2}size" minOccurs="0"/>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsmd_v1p2}location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsmd_v1p2}requirement" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsmd_v1p2}installationremarks" minOccurs="0"/>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsmd_v1p2}otherplatformrequirements" minOccurs="0"/>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsmd_v1p2}duration" minOccurs="0"/>
 *         &lt;group ref="{http://www.imsglobal.org/xsd/imsmd_v1p2}grp.any"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "technicalType", namespace = "http://www.imsglobal.org/xsd/imsmd_v1p2", propOrder = {
    "content"
})
public class TechnicalType {

    @XmlElementRefs({
        @XmlElementRef(name = "duration", namespace = "http://www.imsglobal.org/xsd/imsmd_v1p2", type = JAXBElement.class),
        @XmlElementRef(name = "size", namespace = "http://www.imsglobal.org/xsd/imsmd_v1p2", type = JAXBElement.class),
        @XmlElementRef(name = "installationremarks", namespace = "http://www.imsglobal.org/xsd/imsmd_v1p2", type = JAXBElement.class),
        @XmlElementRef(name = "otherplatformrequirements", namespace = "http://www.imsglobal.org/xsd/imsmd_v1p2", type = JAXBElement.class),
        @XmlElementRef(name = "location", namespace = "http://www.imsglobal.org/xsd/imsmd_v1p2", type = JAXBElement.class),
        @XmlElementRef(name = "format", namespace = "http://www.imsglobal.org/xsd/imsmd_v1p2", type = JAXBElement.class),
        @XmlElementRef(name = "requirement", namespace = "http://www.imsglobal.org/xsd/imsmd_v1p2", type = JAXBElement.class)
    })
    @XmlMixed
    @XmlAnyElement(lax = true)
    protected List<Object> content;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * {@link jakarta.xml.bind.JAXBElement }{@code <}{@link DurationType }{@code >}
     * {@link jakarta.xml.bind.JAXBElement }{@code <}{@link Integer }{@code >}
     * {@link jakarta.xml.bind.JAXBElement }{@code <}{@link InstallationremarksType }{@code >}
     * {@link jakarta.xml.bind.JAXBElement }{@code <}{@link OtherplatformrequirementsType }{@code >}
     * {@link jakarta.xml.bind.JAXBElement }{@code <}{@link LocationType }{@code >}
     * {@link jakarta.xml.bind.JAXBElement }{@code <}{@link String }{@code >}
     * {@link Object }
     * {@link jakarta.xml.bind.JAXBElement }{@code <}{@link RequirementType }{@code >}
     * 
     * 
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
    }

}
