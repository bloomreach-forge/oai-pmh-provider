package org.onehippo.forge.oaipmh.provider.model.oai;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IdentifyType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="IdentifyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="repositoryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="baseURL" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="protocolVersion" type="{http://www.openarchives.org/OAI/2.0/}protocolVersionType"/>
 *         &lt;element name="adminEmail" type="{http://www.openarchives.org/OAI/2.0/}emailType" maxOccurs="unbounded"/>
 *         &lt;element name="earliestDatestamp" type="{http://www.openarchives.org/OAI/2.0/}UTCdatetimeType"/>
 *         &lt;element name="deletedRecord" type="{http://www.openarchives.org/OAI/2.0/}deletedRecordType"/>
 *         &lt;element name="granularity" type="{http://www.openarchives.org/OAI/2.0/}granularityType"/>
 *         &lt;element name="compression" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.openarchives.org/OAI/2.0/}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdentifyType", propOrder = {
        "repositoryName",
        "baseURL",
        "protocolVersion",
        "adminEmail",
        "earliestDatestamp",
        "deletedRecord",
        "granularity",
        "compression",
        "description"
})
public class IdentifyType {

    @XmlElement(required = true)
    protected String repositoryName;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String baseURL;
    @XmlElement(required = true)
    protected String protocolVersion;
    @XmlElement(required = true)
    protected List<String> adminEmail;
    @XmlElement(required = true)
    protected String earliestDatestamp;
    @XmlElement(required = true)
    protected DeletedRecordType deletedRecord;
    @XmlElement(required = true)
    protected GranularityType granularity;
    @XmlElement
    protected List<String> compression;
    @XmlElement
    protected List<DescriptionType> description;

    /**
     * Gets the value of the repositoryName property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRepositoryName() {
        return repositoryName;
    }

    /**
     * Sets the value of the repositoryName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRepositoryName(String value) {
        this.repositoryName = value;
    }

    /**
     * Gets the value of the baseURL property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * Sets the value of the baseURL property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBaseURL(String value) {
        this.baseURL = value;
    }

    /**
     * Gets the value of the protocolVersion property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getProtocolVersion() {
        return protocolVersion;
    }

    /**
     * Sets the value of the protocolVersion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setProtocolVersion(String value) {
        this.protocolVersion = value;
    }

    /**
     * Gets the value of the adminEmail property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the adminEmail property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdminEmail().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getAdminEmail() {
        if (adminEmail == null) {
            adminEmail = new ArrayList<String>();
        }
        return this.adminEmail;
    }

    /**
     * Gets the value of the earliestDatestamp property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEarliestDatestamp() {
        return earliestDatestamp;
    }

    /**
     * Sets the value of the earliestDatestamp property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEarliestDatestamp(String value) {
        this.earliestDatestamp = value;
    }

    /**
     * Gets the value of the deletedRecord property.
     *
     * @return possible object is
     *         {@link DeletedRecordType }
     */
    public DeletedRecordType getDeletedRecord() {
        return deletedRecord;
    }

    /**
     * Sets the value of the deletedRecord property.
     *
     * @param value allowed object is
     *              {@link DeletedRecordType }
     */
    public void setDeletedRecord(DeletedRecordType value) {
        this.deletedRecord = value;
    }

    /**
     * Gets the value of the granularity property.
     *
     * @return possible object is
     *         {@link GranularityType }
     */
    public GranularityType getGranularity() {
        return granularity;
    }

    /**
     * Sets the value of the granularity property.
     *
     * @param value allowed object is
     *              {@link GranularityType }
     */
    public void setGranularity(GranularityType value) {
        this.granularity = value;
    }

    /**
     * Gets the value of the compression property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compression property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompression().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getCompression() {
        if (compression == null) {
            compression = new ArrayList<String>();
        }
        return this.compression;
    }

    /**
     * Gets the value of the description property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link DescriptionType }
     */
    public List<DescriptionType> getDescription() {
        if (description == null) {
            description = new ArrayList<DescriptionType>();
        }
        return this.description;
    }

}
