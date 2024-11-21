package org.dromara.daxpay.service.controller.cashier;

import cn.bootx.platform.core.annotation.IgnoreAuth;
import cn.bootx.platform.core.rest.Res;
import cn.bootx.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.core.result.cashier.CashierCodeResult;
import org.dromara.daxpay.service.service.config.CashierCodeConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Validated
@IgnoreAuth
@Tag(name = "支付码牌配置")
@RestController
@RequestMapping("/cashier/code")
@RequiredArgsConstructor
public class CashierCodeController {
    private final CashierCodeConfigService cashierCodeConfigService;

    @Operation(summary = "根据code和类型查询码牌信息和配置")
    @GetMapping("/findByCashierType")
    public Result<CashierCodeResult> findByCashierType(@NotBlank(message = "码牌code不可为空") String cashierCode,
                                                       @NotBlank(message = "码牌类型不可为空") String cashierType){
        return Res.ok(cashierCodeConfigService.findByCashierType(cashierCode, cashierType));
    }


}
