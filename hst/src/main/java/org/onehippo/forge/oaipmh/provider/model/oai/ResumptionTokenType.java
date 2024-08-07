package org.onehippo.forge.oaipmh.provider.model.oai;

import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;

import javax.xml.datatype.XMLGregorianCalendar;


/**
 * A resumptionToken may have 3 optional attributes
 * and can be used in ListSets, ListIdentifiers, ListRecords
 * responses.
 * <p/>
 * <p>Java class for resumptionTokenType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="resumptionTokenType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="expirationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="completeListSize" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute name="cursor" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resumptionTokenType", propOrder = {
        "value"
})
public class ResumptionTokenType {

    @XmlValue
    protected String value;
    @XmlAttribute
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar expirationDate;
    @XmlAttribute
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger completeListSize;
    @XmlAttribute
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger cursor;

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the expirationDate property.
     *
     * @return possible object is
     *         {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value of the expirationDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setExpirationDate(XMLGregorianCalendar value) {
        this.expirationDate = value;
    }

    /**
     * Gets the value of the completeListSize property.
     *
     * @return possible object is
     *         {@link java.math.BigInteger }
     */
    public BigInteger getCompleteListSize() {
        return completeListSize;
    }

    /**
     * Sets the value of the completeListSize property.
     *
     * @param value allowed object is
     *              {@link java.math.BigInteger }
     */
    public void setCompleteListSize(BigInteger value) {
        this.completeListSize = value;
    }

    /**
     * Gets the value of the cursor property.
     *
     * @return possible object is
     *         {@link java.math.BigInteger }
     */
    public BigInteger getCursor() {
        return cursor;
    }

    /**
     * Sets the value of the cursor property.
     *
     * @param value allowed object is
     *              {@link java.math.BigInteger }
     */
    public void setCursor(BigInteger value) {
        this.cursor = value;
    }

}
