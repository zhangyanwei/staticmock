package com.worescloud.os.staticmock;

import javassist.*;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static com.worescloud.os.staticmock.exception.Reporter.notAnEnhancedClass;
import static com.worescloud.os.staticmock.exception.Reporter.notAnInitializeEnhancedClass;
import static java.lang.String.format;
import static javassist.Modifier.*;

public final class StaticMockito {

	private static final String FIELD_NAME = "_MOCK_OBJECT";

	public static void enhanceClass(String className) {

		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass realClass = pool.getAndRename(className, className + "$_Copy");
			CtClass ctClass = pool.get(className);

			ctClass.setModifiers(ctClass.getModifiers() & ~FINAL);
			CtClass staticMockerClass = pool.makeClass(className + "$_StaticMockInterface", ctClass);

			CtField defField = new CtField(staticMockerClass, FIELD_NAME, ctClass);
			defField.setModifiers(STATIC | PUBLIC);
			ctClass.addField(defField);

			CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
			for (CtMethod declaredMethod : declaredMethods) {
				if ((declaredMethod.getModifiers() & STATIC) == 0) {
					continue;
				}

				declaredMethod.setModifiers(declaredMethod.getModifiers() & ~FINAL);
				boolean voidType = declaredMethod.getReturnType() == CtClass.voidType;
				CtMethod method = CtNewMethod.make(
						declaredMethod.getReturnType(),
						declaredMethod.getName(),
						declaredMethod.getParameterTypes(),
						declaredMethod.getExceptionTypes(),
						toBody(realClass.getName(), declaredMethod, voidType),
						staticMockerClass
				);
				staticMockerClass.addMethod(method);
				declaredMethod.setBody(toBody(FIELD_NAME, declaredMethod, voidType));
			}

			// Load the modified class into current class loader.
			ctClass.toClass();
			realClass.toClass();
			staticMockerClass.toClass();
		} catch (NotFoundException | CannotCompileException ignore) {
			ignore.printStackTrace();
		}
	}

	public static void initMock(Class enhanced) {
		try {
			Field field = enhanced.getDeclaredField(FIELD_NAME);
			field.set(null, Mockito.mock(field.getType()));
		} catch (IllegalAccessException | NoSuchFieldException ignore) {
			notAnEnhancedClass();
		}
	}

	public static <T> T mocker(Class<T> enhanced) {
		Object innerMock = null;
		try {
			Field field = enhanced.getDeclaredField(FIELD_NAME);
			innerMock = field.get(null);
			if (innerMock == null) {
				notAnInitializeEnhancedClass();
			}
		} catch (NoSuchFieldException | IllegalAccessException e) {
			notAnEnhancedClass();
		}

		//noinspection unchecked
		return (T) innerMock;
	}

	private static String toBody(String fieldName, CtMethod declaredMethod, boolean voidType) {
		return format("{%s %s.%s($$);}", voidType ? "" : "return", fieldName, declaredMethod.getName());
	}

}