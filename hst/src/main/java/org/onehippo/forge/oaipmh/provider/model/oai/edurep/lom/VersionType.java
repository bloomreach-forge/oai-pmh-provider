
package org.onehippo.forge.oaipmh.provider.model.oai.edurep.lom;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for versionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="versionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.imsglobal.org/xsd/imsmd_v1p2}langstring" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "versionType", namespace = "http://www.imsglobal.org/xsd/imsmd_v1p2", propOrder = {
    "langstring"
})
public class VersionType {

    @XmlElement(namespace = "http://www.imsglobal.org/xsd/imsmd_v1p2", required = true)
    protected List<LangstringType> langstring;

    /**
     * Gets the value of the langstring property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the langstring property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLangstring().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LangstringType }
     * 
     * 
     */
    public List<LangstringType> getLangstring() {
        if (langstring == null) {
            langstring = new ArrayList<LangstringType>();
        }
        return this.langstring;
    }

}
