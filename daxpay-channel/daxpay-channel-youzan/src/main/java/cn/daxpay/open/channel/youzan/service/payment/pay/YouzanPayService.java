package cn.daxpay.open.channel.youzan.service.payment.pay;

import cn.daxpay.open.channel.youzan.client.YouzanChannelClient;
import cn.daxpay.open.channel.youzan.client.credential.YouzanSdkCredential;
import cn.daxpay.open.channel.youzan.client.enums.YouzanPayBodyType;
import cn.daxpay.open.channel.youzan.client.enums.YouzanPayMethod;
import cn.daxpay.open.channel.youzan.client.req.YouzanPayReq;
import cn.daxpay.open.channel.youzan.client.resp.YouzanPayResp;
import cn.daxpay.open.channel.youzan.strategy.product.YouzanDirectProductStrategy;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.strategy.ProductStrategySupport;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 有赞支付执行业务服务
///
/// 通过 [YouzanChannelClient] 调用子应用 dax-pay-channel-two 完成有赞(高汇通)统一下单。
/// 有赞聚合通道: 通过 `pay_tool`(ALIPAY_JS/WX_JS/WX_APPLET/WX_H5)区分底层渠道与支付形态,
/// JS/小程序类支付需用户标识(微信 open_id / 支付宝 buyer_id), 微信侧另需 appid 拉起收银台。
@Slf4j
@Service
@RequiredArgsConstructor
public class YouzanPayService {

    private final YouzanChannelClient youzanChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final WxAppFacade wxAppFacade;
    private final YouzanDirectProductStrategy productStrategy;

    /// 执行有赞支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, YouzanSdkCredential credential) {
        // 平台支付方式 → 有赞 pay_tool + 支付内容类型
        MethodMapping mapping = mapMethod(payParam.getMethod());

        // 构建请求
        YouzanPayReq req = new YouzanPayReq();
        // 使用支付交易号作为商户订单号透传给有赞(outer_trade_no), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setMethod(mapping.method());
        req.setPayBodyType(mapping.bodyType());
        // 用户标识(微信 open_id / 支付宝 buyer_id)
        req.setOpenId(payParam.getOpenId());
        // 支付完成返回地址(JS/H5 场景透传给通道)
        req.setReturnUrl(payParam.getReturnUrl());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);
        // 微信 JSAPI/小程序支付需 appid 拉起微信收银台;
        // 通过微信开放应用门面解析商户/平台级微信应用(通道能力绑 → 产品级默认绑 → 平台应用推导)
        if (isWechatJsapiOrMini(payParam.getMethod())) {
            WxAppView wxApp = wxAppFacade.resolve(
                    order.getMchNo(), payParam.getChannelMchNo(),
                    payParam.getCapability(), payParam.getChannelAppId(), ProductEnum.YOUZAN_PAY.getCode());
            req.setWxAppId(wxApp.wxAppId());
        }

        // 调用子应用
        DaxResult<YouzanPayResp> result = youzanChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.youzan.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成有赞支付异步通知地址(有赞→平台, pay_notify_url)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/youzan/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/youzan/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 是否微信 JSAPI/小程序支付(需要 appid 拉起微信收银台;
    /// 判定范围由产品策略 [YouzanDirectProductStrategy#wxAppRequiredCapabilities] 声明)
    private boolean isWechatJsapiOrMini(String methodCode) {
        PayMethodEnum method = PayMethodEnum.findByCode(methodCode);
        return ProductStrategySupport.capabilitiesForMethod(productStrategy, method).stream()
                .anyMatch(productStrategy.wxAppRequiredCapabilities()::contains);
    }

    /// 平台支付方式([PayMethodEnum] code) → 有赞 pay_tool + 支付内容类型
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 支付宝 JS 支付(ext 传 buyer_id, 唤起标识码渲染)
            case ALIPAY_JSAPI -> new MethodMapping(YouzanPayMethod.ALIPAY_JS, YouzanPayBodyType.IDENTIFIER);
            // 微信 JS 支付(公众号, ext 传 open_id + app_id, JSAPI 参数渲染)
            case WECHAT_JSAPI -> new MethodMapping(YouzanPayMethod.WX_JS, YouzanPayBodyType.JSAPI);
            // 微信小程序支付(ext 传 open_id + app_id, JSAPI 参数渲染)
            case WECHAT_MINI -> new MethodMapping(YouzanPayMethod.WX_APPLET, YouzanPayBodyType.JSAPI);
            // 微信 H5 支付(ext 传 type=Wap, 返回 awake_pay_url 跳转链接)
            case WECHAT_H5 -> new MethodMapping(YouzanPayMethod.WX_H5, YouzanPayBodyType.LINK);
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.youzan.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(YouzanPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 有赞订单号(trade_no), 供退款/对账使用
                .setOutOrderNo(resp.getOutOrderNo())
                // JS/H5 类下单恒为异步支付, 下单时不完成
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody())
                .setTotalAmount(resp.getTotalAmount());
        // 支付内容类型映射(子应用 YouzanPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }
        return bo;
    }

    /// 支付方式映射内部封装
    private record MethodMapping(YouzanPayMethod method, YouzanPayBodyType bodyType) {
    }
}
