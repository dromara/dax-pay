package cn.bootx.platform.baseapi.core.template.entity;

import cn.bootx.platform.baseapi.core.template.convert.GeneralTemplateConvert;
import cn.bootx.platform.baseapi.dto.template.GeneralTemplateDto;
import cn.bootx.platform.baseapi.param.template.GeneralTemplateParam;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.table.modify.annotation.DbComment;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通用模板管理
 * @author xxm
 * @since 2023/8/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("base_general_template")
public class GeneralTemplate extends MpBaseEntity implements EntityBaseFunction<GeneralTemplateDto> {

    /** 模板名称 */
    @DbComment("模板名称")
    private String name;

    /** 模板代码 */
    @DbComment("模板代码")
    private String code;

    /**
     * 使用类型(导入/导出)
     * @see cn.bootx.platform.baseapi.code.GeneralTemplateCode
     */
    @DbComment("使用类型(导入/导出)")
    private String useType;

    /** 模板类型 */
    @DbComment("模板类型")
    private String fileType;

    /** 模板后缀名 */
    @DbComment("模板后缀名")
    private String fileSuffix;

    /**
     * 状态
     * @see cn.bootx.platform.baseapi.code.GeneralTemplateCode
     */
    @DbComment("状态")
    private String state;

    /** 文件ID */
    @DbComment("文件ID")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long fileId;

    /** 备注 */
    @DbComment("备注")
    private String remark;


    /** 创建对象 */
    public static GeneralTemplate init(GeneralTemplateParam in) {
        return GeneralTemplateConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public GeneralTemplateDto toDto() {
        return GeneralTemplateConvert.CONVERT.convert(this);
    }
}
