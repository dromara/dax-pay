package org.dromara.daxpay.service.common.entity.config;

import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.service.common.convert.PlatformConfigConvert;
import org.dromara.daxpay.service.common.result.config.PlatformBasicConfigResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 管理平台基础配置
 * @author xxm
 * @since 2024/6/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_platform_basic_config")
public class PlatformBasicConfig extends MpBaseEntity implements ToResult<PlatformBasicConfigResult> {

    /** 全局单笔限额 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal singleLimitAmount;

    /** 每月累计限额 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal monthlyLimitAmount;

    /** 每日限额 */
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private BigDecimal dailyLimitAmount;

    /**
     * 转换
     */
    @Override
    public PlatformBasicConfigResult toResult() {
        return PlatformConfigConvert.CONVERT.toResult(this);
    }
}
