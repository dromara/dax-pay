package cn.daxpay.open.payment.trade.notice.payload;

import cn.daxpay.open.payment.common.result.DaxNoticeResult;
import cn.daxpay.open.payment.common.util.JsonSignStrUtil;
import cn.daxpay.open.payment.common.util.PaySignUtil;
import cn.daxpay.open.payment.trade.notice.entity.MchNoticeTask;
import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeFormatEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 标准 DaxPay 签名 JSON 报文构建器
///
/// 组装 [DaxNoticeResult] JSON + 平台私钥 RSA 签名, 产 POST 信封。
/// 对外报文 protocol 字段取自 task.format(值为 system), 保持商户侧契约不变
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemPayloadBuilder implements NoticePayloadBuilder {

    private final PlatformConfigProperties platformConfigProperties;

    @Override
    public String format() {
        return NoticeFormatEnum.SYSTEM.getCode();
    }

    @Override
    public NoticeEnvelope build(MchNoticeTask task) {
        var data = JsonSignStrUtil.buildSortedMap(task.getContent());
        var notice = new DaxNoticeResult<>(CommonCode.SUCCESS_CODE, data, CommonCode.SUCCESS_MSG)
                .setEvent(task.getEvent())
                .setProtocol(task.getFormat())
                .setMchNo(task.getMchNo())
                .setAppId(task.getAppId());
        notice.setResTime(OffsetDateTime.now(ZoneOffset.UTC));
        notice.setReqId(MDC.get(CommonCode.TRACE_ID));
        String privateKey = platformConfigProperties.getKeyConfig().getPrivateKey();
        notice.setSign(PaySignUtil.sign(notice, privateKey));
        String requestJson = JacksonUtil.toJson(notice);
        return new NoticeEnvelope()
                .setMethod("POST")
                .setUrl(task.getUrl())
                .setBody(requestJson)
                .setRequestDigest(StrUtil.sub(requestJson, 0, 500));
    }
}
