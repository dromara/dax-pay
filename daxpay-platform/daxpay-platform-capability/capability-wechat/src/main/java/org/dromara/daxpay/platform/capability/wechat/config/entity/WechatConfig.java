package org.dromara.daxpay.platform.capability.wechat.config.entity;

import org.dromara.daxpay.platform.common.mybatisplus.base.MpBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.capability.wechat.config.convert.WechatConfigConvert;
import org.dromara.daxpay.platform.capability.wechat.config.result.WechatConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_platform_wechat_config")
public class WechatConfig extends MpBaseEntity implements ToResult<WechatConfigResult> {

    /// 微信公众号二维码
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String qrcode;

    /// 微信公众号AppId
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String appId;

    /// 微信公众号AppSecret
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String appSecret;

    /// 交易通知模板Id
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String tradeTemplateId;

    /// 操作通知模板Id
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String operateTemplateId;

    @Override
    public WechatConfigResult toResult() {
        return WechatConfigConvert.CONVERT.toResult(this);
    }
}
