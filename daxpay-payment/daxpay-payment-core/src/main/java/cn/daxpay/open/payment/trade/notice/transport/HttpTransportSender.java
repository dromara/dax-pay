package cn.daxpay.open.payment.trade.notice.transport;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.payload.NoticeEnvelope;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeTransportEnum;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/// # HTTP 传输发送器
///
/// 按 [NoticeEnvelope].method 投递 (POST JSON / GET), ACK 规则: HTTP 2xx 且 body trim 后忽略大小写等于 SUCCESS。
/// system 与 easy_pay 两种报文格式共用本发送器
@Slf4j
@Component
public class HttpTransportSender implements NoticeTransportSender {

    @Override
    public String transport() {
        return NoticeTransportEnum.HTTP.getCode();
    }

    @Override
    public NoticeSendResult send(MchNoticeTask task, NoticeEnvelope envelope) {
        NoticeSendResult result = new NoticeSendResult();
        String body = null;
        Integer httpStatus = null;
        try {
            HttpResponse response;
            if ("GET".equalsIgnoreCase(envelope.getMethod())) {
                response = HttpUtil.createGet(envelope.getUrl()).timeout(15000).execute();
            } else {
                response = HttpUtil.createPost(envelope.getUrl())
                        .body(envelope.getBody(), ContentType.JSON.getValue())
                        .timeout(15000)
                        .execute();
            }
            httpStatus = response.getStatus();
            body = response.body();
        } catch (Exception e) {
            log.error("HTTP 通知发送失败, taskId={}, bizNo={}, url={}",
                    task.getId(), task.getBizNo(), task.getUrl(), e);
            result.setRequestDigest(envelope.getRequestDigest());
            return result.setSuccess(false)
                    .setHttpStatus(httpStatus)
                    .setErrorMsg(e.getMessage());
        }
        result.setRequestDigest(envelope.getRequestDigest());
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
