package cn.bootx.platform.iam.core.permission.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.core.permission.entity.PermPath;
import cn.bootx.platform.iam.param.permission.PermPathParam;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限
 *
 * @author xxm
 * @since 2020/5/10 23:27
 */
@Repository
@RequiredArgsConstructor
public class PermPathManager extends BaseManager<PermPathMapper, PermPath> {

    public Page<PermPath> page(PageParam pageParam, PermPathParam param) {
        Page<PermPath> mpPage = MpUtil.getMpPage(pageParam, PermPath.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(StrUtil.isNotBlank(param.getCode()), PermPath::getCode, param.getCode())
            .like(StrUtil.isNotBlank(param.getPath()), PermPath::getPath, param.getPath())
            .like(StrUtil.isNotBlank(param.getName()), PermPath::getName, param.getName())
            .like(StrUtil.isNotBlank(param.getGroupName()), PermPath::getGroupName, param.getGroupName())
            .page(mpPage);

    }

    /**
     * 查询未被启用的请求路径权限
     */
    public List<PermPath> findByNotEnableAndRequestType(String requestType) {
        return lambdaQuery().eq(PermPath::isEnable, false).eq(PermPath::getRequestType, requestType).list();
    }

    /**
     * 新增
     */
    @Override
    public List<PermPath> saveAll(List<PermPath> permPaths) {
        MpUtil.initEntityList(permPaths, SecurityUtil.getUserIdOrDefaultId());
        MpUtil.executeBatch(permPaths, baseMapper::saveAll, this.DEFAULT_BATCH_SIZE);
        return permPaths;
    }

}
