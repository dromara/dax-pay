package org.dromara.daxpay.payment.merchant.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.common.mybatisplus.handler.DataEncryptTypeHandler;
import org.dromara.daxpay.payment.merchant.common.entity.MchBaseEntity;
import org.dromara.daxpay.payment.merchant.convert.config.MerchantCredentialConvert;
import org.dromara.daxpay.payment.merchant.result.config.MerchantCredentialResult;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户API配置
 * @author xxm
 * @since 2025/9/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_merchant_credential", autoResultMap = true)
public class MerchantCredential extends MchBaseEntity implements ToResult<MerchantCredentialResult> {

    /** 服务商号 */
    private String isvNo;

    /** 商户公钥 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String publicKey;

    /** 通信密钥 */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String secretKey;

    /**
     * 转换
     */
    @Override
    public MerchantCredentialResult toResult() {
        return MerchantCredentialConvert.CONVERT.toResult(this);
    }
}
