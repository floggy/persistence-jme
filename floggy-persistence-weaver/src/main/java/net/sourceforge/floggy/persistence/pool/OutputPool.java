/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
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
package net.sourceforge.floggy.persistence.pool;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

public interface OutputPool {

    public abstract void addClass(CtClass ctClass) throws NotFoundException,
	    IOException, CannotCompileException;

    public abstract void addFile(URL fileURL, String fileName)
	    throws IOException;

    public abstract void addResource(InputStream resourceStream, String fileName)
	    throws IOException;

    public abstract void finish() throws IOException;

}
