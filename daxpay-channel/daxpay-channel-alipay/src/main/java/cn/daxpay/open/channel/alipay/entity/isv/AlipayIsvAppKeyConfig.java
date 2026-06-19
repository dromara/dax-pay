package cn.daxpay.open.channel.alipay.entity.isv;

import cn.daxpay.open.channel.alipay.convert.isv.AlipayIsvAppKeyConfigConvert;
import cn.daxpay.open.channel.alipay.result.isv.AlipayIsvAppKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用密钥配置
///
/// 存储服务商应用的密钥和证书信息，支持公钥模式和证书模式两种认证方式，敏感字段加密存储。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "alipay_isv_app_key_config", autoResultMap = true)
public class AlipayIsvAppKeyConfig extends MpBaseEntity implements ToResult<AlipayIsvAppKeyConfigResult> {

    /// 支付宝服务商应用ID
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long alipayIsvAppId;

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
    public AlipayIsvAppKeyConfigResult toResult() {
        return AlipayIsvAppKeyConfigConvert.CONVERT.toResult(this);
    }
}
