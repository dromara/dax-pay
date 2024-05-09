package cn.bootx.platform.iam.core.client.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.iam.core.client.convert.ClientConvert;
import cn.bootx.platform.iam.dto.client.ClientDto;
import cn.bootx.platform.iam.param.client.ClientParam;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证终端
 *
 * @author xxm
 * @since 2021/8/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("iam_client")
@Accessors(chain = true)
public class Client extends MpBaseEntity implements EntityBaseFunction<ClientDto> {

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 是否系统内置 */
    private boolean internal;

    /** 是否可用 */
    private boolean enable;

    /** 关联登录方式 */
    private String loginTypeIds;

    /** 新注册的用户是否默认赋予该终端 */
    @DbColumn(comment = "新注册的用户是否默认赋予该终端")
    private Boolean defaultEndow;

    /** 描述 */
    private String description;

    /** 创建对象 */
    public static Client init(ClientParam in) {
        Client client = ClientConvert.CONVERT.convert(in);
        if (CollUtil.isNotEmpty(in.getLoginTypeIdList())) {
            String loginTypeIds = String.join(",", in.getLoginTypeIdList());
            client.setLoginTypeIds(loginTypeIds);
        }
        return client;
    }

    /** 转换成dto */
    @Override
    public ClientDto toDto() {
        ClientDto client = ClientConvert.CONVERT.convert(this);
        if (StrUtil.isNotBlank(this.getLoginTypeIds())) {
            List<String> collect = Arrays.stream(this.getLoginTypeIds().split(",")).collect(Collectors.toList());
            client.setLoginTypeIdList(collect);
        }
        return client;
    }

}
