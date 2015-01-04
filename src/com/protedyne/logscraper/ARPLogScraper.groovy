package com.protedyne.logscraper

def filter = ~/.*\.log/
def pattern = /.*Tracking (.*) : (\d*\.?\d*) seconds/
def countMap = [:].withDefault { 0 }
def totalMap = [:].withDefault { 0.0 }

//new File("\\Users\\daveb\\Downloads\\ARP-Logs\\BN").eachFileMatch(filter) { file ->
//new File("\\Users\\daveb\\Downloads\\ARP-Logs\\TA").eachFileMatch(filter) { file ->
//new File("\\Users\\daveb\\Downloads\\ARP-Logs\\Local").eachFileMatch(filter) { file ->
new File("\\ARP-Logs\\TA").eachFileMatch(filter) { file ->
	def count = 0;
	file.eachLine { line ->
		matcher = (line =~ pattern)  
		if  (matcher.size() > 0) {
			String mapKey = matcher[0][1]
			double time = Double.parseDouble(matcher[0][2])
			countMap[mapKey]++
			totalMap[mapKey] = totalMap[mapKey] + time
			count++ 
		}
	}
		
	println "${file.getAbsoluteFile()} count ${count}"
//	countMap.keySet().each { mapKey ->
//		double average = totalMap[mapKey] / countMap[mapKey] 
//		println sprintf("%s : %d %3.6f for and average of %3.6f seconds", mapKey, countMap[mapKey], totalMap[mapKey], average)
//	}
}

//report results
def resultsMap = [:]
def averageMap = [:]
countMap.keySet().each { mapKey ->
	double average = totalMap[mapKey] / countMap[mapKey]
	averageMap[mapKey] = average
	resultsMap[mapKey] = sprintf("%s,%d,%3.6f,%3.6f", mapKey, countMap[mapKey], totalMap[mapKey], average)
}

//Map sortedMap = averageMap.sort { it.value } //sort by value  
//Map sortedMap = averageMap.sort { it1, it2 -> it1.value <=> it2.value } // sort by value with sorting logic
Map sortedMap = averageMap.sort { it1, it2 -> it2.value <=> it1.value } // sort by value reverse

println "Process,Count,Total Time (sec),Average Time (sec)"
sortedMap.keySet().each { mapKey ->
	double average = totalMap[mapKey] / countMap[mapKey]
	println sprintf("%s,%d,%3.6f,%3.6f", mapKey, countMap[mapKey], totalMap[mapKey], average)
}
