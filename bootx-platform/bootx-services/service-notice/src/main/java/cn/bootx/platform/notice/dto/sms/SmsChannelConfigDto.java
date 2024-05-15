package cn.bootx.platform.notice.dto.sms;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 短信渠道配置
 * @author xxm
 * @since 2023-08-04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "短信渠道配置")
@Accessors(chain = true)
public class SmsChannelConfigDto extends BaseDto {

    @Schema(description = "渠道类型编码")
    private String code;
    @Schema(description = "渠道类型名称")
    private String name;
    @Schema(description = "状态")
    private String state;
    @Schema(description = "AccessKey")
    private String accessKey;
    @Schema(description = "AccessSecret")
    private String accessSecret;
    @Schema(description = "配置")
    private String config;
    @Schema(description = "排序")
    private Double sortNo;
    @Schema(description = "图片")
    private Long image;
    @Schema(description = "备注")
    private String remark;

}
