package com.xunye.core.base;

import java.io.Serializable;
import java.util.List;

/**
 * 抽象Service层，定义基础CRUD方法
 *
 * @param <EDIT_DTO>  泛型定义: 新增/编辑DTO
 * @param <BASIC_DTO> 泛型定义: 基础DTO
 * @param <E>         泛型定义: 数据实体
 * @param <ID>        泛型定义: 数据主键
 */
public interface BaseService<EDIT_DTO, BASIC_DTO extends BaseDTO, E, ID extends Serializable> {

    /**
     * 获取Repository
     *
     * @return Repository
     */
    BaseRepository<E, ID> getRepository();

    /**
     * 获取Mapper
     *
     * @return Mapper
     */
    BaseMapper<E, BASIC_DTO, EDIT_DTO> getMapper();

    /**
     * 保存/更新数据
     *
     * @param editDTO 编辑DTO
     * @return 数据实体
     */
    E saveOrUpdate(EDIT_DTO editDTO);

    E saveOrUpdateEntity(E entity);

    List<E> saveAll(List<EDIT_DTO> editDTOList);

    /**
     * 根据ID删除实体
     */
    void deleteById(ID id);

    /**
     * 根据ID查询
     *
     * @return 编辑DTO
     */
    EDIT_DTO findById(ID id);

    E findEntryById(ID id);

    BASIC_DTO findDtoById(ID id);

    /**
     * 新建
     */
    void jdbcSaveAll(List<EDIT_DTO> editDTOList);

    void jdbcSaveAllEntity(List<E> entities);

    /**
     * ！！！！！！！！！！！！！！！！！！！！！！！！更新注意：！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
     * ！！！！！！！！！！！！！！！！！！！！！！！！ 局限1：只能按id更新 ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
     * ！！！！！！！！！！！！ ！！！！！！！！！！！！局限2：只能全字段更新，所以需要全部查出来然后更新，否则丢失数据！！！！！！！！！！！！！！！！！！！！！！！！
     */
    void jdbcUpdateAllEntity(List<E> entities);

    void jdbcUpdateAll(List<EDIT_DTO> editDTOList);

}
