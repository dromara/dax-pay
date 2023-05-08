package cn.bootx.daxpay.core.paymodel.alipay.dao;

import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.daxpay.core.paymodel.alipay.entity.AlipayConfig;
import cn.bootx.daxpay.param.paymodel.alipay.AlipayConfigQuery;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * 支付宝配置
 *
 * @author xxm
 * @date 2021/2/26
 */
@Repository
@RequiredArgsConstructor
public class AlipayConfigManager extends BaseManager<AlipayConfigMapper, AlipayConfig> {

    private Optional<AlipayConfig> alipayConfig;

    @Override
    public AlipayConfig saveOrUpdate(AlipayConfig entity) {
        this.clearCache();
        return super.saveOrUpdate(entity);
    }

    @Override
    public AlipayConfig updateById(AlipayConfig alipayConfig) {
        this.clearCache();
        return super.updateById(alipayConfig);
    }

    /**
     * 获取启用的支付宝配置
     */
    public Optional<AlipayConfig> findActivity() {
        if (Objects.isNull(alipayConfig)) {
            alipayConfig = findByField(AlipayConfig::getActivity, Boolean.TRUE);
        }
        return alipayConfig;
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

    /**
     * 清除所有启用的支付配置
     */
    public void removeAllActivity() {
        this.clearCache();
        lambdaUpdate().eq(AlipayConfig::getActivity, Boolean.TRUE)
            .set(AlipayConfig::getActivity, Boolean.FALSE)
            .update();
    }

    /**
     * 清除缓存
     */
    public void clearCache() {
        alipayConfig = null;
    }

}
