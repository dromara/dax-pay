package cn.daxpay.open.channel.wechat.entity.direct;

import cn.daxpay.open.channel.wechat.convert.direct.WechatDirectKeyConfigConvert;
import cn.daxpay.open.channel.wechat.result.direct.WechatDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信直连密钥配置
///
/// 直连商户维度的API V3密钥和证书配置，一个微信商户号共享一套密钥/证书，与具体应用无关，敏感字段加密存储。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "wechat_direct_key_config", autoResultMap = true)
public class WechatDirectKeyConfig extends MchBaseEntity implements ToResult<WechatDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// API V3密钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String apiKeyV3;

    /// 支付公钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /// 支付公钥ID
    private String publicKeyId;

    /// 商户私钥(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateKey;

    /// 商户证书(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String privateCert;

    /// 证书序列号
    private String certSerialNo;

    /// 转换
    @Override
    public WechatDirectKeyConfigResult toResult() {
        return WechatDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
