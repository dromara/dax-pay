package cn.daxpay.open.platform.capability.sensitiveword.entity;

import cn.daxpay.open.platform.capability.sensitiveword.convert.SystemSensitiveWordHitConvert;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordSceneEnum;
import cn.daxpay.open.platform.capability.sensitiveword.enums.SensitiveWordSourceEnum;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordHitResult;
import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import cn.daxpay.open.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/// # 敏感词命中记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@FieldNameConstants
@Accessors(chain = true)
@TableName("system_sensitive_word_hit")
public class SystemSensitiveWordHit extends MpBaseEntity implements ToResult<SystemSensitiveWordHitResult> {

    /// 关联词库 ID（可空）
    private Long wordId;

    /// 命中词快照
    private String hitWord;

    /// 原文摘要
    private String contentPreview;

    /// 场景
    /// @see SensitiveWordSceneEnum
    private String scene;

    /// 来源
    /// @see SensitiveWordSourceEnum
    private String source;

    /// 商户号
    private String mchNo;

    /// 应用号
    private String appId;

    /// 操作人用户 ID
    private Long operatorId;

    /// 客户端 IP
    private String clientIp;

    /// 请求 path
    private String requestPath;

    /// 备注
    private String remark;

    @Override
    public SystemSensitiveWordHitResult toResult() {
        return SystemSensitiveWordHitConvert.CONVERT.toResult(this);
    }
}

