/**
 * Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
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
 * An interface that helps the developer to delete in cascade Persistable
 * fields. This interface is optional.
 * 
 * The developer can implement this class to clean up the RecordStore system 
 * from orphan entities. The <code>delete</code> method must contain all 
 * clean up code necessary to delete Persistable fields.
 * <br>
 * <br>
 * <code>
 * public class Order implements Persistable, Deletable {<br>
 * &nbsp;&nbsp;protected Shipment shipment;<br>
 * &nbsp;public void delete() throws FloggyException {<br>
 * &nbsp;&nbsp;if (shipment != null) {<br>
 * &nbsp;&nbsp;&nbsp;PersistableManager.getInstance().delete(shipment);<br>
 * &nbsp;&nbsp;}
 * &nbsp;}<br>
 * }
 * </code>
 * <br>
 * @since 1.3.0
 * 
 */
public interface Deletable {
	/**
	* It is used to clean up references that the implementation class may
	* have to another entities.
	*
	* @throws FloggyException Exception thrown if an error occurs while deleting
	* 				the object.
	*/
	void delete() throws FloggyException;
}
