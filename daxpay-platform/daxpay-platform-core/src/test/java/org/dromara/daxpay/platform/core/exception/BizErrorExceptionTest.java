package org.dromara.daxpay.platform.core.exception;

import org.dromara.daxpay.platform.core.code.CommonCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BizErrorExceptionTest {

    @Test
    void twoArgConstructorShouldSetMessageKey() {
        BizErrorException ex = new BizErrorException(CommonCode.FAIL_CODE, "error.artemis.sendFailed");
        assertNotNull(ex.getMessageKey());
        assertEquals("error.artemis.sendFailed", ex.getMessageKey());
    }

    @Test
    void singleArgConstructorShouldSetMessageKey() {
        BizErrorException ex = new BizErrorException("error.artemis.sendFailed");
        assertNotNull(ex.getMessageKey());
        assertEquals("error.artemis.sendFailed", ex.getMessageKey());
    }
}
