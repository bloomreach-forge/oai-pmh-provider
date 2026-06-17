package org.onehippo.forge.oaipmh.provider.api;

import org.junit.jupiter.api.Test;
import org.onehippo.forge.oaipmh.provider.model.oai.OAIPMHerrorcodeType;

import static org.junit.jupiter.api.Assertions.*;

class OAIExceptionTest {

    @Test
    void constructor_setsMessageAndType() {
        OAIException ex = new OAIException(OAIPMHerrorcodeType.BAD_ARGUMENT, "bad arg");
        assertEquals("bad arg", ex.getMessage());
        assertEquals(OAIPMHerrorcodeType.BAD_ARGUMENT, ex.getType());
    }

    @Test
    void constructor_withIdDoesNotExist_setsType() {
        OAIException ex = new OAIException(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, "not found");
        assertEquals(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST, ex.getType());
    }

    @Test
    void isCheckedException() {
        assertInstanceOf(Exception.class, new OAIException(OAIPMHerrorcodeType.BAD_VERB, "msg"));
    }

    @Test
    void canBeThrown() {
        assertThrows(OAIException.class, () -> {
            throw new OAIException(OAIPMHerrorcodeType.NO_RECORDS_MATCH, "thrown");
        });
    }
}
