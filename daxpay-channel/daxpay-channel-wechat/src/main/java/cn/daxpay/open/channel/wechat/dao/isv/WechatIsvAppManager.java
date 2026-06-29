package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/// # 微信服务商应用
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAppManager extends BaseManager<WechatIsvAppMapper, WechatIsvApp> {

    /// 查询全部应用（按创建时间升序，先创建的在前）
    public List<WechatIsvApp> listAll() {
        return lambdaQuery()
                .orderByAsc(WechatIsvApp::getCreateTime)
                .orderByAsc(WechatIsvApp::getId)
                .list();
    }

    /// 查询第一个应用（运行时默认使用）
    public Optional<WechatIsvApp> findFirst() {
        return firstOpt(q -> q
                .orderByAsc(WechatIsvApp::getCreateTime)
                .orderByAsc(WechatIsvApp::getId));
    }

    /// 校验微信应用AppId是否已存在(排除自身)
    public boolean existsByWxAppId(String wxAppId, Long excludeId) {
        return lambdaQuery()
                .eq(WechatIsvApp::getWxAppId, wxAppId)
                .ne(excludeId != null, WechatIsvApp::getId, excludeId)
                .exists();
    }
}
