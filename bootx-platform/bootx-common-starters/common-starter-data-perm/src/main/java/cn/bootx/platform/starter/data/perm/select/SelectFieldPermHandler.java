package cn.bootx.platform.starter.data.perm.select;

import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.List;

/**
 * 数据字段权限业务实现接口
 *
 * @author xxm
 * @since 2023/1/21
 */
public interface SelectFieldPermHandler {

    /**
     * 将没有权限的SQL查询字段给过滤掉
     * @param selectItems SQL查询字段
     */
    List<SelectItem> filterFields(List<SelectItem> selectItems, String tableName);

}
