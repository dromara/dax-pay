package cn.daxpay.open.platform.core.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

/// # 基础返回类
///
@Getter
@Setter
public class BaseResult{

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "创建时间(UTC)")
    private OffsetDateTime createTime;
}
