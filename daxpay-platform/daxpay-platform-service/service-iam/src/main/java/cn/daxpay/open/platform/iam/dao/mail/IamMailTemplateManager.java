package cn.daxpay.open.platform.iam.dao.mail;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.iam.entity.mail.IamMailTemplate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// 邮件模板自定义
@Repository
@AllArgsConstructor
public class IamMailTemplateManager extends BaseManager<IamMailTemplateMapper, IamMailTemplate> {

    /// 按模板编码与语言查找覆盖配置
    public Optional<IamMailTemplate> findByCodeAndLanguage(String templateCode, String languageCode) {
        return lambdaQuery()
                .eq(IamMailTemplate::getTemplateCode, templateCode)
                .eq(IamMailTemplate::getLanguage, languageCode)
                .oneOpt();
    }

    /// 按模板编码与语言删除覆盖配置(恢复出厂默认)
    public void deleteByCodeAndLanguage(String templateCode, String languageCode) {
        lambdaUpdate()
                .eq(IamMailTemplate::getTemplateCode, templateCode)
                .eq(IamMailTemplate::getLanguage, languageCode)
                .remove();
    }
}
