package org.dromara.daxpay.channel.alipay.service.app;

import org.dromara.daxpay.channel.alipay.convert.AlipayMchAppConvert;
import org.dromara.daxpay.channel.alipay.dao.app.AlipayMchAppManager;
import org.dromara.daxpay.channel.alipay.entity.app.AlipayMchApp;
import org.dromara.daxpay.channel.alipay.param.app.AlipayMchAppParam;
import org.dromara.daxpay.channel.alipay.result.app.AlipayMchAppResult;
import org.dromara.daxpay.channel.alipay.service.config.AlipayMchAppAuthConfigService;
import org.dromara.daxpay.channel.alipay.service.config.AlipayMchAppKeyConfigService;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 支付宝直连商户应用管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayMchAppService {

    private final AlipayMchAppManager alipayMchAppManager;
    private final AlipayMchAppKeyConfigService alipayMchAppKeyConfigService;
    private final AlipayMchAppAuthConfigService alipayMchAppAuthConfigService;

    /// 根据商户号和通道商户号查询应用列表
    public List<AlipayMchAppResult> listByMchNoAndChannelMchNo(String mchNo, String channelMchNo) {
        List<AlipayMchApp> list = alipayMchAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo);
        return MpUtil.toListResult(list);
    }

    /// 根据ID查询应用详情
    public AlipayMchAppResult findById(Long id) {
        return alipayMchAppManager.findById(id)
                .map(AlipayMchApp::toResult)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
    }

    /// 判断同一通道商户下支付宝应用ID是否已存在
    public boolean existsAliAppIdByChannel(String mchNo, String channelMchNo, String aliAppId, Long excludeId) {
        if (StrUtil.hasBlank(mchNo, channelMchNo, aliAppId)) {
            return false;
        }
        return alipayMchAppManager.existsByChannelMchNoAndAliAppId(mchNo, channelMchNo, aliAppId, excludeId);
    }

    /// 新增支付宝直连商户应用
    public void add(AlipayMchAppParam param) {
        this.assertAliAppIdUnique(param.getMchNo(), param.getChannelMchNo(), param.getAliAppId(), null);
        AlipayMchApp entity = AlipayMchAppConvert.CONVERT.toEntity(param);
        alipayMchAppManager.save(entity);
    }

    /// 更新支付宝直连商户应用
    public void update(AlipayMchAppParam param) {
        AlipayMchApp entity = alipayMchAppManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        this.assertScopeMatch(entity, param.getMchNo(), param.getChannelMchNo());
        this.assertAliAppIdUnique(entity.getMchNo(), entity.getChannelMchNo(), param.getAliAppId(), param.getId());
        AlipayMchAppConvert.CONVERT.copy(param, entity);
        alipayMchAppManager.updateById(entity);
    }

    /// 删除应用（级联删除密钥配置和授权认证配置）
    public void delete(Long id) {
        alipayMchAppManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.appNotFound"));
        alipayMchAppKeyConfigService.deleteByAppId(id);
        alipayMchAppAuthConfigService.deleteByAppId(id);
        alipayMchAppManager.deleteById(id);
    }

    /// 校验同一通道商户下应用ID唯一
    private void assertAliAppIdUnique(String mchNo, String channelMchNo, String aliAppId, Long excludeId) {
        if (this.existsAliAppIdByChannel(mchNo, channelMchNo, aliAppId, excludeId)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.appIdDuplicate");
        }
    }

    /// 校验操作范围与记录归属一致
    private void assertScopeMatch(AlipayMchApp entity, String mchNo, String channelMchNo) {
        if (!entity.getMchNo().equals(mchNo) || !entity.getChannelMchNo().equals(channelMchNo)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.appNotFound");
        }
    }
}
