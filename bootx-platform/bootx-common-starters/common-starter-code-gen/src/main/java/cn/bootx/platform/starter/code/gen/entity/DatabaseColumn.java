package cn.bootx.platform.starter.code.gen.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数据库行信息
 *
 * @author xxm
 * @since 2022/1/27
 */
@Data
@Accessors(chain = true)
public class DatabaseColumn {

    /** 行名称 */
    private String columnName;

    /** 数据类型 */
    private String dataType;

    /** 行描述 */
    private String columnComment;

    /** 主键类型 */
    private String columnKey;

}
