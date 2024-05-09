package cn.bootx.platform.baseapi.core.chinaword.entity;

import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.platform.baseapi.core.chinaword.convert.ChinaWordConvert;
import cn.bootx.platform.baseapi.dto.chinaword.ChinaWordDto;
import cn.bootx.platform.baseapi.param.chinaword.ChinaWordParam;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpDelEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 敏感词
 * @author xxm
 * @since 2023/8/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "base_china_word",autoResultMap = true)
public class ChinaWord extends MpDelEntity implements EntityBaseFunction<ChinaWordDto> {

    /** 敏感词 */
    @DbColumn(comment = "敏感词")
    private String word;
    /** 类型 */
    @DbColumn(comment = "分类")
    private String type;
    /** 描述 */
    @DbColumn(comment = "描述")
    private String description;
    /** 是否启用 */
    @DbColumn(comment = "是否启用")
    private Boolean enable;
    @Schema(description = "是否是白名单名词")
    private Boolean white;

    /** 创建对象 */
    public static ChinaWord init(ChinaWordParam in) {
        return ChinaWordConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public ChinaWordDto toDto() {
        return ChinaWordConvert.CONVERT.convert(this);
    }
}
