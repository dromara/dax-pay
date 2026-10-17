package cn.daxpay.open.channel.sand.entity.direct;

import cn.daxpay.open.channel.sand.convert.direct.SandDirectKeyConfigConvert;
import cn.daxpay.open.channel.sand.result.direct.SandDirectKeyConfigResult;
import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 杉德直连配置
///
/// 直连商户维度的通道配置: 杉德商户编号(mid) + 市场产品编码(QZF/CSDB) +
/// 商户 pfx 私钥证书与密码 + 杉德公钥证书(均为 Base64 文本, 敏感字段加密存储)。
/// mid 与市场产品创建时录入, 证书由密钥配置维护, 生产/沙箱双环境分别存一份。
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "sand_direct_key_config", autoResultMap = true)
public class SandDirectKeyConfig extends MchBaseEntity implements ToResult<SandDirectKeyConfigResult> {

    /// 通道商户号
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channelMchNo;

    /// 杉德商户编号(mid, 创建时录入不可修改)
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String mid;

    /// 商户私钥证书 pfx 内容(Base64, PKCS12, 签名与响应 encryptKey 解密用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantCertPfx;

    /// 商户私钥证书密码(加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String merchantCertPassword;

    /// 杉德平台公钥证书 cer 内容(Base64/PEM, 请求加密与响应/回调验签用, 加密存储)
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String sandCert;

    /// 市场产品编码(QZF 标准线上收款 / CSDB 企业杉德宝)
    private String marketProduct;

    /// 是否沙箱环境(true 走 UAT 网关)
    private Boolean sandbox;

    @Override
    public SandDirectKeyConfigResult toResult() {
        return SandDirectKeyConfigConvert.CONVERT.toResult(this);
    }
}
