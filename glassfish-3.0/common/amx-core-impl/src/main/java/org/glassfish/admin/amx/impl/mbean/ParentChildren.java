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
 * accompanied this code.  If applicable, add the following below the Licensep
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
package org.glassfish.admin.amx.impl.mbean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import org.glassfish.admin.amx.core.AMXProxy;
import org.glassfish.admin.amx.util.StringUtil;

final class ParentChildren implements Comparable<ParentChildren>
{
    final AMXProxy mParent;

    final List<ParentChildren> mChildren;

    public ParentChildren(final AMXProxy parent, final List<ParentChildren> children)
    {
        mParent = parent;
        mChildren = children;
    }
    
    public void sortChildren()
    {
        Collections.sort(mChildren);
    }
    
    public int compareTo(final ParentChildren rhs)
    {
        int cmp = mParent.type().compareTo( rhs.mParent.type() );
        if ( cmp == 0 )
        {
            cmp = mParent.nameProp().compareTo( rhs.mParent.nameProp() );
        }
        
        if ( cmp == 0 )
        {
            cmp = mChildren.size() - rhs.mChildren.size();
        }
        
        return cmp;
    }

    public AMXProxy parent()
    {
        return mParent;
    }

    public List<ParentChildren> children()
    {
        return mChildren;
    }

    public List<String> toLines(final boolean details)
    {
        sortChildren();
        final List<String> lines = new ArrayList<String>();

        lines.add(descriptionFor(mParent));
        
        for (final ParentChildren child : mChildren )
        {
            final List<String> moreLines = indentAll( child.toLines(details) );
            lines.addAll(moreLines);
        }
        return lines;
    }
    
    public List<AMXProxy> asList()
    {
        final List<AMXProxy> items = new ArrayList<AMXProxy>();
        
        items.add(mParent);
        for( final ParentChildren child : mChildren )
        {
            items.addAll( child.asList() );
        }
        return items;
    }

    public static ParentChildren hierarchy(final AMXProxy top)
    {
        // make a list of all children, grouping by type
        final List<AMXProxy> children = new ArrayList<AMXProxy>();
        final Map<String, Map<String, AMXProxy>> childrenMaps = top.childrenMaps();
        for (final Map<String, AMXProxy> childrenOfType : childrenMaps.values())
        {
            for (final AMXProxy amx : childrenOfType.values())
            {
                children.add(amx);
            }
        }

        final List<ParentChildren> pcList = new ArrayList<ParentChildren>();
        for (final AMXProxy child : children)
        {
            final ParentChildren pc = hierarchy(child);
            pcList.add(pc);
        }

        final ParentChildren result = new ParentChildren(top, pcList);
        result.sortChildren();
        return result;
    }

    public static String descriptionFor(final AMXProxy proxy)
    {
        String desc = proxy.type();
        final String name = proxy.nameProp();
        if ( name != null)
        {
            desc = desc + "=" + name;
        }
        
        return desc;
    }

    private static List<String> indentAll(final List<String> lines)
    {
        final List<String> linesIndented = new ArrayList<String>();
        final String INDENT = "   ";
        for (final String line : lines)
        {
            linesIndented.add(INDENT + line);
        }
        return linesIndented;
    }


    public static String getHierarchyString( final AMXProxy top )
    {
        final ParentChildren pc = hierarchy(top);
        
        return StringUtil.toLines( pc.toLines(true) );
    }

}




































