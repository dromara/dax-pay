package org.dromara.daxpay.channel.alipay.service.isv;

import org.dromara.daxpay.channel.alipay.convert.AlipayIsvAppConvert;
import org.dromara.daxpay.channel.alipay.dao.isv.AlipayIsvAppManager;
import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvApp;
import org.dromara.daxpay.channel.alipay.param.isv.AlipayIsvAppParam;
import org.dromara.daxpay.channel.alipay.result.isv.AlipayIsvAppResult;
import org.dromara.daxpay.channel.alipay.service.isv.AlipayIsvAppAuthConfigService;
import org.dromara.daxpay.channel.alipay.service.isv.AlipayIsvAppKeyConfigService;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 支付宝服务商应用管理
///
/// 提供服务商应用的增删改查功能，包含支付宝应用ID的唯一性校验和级联删除密钥/授权配置。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvAppService {

    private final AlipayIsvAppManager alipayIsvAppManager;
    private final AlipayIsvAppKeyConfigService alipayIsvAppKeyConfigService;
    private final AlipayIsvAppAuthConfigService alipayIsvAppAuthConfigService;

    /// 查询全部应用列表
    public List<AlipayIsvAppResult> listAll() {
        List<AlipayIsvApp> list = alipayIsvAppManager.listAll();
        return MpUtil.toListResult(list);
    }

    /// 根据ID查询应用详情
    public AlipayIsvAppResult findById(Long id) {
        return alipayIsvAppManager.findById(id)
                .map(AlipayIsvApp::toResult)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
    }

    /// 判断支付宝应用ID是否已存在
    public boolean existsAliAppId(String aliAppId, Long excludeId) {
        if (StrUtil.isBlank(aliAppId)) {
            return false;
        }
        return alipayIsvAppManager.existsByAliAppId(aliAppId, excludeId);
    }

    /// 新增支付宝服务商应用
    public void add(AlipayIsvAppParam param) {
        this.assertAliAppIdUnique(param.getAliAppId(), null);
        AlipayIsvApp entity = AlipayIsvAppConvert.CONVERT.toEntity(param);
        alipayIsvAppManager.save(entity);
    }

    /// 更新支付宝服务商应用
    public void update(AlipayIsvAppParam param) {
        AlipayIsvApp entity = alipayIsvAppManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        this.assertAliAppIdUnique(param.getAliAppId(), param.getId());
        AlipayIsvAppConvert.CONVERT.copy(param, entity);
        alipayIsvAppManager.updateById(entity);
    }

    /// 删除应用
    public void delete(Long id) {
        alipayIsvAppManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        alipayIsvAppKeyConfigService.deleteByAppId(id);
        alipayIsvAppAuthConfigService.deleteByAppId(id);
        alipayIsvAppManager.deleteById(id);
    }

    /// 校验应用ID唯一
    private void assertAliAppIdUnique(String aliAppId, Long excludeId) {
        if (this.existsAliAppId(aliAppId, excludeId)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.appIdDuplicate");
        }
    }
}
