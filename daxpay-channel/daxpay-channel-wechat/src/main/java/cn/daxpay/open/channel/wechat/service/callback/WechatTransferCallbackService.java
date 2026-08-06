package cn.daxpay.open.channel.wechat.service.callback;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.req.WechatCallbackParseReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatTransferCallbackParseResp;
import cn.daxpay.open.channel.wechat.code.WechatCode;
import cn.daxpay.open.channel.wechat.service.direct.WechatDirectConfigAssembler;
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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/// # 微信转账回调处理服务
///
/// 微信商家转账异步通知 → 主应用接收原始 header + body → 组装凭证(直连, 转账无服务商模式) →
/// 转发到子应用用 [com.github.binarywang.wxpay.service.NotificationParser] 验签解密 →
/// 构建 [CallbackData] 交由 [TransferCallbackService] 更新转账单状态。
///
/// 状态映射与同步路径([cn.daxpay.open.channel.wechat.service.payment.transfer.WechatTransferService])对齐:
/// SUCCESS → success / FAIL·CANCELLED → close / 其余中间态直接返回成功应答, 不调 [TransferCallbackService#transferCallback],
/// 避免被 [TransferCallbackService#doTransferCallback] 对非 success/close 的一律 fail 处理误置失败。
///
/// 记录保存注意: [TransferCallbackService#transferCallback] 内部已落回调记录,
/// 故本服务在调用成功后不再重复保存(与 [WechatPayCallbackService] 在 payCallback 后再 save 的模式不同)。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatTransferCallbackService {

    /// 转账成功状态
    private static final String STATE_SUCCESS = "SUCCESS";
    /// 转账失败/撤销终态
    private static final List<String> STATE_FAIL_CLOSE = List.of("FAIL", "CANCELLED");
    /// 转账处理中状态(收到此类通知直接返回成功应答, 不流转状态)
    private static final List<String> STATE_PROCESSING =
            List.of("ACCEPTED", "PROCESSING", "WAIT_USER_CONFIRM", "TRANSFERING", "CANCELING");

    /// 微信转账时间格式(RFC3339, 带毫秒与东八区偏移, 与同步路径一致)
    private static final DateTimeFormatter WECHAT_TRANSFER_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private final WechatChannelClient wechatChannelClient;
    private final WechatDirectConfigAssembler wechatDirectConfigAssembler;
    private final TransferCallbackService transferCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 转账回调处理(直连, 转账无服务商模式)
    ///
    /// @param mchNo        商户号(装载租户上下文, 通道商户归属校验)
    /// @param channelMchNo 通道商户号(凭证组装主键)
    /// @param request      原始请求(含微信验签 header + 加密 body)
    /// @return 微信要求应答串(SUCCESS/FAIL JSON)
    public String transferHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 1. 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);

        // 2. 组装凭证(转账仅直连, 回调不解析应用, 只装载密钥与证书并校验通道商户归属)
        WechatSdkCredential credential = wechatDirectConfigAssembler.buildCallbackConfig(mchNo, channelMchNo);

        // 3. 转发到子应用验签
        WechatCallbackParseReq req = new WechatCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSerial(this.getHeader(headerMap, WechatCode.HEADER_SERIAL));
        req.setNonce(this.getHeader(headerMap, WechatCode.HEADER_NONCE));
        req.setSignature(this.getHeader(headerMap, WechatCode.HEADER_SIGNATURE));
        req.setTimestamp(this.getHeader(headerMap, WechatCode.HEADER_TIMESTAMP));

        DaxResult<WechatTransferCallbackParseResp> result = wechatChannelClient.parseTransferCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("微信转账回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            Map<String, Object> notify = new HashMap<>();
            notify.put("body", body);
            notify.put("headers", headerMap);
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("微信转账回调验签失败");
            payCallbackRecordService.saveTransfer(channelMchNo, failData);
            return WechatCode.NOTIFY_FAIL;
        }

        // 4. 解析结果
        WechatTransferCallbackParseResp resp = result.getData();
        String state = resp.getTransferState();

        // 处理中中间态: 直接返回成功应答, 不调 transferCallback(避免被 doTransferCallback 误置失败)
        if (STATE_PROCESSING.contains(state)) {
            log.info("微信转账回调中间态, 忽略: outBillNo={}, state={}", resp.getOutBillNo(), state);
            return WechatCode.NOTIFY_SUCCESS;
        }

        // 5. 构建回调数据
        CallbackData callbackData = new CallbackData();
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        notify.put("outBillNo", resp.getOutBillNo());
        notify.put("transferBillNo", resp.getTransferBillNo());
        notify.put("transferState", state);
        notify.put("failReason", resp.getFailReason());
        notify.put("updateTime", resp.getUpdateTime());
        callbackData.setCallbackData(notify);
        // 反查字段: outBillNo=平台转账单号 transferNo / transferBillNo=通道转账单号 outTransferNo
        callbackData.setTradeNo(resp.getOutBillNo());
        callbackData.setOutTradeNo(resp.getTransferBillNo());
        // 完成时间(RFC3339 东八区, 与同步路径同 formatter)
        if (StrUtil.isNotBlank(resp.getUpdateTime())) {
            try {
                callbackData.setFinishTime(OffsetDateTime.parse(resp.getUpdateTime(), WECHAT_TRANSFER_TIME_FORMATTER));
            } catch (Exception e) {
                log.warn("微信转账回调时间解析失败: updateTime={}", resp.getUpdateTime());
            }
        }
        // 状态映射(与同步路径对齐)
        if (Objects.equals(state, STATE_SUCCESS)) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else if (STATE_FAIL_CLOSE.contains(state)) {
            callbackData.setTradeStatus(CallbackStatusEnum.CLOSE.getCode());
            callbackData.setTradeErrorMsg(resp.getFailReason());
        } else {
            // 未知状态保持处理中, 直接返回成功应答不流转, 交后续同步轮询确认
            log.warn("微信转账回调未知状态, 忽略: outBillNo={}, state={}", resp.getOutBillNo(), state);
            return WechatCode.NOTIFY_SUCCESS;
        }
        try {
            transferCallbackService.transferCallback(channelMchNo, "wechat", callbackData);
        } catch (Exception e) {
            log.error("微信转账回调业务处理失败: tradeNo={}", callbackData.getTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveTransfer(channelMchNo, callbackData);
            return WechatCode.NOTIFY_FAIL;
        }
        return WechatCode.NOTIFY_SUCCESS;
    }

    /// 获取 header(大小写兼容)
    private String getHeader(Map<String, String> headerMap, String name) {
        String value = headerMap.get(name);
        return value != null ? value : headerMap.get(name.toLowerCase());
    }
}
