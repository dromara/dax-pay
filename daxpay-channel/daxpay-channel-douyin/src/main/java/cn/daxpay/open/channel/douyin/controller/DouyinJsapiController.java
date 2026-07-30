package cn.daxpay.open.channel.douyin.controller;

import cn.daxpay.open.channel.douyin.param.assist.DouyinJsapiConfigParam;
import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.device.qrcode.dao.DeviceQrCodeManager;
import cn.daxpay.open.payment.device.qrcode.entity.DeviceQrCode;
import cn.daxpay.open.payment.douyin.facade.DouyinAppFacade;
import cn.daxpay.open.payment.douyin.facade.DyAppView;
import cn.daxpay.open.payment.merchant.dao.channel.ChannelMerchantManager;
import cn.daxpay.open.payment.merchant.entity.channel.ChannelMerchant;
import cn.daxpay.open.payment.merchant.enums.ClientEnvEnum;
import cn.daxpay.open.payment.merchant.enums.ClientRuntimeEnum;
import cn.daxpay.open.payment.merchant.enums.CodePayFormEnum;
import cn.daxpay.open.payment.merchant.service.gateway.GatewayPayConfigResolveService;
import cn.daxpay.open.payment.route.service.runtime.PayRouteService;
import cn.daxpay.open.payment.trade.enums.GatewayOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.runtime.service.pay.gateway.GatewayPayAssistService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.capability.douyin.auth.result.DouyinJsapiConfigResult;
import cn.daxpay.open.platform.capability.douyin.auth.service.DouyinOpenTokenService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/// # 抖音 H5 JSAPI 调起前置 - sdk.config 验签接口
///
/// 签名基于**商户/平台网站应用**的 clientKey/appSecret 换取的 jsapi_ticket,
/// 与 H5 OAuth([cn.daxpay.open.payment.auth.channel.DouyinAuthStrategy]) 同源,
/// 均通过 [DouyinAppFacade#resolveWebAppForH5Auth] 解析 web_app 应用。
///
/// 上下文三选一: orderNo(网关单) / code(码牌) / channelMchNo(+可选 capability)。
@IgnoreAuth
@Validated
@Tag(name = "抖音 JSAPI 调起辅助")
@RestController
@RequestMapping("/unipay/assist/channel/douyin")
@RequiredArgsConstructor
public class DouyinJsapiController {

    private final DouyinOpenTokenService douyinOpenTokenService;
    private final DouyinAppFacade douyinAppFacade;
    private final ChannelMerchantManager channelMerchantManager;
    private final GatewayPayAssistService gatewayPayAssistService;
    private final DeviceQrCodeManager deviceQrCodeManager;
    private final MerchantContextLoader merchantContextLoader;
    private final GatewayPayConfigResolveService gatewayPayConfigResolveService;
    private final PayRouteService payRouteService;

    @Operation(summary = "获取抖音 JS-SDK sdk.config 验签包")
    @GetMapping("/jsapi-config")
    public Result<DouyinJsapiConfigResult> getJsapiConfig(@Valid DouyinJsapiConfigParam param) {
        ResolvedChannel ctx = resolveChannelContext(
                param.getOrderNo(), param.getCode(),
                param.getChannelMchNo(), param.getCapability(), param.getChannelAppId());
        DyAppView app = douyinAppFacade.resolveWebAppForH5Auth(
                ctx.mchNo(), ctx.channelMchNo(), ctx.channelAppId());
        if (StrUtil.isBlank(app.appSecret())) {
            // 抖音: 直连应用授权密钥未配置
            throw new BizInfoException(CommonErrorCode.SYSTEM_ERROR, "error.channel.douyin.appAuthSecretMissing");
        }
        DouyinJsapiConfigResult result = douyinOpenTokenService.buildJsapiConfig(
                app.douyinAppId(), app.appSecret(), param.getUrl());
        return Res.ok(result);
    }

    private ResolvedChannel resolveChannelContext(String orderNo, String code, String channelMchNo,
                                                  String capability, String channelAppId) {
        if (StrUtil.isNotBlank(channelMchNo)) {
            String mchNo = channelMerchantManager.findByChannelMchNo(channelMchNo)
                    .map(ChannelMerchant::getMchNo)
                    .orElseThrow(() -> new DataNotExistException("error.payment.channel.channelMerchantNotExist"));
            return new ResolvedChannel(mchNo, channelMchNo, capability, channelAppId);
        }
        if (StrUtil.isNotBlank(orderNo)) {
            return resolveFromGatewayOrder(orderNo);
        }
        if (StrUtil.isNotBlank(code)) {
            return resolveFromCodePay(code);
        }
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                "error.channel.douyin.jsapiContextRequired");
    }

    private ResolvedChannel resolveFromGatewayOrder(String orderNo) {
        GatewayPayOrder order = gatewayPayAssistService.getOrderAndCheck(orderNo);
        if (Objects.equals(order.getStatus(), GatewayOrderStatusEnum.PAYING.getCode())
                && StrUtil.isNotBlank(order.getChannelMchNo())) {
            return new ResolvedChannel(order.getMchNo(), order.getChannelMchNo(), order.getCapability(), order.getChannelAppId());
        }
        var resolved = gatewayPayConfigResolveService.resolveRequired(
                order.getAppId(), ClientEnvEnum.DOUYIN, CodePayFormEnum.H5);
        NormalPayParam routeParam = new NormalPayParam();
        routeParam.setMchNo(order.getMchNo());
        routeParam.setAppId(order.getAppId());
        routeParam.setMethod(resolved.method());
        routeParam.setChannelMchNo(resolved.channelMchNo());
        routeParam.setCapability(resolved.capability());
        payRouteService.resolve(routeParam);
        return new ResolvedChannel(order.getMchNo(), routeParam.getChannelMchNo(), routeParam.getCapability(), null);
    }

    private ResolvedChannel resolveFromCodePay(String code) {
        DeviceQrCode entity = deviceQrCodeManager.findByCode(code)
                .orElseThrow(() -> new DataNotExistException("error.device.qrcode.notFound"));
        if (StrUtil.isBlank(entity.getMchNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.device.qrcode.notAssigned");
        }
        merchantContextLoader.initMch(entity.getMchNo());
        var mchApp = merchantContextLoader.resolveApp(entity.getMchNo(), entity.getAppId());
        CodePayFormEnum payForm = CodePayFormEnum.fromProgramType(entity.getProgramType());
        var resolved = gatewayPayConfigResolveService.resolveRequired(mchApp.getAppId(), ClientEnvEnum.DOUYIN, payForm);
        NormalPayParam routeParam = new NormalPayParam();
        routeParam.setMchNo(entity.getMchNo());
        routeParam.setAppId(mchApp.getAppId());
        routeParam.setMethod(resolved.method());
        routeParam.setChannelMchNo(resolved.channelMchNo());
        routeParam.setCapability(resolved.capability());
        payRouteService.resolve(routeParam);
        return new ResolvedChannel(entity.getMchNo(), routeParam.getChannelMchNo(), routeParam.getCapability(), null);
    }

    private record ResolvedChannel(String mchNo, String channelMchNo, String capability, String channelAppId) {
    }
}
