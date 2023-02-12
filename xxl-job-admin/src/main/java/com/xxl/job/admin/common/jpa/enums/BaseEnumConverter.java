package com.xxl.job.admin.common.jpa.enums;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author sweeter
 * @date 2021/6/20
 */
public abstract class BaseEnumConverter <T extends Enumerable> implements AttributeConverter<Enumerable, Integer> {

    private Class<T> tClass = null;

    public BaseEnumConverter() {
    }

    private Class<T> getTargetClass() {
        if (this.tClass == null) {
            Type[] actualTypeArguments = ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments();
            this.tClass = actualTypeArguments != null && actualTypeArguments.length != 0 ? (Class)actualTypeArguments[0] : null;
        }
        return this.tClass;
    }

    @Override
    public Integer convertToDatabaseColumn(Enumerable attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T convertToEntityAttribute(Integer dbData) {
        Class<? extends Enumerable> fieldClass = this.getTargetClass();
        if (fieldClass != null && fieldClass.isEnum()) {
            for (Enumerable enumConstant : fieldClass.getEnumConstants()) {
                if (enumConstant != null && dbData != null && enumConstant.getValue() == dbData) {
                    return (T) enumConstant;
                }
            }
        }
        return null;
    }
}
