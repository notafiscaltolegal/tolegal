package gov.to.persistencia;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

public final class UtilReflexao {

    private static final Logger LOG = Logger.getLogger(UtilReflexao.class);
    
    private UtilReflexao(){
    	// nada a fazer
    }

    public static Map<?, ?> obterAtributos(Object obj) {
        Map<?, ?> mapa = null;

        try {
            mapa = PropertyUtils.describe(obj);
        } catch (IllegalAccessException e) {
            LOG.error("IllegalAccessException", e);
        } catch (InvocationTargetException e) {
            LOG.error("InvocationTargetException", e);
        } catch (NoSuchMethodException e) {
            LOG.error("NoSuchMethodException", e);
        }

        return mapa;
    }
    
    public static Field getFieldByName(Class<?> classe, String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        Field field = null;

        for (Field fieldDeclared : classe.getDeclaredFields()) {
            if (fieldDeclared.getName().equals(name)) {
                field = fieldDeclared;
                break;
            }
        }

        return field;
    }

    public static Field getCampoComAnotacao(Class<?> classe, Class<?> annotationClass, String fieldName) {
        Field field = getFieldByName(classe, fieldName);

        if (field == null) {
            return null;
        }

        for (Annotation declaredAnnotation : field.getDeclaredAnnotations()) {
            if (declaredAnnotation.annotationType().equals(annotationClass)) {
                return field;
            }
        }

        return null;
    }
    
}
