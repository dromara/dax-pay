package cn.bootx.platform.iam.core.client.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.ResultConvertUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.iam.param.client.ClientParam;
import cn.bootx.platform.iam.core.client.dao.ClientManager;
import cn.bootx.platform.iam.core.client.entity.Client;
import cn.bootx.platform.iam.dto.client.ClientDto;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (CollUtil.isNotEmpty(param.getLoginTypeIdList())) {
            client.setLoginTypeIds(String.join(",", param.getLoginTypeIdList()));
        }
        else {
            client.setLoginTypeIds("");
        }
        clientManager.updateById(client);
    }

    /**
     * 分页
     */
    public PageResult<ClientDto> page(PageParam pageParam, ClientParam clientParam) {
        return MpUtil.convert2DtoPageResult(clientManager.page(pageParam, clientParam));
    }

    /**
     * 获取单条
     */
    public ClientDto findById(Long id) {
        return clientManager.findById(id).map(Client::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 获取全部
     */
    public List<ClientDto> findAll() {
        return ResultConvertUtil.dtoListConvert(clientManager.findAll());
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
