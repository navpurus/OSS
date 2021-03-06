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
package org.glassfish.ejb.deployment.annotation.handlers;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;

import com.sun.enterprise.deployment.EjbDescriptor;
import com.sun.enterprise.deployment.EjbInterceptor;
import com.sun.enterprise.deployment.LifecycleCallbackDescriptor;

import org.glassfish.apf.AnnotationInfo;
import org.glassfish.apf.AnnotationProcessorException;
import org.glassfish.apf.HandlerProcessingResult;
import com.sun.enterprise.deployment.annotation.context.EjbContext;
import com.sun.enterprise.deployment.annotation.context.EjbInterceptorContext;
import org.glassfish.ejb.deployment.annotation.handlers.AbstractAttributeHandler;
import org.jvnet.hk2.annotations.Service;

/**
 * This handler is responsible for handling the javax.ejb.AroundInvoke attribute
 *
 */
@Service
public class AroundInvokeHandler extends AbstractAttributeHandler {
    
    public AroundInvokeHandler() {
    }
    
    /**
     * @return the annoation type this annotation handler is handling
     */
    public Class<? extends Annotation> getAnnotationType() {
        return AroundInvoke.class;
    }    
        
    protected HandlerProcessingResult processAnnotation(AnnotationInfo ainfo,
            EjbContext[] ejbContexts) throws AnnotationProcessorException {

        for(EjbContext next : ejbContexts) {
            
            EjbDescriptor ejbDescriptor = 
                (EjbDescriptor) next.getDescriptor();

            ejbDescriptor.addAroundInvokeDescriptor(
                getAroundInvocationDescriptor(ainfo));
        }

        return getDefaultProcessedResult();
    }

    protected HandlerProcessingResult processAnnotation(AnnotationInfo ainfo,
            EjbInterceptorContext ejbInterceptorContext)
            throws AnnotationProcessorException {

        EjbInterceptor ejbInterceptor =  ejbInterceptorContext.getDescriptor();

        ejbInterceptor.addAroundInvokeDescriptor(
            getAroundInvocationDescriptor(ainfo));
            
        return getDefaultProcessedResult();
    }

    protected LifecycleCallbackDescriptor getAroundInvocationDescriptor(
            AnnotationInfo ainfo) {
        
        Method m = (Method) ainfo.getAnnotatedElement();
        LifecycleCallbackDescriptor lccDesc =
                new LifecycleCallbackDescriptor();
        lccDesc.setLifecycleCallbackClass(m.getDeclaringClass().getName());
        lccDesc.setLifecycleCallbackMethod(m.getName());
        return lccDesc;
    }

    /**
     * @return an array of annotation types this annotation handler would 
     * require to be processed (if present) before it processes it's own 
     * annotation type.
     */
    public Class<? extends Annotation>[] getTypeDependencies() {
        return getEjbAnnotationTypes();
    }

    protected boolean isDelegatee() {
        return true;
    }
}
