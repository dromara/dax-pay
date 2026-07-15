package cn.daxpay.open.plugin.easypay.entity;

import cn.daxpay.open.payment.common.entity.MchBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.plugin.easypay.convert.EasyPayCredentialConvert;
import cn.daxpay.open.plugin.easypay.result.config.EasyPayCredentialResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Objects;

/// # 易支付凭证（应用级）
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_easy_pay_credential")
public class EasyPayCredential extends MchBaseEntity implements ToResult<EasyPayCredentialResult> {

    /// 易支付商户号 pid
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Integer pid;

    /// 应用号（唯一，支付出口）
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String appId;

    /// 启用
    private Boolean enable;

    /// 开启 V1
    private Boolean enableV1;

    /// 开启 V2
    private Boolean enableV2;

    /// V1 MD5 密钥
    private String md5Key;

    /// V2 使用系统公私钥
    private Boolean useSystemKey;

    /// 商户 RSA 公钥
    private String publicKey;

    /// 平台公钥（非表字段，运行时填充）
    @TableField(exist = false)
    private String platformPublicKey;

    /// 平台私钥（非表字段，运行时填充）
    @TableField(exist = false)
    private String platformPrivateKey;

    public Boolean getEnableV1() {
        return Objects.equals(enableV1, Boolean.TRUE);
    }

    public Boolean getEnableV2() {
        return Objects.equals(enableV2, Boolean.TRUE);
    }

    public Boolean getEnable() {
        return Objects.equals(enable, Boolean.TRUE);
    }

    public Boolean getUseSystemKey() {
        return Objects.equals(useSystemKey, Boolean.TRUE);
    }

    @Override
    public EasyPayCredentialResult toResult() {
        return EasyPayCredentialConvert.CONVERT.toResult(this);
    }
}
