package cn.daxpay.open.channel.mock.service.isv;

import cn.daxpay.open.channel.mock.convert.isv.MockIsvInfoConvert;
import cn.daxpay.open.channel.mock.dao.isv.MockIsvInfoManager;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/// # Mock 模拟服务商信息(全局唯一)
///
/// open 版 mock 通道取消多服务商模式, 收敛为「全局唯一模拟服务商 + 单份全局接入密钥」:
/// - 服务商编号可运营端修改(mock 网关按 isv_no 匹配商户档案, 须与控制台档案一致),
///   名称固定 [#SINGLETON_ISV_NAME], 建档由本类 get-or-create 完成;
/// - 并发/重复调用幂等(唯一索引冲突回查兜底);
/// - 运营端不再提供新增/删除, 仅保留编号编辑与支付开关(payEnable=false 仅拒新支付, 不影响存量交易处理)。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvInfoService {

    /// 全局唯一模拟服务商编号缺省值(首次建档使用, 之后允许运营端修改)
    public static final String SINGLETON_ISV_NO = "MISV1000000000001";

    /// 全局唯一模拟服务商名称
    public static final String SINGLETON_ISV_NAME = "模拟服务商";

    private final MockIsvInfoManager mockIsvInfoManager;

    /// 获取全局唯一模拟服务商(不存在则按缺省编号创建, 幂等)
    ///
    /// 编号可被运营端修改, 故按行定位(取最早一行)而不是按固定编号查;
    /// 单例恒 enable=true(多服务商模式取消后不再有「服务商启停建档闸门」语义);
    /// payEnable 创建时缺省 true, 之后由 [#switchPay] 维护。
    @Transactional(rollbackFor = Exception.class)
    public MockIsvInfo getSingleton() {
        Optional<MockIsvInfo> existing = mockIsvInfoManager.findSingleton();
        if (existing.isPresent()) {
            return existing.get();
        }
        MockIsvInfo entity = new MockIsvInfo();
        entity.setIsvNo(SINGLETON_ISV_NO);
        entity.setIsvName(SINGLETON_ISV_NAME);
        entity.setEnable(true);
        entity.setPayEnable(true);
        try {
            mockIsvInfoManager.save(entity);
        } catch (DuplicateKeyException e) {
            // 并发首查双插兜底: 唯一索引冲突说明他方已建行, 回查即可
            return mockIsvInfoManager.findSingleton()
                    // mock: 模拟服务商不存在
                    .orElseThrow(() -> new BizInfoException(CommonErrorCode.SYSTEM_ERROR,
                            "error.channel.mock.isvNotExist", SINGLETON_ISV_NO));
        }
        return entity;
    }

    /// 查询当前模拟服务商信息(运营端展示)
    @Transactional(rollbackFor = Exception.class)
    public MockIsvInfoResult current() {
        return MockIsvInfoConvert.CONVERT.toResult(this.getSingleton());
    }

    /// 修改全局唯一模拟服务商编号
    ///
    /// 编号须与 mock 网关控制台商户档案的 isv_no 一致, 修改后立即生效
    /// (凭证装配每笔交易重新读取单例, 无缓存); 绑定表只存 isv_id, 不随编号变更迁移。
    @Transactional(rollbackFor = Exception.class)
    public MockIsvInfoResult updateIsvNo(String isvNo) {
        String normalized = StrUtil.trim(isvNo);
        if (StrUtil.isBlank(normalized)) {
            // 服务商编号不能为空
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.isvNoRequired");
        }
        MockIsvInfo isv = this.getSingleton();
        if (normalized.equals(isv.getIsvNo())) {
            return MockIsvInfoConvert.CONVERT.toResult(isv);
        }
        if (mockIsvInfoManager.findByIsvNo(normalized).isPresent()) {
            // 该模拟服务商编号已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.isvNoExists", normalized);
        }
        isv.setIsvNo(normalized);
        mockIsvInfoManager.updateById(isv);
        return MockIsvInfoConvert.CONVERT.toResult(isv);
    }

    /// 切换模拟服务商支付开关
    ///
    /// @param payEnable true 允许发起新支付, false 拒绝新支付(退款/关单/查询/同步/回调不受影响)
    @Transactional(rollbackFor = Exception.class)
    public void switchPay(boolean payEnable) {
        MockIsvInfo isv = this.getSingleton();
        isv.setPayEnable(payEnable);
        mockIsvInfoManager.updateById(isv);
    }
}
