package cn.bootx.platform.starter.data.perm.scope;

/**
 * 数据范围权限业务实现接口
 *
 * @author xxm
 * @since 2021/12/22
 */
public interface DataPermScopeHandler {

    /**
     * 返回数据权限查询条件的集合
     */
    DataPermScope getDataPermScope();

}
