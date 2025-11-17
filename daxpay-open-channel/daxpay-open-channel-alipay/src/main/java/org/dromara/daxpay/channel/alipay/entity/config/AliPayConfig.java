package org.dromara.daxpay.channel.alipay.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.common.mybatisplus.handler.DataEncryptTypeHandler;
import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.AlipayConfigConvert;
import org.dromara.daxpay.channel.alipay.result.config.AlipayConfigResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * 支付宝配置(防止与sdk中类重名, P大写)
 * @author xxm
 * @since 2024/6/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_alipay_config", autoResultMap = true)
public class AliPayConfig extends MchAppBaseEntity implements ToResult<AlipayConfigResult> {

    /** 是否为ISV商户(特约商户) */
    private boolean isv;

    /** 支付宝商户appId */
    private String aliAppId;

    /** 支付宝特约商户Token */
    private String appAuthToken;

    /** 是否启用 */
    private Boolean enable;

    /**
     * 认证类型 证书/公钥
     * @see AlipayCode.AuthType
     */
    private String authType;

    /** 签名类型 RSA2 */
    public String signType;

    /**
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    private String alipayUserId;

    /** 支付宝公钥 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    public String alipayPublicKey;

    /** 应用私钥 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /** 应用公钥证书 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appCert;

    /** 支付宝公钥证书 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String alipayCert;

    /** 支付宝CA根证书 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String alipayRootCert;

    /** 是否沙箱环境 */
    private boolean sandbox;

    /** 商户号 */
    private String mchNo;

    /** 商户AppId */
    private String appId;

    

    @Override
    public AlipayConfigResult toResult() {
        return AlipayConfigConvert.CONVERT.toResult(this);
    }
}
