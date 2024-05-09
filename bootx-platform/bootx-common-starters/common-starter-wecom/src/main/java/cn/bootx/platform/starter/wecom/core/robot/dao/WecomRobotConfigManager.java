package cn.bootx.platform.starter.wecom.core.robot.dao;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.starter.wecom.core.robot.entity.WecomRobotConfig;
import cn.bootx.platform.starter.wecom.param.robot.WecomRobotConfigParam;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 企业微信机器人配置
 *
 * @author bootx
 * @since 2022-07-23
 */
@Repository
@RequiredArgsConstructor
public class WecomRobotConfigManager extends BaseManager<WecomRobotConfigMapper, WecomRobotConfig> {

    /**
     * 根据code获取机器人配置
     */
    public Optional<WecomRobotConfig> findByCode(String code) {
        return findByField(WecomRobotConfig::getCode, code);
    }

    public boolean existsByCode(String code) {
        return existedByField(WecomRobotConfig::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return lambdaQuery().eq(WecomRobotConfig::getCode, code).ne(MpIdEntity::getId, id).exists();
    }

    /**
     * 分页
     */
    public Page<WecomRobotConfig> page(PageParam pageParam, WecomRobotConfigParam param) {
        Page<WecomRobotConfig> mpPage = MpUtil.getMpPage(pageParam, WecomRobotConfig.class);
        return lambdaQuery().like(StrUtil.isNotBlank(param.getCode()), WecomRobotConfig::getCode, param.getCode())
            .like(StrUtil.isNotBlank(param.getName()), WecomRobotConfig::getCode, param.getName())
            .like(StrUtil.isNotBlank(param.getWebhookKey()), WecomRobotConfig::getWebhookKey, param.getWebhookKey())
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

}
