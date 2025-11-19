package org.dromara.daxpay.channel.wechat.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.common.mybatisplus.handler.DataEncryptTypeHandler;
import org.dromara.daxpay.channel.wechat.convert.WechatPaySubConfigConvert;
import org.dromara.daxpay.channel.wechat.enums.WechatAuthTypeEnum;
import org.dromara.daxpay.channel.wechat.result.config.WechatPaySubConfigResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 微信特约商户配置类
 * @author xxm
 * @since 2024/12/27
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_wechat_sub_config", autoResultMap = true)
public class WechatPaySubConfig extends MchAppBaseEntity implements ToResult<WechatPaySubConfigResult> {

    /** 是否启用 */
    private boolean enable;

    /** 微信特约商户号/二级商户号 */
    private String subMchId;

    /**
     * 授权类型
     * @see WechatAuthTypeEnum
     */
    private String authType;

    /** 微信特约商户/二级商户AppId */
    private String subAppId;

    /** 微信特约商户/二级商户密钥 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String wxAppSecret;

    /** 微信特约商户/二级商户授权认证地址 */
    private String wxAuthUrl;

    /**
     * 默认使用服务商配置
     */
    public String getAuthType() {
        return Objects.equals(authType, WechatAuthTypeEnum.SUB.getCode())?
                WechatAuthTypeEnum.SUB.getCode():WechatAuthTypeEnum.SP.getCode();
    }

    @Override
    public WechatPaySubConfigResult toResult() {
        return WechatPaySubConfigConvert.CONVERT.toResult(this);
    }
}
