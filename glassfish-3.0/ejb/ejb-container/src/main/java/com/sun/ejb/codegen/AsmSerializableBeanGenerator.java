/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.ejb.codegen;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;


import java.io.Serializable;

import com.sun.enterprise.deployment.util.TypeUtil;

import java.security.ProtectionDomain ;
import java.security.PrivilegedAction ;
import java.security.AccessController ;
import java.security.Permission ;
import java.lang.reflect.ReflectPermission ;

public class AsmSerializableBeanGenerator
    implements Opcodes {

    private static final int INTF_FLAGS = ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;

    private byte[] classData = null;

    private Class loadedClass = null;
            ;
    private ClassLoader loader;


    private Class baseClass;

    private String subclassName;


    private static final boolean _debug = Boolean.valueOf(System.getProperty("emit.ejb.optional.interface"));

    public AsmSerializableBeanGenerator(ClassLoader loader, Class baseClass, String serializableSubclassName) {
        this.loader = loader;
        this.baseClass = baseClass;
        subclassName = serializableSubclassName;
    }

    public String getSerializableSubclassName() {

        return subclassName;

    }
    
    public Class generateSerializableSubclass()
        throws Exception {

        try {
            loadedClass = loader.loadClass(subclassName);
            return loadedClass;
        } catch(ClassNotFoundException e) {
            // Not loaded yet.  Just continue
        }

        ClassWriter cw = new ClassWriter(INTF_FLAGS);

        //ClassVisitor tv = //(_debug)
                //new TraceClassVisitor(cw, new PrintWriter(System.out));
        ClassVisitor tv = cw;
        String subclassInternalName = subclassName.replace('.', '/');

        String[] interfaces = new String[] {
             Type.getType(Serializable.class).getInternalName()
        };

        tv.visit(V1_1, ACC_PUBLIC,
                subclassInternalName, null,
                Type.getType(baseClass).getInternalName(), interfaces);



        Method m = Method.getMethod("void <init> ()");
        GeneratorAdapter mg = new GeneratorAdapter(ACC_PUBLIC, m, null, null, tv);
        mg.loadThis();
        mg.invokeConstructor(Type.getType(baseClass), m);
        mg.returnValue();
        mg.endMethod();



        MethodVisitor cv = cw.visitMethod(ACC_PRIVATE, "writeObject", "(Ljava/io/ObjectOutputStream;)V", null, new String[] { "java/io/IOException" });
        cv.visitVarInsn(ALOAD, 0);
        cv.visitVarInsn(ALOAD, 1);
        cv.visitMethodInsn(INVOKESTATIC, "com/sun/ejb/EJBUtils", "serializeObjectFields", "(Ljava/lang/Object;Ljava/io/ObjectOutputStream;)V");
        cv.visitInsn(RETURN);
        cv.visitMaxs(2, 2);


        cv = cw.visitMethod(ACC_PRIVATE, "readObject", "(Ljava/io/ObjectInputStream;)V", null, new String[] { "java/io/IOException", "java/lang/ClassNotFoundException" });
        cv.visitVarInsn(ALOAD, 0);
        cv.visitVarInsn(ALOAD, 1);
        cv.visitMethodInsn(INVOKESTATIC, "com/sun/ejb/EJBUtils", "deserializeObjectFields", "(Ljava/lang/Object;Ljava/io/ObjectInputStream;)V");
        cv.visitInsn(RETURN);
        cv.visitMaxs(2, 2);

        
        /**
        Type[] eTypes = new Type[] { Type.getType(java.io.IOException.class)};

        Method writeObjMethod = Method.getMethod("void writeObject (java.io.ObjectOutputStream)");
        GeneratorAdapter writeObjMethodAdapter =
                new GeneratorAdapter(ACC_PRIVATE, writeObjMethod, null, eTypes, tv);

        Type ejbUtilsType = Type.getType(com.sun.ejb.EJBUtils.class);
        Method ejbUtilsWrite = Method.getMethod
                ("void serializeObjectFields (java.lang.Class, java.lang.Object, java.lang.Object)");


        writeObjMethodAdapter.push(Type.getType(ejbClass));
        writeObjMethodAdapter.loadThis();
        writeObjMethodAdapter.loadArg(0);

        writeObjMethodAdapter.invokeStatic( ejbUtilsType, ejbUtilsWrite);

        writeObjMethodAdapter.endMethod();


        //
        eTypes = new Type[] { Type.getType(java.io.IOException.class),
                              Type.getType(java.lang.ClassNotFoundException.class)};

        Method readObjMethod = Method.getMethod("void readObject (java.io.ObjectInputStream)");
        GeneratorAdapter readObjMethodAdapter =
                new GeneratorAdapter(ACC_PRIVATE, readObjMethod, null, eTypes, tv);


        Method ejbUtilsRead = Method.getMethod
                ("void deserializeObjectFields (java.lang.Class, java.lang.Object, java.lang.Object)");


        readObjMethodAdapter.push(Type.getType(ejbClass));
        readObjMethodAdapter.loadThis();
        readObjMethodAdapter.loadArg(0);

        readObjMethodAdapter.invokeStatic( ejbUtilsType, ejbUtilsRead);

        readObjMethodAdapter.endMethod();

        **/



        tv.visitEnd();

        classData = cw.toByteArray();

        loadedClass = (Class) java.security.AccessController.doPrivileged(
                        new java.security.PrivilegedAction() {
                            public java.lang.Object run() {
                                return makeClass(subclassName, classData, baseClass.getProtectionDomain(), loader);
                            }
                        }
                );

        return loadedClass;


    }

      // Name that Java uses for constructor methods
    private static final String CONSTRUCTOR_METHOD_NAME = "<init>" ;

    // Name that Java uses for a classes static initializer method
    private static final String STATIC_INITIALIZER_METHOD_NAME = "<clinit>" ;
    
     // A Method for the protected ClassLoader.defineClass method, which we access
    // using reflection.  This requires the supressAccessChecks permission.
    private static final java.lang.reflect.Method defineClassMethod = AccessController.doPrivileged(
	new PrivilegedAction<java.lang.reflect.Method>() {
	    public java.lang.reflect.Method run() {
		try {
		    java.lang.reflect.Method meth = ClassLoader.class.getDeclaredMethod(
			"defineClass", String.class,
			byte[].class, int.class, int.class,
			ProtectionDomain.class ) ;
		    meth.setAccessible( true ) ;
		    return meth ;
		} catch (Exception exc) {
		    throw new RuntimeException(
			"Could not find defineClass method!", exc ) ;
		}
	    }
	}
    ) ;

    private static final Permission accessControlPermission =
	    new ReflectPermission( "suppressAccessChecks" ) ;

    // This requires a permission check
    private Class<?> makeClass( String name, byte[] def, ProtectionDomain pd,
	    ClassLoader loader ) {

	SecurityManager sman = System.getSecurityManager() ;
	if (sman != null)
	    sman.checkPermission( accessControlPermission ) ;

	try {
	    return (Class)defineClassMethod.invoke( loader,
		name, def, 0, def.length, pd ) ;
	} catch (Exception exc) {
	    throw new RuntimeException( "Could not invoke defineClass!",
		exc ) ;
	}
    }


}