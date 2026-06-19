package cn.daxpay.open.platform.core.exception;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/// BizInfoException 两参数构造与国际化 key 的匹配行为测试
class BizInfoExceptionTest {

    @Test
    void twoArgConstructorShouldSetMessageKeyForI18n() {
        BizInfoException ex = new BizInfoException(
                CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                "pay.route.error.noMatch");
        assertEquals("pay.route.error.noMatch", ex.getMessage());
        assertNotNull(ex.getMessageKey());
        assertEquals("pay.route.error.noMatch", ex.getMessageKey());
    }
}
