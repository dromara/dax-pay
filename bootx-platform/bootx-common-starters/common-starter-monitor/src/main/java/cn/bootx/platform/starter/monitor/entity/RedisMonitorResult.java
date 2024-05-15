package cn.bootx.platform.starter.monitor.entity;

import cn.bootx.platform.common.core.rest.dto.KeyValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Properties;

/**
 * Redis监控信息
 *
 * @author xxm
 * @since 2022/6/12
 */
@Data
@Schema(title = "Redis监控信息")
public class RedisMonitorResult {

    @Schema(description = "Redis系统信息")
    private Properties info;

    @Schema(description = "命令统计")
    private List<KeyValue> commandStats;

    @Schema(description = "key数量")
    private Long dbSize;

}
