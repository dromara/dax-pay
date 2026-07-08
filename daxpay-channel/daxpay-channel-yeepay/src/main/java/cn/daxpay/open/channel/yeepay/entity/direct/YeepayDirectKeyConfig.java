package cn.daxpay.open.channel.yeepay.entity.direct;

import cn.daxpay.open.channel.yeepay.convert.direct.YeepayDirectKeyConfigConvert;
import cn.daxpay.open.channel.yeepay.result.direct.YeepayDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 易宝直连配置
///
/// 直连商户维度的通道配置, 含商户身份(merchantNo/yopIsvNo)与 YOP SDK 密钥(appKey/privateKey/yopPublicKey),
/// 敏感字段(私钥/appKey)加密存储。微信场景相关配置(wxAppId/wxAppSecret)可选。
/// merchantNo/yopIsvNo 创建时录入不可修改, 密钥由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "yeepay_direct_key_config", autoResultMap = true)
public class YeepayDirectKeyConfig extends MchBaseEntity implements ToResult<YeepayDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 易宝商户号(merchantNo, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String merchantNo;

    /// 易宝服务商商编(parentMerchantNo / yopIsvNo, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String yopIsvNo;

    /// 通道应用 AppKey(YOP 应用标识, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appKey;

    /// 商户 RSA 私钥(PEM PKCS#8, SDK 签名用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// 易宝平台 RSA 公钥(PEM, SDK 验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String yopPublicKey;

    /// 微信 AppId(微信 H5/JSAPI 场景用, 可空)
    private String wxAppId;

    /// 微信 AppSecret(微信场景用, 可空, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String wxAppSecret;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public YeepayDirectKeyConfigResult toResult() {
        return YeepayDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
