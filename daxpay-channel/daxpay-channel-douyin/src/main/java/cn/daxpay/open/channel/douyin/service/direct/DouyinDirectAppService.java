package cn.daxpay.open.channel.douyin.service.direct;

import cn.daxpay.open.channel.douyin.convert.direct.DouyinDirectAppConvert;
import cn.daxpay.open.channel.douyin.dao.direct.DouyinDirectAppManager;
import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectApp;
import cn.daxpay.open.channel.douyin.param.direct.DouyinDirectAppParam;
import cn.daxpay.open.channel.douyin.result.direct.DouyinDirectAppResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/// # 抖音直连商户应用管理
///
/// 提供直连商户应用的增删改查功能，包含同一通道商户下应用ID的唯一性校验和操作范围校验(防止跨商户操作)。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinDirectAppService {

    private final DouyinDirectAppManager douyinDirectAppManager;
    private final DouyinDirectAppAuthConfigService douyinDirectAppAuthConfigService;

    /// 根据商户号和通道商户号查询应用列表
    public List<DouyinDirectAppResult> listByMchNoAndChannelMchNo(String mchNo, String channelMchNo) {
        var list = douyinDirectAppManager.listByMchNoAndChannelMchNo(mchNo, channelMchNo);
        return MpUtil.toListResult(list);
    }

    /// 根据ID查询应用详情
    public DouyinDirectAppResult findById(Long id) {
        return douyinDirectAppManager.findById(id)
                .map(DouyinDirectApp::toResult)
                // 抖音: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.douyin.mchAppNotFound"));
    }

    /// 判断同一通道商户下抖音应用ID是否已存在
    public boolean existsDouyinAppIdByChannel(String mchNo, String channelMchNo, String douyinAppId, Long excludeId) {
        if (StrUtil.hasBlank(mchNo, channelMchNo, douyinAppId)) {
            return false;
        }
        return douyinDirectAppManager.existsByChannelMchNoAndDouyinAppId(mchNo, channelMchNo, douyinAppId, excludeId);
    }

    /// 新增抖音直连商户应用
    public void add(DouyinDirectAppParam param) {
        this.assertDouyinAppIdUnique(param.getMchNo(), param.getChannelMchNo(), param.getDouyinAppId(), null);
        var entity = DouyinDirectAppConvert.CONVERT.toEntity(param);
        douyinDirectAppManager.save(entity);
    }

    /// 更新抖音直连商户应用
    public void update(DouyinDirectAppParam param) {
        var entity = douyinDirectAppManager.findById(param.getId())
                // 抖音: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.douyin.mchAppNotFound"));
        this.assertScopeMatch(entity, param.getMchNo(), param.getChannelMchNo());
        this.assertDouyinAppIdUnique(entity.getMchNo(), entity.getChannelMchNo(), param.getDouyinAppId(), param.getId());
        DouyinDirectAppConvert.CONVERT.copy(param, entity);
        douyinDirectAppManager.updateById(entity);
    }

    /// 删除应用（级联删除授权认证配置）
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        douyinDirectAppManager.findById(id)
                // 抖音: 直连商户应用不存在
                .orElseThrow(() -> new DataNotExistException("error.channel.douyin.mchAppNotFound"));
        douyinDirectAppAuthConfigService.deleteByDouyinDirectAppId(id);
        douyinDirectAppManager.deleteById(id);
    }

    /// 校验同一通道商户下应用ID唯一
    private void assertDouyinAppIdUnique(String mchNo, String channelMchNo, String douyinAppId, Long excludeId) {
        if (this.existsDouyinAppIdByChannel(mchNo, channelMchNo, douyinAppId, excludeId)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.douyin.appIdDuplicate");
        }
    }

    /// 校验操作范围与记录归属一致
    private void assertScopeMatch(DouyinDirectApp entity, String mchNo, String channelMchNo) {
        if (!entity.getMchNo().equals(mchNo) || !entity.getChannelMchNo().equals(channelMchNo)) {
            // 抖音: 直连商户应用不存在或商户号归属不匹配
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.douyin.mchAppNotFound");
        }
    }
}
