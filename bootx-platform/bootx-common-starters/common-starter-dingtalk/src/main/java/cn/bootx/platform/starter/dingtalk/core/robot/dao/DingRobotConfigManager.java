package cn.bootx.platform.starter.dingtalk.core.robot.dao;

import cn.bootx.platform.starter.dingtalk.core.robot.entity.DingRobotConfig;
import cn.bootx.platform.starter.dingtalk.param.robot.DingRobotConfigParam;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 钉钉机器人
 *
 * @author xxm
 * @since 2020/11/29
 */
@Repository
@RequiredArgsConstructor
public class DingRobotConfigManager extends BaseManager<DingRobotConfigMapper, DingRobotConfig> {

    public Optional<DingRobotConfig> findByCode(String code) {
        return findByField(DingRobotConfig::getCode, code);
    }

    public boolean existsByCode(String code) {
        return existedByField(DingRobotConfig::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return lambdaQuery().eq(DingRobotConfig::getCode, code).ne(MpIdEntity::getId, id).exists();
    }

    public Page<DingRobotConfig> page(PageParam pageParam, DingRobotConfigParam param) {
        Page<DingRobotConfig> mpPage = MpUtil.getMpPage(pageParam, DingRobotConfig.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
            .like(StrUtil.isNotBlank(param.getCode()), DingRobotConfig::getCode, param.getCode())
            .like(StrUtil.isNotBlank(param.getName()), DingRobotConfig::getName, param.getName())
            .like(StrUtil.isNotBlank(param.getAccessToken()), DingRobotConfig::getAccessToken, param.getAccessToken())
            .page(mpPage);
    }

}
