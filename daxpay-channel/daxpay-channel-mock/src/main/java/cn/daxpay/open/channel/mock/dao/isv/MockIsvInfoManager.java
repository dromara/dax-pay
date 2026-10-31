package cn.daxpay.open.channel.mock.dao.isv;

import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/// # Mock 模拟服务商信息
///
@Repository
public class MockIsvInfoManager extends BaseManager<MockIsvInfoMapper, MockIsvInfo> {

    /// 根据服务商编号查询(凭证保存/装配链路用; 空白不查库, 防 eq 条件被跳过误命中任意行)
    public Optional<MockIsvInfo> findByIsvNo(String isvNo) {
        if (StrUtil.isBlank(isvNo)) {
            return Optional.empty();
        }
        return findByField(MockIsvInfo::getIsvNo, isvNo);
    }

    /// 根据主键查询(支付闸门/凭证组装链路, 不校验启用)
    public Optional<MockIsvInfo> findById(Long id) {
        return super.findById(id);
    }

    /// 查询全部服务商(按创建时间正序, 含停用行: 下拉须展示启停态)
    public List<MockIsvInfo> findAll() {
        return lambdaQuery()
                .orderByAsc(MockIsvInfo::getCreateTime)
                .list();
    }

    /// 查询全部服务商(按 id 倒序, 模拟服务商管理页列表用: 新建行靠前)
    public List<MockIsvInfo> findAllByIdDesc() {
        return lambdaQuery()
                .orderByDesc(MockIsvInfo::getId)
                .list();
    }
}
