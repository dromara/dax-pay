package cn.daxpay.open.payment.trade.notice.protocol;

import cn.daxpay.open.payment.common.result.DaxNoticeResult;
import cn.daxpay.open.payment.common.util.JsonSignStrUtil;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeProtocolEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 标准 DaxPay 签名 JSON 出站发送器
///
/// Ack 规则：HTTP 2xx 且 body trim 后忽略大小写等于 SUCCESS
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemHttpSignedSender implements NoticeProtocolSender {

    private final PlatformConfigProperties platformConfigProperties;

    @Override
    public String protocol() {
        return NoticeProtocolEnum.SYSTEM.getCode();
    }

    @Override
    public NoticeSendResult send(MchNoticeTask task) {
        NoticeSendResult result = new NoticeSendResult();
        String body = null;
        Integer httpStatus = null;
        try {
            var data = JsonSignStrUtil.buildSortedMap(task.getContent());
            var notice = new DaxNoticeResult<>(CommonCode.SUCCESS_CODE, data, CommonCode.SUCCESS_MSG)
                    .setEvent(task.getEvent())
                    .setProtocol(task.getProtocol())
                    .setMchNo(task.getMchNo())
                    .setAppId(task.getAppId());
            notice.setResTime(OffsetDateTime.now(ZoneOffset.UTC));
            notice.setReqId(MDC.get(CommonCode.TRACE_ID));
            String privateKey = platformConfigProperties.getKeyConfig().getPrivateKey();
            notice.setSign(PaySignUtil.sign(notice, privateKey));
            String requestJson = JacksonUtil.toJson(notice);
            result.setRequestDigest(StrUtil.sub(requestJson, 0, 500));
            HttpResponse response = HttpUtil.createPost(task.getUrl())
                    .body(requestJson, ContentType.JSON.getValue())
                    .timeout(15000)
                    .execute();
            httpStatus = response.getStatus();
            body = response.body();
        } catch (Exception e) {
            log.error("系统协议通知发送失败, taskId={}, bizNo={}", task.getId(), task.getBizNo(), e);
            result.setSuccess(false)
                    .setHttpStatus(httpStatus)
                    .setErrorMsg(e.getMessage());
            return result;
        }
        result.setHttpStatus(httpStatus);
        boolean ack = httpStatus != null && httpStatus >= 200 && httpStatus < 300
                && StrUtil.equalsIgnoreCase(StrUtil.trim(body), "SUCCESS");
        result.setSuccess(ack);
        if (!ack) {
            result.setErrorMsg(StrUtil.blankToDefault(StrUtil.sub(body, 0, 300),
                    "httpStatus=" + httpStatus));
        }
        return result;
    }
}
