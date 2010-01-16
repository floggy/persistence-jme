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
 * Exports the RMS id used to identify the Persistable entity in the 
 * underlined system. This interface is optional.
 * The developer must implement it to have access to the RecordStore
 * id of the persisted object.
 * The <code>setId</code> method is used to set the id obtained 
 * from the RMS system. The setId does not have effect if called by the 
 * developer because Floggy uses its internal id value to persist the 
 * objects.
 * <br>
 * <br>
 * <code>
 * public class Order implements Persistable, IDable {<br>
 * &nbsp;private int id;<br>
 * &nbsp;public void setId(int id) {<br>
 * &nbsp;&nbsp;this.id = id;<br>
 * &nbsp;}<br>
 * &nbsp;public int getId() {<br>
 * &nbsp;&nbsp;return id;<br>
 * &nbsp;}<br>
 * }<br>
 * <br>
 * ...<br>
 * <br>
 * public void save() {<br>
 * &nbsp;Order order = ...<br>
 * &nbsp;int id = pm.save(order);<br>
 * &nbsp;//id and order.getId() have the same value<br>
 * }<br>
 * </code>
 * <br>
 * @since 1.3.0
 * 
 */
public interface IDable {
	/**
	* Setter method to the id field. It is used by the framework to export
	* the RMS id value.
	*
	* @param id RMS id.
	*/
	void setId(int id);
}
