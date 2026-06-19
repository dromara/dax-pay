package cn.daxpay.open.platform.capability.wechat.message.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/// # 统一服务消息参数
///
@Data
@Accessors(chain = true)
public class UniformMessageParam {

    /// 小程序AppId
    private String wxAppId;

    /// 小程序AppSecret
    private String appSecret;

    /// 接收者OpenId
    private String openId;

    /// 模板ID
    private String templateId;

    /// 模板数据（Map格式，key为字段名，value为字段值）
    private Map<String, String> data;

    /// 小程序页面路径（可选）
    private String page;

    /// 业务场景标识（可选，用于记录）
    private String scene;
}
