package org.onehippo.forge.oaipmh.provider.api;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import jakarta.xml.bind.annotation.XmlAttribute;


import jakarta.xml.ws.WebServiceException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.FilterException;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.jaxrs.services.AbstractResource;
import org.onehippo.forge.oaipmh.provider.model.oai.GetRecordType;
import org.onehippo.forge.oaipmh.provider.model.oai.HeaderType;
import org.onehippo.forge.oaipmh.provider.model.oai.IdentifyType;
import org.onehippo.forge.oaipmh.provider.model.oai.ListIdentifiersType;
import org.onehippo.forge.oaipmh.provider.model.oai.ListMetadataFormatsType;
import org.onehippo.forge.oaipmh.provider.model.oai.ListRecordsType;
import org.onehippo.forge.oaipmh.provider.model.oai.ListSetsType;
import org.onehippo.forge.oaipmh.provider.model.oai.ListType;
import org.onehippo.forge.oaipmh.provider.model.oai.MetadataFormatType;
import org.onehippo.forge.oaipmh.provider.model.oai.MetadataType;
import org.onehippo.forge.oaipmh.provider.model.oai.OAIPMHerrorType;
import org.onehippo.forge.oaipmh.provider.model.oai.OAIPMHerrorcodeType;
import org.onehippo.forge.oaipmh.provider.model.oai.OAIPMHtype;
import org.onehippo.forge.oaipmh.provider.model.oai.RecordType;
import org.onehippo.forge.oaipmh.provider.model.oai.RequestType;
import org.onehippo.forge.oaipmh.provider.model.oai.SetType;
import org.onehippo.forge.oaipmh.provider.model.oai.VerbType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

/**
 * The Open Archives Initiative Protocol for Metadata Harvesting (referred to as the OAI-PMH in the remainder of this
 * document) provides an application-independent interoperability framework based on metadata harvesting
 *
 */
@Produces({MediaType.APPLICATION_XML})
@Path("/oai")
public abstract class BaseOAIResource extends AbstractResource {

    private static Logger log = LoggerFactory.getLogger(BaseOAIResource.class);

    @SuppressWarnings("HippoHstThreadSafeInspection")
    private boolean useMilliSecondsDatePrecision;

    /**
     * registered variables:
     */
    protected static final String PARAM_VERB = "verb";
    protected static final String PARAM_META_PREFIX = "metadataPrefix";
    protected static final String PARAM_IDENTIFIER = "identifier";
    protected static final String PARAM_RESUMPTION_TOKEN = "resumptionToken";
    protected static final String PARAM_SET = "set";
    protected static final String PARAM_FROM = "from";
    protected static final String PARAM_UNTIL = "until";

    /**
     * property names
     */
    protected static final String HIPPOSTDPUBWF_PUBLICATION_DATE = "hippostdpubwf:publicationDate";
    protected static final String OAI_PUBDATE = "oai:pubdate";

    protected static final String OAI_PUBLICATION_DATE_FORMAT = "yyyyMMddHHmmss";
    protected static final String OAI_PUBLICATION_DATE_FORMAT_MILLI = "yyyyMMddHHmmssSSS";

    private static final String BAD_VERB_NO_VERB = "The request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal syntax. No \"verb\" argument found.";
    private static final String BAD_VERB_NOT_LEGAL = "Value of the verb argument is not a legal OAI-PMH verb, the verb argument is missing, or the verb argument is repeated.";

    protected static final String DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    protected static final String SIMPLEFORMAT = "yyyy-MM-dd";
    protected static final SimpleDateFormat SIMPLEFORMATTER = new SimpleDateFormat(SIMPLEFORMAT);
    protected static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATEFORMAT);
    protected static final SimpleDateFormat OAI_DATE_FORMATTER = new SimpleDateFormat(OAI_PUBLICATION_DATE_FORMAT);
    protected static final SimpleDateFormat OAI_DATE_FORMATTER_MILLI = new SimpleDateFormat(OAI_PUBLICATION_DATE_FORMAT_MILLI);

    protected static final Set<String> VERB_VALUES = new ImmutableSet.Builder<String>()
            .add("Identify")
            .add("ListMetadataFormats")
            .add("ListRecords")
            .add("ListIdentifiers")
            .add("GetRecord")
            .add("ListSets")
            .build();

    protected static final Map<String, Class<? extends OAIBean>> SETMAP = new HashMap<String, Class<? extends OAIBean>>();

    protected static final String THE_COMBINATION_OF_THE_VALUES_OF_THE_FROM_UNTIL_SET_AND_METADATA_PREFIX_ARGUMENTS_RESULTS_IN_AN_EMPTY_LIST = "The combination of the values of the from, until, set and metadataPrefix arguments results in an empty list.";
    protected static final String THE_VALUE_OF_THE_RESUMPTION_TOKEN_ARGUMENT_IS_INVALID_OR_EXPIRED = "The value of the resumptionToken argument is invalid or expired.";
    protected static final String THE_METADATA_FORMAT_IDENTIFIED_BY_THE_VALUE_GIVEN_FOR_THE_METADATA_PREFIX_ARGUMENT_IS_NOT_SUPPORTED_BY_THE_ITEM_OR_BY_THE_REPOSITORY = "The metadata format identified by the value given for the metadataPrefix argument is not supported by the item or by the repository.";
    protected static final String THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX_ARGUMENT_S_S_IS_ARE_ILLEGAL = "The request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal syntax. Argument(s) %s is/are illegal.";
    protected static final String THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX_RESUMPTION_TOKEN_ARGUMENT_MAY_ONLY_BE_USED_EXCLUSIVELY = "The request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal syntax. \"resumptionToken\" argument may only be used exclusively.";
    protected static final String THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX_MISSING_ARGUMENT_S_IDENTIFIER_AND_OR_METADATAPREFIX = "The request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal syntax. Missing argument(s) \"identifier\" and or \"metadataprefix\" .";
    protected static final String THE_VALUE_OF_THE_IDENTIFIER_ARGUMENT_IS_UNKNOWN_OR_ILLEGAL_IN_THIS_REPOSITORY = "The value of the identifier argument is unknown or illegal in this repository.";
    protected static final String THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX = "The request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal syntax.";
    protected static final String THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX_FROM_ARGUMENT_MUST_BE_SMALLER_THAN_UNTIL_ARGUMENT = "The request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal syntax. From argument must be smaller than until argument.";
    protected static final String SLASH = "/";

    /**
     * Responsible for delegating the verb into several actions
     *
     * @param request
     * @param response
     * @param uriInfo
     * @param verb
     * @param metaPrefix
     * @param identifier
     * @param resumptionToken
     * @param set
     * @param from
     * @param until
     * @return
     */
    @GET
    @Path("/")
    public OAIPMHtype oaiDelegate(@Context HttpServletRequest request, @Context HttpServletResponse response, @Context UriInfo uriInfo,
                                  @QueryParam(PARAM_VERB) String verb,
                                  @QueryParam(PARAM_META_PREFIX) String metaPrefix,
                                  @QueryParam(PARAM_IDENTIFIER) String identifier,
                                  @QueryParam(PARAM_RESUMPTION_TOKEN) String resumptionToken,
                                  @QueryParam(PARAM_SET) String set,
                                  @QueryParam(PARAM_FROM) String from,
                                  @QueryParam(PARAM_UNTIL) String until
    ) {
        OAIPMHtype oaipmHtype = new OAIPMHtype();
        RestContext context = new RestContext(this, request, response, uriInfo);
        try {
            processRoot(context, oaipmHtype, verb);
            validateVerb(verb);

            switch (VerbType.fromValue(verb)) {
                case IDENTIFY:
                    identify(context, oaipmHtype);
                    break;
                case LIST_RECORDS:
                    listRecords(context, oaipmHtype, metaPrefix, resumptionToken, set, from, until);
                    break;
                case LIST_IDENTIFIERS:
                    listIdentifiers(context, oaipmHtype, metaPrefix, resumptionToken, set, from, until);
                    break;
                case GET_RECORD:
                    getRecord(context, oaipmHtype, metaPrefix, identifier);
                    break;
                case LIST_METADATA_FORMATS:
                    listMetadataFormats(context, oaipmHtype, identifier);
                    break;
                case LIST_SETS:
                    listSets(context, oaipmHtype);
                    break;
            }
        } catch (OAIException e) {
            processError(oaipmHtype, e);
            log.warn("OAI exception {}", e);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }

        return oaipmHtype;
    }

    protected static final Set<String> METADATAFORMATS_ALLOWED = new ImmutableSet.Builder<String>()
            .add("verb")
            .add("identifier")
            .build();

    /**
     * This verb is used to retrieve the metadata formats available from a repository. An optional argument restricts
     * the request to the formats available for a specific item.
     *
     * @param context
     * @param oaipmHtype
     * @param identifier
     * @throws OAIException
     */
    private void listMetadataFormats(final RestContext context, final OAIPMHtype oaipmHtype, final String identifier) throws OAIException {
        processBase(context, METADATAFORMATS_ALLOWED);
        if (StringUtils.isNotEmpty(identifier)) {
            throw new OAIException(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, THE_VALUE_OF_THE_IDENTIFIER_ARGUMENT_IS_UNKNOWN_OR_ILLEGAL_IN_THIS_REPOSITORY);
        }

        final ListMetadataFormatsType formats = new ListMetadataFormatsType();
        final List<MetadataFormatType> metadataFormatTypes = getMetadataFormatTypes();
        if (metadataFormatTypes != null && !metadataFormatTypes.isEmpty()) {
            metadataFormatTypes.forEach(type -> formats.getMetadataFormat().add(type));
        }
        oaipmHtype.setListMetadataFormats(formats);
    }


    protected static final Set<String> GETRECORD_ALLOWED = new ImmutableSet.Builder<String>()
            .add("verb")
            .add("metadataPrefix")
            .add("identifier")
            .build();

    /**
     * This verb is used to retrieve an individual metadata record from a repository. Required arguments specify the
     * identifier of the item from which the record is requested and the format of the metadata that should be included
     * in the record. Depending on the level at which a repository tracks deletions, a header with a "deleted" value for
     * the status attribute may be returned, in case the metadata format specified by the metadataPrefix is no longer
     * available from the repository or from the specified item.
     *
     * @param context
     * @param oaipmHtype
     * @param metaPrefix
     * @param identifier
     * @throws OAIException
     */
    private void getRecord(final RestContext context, final OAIPMHtype oaipmHtype, final String metaPrefix, final String identifier) throws OAIException, QueryException {
        processBase(context, GETRECORD_ALLOWED);
        if (Strings.isNullOrEmpty(metaPrefix) || Strings.isNullOrEmpty(identifier)) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX_MISSING_ARGUMENT_S_IDENTIFIER_AND_OR_METADATAPREFIX);
        }
        validateMetaDataPrefix(metaPrefix);

        HstQuery query = generateHstQuery(context, OAIBean.class, true);
        applyGetRecordFilter(query, identifier, metaPrefix);
        query.setLimit(1); // there should be only one result
        HstQueryResult result = query.execute();
        final int totalSize = result.getTotalSize();

        if (totalSize <= 0) {
            throw new OAIException(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, THE_VALUE_OF_THE_IDENTIFIER_ARGUMENT_IS_UNKNOWN_OR_ILLEGAL_IN_THIS_REPOSITORY);
        }

        final GetRecordType recordType = new GetRecordType();
        oaipmHtype.setGetRecord(recordType);

        HippoBeanIterator hippoBeans = result.getHippoBeans();

        while (hippoBeans.hasNext()) {
            final OAIBean bean = (OAIBean) hippoBeans.nextHippoBean();

            final RecordType record = new RecordType();
            recordType.setRecord(record);

            final String beanIdentifier = populateIdentifier(context, bean, metaPrefix);
            final Calendar publicationDate = bean.getPublicationDate();

            // header
            final HeaderType headerType = new HeaderType();

            headerType.setIdentifier(beanIdentifier);
            headerType.setDatestamp(getDate(publicationDate));

            final List<String> setSpec = headerType.getSetSpec();
            setSpec.add(StringUtils.substringAfter(bean.getClass().getAnnotation(Node.class).jcrType(), ":"));

            record.setHeader(headerType);
            // meta
            final MetadataType meta = createMetadataType();

            // populate meta:
            meta.setAny(populateMetaData(context, bean, metaPrefix));
            record.setMetadata(meta);
        }


    }

    protected abstract MetadataType createMetadataType();


    protected static final Set<String> LISTSETS_ALLOWED = new ImmutableSet.Builder<String>()
            .add("verb")
            .add("resumptionToken")
            .build();

    private void loadListSets(final RestContext context) throws IOException, ClassNotFoundException {
        final List<Class<?>> annotatedClasses = new ArrayList<>();
        includeAnnotatedTopLevelClassesFromBeansPackage(annotatedClasses);
        annotatedClasses.stream().filter(clazz -> OAIBean.class.isAssignableFrom(clazz) && !clazz.equals(OAIBean.class))
                .filter(clazz -> clazz.isAnnotationPresent(Node.class))
                .forEach(clazz -> SETMAP.put(StringUtils.substringAfter(clazz.getAnnotation(Node.class).jcrType(), ":"), (Class<? extends OAIBean>) clazz));
    }

    private void listSets(final RestContext context, final OAIPMHtype oaipmHtype) throws OAIException, IOException, ClassNotFoundException {
        processBase(context, LISTSETS_ALLOWED);
        final ListSetsType listSetsType = new ListSetsType();
        final List<SetType> setTypes = listSetsType.getSet();
        final List<Class<?>> annotatedClasses = new ArrayList<>();
        includeAnnotatedTopLevelClassesFromBeansPackage(annotatedClasses);
        annotatedClasses.stream().filter(clazz -> OAIBean.class.isAssignableFrom(clazz) && !clazz.equals(OAIBean.class))
                .filter(clazz -> clazz.isAnnotationPresent(Node.class))
                .forEach(clazz -> {
                    final SetType setType = new SetType();
                    setType.setSetSpec(StringUtils.substringAfter(clazz.getAnnotation(Node.class).jcrType(), ":"));
                    if (clazz.isAnnotationPresent(OAI.class)) {
                        setType.setSetName(clazz.getAnnotation(OAI.class).setName());
                    } else {
                        setType.setSetName(setType.getSetSpec());
                    }
                    SETMAP.put(setType.getSetSpec(), (Class<? extends OAIBean>) clazz);
                    setTypes.add(setType);
                });
        oaipmHtype.setListSets(listSetsType);
    }

    private String beansPackage;

    public String getBeansPackage() {
        return beansPackage;
    }

    public void setBeansPackage(final String beansPackage) {
        this.beansPackage = beansPackage;
    }

    private void includeAnnotatedTopLevelClassesFromBeansPackage(final List<Class<?>> allClasses)
            throws IOException, ClassNotFoundException {
        if (!Strings.isNullOrEmpty(beansPackage)) {
            final ClassPath classPath = ClassPath.from(getClass().getClassLoader());
            final ImmutableSet<ClassPath.ClassInfo> topLevelClasses = classPath.getTopLevelClassesRecursive(beansPackage);
            for (ClassPath.ClassInfo topLevelClass : topLevelClasses) {
                final String name = topLevelClass.getName();
                final Class<?> clazz = Class.forName(name);
                if (clazz.isAnnotationPresent(OAI.class)) {
                    allClasses.add(clazz);
                }
            }
        }
    }


    protected static final Set<String> LISTRECORDIDENTIFIER_ALLOWED = new ImmutableSet.Builder<String>()
            .add("verb")
            .add("metadataPrefix")
            .add("resumptionToken")
            .add("until")
            .add("from")
            .add("set")
            .build();

    /**
     * This verb is used to harvest records from a repository. Optional arguments permit selective harvesting of records
     * based on set membership and/or datestamp. Depending on the repository's support for deletions, a returned header
     * may have a status attribute of "deleted" if a record matching the arguments specified in the request has been
     * deleted. No metadata will be present for records with deleted status.
     *
     * @param context
     * @param oaipmHtype
     * @param metaPrefix
     * @param resumptionToken
     * @param set
     * @param from
     * @param until
     * @throws OAIException
     * @throws QueryException
     */
    private void listRecords(final RestContext context, final OAIPMHtype oaipmHtype, String metaPrefix, final String resumptionToken, final String set, final String from, final String until) throws OAIException, QueryException, IOException, ClassNotFoundException {
        listRecordsOrIdentifiers(context, oaipmHtype, metaPrefix, resumptionToken, set, from, until, false);
    }

    /**
     * This verb is an abbreviated form of ListRecords, retrieving only headers rather than records. Optional arguments
     * permit selective harvesting of headers based on set membership and/or datestamp. Depending on the repository's
     * support for deletions, a returned header may have a status attribute of "deleted" if a record matching the
     * arguments specified in the request has been deleted.
     *
     * @param context
     * @param oaipmHtype
     * @param metaPrefix
     * @param resumptionToken
     * @param set
     * @param from
     * @param until
     * @throws OAIException
     * @throws QueryException
     */
    private void listIdentifiers(final RestContext context, final OAIPMHtype oaipmHtype, final String metaPrefix, final String resumptionToken, final String set, final String from, final String until) throws OAIException, QueryException, IOException, ClassNotFoundException {
        listRecordsOrIdentifiers(context, oaipmHtype, metaPrefix, resumptionToken, set, from, until, true);
    }

    private void listRecordsOrIdentifiers(final RestContext context, final OAIPMHtype oaipmHtype, String metaPrefix, final String resumptionToken, final String set, String from, String until, final boolean identifiersOnly) throws OAIException, QueryException, IOException, ClassNotFoundException {
        processBase(context, LISTRECORDIDENTIFIER_ALLOWED);
        boolean useResumptionToken = false;
        final HstQuery query;
        if (StringUtils.isNotEmpty(resumptionToken)) {
            if (!isExclusive(resumptionToken, metaPrefix, set)) {
                throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX_RESUMPTION_TOKEN_ARGUMENT_MAY_ONLY_BE_USED_EXCLUSIVELY);
            }
            if (!validResumptionToken(resumptionToken)) {
                throw new OAIException(OAIPMHerrorcodeType.BAD_RESUMPTION_TOKEN, THE_VALUE_OF_THE_RESUMPTION_TOKEN_ARGUMENT_IS_INVALID_OR_EXPIRED);
            }
            useResumptionToken = true;
            metaPrefix = getMetadataPrefixFromResumptionToken(resumptionToken);
        } else if (StringUtils.isEmpty(metaPrefix)) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX);
        }
        validateMetaDataPrefix(metaPrefix);
        if (SETMAP.isEmpty()) {
            loadListSets(context);
        }
        if (StringUtils.isNotEmpty(set)) {
            if (SETMAP.containsKey(set)) {
                query = generateHstQuery(context, SETMAP.get(set));
            } else {
                throw new OAIException(OAIPMHerrorcodeType.NO_RECORDS_MATCH, THE_COMBINATION_OF_THE_VALUES_OF_THE_FROM_UNTIL_SET_AND_METADATA_PREFIX_ARGUMENTS_RESULTS_IN_AN_EMPTY_LIST);
            }
        } else {
            query = generateHstQuery(context, OAIBean.class, true);
        }
        applyCalendarFilter(query, from, until);
        if (useResumptionToken) {
            processQueryBasedOnResumptionToken(query, resumptionToken);
        }
        query.setLimit(getPageSize());
        query.addOrderByDescending(HIPPOSTDPUBWF_PUBLICATION_DATE);
        final HstQueryResult queryResult = query.execute();
        final int totalSize = queryResult.getTotalSize();
        if (totalSize <= 0) {
            throw new OAIException(OAIPMHerrorcodeType.NO_RECORDS_MATCH, THE_COMBINATION_OF_THE_VALUES_OF_THE_FROM_UNTIL_SET_AND_METADATA_PREFIX_ARGUMENTS_RESULTS_IN_AN_EMPTY_LIST);
        }
        if (identifiersOnly) {
            populateListIdentifiers(context, queryResult, oaipmHtype, metaPrefix, resumptionToken, set, from, until);
        } else {
            populateListRecords(context, queryResult, oaipmHtype, metaPrefix, resumptionToken, set, from, until);
        }
    }


    /**
     * Format a calendar object to a dcr:publicationDate string property.
     */
    protected String getPublicationDateAsString(final Calendar calendar) {
        return new SimpleDateFormat(OAI_PUBLICATION_DATE_FORMAT).format(calendar.getTime());
    }

    private void populateListIdentifiers(final RestContext context, final HstQueryResult queryResult, final OAIPMHtype oaipmHtype, final String metaPrefix, final String resumptionToken, final String set, final String from, final String until) throws OAIException {
        final ListIdentifiersType listIdentifiersType = new ListIdentifiersType();
        final List<HeaderType> headers = listIdentifiersType.getHeader();

        final HippoBeanIterator hippoBeans = queryResult.getHippoBeans();

        Calendar lastKnownPublicationDate = null;

        while (hippoBeans.hasNext()) {
            final OAIBean bean = (OAIBean) hippoBeans.nextHippoBean();
            final String identifier = populateIdentifier(context, bean, metaPrefix);
            final Calendar publicationDate = bean.getPublicationDate();

            // header
            final HeaderType headerType = new HeaderType();

            headerType.setIdentifier(identifier);
            headerType.setDatestamp(getDate(publicationDate));

            final List<String> setSpec = headerType.getSetSpec();
            setSpec.add(StringUtils.substringAfter(bean.getClass().getAnnotation(Node.class).jcrType(), ":"));

            headers.add(headerType);
            lastKnownPublicationDate = publicationDate;
        }
        oaipmHtype.setListIdentifiers(listIdentifiersType);

        final int totalSize = queryResult.getTotalSize();
        if (totalSize > getPageSize()) {
            processResumptionToken(context, listIdentifiersType, resumptionToken, lastKnownPublicationDate, metaPrefix, set, from, until, totalSize);
        }
    }

    private void populateListRecords(final RestContext context, final HstQueryResult queryResult, final OAIPMHtype oaipmHtype, final String metaPrefix, final String resumptionToken, final String set, final String from, final String until) throws OAIException {
        final ListRecordsType listRecordType = new ListRecordsType();
        final List<RecordType> records = listRecordType.getRecord();

        final HippoBeanIterator hippoBeans = queryResult.getHippoBeans();

        Calendar lastKnownPublicationDate = null;

        while (hippoBeans.hasNext()) {
            final OAIBean bean = (OAIBean) hippoBeans.nextHippoBean();

            final RecordType record = new RecordType();
            records.add(record);


            final String identifier = populateIdentifier(context, bean, metaPrefix);
            final Calendar publicationDate = bean.getPublicationDate();

            // header
            final HeaderType headerType = new HeaderType();

            headerType.setIdentifier(identifier);
            headerType.setDatestamp(getDate(publicationDate));

            final List<String> setSpec = headerType.getSetSpec();
            setSpec.add(StringUtils.substringAfter(bean.getClass().getAnnotation(Node.class).jcrType(), ":"));

            record.setHeader(headerType);

            // meta
            final MetadataType meta = createMetadataType();

            // populate meta:
            meta.setAny(populateMetaData(context, bean, metaPrefix));
            record.setMetadata(meta);

            lastKnownPublicationDate = publicationDate;
        }
        oaipmHtype.setListRecords(listRecordType);

        final int totalSize = queryResult.getTotalSize();
        if (totalSize > getPageSize()) {
            processResumptionToken(context, listRecordType, resumptionToken, lastKnownPublicationDate, metaPrefix, set, from, until, totalSize);
        }
    }

    /**
     * Important method, should overwrite for specific cases
     *
     * @param context
     * @param bean
     * @param metaPrefix
     * @return
     */
    public String populateIdentifier(final RestContext context, final OAIBean bean, final String metaPrefix) {
        return OAIUtil.getInstance().getIdentifier(bean, context);
    }

    /**
     * Important method, should overwrite for specific cases
     *
     * @param context
     * @param bean
     * @param metaPrefix
     * @return
     */
    public Object populateMetaData(final RestContext context, final OAIBean bean, final String metaPrefix) {
        return OAIUtil.getInstance().delegate(bean, metaPrefix, context);
    }


    protected static final Set<String> IDENTIFY_ALLOWED = new ImmutableSet.Builder<String>()
            .add("verb")
            .build();

    /**
     * This verb is used to retrieve information about a repository. Some of the information returned is required as
     * part of the OAI-PMH. Repositories may also employ the Identify verb to return additional descriptive
     * information.
     *
     * @param context
     * @param root
     * @throws OAIException
     */
    private void identify(final RestContext context, final OAIPMHtype root) throws OAIException {
        processBase(context, IDENTIFY_ALLOWED);
        root.setIdentify(getIdentifyType(context));
    }


    /**
     * ABSTACTS:
     */

    protected abstract List<MetadataFormatType> getMetadataFormatTypes();

    public abstract IdentifyType getIdentifyType(final RestContext context);

    public abstract boolean validResumptionToken(String resumptionToken);

    public abstract int getPageSize();

    protected abstract void processQueryBasedOnResumptionToken(final HstQuery query, final String resumptionToken) throws OAIException;

    protected abstract void processResumptionToken(final RestContext context, final ListType listType, String resumptionToken, final Calendar lastKnownPublicationDate, final String metaPrefix, final String set, final String from, final String until, final int totalSize) throws OAIException;

    protected abstract void applyGetRecordFilter(final HstQuery query, final String identifier, final String metaPrefix) throws OAIException;

    protected abstract String getMetadataPrefixFromResumptionToken(final String resumptionToken) throws OAIException;

    protected void applyCalendarFilter(final HstQuery query, final String from, final String until) throws OAIException, FilterException {
        final Filter filter = getFilter(query);
        final Calendar fromCalendar;
        final Calendar untilCalendar;
        try {
            fromCalendar = getSimpleDate(from);
            untilCalendar = getSimpleDate(until);
        } catch (ParseException e) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX_FROM_ARGUMENT_MUST_BE_SMALLER_THAN_UNTIL_ARGUMENT);
        }
        if (fromCalendar != null && untilCalendar != null) {
            if (fromCalendar.getTimeInMillis() > untilCalendar.getTimeInMillis()) {
                throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX_FROM_ARGUMENT_MUST_BE_SMALLER_THAN_UNTIL_ARGUMENT);
            }
        }
        if (fromCalendar != null) {
            filter.addGreaterOrEqualThan(OAI_PUBDATE, getPublicationDateAsString(fromCalendar));
            query.setFilter(filter);
        }
        if (untilCalendar != null) {
            filter.addLessOrEqualThan(OAI_PUBDATE, getPublicationDateAsString(untilCalendar));
            query.setFilter(filter);
        }
    }

    /**
     * UTILITIES:
     */

    public Calendar getSimpleDate(String input) throws ParseException {
        final Calendar instance = Calendar.getInstance();
        Date parse;
        try {
            parse = FORMATTER.parse(input);
        } catch (ParseException e) {
            parse = SIMPLEFORMATTER.parse(input);
        } catch (Exception e) {
            return null;
        }
        instance.setTime(parse);
        return instance;
    }

    private boolean isExclusive(String value, String... otherValues) {
        if (StringUtils.isEmpty(value)) {
            return false;
        } else {
            for (String otherValue : otherValues) {
                if (StringUtils.isNotEmpty(otherValue)) {
                    return false;
                }
            }
        }
        return true;
    }


    public Filter getFilter(HstQuery query) {
        if (query.getFilter() != null && query.getFilter() instanceof Filter) {
            return (Filter) query.getFilter();
        }
        return query.createFilter();
    }

    private void validateMetaDataPrefix(final String metaPrefix) throws OAIException {
        final List<MetadataFormatType> metadataFormatTypes = getMetadataFormatTypes();
        boolean metaPrefixExists = metadataFormatTypes.stream().anyMatch(type -> type.getMetadataPrefix().equals(metaPrefix));
        if (!metaPrefixExists) {
            throw new OAIException(OAIPMHerrorcodeType.CANNOT_DISSEMINATE_FORMAT, THE_METADATA_FORMAT_IDENTIFIED_BY_THE_VALUE_GIVEN_FOR_THE_METADATA_PREFIX_ARGUMENT_IS_NOT_SUPPORTED_BY_THE_ITEM_OR_BY_THE_REPOSITORY);
        }
    }

    private void processBase(final RestContext context, final Set<String> allowed) throws OAIException {
        final MultivaluedMap<String, String> pathParameters = context.getUriInfo().getQueryParameters();
        List<String> list = pathParameters.keySet().stream().filter(key -> !allowed.contains(key)).collect(Collectors.toList());
        if (!list.isEmpty()) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, String.format(THE_REQUEST_INCLUDES_ILLEGAL_ARGUMENTS_IS_MISSING_REQUIRED_ARGUMENTS_INCLUDES_A_REPEATED_ARGUMENT_OR_VALUES_FOR_ARGUMENTS_HAVE_AN_ILLEGAL_SYNTAX_ARGUMENT_S_S_IS_ARE_ILLEGAL, StringUtils.join(list.toArray(), ",")));
        }
    }

    private void processRoot(final RestContext context, final OAIPMHtype root, final String verb) throws InvocationTargetException, IllegalAccessException {
        setResponseDate(root);
        setResponseType(context, root, verb);
    }

    private void setResponseType(final RestContext context, final OAIPMHtype root, final String verb) throws InvocationTargetException, IllegalAccessException {
        final RequestType value = new RequestType();
        try {
            value.setVerb(VerbType.fromValue(verb));
        } catch (IllegalArgumentException e) {
            //do nothing
        }

        UriInfo uriInfo = context.getUriInfo();
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();

        final Field[] fields = RequestType.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(XmlAttribute.class)) {
                final String name = field.getName();
                if (queryParameters.containsKey(name)) {
                    if (field.getType().equals(String.class)) {
                        BeanUtils.setProperty(value, name, queryParameters.get(name).get(0));
                    } else if (field.getType().equals(VerbType.class)) {
                        try {
                            BeanUtils.setProperty(value, name, VerbType.fromValue(queryParameters.get(name).get(0)));
                        } catch (IllegalArgumentException e) {
                            //do nothing
                        }

                    }
                }
            }
        }
        value.setValue(resolveRequestUrl(context));
        root.setRequest(value);
    }

    /**
     * Validates requests: if action type is not supported, exception will be thrown
     *
     * @param verb
     */
    private void validateVerb(String verb) throws OAIException {
        if (StringUtils.isEmpty(verb)) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_VERB, BAD_VERB_NO_VERB);
        }
        if (!VERB_VALUES.contains(verb)) {
            throw new OAIException(OAIPMHerrorcodeType.BAD_VERB, BAD_VERB_NOT_LEGAL);
        }
    }


    private void setResponseDate(OAIPMHtype root) {
        root.setResponseDate(createToday());
    }


    protected static String getDate(Calendar calendar) {
        return FastDateFormat.getInstance(DATEFORMAT).format(calendar);
    }

    private String createToday() {
        Calendar c = Calendar.getInstance();
        return getDate(c);
    }


    private void processError(final OAIPMHtype root, OAIException e) {
        final OAIPMHerrorType oaipmHerrorType = new OAIPMHerrorType();
        oaipmHerrorType.setCode(e.getType());
        oaipmHerrorType.setValue(e.getMessage());
        root.getError().add(oaipmHerrorType);
    }


    protected String resolveRequestUrl(RestContext context) {
        final Mount mount = context.getHstRequestContext().getResolvedMount().getMount();

        String host = mount.getScheme() + "://" + mount.getVirtualHost().getHostName();

        if (mount.isPortInUrl()) {
            int port = mount.getPort();
            if (port == 0) {
                // the Mount is port agnostic. Take port from current container url
                port = context.getHstRequestContext().getBaseURL().getPortNumber();
            }

            if (port != 80 && port != 443) {
                host += ":" + port;
            }
        }
        if (mount.isContextPathInUrl()) {
            host += mount.getContextPath();
        }
        return host + context.getHstRequestContext().getBaseURL().getRequestPath();
    }

    public HstRequestContext getRequestContext(final HttpServletRequest request) {
        return super.getRequestContext(request);
    }

    /**
     * Subtype Detail method
     *
     * @param context
     * @param includeSubtypes
     * @param clazz
     * @return
     */
    protected final HstQuery generateHstQuery(final RestContext context, final Class<? extends HippoBean> clazz, boolean includeSubtypes) {
        return generateHstQuery(context, includeSubtypes, clazz);
    }

    protected HstQuery generateHstQuery(final RestContext context, final Class<? extends OAIBean> aClass) {
        return generateHstQuery(context, false, aClass);
    }


    private HstQuery generateHstQuery(final RestContext context, boolean includeSubtypes, final Class<? extends HippoBean>... clazz) {
        HstRequestContext requestContext = getRequestContext(context.getRequest());
        javax.jcr.Node scopeNode = getSiteBean(context.getRequest());
        HstQuery query = null;
        try {
            if (clazz.length == 1 && includeSubtypes) {
                query = getHstQueryManager(requestContext).createQuery(scopeNode, clazz[0], includeSubtypes);
            } else {
                query = getHstQueryManager(requestContext).createQuery(scopeNode, clazz);
            }
        } catch (QueryException e) {
            log.error("Error creating HST query", e);
        }
        if (query == null) {
            throw new WebServiceException("Query was null (failed to create it)");
        }
        return query;
    }

    private javax.jcr.Node getSiteBean(final HttpServletRequest request) {

        try {
            HstRequestContext requestContext = getRequestContext(request);
            Mount siteMount = requestContext.getResolvedMount().getMount();
            if (siteMount == null) {
                log.error("Couldn't find site mount for rest service");
                return null;
            }
            String contentPath = siteMount.getContentPath();
            if (contentPath != null) {
                String relativePath = StringUtils.removeStart(contentPath, SLASH);
                return requestContext.getSession().getRootNode().getNode(relativePath);
            }
        } catch (Exception e) {
            log.error("Error fetching siteBean", e);
        }
        return null;

    }

    public boolean isUseMilliSecondsDatePrecision() {
        return useMilliSecondsDatePrecision;
    }

    public void setUseMilliSecondsDatePrecision(final boolean useMilliSecondsDatePrecision) {
        this.useMilliSecondsDatePrecision = useMilliSecondsDatePrecision;
    }
}
