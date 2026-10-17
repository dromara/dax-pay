package cn.daxpay.open.channel.yisheng.entity.direct;

import cn.daxpay.open.channel.yisheng.convert.direct.YishengDirectKeyConfigConvert;
import cn.daxpay.open.channel.yisheng.result.direct.YishengDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 易生直连配置
///
/// 直连商户维度的通道配置: 合作伙伴ID(partner/reqId)、商户编号(merchant_id)与
/// RSA 密钥对(商户私钥/易生平台公钥, Base64/PEM 文本, 敏感字段加密存储)。
/// partner/merchantId 创建时录入, 密钥由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "yisheng_direct_key_config", autoResultMap = true)
public class YishengDirectKeyConfig extends MchBaseEntity implements ToResult<YishengDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 合作伙伴ID(reqId, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String partner;

    /// 易生商户编号(merchant_id, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String merchantId;

    /// 商户 RSA 私钥(Base64/PEM, PKCS8 与 PKCS1 均兼容, 请求签名用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 易生平台 RSA 公钥(Base64/PEM, X509, 响应/回调验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformPublicKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public YishengDirectKeyConfigResult toResult() {
        return YishengDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
