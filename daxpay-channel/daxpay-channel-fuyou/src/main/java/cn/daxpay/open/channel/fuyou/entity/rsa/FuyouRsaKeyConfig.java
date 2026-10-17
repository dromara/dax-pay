package cn.daxpay.open.channel.fuyou.entity.rsa;

import cn.daxpay.open.channel.fuyou.convert.rsa.FuyouRsaKeyConfigConvert;
import cn.daxpay.open.channel.fuyou.result.rsa.FuyouRsaKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 富友 RSA 密钥对配置(互联网直连/H5 聚合收银台共用)
///
/// 「互联网直连」(fuyou_internet) 与「H5 聚合收银台」(fuyou_h5) 均为直连商户模式且共用同一套
/// JSON + RSA 加密信封机制(商户号 + 富友平台公钥 + 商户私钥), 故两产品共用本表, 以 product 字段区分。
///
/// 密钥用途: 请求业务 JSON 用平台公钥加密上送, 响应/回调用商户私钥解密(非签名机制)。
/// 敏感字段(私钥/公钥)通过 [DataEncryptTypeHandler] 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "fuyou_rsa_key_config", autoResultMap = true)
public class FuyouRsaKeyConfig extends MchBaseEntity implements ToResult<FuyouRsaKeyConfigResult> {

    /// 通道商户号(FUYOU+雪花)
    private String channelMchNo;

    /// 所属支付产品(fuyou_internet / fuyou_h5)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 是否沙箱环境商户(创建通道商户时按产品 activeEnv 固化)
    private boolean sandbox;

    /// 富友商户号(mchnt_cd)
    private String mchntCd;

    /// 商户RSA私钥(Base64, PKCS1/PKCS8 均可, 响应/回调解密用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// 富友平台RSA公钥(Base64, X509, 请求加密用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /// 转换
    @Override
    public FuyouRsaKeyConfigResult toResult() {
        return FuyouRsaKeyConfigConvert.CONVERT.toResult(this);
    }
}
