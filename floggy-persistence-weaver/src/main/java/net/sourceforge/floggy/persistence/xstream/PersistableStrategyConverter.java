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

package net.sourceforge.floggy.persistence.xstream;

import net.sourceforge.floggy.persistence.impl.PersistableMetadata;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class PersistableStrategyConverter implements SingleValueConverter {

	public static final String JOINED = "joined";
	public static final String PER_CLASS = "per-class";
	public static final String SINGLE = "single";

	public boolean canConvert(Class type) {
		return true;
	}

	public Object fromString(String str) {
		if (PersistableStrategyConverter.JOINED.equals(str)) {
			return new Integer(PersistableMetadata.JOINED_STRATEGY);
		} else if (PersistableStrategyConverter.PER_CLASS.equals(str)) {
			return new Integer(PersistableMetadata.PER_CLASS_STRATEGY);
		} else if (PersistableStrategyConverter.SINGLE.equals(str)) {
			return new Integer(PersistableMetadata.SINGLE_STRATEGY);
		}
		throw new IllegalArgumentException(str);
	}

	public String toString(Object obj) {
		if (obj == null) {
			throw new IllegalArgumentException((String) null);
		} else {
			Integer persistableStrategy = (Integer) obj;
			switch (persistableStrategy.intValue()) {
			case PersistableMetadata.JOINED_STRATEGY:
				return PersistableStrategyConverter.JOINED;
			case PersistableMetadata.PER_CLASS_STRATEGY:
				return PersistableStrategyConverter.PER_CLASS;
			case PersistableMetadata.SINGLE_STRATEGY:
				return PersistableStrategyConverter.SINGLE;
			default:
				throw new IllegalArgumentException(persistableStrategy
						.toString());
			}
		}
	}
}
