package cn.daxpay.open.channel.baofu.entity.direct;

import cn.daxpay.open.channel.baofu.convert.direct.BaofuDirectKeyConfigConvert;
import cn.daxpay.open.channel.baofu.result.direct.BaofuDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 宝付直连配置
///
/// 直连商户维度的通道配置: 宝付商户号(merId)/终端号(terId)与两份证书
/// (商户 pfx + 宝付平台 cer, Base64 文本, 敏感字段加密存储)及聚合交易商户号(subMchId)。
/// merId/terId 创建时录入不可修改, 证书与 subMchId 由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "baofu_direct_key_config", autoResultMap = true)
public class BaofuDirectKeyConfig extends MchBaseEntity implements ToResult<BaofuDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 宝付商户号(merId, 收款线, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String merId;

    /// 宝付终端号(terId, 收款线, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String terId;

    /// 聚合交易商户号(subMchId, 在微信/支付宝报备的商户号, 聚合收银台场景上送)
    private String subMchId;

    /// 商户证书 pfx 内容(Base64, PKCS12, 请求签名用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantCertPfx;

    /// 商户证书密码(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantCertPassword;

    /// 宝付平台证书 cer 内容(Base64/PEM, 响应与回调验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformCert;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public BaofuDirectKeyConfigResult toResult() {
        return BaofuDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
