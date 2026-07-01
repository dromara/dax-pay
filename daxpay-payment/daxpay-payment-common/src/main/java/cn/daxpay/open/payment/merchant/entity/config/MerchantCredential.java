package cn.daxpay.open.payment.merchant.entity.config;

import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import cn.daxpay.open.platform.common.mybatisplus.handler.encrypt.DataEncryptTypeHandler;
import cn.daxpay.open.payment.common.entity.merchant.MchBaseEntity;
import cn.daxpay.open.payment.merchant.convert.config.MerchantCredentialConvert;
import cn.daxpay.open.payment.merchant.result.config.MerchantCredentialResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户API配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "mch_credential", autoResultMap = true)
public class MerchantCredential extends MchBaseEntity implements ToResult<MerchantCredentialResult> {

    /// 商户公钥
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /// 通信密钥
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String secretKey;

    /// 转换
    @Override
    public MerchantCredentialResult toResult() {
        return MerchantCredentialConvert.CONVERT.toResult(this);
    }
}
