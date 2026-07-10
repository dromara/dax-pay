package cn.daxpay.open.channel.douyin.service.payment.pay;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.enums.DouyinPayBodyType;
import cn.daxpay.open.channel.douyin.client.enums.DouyinPayMethod;
import cn.daxpay.open.channel.douyin.client.req.DouyinPayReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
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

/// # 抖音支付执行业务服务
///
/// 通过 [DouyinChannelClient] 调用子应用 dax-pay-channel-one 完成抖音支付下单。
/// 请求构建、响应解析全部在本类中完成。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinPayService {

    private final DouyinChannelClient douyinChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行抖音支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, DouyinSdkCredential credential) {
        // 构建请求
        DouyinPayReq req = new DouyinPayReq();
        // 使用支付交易号作为商户订单号透传给抖音, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setDescription(payParam.getTitle());
        // 将平台支付方式(PayMethodEnum code)映射为抖音通道支付方式
        req.setMethod(this.mapMethod(payParam.getMethod()));
        // JSAPI 需要传入 openId
        req.setOpenId(payParam.getOpenId());
        req.setClientIp(payParam.getClientIp());
        // 通道通知地址: 始终使用平台生成的回调地址(抖音→平台), 带 channelMchNo 供回调组装凭证验签
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<DouyinPayResp> result = douyinChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.douyin.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成抖音支付异步通知地址(抖音→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/douyin/{channelMchNo}/pay`
    /// 抖音回调 body 加密, 验签前无法解析 out_trade_no, 因此将 channelMchNo 编码到 URL 中,
    /// 回调时直接从 URL 取 channelMchNo 组装凭证验签。
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成抖音回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/douyin/{}/pay",
                base, order.getMchNo(), order.getAppId(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) -> 抖音通道支付方式
    private static DouyinPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case DOUYIN_QR -> DouyinPayMethod.QR;
            case DOUYIN_JSAPI -> DouyinPayMethod.JSAPI;
            case DOUYIN_H5 -> DouyinPayMethod.H5;
            case DOUYIN_APP -> DouyinPayMethod.APP;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.douyin.notSupportMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(DouyinPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 DouyinPayBodyType -> 平台 PayBodyTypeEnum)
        DouyinPayBodyType bodyType = resp.getPayBodyType();
        if (bodyType != null) {
            switch (bodyType) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }
        return bo;
    }
}
