package cn.daxpay.open.platform.system.param.protocol;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 用户协议项查询
///
@Data
@Accessors(chain = true)
@Schema(title = "用户协议项查询")
public class UserProtocolItemQuery {

    /// 协议id
    @Schema(description = "协议id")
    private Long protocolId;
}

