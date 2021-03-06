/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html or
 * glassfish/bootstrap/legal/CDDLv1.0.txt.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at glassfish/bootstrap/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 */
package com.sun.enterprise.security.auth.digest.api;

import java.security.spec.AlgorithmParameterSpec;

/**
 * Interface to Digest algorithm parameters 
 * @author K.Venugopal@sun.com
 */
public interface DigestAlgorithmParameter extends AlgorithmParameterSpec {
    /**
     * @returns the delimiter to be used while performing digest calculation, null otherwise.
     *
     */
     public byte[] getDelimiter(); 
     /**
      * 
      * @returns the parameter value. 
      */
     public byte[] getValue() ;
     /**
      * @returns the digest algorithm to be used.eg: MD5,MD5-sess etc..
      *  
      */
     public String getAlgorithm();    
     /**
      * @returns the name of the parameter, null if no name is present.
      */
     public String getName();
}
