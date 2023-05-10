package com.xunye.core.support.orm.interceptor;//package com.nanxun.core.support.orm.interceptor;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//
//import java.util.Collection;
//import java.util.concurrent.CopyOnWriteArrayList;
//
//public class OrmInterceptorFactory implements InitializingBean, ApplicationContextAware {
//
//    private ApplicationContext applicationContext;
//    private static final CopyOnWriteArrayList<IOrmInterceptor> ORM_INTERCEPTORS = new CopyOnWriteArrayList<>();
//
//    @Override
//    public void afterPropertiesSet() {
//        Collection<ITaskHandler> taskHandlerList = applicationContext.getBeansOfType(ITaskHandler.class).values();
//
//        // 对handler做过滤,基于注解
//
//        taskHandlerList.forEach(taskHandler -> {
//            TASK_HANDLER_CONCURRENT_MAP.put(taskHandler.getTaskType(), taskHandler);
//        });
//        log.info("任务处理器注册完成：{}", TASK_HANDLER_CONCURRENT_MAP);
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//
//    /**
//     * 根据任务类型获取对应的处理器
//     *
//     * @param taskTypeEnum 任务类型枚举
//     * @return 具体任务处理器
//     */
//    public ITaskHandler getTaskHandlerByType(TaskTypeEnum taskTypeEnum) {
//        return TASK_HANDLER_CONCURRENT_MAP.get(taskTypeEnum);
//    }
//
//}
