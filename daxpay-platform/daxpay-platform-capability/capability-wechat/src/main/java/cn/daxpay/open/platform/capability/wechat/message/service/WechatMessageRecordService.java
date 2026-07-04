package cn.daxpay.open.platform.capability.wechat.message.service;

import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.capability.wechat.message.dao.WechatMessageRecordManager;
import cn.daxpay.open.platform.capability.wechat.message.entity.WechatMessageRecord;
import cn.daxpay.open.platform.capability.wechat.message.param.TemplateMessageParam;
import cn.daxpay.open.platform.capability.wechat.message.param.UniformMessageParam;
import cn.daxpay.open.platform.capability.wechat.message.result.MessageSendResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 微信消息记录服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMessageRecordService {

    private final WechatMessageRecordManager recordManager;
    
    @Lazy
    private final WechatMpMessageService mpMessageService;
    
    @Lazy
    private final WechatMaMessageService maMessageService;

    /// 保存消息记录（异步保存）
    /// @param record 消息记录
    @Async
    public void saveRecord(WechatMessageRecord record) {
        try {
            recordManager.save(record);
            log.debug("保存消息记录成功，recordId: {}", record.getId());
        } catch (Exception e) {
            log.error("保存消息记录失败", e);
        }
    }

    /// 更新消息状态
    /// @param recordId 记录ID
    /// @param status 消息状态
    /// @param msgId 微信消息ID
    /// @param errorMsg 错误信息
    public void updateStatus(Long recordId, String status, String msgId, String errorMsg) {
        try {
            var record = new WechatMessageRecord();
            record.setId(recordId);
            record.setStatus(status);
            record.setMsgId(msgId);
            record.setErrorMsg(errorMsg);
            recordManager.updateById(record);
            log.debug("更新消息状态成功，recordId: {}, status: {}", recordId, status);
        } catch (Exception e) {
            log.error("更新消息状态失败，recordId: {}", recordId, e);
        }
    }

    /// 查询消息记录
    /// @param openId 用户OpenId
    /// @param messageType 消息类型
    /// @param status 发送状态
    /// @param startTime 开始时间
    /// @param endTime 结束时间
    /// @return 消息记录列表
    public List<WechatMessageRecord> queryRecords(String openId, String messageType,
                                                   String status, OffsetDateTime startTime,
                                                   OffsetDateTime endTime) {
        LambdaQueryWrapper<WechatMessageRecord> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(openId)) {
            wrapper.eq(WechatMessageRecord::getOpenId, openId);
        }
        if (StrUtil.isNotBlank(messageType)) {
            wrapper.eq(WechatMessageRecord::getMessageType, messageType);
        }
        if (StrUtil.isNotBlank(status)) {
            wrapper.eq(WechatMessageRecord::getStatus, status);
        }
        if (startTime != null) {
            wrapper.ge(WechatMessageRecord::getSendTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(WechatMessageRecord::getSendTime, endTime);
        }

        wrapper.orderByDesc(WechatMessageRecord::getSendTime);

        return recordManager.findAll(wrapper);
    }

    /// 统计消息状态
    /// @return 状态统计结果
    public Map<String, Long> countByStatus() {
        List<WechatMessageRecord> records = recordManager.findAll();
        return records.stream()
                .collect(Collectors.groupingBy(
                        WechatMessageRecord::getStatus,
                        Collectors.counting()
                ));
    }

    /// 根据ID获取消息记录
    public WechatMessageRecord getById(Long recordId) {
        return recordManager.findById(recordId).orElse(null);
    }

    /// 重发消息
    /// @param recordId 记录ID
    /// @return 消息发送结果
    public MessageSendResult resendMessage(Long recordId) {
        // 获取消息记录
        WechatMessageRecord record = recordManager.findById(recordId).orElse(null);
        if (record == null) {
            // 微信: 消息记录不存在
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.channel.wechat.messageRecordNotExist");
        }
        
        // 验证消息状态
        if (!"failed".equals(record.getStatus())) {
            // 微信: 只能重发失败的消息
            throw new OperationFailException(CommonCode.FAIL_CODE, "error.channel.wechat.onlyFailedCanResend");
        }
        
        log.info("开始重发消息，recordId: {}, messageType: {}", recordId, record.getMessageType());
        
        try {
            MessageSendResult result;
            
            // 根据消息类型调用对应的发送服务
            if ("template".equals(record.getMessageType())) {
                // 重发公众号模板消息
                TemplateMessageParam param = buildTemplateMessageParam(record);
                result = mpMessageService.sendTemplateMessage(param);
            } else if ("uniform".equals(record.getMessageType())) {
                // 重发小程序统一服务消息
                UniformMessageParam param = buildUniformMessageParam(record);
                result = maMessageService.sendUniformMessage(param);
            } else {
                // 微信: 不支持的消息类型: {0}
                throw new OperationFailException("error.channel.wechat.unsupportedMessageType", record.getMessageType());
            }
            
            // 更新原消息记录状态
            if (result.getSuccess()) {
                updateStatus(recordId, "success", result.getMsgId(), null);
            } else {
                updateStatus(recordId, "failed", null, result.getErrorMsg());
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("重发消息失败，recordId: {}", recordId, e);
            updateStatus(recordId, "failed", null, e.getMessage());
            // 微信: 重发消息失败: {0}
            throw new OperationFailException("error.channel.wechat.messageResendFailed", e.getMessage());
        }
    }

    /// 构建模板消息参数
    @SuppressWarnings("unchecked")
    private TemplateMessageParam buildTemplateMessageParam(WechatMessageRecord record) {
        var param = new TemplateMessageParam();
        param.setWxAppId(record.getWxAppId());
        // AppSecret需要从外部传入，这里暂时为空，实际使用时需要调用方提供
        param.setOpenId(record.getOpenId());
        param.setTemplateId(record.getTemplateId());
        param.setUrl(record.getUrl());
        param.setScene(record.getScene());
        
        // 解析模板数据
        if (StrUtil.isNotBlank(record.getTemplateData())) {
            Map<String, String> data = JSONUtil.toBean(record.getTemplateData(), Map.class);
            param.setData(data);
        }
        
        return param;
    }
    
    /// 构建统一服务消息参数
    @SuppressWarnings("unchecked")
    private UniformMessageParam buildUniformMessageParam(WechatMessageRecord record) {
        var param = new UniformMessageParam();
        param.setWxAppId(record.getWxAppId());
        // AppSecret需要从外部传入，这里暂时为空，实际使用时需要调用方提供
        param.setOpenId(record.getOpenId());
        param.setTemplateId(record.getTemplateId());
        param.setPage(record.getUrl());
        param.setScene(record.getScene());
        
        // 解析模板数据
        if (StrUtil.isNotBlank(record.getTemplateData())) {
            Map<String, String> data = JSONUtil.toBean(record.getTemplateData(), Map.class);
            param.setData(data);
        }
        
        return param;
    }
}

