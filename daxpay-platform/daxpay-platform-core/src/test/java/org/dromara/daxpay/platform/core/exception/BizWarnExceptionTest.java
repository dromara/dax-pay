package org.dromara.daxpay.platform.core.exception;

import org.dromara.daxpay.platform.core.code.CommonCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BizWarnExceptionTest {

    @Test
    void twoArgConstructorShouldSetMessageKey() {
        BizWarnException ex = new BizWarnException(CommonCode.FAIL_CODE, "error.common.internalForbidden");
        assertNotNull(ex.getMessageKey());
        assertEquals("error.common.internalForbidden", ex.getMessageKey());
    }
}
