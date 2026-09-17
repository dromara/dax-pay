package cn.daxpay.open.channel.sheng.service.payment;

import cn.daxpay.open.channel.sheng.client.ShengChannelClient;
import cn.daxpay.open.channel.sheng.client.credential.ShengSdkCredential;
import cn.daxpay.open.channel.sheng.client.enums.ShengPayBodyType;
import cn.daxpay.open.channel.sheng.client.enums.ShengPayMethod;
import cn.daxpay.open.channel.sheng.client.req.ShengPayReq;
import cn.daxpay.open.channel.sheng.client.resp.ShengPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 盛付通支付执行业务服务
///
/// 通过 [ShengChannelClient] 调用子应用 dax-pay-channel-two 完成盛付通支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 盛付通聚合通道: 被扫(barcode 族)由子应用分流 authPay, 其余走 unifiedorderOffline,
/// 主应用只透传 payMethod + authCode, 不做接口选型。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShengPayService {

    private final ShengChannelClient shengChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行盛付通支付
    ///
    /// @param order      支付订单(tradeNo 作为商户订单号)
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(密钥配置 + 商户身份)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, ShengSdkCredential credential) {
        // 平台支付方式 → 盛付通支付方式(code 一致, 15 项白名单校验)
        ShengPayMethod payMethod = mapMethod(payParam.getMethod());

        // 构建请求
        ShengPayReq req = new ShengPayReq();
        // 使用支付交易号作为商户订单号透传给盛付通, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setPayMethod(payMethod);
        // 通道专属参数透传(被扫 authCode / jsapi 族 openId+appId 由子应用按需取用)
        req.setAuthCode(payParam.getAuthCode());
        req.setOpenId(payParam.getOpenId());
        req.setAppId(payParam.getChannelAppId());
        // 商品标题(作为盛付通 body 字段): 优先描述, 为空回退标题
        req.setTitle(StrUtil.blankToDefault(payParam.getDescription(), payParam.getTitle()));
        req.setAttach(payParam.getAttach());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setExpireTime(payParam.getExpiredTime());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<ShengPayResp> result = shengChannelClient.pay(req);
        if (result.getCode() != 0) {
            // 盛付通支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.sheng.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成盛付通支付异步通知地址(盛付通 → 平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/sheng/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/sheng/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 盛付通支付方式(15 项白名单, union_h5 一期不接入)
    private static ShengPayMethod mapMethod(String methodCode) {
        ShengPayMethod method = ShengPayMethod.findByCodeOrNull(methodCode);
        if (Objects.isNull(method)) {
            // 盛付通不支持的支付方式(含一期未接入的 union_h5)
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.sheng.unsupportedPayMethod", methodCode);
        }
        return method;
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(ShengPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 ShengPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }

        // 完成时间与金额(被扫同步成功时返回)
        bo.setFinishTime(resp.getFinishTime());
        bo.setTotalAmount(resp.getTotalAmount());
        bo.setRealAmount(resp.getPayerAmount());
        bo.setBuyerPayAmount(resp.getPayerAmount());
        // 用户标识
        bo.setBuyerId(resp.getBuyerId());
        return bo;
    }
}
