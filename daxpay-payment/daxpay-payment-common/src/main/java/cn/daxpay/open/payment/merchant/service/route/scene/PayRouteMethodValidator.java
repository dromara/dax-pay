package cn.daxpay.open.payment.merchant.service.route.scene;

import cn.daxpay.open.payment.masterdata.constants.provider.service.PayProviderMethodService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayProviderEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.payment.merchant.service.route.support.PayRouteI18nHelper;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/// # 通道路由：支付渠道与支付方式校验
///
/// - 有 `provider` 时：`(provider, method)` 须在已启用 DB 目录内（`PayProviderMethodService#contains`）；
/// - 无 `provider` 时：仅允许 other 等无渠道绑定的通用方式行。
///
@Component
@RequiredArgsConstructor
public class PayRouteMethodValidator {

    private final PayProviderMethodService payProviderMethodService;

    /// 校验场景配置项
    ///
    /// - 无 provider：仅允许 other 等无渠道绑定的支付方式；
    /// - 有 provider：支付方式须落在已启用的渠道支付方式目录内。
    public void validateSceneConfigItem(String providerCode, String method) {
        boolean genericMethod = isGenericMethodCode(method);
        if (StrUtil.isBlank(providerCode)) {
            // 非品牌绑定行：只能是跨支付渠道通用支付方式
            if (!genericMethod) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                        "pay.route.error.scenePayProviderRequiredForMethod", PayRouteI18nHelper.payMethod(method));
            }
            return;
        }
        PayProviderEnum provider = PayProviderEnum.findByCode(providerCode);
        if (provider == null) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.route.error.basicProviderInvalid");
        }
        // 品牌绑定行：须在合并且启用的渠道支付方式目录内
        if (!payProviderMethodService.contains(providerCode, method)) {
            // 能力: 支付方式不在渠道目录中
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.payment.capability.methodNotInDirectory",
                    PayRouteI18nHelper.payMethod(method), PayRouteI18nHelper.provider(providerCode));
        }
    }

    /// 是否为跨支付渠道通用支付方式（仅 other）
    private boolean isGenericMethodCode(String methodCode) {
        return Objects.equals(PayMethodEnum.OTHER.getCode(), methodCode);
    }
}