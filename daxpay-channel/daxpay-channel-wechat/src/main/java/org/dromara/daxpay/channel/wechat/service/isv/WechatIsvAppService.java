package org.dromara.daxpay.channel.wechat.service.isv;

import org.dromara.daxpay.channel.wechat.convert.isv.WechatIsvAppConvert;
import org.dromara.daxpay.channel.wechat.dao.isv.WechatIsvAppManager;
import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvApp;
import org.dromara.daxpay.channel.wechat.code.WechatIsvAppTypeEnum;
import org.dromara.daxpay.channel.wechat.param.isv.WechatIsvAppParam;
import org.dromara.daxpay.channel.wechat.result.isv.WechatIsvAppResult;
import org.dromara.daxpay.channel.wechat.service.isv.WechatIsvAppAuthConfigService;
import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/// # 微信服务商应用管理
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAppService {

    private final WechatIsvAppManager wechatIsvAppManager;
    private final WechatIsvAppAuthConfigService wechatIsvAppAuthConfigService;

    /// 查询全部应用列表
    public List<WechatIsvAppResult> listAll() {
        List<WechatIsvApp> list = wechatIsvAppManager.listAll();
        return MpUtil.toListResult(list);
    }

    /// 根据ID查询应用详情
    public WechatIsvAppResult findById(Long id) {
        return wechatIsvAppManager.findById(id)
                .map(WechatIsvApp::toResult)
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.appNotFound"));
    }

    /// 查询第一个应用（运行时默认使用）
    public Optional<WechatIsvApp> findFirst() {
        return wechatIsvAppManager.findFirst();
    }

    /// 判断微信应用AppId是否已存在
    public boolean existsWxAppId(String wxAppId, Long excludeId) {
        if (StrUtil.isBlank(wxAppId)) {
            return false;
        }
        return wechatIsvAppManager.existsByWxAppId(wxAppId, excludeId);
    }

    /// 新增微信服务商应用
    public void add(WechatIsvAppParam param) {
        this.assertWxAppIdUnique(param.getWxAppId(), null);
        this.validateAppType(param.getAppType());
        WechatIsvApp entity = WechatIsvAppConvert.CONVERT.toEntity(param);
        wechatIsvAppManager.save(entity);
    }

    /// 更新微信服务商应用
    public void update(WechatIsvAppParam param) {
        WechatIsvApp entity = wechatIsvAppManager.findById(param.getId())
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.appNotFound"));
        this.assertWxAppIdUnique(param.getWxAppId(), param.getId());
        WechatIsvAppConvert.CONVERT.copy(param, entity);
        wechatIsvAppManager.updateById(entity);
    }

    /// 删除应用
    public void delete(Long id) {
        wechatIsvAppManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.channel.wechat.appNotFound"));
        wechatIsvAppAuthConfigService.deleteByWechatIsvAppId(id);
        wechatIsvAppManager.deleteById(id);
    }

    /// 校验应用AppId唯一
    private void assertWxAppIdUnique(String wxAppId, Long excludeId) {
        if (this.existsWxAppId(wxAppId, excludeId)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.appIdDuplicate");
        }
    }

    /// 校验应用类型
    private void validateAppType(String appType) {
        boolean valid = Arrays.stream(WechatIsvAppTypeEnum.values())
                .anyMatch(item -> item.getCode().equals(appType));
        if (!valid) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "error.channel.wechat.appTypeInvalid");
        }
    }
}
