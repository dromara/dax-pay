package cn.daxpay.open.channel.adapay.entity.direct;

import cn.daxpay.open.channel.adapay.convert.direct.AdapayDirectKeyConfigConvert;
import cn.daxpay.open.channel.adapay.result.direct.AdapayDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Adapay 直连密钥配置
///
/// 直连商户维度的通道配置, 含Adapay 支付应用 ID 与签名密钥, 敏感字段(apiKey/privateKey/publicKey)加密存储。
/// channelMchNo(通道商户号) 创建时录入不可修改, adapayAppId/apiKey/privateKey/publicKey 由密钥配置维护。
/// Adapay 签名算法: SHA1withRSA, 私钥签名 / 平台公钥验签, 无需证书。
/// Adapay 商户身份由 app_id + apiKey(Authorization) + RSA 私钥签名承载, 无需单独的商户号字段。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "adapay_direct_key_config", autoResultMap = true)
public class AdapayDirectKeyConfig extends MchBaseEntity implements ToResult<AdapayDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// Adapay 支付应用 ID(app_id)
    private String adapayAppId;

    /// Adapay API Key(请求头 Authorization, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String apiKey;

    /// 商户 RSA 私钥(PKCS#8 Base64, 请求签名, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// Adapay 平台公钥(X509 Base64, 响应验签, 加密存储; 为空时使用全局默认公钥)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    @Override
    public AdapayDirectKeyConfigResult toResult() {
        return AdapayDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
