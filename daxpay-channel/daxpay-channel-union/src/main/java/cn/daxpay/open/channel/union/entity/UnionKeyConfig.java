package cn.daxpay.open.channel.union.entity;

import cn.daxpay.open.channel.union.convert.UnionKeyConfigConvert;
import cn.daxpay.open.channel.union.result.UnionKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 云闪付密钥配置
///
/// 商户维度的通道配置, 含银联商户号(merId)与 RSA2 三证书, 敏感字段(私钥证书/密码)加密存储。
/// merId 创建时录入不可修改, 三证书由密钥配置维护。
///
/// 银联 ACP 签名依赖证书(区别于银联商务的 HmacSHA256 无证书模式):
/// - **私钥证书**(PKCS12): 请求签名, 加密存储
/// - **中级证书 / 根证书**(X.509): 公开证书, 不加密
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "union_key_config", autoResultMap = true)
public class UnionKeyConfig extends MchBaseEntity implements ToResult<UnionKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 银联商户号(merId, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String merId;

    /// 签名类型(银联 ACP 固定 RSA2)
    private String signType;

    /// 是否证书签名(银联 ACP 固定 true)
    private boolean certSign;

    /// 应用私钥证书(Base64 编码的 PKCS12, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String keyPrivateCert;

    /// 私钥证书密码(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String keyPrivateCertPwd;

    /// 中级证书(Base64 编码的 X.509 DER)
    private String acpMiddleCert;

    /// 根证书(Base64 编码的 X.509 DER)
    private String acpRootCert;

    /// 是否沙箱环境
    private Boolean sandbox;

    @Override
    public UnionKeyConfigResult toResult() {
        return UnionKeyConfigConvert.CONVERT.toResult(this);
    }
}
