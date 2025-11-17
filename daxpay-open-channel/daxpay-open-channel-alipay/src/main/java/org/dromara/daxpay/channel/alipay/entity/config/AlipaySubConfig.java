package org.dromara.daxpay.channel.alipay.entity.config;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import cn.bootx.platform.common.mybatisplus.handler.DataEncryptTypeHandler;
import org.dromara.daxpay.channel.alipay.convert.AlipaySubConfigConvert;
import org.dromara.daxpay.channel.alipay.result.config.AlipaySubConfigResult;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付宝特约商户配置
 * @author xxm
 * @since 2024/6/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "pay_alipay_sub_config", autoResultMap = true)
public class AlipaySubConfig extends MchAppBaseEntity implements ToResult<AlipaySubConfigResult> {

    /** 支付宝特约商户Token */
    @TableField(typeHandler = DataEncryptTypeHandler.class)
    private String appAuthToken;

    /** 是否启用 */
    private boolean enable;

    /**
     * 支付宝商家唯一识别码
     * 是商家与支付宝签约后，商家获得的支付宝商家唯一识别码，以 2088 开头的 16 位数字组成，在开放平台中账户中心获取
     */
    private String alipayUserId;

    @Override
    public AlipaySubConfigResult toResult() {
        return AlipaySubConfigConvert.CONVERT.toResult(this);
    }
}
