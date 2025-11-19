package org.dromara.daxpay.channel.wechat.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.common.mybatisplus.handler.DataEncryptTypeHandler;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.convert.WechatIsvConfigConvert;
import org.dromara.daxpay.channel.wechat.result.config.WechatIsvConfigResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信服务商配置
 * @author xxm
 * @since 2024/11/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_wechat_isv_config", autoResultMap = true)
public class WechatIsvConfig extends MpBaseEntity implements ToResult<WechatIsvConfigResult> {

    /** 是否启用 */
    private boolean enable;

    /** 微信商户Id */
    private String wxMchId;

    /** 微信应用appId */
    private String wxAppId;

    /**
     * 接口版本, 使用v2还是v3接口
     * @see WechatPayCode#API_V2
     */
    private String apiVersion;

    /** 商户平台「API安全」中的 APIv2 密钥 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String apiKeyV2;

    /** 商户平台「API安全」中的 APIv3 密钥 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String apiKeyV3;

    /** APPID对应的接口密码，用于获取微信公众号jsapi支付时使用 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appSecret;

    /** 支付公钥(pub_key.pem) */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /** 支付公钥ID */
    private String publicKeyId;

    /** apiclient_key. pem证书base64编码 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /** apiclient_cert. pem证书base64编码 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateCert;

    /** 证书序列号 */
    private String certSerialNo;

    /** API证书中p12证书Base64 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String p12;

    /** 微信密钥 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String wxAppSecret;

    /** 微信授权认证地址 */
    private String wxAuthUrl;

    /** 服务商号 */
    private String isvNo;

    @Override
    public WechatIsvConfigResult toResult() {
        return WechatIsvConfigConvert.CONVERT.toResult(this);
    }
}

