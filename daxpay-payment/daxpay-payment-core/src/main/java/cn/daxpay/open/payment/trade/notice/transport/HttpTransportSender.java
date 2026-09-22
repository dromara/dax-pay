package cn.daxpay.open.payment.trade.notice.transport;

import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.payment.trade.notice.payload.NoticeEnvelope;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeTransportEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Objects;

/// # HTTP 传输发送器
///
/// 复用全局 [RestClient](Apache HC5 连接池 + OTel traceparent 透传, 超时/池参数见
/// `daxpay.platform.common.spring.rest.*`), 按 [NoticeEnvelope].method 投递 (POST JSON / GET)。
/// ACK 规则: HTTP 2xx 且 body trim 后忽略大小写等于 SUCCESS。
/// system 与 easy_pay 两种报文格式共用本发送器
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpTransportSender implements NoticeTransportSender {

    private final RestClient restClient;

    @Override
    public String transport() {
        return NoticeTransportEnum.HTTP.getCode();
    }

    @Override
    public NoticeSendResult send(MchNoticeTask task, NoticeEnvelope envelope) {
        NoticeSendResult result = new NoticeSendResult();
        result.setRequestDigest(envelope.getRequestDigest());
        ExchangeOutcome outcome;
        try {
            // exchange 手动取状态码与响应体, 非 2xx 不抛异常, 统一交给 ACK 判定;
            // URI.create 显式传完整地址, 跳过 RestClient 的 URI 模板解析(避免回调地址中特殊字符被误展开)
            RestClient.RequestHeadersSpec<?> spec;
            if ("GET".equalsIgnoreCase(envelope.getMethod())) {
                spec = restClient.get().uri(URI.create(envelope.getUrl()));
            } else {
                spec = restClient.post().uri(URI.create(envelope.getUrl())).body(envelope.getBody());
            }
            outcome = spec.exchange((req, resp) ->
                    new ExchangeOutcome(resp.getStatusCode().value(), resp.bodyTo(String.class)));
        } catch (Exception e) {
            log.error("HTTP 通知发送失败, taskId={}, bizNo={}, url={}",
                    task.getId(), task.getBizNo(), task.getUrl(), e);
            return result.setSuccess(false)
                    .setErrorMsg(e.getMessage());
        }
        Integer httpStatus = outcome.status();
        String body = outcome.body();
        result.setHttpStatus(httpStatus);
        boolean ack = Objects.nonNull(httpStatus) && httpStatus >= 200 && httpStatus < 300
                && StrUtil.equalsIgnoreCase(StrUtil.trim(body), "SUCCESS");
        result.setSuccess(ack);
        if (!ack) {
            result.setErrorMsg(StrUtil.blankToDefault(StrUtil.sub(body, 0, 300),
                    "httpStatus=" + httpStatus));
        }
        return result;
    }

    /// 一次 HTTP 交换的原始结果(状态码 + 响应体), 供 ACK 判定
    private record ExchangeOutcome(int status, String body) {}
}
