package cn.daxpay.open.channel.mock.dao.isv;

import cn.daxpay.open.channel.mock.entity.isv.MockIsvChannelMerchant;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/// # Mock 模拟服务商通道商户绑定
///
@Repository
public class MockIsvChannelMerchantManager extends BaseManager<MockIsvChannelMerchantMapper, MockIsvChannelMerchant> {

    /// 校验服务商下是否已有通道商户
    public boolean existsByIsvId(Long isvId) {
        return lambdaQuery()
                .eq(MockIsvChannelMerchant::getIsvId, isvId)
                .exists();
    }

    /// 根据通道商户号查询(支付/回调/凭证组装, channelMchNo 为模拟通道控制台创建后登记的值, 全局唯一;
    /// 空白不查库, 防 eq 条件被跳过误命中任意行)
    public Optional<MockIsvChannelMerchant> findByChannelMchNo(String channelMchNo) {
        if (StrUtil.isBlank(channelMchNo)) {
            return Optional.empty();
        }
        return findByField(MockIsvChannelMerchant::getChannelMchNo, channelMchNo);
    }

    /// 按服务商ID集合统计各自名下服务商通道商户数(模拟服务商管理页列表计数用)
    ///
    /// 轻量 select(仅取 isv_id 列)后内存分组计数, mock 数据量级极小, 不走 groupBy SQL。
    public Map<Long, Long> countGroupByIsvIds(Collection<Long> isvIds) {
        if (CollUtil.isEmpty(isvIds)) {
            return Map.of();
        }
        return lambdaQuery()
                .in(MockIsvChannelMerchant::getIsvId, isvIds)
                .select(MockIsvChannelMerchant::getIsvId)
                .list()
                .stream()
                .collect(Collectors.groupingBy(MockIsvChannelMerchant::getIsvId, Collectors.counting()));
    }
}
