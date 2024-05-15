package cn.bootx.platform.iam.core.scope.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.CollUtil;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.scope.entity.DataRole;
import cn.bootx.platform.iam.dto.scope.DataRoleDto;
import cn.bootx.platform.iam.param.scope.DataRoleParam;
import cn.bootx.platform.iam.param.scope.DataRoleDeptParam;
import cn.bootx.platform.starter.data.perm.code.DataScopeEnum;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.iam.core.dept.event.DeptDeleteEvent;
import cn.bootx.platform.iam.core.scope.dao.DataRoleDeptManager;
import cn.bootx.platform.iam.core.scope.dao.DataRoleManager;
import cn.bootx.platform.iam.core.scope.dao.DataRoleUserManager;
import cn.bootx.platform.iam.core.scope.entity.DataRoleDept;
import cn.bootx.platform.iam.core.upms.dao.UserDataRoleManager;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cn.bootx.platform.iam.code.CachingCode.USER_DATA_SCOPE;

/**
 * 数据范围权限
 *
 * @author xxm
 * @since 2021/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataRoleService {

    private final DataRoleManager dataRoleManager;

    private final DataRoleUserManager dataRoleUserManager;

    private final DataRoleDeptManager dataRoleDeptManager;

    private final UserDataRoleManager userDataRoleManager;

    /**
     * 添加数据范围权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(DataRoleParam param) {
        DataRole dataRole = DataRole.init(param);
        dataRoleManager.save(dataRole);
    }

    /**
     * 修改
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DataRoleParam param) {
        DataRole dataRole = dataRoleManager.findById(param.getId()).orElseThrow(() -> new BizException("数据不存在"));
        BeanUtil.copyProperties(param, dataRole, CopyOptions.create().ignoreNullValue());
        dataRoleManager.updateById(dataRole);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        if (!dataRoleManager.existedById(id)) {
            throw new BizException("数据不存在");
        }
        if (userDataRoleManager.existsByDataRoleId(id)) {
            throw new BizException("该权限已经有用户在使用，无法删除");
        }
        dataRoleManager.deleteById(id);
        dataRoleUserManager.deleteByDataRoleId(id);
        dataRoleDeptManager.deleteByDataRoleId(id);
    }

    /**
     * 添加部门关联范围权限关系
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { USER_DATA_SCOPE }, allEntries = true)
    public void saveDeptAssign(DataRoleDeptParam param) {
        DataRole dataRole = dataRoleManager.findById(param.getDataRoleId()).orElseThrow(DataNotExistException::new);
        val scope = CollUtil.newArrayList(DataScopeEnum.DEPT_SCOPE.getCode(),
                DataScopeEnum.DEPT_AND_USER_SCOPE.getCode());
        if (!scope.contains(dataRole.getType())) {
            throw new BizException("非法操作");
        }

        // 先删后增
        List<DataRoleDept> dateScopedDeptList = dataRoleDeptManager.findByDateRoleId(param.getDataRoleId());
        List<Long> deptIdsByDb = dateScopedDeptList.stream().map(DataRoleDept::getDeptId).collect(Collectors.toList());

        // 要删除的
        List<Long> deptIds = param.getDeptIds();
        List<Long> deleteIds = dateScopedDeptList.stream()
            .filter(dataRoleDept -> !deptIds.contains(dataRoleDept.getDeptId()))
            .map(MpIdEntity::getId)
            .collect(Collectors.toList());
        // 要增加的
        List<DataRoleDept> dataRoleDepths = deptIds.stream()
            .filter(id -> !deptIdsByDb.contains(id))
            .map(deptId -> new DataRoleDept(param.getDataRoleId(), deptId))
            .collect(Collectors.toList());
        dataRoleDeptManager.deleteByIds(deleteIds);
        dataRoleDeptManager.saveAll(dataRoleDepths);
    }

    /**
     * 处理部门被删除的情况
     */
    @EventListener
    public void DeptDeleteEventListener(DeptDeleteEvent event) {
        dataRoleDeptManager.deleteByDeptIds(event.getDeptIds());
    }

    /**
     * 获取关联的部门id集合
     */
    public List<Long> findDeptIds(Long id) {
        return dataRoleDeptManager.findByDateRoleId(id)
            .stream()
            .map(DataRoleDept::getDeptId)
            .collect(Collectors.toList());
    }

    /**
     * 判断权限编码是否存在
     */
    public boolean existsByCode(String code) {
        return dataRoleManager.existsByCode(code);
    }

    /**
     * 判断权限编码是否存在
     */
    public boolean existsByCode(String code, Long id) {
        return dataRoleManager.existsByCode(code, id);
    }

    /**
     * name是否存在
     */
    public boolean existsByName(String name) {
        return dataRoleManager.existsByName(name);
    }

    /**
     * name是否存在
     */
    public boolean existsByName(String name, Long id) {
        return dataRoleManager.existsByName(name, id);
    }

    /**
     * 获取单条
     */
    public DataRoleDto findById(Long id) {
        return dataRoleManager.findById(id).map(DataRole::toDto).orElseThrow(() -> new BizException("数据不存在"));
    }

    /**
     * 分页
     */
    public PageResult<DataRoleDto> page(PageParam pageParam, DataRoleParam param) {
        return MpUtil.convert2DtoPageResult(dataRoleManager.page(MpUtil.getMpPage(pageParam, DataRole.class)));
    }

    /**
     * 列表查询
     */
    public List<DataRoleDto> findAll() {
        return ResultConvertUtil.dtoListConvert(dataRoleManager.findAll());
    }

}
