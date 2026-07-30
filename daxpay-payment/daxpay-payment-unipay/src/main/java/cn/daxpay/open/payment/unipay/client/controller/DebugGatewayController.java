package cn.daxpay.open.payment.unipay.client.controller;

import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.GatewayPayAssistService;
import cn.daxpay.open.payment.unipay.client.param.DebugPrePayParam;
import cn.daxpay.open.payment.unipay.param.gateway.GatewayPrePayParam;
import cn.daxpay.open.payment.unipay.result.gateway.GatewayPrePayResult;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/// # 网关调试接口(仅沙箱环境)
///
/// 为收银台小程序调试中心提供免签名的预下单能力, 便于在沙箱环境联调
/// 收银台 / 聚合支付的完整链路(预下单 → 查支付项 → 认证 → 支付)。
///
/// 安全保障: `@ConditionalOnProperty(sandbox-enabled=true)` 控制整个 Controller 的注册,
/// 生产环境(`daxpay.platform.config.sandbox-enabled=false`)启动时本类不注册, 接口直接 404。
/// 默认 `matchIfMissing=true` 与 [cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties#sandboxEnabled]
/// 字段默认值(true)对齐, 即未显式配置时视为沙箱可用。
///
/// 不加 `@PaymentVerify`, 因此不走商户签名校验; 也不需要登录态, 故加 `@IgnoreAuth`。
@ConditionalOnProperty(prefix = "daxpay.platform.config", name = "sandbox-enabled", havingValue = "true", matchIfMissing = true)
@IgnoreAuth
@Slf4j
@Tag(name = "网关调试接口(仅沙箱)")
@RestController
@RequestMapping("/unipay/gateway/debug")
@RequiredArgsConstructor
public class DebugGatewayController {

    private final GatewayPayAssistService gatewayPayAssistService;

    /// 调试预下单(免签名)
    ///
    /// 将简化的 [DebugPrePayParam] 转为正式 [GatewayPrePayParam] 后复用
    /// [GatewayPayAssistService#prePay] 建容器订单, 返回落地页 URL 与 orderNo。
    /// 商户订单号(bizOrderNo)为空时自动生成 `DEBUG` 前缀单号, 便于联调识别。
    @Operation(summary = "调试预下单(免签名, 仅沙箱)")
    @PostMapping("/pre-pay")
    public Result<GatewayPrePayResult> prePay(@RequestBody @Validated DebugPrePayParam param) {
        GatewayPrePayParam prePayParam = new GatewayPrePayParam();
        prePayParam.setMchNo(param.getMchNo());
        prePayParam.setAppId(param.getAppId());
        // 商户订单号为空时生成调试单号, 便于联调识别
        String bizOrderNo = StrUtil.isBlank(param.getBizOrderNo())
                ? "DEBUG" + System.currentTimeMillis()
                : param.getBizOrderNo();
        prePayParam.setBizOrderNo(bizOrderNo);
        prePayParam.setAmount(param.getAmount());
        prePayParam.setTitle(param.getTitle());
        prePayParam.setGatewayPayType(param.getGatewayPayType());
        log.info("调试预下单: mchNo={}, appId={}, bizOrderNo={}, amount={}, gatewayPayType={}",
                param.getMchNo(), param.getAppId(), bizOrderNo, param.getAmount(), param.getGatewayPayType());
        return Res.ok(gatewayPayAssistService.prePay(prePayParam));
    }
}
