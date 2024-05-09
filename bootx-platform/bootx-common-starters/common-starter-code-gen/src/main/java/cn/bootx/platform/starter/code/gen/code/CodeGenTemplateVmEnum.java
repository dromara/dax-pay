package cn.bootx.platform.starter.code.gen.code;

import cn.bootx.platform.common.core.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 代码生成模板枚举
 *
 * @author xxm
 * @since 2022/2/17
 */
@Getter
@AllArgsConstructor
public enum CodeGenTemplateVmEnum {

    ENTITY("entity", "codegen/template/java/entity.java.vm", ".java"),
    MAPPER("mapper", "codegen/template/java/mapper.java.vm", "Mapper.java"),
    MANAGER("manager", "codegen/template/java/manager.java.vm", "Manager.java"),
    SERVICE("service", "codegen/template/java/service.java.vm", "Service.java"),
    CONTROLLER("controller", "codegen/template/java/controller.java.vm", "Controller.java"),
    DTO("dto", "codegen/template/java/dto.java.vm", "Dto.java"),
    PARAM("param", "codegen/template/java/param.java.vm", "Param.java"),
    CONVERT("convert", "codegen/template/java/convert.java.vm", "Convert.java"),
    API_V3("api_v3", "codegen/template/vue3/api.ts.vm", ".api.ts"),
    LIST_V3("list_v3", "codegen/template/vue3/list.vue.vm", "List.vue"),
    EDIT_V3("edit_v3", "codegen/template/vue3/edit.vue.vm", "Edit.vue");

    /** 名称 */
    private final String name;

    /** 模板地址 */
    private final String path;

    /** 文件名 */
    private final String fileSuffixName;

    public static CodeGenTemplateVmEnum findByName(String name) {
        return Arrays.stream(CodeGenTemplateVmEnum.values())
            .filter(e -> Objects.equals(name, e.getName()))
            .findFirst()
            .orElseThrow(() -> new BizException("不支持的模板类型"));
    }

}
