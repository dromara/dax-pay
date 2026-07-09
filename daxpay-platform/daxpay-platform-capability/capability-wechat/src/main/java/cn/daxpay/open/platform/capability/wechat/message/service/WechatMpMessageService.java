package cn.daxpay.open.platform.capability.wechat.message.service;

import cn.daxpay.open.platform.capability.wechat.message.param.TemplateMessageParam;
import cn.daxpay.open.platform.capability.wechat.message.result.MessageSendResult;
import cn.daxpay.open.platform.capability.wechat.token.service.WechatTokenService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/// # 微信公众号消息服务(纯执行层)
///
/// 所有配置(wxAppId/appSecret/openId/templateId/data)均由调用方传入, 本类不读取配置、不写记录,
/// 仅负责调用微信 SDK 发送模板消息并返回结果. 失败封装进 [MessageSendResult] 返回(不抛异常),
/// 便于上层(service-notify)统一记录成功/失败, 无需 try-catch.
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMpMessageService {

    private final WechatTokenService tokenService;

    /// 发送模板消息(纯发送, 失败封装进 result 返回, 不抛异常)
    public MessageSendResult sendTemplateMessage(TemplateMessageParam param) {
        var result = new MessageSendResult().setSuccess(false);
        // 参数校验: 配置不全直接返回失败结果
        if (StrUtil.isBlank(param.getWxAppId()) || StrUtil.isBlank(param.getAppSecret())) {
            log.error("微信配置参数为空，wxAppId: {}", param.getWxAppId());
            return result.setErrorMsg("wxAppId/appSecret is required");
        }
        try {
            // 获取 AccessToken
            String accessToken = tokenService.getAccessToken(param.getWxAppId(), param.getAppSecret());
            // 构建模板消息并发送
            WxMpTemplateMessage templateMessage = buildTemplateMessage(param);
            WxMpService wxMpService = createWxMpService(param.getWxAppId(), param.getAppSecret(), accessToken);
            String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            result.setSuccess(true).setMsgId(msgId);
            log.info("发送公众号模板消息成功，openId: {}, templateId: {}, msgId: {}",
                    param.getOpenId(), param.getTemplateId(), msgId);
        } catch (WxErrorException e) {
            log.error("发送公众号模板消息失败，openId: {}, templateId: {}, 错误: {}",
                    param.getOpenId(), param.getTemplateId(), e.getMessage());
            result.setErrorCode(String.valueOf(e.getError().getErrorCode()))
                    .setErrorMsg(e.getError().getErrorMsg());
        } catch (Exception e) {
            log.error("发送公众号模板消息异常，openId: {}, templateId: {}",
                    param.getOpenId(), param.getTemplateId(), e);
            result.setErrorMsg(e.getMessage());
        }
        return result;
    }

    /// 批量发送模板消息(异步处理)
    public List<MessageSendResult> batchSendTemplateMessage(List<TemplateMessageParam> params) {
        List<MessageSendResult> results = new ArrayList<>();
        List<CompletableFuture<MessageSendResult>> futures = new ArrayList<>();
        for (TemplateMessageParam param : params) {
            futures.add(sendTemplateMessageAsync(param));
        }
        for (CompletableFuture<MessageSendResult> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                log.error("批量发送消息异常", e);
                results.add(new MessageSendResult().setSuccess(false).setErrorMsg(e.getMessage()));
            }
        }
        return results;
    }

    /// 异步发送模板消息
    @Async
    public CompletableFuture<MessageSendResult> sendTemplateMessageAsync(TemplateMessageParam param) {
        try {
            MessageSendResult result = sendTemplateMessage(param);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            log.error("异步发送消息失败", e);
            return CompletableFuture.completedFuture(new MessageSendResult()
                    .setSuccess(false)
                    .setErrorMsg(e.getMessage()));
        }
    }

    /// 构建模板消息
    private WxMpTemplateMessage buildTemplateMessage(TemplateMessageParam param) {
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(param.getOpenId())
                .templateId(param.getTemplateId())
                .url(param.getUrl())
                .build();
        // 设置模板数据
        if (CollUtil.isNotEmpty(param.getData())) {
            List<WxMpTemplateData> dataList = new ArrayList<>();
            for (Map.Entry<String, String> entry : param.getData().entrySet()) {
                dataList.add(new WxMpTemplateData(entry.getKey(), entry.getValue()));
            }
            templateMessage.setData(dataList);
        }
        return templateMessage;
    }

    /// 创建微信公众号 Service
    private WxMpService createWxMpService(String wxAppId, String appSecret, String accessToken) {
        WxMpService wxMpService = new WxMpServiceImpl();
        var config = new WxMpDefaultConfigImpl();
        config.setAppId(wxAppId);
        config.setSecret(appSecret);
        config.setAccessToken(accessToken);
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }
}
