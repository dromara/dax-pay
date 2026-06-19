package cn.daxpay.open.platform.capability.wechat.message.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/// # 模板消息参数
///
@Data
@Accessors(chain = true)
public class TemplateMessageParam {

    /// 公众号AppId
    private String wxAppId;

    /// 公众号AppSecret
    private String appSecret;

    /// 接收者OpenId
    private String openId;

    /// 模板ID
    private String templateId;

    /// 模板数据（Map格式，key为字段名，value为字段值）
    private Map<String, String> data;

    /// 跳转链接（可选）
    private String url;

    /// 业务场景标识（可选，用于记录）
    private String scene;
}
