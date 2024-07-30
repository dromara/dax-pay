package cn.daxpay.multi.channel.alipay.service.notice;

import cn.bootx.platform.core.util.CertUtil;
import cn.daxpay.multi.channel.alipay.code.AliPayCode;
import cn.daxpay.multi.channel.alipay.entity.config.AliPayConfig;
import cn.daxpay.multi.channel.alipay.service.config.AliPayConfigService;
import cn.daxpay.multi.core.util.PayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * 支付宝通知消息接收
 * @author xxm
 * @since 2024/5/31
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliPayNoticeReceiverService {

    private final AliPayConfigService aliPayConfigService;

    /**
     * 通知消息处理
     */
    public String noticeReceiver(HttpServletRequest request) {
        Map<String, String> map = PayUtil.toMap(request);
        // 首先进行验签
        if (!this.verifyNotify(map)){
            log.error("支付宝消息通知验签失败");
            return "fail";
        }

        // 通过 msg_method 获取消息类型
        String msgMethod = map.get("msg_method");
        // 通过 biz_content 获取值
        String bizContent = map.get("biz_content");
        return null;
    }

    /**
     * 校验消息通知
     */
    private boolean verifyNotify(Map<String, String> params) {
        String callReq = JSONUtil.toJsonStr(params);
        log.info("支付宝消息通知报文: {}", callReq);
        String appId = params.get("app_id");
        if (StrUtil.isBlank(appId)) {
            log.error("支付宝消息通知报文appId为空");
            return false;
        }
        AliPayConfig alipayConfig = aliPayConfigService.getAliPayConfig();
        if (Objects.isNull(alipayConfig)) {
            log.error("支付宝支付配置不存在");
            return false;
        }
        try {
            if (Objects.equals(alipayConfig.getAuthType(), AliPayCode.AUTH_TYPE_KEY)) {
                return AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);
            }
            else {
                return AlipaySignature.verifyV1(params, CertUtil.getCertByContent(alipayConfig.getAlipayCert()), CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验签失败", e);
            return false;
        }
    }
}
