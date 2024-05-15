package cn.bootx.platform.starter.audit.log.core.db.entity;

import cn.bootx.platform.starter.audit.log.core.db.convert.LogConvert;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.starter.audit.log.dto.DataVersionLogDto;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 数据版本日志
 *
 * @author xxm
 * @since 2022/1/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("starter_audit_data_version")
public class DataVersionLogDb extends MpIdEntity implements EntityBaseFunction<DataVersionLogDto> {

    @Schema(description = "表名称")
    private String tableName;

    @Schema(description = "数据名称")
    private String dataName;

    @Schema(description = "数据主键")
    private String dataId;

    @Schema(description = "数据内容")
    private String dataContent;

    @Schema(description = "本次变动的数据内容")
    private Object changeContent;

    @Schema(description = "数据版本")
    private Integer version;

    @Schema(description = "创建者ID")
    private Long creator;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Override
    public DataVersionLogDto toDto() {
        return LogConvert.CONVERT.convert(this);
    }

}
