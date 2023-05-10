package com.xunye.core.base;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import cn.hutool.core.util.ReflectUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xunye.core.exception.BusinessException;
import com.xunye.core.tools.ReflectUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 抽象服务层实现
 *
 * @param <EDIT_DTO>  泛型定义: 新增/编辑DTO
 * @param <BASIC_DTO> 泛型定义: 基础DTO
 * @param <E>         泛型定义: 数据实体
 * @param <ID>        泛型定义: 数据主键
 */
public abstract class BaseServiceImpl<EDIT_DTO, BASIC_DTO extends BaseDTO, E, ID extends Serializable>
        implements BaseService<EDIT_DTO, BASIC_DTO, E, ID> {

    protected static final String EMPTY_STR = "";

    @Resource
    private JPAQueryFactory jpaQueryFactory;

    @Resource
    private JdbcTemplate jdbcTemplate;

    //@Autowired
    //private RedisHelper redisHelper;

    @Autowired
    private EntityManager entityManager;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 获取JPAQueryFactory
     * 供子类使用
     */
    protected JPAQueryFactory getJpaQueryFactory() {
        return jpaQueryFactory;
    }

    /**
     * 获取JdbcTemplate
     * 供子类使用
     */
    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * 获取redisHelper
     * 供子类使用
     */
    //protected RedisHelper getRedisHelper() {
    //    return redisHelper;
    //}

    /**
     * 获取数据库Session
     */
    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }


    protected ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    /**
     * 释放持久态
     */
    protected void evict(Object entity) {
        //检查对象是否是持久化态
        if (entityManager.contains(entity)) {
            //获取session
            Session session = getSession();
            //转换成游离态
            session.evict(entity);
        }
    }

    /**
     * 保存/更新数据
     */
    @Override
    public E saveOrUpdate(EDIT_DTO editDTO) {
        E entity = getMapper().toEntity(editDTO);
        return getRepository().saveAndFlush(entity);
    }

    @Override
    public E saveOrUpdateEntity(E entity) {
        return getRepository().save(entity);
    }

    @Override
    public List<E> saveAll(List<EDIT_DTO> editDTOList) {
        List<E> entities = getMapper().toEntitiesByEditDTOL(editDTOList);
        for (E entity : entities) {
            getRepository().saveAndFlush(entity);
        }
        return entities;
    }

    /**
     * 根据ID删除实体
     */
    @Override
    public void deleteById(ID id) {
        // todo： 后面这边可以拓展logic delete
        getRepository().deleteById(id);
        entityManager.flush();
    }

    /**
     * 根据ID查询
     */
    @Override
    public EDIT_DTO findById(ID id) {
        return getRepository().findById(id)
                .map(entity -> getMapper().toEditDTO(entity))
                .orElse(null);
    }

    @Override
    public E findEntryById(ID id) {
        return getRepository().findById(id)
                .orElse(null);
    }

    @Override
    public BASIC_DTO findDtoById(ID id) {
        return getRepository().findById(id)
                .map(entity -> getMapper().toBasicDTO(entity))
                .orElse(null);
    }

    /**
     * --------------------------------------------- 批量插入 ---------------------------------------------
     */
    /**
     * private，通过index找到值
     */
    private Object getValueByIdx(Object object, List<TblField> fields, int idx) {
        String objFieldName = fields.get(idx).getObjFieldName();

        try {
            //todo，目前顺序是一样的，通过index做遍历匹配更加可靠
            return ReflectUtils.getValueOfGetIncludeObjectFeild(object, objFieldName);
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    private Object getValueOfId(Object object) {

        try {
            //todo，目前顺序是一样的，通过index做遍历匹配更加可靠
            return ReflectUtils.getValueOfGetIncludeObjectFeild(object, "id");
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    /**
     * private，生成前半段
     */
    private String genFirstHalf(String tableName, List<TblField> fieldList) {
        StringBuilder fields = new StringBuilder("(");
        StringBuilder placeOn = new StringBuilder("(");
        for (int i = 0; i < fieldList.size(); i++) {
            TblField tblField = fieldList.get(i);
            if (i != fieldList.size() - 1) {
                fields.append(tblField.getDbFieldName()).append(", ");
                placeOn.append("?,");
            } else {
                fields.append(tblField.getDbFieldName());
                placeOn.append("?");
            }
        }
        fields.append(") ");
        placeOn.append(") ");

        return "INSERT INTO " + tableName + " " + fields.toString() + " VALUES " + placeOn.toString();
    }

    private void genPs(PreparedStatement ps, Object object, List<TblField> fields) {
        try {
            int cyc = 0;
            for (int idx = 0; idx < fields.size(); idx++) {
                if (fields.get(idx).getDbFieldName().equals("id")) {
                    if (getValueByIdx(object, fields, idx) == null) {
                        String newId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
                        ReflectUtil.setFieldValue(object, "id", newId);
                        ps.setObject(idx + 1, newId);
                    } else {
                        ps.setObject(idx + 1, getValueByIdx(object, fields, idx));
                    }
                } else if (fields.get(idx).getDbFieldName().equals("create_time")) {
                    if (getValueByIdx(object, fields, idx) == null) {
                        ps.setObject(idx + 1, new java.util.Date());
                    } else {
                        ps.setObject(idx + 1, getValueByIdx(object, fields, idx));
                    }
                } else if (fields.get(idx).getDbFieldName().equals("update_time")) {
                    if (getValueByIdx(object, fields, idx) == null) {
                        ps.setObject(idx + 1, new java.util.Date());
                    } else {
                        ps.setObject(idx + 1, getValueByIdx(object, fields, idx));
                    }
                } else {
                    ps.setObject(idx + 1, getValueByIdx(object, fields, idx));
                }
            }
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
        return;
    }

    @Override
    public void jdbcSaveAllEntity(List<E> entities) {
        long startTime = System.currentTimeMillis();

        if (entities == null || entities.size() == 0) {
            System.out.printf("错误:未找到对象名");
            return;
        }

        Class<?> cls = entities.get(0).getClass();

        List<TblField> fields = ReflectUtils.getTableFields(cls);
        String tableName = ReflectUtils.getTableName(cls);

        if (tableName == null) {
            System.out.printf("错误:未找到对象名");
            return;
        }
        String firstHalf = genFirstHalf(tableName, fields);
        System.out.printf("firstHalf:%s\n", firstHalf);
        jdbcTemplate.batchUpdate(firstHalf, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object object = entities.get(i);
                genPs(ps, object, fields);
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });

        System.out.printf("用时[%d]毫秒,sql:[%s]\n", System.currentTimeMillis() - startTime, firstHalf);

        //todo ，返回真实保存的条数
        return;
    }


    @Override
    public void jdbcSaveAll(List<EDIT_DTO> editDTOList) {
        if (editDTOList == null || editDTOList.size() == 0) {
            return;
        }

        List<E> entities = getMapper().toEntitiesByEditDTOL(editDTOList);

        jdbcSaveAllEntity(entities);
    }

    /**
     * --------------------------------------------- 批量更新 ---------------------------------------------
     */
    private void genPsOfUpdate(PreparedStatement ps, Object object, List<TblField> fields) {
        try {
            int cyc = 0;
            for (int idx = 0; idx < fields.size(); idx++) {
                if (fields.get(idx).getDbFieldName().equals("id")) {
                    if (getValueByIdx(object, fields, idx) == null) {
                        throw new BusinessException("更新的实体不能没有ID");
//                        ps.setObject(idx + 1, UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
                    } else {
                        ps.setObject(idx + 1, getValueByIdx(object, fields, idx));
                    }
                } else if (fields.get(idx).getDbFieldName().equals("create_time")) {
                    if (getValueByIdx(object, fields, idx) == null) {
                        ps.setObject(idx + 1, new java.util.Date());
                    } else {
                        ps.setObject(idx + 1, getValueByIdx(object, fields, idx));
                    }
                } else if (fields.get(idx).getDbFieldName().equals("update_time")) {
                    if (getValueByIdx(object, fields, idx) == null) {
                        ps.setObject(idx + 1, new java.util.Date());
                    } else {
                        ps.setObject(idx + 1, getValueByIdx(object, fields, idx));
                    }
                } else {
                    ps.setObject(idx + 1, getValueByIdx(object, fields, idx));
                }
            }
            ps.setObject(fields.size() + 1, getValueOfId(object));
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
        return;
    }

    /**
     * private，生成前半段
     */
    private String genFirstHalfOfUpdate(String tableName, List<TblField> fieldList) {
        String sql = "UPDATE " + tableName + " set ";
        for (int i = 0; i < fieldList.size(); i++) {
            TblField tblField = fieldList.get(i);
            if (i != fieldList.size() - 1) {
                sql = sql + tblField.getDbFieldName() + " = ? ,";
            } else {
                sql = sql + tblField.getDbFieldName() + " = ? ";
            }
        }

        sql = sql + " where id = ?";
        return sql;
    }

    @Override
    public void jdbcUpdateAllEntity(List<E> entities) {
        long startTime = System.currentTimeMillis();

        if (entities == null || entities.size() == 0) {
            System.out.printf("错误:未找到对象名");
            return;
        }

        Class<?> cls = entities.get(0).getClass();

        List<TblField> fields = ReflectUtils.getTableFields(cls);
        String tableName = ReflectUtils.getTableName(cls);

        if (tableName == null) {
            System.out.printf("错误:未找到对象名");
            return;
        }
        String firstHalf = genFirstHalfOfUpdate(tableName, fields);
        System.out.printf("firstHalf:%s\n", firstHalf);
        jdbcTemplate.batchUpdate(firstHalf, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Object object = entities.get(i);
                genPsOfUpdate(ps, object, fields);
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }
        });

        System.out.printf("用时[%d]毫秒,sql:[%s]\n", System.currentTimeMillis() - startTime, firstHalf);

        // 清除缓存，防止其他查询查不到更新
        entityManager.clear();

        //todo ，返回真实保存的条数
        return;
    }

    @Override
    public void jdbcUpdateAll(List<EDIT_DTO> editDTOList) {
        if (editDTOList == null || editDTOList.size() == 0) {
            return;
        }

        List<E> entities = getMapper().toEntitiesByEditDTOL(editDTOList);

        jdbcUpdateAllEntity(entities);
    }

}
