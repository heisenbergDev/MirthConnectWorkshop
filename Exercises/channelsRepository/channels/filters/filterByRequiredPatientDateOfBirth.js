var patientName = '' + msg['PID']['PID.7'].toString();

if (patientName.length == 0) {
	return false;
} else {
	return true;
}