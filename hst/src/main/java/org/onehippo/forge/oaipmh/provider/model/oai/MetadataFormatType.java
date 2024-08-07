package org.onehippo.forge.oaipmh.provider.model.oai;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for metadataFormatType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="metadataFormatType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="metadataPrefix" type="{http://www.openarchives.org/OAI/2.0/}metadataPrefixType"/>
 *         &lt;element name="schema" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="metadataNamespace" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metadataFormatType", propOrder = {
        "metadataPrefix",
        "schema",
        "metadataNamespace"
})
public class MetadataFormatType {

    @XmlElement(required = true)
    protected String metadataPrefix;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String schema;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String metadataNamespace;

    /**
     * Gets the value of the metadataPrefix property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    /**
     * Sets the value of the metadataPrefix property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMetadataPrefix(String value) {
        this.metadataPrefix = value;
    }

    /**
     * Gets the value of the schema property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSchema() {
        return schema;
    }

    /**
     * Sets the value of the schema property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSchema(String value) {
        this.schema = value;
    }

    /**
     * Gets the value of the metadataNamespace property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getMetadataNamespace() {
        return metadataNamespace;
    }

    /**
     * Sets the value of the metadataNamespace property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMetadataNamespace(String value) {
        this.metadataNamespace = value;
    }

}
