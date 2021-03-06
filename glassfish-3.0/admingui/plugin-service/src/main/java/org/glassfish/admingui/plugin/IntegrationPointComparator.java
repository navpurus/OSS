/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
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
package org.glassfish.admingui.plugin;

import java.io.Serializable;
import java.util.Comparator;

/**
 *  <p>	This class compares two {@link IntegrationPoint} Objects.  See
 *	{@link #compare} for more details.</p>
 */
public class IntegrationPointComparator implements Comparator<IntegrationPoint>, Serializable {
    /**
     *	<p> Protected constructor.  Use {@link #getInstance()} instead.</p>
     */
    protected IntegrationPointComparator() {
    }

    /**
     *	<p> Accessor for this <code>Comparator</code>.</p>
     */
    public static IntegrationPointComparator getInstance() {
	return _instance;
    }

    /**
     *	<p> This method compares two {@link IntegrationPoint}s.  It will first
     *	    check the <code>parentId</code>, then the <code>priority</code> if
     *	    the <code>parentId</code>s are equal.  If the priorities happen to
     *	    be equal as well, it will compare the <code>id</code>s.</p>
     */
    public int compare(IntegrationPoint ip1, IntegrationPoint ip2) {
	// First check parentIds
	String left = "" + ip1.getParentId();
	int result = left.compareTo("" + ip2.getParentId());
	if (result == 0) {
	    // parentIds are the same, check the priorities
	    result = ip1.getPriority() - ip2.getPriority();
	    if (result == 0) {
		// priorities are the same, check the ids
		left = "" + ip1.getId();
		result = left.compareTo("" + ip2.getId());
		if (result == 0) {
		    // Equal
		    return 0;
		}
	    }
	}

	// Return the answer
	return (result < 0) ? -1 : 1;
    }

    private static IntegrationPointComparator _instance =
	    new IntegrationPointComparator();
}
