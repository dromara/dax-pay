package cn.daxpay.open.payment.admin.controller.merchant.config;

import cn.daxpay.open.payment.merchant.param.config.MchRiskConfigParam;
import cn.daxpay.open.payment.merchant.result.config.MchRiskConfigResult;
import cn.daxpay.open.payment.merchant.service.config.MchRiskConfigService;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 商户风控配置管理控制器（运营端）
///
/// 本期仅运营端可配置商户级风控（地理围栏），商户端暂不自助修改以防规避。
@PermCode(menuCode = PermCodes.Payment.Risk.MchConfig.MENU)
@Validated
@Tag(name = "商户风控配置管理")
@RestController
@RequestMapping("/admin/merchant/risk-config")
@RequiredArgsConstructor
public class MchRiskConfigAdminController {

    private final MchRiskConfigService mchRiskConfigService;

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "根据商户号查询风控配置")
    @GetMapping("/get-by-mch-no")
    public Result<MchRiskConfigResult> findByMchNo(
            @NotBlank(message = "{validation.field.mchNo.notBlank}") String mchNo) {
        return Res.ok(mchRiskConfigService.findByMchNo(mchNo));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "保存或更新风控配置")
    @PostMapping("/save-or-update")
    public Result<Void> saveOrUpdate(@RequestBody @Validated MchRiskConfigParam param) {
        mchRiskConfigService.saveOrUpdate(param);
        return Res.ok();
    }
}
