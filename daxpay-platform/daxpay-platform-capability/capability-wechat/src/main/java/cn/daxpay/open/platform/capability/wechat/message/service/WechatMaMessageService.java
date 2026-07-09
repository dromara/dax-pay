package cn.daxpay.open.platform.capability.wechat.message.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaUniformMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.daxpay.open.platform.capability.wechat.message.param.UniformMessageParam;
import cn.daxpay.open.platform.capability.wechat.message.result.MessageSendResult;
import cn.daxpay.open.platform.capability.wechat.token.service.WechatTokenService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/// # 微信小程序消息服务(纯执行层)
///
/// 所有配置均由调用方传入, 不读取配置、不写记录, 仅发送并返回结果.
/// 失败封装进 [MessageSendResult] 返回, 不抛异常.
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMaMessageService {

    private final WechatTokenService tokenService;

    /// 发送统一服务消息(纯发送, 失败封装进 result 返回, 不抛异常)
    public MessageSendResult sendUniformMessage(UniformMessageParam param) {
        var result = new MessageSendResult().setSuccess(false);
        // 参数校验
        if (StrUtil.isBlank(param.getWxAppId()) || StrUtil.isBlank(param.getAppSecret())) {
            log.error("微信配置参数为空，wxAppId: {}", param.getWxAppId());
            return result.setErrorMsg("wxAppId/appSecret is required");
        }
        try {
            // 获取 AccessToken
            String accessToken = tokenService.getAccessToken(param.getWxAppId(), param.getAppSecret());
            // 构建并发送统一服务消息
            WxMaUniformMessage uniformMessage = buildUniformMessage(param);
            WxMaService wxMaService = createWxMaService(param.getWxAppId(), param.getAppSecret(), accessToken);
            wxMaService.getMsgService().sendUniformMsg(uniformMessage);
            // 统一服务消息没有返回 msgId, 用固定前缀占位
            result.setSuccess(true).setMsgId("uniform");
            log.info("发送小程序统一服务消息成功，openId: {}, templateId: {}",
                    param.getOpenId(), param.getTemplateId());
        } catch (WxErrorException e) {
            log.error("发送小程序统一服务消息失败，openId: {}, templateId: {}, 错误: {}",
                    param.getOpenId(), param.getTemplateId(), e.getMessage());
            result.setErrorCode(String.valueOf(e.getError().getErrorCode()))
                    .setErrorMsg(e.getError().getErrorMsg());
        } catch (Exception e) {
            log.error("发送小程序统一服务消息异常，openId: {}, templateId: {}",
                    param.getOpenId(), param.getTemplateId(), e);
            result.setErrorMsg(e.getMessage());
        }
        return result;
    }

    /// 批量发送统一服务消息(异步处理)
    public List<MessageSendResult> batchSendUniformMessage(List<UniformMessageParam> params) {
        List<MessageSendResult> results = new ArrayList<>();
        List<CompletableFuture<MessageSendResult>> futures = new ArrayList<>();
        for (UniformMessageParam param : params) {
            futures.add(sendUniformMessageAsync(param));
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

    /// 异步发送统一服务消息
    @Async
    public CompletableFuture<MessageSendResult> sendUniformMessageAsync(UniformMessageParam param) {
        try {
            MessageSendResult result = sendUniformMessage(param);
            return CompletableFuture.completedFuture(result);
        } catch (Exception e) {
            log.error("异步发送消息失败", e);
            return CompletableFuture.completedFuture(new MessageSendResult()
                    .setSuccess(false)
                    .setErrorMsg(e.getMessage()));
        }
    }

    /// 构建统一服务消息
    private WxMaUniformMessage buildUniformMessage(UniformMessageParam param) {
        var uniformMessage = new WxMaUniformMessage();
        uniformMessage.setToUser(param.getOpenId());
        // 使用小程序模板消息
        uniformMessage.setMpTemplateMsg(true);
        return uniformMessage;
    }

    /// 创建微信小程序 Service
    private WxMaService createWxMaService(String wxAppId, String appSecret, String accessToken) {
        WxMaService wxMaService = new WxMaServiceImpl();
        var config = new WxMaDefaultConfigImpl();
        config.setAppid(wxAppId);
        config.setSecret(appSecret);
        config.setToken(accessToken);
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }
}
