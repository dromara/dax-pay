package cn.daxpay.open.payment.masterdata.dao.product;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.payment.masterdata.entity.product.PayProduct;
import cn.daxpay.open.payment.masterdata.param.product.PayProductQuery;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.daxpay.open.platform.common.mybatisplus.query.generator.QueryGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # 支付产品
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class PayProductManager extends BaseManager<PayProductMapper, PayProduct> {

    /// 根据产品编码查询
    public Optional<PayProduct> findByCode(String code) {
        return lambdaQuery()
                .eq(PayProduct::getCode, code)
                .oneOpt();
    }

    /// 按通道编码查询
    public List<PayProduct> listByChannel(String channel) {
        return lambdaQuery()
                .eq(PayProduct::getChannel, channel)
                .orderByAsc(PayProduct::getSortNo)
                .orderByAsc(PayProduct::getId)
                .list();
    }

    /// 分页
    public Page<PayProduct> page(PageParam pageParam, PayProductQuery query) {
        Page<PayProduct> mpPage = MpUtil.getMpPage(pageParam);
        QueryWrapper<PayProduct> generator = QueryGenerator.generator(query);
        generator.lambda().orderByAsc(PayProduct::getSortNo).orderByAsc(PayProduct::getId);
        return page(mpPage, generator);
    }
}
