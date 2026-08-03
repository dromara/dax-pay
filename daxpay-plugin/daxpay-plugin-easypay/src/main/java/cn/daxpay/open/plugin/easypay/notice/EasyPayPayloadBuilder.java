package cn.daxpay.open.plugin.easypay.notice;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.payload.NoticeEnvelope;
import cn.daxpay.open.payment.trade.notice.payload.NoticePayloadBuilder;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeFormatEnum;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.enums.EasyPayApiVersionEnum;
import cn.daxpay.open.plugin.easypay.result.api.v1.EasyPayCallbackV1Result;
import cn.daxpay.open.plugin.easypay.result.api.v2.EasyPayCallbackV2Result;
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
    private final EasyPayCredentialService easyPayCredentialService;

    @Override
    public String format() {
        return NoticeFormatEnum.EASY_PAY.getCode();
    }

    @Override
    public NoticeEnvelope build(MchNoticeTask task) {
        Long easyPayOrderId = JSONUtil.parseObj(task.getContent()).getLong("id");
        if (easyPayOrderId == null) {
            throw new IllegalStateException("easy pay ref missing id");
        }
        EasyPayOrder order = easyPayOrderManager.findByIdNotTenant(easyPayOrderId).orElse(null);
        if (order == null) {
            throw new IllegalStateException("easy pay order not found: " + easyPayOrderId);
        }
        Object callback = Objects.equals(order.getApiVersion(), EasyPayApiVersionEnum.V1.getCode())
                ? buildV1(order) : buildV2(order);
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

    private EasyPayCallbackV1Result buildV1(EasyPayOrder order) {
        var credential = easyPayCredentialService.getAndCheck(order.getPid());
        var callback = new EasyPayCallbackV1Result()
                .setPid(order.getPid())
                .setTradeNo(order.getTradeNo())
                .setOutTradeNo(order.getOutTradeNo())
                .setType(order.getType())
                .setName(order.getName())
                .setMoney(order.getMoney() == null ? null : order.getMoney().toPlainString())
                .setTradeStatus("TRADE_SUCCESS")
                .setParam(order.getParam())
                .setSignType("MD5");
        // 仅一次 MD5 签名（修复商业版重复 setSign）
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
                .setAddTime(order.getAddTime() == null ? null : NORM.format(order.getAddTime().toInstant()))
                .setEndTime(order.getEndTime() == null ? null : NORM.format(order.getEndTime().toInstant()))
                .setName(order.getName())
                .setMoney(order.getMoney() == null ? null : order.getMoney().toPlainString())
                .setParam(order.getParam())
                .setBuyer(order.getBuyer())
                .setTimestamp(String.valueOf(System.currentTimeMillis() / 1000))
                .setSignType("RSA");
        callback.setSign(EasyPayUtil.signByRsa(callback, credential.getPlatformPrivateKey()));
        return callback;
    }
}
