package com.monkesoft.oura.mybatis.interceptor;

import com.monkesoft.oura.data.IDataStoreProcess;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Properties;

@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class),
})
@ConditionalOnProperty(value = "oura.crypt", havingValue = "true")
@Component
public class ParameterInterceptor implements Interceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IDataStoreProcess dbProcess;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        logger.info("拦截器ParamInterceptor:"+invocation.getTarget());
        //拦截 ParameterHandler 的 setParameters 方法 动态设置参数
        if (invocation.getTarget() instanceof ParameterHandler) {
            ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
            PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];

            // 反射获取 BoundSql 对象，此对象包含生成的sql和sql的参数map映射
			/*Field boundSqlField = parameterHandler.getClass().getDeclaredField("boundSql");
			boundSqlField.setAccessible(true);
			BoundSql boundSql = (BoundSql) boundSqlField.get(parameterHandler);*/

            // 反射获取 参数对像
            Field parameterField =
                    parameterHandler.getClass().getDeclaredField("parameterObject");
            parameterField.setAccessible(true);
            Object parameterObject = parameterField.get(parameterHandler);
            if (Objects.nonNull(parameterObject)) {
                Class<?> parameterObjectClass = parameterObject.getClass();
//                EncryptDecryptClass encryptDecryptClass = AnnotationUtils.findAnnotation(parameterObjectClass, EncryptDecryptClass.class);
//                if (Objects.nonNull(encryptDecryptClass)) {
//                    Field[] declaredFields = parameterObjectClass.getDeclaredFields();
//
//                    final Object encrypt = encryptDecrypt.encrypt(declaredFields, parameterObject);
//                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}