package cn.daxpay.open.payment.trade.runtime.service.pay.gateway;

import cn.daxpay.open.payment.auth.PlatformAuthService;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayAuthUrlParam;
import cn.daxpay.open.payment.unipay.result.assist.AuthUrlResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.ChannelAuthTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 网关 H5 授权服务
///
/// 公开端(无商户签名)根据网关订单生成 OAuth 链接, 用于收银台/聚合页取 openId。
/// 安全约束:
/// - 订单必须存在且可支付([GatewayPayAssistService#getOrderAndCheck])
/// - returnPath 仅允许站内业务相对路径, 防止开放重定向
///
/// 一期使用平台级认证配置(与调试工具同源); 通道级按支付项解析可二期扩展。
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayAuthService {

    private final GatewayPayAssistService gatewayPayAssistService;
    private final PlatformAuthService platformAuthService;

    /// 生成授权链接
    public AuthUrlResult generateAuthUrl(GatewayAuthUrlParam param) {
        // 校验订单可支付并装载商户上下文
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        String returnPath = sanitizeReturnPath(param.getReturnPath(), order.getOrderNo());
        ChannelAuthTypeEnum authType = ChannelAuthTypeEnum.findByCode(param.getAuthType());
        return switch (authType) {
            case WECHAT -> platformAuthService.generateWechatMpAuthUrl(returnPath);
            case ALIPAY -> platformAuthService.generateAlipayAuthUrl(returnPath);
            case DOUYIN -> platformAuthService.generateDouyinAuthUrl(returnPath);
            default -> throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.clientEnvNotSupport");
        };
    }

    /// 校验并规范化 returnPath: 必须是以 / 开头的相对路径, 禁止协议/外链/反斜杠
    private String sanitizeReturnPath(String returnPath, String orderNo) {
        if (StrUtil.isBlank(returnPath)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "validation.field.returnPath.notBlank");
        }
        String path = returnPath.trim();
        // 禁止绝对 URL / 协议相对 / 反斜杠逃逸
        if (!path.startsWith("/")
                || path.startsWith("//")
                || path.contains("://")
                || path.contains("\\")
                || path.contains("@")) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.returnPathInvalid");
        }
        // 仅允许网关业务落地前缀, 且路径中应含本订单号(防跨单串跳)
        boolean allowedPrefix = path.startsWith("/cashier/")
                || path.startsWith("/aggregate/")
                || path.startsWith("/h/");
        if (!allowedPrefix || !path.contains(orderNo)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "pay.error.gateway.returnPathInvalid");
        }
        // 去掉 hash, 保留 query
        int hash = path.indexOf('#');
        if (hash >= 0) {
            path = path.substring(0, hash);
        }
        return path;
    }
}
