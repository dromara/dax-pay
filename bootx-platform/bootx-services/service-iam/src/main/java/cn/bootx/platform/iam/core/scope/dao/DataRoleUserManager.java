package cn.bootx.platform.iam.core.scope.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.iam.core.scope.entity.DataRoleUser;
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
public class DataRoleUserManager extends BaseManager<DataRoleUserMapper, DataRoleUser> {

    public void deleteByDataRoleId(Long dataRoleId) {
        this.deleteByField(DataRoleUser::getRoleId, dataRoleId);
    }

    public List<DataRoleUser> findByDateRoleId(Long dataRoleId) {
        return this.findAllByField(DataRoleUser::getRoleId, dataRoleId);
    }

}
