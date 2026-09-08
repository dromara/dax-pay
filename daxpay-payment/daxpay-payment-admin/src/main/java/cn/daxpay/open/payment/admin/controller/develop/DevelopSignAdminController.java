package cn.daxpay.open.payment.admin.controller.develop;

import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.payment.common.develop.DevelopSignParam;
import cn.daxpay.open.payment.common.develop.DevelopSignResult;
import cn.daxpay.open.payment.common.develop.DevelopSignService;
import cn.daxpay.open.payment.common.develop.DevelopVerifyParam;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// 签名调试(管理)
@PermCode(menuCode = PermCodes.Develop.Sign.MENU)
@Tag(name = "签名调试服务")
@RestController
@RequestMapping("/admin/develop/sign")
@RequiredArgsConstructor
public class DevelopSignAdminController {

    private final DevelopSignService developSignService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "生成签名")
    @PostMapping("/gen")
    public Result<DevelopSignResult> sign(@RequestBody DevelopSignParam param) {
        return Res.ok(developSignService.sign(param));
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "验签")
    @PostMapping("/verify")
    public Result<Boolean> verify(@RequestBody DevelopVerifyParam param) {
        return Res.ok(developSignService.verify(param));
    }
}
