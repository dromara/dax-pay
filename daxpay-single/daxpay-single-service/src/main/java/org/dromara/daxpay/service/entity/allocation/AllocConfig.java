package org.dromara.daxpay.service.entity.allocation;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.convert.allocation.AllocConfigConvert;
import org.dromara.daxpay.service.param.allocation.AllocConfigParam;
import org.dromara.daxpay.service.result.allocation.AllocConfigResult;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 分账配置
 * @author xxm
 * @since 2024/10/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alloc_config")
public class AllocConfig extends MchAppBaseEntity implements ToResult<AllocConfigResult> {

    /** 是否自动分账 */
    private Boolean autoAlloc;

    /** 自动完结 */
    private Boolean autoFinish;

    /** 分账起始额 */
    private BigDecimal minAmount;

    /** 分账延迟时长(分钟) */
    private Integer delayTime;


    public Boolean getAutoFinish() {
        return Objects.equals(autoFinish, true);
    }

    public Boolean getAutoAlloc() {
        return Objects.equals(autoAlloc, true);
    }

    /**
     * 创建对象
     */
    public static AllocConfig init(AllocConfigParam param) {
        return AllocConfigConvert.CONVERT.toEntity(param);
    }
    /**
     * 转换
     */
    @Override
    public AllocConfigResult toResult() {
        return AllocConfigConvert.CONVERT.toResult(this);
    }
}
