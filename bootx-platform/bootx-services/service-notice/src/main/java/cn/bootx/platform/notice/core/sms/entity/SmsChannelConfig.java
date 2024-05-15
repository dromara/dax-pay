package cn.bootx.platform.notice.core.sms.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.notice.core.sms.convert.SmsChannelConfigConvert;
import cn.bootx.platform.notice.dto.sms.SmsChannelConfigDto;
import cn.bootx.platform.notice.param.sms.SmsChannelConfigParam;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.sms4j.provider.enumerate.SupplierType;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.ALWAYS;
import static com.baomidou.mybatisplus.annotation.FieldStrategy.NEVER;

/**
 * 短信渠道配置
 * @author xxm
 * @since 2023/3/28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("notice_sms_channel_config")
public class SmsChannelConfig extends MpBaseEntity implements EntityBaseFunction<SmsChannelConfigDto> {

    /**
     * 渠道类型编码
     * @see SupplierType#name()
     */
    @DbColumn(comment = "渠道类型编码")
    @TableField(updateStrategy = NEVER)
    private String code;

    /**
     * 渠道类型名称
     * @see SupplierType#getName()
     */
    @DbColumn(comment = "渠道类型名称")
    private String name;

    /**
     * 状态
     * @see cn.bootx.platform.notice.code.SmsChannelStatusCode
     */
    @DbColumn(comment = "状态")
    private String state;

    /** AccessKey */
    @DbColumn(comment = "AccessKey")
    private String accessKey;

    /** AccessSecret */
    @DbColumn(comment = "AccessSecret")
    private String accessSecret;

    /** 配置字符串 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.TEXT)
    @DbColumn(comment = "配置字符串")
    @TableField(updateStrategy = ALWAYS)
    private String config;

    /** 图片 */
    @TableField(updateStrategy = ALWAYS)
    @DbColumn(comment = "图片")
    private Long image;

    /** 排序 */
    @DbColumn(comment = "排序")
    private Double sortNo;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    /** 创建对象 */
    public static SmsChannelConfig init(SmsChannelConfigParam in) {
        return SmsChannelConfigConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public SmsChannelConfigDto toDto() {
        return SmsChannelConfigConvert.CONVERT.convert(this);
    }
}
