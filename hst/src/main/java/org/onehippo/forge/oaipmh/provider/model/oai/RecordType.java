package org.onehippo.forge.oaipmh.provider.model.oai;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * A record has a header, a metadata part, and
 * an optional about container
 * <p/>
 * <p>Java class for recordType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="recordType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://www.openarchives.org/OAI/2.0/}headerType"/>
 *         &lt;element name="metadata" type="{http://www.openarchives.org/OAI/2.0/}metadataType" minOccurs="0"/>
 *         &lt;element name="about" type="{http://www.openarchives.org/OAI/2.0/}aboutType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recordType", propOrder = {
        "header",
        "metadata",
        "about"
})
public class RecordType {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement
    protected MetadataType metadata;
    @XmlElement
    protected List<AboutType> about;

    /**
     * Gets the value of the header property.
     *
     * @return possible object is
     *         {@link HeaderType }
     */
    public HeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     *
     * @param value allowed object is
     *              {@link HeaderType }
     */
    public void setHeader(HeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the metadata property.
     *
     * @return possible object is
     *         {@link MetadataType }
     */
    public MetadataType getMetadata() {
        return metadata;
    }

    /**
     * Sets the value of the metadata property.
     *
     * @param value allowed object is
     *              {@link MetadataType }
     */
    public void setMetadata(MetadataType value) {
        this.metadata = value;
    }

    /**
     * Gets the value of the about property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the about property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbout().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link AboutType }
     */
    public List<AboutType> getAbout() {
        if (about == null) {
            about = new ArrayList<AboutType>();
        }
        return this.about;
    }

}
