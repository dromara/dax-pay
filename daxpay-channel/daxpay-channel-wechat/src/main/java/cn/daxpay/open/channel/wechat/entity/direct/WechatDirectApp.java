package cn.daxpay.open.channel.wechat.entity.direct;

import cn.daxpay.open.channel.wechat.convert.direct.WechatDirectAppConvert;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectAppResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信直连商户应用
///
/// 表示微信直连模式下商户注册的应用实体，每个应用关联一个通道商户号，拥有独立的微信应用ID。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_direct_app", autoResultMap = true)
public class WechatDirectApp extends MchBaseEntity implements ToResult<WechatDirectAppResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 应用名称
    private String appName;

    /// 应用类型
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appType;

    /// 微信应用AppId
    private String wxAppId;

    /// 转换
    @Override
    public WechatDirectAppResult toResult() {
        return WechatDirectAppConvert.CONVERT.toResult(this);
    }
}
