package org.onehippo.forge.oaipmh.provider.model.oai;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deletedRecordType.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="deletedRecordType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="no"/>
 *     &lt;enumeration value="persistent"/>
 *     &lt;enumeration value="transient"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "deletedRecordType")
@XmlEnum
public enum DeletedRecordType {

    @XmlEnumValue("no")
    NO("no"),
    @XmlEnumValue("persistent")
    PERSISTENT("persistent"),
    @XmlEnumValue("transient")
    TRANSIENT("transient");
    private final String value;

    DeletedRecordType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DeletedRecordType fromValue(String v) {
        for (DeletedRecordType c : DeletedRecordType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
