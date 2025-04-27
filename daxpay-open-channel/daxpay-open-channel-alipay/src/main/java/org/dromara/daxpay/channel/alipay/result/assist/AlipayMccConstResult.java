package org.dromara.daxpay.channel.alipay.result.assist;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 支付宝经营类目
 * @author xxm
 * @since 2024/11/11
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝经营类目")
public class AlipayMccConstResult {

    /** 类目 */
    @Schema(description = "类目")
    private String code;

    /** 上级类目 */
    @Schema(description = "上级类目")
    private String parentCode;

    /** 类目名称 */
    @Schema(description = "类目名称")
    private String name;

    /** 是否需要特殊资质 */
    @Schema(description = "是否需要特殊资质")
    private Boolean special;

    /** 子类目 */
    @Schema(description = "子类目")
    private List<AlipayMccConstResult> children;
}
