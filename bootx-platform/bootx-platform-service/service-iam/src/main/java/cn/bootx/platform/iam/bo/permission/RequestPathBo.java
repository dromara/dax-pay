package cn.bootx.platform.iam.bo.permission;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 请求路径信息类
 * @author xxm
 * @since 2024/7/4
 */
@Data
@Accessors(chain = true)
public class RequestPathBo {

    /** 主键, 只有通过数据库数据生成的才会有值 */
    private Long id;

    /** 功能模块编码 */
    private String moduleCode;

    /** 功能模块名称 */
    private String moduleName;

    /** 功能分组名称 */
    private String groupName;

    /** 功能分组code */
    private String groupCode;

    /** 名称 */
    private String name;

    /** 请求路径 */
    private String path;

    /** 请求类型 */
    private String method;


    public RequestPathBo setModuleName(String moduleName) {
        if (StrUtil.isNotBlank(moduleName)) {
            this.moduleName = moduleName;
        }
        return this;
    }

    public RequestPathBo setGroupName(String groupName) {
        if (StrUtil.isNotBlank(groupName)){
            this.groupName = groupName;
        }
        return this;
    }

}
