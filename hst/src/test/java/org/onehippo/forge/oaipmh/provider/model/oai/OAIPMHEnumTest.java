package org.onehippo.forge.oaipmh.provider.model.oai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OAIPMHEnumTest {

    @Test
    void errorCodeType_fromValue_roundTrips() {
        assertEquals(OAIPMHerrorcodeType.BAD_ARGUMENT, OAIPMHerrorcodeType.fromValue("badArgument"));
        assertEquals(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, OAIPMHerrorcodeType.fromValue("idDoesNotExist"));
        assertEquals(OAIPMHerrorcodeType.BAD_VERB, OAIPMHerrorcodeType.fromValue("badVerb"));
        assertEquals(OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT, OAIPMHerrorcodeType.fromValue("cannotDisseminateFormat"));
        assertEquals(OAIPMHerrorcodeType.NO_METADATA_FORMATS, OAIPMHerrorcodeType.fromValue("noMetadataFormats"));
        assertEquals(OAIPMHerrorcodeType.NO_RECORDS_MATCH, OAIPMHerrorcodeType.fromValue("noRecordsMatch"));
        assertEquals(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN, OAIPMHerrorcodeType.fromValue("badResumptionToken"));
        assertEquals(OAIPMHerrorcodeType.NO_SET_HIERARCHY, OAIPMHerrorcodeType.fromValue("noSetHierarchy"));
    }

    @Test
    void errorCodeType_fromValue_unknownThrows() {
        assertThrows(IllegalArgumentException.class, () -> OAIPMHerrorcodeType.fromValue("unknown"));
    }

    @Test
    void errorCodeType_value_returnsXmlString() {
        assertEquals("badArgument", OAIPMHerrorcodeType.BAD_ARGUMENT.value());
    }

    @Test
    void granularityType_fromValue_roundTrips() {
        assertEquals(GranularityType.YYYY_MM_DD, GranularityType.fromValue("YYYY-MM-DD"));
        assertEquals(GranularityType.YYYY_MM_DD_THH_MM_SS_Z, GranularityType.fromValue("YYYY-MM-DDThh:mm:ssZ"));
    }

    @Test
    void granularityType_fromValue_unknownThrows() {
        assertThrows(IllegalArgumentException.class, () -> GranularityType.fromValue("invalid"));
    }

    @Test
    void verbType_fromValue_roundTrips() {
        assertEquals(VerbType.IDENTIFY, VerbType.fromValue("Identify"));
        assertEquals(VerbType.LIST_RECORDS, VerbType.fromValue("ListRecords"));
        assertEquals(VerbType.GET_RECORD, VerbType.fromValue("GetRecord"));
    }

    @Test
    void deletedRecordType_values_containsExpectedEntries() {
        assertTrue(DeletedRecordType.values().length >= 2);
    }

}
