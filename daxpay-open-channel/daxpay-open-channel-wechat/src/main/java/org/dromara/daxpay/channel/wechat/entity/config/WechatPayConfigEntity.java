package org.dromara.daxpay.channel.wechat.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.convert.WechatPayConfigConvert;
import org.dromara.daxpay.channel.wechat.enums.WechatAuthTypeEnum;
import org.dromara.daxpay.channel.wechat.result.config.WechatPayConfigResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信支付配置
 *
 * @author xxm
 * @since 2021/3/1
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_wechat_pay_config",autoResultMap = true)
public class WechatPayConfigEntity extends MchAppBaseEntity implements ToResult<WechatPayConfigResult> {

    /** 是否启用 */
    private boolean enable;

    /** 微信商户Id */
    private String wxMchId;

    /** 微信应用appId */
    private String wxAppId;

    /**
     * 授权类型
     * @see WechatAuthTypeEnum
     */
    private String authType;

    /** 授权认证地址 */
    private String authUrl;

    /**
     * 接口版本, 使用v2还是v3接口
     * @see WechatPayCode#API_V2
     */
    private String apiVersion;

    /** 商户平台「API安全」中的 APIv2 密钥 */
    private String apiKeyV2;

    /** 商户平台「API安全」中的 APIv3 密钥 */
    private String apiKeyV3;

    /** APPID对应的接口密码，用于获取微信公众号jsapi支付时使用 */
    private String appSecret;

    /** 支付公钥(pub_key.pem) */
    private String publicKey;

    /** 支付公钥ID */
    private String publicKeyId;

    /** 商户API证书(apiclient_cert.pem)base64编码 */
    private String privateCert;

    /** 商户API证书私钥(apiclient_key.pem)证书base64编码 */
    private String privateKey;

    /** 商户API证书序列号 */
    private String certSerialNo;

    /** p12证书Base64 */
    private String p12;



    public String getAuthUrl() {
        return StrUtil.removeSuffix(authUrl, "/");
    }

    @Override
    public WechatPayConfigResult toResult() {
        return WechatPayConfigConvert.CONVERT.toResult(this);
    }
}
