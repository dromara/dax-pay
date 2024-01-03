package cn.bootx.platform.daxpay.service.core.system.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.service.core.system.convert.WechatNoticeConfigConvert;
import cn.bootx.platform.daxpay.service.dto.system.WechatNoticeConfigDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信消息通知相关配置
 * @author xxm
 * @since 2023/12/24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "微信消息通知相关配置")
@TableName("pay_wechat_notice_config")
public class WechatNoticeConfig extends MpBaseEntity implements EntityBaseFunction<WechatNoticeConfigDto> {

    @DbColumn(comment = "应用id")
    private String appId;
    @DbColumn(comment = "应用秘钥")
    private String appSecret;
    @DbColumn(comment = "公众号二维码")
    private String qrUrl;
    @DbColumn(comment = "OAuth2地址")
    private String oauth2Url;
    @DbColumn(comment = "微信校验文件名称")
    private String verifyFileName;
    @DbColumn(comment = "微信校验文件内容")
    private String verifyFileContent;
    @DbColumn(comment = "模板消息Id")
    private String templateId;
    @DbColumn(comment = "模板消息内容")
    private String templateContent;
    @DbColumn(comment = "模板消息备注")
    private String templateRemark;

    /**
     * 转换
     */
    @Override
    public WechatNoticeConfigDto toDto() {
        return WechatNoticeConfigConvert.CONVERT.convert(this);
    }
}
