package cn.bootx.platform.daxpay.core.channel.alipay.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AlipayConfig;
import cn.bootx.platform.daxpay.param.channel.alipay.AlipayConfigQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付宝配置
 *
 * @author xxm
 * @since 2021/2/26
 */
@Repository
@RequiredArgsConstructor
public class AlipayConfigManager extends BaseManager<AlipayConfigMapper, AlipayConfig> {

    /**
     * 获取关联的的支付宝配置
     */
    public Optional<AlipayConfig> findByMchAppCode(String mchAppCOde) {
        return findByField(AlipayConfig::getMchAppCode, mchAppCOde);
    }

    /**
     * 分页
     */
    public Page<AlipayConfig> page(PageParam pageParam, AlipayConfigQuery param) {
        Page<AlipayConfig> mpPage = MpUtil.getMpPage(pageParam, AlipayConfig.class);
        return lambdaQuery().select(AlipayConfig.class, MpUtil::excludeBigField)
            .like(StrUtil.isNotBlank(param.getName()), AlipayConfig::getName, param.getName())
            .like(StrUtil.isNotBlank(param.getAppId()), AlipayConfig::getAppId, param.getAppId())
            .page(mpPage);
    }

}
