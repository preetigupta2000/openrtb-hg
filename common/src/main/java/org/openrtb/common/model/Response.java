package org.openrtb.common.model;

/**
 * Created by IntelliJ IDEA. Response
 *
 * @author jdrahos
 */
public interface Response extends Message {
	void setStatus(Status status);

	void setIdentification(Identification identification);
}
