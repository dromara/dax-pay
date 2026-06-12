package org.dromara.daxpay.platform.core.exception;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.business.UnsupportedAbilityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/// UnsupportedAbilityException messageKey 与占位参数构造测试
class UnsupportedAbilityExceptionTest {

    @Test
    void messageKeyWithArgsShouldSetFieldsForI18n() {
        UnsupportedAbilityException ex = new UnsupportedAbilityException("pay.route.error.routeModeNotExist", "basic");
        assertEquals(DaxPayErrorCode.UNSUPPORTED_ABILITY, ex.getCode());
        assertEquals("pay.route.error.routeModeNotExist", ex.getMessageKey());
        assertArrayEquals(new Object[]{"basic"}, ex.getArgs());
    }
}
