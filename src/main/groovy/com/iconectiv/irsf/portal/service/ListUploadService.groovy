package com.iconectiv.irsf.portal.service

import com.iconectiv.irsf.portal.model.customer.ListDetails
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository
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

	void parseBlackWhiteListData(ListUploadRequest uploadReq, List<ListDetails> listEntries, StringBuilder errorList) {
        def headerMap = parseHeader(uploadReq.data, uploadReq.delimiter)
		uploadReq.data.eachWithIndex {item, index -> 
			parseListLine(item, ++index, uploadReq, uploadReq.delimiter, headerMap, listEntries, errorList)
		}
	}

    def parseHeader(data, delimiter) {
        def headerMap = [:]
        if (data[0]==~ /.*(?i)dialcode.*/) {
            def elements = data[0].split(/\$delimiter/)

            elements.eachWithIndex {item, index ->
                headerMap[item.toLowerCase()] = index
            }
            data.remove(0)
        } else {
            headerMap['dialcode'] = 0
            headerMap['date'] = 1
            headerMap['description'] = 2
            headerMap['notes'] = 3
        }
        return headerMap
    }
	
	void parseListLine(String item, Integer index, ListUploadRequest uploadReq, String delimiter, headerMap, listEntries, errorList) {
		def elements = item.split(/\$delimiter/)
        def dialCode = elements.size() > headerMap['dialcode'] ? elements[headerMap['dialcode']] : ''
        def customerDate = elements.size() > headerMap['date'] ? elements[headerMap['date']] : null
        def description = elements.size() > headerMap['description'] ? elements[headerMap['description']] : null
        def notes = elements.size() > headerMap['notes'] ? elements[headerMap['notes']] : null

        ['"', ' ', '-', '(', ')'].each {
            dialCode =  dialCode.replaceAll(/\${it}/, '')
        }

        if (dialCode == '') {
            errorList.append("line $index does NOT have dial code\n")
            return
        }

        if ( ! (dialCode ==~ /^\d+$/) ) {
            errorList.append("line $index contains non digit character in dial code field\n")
            return
        }

        if (hasDuplicateEntry(listEntries, dialCode)) {
            errorList.append("line $index dial code <${dialCode}> already exists in the file\n")
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
                listDetails.customerDate = Date.parse('MMddyyyy', customerDate.replaceAll('"', '').trim())
            } catch (Exception e) {
                log.error("Error to parse date field: ${e.getMessage()}" )
                errorList.append("line $index has invalid date value <$customerDate>\n")
                return
            }
        }

        if (description) {
            listDetails.description = description.replaceAll('"', '').trim()
        }

        if (notes) {
            listDetails.notes = notes.replaceAll('"', '').trim()
        }

        listDetails.listRefId = uploadReq.listRefId
        listDetails.upLoadRefId = uploadReq.id
        listDetails.active = true
        listDetails.lastUpdated = new Date()
        listDetails.lastUpdatedBy = uploadReq.lastUpdatedBy
		listEntries.add(listDetails)
	}

    //check duplicate dial code in the input file
    boolean hasDuplicateEntry(listEntries, dialPattern) {
        listEntries.each {
            if (it.dialPattern.equals(dialPattern)) {
                return true
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
