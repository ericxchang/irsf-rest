def header = []
def data = []
data[0] = '"1732-56789 123330",02212016,partner provided, verizon wireless"'
data[1] = '1732 56789 123331,,, verizon wireless"'
data[2] = '1732 56789 123332'
data[3] = '"1732-AB789 123333",02212016,partner provided, verizon wireless"'
data[4] = ',02212016,partner provided, verizon wireless"'


def headerMap = parseHeader(data, ",")
data.eachWithIndex {item, index ->
    parseListLine(headerMap, item, ++index, ',')
}

println "processing data2 ...."
def data2 = []
data2[0] = 'dialCode|date|description|notes'
data2[1] = '"1732-56789 123338"|02212018|partner provided|verizon wireless"'

headerMap = parseHeader(data2, "|")
data2.eachWithIndex {item, index ->
    parseListLine(headerMap, item, ++index, "|")
}

println "processing data3 ...."
def data3 = []
data3[0] = 'date|dialCode|notes|description'
data3[1] = '02212016|"(1732)-56789 123339"|partner provided|verizon wireless"'
data3[2] = '22BC016|"1732-56789 123339"|partner provided|verizon wireless"'
data3[3] = '02212016|"1732ABC"|partner provided|verizon wireless"'

headerMap = parseHeader(data3, "|")
data3.eachWithIndex {item, index ->
    parseListLine(headerMap, item, ++index, "|")
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

void parseListLine(headerMap, row, index, delimiter) {
    def elements = row.split(/\$delimiter/)

    def dialCode = elements.size() > headerMap['dialcode'] ? elements[headerMap['dialcode']] : ''
    def customerDate = elements.size() > headerMap['date'] ? elements[headerMap['date']] : null
    def description = elements.size() > headerMap['description'] ? elements[headerMap['description']] : null
    def notes = elements.size() > headerMap['notes'] ? elements[headerMap['notes']] : null

    ['"', ' ', '-', '(', ')'].each {
        dialCode =  dialCode.replaceAll(/\${it}/, '')
    }

    if (dialCode == '') {
        println("line $index does NOT have dial code")
        return
    }

    if ( ! (dialCode ==~ /^\d+$/) ) {
        println("line $index contains non digit character in dial code field")
        return
    }

    ListDetails listDetails = new ListDetails()
    listDetails.dialPattern = dialCode

    if (customerDate) {
        try {
            listDetails.customerDate = Date.parse('MMddyyyy', customerDate.replaceAll('"', '').trim())
        } catch (Exception e) {
            println("line $index has invalid date value <$customerDate>")
            return
        }
    }

    if (description) {
        listDetails.description = description.replaceAll('"', '').trim()
    }

    if (notes) {
        listDetails.notes = notes.replaceAll('"', '').trim()
    }

    println listDetails.dump()
}

boolean hasDuplicateEntry(listEntries, dialPattern) {
    listEntries.each {
        if (it.dialPattern.equals(dialPattern)) {
            return true
        }
    }
    return false
}

class ListDetails {
    String dialPattern
    String description
    String notes
    Date customerDate
    boolean active
    private Date lastUpdated
    private String lastUpdatedBy
}


