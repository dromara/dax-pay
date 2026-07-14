package cn.daxpay.open.platform.core.util;

import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PermCodeUtilTest {

    @Test
    void resolveFullCode_shouldJoinMenuCodeAndActionCode() throws NoSuchMethodException {
        PermCode classPermCode = SampleController.class.getAnnotation(PermCode.class);
        PermCode methodPermCode = SampleController.class.getMethod("page").getAnnotation(PermCode.class);
        assertEquals("payment:merchant:view", PermCodeUtil.resolveFullCode(classPermCode, methodPermCode));
    }

    @Test
    void resolveFullCode_withoutMenuCode_shouldReturnActionCodeOnly() throws NoSuchMethodException {
        PermCode methodPermCode = StandaloneController.class.getMethod("action").getAnnotation(PermCode.class);
        assertEquals("standalone_action", PermCodeUtil.resolveFullCode(null, methodPermCode));
    }

    @PermCode(menuCode = "payment:merchant")
    @RestController
    static class SampleController {

        @PermCode(code = PermCodes.Action.VIEW)
        @GetMapping("/page")
        public void page() {
        }
    }

    @RestController
    static class StandaloneController {

        @PermCode(code = "standalone_action")
        @GetMapping("/action")
        public void action() {
        }
    }

}
