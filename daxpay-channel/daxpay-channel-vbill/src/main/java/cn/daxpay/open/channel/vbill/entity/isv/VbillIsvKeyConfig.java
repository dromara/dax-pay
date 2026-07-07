package cn.daxpay.open.channel.vbill.entity.isv;

import cn.daxpay.open.channel.vbill.convert.isv.VbillIsvKeyConfigConvert;
import cn.daxpay.open.channel.vbill.result.isv.VbillIsvKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 随行付服务商密钥配置
///
/// 随行付(天阙科技)为收单机构服务商模式, 服务商密钥全局唯一(按 product 查询),
/// 子商户仅需天阙商户号(mno), 见 [VbillIsvChannelMerchant]。
///
/// 签名算法: SHA1withRSA, 私钥签名 / 公钥验签。
/// 敏感字段(私钥/公钥)通过 [DataEncryptTypeHandler] 加密入库。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "vbill_isv_key_config", autoResultMap = true)
public class VbillIsvKeyConfig extends MpBaseEntity implements ToResult<VbillIsvKeyConfigResult> {

    /// 产品编码
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 天阙合作机构ID(orgId)
    private String orgId;

    /// 天阙RSA公钥(X509 Base64, 用于响应/回调验签, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /// 商户RSA私钥(PKCS8 Base64, SHA1withRSA 签名, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    /// 转换
    @Override
    public VbillIsvKeyConfigResult toResult() {
        return VbillIsvKeyConfigConvert.CONVERT.toResult(this);
    }
}
