package cn.daxpay.open.channel.douyin.service.callback;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.req.DouyinCallbackParseReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinTransferCallbackParseResp;
import cn.daxpay.open.channel.douyin.code.DouyinPayCode;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectKeyConfigService;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.transfer.runtime.service.TransferCallbackService;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/// # 抖音转账回调处理服务
///
/// 抖音商家转账异步通知 → 主应用接收原始 header + body → 按 channelMchNo 组装凭证 →
/// 转发到子应用(channel-one)用 [com.douyinpay.api.notification.NotificationParser] 验签解密 →
/// 构建 [CallbackData] 交由 [TransferCallbackService] 更新转账单状态。
///
/// 状态映射与同步路径([cn.daxpay.open.channel.douyin.service.payment.transfer.DouyinTransferService])对齐:
/// SUCCESS → success / FAIL → close / 其余中间态(ACCEPTED/TRANSFERING)直接返回成功应答, 不调
/// [TransferCallbackService#transferCallback], 避免被 [TransferCallbackService#doTransferCallback]
/// 对非 success/close 的一律 fail 处理误置失败。
///
/// 抖音转账通知([com.douyinpay.api.transfer.models.TransferPayeeNotification])仅含 order_id(通道转账单号),
/// 不含商户单号 out_bill_no, 故 [CallbackData] 的 tradeNo 留空, 反查走 [TransferCallbackService#resolveTrade]
/// 的 outTransferNo 兜底(findByOutTransferNo)。
///
/// 记录保存注意: [TransferCallbackService#transferCallback] 内部已落回调记录,
/// 故本服务在调用成功后不再重复保存(与 [DouyinPayCallbackService] 在 payCallback 后再 save 的模式不同)。
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinTransferCallbackService {

    /// 转账成功状态
    private static final String STATE_SUCCESS = "SUCCESS";
    /// 转账失败终态
    private static final String STATE_FAIL = "FAIL";
    /// 转账处理中状态(收到此类通知直接返回成功应答, 不流转状态)
    private static final List<String> STATE_PROCESSING = List.of("ACCEPTED", "TRANSFERING");

    private final DouyinChannelClient douyinChannelClient;
    private final DouyinDirectKeyConfigService keyConfigService;
    private final DouyinDirectChannelMerchantManager channelMerchantManager;
    private final TransferCallbackService transferCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 转账回调处理
    ///
    /// @param mchNo        商户号(装载租户上下文)
    /// @param channelMchNo 通道商户号(凭证组装主键)
    /// @param request      原始请求(含抖音验签 header + 加密 body)
    /// @return 抖音要求应答串(SUCCESS/FAIL JSON)
    public String transferHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);

        // 2. 组装凭证(回调验签只需 mchId/serial/privateKey/encryptKey, 不需 douyinAppId)
        DouyinSdkCredential credential = this.buildCredential(channelMchNo);

        // 3. 转发到子应用验签
        DouyinCallbackParseReq req = new DouyinCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSerial(this.getHeader(headerMap, DouyinPayCode.HEADER_SERIAL));
        req.setNonce(this.getHeader(headerMap, DouyinPayCode.HEADER_NONCE));
        req.setSignature(this.getHeader(headerMap, DouyinPayCode.HEADER_SIGNATURE));
        req.setTimestamp(this.getHeader(headerMap, DouyinPayCode.HEADER_TIMESTAMP));

        DaxResult<DouyinTransferCallbackParseResp> result = douyinChannelClient.parseTransferCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("抖音转账回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            Map<String, Object> notify = new HashMap<>();
            notify.put("body", body);
            notify.put("headers", headerMap);
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("抖音转账回调验签失败");
            payCallbackRecordService.saveTransfer(channelMchNo, failData);
            return DouyinPayCode.NOTIFY_FAIL;
        }

        // 4. 解析结果
        DouyinTransferCallbackParseResp resp = result.getData();
        String state = resp.getTransferState();

        // 处理中中间态: 直接返回成功应答, 不调 transferCallback(避免被 doTransferCallback 误置失败)
        if (STATE_PROCESSING.contains(state)) {
            log.info("抖音转账回调中间态, 忽略: transferBillNo={}, state={}", resp.getTransferBillNo(), state);
            return DouyinPayCode.NOTIFY_SUCCESS;
        }

        // 5. 构建回调数据
        CallbackData callbackData = new CallbackData();
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        notify.put("transferBillNo", resp.getTransferBillNo());
        notify.put("transferState", state);
        notify.put("statusDesc", resp.getTransferStatusDesc());
        notify.put("successTime", resp.getSuccessTime());
        callbackData.setCallbackData(notify);
        // 抖音转账通知无商户单号, tradeNo 留空, 反查走 outTransferNo(orderId)兜底
        callbackData.setTradeNo(null);
        callbackData.setOutTradeNo(resp.getTransferBillNo());
        // 完成时间(RFC3339)
        if (StrUtil.isNotBlank(resp.getSuccessTime())) {
            try {
                callbackData.setFinishTime(OffsetDateTime.parse(resp.getSuccessTime()));
            } catch (Exception e) {
                log.warn("抖音转账回调时间解析失败: successTime={}", resp.getSuccessTime());
            }
        }
        // 状态映射(与同步路径对齐)
        if (Objects.equals(state, STATE_SUCCESS)) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else if (Objects.equals(state, STATE_FAIL)) {
            callbackData.setTradeStatus(CallbackStatusEnum.CLOSE.getCode());
            callbackData.setTradeErrorMsg(resp.getTransferStatusDesc());
        } else {
            // 未知状态保持处理中, 直接返回成功应答不流转, 交后续同步轮询确认
            log.warn("抖音转账回调未知状态, 忽略: transferBillNo={}, state={}", resp.getTransferBillNo(), state);
            return DouyinPayCode.NOTIFY_SUCCESS;
        }
        try {
            transferCallbackService.transferCallback(channelMchNo, "douyin", callbackData);
        } catch (Exception e) {
            log.error("抖音转账回调业务处理失败: outTransferNo={}", callbackData.getOutTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveTransfer(channelMchNo, callbackData);
            return DouyinPayCode.NOTIFY_FAIL;
        }
        return DouyinPayCode.NOTIFY_SUCCESS;
    }

    /// 组装回调验签凭证(只需 mchId + 密钥, 不经过 ConfigAssembler 避免依赖 capability)
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
