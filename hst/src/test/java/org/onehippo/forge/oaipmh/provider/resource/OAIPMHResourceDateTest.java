package org.onehippo.forge.oaipmh.provider.resource;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.onehippo.forge.oaipmh.provider.api.OAIException;
import org.onehippo.forge.oaipmh.provider.api.RestContext;
import org.onehippo.forge.oaipmh.provider.model.oai.ListRecordsType;
import org.onehippo.forge.oaipmh.provider.model.oai.ResumptionTokenType;

/**
 * Test class to verify FORGE-563 resumption token consistency.
 *
 * Tests that resumption tokens use the same date format as oai:pubdate
 * to ensure consistent pagination during harvesting.
 */
public class OAIPMHResourceDateTest {

    private OAIPMHResource resource;

    @Before
    public void setUp() {
        resource = new OAIPMHResource();
    }

    /**
     * FORGE-563: Test that resumption token format matches oai:pubdate format (seconds)
     */
    @Test
    public void testResumptionTokenFormatSeconds() throws OAIException {
        resource.setUseMilliSecondsDatePrecision(false);

        Calendar testDate = Calendar.getInstance();
        testDate.set(2024, Calendar.JULY, 11, 18, 5, 20);
        testDate.set(Calendar.MILLISECOND, 581);

        ListRecordsType listType = new ListRecordsType();
        resource.processResumptionToken(
            null, // context not needed for this test
            listType,
            null, // no previous token
            testDate,
            "lom",
            null, // no set
            null, // no from
            null  // no until
            , 100 // totalSize
        );

        ResumptionTokenType token = listType.getResumptionToken();
        assertNotNull("Resumption token should be created", token);

        String tokenValue = token.getValue();
        assertNotNull("Resumption token value should not be null", tokenValue);

        // Extract date portion from token: tx20240711180520xmx...
        Pattern pattern = Pattern.compile("tx(\\d{14})xmx.*");
        Matcher matcher = pattern.matcher(tokenValue);
        assertTrue("Token should match expected format", matcher.matches());

        String dateInToken = matcher.group(1);
        assertEquals("Token date should have 14 digits (no milliseconds)", 14, dateInToken.length());
        assertEquals("Token date should start with 20240711", "20240711", dateInToken.substring(0, 8));
    }

    /**
     * FORGE-563: Test that resumption token format matches oai:pubdate format (milliseconds)
     */
    @Test
    public void testResumptionTokenFormatMilliseconds() throws OAIException {
        resource.setUseMilliSecondsDatePrecision(true);

        Calendar testDate = Calendar.getInstance();
        testDate.set(2024, Calendar.JULY, 11, 18, 5, 20);
        testDate.set(Calendar.MILLISECOND, 581);

        ListRecordsType listType = new ListRecordsType();
        resource.processResumptionToken(
            null, // context not needed for this test
            listType,
            null, // no previous token
            testDate,
            "lom",
            null, // no set
            null, // no from
            null  // no until
            , 100 // totalSize
        );

        ResumptionTokenType token = listType.getResumptionToken();
        assertNotNull("Resumption token should be created", token);

        String tokenValue = token.getValue();
        assertNotNull("Resumption token value should not be null", tokenValue);

        // Extract date portion from token: tx20240711180520581xmx...
        Pattern pattern = Pattern.compile("tx(\\d{17})xmx.*");
        Matcher matcher = pattern.matcher(tokenValue);
        assertTrue("Token should match expected format with milliseconds", matcher.matches());

        String dateInToken = matcher.group(1);
        assertEquals("Token date should have 17 digits (with milliseconds)", 17, dateInToken.length());
        assertEquals("Token date should start with 20240711180520", "20240711180520", dateInToken.substring(0, 14));
    }

    /**
     * FORGE-563: Test that date formatter selection is consistent
     */
    @Test
    public void testOaiDateFormatterSelection() {
        // Test seconds precision
        resource.setUseMilliSecondsDatePrecision(false);
        SimpleDateFormat formatter = resource.getOaiDateFormatter();
        assertEquals("Formatter pattern should be for seconds", "yyyyMMddHHmmss", formatter.toPattern());

        // Test milliseconds precision
        resource.setUseMilliSecondsDatePrecision(true);
        SimpleDateFormat formatterMillis = resource.getOaiDateFormatter();
        assertEquals("Formatter pattern should be for milliseconds", "yyyyMMddHHmmssSSS", formatterMillis.toPattern());
    }

    /**
     * FORGE-563: Test resumption token parsing with seconds precision
     */
    @Test
    public void testResumptionTokenParsingSeconds() throws OAIException {
        resource.setUseMilliSecondsDatePrecision(false);

        // Token format: tx{date}xmx{metadataPrefix}xpx{set}frm{from}utl{until}
        // Use "lom" as metadata prefix (matches [a-z0-9] pattern, no underscore)
        String tokenValue = "tx20240711180520xmxlomxpxfrmutl";
        assertTrue("Token should be valid", resource.validResumptionToken(tokenValue));

        Calendar parsedDate = resource.getCalendarFromResumptionToken(tokenValue);
        assertNotNull("Parsed date should not be null", parsedDate);

        // Verify the parsed date matches expected values
        assertEquals("Year should be 2024", 2024, parsedDate.get(Calendar.YEAR));
        assertEquals("Month should be July", Calendar.JULY, parsedDate.get(Calendar.MONTH));
        assertEquals("Day should be 11", 11, parsedDate.get(Calendar.DAY_OF_MONTH));
        assertEquals("Hour should be 18", 18, parsedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Minute should be 5", 5, parsedDate.get(Calendar.MINUTE));
        assertEquals("Second should be 20", 20, parsedDate.get(Calendar.SECOND));
    }

    /**
     * FORGE-563: Test resumption token parsing with milliseconds precision
     */
    @Test
    public void testResumptionTokenParsingMilliseconds() throws OAIException {
        resource.setUseMilliSecondsDatePrecision(true);

        String tokenValue = "tx20240711180520581xmxlomxpxfrmutl";
        assertTrue("Token should be valid", resource.validResumptionToken(tokenValue));

        Calendar parsedDate = resource.getCalendarFromResumptionToken(tokenValue);
        assertNotNull("Parsed date should not be null", parsedDate);

        // Verify the parsed date matches expected values including milliseconds
        assertEquals("Year should be 2024", 2024, parsedDate.get(Calendar.YEAR));
        assertEquals("Month should be July", Calendar.JULY, parsedDate.get(Calendar.MONTH));
        assertEquals("Day should be 11", 11, parsedDate.get(Calendar.DAY_OF_MONTH));
        assertEquals("Hour should be 18", 18, parsedDate.get(Calendar.HOUR_OF_DAY));
        assertEquals("Minute should be 5", 5, parsedDate.get(Calendar.MINUTE));
        assertEquals("Second should be 20", 20, parsedDate.get(Calendar.SECOND));
        assertEquals("Millisecond should be 581", 581, parsedDate.get(Calendar.MILLISECOND));
    }

    /**
     * FORGE-563: Test that token pattern matches precision setting
     */
    @Test
    public void testTokenPatternSelection() {
        // Test seconds pattern
        resource.setUseMilliSecondsDatePrecision(false);
        Pattern patternSeconds = resource.getTokenPattern();
        String testTokenSeconds = "tx20240711180520xmxlomxpxfrmutl";
        assertTrue("Seconds token should match seconds pattern", patternSeconds.matcher(testTokenSeconds).matches());

        // Test milliseconds pattern
        resource.setUseMilliSecondsDatePrecision(true);
        Pattern patternMillis = resource.getTokenPattern();
        String testTokenMillis = "tx20240711180520581xmxlomxpxfrmutl";
        assertTrue("Milliseconds token should match milliseconds pattern", patternMillis.matcher(testTokenMillis).matches());
    }

    /**
     * FORGE-563: Test round-trip date conversion (Calendar -> Token -> Calendar)
     */
    @Test
    public void testDateRoundTripConsistency() throws OAIException {
        // Set millisecond precision before creating or parsing tokens
        resource.setUseMilliSecondsDatePrecision(true);

        // Original date
        Calendar originalDate = Calendar.getInstance();
        originalDate.set(2024, Calendar.DECEMBER, 9, 14, 30, 45);
        originalDate.set(Calendar.MILLISECOND, 123);

        // Create token
        ListRecordsType listType = new ListRecordsType();
        resource.processResumptionToken(null, listType, null, originalDate, "lom", null, null, null, 100);

        String tokenValue = listType.getResumptionToken().getValue();
        assertNotNull("Token should be created", tokenValue);

        // Parse token back to date (must have same precision setting)
        Calendar parsedDate = resource.getCalendarFromResumptionToken(tokenValue);

        // Compare (note: we need to use the formatted strings because Calendar.equals
        // compares all fields including timezone, which may differ)
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String originalFormatted = formatter.format(originalDate.getTime());
        String parsedFormatted = formatter.format(parsedDate.getTime());

        assertEquals("Round-trip date conversion should preserve date precision",
            originalFormatted, parsedFormatted);
    }

    /**
     * FORGE-563: Test metadata prefix extraction from resumption token
     */
    @Test
    public void testMetadataPrefixFromToken() throws OAIException {
        resource.setUseMilliSecondsDatePrecision(false);
        String tokenValue = "tx20240711180520xmxlomxpxfrmutl";
        String metadataPrefix = resource.getMetadataPrefixFromResumptionToken(tokenValue);

        assertEquals("Metadata prefix should be extracted correctly", "lom", metadataPrefix);
    }

    /**
     * FORGE-563: Test set specification extraction from resumption token
     */
    @Test
    public void testSetFromToken() {
        resource.setUseMilliSecondsDatePrecision(false);

        // Token format: tx{date}xmx{metadataPrefix}xpx{set}frm{from}utl{until}
        // When set is present: tx...xmxlomxpxmytestsetfrm...
        String tokenWithSet = "tx20240711180520xmxlomxpxmytestsetfrmutl";
        String setSpec = resource.getSetFromResumptionToken(tokenWithSet);

        assertEquals("Set should be extracted correctly", "mytestset", setSpec);

        // Test token without set (empty between xpx and frm)
        String tokenWithoutSet = "tx20240711180520xmxlomxpxfrmutl";
        String emptySet = resource.getSetFromResumptionToken(tokenWithoutSet);

        assertTrue("Empty set should be empty string or null", emptySet == null || emptySet.isEmpty());
    }

    /**
     * FORGE-563: Test that invalid tokens are rejected
     */
    @Test
    public void testInvalidTokenRejection() {
        resource.setUseMilliSecondsDatePrecision(false);

        String invalidToken = "invalid-token-format";
        assertFalse("Invalid token should not validate", resource.validResumptionToken(invalidToken));

        String missingXmx = "tx20240711180520lomxpxfrmutl";
        assertFalse("Token missing xmx separator should not validate", resource.validResumptionToken(missingXmx));

        String invalidPrefix = "tx20240711180520xmxOAI_DCxpxfrmutl";
        assertFalse("Token with uppercase in prefix should not validate", resource.validResumptionToken(invalidPrefix));
    }

    /**
     * FORGE-563: Test date precision consistency across multiple operations
     */
    @Test
    public void testDatePrecisionConsistency() throws OAIException {
        // Test that switching precision doesn't cause issues
        Calendar testDate = Calendar.getInstance();
        testDate.set(2024, Calendar.JULY, 11, 18, 5, 20);
        testDate.set(Calendar.MILLISECOND, 581);

        // Generate token with seconds precision
        resource.setUseMilliSecondsDatePrecision(false);
        ListRecordsType listType1 = new ListRecordsType();
        resource.processResumptionToken(null, listType1, null, testDate, "oai_dc", null, null, null, 100);
        String tokenSeconds = listType1.getResumptionToken().getValue();

        // Generate token with milliseconds precision
        resource.setUseMilliSecondsDatePrecision(true);
        ListRecordsType listType2 = new ListRecordsType();
        resource.processResumptionToken(null, listType2, null, testDate, "oai_dc", null, null, null, 100);
        String tokenMillis = listType2.getResumptionToken().getValue();

        // Tokens should be different lengths
        assertTrue("Milliseconds token should be longer than seconds token",
            tokenMillis.length() > tokenSeconds.length());

        // Both should start with "tx" and contain "xmx"
        assertTrue("Seconds token should have correct format", tokenSeconds.startsWith("tx") && tokenSeconds.contains("xmx"));
        assertTrue("Milliseconds token should have correct format", tokenMillis.startsWith("tx") && tokenMillis.contains("xmx"));
    }
}
