package org.dromara.daxpay.channel.alipay.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.common.mybatisplus.handler.DataEncryptTypeHandler;
import org.dromara.daxpay.channel.alipay.code.AlipayCode;
import org.dromara.daxpay.channel.alipay.convert.AlipayIsvConfigConvert;
import org.dromara.daxpay.channel.alipay.result.config.AlipayIsvConfigResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;


/**
 * 支付宝服务商配置
 * @author xxm
 * @since 2024/10/31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_alipay_isv_config", autoResultMap = true)
public class AlipayIsvConfig extends MpBaseEntity implements ToResult<AlipayIsvConfigResult> {

    /** 支付宝商户appId */
    private String aliAppId;

    /** 是否启用 */
    private boolean enable;

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

    /** 服务商号 */
    private String isvNo;

    

    @Override
    public AlipayIsvConfigResult toResult() {
        return AlipayIsvConfigConvert.CONVERT.toResult(this);
    }
}
