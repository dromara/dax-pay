package cn.daxpay.open.channel.sheng.entity;

import cn.daxpay.open.channel.sheng.convert.ShengKeyConfigConvert;
import cn.daxpay.open.channel.sheng.result.ShengKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 盛付通通道密钥配置
///
/// 通道商户维度的盛付通聚合配置, 含商户身份(mchId/sdpAppId)与 RSA 签名密钥对,
/// 敏感字段(商户私钥/盛付通公钥)通过 [DataEncryptTypeHandler] 加密入库。
/// channelMchNo 创建时录入不可修改; 密钥字段由密钥配置维护(空值不覆盖)。
///
/// 签名算法: SHA1withRSA, 商户私钥签名 / 盛付通公钥验签。
/// 盛付通不提供集测接口(2026-09-17 官方二次确认), 不设沙箱字段, 单一生产网关。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "sheng_key_config", autoResultMap = true)
public class ShengKeyConfig extends MchBaseEntity implements ToResult<ShengKeyConfigResult> {

    /// 通道商户号(关联 mch_channel_merchant, 创建后不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 盛付通商户号(mchId)
    private String shengMchId;

    /// 盛付通分配的 AppId(可空, 直连场景必填)
    private String sdpAppId;

    /// 商户RSA私钥(PKCS8, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 盛付通验签公钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String shengpayPublicKey;

    @Override
    public ShengKeyConfigResult toResult() {
        return ShengKeyConfigConvert.CONVERT.toResult(this);
    }
}
