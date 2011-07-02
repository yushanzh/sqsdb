/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sinovatech.sqsdb.common.pool;

/**
 * Configuration settings for handling abandoned objects.
 *                                                            
 * @author Glenn L. Nielsen           
 * @version $Revision: 1.1 $ $Date: 2009/07/07 06:02:02 $
 */
public class AbandonedConfig {

    /**
     * Whether or not a object is considered abandoned and eligible
     * for removal if it has been idle longer than the removeAbandonedTimeout
     */
    private boolean removeAbandoned = true;

    /**
     * Flag to remove abandoned objects if they exceed the
     * removeAbandonedTimeout.
     *
     * Set to true or false, default false.
     * If set to true a object is considered abandoned and eligible
     * for removal if it has been idle longer than the removeAbandonedTimeout.
     * Setting this to true can recover objects from poorly written    
     * applications which fail to close a object.
     *
     * @return true if abandoned objects are to be removed
     */
    public boolean getRemoveAbandoned() {
        return (this.removeAbandoned);
    }

    /**
     * Flag to remove abandoned objects if they exceed the
     * removeAbandonedTimeout.
     *
     * Set to true or false, default false.
     * If set to true a object is considered abandoned and eligible   
     * for removal if it has been idle longer than the removeAbandonedTimeout.
     * Setting this to true can recover objects from poorly written
     * applications which fail to close a object.
     *
     * @param removeAbandoned true means abandoned objects will be
     *   removed
     */
    public void setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    /**
     * Timeout in seconds before an abandoned object can be removed
     */
    private int removeAbandonedTimeout = 10 * 60;

    /**
     * Timeout in seconds before an abandoned object can be removed.
     *
     * Defaults to 300 seconds.
     *
     * @return abandoned timeout in seconds
     */
    public int getRemoveAbandonedTimeout() {
        return (this.removeAbandonedTimeout);
    }

    /**
     * Timeout in seconds before an abandoned object can be removed.
     *
     * Defaults to 300 seconds.
     *
     * @param removeAbandonedTimeout abandoned timeout in seconds
     */
    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    /**
     * Determines whether or not to log stack traces for application code
     * which abandoned a object.
     */
    private boolean logAbandoned = true;

    /**
     * Flag to log stack traces for application code which abandoned
     * a object.
     *
     * Defaults to false.
     * Logging of abandoned objects adds overhead
     * for every object open because a stack
     * trace has to be generated.
     * 
     * @return boolean true if stack trace logging is turned on for abandoned
     * objects
     *
     */
    public boolean getLogAbandoned() {
        return (this.logAbandoned);
    }

    /**
     * Flag to log stack traces for application code which abandoned
     * a object.
     *
     * Defaults to false.
     * Logging of abandoned objects adds overhead
     * for every object open because a stack
     * trace has to be generated.
     * @param logAbandoned true turns on abandoned stack trace logging
     *
     */
    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

}
