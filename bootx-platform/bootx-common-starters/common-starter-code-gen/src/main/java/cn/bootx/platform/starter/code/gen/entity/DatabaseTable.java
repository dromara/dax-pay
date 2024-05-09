package cn.bootx.platform.starter.code.gen.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

/**
 * 表信息
 *
 * @author xxm
 * @since 2022/1/27
 */
@Data
@Accessors(chain = true)
@FieldNameConstants
public class DatabaseTable {

    /** 表名 */
    private String tableName;

    /** 引擎类型 */
    private String engine;

    /** 表表述 */
    private String tableComment;

    /** 创建时间 */
    private LocalDateTime createTime;

}
