package org.onehippo.forge.oaipmh.provider.model.oai;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OAI-PMHerrorcodeType.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="OAI-PMHerrorcodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="cannotDisseminateFormat"/>
 *     &lt;enumeration value="idDoesNotExist"/>
 *     &lt;enumeration value="badArgument"/>
 *     &lt;enumeration value="badVerb"/>
 *     &lt;enumeration value="noMetadataFormats"/>
 *     &lt;enumeration value="noRecordsMatch"/>
 *     &lt;enumeration value="badResumptionToken"/>
 *     &lt;enumeration value="noSetHierarchy"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "OAI-PMHerrorcodeType")
@XmlEnum
public enum OAIPMHerrorcodeType {

    @XmlEnumValue("cannotDisseminateFormat")
    CANNOT_DISSEMINATE_FORMAT("cannotDisseminateFormat"),
    @XmlEnumValue("idDoesNotExist")
    ID_DOES_NOT_EXIST("idDoesNotExist"),
    @XmlEnumValue("badArgument")
    BAD_ARGUMENT("badArgument"),
    @XmlEnumValue("badVerb")
    BAD_VERB("badVerb"),
    @XmlEnumValue("noMetadataFormats")
    NO_METADATA_FORMATS("noMetadataFormats"),
    @XmlEnumValue("noRecordsMatch")
    NO_RECORDS_MATCH("noRecordsMatch"),
    @XmlEnumValue("badResumptionToken")
    BAD_RESUMPTION_TOKEN("badResumptionToken"),
    @XmlEnumValue("noSetHierarchy")
    NO_SET_HIERARCHY("noSetHierarchy");
    private final String value;

    OAIPMHerrorcodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OAIPMHerrorcodeType fromValue(String v) {
        for (OAIPMHerrorcodeType c : OAIPMHerrorcodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
