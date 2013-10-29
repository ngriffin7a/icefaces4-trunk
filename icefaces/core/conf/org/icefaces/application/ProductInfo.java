/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.application;

public class ProductInfo {

    /**
     * The company that owns this product.
     */
    public static String COMPANY = "@company@";

    /**
     * The name of the product.
     */
    public static String PRODUCT = "@product@";

    /**
     * The 3 levels of version identification, e.g. 1.0.0.
     */
    public static String PRIMARY = "@version.primary@";
    public static String SECONDARY = "@version.secondary@";
    public static String TERTIARY = "@version.tertiary@";

    /**
     * The release type of the product (alpha, beta, production).
     */
    public static String RELEASE_TYPE = "@release.type@";

    /**
     * The build number.  Typically this would be tracked and maintained
     * by the build system (i.e. Ant).
     */
    public static String BUILD_NO = "@build.number@";

    /**
     * The revision number retrieved from the repository for this build.
     * This is substituted automatically by subversion.
     */
    public static String REVISION = "@revision@";

    /**
     * The date that this build was done.
     */
    public static String BUILD_DATE = "@build.date@";

    /**
     * A configurable integer value used to uniquely identify a build.
     */
    public static String PATCH_NUMBER = "@version.patch@";

    public static String RESOURCE_VERSION = PRIMARY + "_" + SECONDARY + "_" + TERTIARY + "_" + BUILD_DATE;
    static{
        if ((PATCH_NUMBER.length() > 0) &&
                (PATCH_NUMBER.indexOf("version.patch") < 0)) {
            RESOURCE_VERSION = RESOURCE_VERSION + "_" + PATCH_NUMBER;
        }
    }

    /**
     * Convenience method to get all the relevant product information.
     * @return
     */
    public String toString(){
        StringBuffer info = new StringBuffer();
        info.append( "\n" );
        info.append( COMPANY );
        info.append( "\n" );
        info.append( PRODUCT );
        info.append( " " );
        info.append( PRIMARY );
        info.append( "." );
        info.append( SECONDARY );
        info.append( "." );
        info.append( TERTIARY );
	    if ( (RELEASE_TYPE.length() > 0) &&
	       	(!RELEASE_TYPE.equals("x")) &&
	       	(!RELEASE_TYPE.equals("${release.type}")) ) { 
	        info.append(".");
	        info.append(RELEASE_TYPE);
	     }    
        if ((PATCH_NUMBER.length() > 0) &&
                (PATCH_NUMBER.indexOf("version.patch") < 0)) {
            info.append("\nPatch: ");
            info.append(PATCH_NUMBER);
        }
        info.append( "\n" );
        info.append( "Build number: " );
        info.append( BUILD_NO );
        info.append( "\n" );
        info.append( "Build date: " );
        info.append( BUILD_DATE );
        info.append( "\n" );
        info.append( "Revision: " );
        info.append( REVISION );
        info.append( "\n" );
        return info.toString();
    }

	public static void main(String[] args) {
        ProductInfo app = new ProductInfo();
        System.out.println( app.toString() );
    }

}
