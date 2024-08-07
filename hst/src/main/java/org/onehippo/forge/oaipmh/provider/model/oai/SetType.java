package org.onehippo.forge.oaipmh.provider.model.oai;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="setType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="setSpec" type="{http://www.openarchives.org/OAI/2.0/}setSpecType"/>
 *         &lt;element name="setName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="setDescription" type="{http://www.openarchives.org/OAI/2.0/}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setType", propOrder = {
        "setSpec",
        "setName",
        "setDescription"
})
public class SetType {

    @XmlElement(required = true)
    protected String setSpec;
    @XmlElement(required = true)
    protected String setName;
    @XmlElement
    protected List<DescriptionType> setDescription;

    /**
     * Gets the value of the setSpec property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSetSpec() {
        return setSpec;
    }

    /**
     * Sets the value of the setSpec property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSetSpec(String value) {
        this.setSpec = value;
    }

    /**
     * Gets the value of the setName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSetName() {
        return setName;
    }

    /**
     * Sets the value of the setName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSetName(String value) {
        this.setName = value;
    }

    /**
     * Gets the value of the setDescription property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the setDescription property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSetDescription().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link DescriptionType }
     */
    public List<DescriptionType> getSetDescription() {
        if (setDescription == null) {
            setDescription = new ArrayList<DescriptionType>();
        }
        return this.setDescription;
    }

}
