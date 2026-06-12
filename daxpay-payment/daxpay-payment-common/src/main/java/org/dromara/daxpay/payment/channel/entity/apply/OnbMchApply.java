package org.dromara.daxpay.payment.channel.entity.apply;

import org.dromara.daxpay.platform.common.mybatisplus.function.ToResult;
import org.dromara.daxpay.platform.core.enums.channel.OnbApplyStatusEnum;
import org.dromara.daxpay.platform.core.enums.channel.OnbApplyTypeEnum;
import org.dromara.daxpay.payment.common.entity.merchant.MchBaseEntity;
import org.dromara.daxpay.payment.channel.convert.OnbMchApplyConvert;
import org.dromara.daxpay.platform.core.enums.channel.OnbApplySourceEnum;
import org.dromara.daxpay.payment.channel.param.apply.OnbMchApplyParam;
import org.dromara.daxpay.payment.channel.result.apply.OnbMchApplyResult;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// # 商户进件申请单
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_onb_mch_apply")
public class OnbMchApply extends MchBaseEntity implements ToResult<OnbMchApplyResult> {

    /// 申请名称
    private String name;

    /// 进件通道
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String channel;

    /// 进件类型
    /// @see OnbApplyTypeEnum
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String applyType;

    /// 外部通道状态
    private String outStatus;

    /// 来源
    /// @see OnbApplySourceEnum
    private String source;

    /// 最后提交时间
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private LocalDateTime lastSubmitTime;

    /// 状态
    /// @see OnbApplyStatusEnum
    private String status;

    /// 错误提示
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String errorMsg;

    public static OnbMchApply init(OnbMchApplyParam param){
        return OnbMchApplyConvert.CONVERT.toEntity(param);
    }

    /// 转换
    @Override
    public OnbMchApplyResult toResult() {
        return OnbMchApplyConvert.CONVERT.toResult(this);
    }
}

