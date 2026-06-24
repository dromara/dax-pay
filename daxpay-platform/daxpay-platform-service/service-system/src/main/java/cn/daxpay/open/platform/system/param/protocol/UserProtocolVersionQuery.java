package cn.daxpay.open.platform.system.param.protocol;

import cn.daxpay.open.platform.common.mybatisplus.query.entity.SortParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 用户协议版本查询
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "用户协议版本查询")
public class UserProtocolVersionQuery extends SortParam {

    /// 协议ID
    @Schema(description = "协议ID")
    private Long protocolId;

    /// 语言
    @Schema(description = "语言")
    private String language;

    /// 状态
    @Schema(description = "状态")
    private String status;
}
