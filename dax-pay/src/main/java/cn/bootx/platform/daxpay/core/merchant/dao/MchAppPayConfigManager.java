package cn.bootx.platform.daxpay.core.merchant.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.daxpay.param.merchant.MchAppPayConfigParam;
import cn.bootx.platform.daxpay.core.merchant.entity.MchAppPayConfig;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 商户应用支付配置
 * @author xxm
 * @date 2023-05-19
 */
@Repository
@RequiredArgsConstructor
public class MchAppPayConfigManager extends BaseManager<MchAppPayConfigMapper, MchAppPayConfig> {

    /**
    * 分页
    */
    public Page<MchAppPayConfig> page(PageParam pageParam, MchAppPayConfigParam param) {
        Page<MchAppPayConfig> mpPage = MpUtil.getMpPage(pageParam, MchAppPayConfig.class);
        return this.lambdaQuery()
                .select(this.getEntityClass(),MpUtil::excludeBigField)
                .orderByDesc(MpIdEntity::getId)
                .page(mpPage);
    }
}
