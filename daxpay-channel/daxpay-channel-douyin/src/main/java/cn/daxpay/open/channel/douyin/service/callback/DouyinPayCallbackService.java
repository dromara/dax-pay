package cn.daxpay.open.channel.douyin.service.callback;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.req.DouyinCallbackParseReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinCallbackParseResp;
import cn.daxpay.open.channel.douyin.code.DouyinPayCode;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectKeyConfigService;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

/// # 抖音支付回调处理服务
///
/// 抖音异步通知 → 主应用接收原始 header + body → 按 channelMchNo 组装凭证 →
/// 转发到子应用(channel-one)用 NotificationParser 验签解密 →
/// 构建 [CallbackData] 交由 [PayCallbackService] 更新订单状态。
///
/// 主应用零 SDK 依赖, 验签能力集中在 channel-one。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinPayCallbackService {

    private final DouyinChannelClient douyinChannelClient;
    private final DouyinDirectKeyConfigService keyConfigService;
    private final DouyinDirectChannelMerchantManager channelMerchantManager;
    private final PayCallbackService payCallbackService;

    /// 支付回调处理
    public String payHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);

        // 2. 组装凭证(回调验签只需 mchId/serial/privateKey/encryptKey, 不需 douyinAppId)
        DouyinSdkCredential credential = this.buildCredential(mchNo, channelMchNo);

        // 3. 转发到子应用验签
        DouyinCallbackParseResp resp = this.parsePayCallback(credential, body, headerMap);
        if (resp == null || !resp.isVerified()) {
            log.error("抖音支付回调验签失败: channelMchNo={}", channelMchNo);
            return DouyinPayCode.NOTIFY_FAIL;
        }

        // 4. 构建回调数据交由框架处理
        CallbackData callbackData = this.buildCallbackData(resp);
        try {
            payCallbackService.payCallback(callbackData);
        } catch (Exception e) {
            log.error("抖音支付回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            return DouyinPayCode.NOTIFY_FAIL;
        }
        return DouyinPayCode.NOTIFY_SUCCESS;
    }

    /// 组装回调验签凭证(只需 mchId + 密钥, 不经过 ConfigAssembler 避免依赖 capability)
    private DouyinSdkCredential buildCredential(String mchNo, String channelMchNo) {
        DouyinDirectKeyConfig keyConfig = keyConfigService.findByChannelMchNo(channelMchNo);
        DouyinDirectChannelMerchant merchant = channelMerchantManager.lambdaQuery()
                .eq(DouyinDirectChannelMerchant::getChannelMchNo, channelMchNo)
                .oneOpt()
                .orElse(null);
        var credential = new DouyinSdkCredential();
        credential.setMchId(merchant != null ? merchant.getDyMchId() : null);
        credential.setMerchantSerialNumber(keyConfig.getMerchantSerialNumber());
        credential.setMerchantPrivateKey(keyConfig.getMerchantPrivateKey());
        credential.setEncryptKey(keyConfig.getEncryptKey());
        return credential;
    }

    /// 转发到子应用验签解析
    private DouyinCallbackParseResp parsePayCallback(DouyinSdkCredential credential,
                                                      String body, Map<String, String> headerMap) {
        DouyinCallbackParseReq req = new DouyinCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSerial(this.getHeader(headerMap, DouyinPayCode.HEADER_SERIAL));
        req.setNonce(this.getHeader(headerMap, DouyinPayCode.HEADER_NONCE));
        req.setSignature(this.getHeader(headerMap, DouyinPayCode.HEADER_SIGNATURE));
        req.setTimestamp(this.getHeader(headerMap, DouyinPayCode.HEADER_TIMESTAMP));
        DaxResult<DouyinCallbackParseResp> result = douyinChannelClient.parsePayCallback(req);
        if (result.getCode() != 0) {
            log.error("抖音支付回调验签通道调用失败: {}", result.getMsg());
            return null;
        }
        return result.getData();
    }

    /// 构建框架回调数据
    private CallbackData buildCallbackData(DouyinCallbackParseResp resp) {
        CallbackData data = new CallbackData();
        // resp.outTradeNo 是下单时传入的商户订单号 = 平台 tradeNo
        data.setTradeNo(resp.getOutTradeNo());
        // resp.transactionId 是抖音交易号
        data.setOutTradeNo(resp.getTransactionId());
        // 支付成功时间
        if (resp.getSuccessTime() != null && !resp.getSuccessTime().isBlank()) {
            data.setFinishTime(OffsetDateTime.parse(resp.getSuccessTime()));
        }
        // 交易状态映射: 抖音 SUCCESS → 回调 SUCCESS; 其他 → 非成功(触发 fail 处理)
        if (Objects.equals(DouyinPayCode.TRADE_STATE_SUCCESS, resp.getTradeState())) {
            data.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            data.setTradeStatus(resp.getTradeState());
            data.setCallbackErrorMsg("抖音回调状态非成功: " + resp.getTradeState());
        }
        return data;
    }

    /// 获取 header(大小写兼容)
    private String getHeader(Map<String, String> headerMap, String name) {
        String value = headerMap.get(name);
        return value != null ? value : headerMap.get(name.toLowerCase());
    }
}
