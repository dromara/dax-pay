package cn.bootx.platform.starter.wechat.core.menu.entity;

import cn.bootx.platform.starter.wechat.core.menu.convert.WeChatMenuConvert;
import cn.bootx.platform.starter.wechat.param.menu.WeChatMenuParam;
import cn.bootx.platform.common.core.annotation.BigField;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.starter.wechat.core.menu.domin.WeChatMenuInfo;
import cn.bootx.platform.starter.wechat.dto.menu.WeChatMenuDto;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 微信自定义菜单
 *
 * @author xxm
 * @since 2022-08-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName(value = "starter_wx_menu", autoResultMap = true)
public class WeChatMenu extends MpBaseEntity implements EntityBaseFunction<WeChatMenuDto> {

    /** 名称 */
    private String name;

    /** 菜单信息 */
    @BigField
    @TableField(typeHandler = JacksonTypeHandler.class)
    private WeChatMenuInfo menuInfo;

    /** 是否发布 */
    private boolean publish;

    /** 备注 */
    private String remark;

    /** 创建对象 */
    public static WeChatMenu init(WeChatMenuParam in) {
        return WeChatMenuConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public WeChatMenuDto toDto() {
        return WeChatMenuConvert.CONVERT.convert(this);
    }

}
