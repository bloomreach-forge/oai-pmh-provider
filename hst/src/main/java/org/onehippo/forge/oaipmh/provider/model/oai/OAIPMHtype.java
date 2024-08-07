package org.onehippo.forge.oaipmh.provider.model.oai;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

import org.onehippo.forge.oaipmh.provider.model.oai.toolkit.ToolkitType;


/**
 * <p>Java class for OAI-PMHtype complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="OAI-PMHtype">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="responseDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="request" type="{http://www.openarchives.org/OAI/2.0/}requestType"/>
 *         &lt;choice>
 *           &lt;element name="error" type="{http://www.openarchives.org/OAI/2.0/}OAI-PMHerrorType"
 * maxOccurs="unbounded"/>
 *           &lt;element name="Identify" type="{http://www.openarchives.org/OAI/2.0/}IdentifyType"/>
 *           &lt;element name="ListMetadataFormats" type="{http://www.openarchives.org/OAI/2.0/}ListMetadataFormatsType"/>
 *           &lt;element name="ListSets" type="{http://www.openarchives.org/OAI/2.0/}ListSetsType"/>
 *           &lt;element name="GetRecord" type="{http://www.openarchives.org/OAI/2.0/}GetRecordType"/>
 *           &lt;element name="ListIdentifiers" type="{http://www.openarchives.org/OAI/2.0/}ListIdentifiersType"/>
 *           &lt;element name="ListRecords" type="{http://www.openarchives.org/OAI/2.0/}ListRecordsType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OAI-PMHtype", namespace = "http://www.openarchives.org/OAI/2.0/", propOrder = {
        "responseDate",
        "request",
        "error",
        "identify",
        "listMetadataFormats",
        "listSets",
        "getRecord",
        "listIdentifiers",
        "listRecords"
})
@XmlSeeAlso(ToolkitType.class)
@XmlRootElement(name = "OAI-PMH")
public class OAIPMHtype {

    @XmlElement(required = true)
    //@XmlSchemaType(name = "dateTime")
    protected String responseDate;
    @XmlElement(required = true)
    protected RequestType request;
    @XmlElement
    protected List<OAIPMHerrorType> error;
    @XmlElement(name = "Identify")
    protected IdentifyType identify;
    @XmlElement(name = "ListMetadataFormats")
    protected ListMetadataFormatsType listMetadataFormats;
    @XmlElement(name = "ListSets")
    protected ListSetsType listSets;
    @XmlElement(name = "GetRecord")
    protected GetRecordType getRecord;
    @XmlElement(name = "ListIdentifiers")
    protected ListIdentifiersType listIdentifiers;
    @XmlElement(name = "ListRecords")
    protected ListRecordsType listRecords;
    @XmlAttribute(name = "xsi:schemaLocation")
    protected String xsi = "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd";
    @XmlAttribute(name = "xmlns")
    protected String xmlns = "http://www.openarchives.org/OAI/2.0/";

    /**
     * Gets the value of the responseDate property.
     *
     * @return possible object is {@link jakarta.xml.datatype.XMLGregorianCalendar }
     */
    public String getResponseDate() {
        return responseDate;
    }

    /**
     * Sets the value of the responseDate property.
     *
     * @param value allowed object is {@link jakarta.xml.datatype.XMLGregorianCalendar }
     */
    public void setResponseDate(String value) {
        this.responseDate = value;
    }

    /**
     * Gets the value of the request property.
     *
     * @return possible object is {@link RequestType }
     */
    public RequestType getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     *
     * @param value allowed object is {@link RequestType }
     */
    public void setRequest(RequestType value) {
        this.request = value;
    }

    /**
     * Gets the value of the error property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the error property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getError().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list {@link OAIPMHerrorType }
     */
    public List<OAIPMHerrorType> getError() {
        if (error == null) {
            error = new ArrayList<OAIPMHerrorType>();
        }
        return this.error;
    }

    /**
     * Gets the value of the identify property.
     *
     * @return possible object is {@link IdentifyType }
     */
    public IdentifyType getIdentify() {
        return identify;
    }

    /**
     * Sets the value of the identify property.
     *
     * @param value allowed object is {@link IdentifyType }
     */
    public void setIdentify(IdentifyType value) {
        this.identify = value;
    }

    /**
     * Gets the value of the listMetadataFormats property.
     *
     * @return possible object is {@link ListMetadataFormatsType }
     */
    public ListMetadataFormatsType getListMetadataFormats() {
        return listMetadataFormats;
    }

    /**
     * Sets the value of the listMetadataFormats property.
     *
     * @param value allowed object is {@link ListMetadataFormatsType }
     */
    public void setListMetadataFormats(ListMetadataFormatsType value) {
        this.listMetadataFormats = value;
    }

    /**
     * Gets the value of the listSets property.
     *
     * @return possible object is {@link ListSetsType }
     */
    public ListSetsType getListSets() {
        return listSets;
    }

    /**
     * Sets the value of the listSets property.
     *
     * @param value allowed object is {@link ListSetsType }
     */
    public void setListSets(ListSetsType value) {
        this.listSets = value;
    }

    /**
     * Gets the value of the getRecord property.
     *
     * @return possible object is {@link GetRecordType }
     */
    public GetRecordType getGetRecord() {
        return getRecord;
    }

    /**
     * Sets the value of the getRecord property.
     *
     * @param value allowed object is {@link GetRecordType }
     */
    public void setGetRecord(GetRecordType value) {
        this.getRecord = value;
    }

    /**
     * Gets the value of the listIdentifiers property.
     *
     * @return possible object is {@link ListIdentifiersType }
     */
    public ListIdentifiersType getListIdentifiers() {
        return listIdentifiers;
    }

    /**
     * Sets the value of the listIdentifiers property.
     *
     * @param value allowed object is {@link ListIdentifiersType }
     */
    public void setListIdentifiers(ListIdentifiersType value) {
        this.listIdentifiers = value;
    }

    /**
     * Gets the value of the listRecords property.
     *
     * @return possible object is {@link ListRecordsType }
     */
    public ListRecordsType getListRecords() {
        return listRecords;
    }

    /**
     * Sets the value of the listRecords property.
     *
     * @param value allowed object is {@link ListRecordsType }
     */
    public void setListRecords(ListRecordsType value) {
        this.listRecords = value;
    }

}
