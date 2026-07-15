package cn.daxpay.open.plugin.easypay.service.config;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.plugin.easypay.convert.EasyPayConfigConvert;
import cn.daxpay.open.plugin.easypay.dao.EasyPayConfigManager;
import cn.daxpay.open.plugin.easypay.dao.EasyPayCredentialManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayConfig;
import cn.daxpay.open.plugin.easypay.param.config.EasyPayConfigParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 易支付场景配置服务
///
@Service
@RequiredArgsConstructor
public class EasyPayConfigService {

    private final EasyPayConfigManager easyPayConfigManager;
    private final EasyPayCredentialManager easyPayCredentialManager;

    /// 按 pid 查询（不存在则按凭证创建）
    public EasyPayConfig findByPid(Integer pid) {
        var optional = easyPayConfigManager.findByPid(pid);
        if (optional.isPresent()) {
            return optional.get();
        }
        var credential = easyPayCredentialManager.findByPid(pid)
                .orElseThrow(() -> new DataNotExistException("error.plugin.easypay.credentialNotFound"));
        var config = new EasyPayConfig();
        config.setPid(pid);
        config.setAppId(credential.getAppId());
        config.setMchNo(credential.getMchNo());
        easyPayConfigManager.save(config);
        return config;
    }

    /// 更新场景配置
    @Transactional(rollbackFor = Exception.class)
    public void update(EasyPayConfigParam param) {
        var config = this.findByPid(param.getPid());
        EasyPayConfigConvert.CONVERT.copy(param, config);
        easyPayConfigManager.updateById(config);
    }
}
