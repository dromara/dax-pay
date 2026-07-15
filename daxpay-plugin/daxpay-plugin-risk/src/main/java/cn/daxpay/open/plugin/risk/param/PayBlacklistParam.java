package cn.daxpay.open.plugin.risk.param;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 黑名单参数
///
@Data
@Accessors(chain = true)
@Schema(title = "黑名单参数")
public class PayBlacklistParam {

    @Schema(description = "主键")
    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    private Long id;

    /// 类型：ip / open_id
    @Schema(description = "类型")
    @NotBlank(message = "{validation.field.type.notBlank}", groups = ValidationGroup.add.class)
    @Size(max = 32, message = "{validation.field.product.size}")
    private String type;

    /// 名单值
    @Schema(description = "名单值")
    @NotBlank(message = "{validation.field.type.notBlank}", groups = ValidationGroup.add.class)
    @Size(max = 128, message = "{validation.field.openId.size}")
    private String value;

    @Schema(description = "通道族")
    @Size(max = 32, message = "{validation.field.channel.size}")
    private String channel;

    @Schema(description = "通道应用AppId")
    @Size(max = 64, message = "{validation.field.channelAppId.size}")
    private String channelAppId;

    /// 状态：enable / disable
    @Schema(description = "状态")
    @Size(max = 16, message = "{validation.field.status.notBlank}")
    private String status;

    @Schema(description = "拉黑原因")
    @Size(max = 255, message = "{validation.field.remark.size}")
    private String reason;

    @Schema(description = "过期时间(北京时间 yyyy-MM-dd HH:mm:ss，空=永久)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private OffsetDateTime expireTime;

    @Schema(description = "备注")
    @Size(max = 255, message = "{validation.field.remark.size}")
    private String remark;
}
