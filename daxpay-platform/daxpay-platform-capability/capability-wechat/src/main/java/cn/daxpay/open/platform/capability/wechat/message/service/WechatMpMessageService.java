package cn.daxpay.open.platform.capability.wechat.message.service;

import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.capability.wechat.message.entity.WechatMessageRecord;
import cn.daxpay.open.platform.capability.wechat.message.param.TemplateMessageParam;
import cn.daxpay.open.platform.capability.wechat.message.result.MessageSendResult;
import cn.daxpay.open.platform.capability.wechat.token.service.WechatTokenService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/// # 微信公众号消息服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMpMessageService {

    private final WechatTokenService tokenService;
    private final WechatMessageRecordService recordService;

    /// 发送模板消息
    /// @param param 模板消息参数（包含配置信息）
    /// @return 消息发送结果
    public MessageSendResult sendTemplateMessage(TemplateMessageParam param) {
        // 验证配置参数
        validateConfig(param.getWxAppId(), param.getAppSecret());
        
        // 创建消息记录
        WechatMessageRecord record = createMessageRecord(param);
        recordService.saveRecord(record);
        
        var result = new MessageSendResult()
                .setRecordId(record.getId())
                .setSuccess(false);
        
        try {
            // 获取AccessToken
            String accessToken = tokenService.getAccessToken(param.getWxAppId(), param.getAppSecret());
            
            // 构建模板消息
            WxMpTemplateMessage templateMessage = buildTemplateMessage(param);
            
            // 创建微信服务
            WxMpService wxMpService = createWxMpService(param.getWxAppId(), param.getAppSecret(), accessToken);
            
            // 发送消息
            String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            
            // 更新结果
            result.setSuccess(true)
                    .setMsgId(msgId);
            
            // 更新消息记录状态
            recordService.updateStatus(record.getId(), "success", msgId, null);
            
            log.info("发送公众号模板消息成功，openId: {}, templateId: {}, msgId: {}", 
                    param.getOpenId(), param.getTemplateId(), msgId);
            
        } catch (WxErrorException e) {
            log.error("发送公众号模板消息失败，openId: {}, templateId: {}, 错误: {}", 
                    param.getOpenId(), param.getTemplateId(), e.getMessage());
            
            result.setErrorCode(String.valueOf(e.getError().getErrorCode()))
                    .setErrorMsg(e.getError().getErrorMsg());
            
            // 更新消息记录状态
            recordService.updateStatus(record.getId(), "failed", null, e.getError().getErrorMsg());
            
            // 微信: 发送公众号模板消息失败: {0}
            throw new OperationFailException("error.channel.wechat.mpTemplateMessageFailed", e.getError().getErrorMsg());
        } catch (Exception e) {
            log.error("发送公众号模板消息异常，openId: {}, templateId: {}", 
                    param.getOpenId(), param.getTemplateId(), e);
            
            result.setErrorMsg(e.getMessage());
            
            // 更新消息记录状态
            recordService.updateStatus(record.getId(), "failed", null, e.getMessage());
            
            // 微信: 发送公众号模板消息异常: {0}
            throw new OperationFailException("error.channel.wechat.mpTemplateMessageError", e.getMessage());
        }
        
        return result;
    }

    /// 批量发送模板消息（使用异步处理）
    /// @param params 模板消息参数列表（包含配置信息）
    /// @return 消息发送结果列表
    public List<MessageSendResult> batchSendTemplateMessage(List<TemplateMessageParam> params) {
        List<MessageSendResult> results = new ArrayList<>();
        
        // 使用CompletableFuture异步处理
        List<CompletableFuture<MessageSendResult>> futures = new ArrayList<>();
        
        for (TemplateMessageParam param : params) {
            CompletableFuture<MessageSendResult> future = sendTemplateMessageAsync(param);
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

    /// 验证配置参数
    private void validateConfig(String wxAppId, String appSecret) {
        if (StrUtil.isBlank(wxAppId) || StrUtil.isBlank(appSecret)) {
            log.error("微信配置参数为空，wxAppId: {}", wxAppId);
            // 微信: 微信配置参数不能为空
            throw new OperationFailException("error.channel.wechat.configParamsRequired");
        }
    }

    /// 创建消息记录
    private WechatMessageRecord createMessageRecord(TemplateMessageParam param) {
        var record = new WechatMessageRecord();
        record.setMessageType("template");
        record.setOpenId(param.getOpenId());
        record.setTemplateId(param.getTemplateId());
        record.setTemplateData(JSONUtil.toJsonStr(param.getData()));
        record.setUrl(param.getUrl());
        record.setStatus("sending");
        record.setSendTime(OffsetDateTime.now(ZoneOffset.UTC));
        record.setScene(param.getScene());
        record.setWxAppId(param.getWxAppId());
        return record;
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

    /// 创建微信公众号Service
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

