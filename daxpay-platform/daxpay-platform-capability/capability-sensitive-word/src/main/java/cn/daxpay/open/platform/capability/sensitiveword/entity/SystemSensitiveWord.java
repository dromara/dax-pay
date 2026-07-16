package cn.daxpay.open.platform.capability.sensitiveword.entity;

import cn.daxpay.open.platform.capability.sensitiveword.convert.SystemSensitiveWordConvert;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordCategoryEnum;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordMatchModeEnum;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordStatusEnum;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 敏感词词库
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("system_sensitive_word")
public class SystemSensitiveWord extends MpBaseEntity implements ToResult<SystemSensitiveWordResult> {

    /// 敏感词原文（建议简体录入）
    private String word;

    /// 分类
    /// @see SensitiveWordCategoryEnum
    private String category;

    /// 匹配模式
    /// @see SensitiveWordMatchModeEnum
    private String matchMode;

    /// 处理级别（一期固定 reject）
    private String level;

    /// 状态
    /// @see SensitiveWordStatusEnum
    private String status;

    /// 备注
    private String remark;

    @Override
    public SystemSensitiveWordResult toResult() {
        return SystemSensitiveWordConvert.CONVERT.toResult(this);
    }
}

