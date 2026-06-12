package org.dromara.daxpay.platform.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DataNotExistExceptionTest {

    @Test
    void messageKeyConstructorShouldSetMessageKey() {
        DataNotExistException ex = new DataNotExistException("error.system.dict.notExist");
        assertNotNull(ex.getMessageKey());
        assertEquals("error.system.dict.notExist", ex.getMessageKey());
    }

    @Test
    void noArgConstructorShouldSetDefaultMessageKey() {
        DataNotExistException ex = new DataNotExistException();
        assertEquals("error.common.dataNotExist", ex.getMessageKey());
    }
}
