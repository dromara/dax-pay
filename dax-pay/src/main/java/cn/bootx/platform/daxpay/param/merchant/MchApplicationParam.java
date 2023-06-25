package cn.bootx.platform.daxpay.param.merchant;

import cn.bootx.platform.common.core.annotation.QueryParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商户应用
 *
 * @author xxm
 * @since 2023-05-19
 */
@Data
@QueryParam(type = QueryParam.CompareTypeEnum.LIKE)
@Schema(title = "商户应用")
@Accessors(chain = true)
public class MchApplicationParam {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "应用编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "商户号")
    private String mchCode;

    @Schema(description = "状态类型")
    private String state;

    @Schema(description = "备注")
    private String remark;

}
