package cn.bootx.platform.iam.core.permission.service;

import cn.bootx.platform.common.core.annotation.OperateLog;
import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.websocket.entity.WsRes;
import cn.bootx.platform.common.websocket.service.UserWsNoticeService;
import cn.bootx.platform.iam.param.permission.PermPathBatchEnableParam;
import cn.bootx.platform.iam.param.permission.PermPathParam;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.iam.core.permission.convert.PermConvert;
import cn.bootx.platform.iam.core.permission.dao.PermPathManager;
import cn.bootx.platform.iam.core.permission.entity.PermPath;
import cn.bootx.platform.iam.core.permission.entity.RequestPath;
import cn.bootx.platform.iam.core.upms.dao.RolePathManager;
import cn.bootx.platform.iam.dto.permission.PermPathDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.bootx.platform.iam.code.CachingCode.IGNORE_PATH;
import static cn.bootx.platform.iam.code.CachingCode.USER_PATH;

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

    private final RequestPathService requestPathService;

    private final UserWsNoticeService userWsNoticeService;

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
    @CacheEvict(value = { USER_PATH, IGNORE_PATH }, allEntries = true)
    public void update(PermPathParam param) {
        PermPath permPath = permPathManager.findById(param.getId()).orElseThrow(() -> new BizException("信息不存在"));
        BeanUtil.copyProperties(param, permPath, CopyOptions.create().ignoreNullValue());
        // 编辑过的信息不再作为系统生成的
        permPath.setGenerate(false);
        permPathManager.updateById(permPath);
    }

    /**
     * 批量更新状态
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = { USER_PATH, IGNORE_PATH }, allEntries = true)
    public void batchUpdateEnable(PermPathBatchEnableParam param) {
        List<PermPath> permPaths = permPathManager.findAllByIds(param.getPermPathIds());
        permPaths.forEach(permPath -> permPath.setEnable(param.isEnable()).setGenerate(false));
        permPathManager.updateAllById(permPaths);
    }

    /**
     * 删除
     */
    @CacheEvict(value = { USER_PATH, IGNORE_PATH }, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        rolePathManager.deleteByPermission(id);
        permPathManager.deleteById(id);
    }

    /**
     * 批量删除
     */
    @CacheEvict(value = { USER_PATH, IGNORE_PATH }, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> ids) {
        rolePathManager.deleteByPermissions(ids);
        permPathManager.deleteByIds(ids);
    }

    /**
     * 获取指请求定类型未启用访问控制的请求路径
     */
    @Cacheable(value = { IGNORE_PATH }, key = "#requestType")
    public List<String> findIgnorePathByRequestType(String requestType) {
        return permPathManager.findByNotEnableAndRequestType(requestType)
            .stream()
            .map(PermPath::getPath)
            .collect(Collectors.toList());
    }

    /**
     * 获取单条
     */
    public PermPathDto findById(Long id) {
        return permPathManager.findById(id).map(PermPath::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 根据ids查询权限信息
     */
    public List<PermPathDto> findByIds(List<Long> ids) {
        return ResultConvertUtil.dtoListConvert(permPathManager.findAllByIds(ids));
    }

    /**
     * 列表
     */
    public List<PermPathDto> findAll() {
        return ResultConvertUtil.dtoListConvert(permPathManager.findAll());
    }

    /**
     * 分页
     */
    public PageResult<PermPathDto> page(PageParam pageParam, PermPathParam param) {
        return MpUtil.convert2DtoPageResult(permPathManager.page(pageParam, param));
    }

    /**
     * 同步系统请求资源, 添加没有的资源, 删掉已经存在的资源
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(title = "同步系统请求资源")
    @CacheEvict(value = { USER_PATH }, allEntries = true)
    @Async("asyncExecutor")
    public void syncSystem() {
        List<RequestPath> requestPaths = requestPathService.getRequestPaths();
        List<PermPath> permPaths = requestPaths.stream().map(requestPath -> {
            PermPath permPath = PermConvert.CONVERT.convert(requestPath);
            // 请求权限码
            String code = null;
            if (StrUtil.isNotBlank(requestPath.getClassName()) && StrUtil.isNotBlank(requestPath.getMethodName())) {
                code = requestPath.getClassName() + "#" + requestPath.getMethodName();
            }
            // 请求备注
            String remark = null;
            if (StrUtil.isNotBlank(requestPath.getClassRemark()) && StrUtil.isNotBlank(requestPath.getMethodRemark())) {
                remark = requestPath.getClassRemark() + " " + requestPath.getMethodRemark();
            }
            return permPath.setCode(code)
                .setName(requestPath.getMethodRemark())
                .setRemark(remark)
                .setGroupName(requestPath.getClassRemark())
                .setGenerate(true)
                .setEnable(true);
        }).collect(Collectors.toList());
        // 增量更新
        this.incrementUpdate(permPaths);
        // 更新成功通知
        Optional<UserDetail> currentUser = SecurityUtil.getCurrentUser();
        currentUser.ifPresent(userDetail -> userWsNoticeService.sendMessageByUser(WsRes.notificationInfo("请求资源同步完成"),
                userDetail.getId()));
    }

    /**
     * 增量更新，对数据库不存在的进行添加, 对系统请求不存在的进行删除
     */
    private void incrementUpdate(List<PermPath> permPaths) {
        List<PermPath> pathList = permPathManager.findAll();
        List<String> paths = pathList.stream().map(PermPath::getPath).collect(Collectors.toList());
        // 过滤掉数据库已经存在的
        List<PermPath> permPathList = permPaths.stream()
            .filter(permPath -> !paths.contains(permPath.getPath()))
            .collect(Collectors.toList());

        // 过滤出不存在的系统请求资源, 进行删除
        // 数据库已存在的路径集合
        Set<String> permPathSet = permPaths.stream().map(PermPath::getPath).collect(Collectors.toSet());
        // 挑出被删除的路径
        List<String> removePaths = paths.stream()
            .filter(path -> !permPathSet.contains(path))
            .collect(Collectors.toList());
        // 找到对应的数据条目(限定根据系统生成的条目)
        List<Long> removeIds = pathList.stream()
            .filter(PermPath::isGenerate)
            .filter(permPath -> removePaths.contains(permPath.getPath()))
            .map(MpIdEntity::getId)
            .collect(Collectors.toList());
        permPathManager.saveAll(permPathList);
        permPathManager.deleteByIds(removeIds);
    }

}
