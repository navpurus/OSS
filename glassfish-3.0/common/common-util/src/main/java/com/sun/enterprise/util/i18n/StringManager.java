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
 * @(#) StringManager.java
 *
 * Copyright 2000-2001 by iPlanet/Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of iPlanet/Sun Microsystems, Inc. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license
 * agreement you entered into with iPlanet/Sun Microsystems.
 */
package com.sun.enterprise.util.i18n;

import java.util.Hashtable;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.logging.LogDomains;

/**
 * Implementation of a local string manager. Provides access to i18n messages
 * for classes that need them.
 *
 * <p> One StringManager per package can be created and accessed by the 
 * getManager method call. The ResourceBundle name is constructed from 
 * the given package name in the constructor plus the suffix of "LocalStrings".
 * Thie means that localized information will be contained in a 
 * LocalStrings.properties file located in the package directory of the 
 * classpath.
 *
 * <xmp>
 * Example:
 *
 * [LocalStrings.properties]
 * test=At {1,time} on {1,date}, there was {2} on planet {0,number,integer}
 *
 *
 *  StringManager sm  = StringManager.getManager(this);
 *  .....
 *
 *
 *  try {
 *      ....
 *  } catch (Exception e) {
 *      String localizedMsg = sm.getString("test", 
 *          new Integer(7), new java.util.Date(System.currentTimeMillis()), 
 *          "a disturbance in the Force");
 *
 *      throw new MyException(localizedMsg, e);
 *  }
 *
 * Localized message:
 *   At 2:27:41 PM on Jul 8, 2002, there was a disturbance in the Force
 *   on planet 7
 *
 * </xmp>
 *
 * @author  Nazrul Islam
 * @since   JDK1.4
 */
public class StringManager extends StringManagerBase {

    /** logger used for this class */
    private static Logger _logger=LogDomains.getLogger(StringManager.class, LogDomains.UTIL_LOGGER);

    /** name of the resource bundle property file name */
    private static final String RES_BUNDLE_NM = ".LocalStrings";
    

    /** cache for all the local string managers (per pkg) */
    private static Hashtable managers = new Hashtable();

    /**
     * Initializes the resource bundle.
     *
     * @param    packageName    name of the package
     */
    private StringManager(String packageName, ClassLoader classLoader) {
        super(packageName + RES_BUNDLE_NM, classLoader);        
    }

    /**
     * Returns a local string manager for the given package name.
     *
     * @param    packageName    name of the package of the src
     *
     * @return   a local string manager for the given package name
     */
    public synchronized static StringManager getManager(String packageName, ClassLoader classLoader) {

        StringManager mgr = (StringManager) managers.get(packageName);

        if (mgr == null) {
            mgr = new StringManager(packageName, classLoader);
            try {
                managers.put(packageName, mgr);
            } catch (Exception e) {
                _logger.log(Level.SEVERE,"iplanet_util.error_while_caching",e);
            }
        }
        return mgr;
    }

    /**
     *
     * Returns a local string manager for the given package name.
     *
     * @param    callerClass    the object making the call
     *
     * @return   a local string manager for the given package name
     */
    public synchronized static StringManager getManager(Class callerClass) {

        try {
            Package pkg = callerClass.getPackage();
            if (pkg != null) {
                String pkgName = pkg.getName();
                return getManager(pkgName, callerClass.getClassLoader());
            } else {
                // class does not belong to any pkg
                String pkgName = callerClass.getName();
                return getManager(pkgName, callerClass.getClassLoader());
            }
        } catch (Exception e) {
            _logger.log(Level.SEVERE, "iplanet_util.error_in_getMgr", e);

            // dummy string manager
            return getManager("", callerClass.getClassLoader());
        }
    }
}