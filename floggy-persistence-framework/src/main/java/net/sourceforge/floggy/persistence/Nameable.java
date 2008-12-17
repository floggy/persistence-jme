/**
 * Copyright (c) 2006-2008 Floggy Open Source Group. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.floggy.persistence;

/**
 * An interface that helps the developer to define the name of the RecordStore 
 * for each Persistable class. This interface is optional, the weaver will 
 * implement it if the developer don't.
 * 
 * The value returned by the <code>getRecordStoreName</code> method must be 
 * the same to all instances created otherwise each instance will be saved 
 * in a different RecordStore.
 * <br>
 * <br>
 * <code>
 * public class Order implements Persistable, Nameable {<br>
 * &nbsp;&nbsp;...<br>
 * &nbsp;public String getRecordStoreName() {<br>
 * &nbsp;&nbsp;return "Order";<br>
 * &nbsp;}<br>
 * }
 * </code>
 * <br>
 * @since 1.2.0
 * 
 */
public interface Nameable {
    /**
     * It is used to retrieve the name of the RecordStore where the
     * class will be stored.
     *
     * @return the name of the RecordStore.
     */
    String getRecordStoreName();
}
