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

/*
 * @(#)Util.java - ver 1.1 - 01/04/2006
 *
 * Copyright 2004-2006 Sun Microsystems, Inc. All Rights Reserved.
 */

package com.sun.enterprise.jbi.serviceengine.util;

import com.sun.enterprise.jbi.serviceengine.core.JavaEEServiceEngineContext;

import javax.jbi.component.ComponentContext;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

/**
 * This utility class provides method to build management message.
 *
 * @author bhavanishankar@dev.java.net.
 * @see com.sun.enterprise.jbi.serviceengine.core.JavaEEServiceEngineSUManager
 */
public final class Util {
    /**
     * Creates a new instance of Util. The constructor has been declared private to
     * prevent instantiation.
     */
    private Util() {
    }

    /**
     * Helper method to build a management message for soap binding component.
     *
     * @param messageType component message holder type
     * @param taskName    task name
     * @param taskResult  task result indicating success or failure
     * @param locToken    token indicating the location of error.
     * @param locParam    parameters for error message.
     * @param locMessage  error message.
     * @param expObj      exceptino object
     * @return management message for the component.
     */
    public static String buildManagementMessage(String messageType,
                                                String taskName,
                                                String taskResult,
                                                String locToken,
                                                String locParam,
                                                String locMessage,
                                                Throwable expObj) {
        String componentName = JavaEEServiceEngineContext.getInstance()
                .getJBIContext()
                .getComponentName();
        ComponentMessageHolder messageHolder =
                new ComponentMessageHolder(messageType);
        messageHolder.setComponentName(componentName);
        messageHolder.setTaskName(taskName);
        messageHolder.setTaskResult(taskResult);

        if (locToken != null) {
            messageHolder.setLocToken(1, locToken);
        }

        if (locParam != null) {
            String[] locParams = new String[1];
            locParams[0] = locParam;
            messageHolder.setLocParam(1, locParams);
        }

        if (locMessage != null) {
            messageHolder.setLocMessage(1, locMessage);
        }

        if (expObj != null) {
            messageHolder.setExceptionObject(expObj);
        }

        String retMsg = null;

        try {
            ManagementMessageBuilder managementMessageBuilder =
                    new ManagementMessageBuilder();
            retMsg =
                    managementMessageBuilder.buildComponentMessage(messageHolder);
        }
        catch (Exception exp) {
            retMsg = null;
        }

        return retMsg;
    }
    
    public static XMLInputFactory getXMLInputFactory() {
        XMLInputFactory XIF = XMLInputFactory.newInstance();
        /*
        try {
            XIF.setProperty("reuse-instance", Boolean.FALSE);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        */
        return XIF;
    }
    
    public static XMLOutputFactory getXMLOutputFactory() {
        XMLOutputFactory XOF = XMLOutputFactory.newInstance();
        /*
        try {
            XOF.setProperty("reuse-instance", Boolean.FALSE);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        */
        return XOF;
    }
    
}
