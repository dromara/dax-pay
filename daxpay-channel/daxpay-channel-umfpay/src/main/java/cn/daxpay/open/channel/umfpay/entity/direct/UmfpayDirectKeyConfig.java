package cn.daxpay.open.channel.umfpay.entity.direct;

import cn.daxpay.open.channel.umfpay.convert.direct.UmfpayDirectKeyConfigConvert;
import cn.daxpay.open.channel.umfpay.result.direct.UmfpayDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 联动优势直连配置
///
/// 直连商户维度的通道配置: 联动优势商户号(mer_id)与密钥对(商户私钥 + 平台公钥, PEM/Base64 文本, 敏感字段加密存储)。
/// mer_id 创建时录入, 密钥由密钥配置维护。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "umfpay_direct_key_config", autoResultMap = true)
public class UmfpayDirectKeyConfig extends MchBaseEntity implements ToResult<UmfpayDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 联动优势商户号(mer_id, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String merId;

    /// 商户私钥(PEM/Base64 文本, 兼容 PKCS1/PKCS8, 请求/应答签名用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantPrivateKey;

    /// 联动优势平台公钥(PEM/Base64 文本, X509 格式, 响应/回调验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String platformPublicKey;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public UmfpayDirectKeyConfigResult toResult() {
        return UmfpayDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
