package com.iconectiv.irsf.portal.service

import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDetails
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository
import com.iconectiv.irsf.util.DateTimeHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
/**
 * Created by echang on 1/13/2017.
 */
@Service
class ListUploadService {
    private static Logger log = LoggerFactory.getLogger(ListUploadService.class)

    @Autowired
    private ListDetailsRepository listDetailRepo
	@Autowired
	MobileIdDataService midDataService;

	void parseBlackWhiteListData(ListUploadRequest uploadReq, List<ListDetails> listEntries, StringBuilder errorList) {
        def headerMap = parseHeader(uploadReq, uploadReq.delimiter)
		uploadReq.data.eachWithIndex {item, index ->
            if (log.isTraceEnabled()) log.trace("Parsing ${index} line: ${item}")
			
			if (item.trim()) {
				parseListLine(item, ++index, uploadReq, uploadReq.delimiter, headerMap, listEntries, errorList)				
			}
		}
		log.info("Finish parsing input file, about to insert")
	}

    def parseHeader(uploadReq, delimiter) {
        def headerMap = [:]
        if (uploadReq.data.get(0)==~ /.*(?i)dialpattern.*/) {
            if (log.isDebugEnabled()) log.debug("Parsing header line ${uploadReq.data[0]}")
            def elements = uploadReq.data.get(0).split(/\$delimiter/)
            elements.eachWithIndex {item, index ->
                headerMap[item.toLowerCase()] = index
            }
            uploadReq.data.remove(0)
        } else {
            if (log.isDebugEnabled()) log.debug("Input file does not have header, use default settings")
            headerMap['dialpattern'] = 0
            headerMap['customerdate'] = 1
            headerMap['reason'] = 2
            headerMap['notes'] = 3
        }
        return headerMap
    }
	
	void parseListLine(String item, Integer index, ListUploadRequest uploadReq, String delimiter, headerMap, listEntries, errorList) {
		String[] elements = item.split(/\$delimiter/)
        def dialCode = elements.size() > headerMap['dialpattern'] ? elements[headerMap['dialpattern']] : ''
        def customerDate = elements.size() > headerMap['customerdate'] ? elements[headerMap['customerdate']] : null
        def reason = elements.size() > headerMap['reason'] ? elements[headerMap['reason']] : null
        def notes = elements.size() > headerMap['notes'] ? elements[headerMap['notes']] : null

        ['"', ' ', '-', '(', ')'].each {
            dialCode =  dialCode.replaceAll(/\${it}/, '')
        }

        if (dialCode == '') {
            errorList.append("line $index does not have dial code\n")
            return
        }

        if ( ! (dialCode ==~ /^\d+$/) ) {
            errorList.append("line $index The Dial Pattern field contains non digit character\n")
            return
        }

        if (dialCode.size() > 15) {
            errorList.append("line $index The Dial Pattern field is over the max length of 15\n")
            return
        }

        if (reason && reason.size() > 100) {
            errorList.append("line $index The Reason field is over the max length of 100\n")
            return
        }

        if (notes && notes.size() > 100) {
            errorList.append("line $index The Notes field is over the max length of 100\n")
            return
        }

        if (hasDuplicateEntry(listEntries, dialCode)) {
            errorList.append("line $index The Dial Pattern <${dialCode}> already exists in the upload file\n")
            return
        }

        if (uploadReq.listRefId && hasDialCodeInDB(dialCode, uploadReq.listRefId)) {
            errorList.append("line $index dial code <${dialCode}> already exists in the same list\n")
            return
        }

        ListDetails listDetails = new ListDetails()
        listDetails.dialPattern = dialCode

        if (customerDate) {
            try {
                listDetails.customerDate = DateTimeHelper.formatDate(customerDate.replaceAll('"', '').trim(), 'MMddyyyy')
            } catch (Exception e) {
                log.error("Error to parse date field: ${e.getMessage()}" )
                errorList.append("line $index has invalid date value <$customerDate>\n")
                return
            }
        }

        if (reason) {
            listDetails.reason = reason.replaceAll('"', '').trim()
        }

        if (notes) {
            listDetails.notes = notes.replaceAll('"', '').trim()
        }

        listDetails.listRefId = uploadReq.listRefId
        listDetails.upLoadRefId = uploadReq.id
		listDetails.matchCCNDC = midDataService.findMatchingCCNDC(dialCode)
        listDetails.active = true
        listDetails.lastUpdated = new Date()
        listDetails.lastUpdatedBy = uploadReq.lastUpdatedBy
		listEntries.add(listDetails)
	}

	
	void validateListEntry(ListDetails listDetail) throws AppException {
        if (listDetail.dialPattern == '') {
            throw new AppException("Miss Dial Pattern")
        }
		
		if (!listDetail.listRefId) {
			throw new AppException("Miss List ID")			
		}

        if ( ! (listDetail.dialPattern ==~ /^\d+$/) ) {
            throw new AppException("Dial Pattern contains non digit character")
        }

        if (listDetail.dialPattern.size() > 15) {
            throw new AppException("Dial Pattern is over the max length of 15")
        }

        if (listDetail.reason && listDetail.reason.size() > 100) {
            throw new AppException("The Reason field is over the max length of 100")
        }

        if (listDetail.notes && listDetail.notes.size() > 100) {
            throw new AppException("The Notes field is over the max length of 100")
        }

        if (hasDialCodeInDB(listDetail.dialPattern, listDetail.listRefId)) {
            throw new AppException("Dial Pattern <${listDetail.dialPattern}> already exists in the same list\n")
        }

	}
	
	//check duplicate dial code in the input file
    boolean hasDuplicateEntry(listEntries, dialPattern) {
        for (def listDetail : listEntries) {
            if (listDetail.dialPattern.equals(dialPattern)) {
				return true;			
            }
        }
        return false
    }

    //check duplicate dial pattern in database
    boolean hasDialCodeInDB(dialCode, listId) {
        ListDetails listDetails = listDetailRepo.findOneByListRefIdAndDialPattern(listId, dialCode)
        return listDetails != null
    }
}
