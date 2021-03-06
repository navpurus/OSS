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

package org.glassfish.jdbc.admin.cli;

import com.sun.enterprise.config.serverbeans.JdbcResource;
import com.sun.enterprise.config.serverbeans.Resource;
import com.sun.enterprise.config.serverbeans.Resources;
import com.sun.enterprise.v3.common.PropsFileActionReporter;
import com.sun.logging.LogDomains;

import java.util.List;

import org.glassfish.api.admin.AdminCommandContext;
import org.glassfish.api.admin.CommandRunner;
import org.glassfish.api.admin.ParameterMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jvnet.hk2.component.Habitat;
import org.glassfish.api.ActionReport;
import org.glassfish.api.ActionReport.MessagePart;
import org.glassfish.tests.utils.Utils;
import org.glassfish.tests.utils.ConfigApiTest;
import org.jvnet.hk2.config.DomDocument;
import org.jvnet.hk2.config.TransactionFailure;

/**
 *
 * @author Jennifer
 */
//@Ignore // temporarily disabled
public class ListJdbcResourcesTest extends ConfigApiTest {
    private Habitat habitat = Utils.instance.getHabitat(this);
    private Resources resources = habitat.getComponent(Resources.class);
    private int origNum = 0;
    private ParameterMap parameters = new ParameterMap();
    CreateJdbcResource createCommand = null;
    DeleteJdbcResource deleteCommand = null;
    ListJdbcResources listCommand = null;
    AdminCommandContext context = null;
    CommandRunner cr = habitat.getComponent(CommandRunner.class);
    
    public DomDocument getDocument(Habitat habitat) {

        return new TestDocument(habitat);
    }    

    /**
     * Returns the file name without the .xml extension to load the test configuration
     * from. By default, it's the name of the TestClass.
     *
     * @return the configuration file name
     */
    public String getFileName() {
        return "DomainTest";
    }
    
    @Before
    public void setUp() {
        for (Resource resource : resources.getResources()) {
            if (resource instanceof JdbcResource) {
                origNum = origNum + 1;
            }
        }       
    }

    @After
    public void tearDown() throws TransactionFailure {
        parameters = new ParameterMap();
    }
    
    /**
     * Test of execute method, of class ListJdbcResources.
     * list-jdbc-resources
     */
    @Test
    public void testExecuteSuccessListOriginal() {
        // List the original set of JDBC Resources 
        //Get an instance of the ListJdbcResources command
        ListJdbcResources listCommand = habitat.getComponent(ListJdbcResources.class);

        AdminCommandContext context = new AdminCommandContext(
                LogDomains.getLogger(ListJdbcResourcesTest.class, LogDomains.ADMIN_LOGGER),
                new PropsFileActionReporter());
        
        //Call CommandRunnerImpl.doCommand(..) to execute the command
        cr.getCommandInvocation("list-jdbc-resources", context.getActionReport()).parameters(parameters).execute(listCommand);
        
        List<MessagePart> list = context.getActionReport().getTopMessagePart().getChildren();
        assertEquals(origNum, list.size());
        
        // Check the exit code is SUCCESS
        assertEquals(ActionReport.ExitCode.SUCCESS, context.getActionReport().getActionExitCode());
    }
    
    /**
     * Test of execute method, of class ListJdbcResource.
     * list-jdbc-resources server
     */
    @Test
    public void testExecuteSuccessValidTargetOperand() {
        // List the original set of JDBC Resources 
        //Get an instance of the ListJdbcResources command
        listCommand = habitat.getComponent(ListJdbcResources.class);

        parameters.add("DEFAULT", "server");
        
        context = new AdminCommandContext(
                LogDomains.getLogger(ListJdbcResourcesTest.class, LogDomains.ADMIN_LOGGER),
                new PropsFileActionReporter());
        
        //Call CommandRunnerImpl.doCommand(..) to execute the command
        cr.getCommandInvocation("list-jdbc-resources", context.getActionReport()).parameters(parameters).execute(listCommand);
        
        List<MessagePart> list = context.getActionReport().getTopMessagePart().getChildren();        
        assertEquals(origNum, list.size());
        
        // Check the exit code is Success
        assertEquals(ActionReport.ExitCode.SUCCESS, context.getActionReport().getActionExitCode());
    }
    
    /**
     * Test of execute method, of class ListJdbcResource.
     * create-jdbc-resource --connectionpoolid DerbyPool bob
     * list-jdbc-resources
     */
    @Test
    public void testExecuteSuccessListBob() {
        // Create JDBC Resource bob
        assertTrue(resources!=null);
        
        //Get an instance of the CreateJdbcResource command
        createCommand = habitat.getComponent(CreateJdbcResource.class);
        assertTrue(createCommand!=null);

        parameters.add("connectionpoolid", "DerbyPool");
        parameters.add("DEFAULT", "bob");
        
        context = new AdminCommandContext(
                LogDomains.getLogger(ListJdbcResourcesTest.class, LogDomains.ADMIN_LOGGER),
                new PropsFileActionReporter());

        cr.getCommandInvocation("create-jdbc-resource", context.getActionReport()).parameters(parameters).execute(createCommand);
        
        assertEquals(ActionReport.ExitCode.SUCCESS, context.getActionReport().getActionExitCode());
        
        // List JDBC Resources and check if bob is in the list
        //Get an instance of the ListJdbcResources command
        listCommand = habitat.getComponent(ListJdbcResources.class);
        parameters = new ParameterMap();
        context = new AdminCommandContext(
                LogDomains.getLogger(ListJdbcResourcesTest.class, LogDomains.ADMIN_LOGGER),
                new PropsFileActionReporter());
        
        //Call CommandRunnerImpl.doCommand(..) to execute the command
        cr.getCommandInvocation("list-jdbc-resources", context.getActionReport()).parameters(parameters).execute(listCommand);
                
        List<MessagePart> list = context.getActionReport().getTopMessagePart().getChildren();
        
        assertEquals(origNum + 1, list.size());
        
        List<String> listStr = new java.util.ArrayList();
        for (MessagePart mp : list) {
            listStr.add(mp.getMessage());
        }  
        assertTrue(listStr.contains("bob"));
        
        // Check the exit code is SUCCESS
        assertEquals(ActionReport.ExitCode.SUCCESS, context.getActionReport().getActionExitCode());
    }
    
    /**
     * Test of execute method, of class ListJdbcResource.
     * delete-jdbc-resource bob
     * list-jdbc-resources
     */
    @Test
    public void testExecuteSuccessListNoBob() {
        // Delete JDBC Resource bob
        assertTrue(resources!=null);
        
        //Get an instance of the CreateJdbcResource command
        deleteCommand = habitat.getComponent(DeleteJdbcResource.class);
        assertTrue(deleteCommand!=null);

        parameters.add("DEFAULT", "bob");
        
        context = new AdminCommandContext(
                LogDomains.getLogger(ListJdbcResourcesTest.class, LogDomains.ADMIN_LOGGER),
                new PropsFileActionReporter());

        cr.getCommandInvocation("delete-jdbc-resource", context.getActionReport()).parameters(parameters).execute(deleteCommand);

        assertEquals(ActionReport.ExitCode.SUCCESS, context.getActionReport().getActionExitCode());
        
        // List JDBC Resources and check if bob is in the list
        //Get an instance of the ListJdbcResources command
        listCommand = habitat.getComponent(ListJdbcResources.class);
        parameters = new ParameterMap();        
        context = new AdminCommandContext(
                LogDomains.getLogger(ListJdbcResourcesTest.class, LogDomains.ADMIN_LOGGER),
                new PropsFileActionReporter());
        
        //Call CommandRunnerImpl.doCommand(..) to execute the command
        cr.getCommandInvocation("list-jdbc-resources", context.getActionReport()).parameters(parameters).execute(listCommand);

        List<MessagePart> list = context.getActionReport().getTopMessagePart().getChildren();
        
        assertEquals(origNum - 1, list.size());
        
        List<String> listStr = new java.util.ArrayList();
        for (MessagePart mp : list) {
            listStr.add(mp.getMessage());
        }  
        assertFalse(listStr.contains("bob"));
        
        // Check the exit code is SUCCESS
        assertEquals(ActionReport.ExitCode.SUCCESS, context.getActionReport().getActionExitCode());
    }
    
   /**
     * Test of execute method, of class ListJdbcResource.
     * list-jdbc-resources invalid
     */
    @Ignore
    @Test
    public void testExecuteFailInvalidTargetOperand() {
        // List the original set of JDBC Resources 
        //Get an instance of the ListJdbcResources command
        listCommand = habitat.getComponent(ListJdbcResources.class);

        parameters.add("DEFAULT", "invalid");
        
        context = new AdminCommandContext(
                LogDomains.getLogger(ListJdbcResourcesTest.class, LogDomains.ADMIN_LOGGER),
                new PropsFileActionReporter());
        
        //Call CommandRunnerImpl.doCommand(..) to execute the command
       cr.getCommandInvocation("list-jdbc-resources", context.getActionReport()).parameters(parameters).execute(listCommand);
        
        // Need bug fix before uncommenting assertion
        //List<MessagePart> list = context.getActionReport().getTopMessagePart().getChildren();
        //assertEquals(0, list.size());
        
        // Check the exit code is FAILURE
        // Need bug fix before uncommenting assertion
        //assertEquals(ActionReport.ExitCode.FAILURE, context.getActionReport().getActionExitCode());
        // Check error msg 'Invalid target: invalid'
    }

    /**
     * Test of execute method, of class ListJdbcResource.
     * list-jdbc-resources --invalid invalid
     */
    @Ignore
    @Test
    public void testExecuteFailInvalidOption() {
        listCommand = habitat.getComponent(ListJdbcResources.class);
        parameters.add("invalid", "invalid");
        context = new AdminCommandContext(
                LogDomains.getLogger(ListJdbcResourcesTest.class, LogDomains.ADMIN_LOGGER),
                new PropsFileActionReporter());

        cr.getCommandInvocation("list-jdbc-resources", context.getActionReport()).parameters(parameters).execute(listCommand);

        List<MessagePart> list = context.getActionReport().getTopMessagePart().getChildren();
        assertEquals(1, list.size());
        
        for (MessagePart mp : list) {
            assertEquals("Usage: list-jdbc-resources ", mp.getMessage());
        }  
        // Check the exit code is FAILURE
        assertEquals(ActionReport.ExitCode.FAILURE, context.getActionReport().getActionExitCode());
    }
    
}
