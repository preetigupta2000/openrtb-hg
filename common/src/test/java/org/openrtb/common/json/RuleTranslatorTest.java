package org.openrtb.common.json;

import org.junit.Test;
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
		VALUES.add("test1.co");
		VALUES.add(2);
	}
	private static final Rule RULE = new Rule("include","URL", VALUES);
	private static final String PRETTY_VALUE = "{" +
		"  \"operator\": \""+ RULE.getOperator() +"\"," +
		"  \"type\": \""+ RULE.getType() +"\"," +
		"  \"values\": [\""+VALUES.get(0) +"\","+VALUES.get(1) +"]"+
		"}";

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
		assertEquals("unable to deserialize the operator",
					 expected.getOperator(), actual.getOperator());
		assertEquals("unable to deserialize the type",
					 expected.getType(), actual.getType());

		if(actual.getValues() != null){
			assertNotNull(expected.getValues());
		}

		if(expected.getValues() != null){
			assertNotNull(actual.getValues());
			assertEquals("unable to deserialize values", actual.getValues().size(),expected.getValues().size());

			findExtraItems:
			for(Object actualVal: actual.getValues()){
				for(Object expectedVal: expected.getValues()){
					if(expectedVal.equals(actualVal))
						continue findExtraItems;
				}

				fail("found extra value: "+ actualVal );
			}

			findMissingItems:
			for(Object expectedVal: expected.getValues()){
				for(Object actualVal: actual.getValues()){
					if(expectedVal.equals(actualVal))
						continue findMissingItems;
				}

				fail("found missing value: "+ expectedVal );
			}
		}
	}

	private static class RuleTranslator extends AbstractJsonTranslator<Rule> {
		public RuleTranslator() {
			super(RuleTranslator.class);
		}
	}

	private static class TestClass extends Rule{}
}
