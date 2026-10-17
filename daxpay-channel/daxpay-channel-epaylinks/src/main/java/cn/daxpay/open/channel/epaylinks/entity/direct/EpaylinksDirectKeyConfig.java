package cn.daxpay.open.channel.epaylinks.entity.direct;

import cn.daxpay.open.channel.epaylinks.convert.direct.EpaylinksDirectKeyConfigConvert;
import cn.daxpay.open.channel.epaylinks.result.direct.EpaylinksDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 易票联直连配置
///
/// 直连商户维度的通道配置: 易票联商户编号(customerCode)、上游商户号与
/// 密钥三元组(证书序列号 sign_no + 商户私钥 + 平台公钥证书, 文本形态兼容 PEM/Base64, 敏感字段加密存储)。
/// customerCode/channelMchtNo 创建时录入, 密钥三元组由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "epaylinks_direct_key_config", autoResultMap = true)
public class EpaylinksDirectKeyConfig extends MchBaseEntity implements ToResult<EpaylinksDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 易票联商户编号(customerCode, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String customerCode;

    /// 上游商户号(可空, 创建时录入; 设置后下单上送 channelMchtNo 指定受理商户)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchtNo;

    /// 证书序列号(sign_no, 与商户私钥证书配套, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String signNo;

    /// 商户私钥(PEM/Base64, 兼容 PKCS1/PKCS8, 请求签名用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 易票联平台公钥证书(efps.cer, PEM/Base64, 回调验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformCert;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public EpaylinksDirectKeyConfigResult toResult() {
        return EpaylinksDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
