package org.openrtb.common.model;

import org.openrtb.common.json.AbstractJsonTranslator;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA. Message
 *
 * @author jdrahos
 */
public interface Message {
	Identification getIdentification();

	void sign(byte[] sharedSecret, AbstractJsonTranslator translator) throws IOException;

	boolean verify(byte[] sharedSecret, AbstractJsonTranslator translator) throws IOException;
}
