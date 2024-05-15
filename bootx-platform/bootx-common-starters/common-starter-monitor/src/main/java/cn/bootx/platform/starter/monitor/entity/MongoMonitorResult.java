package cn.bootx.platform.starter.monitor.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * MongoDB监控信息
 *
 * @author xxm
 * @since 2022/6/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "MongoDB监控信息")
public class MongoMonitorResult {

}
