package cn.daxpay.multi.core.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 商户应用基础返回结果
 * @author xxm
 * @since 2024/7/20
 */
@Data
@Accessors(chain = true)
@Schema(title = "商户应用基础返回结果")
public class MchResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "应用AppId")
    private String appId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
