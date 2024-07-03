package cn.bootx.platform.iam.service.client;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.bootx.platform.iam.dao.client.ClientManager;
import cn.bootx.platform.iam.entity.client.Client;
import cn.bootx.platform.iam.param.client.ClientParam;
import cn.bootx.platform.iam.result.client.ClientResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证应用
 *
 * @author xxm
 * @since 2022-06-27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientManager clientManager;

    /**
     * 添加
     */
    public void add(ClientParam param) {
        Client client = Client.init(param);
        clientManager.save(client);
    }

    /**
     * 修改
     */
    public void update(ClientParam param) {
        Client client = clientManager.findById(param.getId()).orElseThrow(DataNotExistException::new);
        BeanUtil.copyProperties(param, client, CopyOptions.create().ignoreNullValue());
        clientManager.updateById(client);
    }

    /**
     * 分页
     */
    public PageResult<ClientResult> page(PageParam pageParam, ClientParam clientParam) {
        return MpUtil.toPageResult(clientManager.page(pageParam, clientParam));
    }

    /**
     * 获取单条
     */
    public ClientResult findById(Long id) {
        return clientManager.findById(id).map(Client::toResult).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<ClientResult> findAll() {
        return clientManager.findAll().stream().map(Client::toResult).collect(Collectors.toList());
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        clientManager.deleteById(id);
    }

    /**
     * 编码是否已经存在
     */
    public boolean existsByCode(String code) {
        return clientManager.existsByCode(code);
    }

    /**
     * 编码是否已经存在(不包含自身)
     */
    public boolean existsByCode(String code, Long id) {
        return clientManager.existsByCode(code, id);
    }

}
