package cn.daxpay.open.platform.iam.entity.mail;

import cn.daxpay.open.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// 邮件模板自定义(运营端按场景按语言覆盖出厂默认槽位)
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("iam_mail_template")
public class IamMailTemplate extends MpBaseEntity {

    /// 模板编码(关联 EmailTemplateEnum.templateName)
    private String templateCode;

    /// 语言(zh/en, 关联 MailLanguageEnum.code)
    private String language;

    /// 邮件主题
    private String subject;

    /// 邮件标题
    private String title;

    /// 正文文案(纯文本, 支持 {变量} 占位符)
    private String content;

    /// 提示行文案(可空, 空则不输出该行)
    private String tip;

    /// 页脚文案(可空, 空则不输出该行)
    private String footer;
}
