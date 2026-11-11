package cn.daxpay.open.plugin.easypay.notice;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.payload.NoticeEnvelope;
import cn.daxpay.open.payment.trade.notice.payload.NoticePayloadBuilder;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeFormatEnum;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.dao.EasyPayRefundOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.entity.EasyPayRefundOrder;
import cn.daxpay.open.plugin.easypay.enums.EasyPayApiVersionEnum;
import cn.daxpay.open.plugin.easypay.result.api.v1.EasyPayCallbackV1Result;
import cn.daxpay.open.plugin.easypay.result.api.v2.EasyPayCallbackV2Result;
import cn.daxpay.open.plugin.easypay.result.api.v2.EasyPayRefundCallbackV2Result;
import cn.daxpay.open.plugin.easypay.service.config.EasyPayCredentialService;
import cn.daxpay.open.plugin.easypay.util.EasyPayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import tools.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TreeMap;

/// # 易支付协议报文构建器
///
/// content_mode=ref: content 存 EasyPayOrder.id; 构建时实时组装 V1/V2 GET 回调信封 (含签名)。
/// HTTP 投递与 ACK 判定由 [cn.daxpay.open.payment.trade.notice.transport.HttpTransportSender] 统一处理
@Slf4j
@Component
@RequiredArgsConstructor
public class EasyPayPayloadBuilder implements NoticePayloadBuilder {

    private static final DateTimeFormatter NORM =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));

    private final EasyPayOrderManager easyPayOrderManager;
    private final EasyPayRefundOrderManager easyPayRefundOrderManager;
    private final EasyPayCredentialService easyPayCredentialService;

    @Override
    public String format() {
        return NoticeFormatEnum.EASY_PAY.getCode();
    }

    @Override
    public NoticeEnvelope build(MchNoticeTask task) {
        // 退款成功通知: content ref 存退款镜像单 id, 信封由退款单与原支付单组装
        if (Objects.equals(task.getEvent(), NoticeEventEnum.REFUND_SUCCESS.getCode())) {
            return buildRefundV2(task);
        }
        Long easyPayOrderId = JSONUtil.parseObj(task.getContent()).getLong("id");
        if (Objects.isNull(easyPayOrderId)) {
            throw new IllegalStateException("easy pay ref missing id");
        }
        EasyPayOrder order = easyPayOrderManager.findByIdNotTenant(easyPayOrderId).orElse(null);
        if (Objects.isNull(order)) {
            throw new IllegalStateException("easy pay order not found: " + easyPayOrderId);
        }
        Object callback = Objects.equals(order.getApiVersion(), EasyPayApiVersionEnum.V1.getCode())
                ? buildV1(order) : buildV2(order);
        return toGetEnvelope(task, callback);
    }

    /// 组装 GET query 信封: 报文按 key 排序拼接为 query 附到通知地址
    private NoticeEnvelope toGetEnvelope(MchNoticeTask task, Object callback) {
        // 转 TreeMap 拼接 query (按 key 排序)
        TreeMap<String, String> map = JacksonUtil.toBean(JacksonUtil.toJson(callback),
                new TypeReference<TreeMap<String, String>>() {});
        String query = URLUtil.buildQuery(map, StandardCharsets.UTF_8);
        String baseUrl = task.getUrl();
        String fullUrl = baseUrl.contains("?") ? baseUrl + "&" + query : baseUrl + "?" + query;
        return new NoticeEnvelope()
                .setMethod("GET")
                .setUrl(fullUrl)
                .setRequestDigest(StrUtil.sub(fullUrl, 0, 500));
    }

    /// 退款成功通知信封(V2 RSA 签名; V1 无退款接口, 退款镜像单 apiVersion 恒为 v2)
    private NoticeEnvelope buildRefundV2(MchNoticeTask task) {
        Long refundId = JSONUtil.parseObj(task.getContent()).getLong("refundId");
        if (Objects.isNull(refundId)) {
            throw new IllegalStateException("easy pay refund ref missing refundId");
        }
        EasyPayRefundOrder refund = easyPayRefundOrderManager.findByIdNotTenant(refundId)
                .orElseThrow(() -> new IllegalStateException("easy pay refund order not found: " + refundId));
        // 原支付单提供 type/name, 查不到时字段留空不影响签名与投递
        EasyPayOrder order = Objects.isNull(refund.getEasyPayOrderId())
                ? null
                : easyPayOrderManager.findByIdNotTenant(refund.getEasyPayOrderId()).orElse(null);
        var credential = easyPayCredentialService.getAndCheck(refund.getPid());
        var callback = new EasyPayRefundCallbackV2Result()
                .setPid(refund.getPid())
                .setTradeNo(refund.getTradeNo())
                .setOutTradeNo(refund.getOutTradeNo())
                .setRefundNo(refund.getRefundNo())
                .setBizRefundNo(refund.getBizRefundNo())
                .setType(Objects.isNull(order) ? null : order.getType())
                .setName(Objects.isNull(order) ? null : order.getName())
                // 协议金额单位为元, 镜像单存分需转换
                .setMoney(Objects.isNull(refund.getMoney()) ? null : EasyPayUtil.fenToYuanString(refund.getMoney()))
                .setTradeStatus("REFUND_SUCCESS")
                .setAddTime(Objects.isNull(refund.getAddTime()) ? null : NORM.format(refund.getAddTime().toInstant()))
                .setEndTime(Objects.isNull(refund.getEndTime()) ? null : NORM.format(refund.getEndTime().toInstant()))
                .setTimestamp(String.valueOf(System.currentTimeMillis() / 1000))
                .setSignType("RSA");
        callback.setSign(EasyPayUtil.signByRsa(callback, credential.getPlatformPrivateKey()));
        return toGetEnvelope(task, callback);
    }

    private EasyPayCallbackV1Result buildV1(EasyPayOrder order) {
        var credential = easyPayCredentialService.getAndCheck(order.getPid());
        var callback = new EasyPayCallbackV1Result()
                .setPid(order.getPid())
                .setTradeNo(order.getTradeNo())
                .setOutTradeNo(order.getOutTradeNo())
                .setType(order.getType())
                .setName(order.getName())
                .setMoney(Objects.isNull(order.getMoney()) ? null : EasyPayUtil.fenToYuanString(order.getMoney()))
                .setTradeStatus("TRADE_SUCCESS")
                .setParam(order.getParam())
                .setSignType("MD5");
        // 仅一次 MD5 签名（商业版实现会重复 setSign, 勿对齐）
        callback.setSign(EasyPayUtil.signByMd5(callback, credential.getMd5Key()));
        return callback;
    }

    private EasyPayCallbackV2Result buildV2(EasyPayOrder order) {
        var credential = easyPayCredentialService.getAndCheck(order.getPid());
        var callback = new EasyPayCallbackV2Result()
                .setPid(order.getPid())
                .setTradeNo(order.getTradeNo())
                .setOutTradeNo(order.getOutTradeNo())
                .setApiTradeNo(order.getApiTradeNo())
                .setType(order.getType())
                .setTradeStatus("TRADE_SUCCESS")
                .setAddTime(Objects.isNull(order.getAddTime()) ? null : NORM.format(order.getAddTime().toInstant()))
                .setEndTime(Objects.isNull(order.getEndTime()) ? null : NORM.format(order.getEndTime().toInstant()))
                .setName(order.getName())
                .setMoney(Objects.isNull(order.getMoney()) ? null : EasyPayUtil.fenToYuanString(order.getMoney()))
                .setParam(order.getParam())
                .setBuyer(order.getBuyer())
                .setTimestamp(String.valueOf(System.currentTimeMillis() / 1000))
                .setSignType("RSA");
        callback.setSign(EasyPayUtil.signByRsa(callback, credential.getPlatformPrivateKey()));
        return callback;
    }
}
