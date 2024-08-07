package org.onehippo.forge.oaipmh.provider.model.oai;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetRecordType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="GetRecordType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="record" type="{http://www.openarchives.org/OAI/2.0/}recordType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetRecordType", propOrder = {
        "record"
})
public class GetRecordType {

    @XmlElement(required = true)
    protected RecordType record;

    /**
     * Gets the value of the record property.
     *
     * @return possible object is
     *         {@link RecordType }
     */
    public RecordType getRecord() {
        return record;
    }

    /**
     * Sets the value of the record property.
     *
     * @param value allowed object is
     *              {@link RecordType }
     */
    public void setRecord(RecordType value) {
        this.record = value;
    }

}
