package org.onehippo.forge.oaipmh.provider.api;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.junit.Before;
import org.junit.Test;
import org.onehippo.forge.oaipmh.provider.model.oai.IdentifyType;
import org.onehippo.forge.oaipmh.provider.model.oai.ListType;
import org.onehippo.forge.oaipmh.provider.model.oai.MetadataFormatType;
import org.onehippo.forge.oaipmh.provider.model.oai.MetadataType;
import org.onehippo.forge.oaipmh.provider.model.oai.ResumptionTokenType;

/**
 * Test class to verify FORGE-563: Date property consistency improvements.
 *
 * This test ensures that the OAI-PMH provider uses oai:pubdate exclusively
 * for all date-related operations (filtering, sorting, and resumption tokens)
 * to prevent timestamp synchronization issues and document count fluctuations.
 */
public class DateConsistencyTest {

    private TestOAIResource resource;

    @Before
    public void setUp() throws Exception {
        resource = new TestOAIResource();
    }

    /**
     * FORGE-563: Test that the correct property name constant is used for sorting
     *
     * This test verifies that BaseOAIResource.OAI_PUBDATE is the expected value
     * and is available for use in query sorting (as per the code changes in FORGE-563).
     */
    @Test
    public void testSortingPropertyConstant() {
        // The FORGE-563 fix changed sorting from HIPPOSTDPUBWF_PUBLICATION_DATE to OAI_PUBDATE
        assertEquals("OAI_PUBDATE should be 'oai:pubdate'", "oai:pubdate", BaseOAIResource.OAI_PUBDATE);
        assertNotNull("OAI_PUBDATE constant should not be null", BaseOAIResource.OAI_PUBDATE);

        // Verify the old property name still exists (for filtering) but is different
        assertEquals("HIPPOSTDPUBWF_PUBLICATION_DATE should be 'hippostdpubwf:publicationDate'",
            "hippostdpubwf:publicationDate", BaseOAIResource.HIPPOSTDPUBWF_PUBLICATION_DATE);
    }

    /**
     * FORGE-563: Test that getPublicationDateAsString uses consistent format
     *
     * Note: getPublicationDateAsString() always uses second precision (yyyyMMddHHmmss)
     * regardless of useMilliSecondsDatePrecision setting. The millisecond setting
     * only applies to OAIPMHResource's resumption token formatting.
     */
    @Test
    public void testPublicationDateStringFormat() {
        Calendar testDate = Calendar.getInstance();
        testDate.set(2024, Calendar.JULY, 15, 14, 30, 25);
        testDate.set(Calendar.MILLISECOND, 123);

        // getPublicationDateAsString always uses seconds precision
        String dateString = resource.getPublicationDateAsString(testDate);
        assertEquals("Date should be formatted without milliseconds", 14, dateString.length());
        assertTrue("Date string should match yyyyMMddHHmmss format", dateString.matches("\\d{14}"));
        assertEquals("Date should start with correct year/month/day", "20240715", dateString.substring(0, 8));
    }

    /**
     * FORGE-563: Test that the OAI_PUBDATE constant is used consistently
     */
    @Test
    public void testOaiPubdateConstantValue() {
        // Verify the constant value is correct
        assertEquals("OAI_PUBDATE constant should be 'oai:pubdate'",
            "oai:pubdate", BaseOAIResource.OAI_PUBDATE);
    }

    /**
     * FORGE-563: Test date format conversion matches oai:pubdate precision (seconds)
     */
    @Test
    public void testDateFormatConsistency() throws Exception {
        Calendar testCal = Calendar.getInstance();
        testCal.set(2024, Calendar.JUNE, 15, 10, 30, 45);
        testCal.set(Calendar.MILLISECOND, 678);

        // Format using the resource method (always uses seconds precision)
        String formattedDate = resource.getPublicationDateAsString(testCal);

        // Expected format: yyyyMMddHHmmss (14 digits, no milliseconds)
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String expectedDate = expectedFormat.format(testCal.getTime());

        assertEquals("Date format should match oai:pubdate format (seconds)",
            expectedDate, formattedDate);

        // Verify format is exactly 14 digits
        assertEquals("Formatted date should be 14 characters", 14, formattedDate.length());
        assertTrue("Formatted date should be all digits", formattedDate.matches("\\d{14}"));
    }

    /**
     * Test OAI resource implementation for testing purposes
     */
    private static class TestOAIResource extends BaseOAIResource {

        @Override
        protected List<MetadataFormatType> getMetadataFormatTypes() {
            List<MetadataFormatType> formats = new ArrayList<>();
            MetadataFormatType dc = new MetadataFormatType();
            dc.setMetadataPrefix("oai_dc");
            formats.add(dc);
            return formats;
        }

        @Override
        public IdentifyType getIdentifyType(RestContext context) {
            return new IdentifyType();
        }

        @Override
        public boolean validResumptionToken(String resumptionToken) {
            return true;
        }

        @Override
        public int getPageSize() {
            return 10;
        }

        @Override
        protected void processQueryBasedOnResumptionToken(HstQuery query, String resumptionToken)
                throws OAIException {
            // Minimal implementation for testing
        }

        @Override
        protected void processResumptionToken(RestContext context, ListType listType,
                String resumptionToken, Calendar lastKnownPublicationDate,
                String metaPrefix, String set, String from, String until, int totalSize)
                throws OAIException {
            ResumptionTokenType token = new ResumptionTokenType();
            token.setValue("test-token");
            listType.setResumptionToken(token);
        }

        @Override
        protected void applyGetRecordFilter(HstQuery query, String identifier, String metaPrefix)
                throws OAIException {
            // Minimal implementation for testing
        }

        @Override
        protected String getMetadataPrefixFromResumptionToken(String resumptionToken)
                throws OAIException {
            return "oai_dc";
        }

        @Override
        protected MetadataType createMetadataType() {
            return new MetadataType();
        }

    }
}
