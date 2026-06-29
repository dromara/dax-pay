package cn.daxpay.open.channel.alipay.service.direct;

import cn.daxpay.open.channel.alipay.convert.direct.AlipayDirectAppConvert;
import cn.daxpay.open.channel.alipay.dao.direct.AlipayDirectAppManager;
import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectApp;
import cn.daxpay.open.channel.alipay.param.direct.AlipayDirectAppParam;
import cn.daxpay.open.channel.alipay.result.direct.AlipayDirectAppResult;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppAuthConfigService;
import cn.daxpay.open.channel.alipay.service.direct.AlipayDirectAppKeyConfigService;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 支付宝直连商户应用管理
///
/// 提供直连商户应用的增删改查功能，包含同一通道商户下应用ID的唯一性校验和操作范围校验(防止跨商户操作)。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayDirectAppService {

    private final AlipayDirectAppManager alipayDirectAppManager;
    private final AlipayDirectAppKeyConfigService alipayDirectAppKeyConfigService;
    private final AlipayDirectAppAuthConfigService alipayDirectAppAuthConfigService;
    private final AlipayDirectAppCapabilityService alipayDirectAppCapabilityService;

    /// 根据商户号和通道商户号查询应用列表
    public List<AlipayDirectAppResult> listByMchNoAndChannelMchNo(String mchNo, String channelMchNo) {
        var list = alipayDirectAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo);
        return MpUtil.toListResult(list);
    }

    /// 根据ID查询应用详情
    public AlipayDirectAppResult findById(Long id) {
        return alipayDirectAppManager.findById(id)
                .map(AlipayDirectApp::toResult)
                // 支付宝: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
    }

    /// 判断同一通道商户下支付宝应用ID是否已存在
    public boolean existsAliAppIdByChannel(String mchNo, String channelMchNo, String aliAppId, Long excludeId) {
        if (StrUtil.hasBlank(mchNo, channelMchNo, aliAppId)) {
            return false;
        }
        return alipayDirectAppManager.existsByChannelMchNoAndAliAppId(mchNo, channelMchNo, aliAppId, excludeId);
    }

    /// 新增支付宝直连商户应用
    public void add(AlipayDirectAppParam param) {
        this.assertAliAppIdUnique(param.getMchNo(), param.getChannelMchNo(), param.getAliAppId(), null);
        var entity = AlipayDirectAppConvert.CONVERT.toEntity(param);
        alipayDirectAppManager.save(entity);
    }

    /// 更新支付宝直连商户应用
    public void update(AlipayDirectAppParam param) {
        var entity = alipayDirectAppManager.findById(param.getId())
                // 支付宝: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
        this.assertScopeMatch(entity, param.getMchNo(), param.getChannelMchNo());
        this.assertAliAppIdUnique(entity.getMchNo(), entity.getChannelMchNo(), param.getAliAppId(), param.getId());
        AlipayDirectAppConvert.CONVERT.copy(param, entity);
        alipayDirectAppManager.updateById(entity);
    }

    /// 删除应用（级联删除密钥配置、授权认证配置与能力关联）
    public void delete(Long id) {
        alipayDirectAppManager.findById(id)
                // 支付宝: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.alipay.mchAppNotFound"));
        alipayDirectAppKeyConfigService.deleteByAlipayDirectAppId(id);
        alipayDirectAppAuthConfigService.deleteByAlipayDirectAppId(id);
        // 级联清理能力关联，避免悬空引用
        alipayDirectAppCapabilityService.deleteByAlipayDirectAppId(id);
        alipayDirectAppManager.deleteById(id);
    }

    /// 校验同一通道商户下应用ID唯一
    private void assertAliAppIdUnique(String mchNo, String channelMchNo, String aliAppId, Long excludeId) {
        if (this.existsAliAppIdByChannel(mchNo, channelMchNo, aliAppId, excludeId)) {
            // 支付宝: 同一商户下应用ID已存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.appIdDuplicate");
        }
    }

    /// 校验操作范围与记录归属一致
    private void assertScopeMatch(AlipayDirectApp entity, String mchNo, String channelMchNo) {
        if (!entity.getMchNo().equals(mchNo) || !entity.getChannelMchNo().equals(channelMchNo)) {
            // 支付宝: 直连商户应用不存在或商户号归属不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.alipay.mchAppNotFound");
        }
    }
}
