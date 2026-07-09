package cn.daxpay.open.platform.notify.entity.wechat;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.notify.convert.wechat.WechatMessageRecordConvert;
import cn.daxpay.open.platform.notify.result.wechat.WechatMessageRecordResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 微信消息发送记录
///
/// 每次发送(含重发)产生一条记录, 记录发送参数与结果(成功/失败/错误信息), 供管理端查询与重发.
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_platform_wechat_message_record")
public class WechatMessageRecord extends MpBaseEntity implements ToResult<WechatMessageRecordResult> {

    /// 接收平台用户ID(发送目标, 即使未绑定 openId 也能追溯)
    private Long userId;

    /// 消息类型: template-公众号模板消息, uniform-小程序统一服务消息
    private String messageType;

    /// 接收者 OpenId
    private String openId;

    /// 模板ID
    private String templateId;

    /// 模板数据(JSON 格式)
    private String templateData;

    /// 跳转链接或小程序页面路径
    private String url;

    /// 发送状态: sending-发送中, success-成功, failed-失败
    private String status;

    /// 微信消息ID(成功时返回)
    private String msgId;

    /// 错误码(微信错误码, 失败时填充)
    private String errorCode;

    /// 错误信息(失败时填充)
    private String errorMsg;

    /// 发送时间
    private OffsetDateTime sendTime;

    /// 业务场景标识(trade/operate 等)
    private String scene;

    /// 使用的 AppId(发送时配置的公众号 AppId)
    private String wxAppId;

    @Override
    public WechatMessageRecordResult toResult() {
        return WechatMessageRecordConvert.CONVERT.toResult(this);
    }
}
