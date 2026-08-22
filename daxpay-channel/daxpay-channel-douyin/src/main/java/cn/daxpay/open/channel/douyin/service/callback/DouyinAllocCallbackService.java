package cn.daxpay.open.channel.douyin.service.callback;

import cn.daxpay.open.channel.douyin.client.DouyinChannelClient;
import cn.daxpay.open.channel.douyin.client.credential.DouyinSdkCredential;
import cn.daxpay.open.channel.douyin.client.req.DouyinCallbackParseReq;
import cn.daxpay.open.channel.douyin.client.resp.DouyinAllocCallbackParseResp;
import cn.daxpay.open.channel.douyin.code.DouyinPayCode;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectChannelMerchantManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectChannelMerchant;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectKeyConfig;
import cn.daxpay.open.channel.douyin.service.direct.DouyinDirectKeyConfigService;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.alloc.bo.AllocResultBo;
import cn.daxpay.open.payment.trade.alloc.enums.AllocDetailResultEnum;
import cn.daxpay.open.payment.trade.alloc.runtime.service.AllocCallbackService;
import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/// # 抖音分账回调服务
///
/// 抖音分账异步通知入口(纯查询式通道, 仅抖音有分账回调)。
/// 链路: 收 body+headers → 组装凭证 → 转发子应用验签 → 状态分类 → 调 [AllocCallbackService] 流转。
///
/// 与 [DouyinTransferCallbackService] 同模式; 差异点:
/// - [AllocCallbackService] 不落回调记录, 本服务在成功/验签失败/业务异常三条路径自行落 [PayCallbackRecordService#saveAlloc]
/// - 分账为多接收方, 回调体含逐明细结果, 需装配 [AllocResultBo.DetailResult] 列表
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinAllocCallbackService {

    /// 分账成功状态
    private static final String STATE_SUCCESS = "SUCCESS";
    /// 分账处理中状态(收到此类通知直接返回成功应答, 不流转状态)
    private static final List<String> STATE_PROCESSING = List.of("PROCESSING");

    private final DouyinChannelClient douyinChannelClient;
    private final DouyinDirectKeyConfigService keyConfigService;
    private final DouyinDirectChannelMerchantManager channelMerchantManager;
    private final AllocCallbackService allocCallbackService;
    private final PayCallbackRecordService payCallbackRecordService;

    /// 抖音分账回调处理
    public String allocHandle(String mchNo, String channelMchNo, HttpServletRequest request) {
        // 提取回调原始数据
        String body = JakartaServletUtil.getBody(request);
        Map<String, String> headerMap = JakartaServletUtil.getHeaderMap(request);

        // 组装凭证(回调验签只需 mchId/serial/privateKey/encryptKey, 不需 douyinAppId)
        DouyinSdkCredential credential = this.buildCredential(channelMchNo);

        // 转发到子应用验签
        DouyinCallbackParseReq req = new DouyinCallbackParseReq();
        req.setCredential(credential);
        req.setBody(body);
        req.setSerial(this.getHeader(headerMap, DouyinPayCode.HEADER_SERIAL));
        req.setNonce(this.getHeader(headerMap, DouyinPayCode.HEADER_NONCE));
        req.setSignature(this.getHeader(headerMap, DouyinPayCode.HEADER_SIGNATURE));
        req.setTimestamp(this.getHeader(headerMap, DouyinPayCode.HEADER_TIMESTAMP));

        DaxResult<DouyinAllocCallbackParseResp> result = douyinChannelClient.parseAllocCallback(req);
        if (result.getCode() != 0 || result.getData() == null || !result.getData().isVerified()) {
            log.error("抖音分账回调验签失败: channelMchNo={}", channelMchNo);
            CallbackData failData = new CallbackData();
            Map<String, Object> notify = new HashMap<>();
            notify.put("body", body);
            notify.put("headers", headerMap);
            failData.setCallbackData(notify);
            failData.setCallbackStatus(CallbackStatusEnum.FAIL);
            failData.setCallbackErrorMsg("抖音分账回调验签失败");
            payCallbackRecordService.saveAlloc(channelMchNo, failData);
            return DouyinPayCode.NOTIFY_FAIL;
        }

        // 解析结果
        DouyinAllocCallbackParseResp resp = result.getData();
        String state = resp.getState();

        // 处理中中间态: 直接返回成功应答, 不调 allocCallback(避免误置失败)
        if (STATE_PROCESSING.contains(state)) {
            log.info("抖音分账回调中间态, 忽略: orderId={}, state={}", resp.getOrderId(), state);
            return DouyinPayCode.NOTIFY_SUCCESS;
        }

        // 构建回调数据
        CallbackData callbackData = new CallbackData();
        Map<String, Object> notify = new HashMap<>();
        notify.put("body", body);
        notify.put("orderId", resp.getOrderId());
        notify.put("allocState", state);
        notify.put("splitFinishTime", resp.getSplitFinishTime());
        callbackData.setCallbackData(notify);
        // 商户分账单号(平台 allocNo, 发起时上送的 out_trade_no)填 tradeNo, 供按 allocNo 主路径定位
        callbackData.setTradeNo(resp.getOutTradeNo());
        // 通道分账单号(抖音 orderId = 平台 outAllocNo)填 outTradeNo, 供容错反查
        callbackData.setOutTradeNo(resp.getOrderId());
        // 完成时间(子应用已解析为 OffsetDateTime)
        callbackData.setFinishTime(resp.getSplitFinishTime());
        // 装配逐明细结果
        List<AllocResultBo.DetailResult> detailResults = new ArrayList<>();
        if (resp.getReceiverResults() != null) {
            for (DouyinAllocCallbackParseResp.ReceiverResult r : resp.getReceiverResults()) {
                detailResults.add(new AllocResultBo.DetailResult()
                        .setReceiverAccount(r.getAccount())
                        .setResult(mapDetailResult(r.getSplitStatus()))
                        .setErrorMsg(r.getFailReason())
                        .setFinishTime(r.getFinishTime()));
            }
        }
        // 状态映射(与同步路径对齐)
        if (Objects.equals(state, STATE_SUCCESS)) {
            callbackData.setTradeStatus(CallbackStatusEnum.SUCCESS.getCode());
        } else {
            // CLOSED/FAIL 及未知状态: 由明细结果聚合(全部 fail → fail, 混合 → partial)
            callbackData.setTradeStatus(CallbackStatusEnum.CLOSE.getCode());
        }
        try {
            allocCallbackService.allocCallback(callbackData, detailResults);
            // AllocCallbackService 不落回调记录, 此处补落(只审计不重放)
            payCallbackRecordService.saveAlloc(channelMchNo, callbackData);
        } catch (Exception e) {
            log.error("抖音分账回调业务处理失败: outAllocNo={}", callbackData.getOutTradeNo(), e);
            callbackData.setCallbackStatus(CallbackStatusEnum.EXCEPTION).setCallbackErrorMsg(e.getMessage());
            payCallbackRecordService.saveAlloc(channelMchNo, callbackData);
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

    /// 映射抖音明细状态(SUCCESS → success / CLOSED|FAIL → fail / 其余 → pending)
    private String mapDetailResult(String state) {
        if (Objects.equals(state, "SUCCESS")) {
            return AllocDetailResultEnum.SUCCESS.getCode();
        } else if (Objects.equals(state, "CLOSED") || Objects.equals(state, "FAIL")) {
            return AllocDetailResultEnum.FAIL.getCode();
        }
        return AllocDetailResultEnum.PENDING.getCode();
    }
}
