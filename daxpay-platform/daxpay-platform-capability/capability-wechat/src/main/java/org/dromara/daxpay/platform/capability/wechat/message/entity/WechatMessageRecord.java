package org.dromara.daxpay.platform.capability.wechat.message.entity;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 微信消息记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_platform_wechat_message_record")
public class WechatMessageRecord extends MpBaseEntity {

    /// 消息类型：template-公众号模板消息，uniform-小程序统一服务消息
    private String messageType;

    /// 接收者OpenId
    private String openId;

    /// 模板ID
    private String templateId;

    /// 模板数据（JSON格式）
    private String templateData;

    /// 跳转链接或小程序页面路径
    private String url;

    /// 发送状态：success-成功，failed-失败，retry-待重试
    private String status;

    /// 微信消息ID
    private String msgId;

    /// 错误码
    private String errorCode;

    /// 错误信息
    private String errorMsg;

    /// 发送时间
    private OffsetDateTime sendTime;

    /// 业务场景标识
    private String scene;

    /// 使用的AppId（用于多副本部署时区分不同配置）
    private String wxAppId;
}
