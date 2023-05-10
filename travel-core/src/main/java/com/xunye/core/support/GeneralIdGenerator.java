//package com.xunye.core.support;
//
//import java.io.Serializable;
//
//import cn.hutool.core.util.IdUtil;
//import com.xunye.core.tools.CheckTools;
//import org.hibernate.HibernateException;
//import org.hibernate.engine.spi.SharedSessionContractImplementor;
//import org.hibernate.id.IdentifierGenerator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class GeneralIdGenerator implements IdentifierGenerator {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralIdGenerator.class);
//
//    @Override
//    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
//        Serializable id = session
//                .getEntityPersister(null, object)
//                .getClassMetadata()
//                .getIdentifier(object, session);
//
//        if (CheckTools.isNullOrEmpty(id)) {
//            id = IdUtil.simpleUUID();
//        }
//        LOGGER.info("---> IdGenerator make id:[{}]，objClass:[{}]", id, object.getClass().getSimpleName());
//        return id;
//    }
//
//}
