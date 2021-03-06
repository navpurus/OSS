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
 * AppClientClosureCompiler.java
 *
 * Created on August 24, 2004, 2:24 PM
 */

package com.sun.enterprise.tools.verifier.apiscan.stdapis;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import com.sun.enterprise.tools.verifier.apiscan.classfile.ClassFileLoader;
import com.sun.enterprise.tools.verifier.apiscan.classfile.ClosureCompilerImpl;

/**
 * @author Sanjeeb.Sahoo@Sun.COM
 */
public class AppClientClosureCompiler extends ClosureCompilerImpl {

    private static Logger logger = Logger.getLogger("apiscan.stdapis"); // NOI18N
    private static final String myClassName = "AppClientClosureCompiler"; // NOI18N
    private String specVersion;

    /**
     * Creates a new instance of AppClientClosureCompiler
     */
    public AppClientClosureCompiler(String specVersion, ClassFileLoader cfl) {
        super(cfl);
        logger.entering(myClassName, "init<>", specVersion); // NOI18N
        this.specVersion = specVersion;
        addStandardAPIs();
    }

    //this method adds APIs specific to versions.
    protected void addStandardAPIs() {
        String apiName = "appclient_" + specVersion; // NOI18N
        Collection classes = APIRepository.Instance().getClassesFor(apiName);
        for (Iterator i = classes.iterator(); i.hasNext();) {
            addExcludedClass((String) i.next());
        }
        Collection pkgs = APIRepository.Instance().getPackagesFor(apiName);
        for (Iterator i = pkgs.iterator(); i.hasNext();) {
            addExcludedPackage((String) i.next());
        }
        Collection patterns = APIRepository.Instance().getPatternsFor(apiName);
        for (Iterator i = patterns.iterator(); i.hasNext();) {
            addExcludedPattern((String) i.next());
        }
    }

}
