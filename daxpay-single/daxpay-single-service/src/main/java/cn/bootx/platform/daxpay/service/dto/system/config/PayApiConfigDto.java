package cn.bootx.platform.daxpay.service.dto.system.config;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.service.code.PayApiCallBackTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付接口管理
 * @author xxm
 * @since 2024/1/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付接口管理")
public class PayApiConfigDto extends BaseDto {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "接口地址")
    private String api;

    @Schema(description = "名称")
    private String name;

    /**
     * 是否支持回调通知
     * @see PayApiCallBackTypeEnum
     */
    @Schema(description = "是否支持回调通知")
    private boolean noticeSupport;

    @Schema(description = "是否启用")
    private boolean enable;

    @Schema(description = "是否开启回调通知")
    private boolean notice;

    @Schema(description = "默认回调地址")
    private String noticeUrl;

    @Schema(description = "请求参数是否签名")
    private boolean reqSign;

    @Schema(description = "响应参数是否签名")
    private boolean resSign;

    @Schema(description = "回调信息是否签名")
    private boolean noticeSign;

    @Schema(description = "是否记录请求的信息")
    private boolean record;

    @Schema(description = "备注")
    private String remark;

}
