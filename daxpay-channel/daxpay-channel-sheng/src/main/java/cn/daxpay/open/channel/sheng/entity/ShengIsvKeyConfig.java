package cn.daxpay.open.channel.sheng.entity;

import cn.daxpay.open.channel.sheng.convert.ShengIsvKeyConfigConvert;
import cn.daxpay.open.channel.sheng.result.ShengIsvKeyConfigResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 盛付通服务商(ISV)密钥配置
///
/// 服务商模式下服务商凭证产品级全局一份(按 product 定位, 不挂商户维度),
/// 含服务商盛付通商户号(shengMchId)与 RSA 签名密钥对,
/// 敏感字段(服务商私钥/盛付通公钥)通过 [DataEncryptTypeHandler] 加密入库。
///
/// 子商户身份(subMchId/sdpAppId)按通道商户维度存于 [ShengIsvChannelMerchant] 绑定行,
/// 交易发起时与服务商密钥合并组装凭证(代子商户发起交易)。
///
/// 签名算法: SHA1withRSA, 服务商私钥签名 / 盛付通公钥验签。
/// 聚合 API 商户/服务商两模式同网关同端点同签名, 子应用零改动。
/// 盛付通不提供集测接口(2026-09-17 官方二次确认), 不设沙箱字段, 单一生产网关。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "sheng_isv_key_config", autoResultMap = true)
public class ShengIsvKeyConfig extends MpBaseEntity implements ToResult<ShengIsvKeyConfigResult> {

    /// 产品编码(sheng_isv, 唯一定位键)
    /// @see cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum
    private String product;

    /// 服务商盛付通商户号(mchId)
    private String shengMchId;

    /// 服务商RSA私钥(PKCS8, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 盛付通验签公钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String shengpayPublicKey;

    @Override
    public ShengIsvKeyConfigResult toResult() {
        return ShengIsvKeyConfigConvert.CONVERT.toResult(this);
    }
}
