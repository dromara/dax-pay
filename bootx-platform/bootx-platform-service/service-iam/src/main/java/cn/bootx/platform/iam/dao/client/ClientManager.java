package cn.bootx.platform.iam.dao.client;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.iam.entity.client.Client;
import cn.bootx.platform.iam.param.client.ClientParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 认证应用
 *
 * @author xxm
 * @since 2022-06-27
 */
@Repository
@RequiredArgsConstructor
public class ClientManager extends BaseManager<ClientMapper, Client> {

    /**
     * 分页
     */
    public Page<Client> page(PageParam pageParam, ClientParam param) {
        Page<Client> mpPage = MpUtil.getMpPage(pageParam);
        return lambdaQuery().like(StrUtil.isNotBlank(param.getCode()), Client::getCode, param.getCode())
            .like(StrUtil.isNotBlank(param.getName()), Client::getName, param.getName())
            .orderByDesc(MpIdEntity::getId)
            .page(mpPage);
    }

    public Optional<Client> findByCode(String code) {
        return findByField(Client::getCode, code);
    }

    public boolean existsByCode(String code) {
        return existedByField(Client::getCode, code);
    }

    public boolean existsByCode(String code, Long id) {
        return existedByField(Client::getCode, code, id);
    }

}
