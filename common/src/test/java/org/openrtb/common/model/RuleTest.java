/*
 * Copyright (c) 2010, The OpenRTB Project All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the OpenRTB nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
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
package org.openrtb.common.model;

import org.junit.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA. RuleTest
 *
 * @author jdrahos
 */
public class RuleTest {
	private static final Operator OPERATOR = Operator.include;
	private static final OperandType TYPE = OperandType.URL;
	private static Collection<Object> VALUES = new LinkedList<Object>();

	static {
		VALUES.add("TEST");
		VALUES.add(1);
	}

	private Rule test = new Rule();

	@Test
	public void create() {
		Rule rule = new Rule(OPERATOR, TYPE, VALUES);
		assertNotNull("Operator is required", rule.getOperator());
		assertNotNull("Type is required", rule.getType());
		assertNotNull("Values is required", rule.getValues());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_OperatorIsNull() {
		Operator operator = null;
		new Rule(operator, TYPE, VALUES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_TypeIsNull() {
		OperandType type = null;
		new Rule(OPERATOR, type, VALUES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_ValuesIsNull() {
		Collection<Object> values = null;
		new Rule(OPERATOR, TYPE, values);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalid_ValuesOtherThanStringOrInteger() {
		Collection<Object> values = new LinkedList<Object>();
		values.add(1L);
		new Rule(OPERATOR, TYPE, values);
	}

	@Test
	public void setOperator() {
		test.setOperator(OPERATOR);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setOperator_Null() {
		Operator operator = null;
		test.setOperator(operator);
	}

	@Test
	public void setType() {
		test.setType(TYPE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setType_Null() {
		OperandType type = null;
		test.setType(type);
	}

	@Test
	public void setValues() {
		test.setValues(VALUES);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setValues_Null() {
		Collection<Object> values = null;
		test.setValues(values);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setValues_SizeIs0() {
		Collection<Object> values = new LinkedList<Object>();
		test.setValues(values);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setValues_OtherThanStringOrInteger() {
		Collection<Object> values = new LinkedList<Object>();
		values.add(1L);
		test.setValues(values);
	}
}
