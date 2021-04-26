package com.xiaofu.rpc.consumer.consumer;

import com.xiaofu.rpc.consumer.annotation.RpcReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaofu
 * @Date: 2021/4/26 10:57
 */
@Component
public class RpcConsumerPostProcessor implements BeanClassLoaderAware, ApplicationContextAware, BeanFactoryPostProcessor {


    private ClassLoader classLoader;

    private ApplicationContext applicationContext;


    /**
     * 用来保存解析过程中，bean定义的映射关系
     */
    private static Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
       this.classLoader = classLoader;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //时机：在bean初始化之前
        //1.从beanFactory获取所有的bean的定义信息
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            //2.逐个去获取bean的定义信息
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            //3.获取每个bean的类名称
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null){
                //4.根据bean的名称获取到对应的Class对象
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, classLoader);
                //5.判断类对象的属性是否包含了@RpcReference注解
                //存在，则需要构建对应的对象
                ReflectionUtils.doWithFields(clazz, new ReflectionUtils.FieldCallback() {
                    @Override
                    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                        //得到这个类的每一个属性 field
                        parseRpcReference(field);
                    }
                });
            }
        }
        //6.解析完毕之后，将bean注册到Spring容器中
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        beanDefinitionMap.forEach((beanName,beanDef)->{
            //避免重复注册的问题
            //基于容器的上下文对象来判断当前类是否已经存在
            if (applicationContext.containsBean(beanName)){
                throw new IllegalArgumentException("Spring context already has bean: "+beanName);
            }
            //注册bean
            registry.registerBeanDefinition(beanName,beanDef);
        });
    }

    /**
     * 判断类对象的属性是否包含了@RpcReference
     * 存在，则需要构建出对应的对象
     * @param field
     */
    private void parseRpcReference(Field field) {
        //判断类对象的属性是否包含了@RpcReference注解
        //存在，则需要构建出对应的对象
        //1.获取当前属性的注解信息
        RpcReference annotation = AnnotationUtils.getAnnotation(field, RpcReference.class);
        //2.判断注解是否为空
        if (annotation != null){
            //说明该属性存在@RpcReference注解
            //3.基于RpcReferenceBean完成bean的实例化
            BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
            //设置初始化方法init
            definitionBuilder.setInitMethodName("init");
            //4.为其成员变量赋值
            definitionBuilder.addPropertyValue("interfaceClass",field.getType());
            definitionBuilder.addPropertyValue("registryType",annotation.registryType());
            definitionBuilder.addPropertyValue("registryAddr",annotation.registryAddr());
            definitionBuilder.addPropertyValue("version",annotation.version());
            definitionBuilder.addPropertyValue("timeout",annotation.timeout());
            //5.基于这些属性构建
            AbstractBeanDefinition beanDefinition = definitionBuilder.getBeanDefinition();
            //将这个结果保存到map中
            beanDefinitionMap.put(field.getName(),beanDefinition);
        }
    }


}




















