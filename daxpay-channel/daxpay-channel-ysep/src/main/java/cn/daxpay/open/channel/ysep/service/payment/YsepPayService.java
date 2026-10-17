package cn.daxpay.open.channel.ysep.service.payment;

import cn.daxpay.open.channel.ysep.client.YsepChannelClient;
import cn.daxpay.open.channel.ysep.client.credential.YsepSdkCredential;
import cn.daxpay.open.channel.ysep.client.enums.YsepPayBodyType;
import cn.daxpay.open.channel.ysep.client.enums.YsepPayMethod;
import cn.daxpay.open.channel.ysep.client.req.YsepPayReq;
import cn.daxpay.open.channel.ysep.client.resp.YsepPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 银盛支付执行业务服务(两产品共用)
///
/// 通过 [YsepChannelClient] 调用子应用 dax-pay-channel-two 完成银盛支付下单,
/// 按 [ProductEnum] 组装 req.product 分发到开放平台(ysep_pay)/e企通(yseqt_pay)报文族。
/// 请求构建、响应解析全部在本类中完成, 两产品无分支逻辑(报文差异在子应用层表达)。
@Slf4j
@Service
@RequiredArgsConstructor
public class YsepPayService {

    private final YsepChannelClient ysepChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行银盛支付
    ///
    /// @param order      支付订单(tradeNo 作为商户订单号)
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(密钥配置 + 商户身份)
    /// @param product    支付产品(ysep_pay / yseqt_pay)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, YsepSdkCredential credential,
                                ProductEnum product) {
        // 平台支付方式 → 银盛支付方式(9 项白名单校验, 两产品族取值并集)
        YsepPayMethod payMethod = mapMethod(payParam.getMethod());

        // 构建请求
        YsepPayReq req = new YsepPayReq();
        // 使用支付交易号作为商户订单号透传给银盛, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setPayMethod(payMethod);
        req.setProduct(product.getCode());
        // 通道专属参数透传(jsapi 族 openId + appId 由子应用按产品族取用)
        req.setOpenId(payParam.getOpenId());
        req.setAppId(payParam.getChannelAppId());
        // 商品标题: 优先描述, 为空回退标题
        req.setTitle(StrUtil.blankToDefault(payParam.getDescription(), payParam.getTitle()));
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setExpireTime(payParam.getExpiredTime());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<YsepPayResp> result = ysepChannelClient.pay(req);
        if (result.getCode() != 0) {
            // 银盛支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.ysep.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成银盛支付异步通知地址(银盛 → 平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/ysep/pay`
    /// 两产品共用同一回调端点, 子应用按报文形态自动分流验签协议。
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/ysep/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 银盛支付方式(9 项白名单)
    private static YsepPayMethod mapMethod(String methodCode) {
        YsepPayMethod method = YsepPayMethod.findByCodeOrNull(methodCode);
        if (Objects.isNull(method)) {
            // 银盛不支持的支付方式
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.ysep.unsupportedPayMethod", methodCode);
        }
        return method;
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(YsepPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 YsepPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
                case FORM -> bo.setPayBodyType(PayBodyTypeEnum.FROM);
            }
        }

        // 完成时间与金额
        bo.setFinishTime(resp.getFinishTime());
        bo.setTotalAmount(resp.getTotalAmount());
        bo.setBuyerId(resp.getBuyerId());
        return bo;
    }
}
