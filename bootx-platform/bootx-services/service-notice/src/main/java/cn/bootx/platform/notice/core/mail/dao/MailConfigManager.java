package cn.bootx.platform.notice.core.mail.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.notice.core.mail.entity.MailConfig;
import cn.bootx.platform.notice.param.mail.MailConfigParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author xxm
 * @since 2020/4/8 13:27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MailConfigManager extends BaseManager<MailConfigMapper, MailConfig> {

    public Optional<MailConfig> findByActivity() {
        return findByField(MailConfig::getActivity, Boolean.TRUE);
    }

    public Optional<MailConfig> findByCode(String code) {
        return findByField(MailConfig::getCode, code);
    }

    public Page<MailConfig> page(PageParam pageParam, MailConfigParam param) {
        Page<MailConfig> mpPage = MpUtil.getMpPage(pageParam, MailConfig.class);
        return this.lambdaQuery()
            .orderByDesc(MpIdEntity::getId)
            .like(StrUtil.isNotBlank(param.getCode()), MailConfig::getCode, param.getCode())
            .like(StrUtil.isNotBlank(param.getName()), MailConfig::getName, param.getName())
            .page(mpPage);
    }

    public boolean existsByCode(String code) {
        return existedByField(MailConfig::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return existedByField(MailConfig::getCode, code, id);
    }

    public void removeAllActivity() {
        lambdaUpdate().eq(MailConfig::getActivity, Boolean.TRUE).set(MailConfig::getActivity, Boolean.FALSE).update();
    }

}
