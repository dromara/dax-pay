package cn.bootx.platform.starter.file.entity;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.table.modify.annotation.DbColumn;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 数据库存储上传的文件数据
 * @author xxm
 * @since 2023/8/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("starter_file_data")
public class JdbcFileData extends MpIdEntity {

    /** base64方式存储 */
    @DbColumn(comment = "base64方式存储")
    private String base64;

    /** 数据方式存储 */
    @DbColumn(comment = "数据方式存储")
    private byte[] data;
}
