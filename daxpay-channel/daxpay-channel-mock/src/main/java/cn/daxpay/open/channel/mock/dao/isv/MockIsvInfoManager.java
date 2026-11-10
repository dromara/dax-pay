package cn.daxpay.open.channel.mock.dao.isv;

import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # Mock 模拟服务商信息
///
@Repository
public class MockIsvInfoManager extends BaseManager<MockIsvInfoMapper, MockIsvInfo> {

    /// 根据服务商编号查询(单例服务商 get-or-create; 空白不查库, 防 eq 条件被跳过误命中任意行)
    public Optional<MockIsvInfo> findByIsvNo(String isvNo) {
        if (StrUtil.isBlank(isvNo)) {
            return Optional.empty();
        }
        return findByField(MockIsvInfo::getIsvNo, isvNo);
    }

    /// 查询全局唯一模拟服务商(编号允许运营端修改, 不能按固定编号定位, 取最早一行)
    public Optional<MockIsvInfo> findSingleton() {
        return lambdaQuery().orderByAsc(MockIsvInfo::getId).last("LIMIT 1").oneOpt();
    }
}
