package cn.daxpay.open.channel.adapay.entity.isv;

import cn.daxpay.open.channel.adapay.convert.isv.AdapayIsvKeyConfigConvert;
import cn.daxpay.open.channel.adapay.result.isv.AdapayIsvKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Adapay 服务商密钥配置
///
/// Adapay(汇付天下)为服务商性质通道: 平台作为唯一服务商, 按环境(生产/沙箱)各保存一份服务商密钥。
/// 与通道商户直连配置([cn.daxpay.open.channel.adapay.entity.direct.AdapayDirectKeyConfig])并存:
/// 直连配置承载每个通道商户独立的支付应用 ID 与签名密钥, 服务商配置承载平台服务商主体身份,
/// 供服务商台账 / 进件等场景使用。
///
/// 签名算法: SHA1withRSA, 私钥签名 / 平台公钥验签, 无需证书。
/// 敏感字段(apiKey/privateKey/publicKey)通过 [DataEncryptTypeHandler] 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "adapay_isv_key_config", autoResultMap = true)
public class AdapayIsvKeyConfig extends MpBaseEntity implements ToResult<AdapayIsvKeyConfigResult> {

    /// 服务商号(平台在汇付的服务商/主体编号)
    private String isvNo;

    /// Adapay 交易密钥(请求头 Authorization, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String apiKey;

    /// 商户 RSA 私钥(PKCS#8 Base64, 请求签名, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// Adapay 平台公钥(X509 Base64, 响应验签, 加密存储; 为空时使用全局默认公钥)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 转换
    @Override
    public AdapayIsvKeyConfigResult toResult() {
        return AdapayIsvKeyConfigConvert.CONVERT.toResult(this);
    }
}
