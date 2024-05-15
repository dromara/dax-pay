package cn.bootx.platform.starter.wechat.core.portal.service;

import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Service;

/**
 * 微信门户处理
 *
 * @author xxm
 * @since 2022/7/16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatPortalService {

    private final WxMpService wxMpService;

    private final WxMpMessageRouter wxMpMessageRouter;

    /**
     * 微信接入校验处理
     * @param signature 微信签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     */
    public String auth(String signature, String timestamp, String nonce, String echostr) {
        log.info("接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
        if (StrUtil.isAllBlank(signature, timestamp, nonce, echostr)) {
            throw new ValidationFailedException();
        }

        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    /**
     * 微信消息处理
     * @param requestBody 请求报文体
     * @param signature 微信签名
     * @param encType 加签方式
     * @param msgSignature 微信签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     */
    public String handleMessage(String requestBody, String signature, String timestamp, String nonce, String openid,
            String encType, String msgSignature) {
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            throw new ValidationFailedException("非法请求，可能属于伪造的请求！");
        }
        String out = "success";

        // 明文模式
        if (StrUtil.isBlank(encType)) {
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage != null) {
                out = outMessage.toXml();
            }
        }

        // aes加密模式
        if (SymmetricAlgorithm.AES.name().equalsIgnoreCase(encType)) {
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxMpService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);

            log.debug("消息解密后内容为：{} ", inMessage.toString());

            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage != null) {
                out = outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage());
            }
        }

        log.debug("组装回复信息：{}", out);
        return out;
    }

}
