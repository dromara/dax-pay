package org.dromara.daxpay.channel.alipay.entity.direct;

import org.dromara.daxpay.channel.alipay.convert.direct.AlipayDirectAppKeyConfigConvert;
import org.dromara.daxpay.channel.alipay.result.direct.AlipayDirectAppKeyConfigResult;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用密钥配置
///
/// 存储直连商户应用的密钥和证书信息，支持公钥模式和证书模式两种认证方式，敏感字段加密存储。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_direct_app_key_config", autoResultMap = true)
public class AlipayDirectAppKeyConfig extends MchBaseEntity implements ToResult<AlipayDirectAppKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 关联应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long appId;

    /// 认证类型
    private String authType;

    /// 支付宝公钥
    private String alipayPublicKey;

    /// 应用私钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// 应用公钥证书(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appCert;

    /// 支付宝公钥证书(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String alipayCert;

    /// 支付宝CA根证书(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String alipayRootCert;

    /// AES通信密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String secretKey;

    /// 转换
    @Override
    public AlipayDirectAppKeyConfigResult toResult() {
        return AlipayDirectAppKeyConfigConvert.CONVERT.toResult(this);
    }
}
