package cn.bootx.platform.iam.core.dept.service;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.iam.param.dept.DeptParam;
import cn.bootx.platform.iam.core.dept.dao.DeptManager;
import cn.bootx.platform.iam.core.dept.entity.Dept;
import cn.bootx.platform.iam.core.dept.event.DeptDeleteEvent;
import cn.bootx.platform.iam.dto.dept.DeptDto;
import cn.bootx.platform.iam.dto.dept.DeptTreeResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static cn.bootx.platform.iam.code.CachingCode.USER_DATA_SCOPE;

/**
 * @author xxm
 * @since 2020/5/7 17:42
 */
@Slf4j
@Service
@AllArgsConstructor
public class DeptService {

    private final DeptManager deptManager;

    private final DeptRuleService deptRuleService;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 添加部门
     */
    @CacheEvict(value = { USER_DATA_SCOPE }, allEntries = true)
    public DeptDto add(DeptParam param) {
        Dept dept = Dept.init(param);

        // 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
        Long parentId = param.getParentId();

        // 部门code生成
        dept.setOrgCode(deptRuleService.generateOrgCode(parentId));

        return deptManager.save(dept).toDto();
    }

    /**
     * 树状展示
     */
    public List<DeptTreeResult> tree() {
        List<Dept> list = deptManager.findAll();
        return deptRuleService.buildTreeList(list);
    }

    /**
     * 根据id查询
     */
    public DeptDto findById(Long id) {
        return deptManager.findById(id).map(Dept::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 编辑数据 编辑部门的部分数据,并保存到数据库
     */
    @CacheEvict(value = { USER_DATA_SCOPE }, allEntries = true)
    public DeptDto update(DeptParam param) {
        // 父机构ID 机构编码 不能修改
        Dept dept = deptManager.findById(param.getId()).orElseThrow(() -> new BizException("机构未找到"));
        param.setParentId(null);
        BeanUtil.copyProperties(param, dept, CopyOptions.create().ignoreNullValue());
        return deptManager.updateById(dept).toDto();
    }

    /**
     * 删除部门
     */
    @CacheEvict(value = { USER_DATA_SCOPE }, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        if (deptManager.existsParent(id)) {
            throw new BizException("存在子部门,无法直接删除");
        }
        deptManager.deleteById(id);
        // 发布部门修改事件
        eventPublisher.publishEvent(new DeptDeleteEvent(this, Collections.singletonList(id)));
    }

    /**
     * 删除部门及下级部门
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { USER_DATA_SCOPE }, allEntries = true)
    public boolean deleteAndChildren(Long id) {
        Dept dept = deptManager.findById(id).orElseThrow(DataNotExistException::new);
        if (Objects.isNull(dept)) {
            return false;
        }
        deptManager.deleteByOrgCode(dept.getOrgCode());

        // 删除下级部门
        deptManager.deleteById(id);
        return true;
    }

}
