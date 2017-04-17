/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
 *
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.catalina.authenticator;


public class Constants {

    public static final String Package = "org.apache.catalina.authenticator";

    // Authentication methods for login configuration
    public static final String BASIC_METHOD = "BASIC";
    public static final String CERT_METHOD = "CLIENT_CERT";
    public static final String DIGEST_METHOD = "DIGEST";
    public static final String FORM_METHOD = "FORM";

    // User data constraints for transport guarantee
    public static final String NONE_TRANSPORT = "NONE";
    public static final String INTEGRAL_TRANSPORT = "INTEGRAL";
    public static final String CONFIDENTIAL_TRANSPORT = "CONFIDENTIAL";

    // Form based authentication constants
    public static final String FORM_ACTION = "/j_security_check";
    public static final String FORM_PASSWORD = "j_password";
    public static final String FORM_USERNAME = "j_username";

    // Cookie name for single sign on support
    public static final String SINGLE_SIGN_ON_COOKIE = "JSESSIONIDSSO";
    public static final String SINGLE_SIGN_ON_VERSION_COOKIE = "JSESSIONIDSSOVERSION";


    // --------------------------------------------------------- Request Notes


    /**
     * <p>If a user has been authenticated by the web layer, by means of a
     * login method other than CLIENT_CERT, the username and password
     * used to authenticate the user will be attached to the request as
     * Notes for use by other server components.  A server component can
     * also call several existing methods on Request to determine whether
     * or not any user has been authenticated:</p>
     * <ul>
     * <li><strong>((HttpServletRequest) getRequest()).getAuthType()</strong>
     *     will return BASIC, CLIENT_CERT, DIGEST, FORM, or <code>null</code>
     *     if there is no authenticated user.</li>
     * <li><strong>((HttpServletRequest) getRequest()).getUserPrincipal()</strong>
     *     will return the authenticated <code>Principal</code> returned by the
     *     <code>Realm</code> that authenticated this user.</li>
     * </ul>
     * <p>If CLIENT_CERT authentication was performed, the certificate chain
     * will be available as a request attribute, as defined in the
     * servlet specification.</p>
     */


    /**
     * The notes key for the password used to authenticate this user.
     */
    public static final String REQ_PASSWORD_NOTE =
      "org.apache.catalina.request.PASSWORD";


    /**
     * The notes key for the username used to authenticate this user.
     */
    public static final String REQ_USERNAME_NOTE =
      "org.apache.catalina.request.USERNAME";


    /**
     * The notes key to track the single-sign-on identity with which this
     * request is associated.
     */
    public static final String REQ_SSOID_NOTE =
      "org.apache.catalina.request.SSOID";

    /**
     * The notes key to track the single-sign-on version with which this
     * request is associated.
     */
    public static final String REQ_SSO_VERSION_NOTE =
      "org.apache.catalina.request.SSOVersion";

    // ---------------------------------------------------------- Session Notes


    /**
     * If the <code>cache</code> property of our authenticator is set, and
     * the current request is part of a session, authentication information
     * will be cached to avoid the need for repeated calls to
     * <code>Realm.authenticate()</code>, under the following keys:
     */


    /**
     * The notes key for the password used to authenticate this user.
     */
    public static final String SESS_PASSWORD_NOTE =
      "org.apache.catalina.session.PASSWORD";


    /**
     * The notes key for the username used to authenticate this user.
     */
    public static final String SESS_USERNAME_NOTE =
      "org.apache.catalina.session.USERNAME";


    /**
     * The following note keys are used during form login processing to
     * cache required information prior to the completion of authentication.
     */


    /**
     * The previously authenticated principal (if caching is disabled).
     */
    public static final String FORM_PRINCIPAL_NOTE =
        "org.apache.catalina.authenticator.PRINCIPAL";


    /**
     * The original request information, to which the user will be
     * redirected if authentication succeeds.
     */
    public static final String FORM_REQUEST_NOTE =
        "org.apache.catalina.authenticator.REQUEST";


}
