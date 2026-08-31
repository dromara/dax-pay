package cn.daxpay.open.plugin.easypay.controller.appmch;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.merchant.dao.appinfo.MchAppInfoManager;
import cn.daxpay.open.platform.core.annotation.PermCode;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.PermCodes;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.daxpay.open.plugin.easypay.param.config.EasyPayCredentialParam;
import cn.daxpay.open.plugin.easypay.result.config.EasyPayCredentialResult;
import cn.daxpay.open.plugin.easypay.service.config.EasyPayCredentialService;
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

import java.util.Objects;

/// 小程序商户端-易支付凭证配置
///
/// 镜像自 `EasyPayCredentialController`(admin/mch 双路径), 保留小程序商户端易支付配置页
/// 用到的查询与更新端点; 同权限码同 Service。
/// 与运营端镜像 [AppAdminEasyPayCredentialController] 的差异: 商户号取自登录上下文,
/// 并在进入 Service 前校验应用归属当前商户(凭证查询有"不存在则自动创建"副作用, 不可先查后验), 防越权。
@PermCode(menuCode = PermCodes.Merchant.EasyPay.MENU)
@Validated
@Tag(name = "小程序商户端-易支付凭证配置")
@RestController
@RequestMapping("/app-mch/easypay/credential")
@RequiredArgsConstructor
public class AppMerchantEasyPayCredentialController {

    private final EasyPayCredentialService easyPayCredentialService;
    private final MchAppInfoManager mchAppInfoManager;
    private final PaymentContext paymentContext;

    /// 当前登录商户号
    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户上下文缺失
            throw new BizInfoException(CommonCode.FAIL_CODE, "pay.error.assist.mchContextMissing");
        }
        return mchNo;
    }

    /// 校验应用归属当前商户（租户内解析应用商户号, 不一致按应用不存在处理, 不暴露存在性）
    private void assertAppOwned(String appId) {
        String ownerMchNo = mchAppInfoManager.requireMchNoByAppId(appId);
        if (!Objects.equals(ownerMchNo, this.requireMchNo())) {
            // 商户: 未找到指定应用的配置
            throw new ConfigNotExistException("error.payment.merchant.specifiedAppConfigNotFound");
        }
    }

    @PermCode(code = PermCodes.Action.VIEW)
    @Operation(summary = "按应用号查询凭证")
    @GetMapping("/get-by-app-id")
    public Result<EasyPayCredentialResult> getByAppId(
            @NotBlank(message = "{validation.field.appId.notBlank}") String appId) {
        this.assertAppOwned(appId);
        return Res.ok(easyPayCredentialService.findResultByAppId(appId));
    }

    @PermCode(code = PermCodes.Action.MANAGE)
    @Operation(summary = "更新凭证")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody @Validated EasyPayCredentialParam param) {
        this.assertAppOwned(param.getAppId());
        easyPayCredentialService.update(param);
        return Res.ok();
    }
}
