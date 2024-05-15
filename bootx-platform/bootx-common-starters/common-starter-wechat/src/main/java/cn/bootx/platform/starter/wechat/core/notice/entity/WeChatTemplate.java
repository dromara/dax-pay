package cn.bootx.platform.starter.wechat.core.notice.entity;

import cn.bootx.platform.starter.wechat.core.notice.convert.WeChatTemplateConvert;
import cn.bootx.platform.starter.wechat.param.notice.WeChatTemplateParam;
import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpDelEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.bootx.platform.starter.wechat.dto.notice.WeChatTemplateDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;

/**
 * 微信消息模板
 *
 * @author xxm
 * @since 2022-08-03
 */
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("starter_wx_template")
@Accessors(chain = true)
public class WeChatTemplate extends MpDelEntity implements EntityBaseFunction<WeChatTemplateDto> {

    /** 名称 */
    private String name;

    /** 编码 */
    private String code;

    /** 是否启用 */
    private Boolean enable;

    /** 模板ID */
    private String templateId;

    /** 模板标题 */
    private String title;

    /** 模板所属行业的一级行业 */
    private String primaryIndustry;

    /** 模板所属行业的二级行业 */
    private String deputyIndustry;

    /** 模板内容 */
    @BigField
    private String content;

    /** 示例 */
    @BigField
    private String example;

    /** 创建对象 */
    public static WeChatTemplate init(WeChatTemplateParam in) {
        return WeChatTemplateConvert.CONVERT.convert(in);
    }

    public static WeChatTemplate init(WxMpTemplate wxMpTemplate) {
        WeChatTemplate template = WeChatTemplateConvert.CONVERT.convert(wxMpTemplate);
        template.setEnable(true);
        return template;
    }

    /** 转换成dto */
    @Override
    public WeChatTemplateDto toDto() {
        return WeChatTemplateConvert.CONVERT.convert(this);
    }

}
