/**
 * Copyright (c) 2006-2011 Floggy Open Source Group. All rights reserved.
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
package net.sourceforge.floggy.persistence.verifier.test;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.ClassLoaderRepository;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class BCELVerifierTest extends AbstractVerifierTest {
	/**
	 * DOCUMENT ME!
	*
	* @param className DOCUMENT ME!
	* @param createInstance DOCUMENT ME!
	*/
	protected void evaluate(String className, boolean createInstance) {
		Verifier verifier = VerifierFactory.getVerifier(className);

		VerificationResult vr = verifier.doPass1();

		assertEquals(vr.getMessage(), vr, VerificationResult.VR_OK);

		vr = verifier.doPass2();
		assertEquals(vr.getMessage(), vr, VerificationResult.VR_OK);

		if (vr == VerificationResult.VR_OK) {
			JavaClass jc;

			try {
				jc = Repository.lookupClass(className);

				Method[] methods = jc.getMethods();

				for (int i = 0; i < methods.length; i++) {
					vr = verifier.doPass3a(i);
					assertEquals("Method: " + methods[i].getName() + "\n"
						+ vr.getMessage(), vr, VerificationResult.VR_OK);

					vr = verifier.doPass3b(i);
					assertEquals(vr.getMessage(), vr, VerificationResult.VR_OK);
				}
			} catch (ClassNotFoundException e) {
				fail(e.getMessage());
			}
		}

		verifier.flush();
		Repository.clearCache();
		System.gc();
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected void setUp() throws Exception {
		Repository.setRepository(new ClassLoaderRepository(
				this.getClass().getClassLoader()));
	}
}
