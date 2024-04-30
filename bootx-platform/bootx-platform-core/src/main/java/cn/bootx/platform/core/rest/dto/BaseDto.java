package cn.bootx.platform.core.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础Dto
 *
 * @author xxm
 * @since 2020/6/1 16:12
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseDto {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后修改时间")
    private LocalDateTime lastModifiedTime;

    @Schema(description = "版本号")
    private Integer version;

}
