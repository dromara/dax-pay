package cn.bootx.platform.iam.core.scope.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.core.scope.entity.DataRoleDept;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xxm
 * @since 2021/12/23
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class DataRoleDeptManager extends BaseManager<DataRoleDeptMapper, DataRoleDept> {

    public void deleteByDataRoleId(Long dataRoleId) {
        this.deleteByField(DataRoleDept::getRoleId, dataRoleId);
    }

    /**
     * 根据部门进行删除
     */
    public void deleteByDeptIds(List<Long> deptIds) {
        this.deleteByFields(DataRoleDept::getDeptId, deptIds);
    }

    public List<DataRoleDept> findByDateRoleId(Long dataRoleId) {
        return this.findAllByField(DataRoleDept::getRoleId, dataRoleId);
    }

}
