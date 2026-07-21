package cn.daxpay.open.plugin.easypay.notice;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.protocol.NoticeProtocolSender;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeProtocolEnum;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.enums.EasyPayApiVersionEnum;
import cn.daxpay.open.plugin.easypay.result.api.v1.EasyPayCallbackV1Result;
import cn.daxpay.open.plugin.easypay.result.api.v2.EasyPayCallbackV2Result;
import cn.daxpay.open.plugin.easypay.service.config.EasyPayCredentialService;
import cn.daxpay.open.plugin.easypay.util.EasyPayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
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

/// # 易支付协议出站发送器
///
/// content_mode=ref：content 存 EasyPayOrder.id；发送时实时组装 V1/V2 GET 回调
/// Ack：HTTP 2xx 且 body=SUCCESS（忽略大小写）
@Slf4j
@Component
@RequiredArgsConstructor
public class EasyPayNoticeSender implements NoticeProtocolSender {

    private static final DateTimeFormatter NORM =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));

    private final EasyPayOrderManager easyPayOrderManager;
    private final EasyPayCredentialService easyPayCredentialService;

    @Override
    public String protocol() {
        return NoticeProtocolEnum.EASY_PAY.getCode();
    }

    @Override
    public NoticeSendResult send(MchNoticeTask task) {
        NoticeSendResult result = new NoticeSendResult();
        Long easyPayOrderId = JSONUtil.parseObj(task.getContent()).getLong("id");
        if (easyPayOrderId == null) {
            return result.setSuccess(false).setErrorMsg("easy pay ref missing id");
        }
        EasyPayOrder order = easyPayOrderManager.findByIdNotTenant(easyPayOrderId).orElse(null);
        if (order == null) {
            return result.setSuccess(false).setErrorMsg("easy pay order not found: " + easyPayOrderId);
        }
        if (Objects.equals(order.getApiVersion(), EasyPayApiVersionEnum.V1.getCode())) {
            return sendV1(task, order, result);
        }
        return sendV2(task, order, result);
    }

    private NoticeSendResult sendV1(MchNoticeTask task, EasyPayOrder order, NoticeSendResult result) {
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
        return doGet(task.getUrl(), callback, result);
    }

    private NoticeSendResult sendV2(MchNoticeTask task, EasyPayOrder order, NoticeSendResult result) {
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
        return doGet(task.getUrl(), callback, result);
    }

    private NoticeSendResult doGet(String baseUrl, Object callback, NoticeSendResult result) {
        String body = null;
        Integer httpStatus = null;
        try {
            TreeMap<String, String> map = JacksonUtil.toBean(JacksonUtil.toJson(callback),
                    new TypeReference<TreeMap<String, String>>() {});
            String query = URLUtil.buildQuery(map, StandardCharsets.UTF_8);
            String fullUrl = baseUrl.contains("?") ? baseUrl + "&" + query : baseUrl + "?" + query;
            result.setRequestDigest(StrUtil.sub(fullUrl, 0, 500));
            HttpResponse response = HttpUtil.createGet(fullUrl).timeout(15000).execute();
            httpStatus = response.getStatus();
            body = response.body();
        } catch (Exception e) {
            log.error("易支付通知发送失败, url={}", baseUrl, e);
            return result.setSuccess(false).setHttpStatus(httpStatus).setErrorMsg(e.getMessage());
        }
        result.setHttpStatus(httpStatus);
        boolean ack = httpStatus != null && httpStatus >= 200 && httpStatus < 300
                && StrUtil.equalsIgnoreCase(StrUtil.trim(body), "SUCCESS");
        result.setSuccess(ack);
        if (!ack) {
            result.setErrorMsg(StrUtil.blankToDefault(StrUtil.sub(body, 0, 300), "httpStatus=" + httpStatus));
        }
        return result;
    }
}
