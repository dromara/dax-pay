package cn.bootx.platform.iam.service.permission;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.BizException;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.iam.dao.permission.PermPathManager;
import cn.bootx.platform.iam.dao.upms.RolePathManager;
import cn.bootx.platform.iam.entity.permission.PermPath;
import cn.bootx.platform.iam.param.permission.PermPathParam;
import cn.bootx.platform.iam.result.permission.PermPathResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求权限
 *
 * @author xxm
 * @since 2020/5/10 23:20
 */
@Service
@AllArgsConstructor
public class PermPathService {

    private final PermPathManager permPathManager;

    private final RolePathManager rolePathManager;

    /**
     * 添加权限信息
     */
    public void add(PermPathParam param) {
        PermPath permPath = PermPath.init(param);
        permPathManager.save(permPath);
    }

    /**
     * 更新权限信息
     */
    public void update(PermPathParam param) {
        PermPath permPath = permPathManager.findById(param.getId()).orElseThrow(() -> new BizException("信息不存在"));
        BeanUtil.copyProperties(param, permPath, CopyOptions.create().ignoreNullValue());
        // 编辑过的信息不再作为系统生成的
        permPath.setGenerate(false);
        permPathManager.updateById(permPath);
    }


    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        rolePathManager.deleteByPermission(id);
        permPathManager.deleteById(id);
    }

    /**
     * 批量删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids) {
        rolePathManager.deleteByPermissions(ids);
        permPathManager.deleteByIds(ids);
    }

    /**
     * 获取指请求定类型未启用访问控制的请求路径
     */
    public List<String> findIgnorePathByRequestType(String requestType) {
        return permPathManager.findByNotEnableAndRequestType(requestType)
            .stream()
            .map(PermPath::getPath)
            .collect(Collectors.toList());
    }

    /**
     * 获取单条
     */
    public PermPathResult findById(Long id) {
        return permPathManager.findById(id).map(PermPath::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据ids查询权限信息
     */
    public List<PermPathResult> findByIds(List<Long> ids) {
        return MpUtil.toListResult(permPathManager.findAllByIds(ids));
    }

    /**
     * 列表
     */
    public List<PermPathResult> findAll() {
        return MpUtil.toListResult(permPathManager.findAll());
    }

    /**
     * 分页
     */
    public PageResult<PermPathResult> page(PageParam pageParam, PermPathParam param) {
        return MpUtil.toPageResult(permPathManager.page(pageParam, param));
    }


}
