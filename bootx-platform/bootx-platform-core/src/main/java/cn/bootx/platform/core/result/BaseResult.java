package cn.bootx.platform.core.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 基础返回类
 * @author xxm
 * @since 2024/8/12
 */
@Getter
@Setter
public class BaseResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
