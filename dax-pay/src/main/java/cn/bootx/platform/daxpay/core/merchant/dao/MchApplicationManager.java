package cn.bootx.platform.daxpay.core.merchant.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.core.merchant.entity.MchApplication;
import cn.bootx.platform.daxpay.param.merchant.MchApplicationParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 商户应用
 * @author xxm
 * @date 2023-05-23
 */
@Repository
@RequiredArgsConstructor
public class MchApplicationManager extends BaseManager<MchApplicationMapper, MchApplication> {

    /**
    * 分页
    */
    public Page<MchApplication> page(PageParam pageParam, MchApplicationParam param) {
        Page<MchApplication> mpPage = MpUtil.getMpPage(pageParam, MchApplication.class);
        QueryWrapper<MchApplication> wrapper = QueryGenerator.generator(param, this.getEntityClass());
        wrapper.select(this.getEntityClass(),MpUtil::excludeBigField)
                .orderByDesc(MpUtil.getColumnName(MchApplication::getId));
        return this.page(mpPage,wrapper);
    }
}
