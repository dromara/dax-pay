package cn.bootx.platform.iam.core.scope.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.core.scope.entity.DataRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @author xxm
 * @since 2021/12/23
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class DataRoleManager extends BaseManager<DataRoleMapper, DataRole> {

    public boolean existsByCode(String code) {
        return this.existedByField(DataRole::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return this.existedByField(DataRole::getCode, code, id);
    }

    public boolean existsByName(String name) {
        return this.existedByField(DataRole::getName, name);
    }

    public boolean existsByName(String name, Long id) {
        return this.existedByField(DataRole::getName, name, id);
    }

}
