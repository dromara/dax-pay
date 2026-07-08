package cn.daxpay.open.channel.hmpay.entity.isv;

import cn.daxpay.open.channel.hmpay.convert.isv.HmpayIsvKeyConfigConvert;
import cn.daxpay.open.channel.hmpay.result.isv.HmpayIsvKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 河马付服务商密钥配置
///
/// 河马付(杉德)为服务商模式, 服务商密钥全局唯一(按 product 查询),
/// 子商户仅需商户号(merchantNo) + 门店号(storeId), 见 [HmpayIsvChannelMerchant]。
///
/// 签名算法: RSA SHA1WithRSA, 私钥签名 / 公钥验签。
/// 敏感字段(私钥/公钥)通过 [DataEncryptTypeHandler] 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "hmpay_isv_key_config", autoResultMap = true)
public class HmpayIsvKeyConfig extends MpBaseEntity implements ToResult<HmpayIsvKeyConfigResult> {

    /// 产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 杉德代理号(sandAppId / app_id)
    private String sandAppId;

    /// 商户 RSA 私钥(PKCS#8 Base64, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// 杉德 RSA 公钥(X509 Base64, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 转换
    @Override
    public HmpayIsvKeyConfigResult toResult() {
        return HmpayIsvKeyConfigConvert.CONVERT.toResult(this);
    }
}
