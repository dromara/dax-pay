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
import cn.daxpay.open.payment.trade.runtime.bo.RefundCallbackData;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.service.callback.RefundCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/// # 抖音退款回调处理服务
///
/// 抖音退款异步通知 → 主应用接收 → 按 channelMchNo 组装凭证 →
/// 转发到子应用验签解密 → 日志记录退款结果。
///
/// TODO 退款单状态更新待接入退款回调框架(平台目前无独立 RefundCallbackService,
///      后续可通过 RefundService 或新增退款回调入口完成状态流转)。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinRefundCallbackService {

    private final DouyinChannelClient douyinChannelClient;
    private final DouyinDirectKeyConfigService keyConfigService;
    private final DouyinDirectChannelMerchantManager channelMerchantManager;
    private final RefundCallbackService refundCallbackService;

    /// 退款回调处理
    public String refundHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);

        // 2. 组装凭证
        DouyinSdkCredential credential = this.buildCredential(channelMchNo);

        // 3. 转发到子应用验签
        DouyinCallbackParseReq req = new DouyinCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSerial(this.getHeader(headerMap, DouyinPayCode.HEADER_SERIAL));
        req.setNonce(this.getHeader(headerMap, DouyinPayCode.HEADER_NONCE));
        req.setSignature(this.getHeader(headerMap, DouyinPayCode.HEADER_SIGNATURE));
        req.setTimestamp(this.getHeader(headerMap, DouyinPayCode.HEADER_TIMESTAMP));

        DaxResult<DouyinCallbackParseResp> result = douyinChannelClient.parseRefundCallback(req);
        if (result.getCode() != 0) {
            log.error("抖音退款回调验签通道调用失败: {}", result.getMsg());
            return DouyinPayCode.NOTIFY_FAIL;
        }

        DouyinCallbackParseResp resp = result.getData();
        if (resp == null || !resp.isVerified()) {
            log.error("抖音退款回调验签失败: channelMchNo={}", channelMchNo);
            return DouyinPayCode.NOTIFY_FAIL;
        }

        // 4. 构建退款回调数据, 交框架更新退款单状态
        RefundCallbackData callbackData = new RefundCallbackData();
        callbackData.setRefundNo(resp.getOutRefundNo());
        callbackData.setOutRefundNo(resp.getRefundId());
        if (Objects.equals(DouyinPayCode.REFUND_STATUS_SUCCESS, resp.getRefundStatus())) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            callbackData.setTradeErrorMsg("抖音退款状态非成功: " + resp.getRefundStatus());
        }
        refundCallbackService.refundCallback(callbackData);
        return DouyinPayCode.NOTIFY_SUCCESS;
    }

    /// 组装回调验签凭证
    private DouyinSdkCredential buildCredential(String channelMchNo) {
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

    /// 获取 header(大小写兼容)
    private String getHeader(Map<String, String> headerMap, String name) {
        String value = headerMap.get(name);
        return value != null ? value : headerMap.get(name.toLowerCase());
    }
}
