package cn.daxpay.open.platform.core.exception;

import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PayFailureExceptionTest {

    @Test
    void twoArgConstructorShouldSetMessageKey() {
        PayFailureException ex = new PayFailureException(DaxPayErrorCode.UNCLASSIFIED_ERROR, "pay.error.payFailure");
        assertNotNull(ex.getMessageKey());
        assertEquals("pay.error.payFailure", ex.getMessageKey());
    }
}
