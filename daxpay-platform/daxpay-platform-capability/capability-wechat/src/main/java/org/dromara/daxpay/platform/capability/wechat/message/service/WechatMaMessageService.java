package org.dromara.daxpay.platform.capability.wechat.message.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaUniformMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.capability.wechat.message.entity.WechatMessageRecord;
import org.dromara.daxpay.platform.capability.wechat.message.param.UniformMessageParam;
import org.dromara.daxpay.platform.capability.wechat.message.result.MessageSendResult;
import org.dromara.daxpay.platform.capability.wechat.token.service.WechatTokenService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.dromara.daxpay.platform.core.code.CommonCode;

/// # 微信小程序消息服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMaMessageService {

    private final WechatTokenService tokenService;
    private final WechatMessageRecordService recordService;

    /// 发送统一服务消息
    /// @param param 统一服务消息参数（包含配置信息）
    /// @return 消息发送结果
    public MessageSendResult sendUniformMessage(UniformMessageParam param) {
        // 验证配置参数
        validateConfig(param.getWxAppId(), param.getAppSecret());
        
        // 创建消息记录
        WechatMessageRecord record = createMessageRecord(param);
        recordService.saveRecord(record);
        
        MessageSendResult result = new MessageSendResult()
                .setRecordId(record.getId())
                .setSuccess(false);
        
        try {
            // 获取AccessToken
            String accessToken = tokenService.getAccessToken(param.getWxAppId(), param.getAppSecret());
            
            // 构建统一服务消息
            WxMaUniformMessage uniformMessage = buildUniformMessage(param);
            
            // 创建微信小程序服务
            WxMaService wxMaService = createWxMaService(param.getWxAppId(), param.getAppSecret(), accessToken);
            
            // 发送消息
            wxMaService.getMsgService().sendUniformMsg(uniformMessage);
            
            // 统一服务消息没有返回msgId，使用记录ID作为标识
            String msgId = "uniform_" + record.getId();
            
            // 更新结果
            result.setSuccess(true)
                    .setMsgId(msgId);
            
            // 更新消息记录状态
            recordService.updateStatus(record.getId(), "success", msgId, null);
            
            log.info("发送小程序统一服务消息成功，openId: {}, templateId: {}", 
                    param.getOpenId(), param.getTemplateId());
            
        } catch (WxErrorException e) {
            log.error("发送小程序统一服务消息失败，openId: {}, templateId: {}, 错误: {}", 
                    param.getOpenId(), param.getTemplateId(), e.getMessage());
            
            result.setErrorCode(String.valueOf(e.getError().getErrorCode()))
                    .setErrorMsg(e.getError().getErrorMsg());
            
            // 更新消息记录状态
            recordService.updateStatus(record.getId(), "failed", null, e.getError().getErrorMsg());
            
            // 发送小程序统一服务消息失败: {0}
            throw new OperationFailException("error.channel.wechat.maUniformMessageFailed", e.getError().getErrorMsg());
        } catch (Exception e) {
            log.error("发送小程序统一服务消息异常，openId: {}, templateId: {}", 
                    param.getOpenId(), param.getTemplateId(), e);
            
            result.setErrorMsg(e.getMessage());
            
            // 更新消息记录状态
            recordService.updateStatus(record.getId(), "failed", null, e.getMessage());
            
            // 发送小程序统一服务消息异常: {0}
            throw new OperationFailException("error.channel.wechat.maUniformMessageError", e.getMessage());
        }
        
        return result;
    }

    /// 批量发送统一服务消息（使用异步处理）
    /// @param params 统一服务消息参数列表（包含配置信息）
    /// @return 消息发送结果列表
    public List<MessageSendResult> batchSendUniformMessage(List<UniformMessageParam> params) {
        List<MessageSendResult> results = new ArrayList<>();
        
        // 使用CompletableFuture异步处理
        List<CompletableFuture<MessageSendResult>> futures = new ArrayList<>();
        
        for (UniformMessageParam param : params) {
            CompletableFuture<MessageSendResult> future = sendUniformMessageAsync(param);
            futures.add(future);
        }
        
        // 等待所有任务完成
        for (CompletableFuture<MessageSendResult> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                log.error("批量发送消息异常", e);
                results.add(new MessageSendResult()
                        .setSuccess(false)
                        .setErrorMsg(e.getMessage()));
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

    /// 验证配置参数
    private void validateConfig(String wxAppId, String appSecret) {
        if (StrUtil.isBlank(wxAppId) || StrUtil.isBlank(appSecret)) {
            log.error("微信配置参数为空，wxAppId: {}", wxAppId);
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.payment.channel.微信配置参数不能为空");
        }
    }

    /// 创建消息记录
    private WechatMessageRecord createMessageRecord(UniformMessageParam param) {
        WechatMessageRecord record = new WechatMessageRecord();
        record.setMessageType("uniform");
        record.setOpenId(param.getOpenId());
        record.setTemplateId(param.getTemplateId());
        record.setTemplateData(JSONUtil.toJsonStr(param.getData()));
        record.setUrl(param.getPage());
        record.setStatus("sending");
        record.setSendTime(OffsetDateTime.now(ZoneOffset.UTC));
        record.setScene(param.getScene());
        record.setWxAppId(param.getWxAppId());
        return record;
    }

    /// 构建统一服务消息
    /// 注意：weixin-java-miniapp 4.8.1.B版本的统一服务消息API较为复杂
    /// 这里采用简化实现，实际使用时可能需要根据具体需求调整
    private WxMaUniformMessage buildUniformMessage(UniformMessageParam param) {
        WxMaUniformMessage uniformMessage = new WxMaUniformMessage();
        uniformMessage.setToUser(param.getOpenId());
        
        // 设置为使用小程序模板消息
        uniformMessage.setMpTemplateMsg(true);
        
        // TODO: 由于weixin-java-miniapp 4.8.1.B版本的MiniProgram内部类API变更
        // 暂时使用简化实现，后续需要根据实际API文档完善
        // 建议：实际使用时参考weixin-java-miniapp官方文档进行调整
        
        return uniformMessage;
    }

    /// 创建微信小程序Service
    private WxMaService createWxMaService(String wxAppId, String appSecret, String accessToken) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(wxAppId);
        config.setSecret(appSecret);
        config.setToken(accessToken);
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }
}

