package cn.bootx.platform.daxpay.service.core.channel.union.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.common.mybatisplus.handler.StringListTypeHandler;
import cn.bootx.platform.daxpay.service.core.channel.union.convert.UnionPayConvert;
import cn.bootx.platform.daxpay.service.dto.channel.union.UnionPayConfigDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 云闪付支付配置
 *
 * @author xxm
 * @since 2022/3/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "云闪付支付配置")
@Accessors(chain = true)
@TableName(value = "pay_union_pay_config",autoResultMap = true)
public class UnionPayConfig extends MpBaseEntity implements EntityBaseFunction<UnionPayConfigDto> {

    /** 商户号 */
    @DbColumn(comment = "商户号")
    private String machId;

    /** 是否启用, 只影响支付和退款操作 */
    @DbColumn(comment = "是否启用")
    private Boolean enable;

    /** 密钥 */
    @DbColumn(comment = "密钥")
    private String appKey;

    /** 支付网关地址 */
    @DbColumn(comment = "支付网关地址")
    private String serverUrl;

    /**
     * 服务器异步通知页面路径, 需要填写本网关服务的地址, 不可以直接填写业务系统的地址
     * 1. 需http://或者https://格式的完整路径，
     * 2. 不能加?id=123这类自定义参数，必须外网可以正常访问
     * 3. 消息顺序 银联网关 -> 本网关进行处理 -> 发送消息通知业务系统
     */
    @DbColumn(comment = "异步通知路径")
    private String notifyUrl;

    /** 可用支付方式 */
    @DbColumn(comment = "可用支付方式")
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @TableField(typeHandler = StringListTypeHandler.class)
    private List<String> payWays;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;

    @Override
    public UnionPayConfigDto toDto() {
        return UnionPayConvert.CONVERT.convert(this);
    }

}
