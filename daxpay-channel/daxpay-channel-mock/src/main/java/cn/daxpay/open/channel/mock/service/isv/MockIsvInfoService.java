package cn.daxpay.open.channel.mock.service.isv;

import cn.daxpay.open.channel.mock.convert.isv.MockIsvInfoConvert;
import cn.daxpay.open.channel.mock.dao.credential.MockIsvCredentialManager;
import cn.daxpay.open.channel.mock.dao.isv.MockIsvChannelMerchantManager;
import cn.daxpay.open.channel.mock.dao.isv.MockIsvInfoManager;
import cn.daxpay.open.channel.mock.entity.isv.MockIsvInfo;
import cn.daxpay.open.channel.mock.param.isv.MockIsvInfoParam;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoListResult;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoOption;
import cn.daxpay.open.channel.mock.result.isv.MockIsvInfoResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

/// # Mock 模拟服务商信息管理
///
/// mock 服务商主数据维护: 服务商编号(MISV+雪花ID)创建时自动生成不可改;
/// enable 控新建通道商户闸门, payEnable 控新支付闸门(停用不影响存量交易处理)。
///
/// open 版无通用编号工具(BizNoGenerateUtil 不在本仓), 编号在本类内以
/// [IdUtil#getSnowflakeNextId] 自拼, 不改动通用工具类。
@Slf4j
@Service
@RequiredArgsConstructor
public class MockIsvInfoService {

    private final MockIsvInfoManager mockIsvInfoManager;
    private final MockIsvChannelMerchantManager mockIsvChannelMerchantManager;
    private final MockIsvCredentialManager mockIsvCredentialManager;

    /// 查询全部服务商(含停用, 下拉须展示启停态; 按创建时间正序)
    public List<MockIsvInfoOption> dropdown() {
        return mockIsvInfoManager.findAll().stream()
                .map(MockIsvInfoConvert.CONVERT::toOption)
                .toList();
    }

    /// 全量服务商列表(模拟服务商管理页, 不分页: 服务商数量极少; 按 id 倒序),
    /// 每行附带名下服务商通道商户(mock_isv)计数与签名密钥配置标记
    public List<MockIsvInfoListResult> listWithCount() {
        List<MockIsvInfo> isvList = mockIsvInfoManager.findAllByIdDesc();
        // 名下服务商通道商户数分组计数(含被通用删除链清理掉绑定行的历史服务商, 计数自然为 0)
        List<Long> isvIds = isvList.stream().map(MockIsvInfo::getId).toList();
        Map<Long, Long> countMap = mockIsvChannelMerchantManager.countGroupByIsvIds(isvIds);
        // 签名密钥配置存在性批量判定(isv_no 一次 IN 查询避免 N+1)
        Set<String> configuredIsvNos = mockIsvCredentialManager.findConfiguredIsvNos(
                isvList.stream().map(MockIsvInfo::getIsvNo).toList());
        return isvList.stream()
                .map(isv -> {
                    MockIsvInfoListResult result = MockIsvInfoConvert.CONVERT.toListResult(isv);
                    result.setChannelMerchantCount(countMap.getOrDefault(isv.getId(), 0L));
                    result.setCredentialConfigured(configuredIsvNos.contains(isv.getIsvNo()));
                    return result;
                })
                .toList();
    }

    /// 查询详情
    public MockIsvInfoResult findById(Long id) {
        MockIsvInfoResult result = mockIsvInfoManager.findById(id)
                .map(MockIsvInfoConvert.CONVERT::toResult)
                // mock: 模拟服务商不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.mock.isvNotExist", id));
        result.setCredentialConfigured(mockIsvCredentialManager.existConfiguredByIsvNo(result.getIsvNo()));
        return result;
    }

    /// 创建模拟服务商(服务商编号自动生成, enable/payEnable 缺省 true)
    @Transactional(rollbackFor = Exception.class)
    public void create(MockIsvInfoParam param) {
        MockIsvInfo entity = new MockIsvInfo();
        // 编号格式对齐 DDL 语义: MISV + 雪花ID(open 无 BizNoGenerateUtil, 本类内自拼)
        entity.setIsvNo("MISV" + IdUtil.getSnowflakeNextId());
        entity.setIsvName(param.getIsvName());
        // mock 无密钥配置环节, 启停位缺省即启用
        entity.setEnable(param.getEnable() == null || param.getEnable());
        entity.setPayEnable(param.getPayEnable() == null || param.getPayEnable());
        mockIsvInfoManager.save(entity);
    }

    /// 编辑 + 启停合一(空字段不覆盖, 服务商编号不可改)
    @Transactional(rollbackFor = Exception.class)
    public void update(MockIsvInfoParam param) {
        MockIsvInfo entity = mockIsvInfoManager.findById(param.getId())
                // mock: 模拟服务商不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.mock.isvNotExist", param.getId()));
        MockIsvInfoConvert.CONVERT.copy(param, entity);
        mockIsvInfoManager.updateById(entity);
    }

    /// 删除服务商(名下仍有服务商通道商户(mock_isv)绑定时拒绝)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        mockIsvInfoManager.findById(id)
                // mock: 模拟服务商不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.mock.isvNotExist", id));
        if (mockIsvChannelMerchantManager.existsByIsvId(id)) {
            // 该模拟服务商名下仍有通道商户, 无法删除
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.mock.isvHasChannelMerchant", id);
        }
        mockIsvInfoManager.deleteById(id);
    }
}
