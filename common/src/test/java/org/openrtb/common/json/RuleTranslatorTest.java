/*
 * Copyright (c) 2010, The OpenRTB Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *   3. Neither the name of the OpenRTB nor the names of its contributors
 *      may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.openrtb.common.json;

import org.junit.Test;
import org.openrtb.common.model.Operator;
import org.openrtb.common.model.PreferenceType;
import org.openrtb.common.model.Rule;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA. RuleTranslatorTest
 *
 * @author jdrahos
 */
public class RuleTranslatorTest {
	private static final List<Object> VALUES = new LinkedList<Object>();

	static {
		VALUES.add(2);
		VALUES.add("test1.co");
	}

	private static final Rule RULE = new Rule(Operator.include, PreferenceType.URL, VALUES);
	private static final String PRETTY_VALUE = "{" + "  \"operator\": \"" + RULE.getOperator() + "\"," + "  \"type\": \"" + RULE.getType() + "\"," + "  \"values\": [" + VALUES.get(0) + ",\"" + VALUES.get(1) + "\"]" + "}";

	private static final String EXPECTED_VALUE = PRETTY_VALUE.replaceAll("[ \n]", "");
	private RuleTranslator test = new RuleTranslator();

	@Test
	public void serializeObject() throws IOException {
		assertEquals(EXPECTED_VALUE, test.toJSON(RULE));
	}

	@Test
	public void deserializeObject() throws IOException {
		validateObject(RULE, test.fromJSON(PRETTY_VALUE));
	}

	@Test
	public void serializeEmptyObject() throws IOException {
		assertEquals("{}", test.toJSON(new TestClass()));
	}

	@Test
	public void deserializeEmptyObject() throws IOException {
		validateObject(new TestClass(), test.fromJSON("{}"));
	}

	public static void validateObject(final Rule expected, final Rule actual) {
		assertEquals("unable to deserialize the operator", expected.getOperator(), actual.getOperator());
		assertEquals("unable to deserialize the type", expected.getType(), actual.getType());

		if (actual.getValues() != null) {
			assertNotNull(expected.getValues());
		}

		if (expected.getValues() != null) {
			assertNotNull(actual.getValues());
			assertEquals("unable to deserialize values", actual.getValues().size(), expected.getValues().size());

			findExtraItems:
			for (Object actualVal : actual.getValues()) {
				for (Object expectedVal : expected.getValues()) {
					if (expectedVal.equals(actualVal)) {
						continue findExtraItems;
					}
				}

				fail("found extra value: " + actualVal);
			}

			findMissingItems:
			for (Object expectedVal : expected.getValues()) {
				for (Object actualVal : actual.getValues()) {
					if (expectedVal.equals(actualVal)) {
						continue findMissingItems;
					}
				}

				fail("found missing value: " + expectedVal);
			}
		}
	}

	private static class RuleTranslator extends AbstractJsonTranslator<Rule> {
		public RuleTranslator() {
			super(RuleTranslator.class);
		}
	}

	private static class TestClass extends Rule {
	}
}
