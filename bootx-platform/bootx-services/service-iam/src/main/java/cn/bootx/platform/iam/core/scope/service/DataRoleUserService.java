package cn.bootx.platform.iam.core.scope.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.function.CollectorsFunction;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.iam.core.scope.dao.DataRoleManager;
import cn.bootx.platform.iam.core.scope.dao.DataRoleUserManager;
import cn.bootx.platform.iam.core.scope.entity.DataRole;
import cn.bootx.platform.iam.core.scope.entity.DataRoleUser;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.iam.dto.scope.DataRoleUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.bootx.platform.iam.code.CachingCode.USER_DATA_SCOPE;
import static cn.bootx.platform.starter.data.perm.code.DataScopeEnum.DEPT_AND_USER_SCOPE;
import static cn.bootx.platform.starter.data.perm.code.DataScopeEnum.USER_SCOPE;

/**
 * 数据范围权限限定用户级别
 *
 * @author xxm
 * @since 2022/1/4
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataRoleUserService {

    private final DataRoleManager dataRoleManager;

    private final DataRoleUserManager dataRoleUserManager;

    private final UserInfoManager userInfoManager;

    /**
     * 关联用户列表
     */
    public List<DataRoleUserDto> findUsersByDataRoleId(Long dataRoleId) {
        Map<Long, DataRoleUser> dataScopeUserMap = dataRoleUserManager.findByDateRoleId(dataRoleId)
            .stream()
            .collect(Collectors.toMap(DataRoleUser::getUserId, Function.identity(), CollectorsFunction::retainLatest));
        // 查询出用户id
        List<Long> userIds = dataScopeUserMap.values()
            .stream()
            .map(DataRoleUser::getUserId)
            .collect(Collectors.toList());
        // 查询出用户
        List<UserInfo> userInfos = userInfoManager.findAllByIds(userIds);

        return userInfos.stream()
            .map(userInfo -> new DataRoleUserDto().setId(dataScopeUserMap.get(userInfo.getId()).getId())
                .setUserId(userInfo.getId())
                .setUsername(userInfo.getUsername())
                .setName(userInfo.getName()))
            .collect(Collectors.toList());
    }

    /**
     * 添加用户范围权限关联关系
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { USER_DATA_SCOPE }, allEntries = true)
    public void saveUserAssign(Long dataRoleId, List<Long> userIds) {
        DataRole dataRole = dataRoleManager.findById(dataRoleId).orElseThrow(() -> new BizException("数据不存在"));

        val scope = CollUtil.newArrayList(USER_SCOPE.getCode(),
                DEPT_AND_USER_SCOPE.getCode());
        if (!scope.contains(dataRole.getType())) {
            throw new BizException("非法操作");
        }
        List<Long> dataScopeUserIds = dataRoleUserManager.findByDateRoleId(dataRoleId)
            .stream()
            .map(DataRoleUser::getUserId)
            .collect(Collectors.toList());

        List<DataRoleUser> dataScopeUsers = userIds.stream()
            .filter(userId -> !dataScopeUserIds.contains(userId))
            .map(userId -> new DataRoleUser(dataRoleId, userId))
            .collect(Collectors.toList());
        dataRoleUserManager.saveAll(dataScopeUsers);
    }

    /**
     * 批量删除
     */
    @CacheEvict(value = { USER_DATA_SCOPE }, allEntries = true)
    public void deleteBatch(List<Long> ids) {
        dataRoleUserManager.deleteByIds(ids);
    }

}
