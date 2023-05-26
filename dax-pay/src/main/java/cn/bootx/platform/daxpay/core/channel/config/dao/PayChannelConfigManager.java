package cn.bootx.platform.daxpay.core.channel.config.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.core.channel.config.entity.PayChannelConfig;
import cn.bootx.platform.daxpay.param.channel.config.PayChannelConfigParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 支付通道配置
 * @author xxm
 * @date 2023-05-24
 */
@Repository
@RequiredArgsConstructor
public class PayChannelConfigManager extends BaseManager<PayChannelConfigMapper, PayChannelConfig> {


    /**
    * 分页
    */
    public Page<PayChannelConfig> page(PageParam pageParam, PayChannelConfigParam param) {
        Page<PayChannelConfig> mpPage = MpUtil.getMpPage(pageParam, PayChannelConfig.class);
        QueryWrapper<PayChannelConfig> wrapper = QueryGenerator.generator(param, this.getEntityClass());
        wrapper.select(this.getEntityClass(),MpUtil::excludeBigField)
                .orderByDesc(MpUtil.getColumnName(PayChannelConfig::getId));
        return this.page(mpPage,wrapper);
    }

    /**
     * 查询全部并排序
     */
    public List<PayChannelConfig> findAllByOrder(){
        return lambdaQuery()
                .orderByAsc(PayChannelConfig::getSortNo)
                .list();
    }

    public boolean existsByCode(String code) {
        return existedByField(PayChannelConfig::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return lambdaQuery().eq(PayChannelConfig::getCode, code).ne(MpIdEntity::getId, id).exists();
    }
}
