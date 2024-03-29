package org.onehippo.forge.oaipmh.provider.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.manager.ObjectConverter;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.query.filter.NodeTypeFilter;
import org.hippoecm.hst.content.beans.query.filter.PrimaryNodeTypeFilterImpl;

import org.hippoecm.hst.core.request.HstRequestContext;

import org.onehippo.forge.oaipmh.provider.api.BaseOAIResource;
import org.onehippo.forge.oaipmh.provider.api.Identify;
import org.onehippo.forge.oaipmh.provider.api.OAIBean;
import org.onehippo.forge.oaipmh.provider.api.OAIException;
import org.onehippo.forge.oaipmh.provider.api.RestContext;
import org.onehippo.forge.oaipmh.provider.model.oai.DeletedRecordType;
import org.onehippo.forge.oaipmh.provider.model.oai.GranularityType;
import org.onehippo.forge.oaipmh.provider.model.oai.IdentifyType;
import org.onehippo.forge.oaipmh.provider.model.oai.ListType;
import org.onehippo.forge.oaipmh.provider.model.oai.MetadataFormatType;
import org.onehippo.forge.oaipmh.provider.model.oai.MetadataType;
import org.onehippo.forge.oaipmh.provider.model.oai.OAIPMHerrorcodeType;
import org.onehippo.forge.oaipmh.provider.model.oai.ResumptionTokenType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource end point for OAI-PMH output
 */
public class OAIPMHResource extends BaseOAIResource {

    private static Logger log = LoggerFactory.getLogger(OAIPMHResource.class);

    @SuppressWarnings("WeakerAccess")
    protected static final String THE_VALUE_OF_THE_RESUMPTION_TOKEN_ARGUMENT_IS_INVALID_OR_EXPIRED = "The value of the resumptionToken argument is invalid or expired.";

    @SuppressWarnings("WeakerAccess")
    protected static final String OAI_IDENTIFIER = "oai:identifier";

    @SuppressWarnings("WeakerAccess")
    protected static final Pattern TOKEN_PATTERN = Pattern.compile("tx([0-9]+)xmx([a-z[0-9]]+)xpx(.*)frm(.*)utl(.*)");

    @SuppressWarnings("WeakerAccess")
    protected static final Pattern TOKEN_PATTERN_MILLI = Pattern.compile("tx([0-9]+)xmx([a-z0-9]+)xpx(.*)frm(.*)utl(.*)");

    private static final String RESUMPTION_TOKEN_FORMAT = "tx%sxmx%sxpx%sfrm%sutl%s";

    @SuppressWarnings("WeakerAccess")
    protected static final String PROTOCOL_VERSION = "2.0";

    @Override
    protected MetadataType createMetadataType() {
        return new MetadataType();
    }

    @Override
    protected List<MetadataFormatType> getMetadataFormatTypes() {
        final List<MetadataFormatType> metaDataFormatType = new ArrayList<>();
        final MetadataFormatType dc = new MetadataFormatType();
        dc.setMetadataPrefix("oai_dc");
        dc.setSchema("http://www.openarchives.org/OAI/2.0/oai_dc.xsd");
        dc.setMetadataNamespace("http://www.openarchives.org/OAI/2.0/oai_dc/");
        metaDataFormatType.add(dc);

        final MetadataFormatType lom = new MetadataFormatType();
        lom.setMetadataPrefix("lom");
        lom.setSchema("http://www.imsglobal.org/xsd/imsmd_v1p2p4.xsd");
        lom.setMetadataNamespace("http://www.imsglobal.org/xsd/imsmd_v1p2");
        metaDataFormatType.add(lom);

        return metaDataFormatType;
    }

    /**
     * Get IdentifyType from a REST context
     */
    @Override
    public IdentifyType getIdentifyType(final RestContext context) {
        final IdentifyType identifyType = new IdentifyType();
        final HstRequestContext hstRequestContext = context.getHstRequestContext();
        final Mount mount = hstRequestContext.getResolvedMount().getMount();
        final String property = mount.getProperty("identify.path");
        if (StringUtils.isNotEmpty(property)) {
            try {
                final Session session = hstRequestContext.getSession();
                if (session.itemExists(property)) {
                    final ObjectConverter objectConverter = getObjectConverter(hstRequestContext);
                    final Identify identify = (Identify) objectConverter.getObject(session, property);
                    identifyType.setRepositoryName(identify.getRepositoryName());
                    identifyType.getAdminEmail().add(identify.getAdminEmail());
                }
            } catch (Exception e) {
                log.error("Exception happened while trying to retrieve identifiable document definition", e);
            }
        }
        identifyType.setBaseURL(resolveRequestUrl(context));
        identifyType.setProtocolVersion(PROTOCOL_VERSION);
        final Calendar earliestDate = Calendar.getInstance();
        earliestDate.setTime(new Date(0));
        identifyType.setEarliestDatestamp(getDate(earliestDate));
        identifyType.setDeletedRecord(DeletedRecordType.NO);
        identifyType.setGranularity(GranularityType.YYYY_MM_DD_THH_MM_SS_Z);
        return identifyType;
    }

    @Override
    public boolean validResumptionToken(final String resumptionToken) {
        return getTokenPattern().matcher(resumptionToken).matches();
    }

    @Override
    public int getPageSize() {
        return 10;
    }

    @Override
    protected void processQueryBasedOnResumptionToken(final HstQuery query, final String resumptionToken) throws OAIException {
        final Calendar calendarFromResumptionToken = getCalendarFromResumptionToken(resumptionToken);
        final Filter filter = getFilter(query);
        try {
            final String from = getFromResumptionTokenOrUseDefaultNull(resumptionToken, 4, null);
            final String until = getFromResumptionTokenOrUseDefaultNull(resumptionToken, 5, null);

            applyCalendarFilter(query, from, until);
            filter.addLessThan(OAI_PUBDATE, getPublicationDateAsString(calendarFromResumptionToken));
        } catch (FilterException e) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN, THE_VALUE_OF_THE_RESUMPTION_TOKEN_ARGUMENT_IS_INVALID_OR_EXPIRED);
        }
        final String setFromResumptionToken = getSetFromResumptionToken(resumptionToken);
        if (StringUtils.isNotEmpty(setFromResumptionToken)) {
            if (!SETMAP.containsKey(setFromResumptionToken)) {
                throw new OAIException(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN, THE_VALUE_OF_THE_RESUMPTION_TOKEN_ARGUMENT_IS_INVALID_OR_EXPIRED);
            }
            final Class<? extends OAIBean> aClass = SETMAP.get(setFromResumptionToken);
            if (aClass.isAnnotationPresent(Node.class)) {
                final String primaryNodeTypeNameForBean = aClass.getAnnotation(Node.class).jcrType();
                NodeTypeFilter primaryNodeTypeFilter = new PrimaryNodeTypeFilterImpl(primaryNodeTypeNameForBean);
                filter.addAndFilter(primaryNodeTypeFilter);
            }
        }
        query.setFilter(filter);
    }

    @Override
    protected void processResumptionToken(final RestContext context, final ListType listType, final String resumptionToken, final Calendar lastKnownPublicationDate, final String metaPrefix, final String set, final String from, final String until, final int totalSize) {
        final ResumptionTokenType resumptionTokenType = new ResumptionTokenType();
        final String time = getOaiDateFormatter().format(lastKnownPublicationDate.getTime());

        String resToken = String.format(RESUMPTION_TOKEN_FORMAT, time, getFromResumptionTokenOrUseDefault(resumptionToken, 2, metaPrefix), getFromResumptionTokenOrUseDefault(resumptionToken, 3, set), getFromResumptionTokenOrUseDefault(resumptionToken, 4, from), getFromResumptionTokenOrUseDefault(resumptionToken, 5, until));
        resumptionTokenType.setValue(resToken);

        listType.setResumptionToken(resumptionTokenType);
    }

    @SuppressWarnings("WeakerAccess")
    protected SimpleDateFormat getOaiDateFormatter() {
        return isUseMilliSecondsDatePrecision()? OAI_DATE_FORMATTER_MILLI : OAI_DATE_FORMATTER;
    }

    @Override
    protected void applyGetRecordFilter(final HstQuery query, final String identifier, final String metaPrefix) throws OAIException {
        if (StringUtils.isEmpty(identifier)) {
            throw new OAIException(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, "The value of the identifier argument is unknown or illegal in this repository.");
        }
        final Filter filter = getFilter(query);
        try {
            //noinspection HippoHstFilterInspection
            filter.addEqualTo(OAI_IDENTIFIER, identifier);
        } catch (FilterException e) {
            log.error("Filter exception happed while trying to retrieve single record by ID:" + identifier, e);
        }
        query.setFilter(filter);
    }

    /**
     * UTILS
     */

    @SuppressWarnings("WeakerAccess")
    public String getFromResumptionTokenOrUseDefault(String resumptionToken, int group, String defaultString) {
        if (StringUtils.isNotEmpty(resumptionToken)) {
            final Matcher matcher = getTokenPattern().matcher(resumptionToken);
            if (matcher.matches()) {
                return matcher.group(group);
            }
        }
        return defaultString == null ? "" : defaultString;
    }

    @SuppressWarnings("WeakerAccess")
    public String getFromResumptionTokenOrUseDefaultNull(String resumptionToken, int group, String defaultString) {
        if (StringUtils.isNotEmpty(resumptionToken)) {
            final Matcher matcher = getTokenPattern().matcher(resumptionToken);
            if (matcher.matches()) {
                return StringUtils.isEmpty(matcher.group(group)) ? defaultString : matcher.group(group);
            }
        }
        return defaultString;
    }


    @SuppressWarnings("WeakerAccess")
    protected Calendar getCalendarFromResumptionToken(final String resumptionToken) throws OAIException {
        final Matcher matcher = getTokenPattern().matcher(resumptionToken);
        if (matcher.matches()) {
            final String pubDateString = matcher.group(1);
            final Calendar calendar = Calendar.getInstance();
            Date parse;
            try {
                parse = getOaiDateFormatter().parse(pubDateString);
            } catch (ParseException e) {
                throw new OAIException(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN, THE_VALUE_OF_THE_RESUMPTION_TOKEN_ARGUMENT_IS_INVALID_OR_EXPIRED);
            }
            calendar.setTime(parse);
            return calendar;

        }
        throw new OAIException(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN, THE_VALUE_OF_THE_RESUMPTION_TOKEN_ARGUMENT_IS_INVALID_OR_EXPIRED);
    }

    @Override
    protected String getMetadataPrefixFromResumptionToken(final String resumptionToken) throws OAIException {
        final Matcher matcher = getTokenPattern().matcher(resumptionToken);
        if (matcher.matches()) {
            // 'metadataPrefix' is second group
            return matcher.group(2);
        }

        throw new OAIException(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN, THE_VALUE_OF_THE_RESUMPTION_TOKEN_ARGUMENT_IS_INVALID_OR_EXPIRED);
    }


    @SuppressWarnings("WeakerAccess")
    protected String getSetFromResumptionToken(final String resumptionToken)  {
        final Matcher matcher = getTokenPattern().matcher(resumptionToken);
        if (matcher.matches()) {
            // 'set' is third group
            return matcher.group(3);
        }

        return null;
    }

    @SuppressWarnings("WeakerAccess")
    protected Pattern getTokenPattern() {
        return isUseMilliSecondsDatePrecision() ? TOKEN_PATTERN_MILLI : TOKEN_PATTERN;
    }

}
