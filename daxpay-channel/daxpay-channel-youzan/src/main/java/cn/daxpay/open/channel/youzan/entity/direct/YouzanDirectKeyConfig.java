package cn.daxpay.open.channel.youzan.entity.direct;

import cn.daxpay.open.channel.youzan.convert.direct.YouzanDirectKeyConfigConvert;
import cn.daxpay.open.channel.youzan.result.direct.YouzanDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 有赞直连配置
///
/// 直连商户维度的通道配置: 高汇通商户号(merchant_id)、商户 RSA 私钥、有赞支付平台公钥与公钥唯一标识
/// (私钥/公钥/PEM Base64 文本, 敏感字段加密存储)。
/// merchant_id 创建时录入, 密钥三元组(私钥/平台公钥/公钥标识)由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "youzan_direct_key_config", autoResultMap = true)
public class YouzanDirectKeyConfig extends MchBaseEntity implements ToResult<YouzanDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 高汇通商户号(mch_id, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String merchantId;

    /// 商户 RSA 私钥(Base64/PEM, PKCS8 与 PKCS1 均兼容, 请求签名用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 有赞支付平台 RSA 公钥(Base64/PEM, X509, 响应/回调验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformPublicKey;

    /// 公钥唯一标识(请求头 publickeyid, 与平台公钥成对, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKeyId;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public YouzanDirectKeyConfigResult toResult() {
        return YouzanDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
