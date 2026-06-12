package org.dromara.daxpay.payment.channel.bo.profile;

import org.dromara.daxpay.payment.common.result.MchBaseResult;
import org.dromara.daxpay.platform.core.enums.subject.SubjectTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 进件商户信息结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "进件商户信息")
public class OnbBaseProfileBo extends MchBaseResult {

    @Schema(description = "申请ID")
    private Long applyId;

    /// 主体类型
    /// @see SubjectTypeEnum
    @Schema(description = "主体类型")
    private String subjectType;

    @Schema(description = "商户名称")
    private String merchantName;

    @Schema(description = "商户简称")
    private String merchantShortName;

    /// 经营内容编号
    @Schema(description = "经营内容编号")
    private String businessContent;

    /// 经营内容名称（预留展示用）
    @Schema(description = "经营内容名称")
    private String businessContentName;
}

