package org.onehippo.forge.oaipmh.provider.model.oai;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListSetsType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="ListSetsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="set" type="{http://www.openarchives.org/OAI/2.0/}setType" maxOccurs="unbounded"/>
 *         &lt;element name="resumptionToken" type="{http://www.openarchives.org/OAI/2.0/}resumptionTokenType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListSetsType", propOrder = {
        "set",
        "resumptionToken"
})
public class ListSetsType {

    @XmlElement(required = true)
    protected List<SetType> set;
    @XmlElement
    protected ResumptionTokenType resumptionToken;

    /**
     * Gets the value of the set property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the set property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSet().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link SetType }
     */
    public List<SetType> getSet() {
        if (set == null) {
            set = new ArrayList<SetType>();
        }
        return this.set;
    }

    /**
     * Gets the value of the resumptionToken property.
     *
     * @return possible object is
     *         {@link ResumptionTokenType }
     */
    public ResumptionTokenType getResumptionToken() {
        return resumptionToken;
    }

    /**
     * Sets the value of the resumptionToken property.
     *
     * @param value allowed object is
     *              {@link ResumptionTokenType }
     */
    public void setResumptionToken(ResumptionTokenType value) {
        this.resumptionToken = value;
    }

}
