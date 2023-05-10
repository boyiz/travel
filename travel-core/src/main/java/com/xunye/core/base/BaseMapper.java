package com.xunye.core.base;

import java.util.List;

public interface BaseMapper<E, BASIC_DTO extends BaseDTO, EDIT_DTO> {

    /**
     * 新增/编辑DTO => 数据实体
     *
     * @param editDTO 新增/编辑DTO
     * @return 数据实体
     */
    E toEntity(EDIT_DTO editDTO);

    /**
     * 基础DTO => 数据实体
     *
     * @param basicDTO 基础DTO
     * @return 数据实体
     */
    E toEntity(BASIC_DTO basicDTO);

    /**
     * 数据实体 => 基础DTO
     *
     * @param entity 数据实体
     * @return 基础DTO
     */
    BASIC_DTO toBasicDTO(E entity);

    /**
     * 数据实体 => 新增/编辑DTO
     *
     * @param entity 数据实体
     * @return 新增/编辑DTO
     */
    EDIT_DTO toEditDTO(E entity);

    /**
     * 基础DTO集合 => 数据实体集合
     *
     * @param basicDtoList 基础DTO集合
     * @return 数据实体集合
     */
    List<E> toEntitiesByBasicDTOL(List<BASIC_DTO> basicDtoList);

    /**
     * 数据实体集合 => 基础DTO集合
     *
     * @param entityList 数据实体集合
     * @return 基础DTO集合
     */
    List<BASIC_DTO> toBasicDTOL(List<E> entityList);

    /**
     * 新增/编辑DTO集合 => 数据实体集合
     *
     * @param editDtoList 新增/编辑DTO集合
     * @return 数据实体集合
     */
    List<E> toEntitiesByEditDTOL(List<EDIT_DTO> editDtoList);

    /**
     * 数据实体集合 => 新增/编辑DTO集合
     *
     * @param entityList 数据实体集合
     * @return 新增/编辑DTO集合
     */
    List<EDIT_DTO> toEditDTOL(List<E> entityList);

}
