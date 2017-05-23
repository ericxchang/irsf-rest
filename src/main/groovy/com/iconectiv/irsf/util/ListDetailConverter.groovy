package com.iconectiv.irsf.util

import com.iconectiv.irsf.portal.core.ListDetailQueryPosition;
import com.iconectiv.irsf.portal.model.customer.ListDetails
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by echang on 1/13/2017.
 */

import org.springframework.core.convert.converter.Converter;


class ListDetailConvert implements Converter<Object[], ListDetails> {
	private static Logger log = LoggerFactory.getLogger(ListDetailConvert.class)

	@Override
	public ListDetails convert (Object[] row) {
		ListDetails listDetails = new ListDetails();
		listDetails.id = row[0]
		listDetails.listRefId = row[1]
		listDetails.upLoadRefId = row[2]
		listDetails.dialPattern = row[3]
		listDetails.reason = row[4]
		listDetails.notes = row[5]
		listDetails.customerDate = row[6]
		listDetails.active = row[7]
		listDetails.matchCCNDC = row[8]
		listDetails.lastUpdated = row[9]
		listDetails.lastUpdatedBy = row[10]
		listDetails.termCountry = row[11]
		listDetails.ccNdc = row[12]
		listDetails.iso2 = row[13]
		listDetails.code = row[14]
		listDetails.tos = row[15]
		listDetails.tosdesc = row[16]
		listDetails.ndc = row[17]
		listDetails.provider = row[18]
		listDetails.billingId = row[19]
		listDetails.supplement = row[20]

		return listDetails
	}
}