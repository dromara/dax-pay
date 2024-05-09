package cn.bootx.platform.starter.code.gen.domain;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 代码生成所需的参数数据
 *
 * @author xxm
 * @since 2022/8/1
 */
@Data
@Accessors(chain = true)
public class CodeGenData {

    /** 表名 */
    private String tableName;

    /** 小驼峰的实体名称 */
    private String entityLowName;

    /** 大驼峰的实体名称 */
    private String entityUpName;

    /** 中划线类型的实体名称 */
    private String entityDashName;

    /** 基类 */
    private String baseClass;

    /** 编辑窗类型 */
    private String editType;

    /** 编辑窗类型 */
    private String deleteType;

    /** 核心包名(service/entity/dao等所在的包) */
    private String corePack;

    /** param参数类包名 */
    private String paramPack;

    /** dto参数类包名 */
    private String dtoPack;

    /** 控制器包名 */
    private String controllerPack;

    /** 请求地址 */
    private String requestPath;

    /** 前端接口文件所在目录 */
    private String vueApiPath;

    /** 创建人 */
    private String author;

    /** 创建时间 */
    private String datetime = DateUtil.formatDate(new Date());

    /** 备注 */
    private String comments;

    /** 行信息 */
    private List<CodeGenColumnData> columns;

    /**
     * 行信息
     *
     * @author xxm
     * @since 2022/8/1
     */
    @Data
    @Accessors(chain = true)
    public static class CodeGenColumnData {

        /** 列名称 */
        private String name;

        /** 类型 */
        private String sqlType;

        /** java类型 */
        private String javaType;

        /** ts类型 */
        private String tsType;

        /** 备注 */
        private String comments;

    }

}
