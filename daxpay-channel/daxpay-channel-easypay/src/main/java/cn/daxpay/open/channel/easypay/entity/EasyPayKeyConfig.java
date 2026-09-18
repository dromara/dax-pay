package cn.daxpay.open.channel.easypay.entity;

import cn.daxpay.open.channel.easypay.convert.EasyPayKeyConfigConvert;
import cn.daxpay.open.channel.easypay.result.EasyPayKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 易支付通道密钥配置
///
/// 通道商户维度的易支付三方聚合平台对接配置, 含平台地址/商户ID(pid)与 RSA 签名密钥对,
/// 敏感字段(商户私钥/平台公钥)通过 [DataEncryptTypeHandler] 加密入库。
/// channelMchNo 创建时录入不可修改; 密钥字段由密钥配置维护(空值不覆盖)。
///
/// 签名算法: SHA256withRSA, 商户私钥签名 / 易支付平台公钥验签。
/// 易支付不提供集测环境, 不设沙箱字段, 对接配置中的平台地址。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "easy_pay_key_config", autoResultMap = true)
public class EasyPayKeyConfig extends MchBaseEntity implements ToResult<EasyPayKeyConfigResult> {

    /// 通道商户号(关联 mch_channel_merchant, 创建后不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 易支付平台网关地址(如 https://pay.xxx.com)
    private String serverUrl;

    /// 易支付商户ID(pid, 上游平台分配)
    private String partnerId;

    /// 商户RSA私钥(PKCS8, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 易支付平台验签公钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformPublicKey;

    @Override
    public EasyPayKeyConfigResult toResult() {
        return EasyPayKeyConfigConvert.CONVERT.toResult(this);
    }
}
