package cn.bootx.platform.notice.param.sms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 短信渠道配置
 * @author xxm
 * @since 2023-08-04
 */
@Data
@Schema(title = "短信渠道配置")
@Accessors(chain = true)
public class SmsChannelConfigParam {

    @Schema(description= "主键")
    private Long id;

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
    @Schema(description = "图片")
    private Long image;
    @Schema(description = "排序")
    private Double sortNo;
    @Schema(description = "配置")
    private String config;
    @Schema(description = "备注")
    private String remark;

}
