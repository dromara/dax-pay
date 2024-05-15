package cn.bootx.platform.starter.wechat.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.starter.wechat.core.portal.service.WeChatPortalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 微信工作台接入入口
 *
 * @author xxm
 * @since 2022/7/16
 */
@IgnoreAuth
@Slf4j
@Tag(name = "微信接入入口")
@RestController
@RequestMapping("/wechat/portal")
@RequiredArgsConstructor
public class WeChatPortalController {

    private final WeChatPortalService weChatPortalService;

    /**
     * 微信接入校验处理
     * @param signature 微信签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     */
    @GetMapping(produces = "text/plain;charset=utf-8")
    public String auth(String signature, String timestamp, String nonce, String echostr) {
        return weChatPortalService.auth(signature, timestamp, nonce, echostr);
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
    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody, String signature, String timestamp, String nonce, String openid,
            @RequestParam(name = "encrypt_type", required = false) String encType,
            @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        return weChatPortalService.handleMessage(requestBody, signature, timestamp, nonce, openid, encType,
                msgSignature);
    }

}
