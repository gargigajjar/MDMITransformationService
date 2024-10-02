try {
 load("nashorn:mozilla_compat.js");
} 
catch (e) 
{ 
}
importPackage(org.mdmi.core.engine.javascript);
function mapStringToPhysicalQuantity(source,target) {
target.setValue('value',source);
}
function mapPhysicalQuantityToString(source,target) {

if (source.getValue('value') + null) {
target.setValue(source.getValue('value'));
}
}

function mapPhysicalQuantityUnitToString(source,target) {

if (source.getValue('unit') + null) {
target.setValue(source.getValue('unit'));
}
}
function mapStringToCodedElement(source,target) {
	target.setValue('code', source);
}
function mapCodedElementToString(source,target) {
	target.setValue(source.getValue('code'));	
}
 
 
function mapClaimItemLocationToString(source,target){
	var result = "";
	result += source.getValue('code');

	result += ":";
	if(source.getValue('codeSystem') == "https://www.nubc.org/CodeSystem/TypeOfBill"){
		result += "A";
	}else{
		result += "B";
	}
		
	target.setValue(result);
}
function mapStringToPersonName(source,target) {
	Utils.StringToPatientName(source,target);
}

 function mapPersonNameToString(source,target) {
 
	 var personName = '';
	 
	 var prefixSource = source.getXValue('prefix');
	var i;
//	 var prefixTarget = target.getXValue('prefix');

		for (i = 0; i < prefixSource.getValues().size(); i++) {
			personName += ' ' + prefixSource.getValue(i);
//			prefixTarget.addValue(prefixSource.getValue(i));
		}

		var givenSource = source.getXValue('given');
//		var givenTarget = target.getXValue('given');

		for (i = 0; i < givenSource.getValues().size(); i++) {
			personName += ' ' + givenSource.getValue(i);
//			givenTarget.addValue(givenSource.getValue(i));
		}

		var familySource = source.getXValue('family');
//		var familyTarget = target.getXValue('family');

		for (i = 0; i < familySource.getValues().size(); i++) {
			personName += ' ' + familySource.getValue(i);
//			familyTarget.addValue(familySource.getValue(i));
		}

		target.setValue(personName);
}

function mapPersonNamePrefixToString(source,target){
	if(source.getValue('prefix')){
		target.setValue(source.getXValueAsString('prefix'));
	}else{
		target.setValue("999AaA999");
	}
}

function mapPersonNameSuffixToString(source,target){
	if(source.getValue('suffix')){
		target.setValue(source.getXValueAsString('suffix'));
	}else{
		target.setValue("999AaA999");
	}
}

function mapPersonNameFamilyToString(source,target){
	if(source.getValue('family')){
		target.setValue(source.getXValueAsString('family'));
	}else{
		target.setValue("999AaA999");
	}
}

function mapPersonNameGivenToString(source,target){
	var names = source.getXValue('given');
	if(source.getValue('given')){
		target.setValue(names.getValues().get(0));
	}else{
		target.setValue("999AaA999");
	}
}

function mapPersonNameMiddleToString(source,target){
	var names = source.getXValue('given');
	if(names.size() > 1){
		target.setValue(names.getValues().get(1));
	}else{
		target.setValue("999AaA999");
	}
}function mapStringToTelecom(source,target) {
	target.setValue('value',source);
}
function mapTelecomToString(source,target) {
	target.setValue(source.getValue('value'));
}

function mapTelecomSystemToString(source,target) {
	var sys = source.getValue('system');
	var newSys;
	if(sys == "phone" || sys == "pager" || sys == "sms"){
		newSys = "TE";
	}else if(sys == "email"){
		newSys = "EM";
	}else if(sys == "fax"){
		newSys = "FX";
	}else if(sys == "url"){
		newSys = "UR";
	}else{
		newSys = "";
	}
	target.setValue(newSys);
}function mapStringToInstanceIdentifier(source,target) {
	var values = source.split(":");
	if(values.length > 1){
		target.setValue('extension', values[0]);
		target.setValue('root', values[1]);
	}else{
		target.setValue('extension', source);
	}
}

function mapInstanceIdentifierToString(source,target) {
	var value;
		 if (source.getValue('extension') != null) {
			value = source.getValue('extension');
		}else if (source.getValue('root') != null) {
			value = source.getValue('root');
		}
	
	if (value != null) {
		target.setValue(value);
	}else{
		target.setValue("999AaA999");
	}
}

function mapInstanceIdentifierAssignerToString(source,target) {
	var value;
	if (source.getValue('assigningAuthorityName') != null) {
		value = source.getValue('assigningAuthorityName');
	}
	
	if (value != null) {
		target.setValue(value);
	}else{
		target.setValue("999AaA999");
	}
}

function mapInstanceIdentifierToStringIdentifierType(source,target) {
		if (source.getValue('type') != null) {
			var stype = source.getValue('type');
			var ttype;
			if(stype == "MC"){
				ttype = "F6";
			}else if(stype == "MA"){
				ttype = "NQ";
			}else if(stype == "SS" || stype == "SB"){
				ttype = "SY";
			}else if(stype == "U"){
				ttype = "PI";
			}else if(stype == "NPI"){
				ttype = "XX";
			}else if(stype == "SL"){
				ttype = "0B";
			}else if(stype == "FI"){
				ttype = "1J";
			}else if(stype == "EN"){
				ttype = "EI";
			}else{
				ttype = stype;
			}
			target.setValue(ttype);
		}else{
		target.setValue("999AaA999");
	}
}function mapStringToAddress(source,target) {	
	Utils.StringToAddress(source,target);
}
 function mapAddressToString(source,target) {
target.setValue(source.getXValueAsString('streetAddressLine') + ' ' + source.getXValueAsString('city') + ' ' + source.getXValueAsString('state') + ' ' + source.getXValueAsString('postalCode'));
}

function mapAddressStreetOneToString(source,target){
	var street = source.getXValue('streetAddressLine');
	if(street.getValues().get(0)){
		target.setValue(street.getValues().get(0));
	}else{
		target.setValue("999AaA999");
	}
}

function mapAddressStreetTwoToString(source,target){
	var street = source.getXValue('streetAddressLine');
	if(street.size() > 1){
		target.setValue(street.getValues().get(1));
	}
	else{
		target.setValue("999AaA999");
	}
}

function mapAddressCityToString(source,target){
	if(source.getValue('city')){
		target.setValue(source.getXValueAsString('city'));
	}else{
		target.setValue("999AaA999");
	}
}

function mapAddressStateToString(source,target){
	if(source.getValue('state')){
		target.setValue(source.getXValueAsString('state'));
	}else{
		target.setValue("999AaA999");
	}
}

function mapAddressCountryToString(source,target){
	if(source.getValue('country')){
		target.setValue(source.getXValueAsString('country'));
	}else{
		target.setValue("999AaA999");
	}
}

function mapAddressPostalCodeToString(source,target){
	if(source.getValue('postalCode')){
		target.setValue(source.getXValueAsString('postalCode'));
	}else{
		target.setValue("999AaA999");
	}
}
function mapStringToBoolean(source,target) { 
	if(source == "Y"){
		target.setValue("true");
	}else{
		target.setValue("false");
	}
}
 
function mapBooleanToString(source,target) { 
	if(source.getValue() == "true"){
		target.setValue("Y");
	}else{
		target.setValue("N");
	}
}
 
function mapStringToDateTime(source,target) {
	var temp = source.split("-");
	target.setValue(org.mdmi.core.engine.javascript.Utils.FormatDate("yyyyMMddHHmmssZ","yyyy-MM-dd'T'hh:mm:ss",temp[0]));
}
function mapDateTimeToString(source,target) {
	target.setValue(org.mdmi.core.engine.javascript.Utils.FormatDate("yyyy-MM-dd'T'hh:mm:ss","yyyyMMddHHmmssZ",source.getValue()));
}function mapStringToInteger(source,target) { 
	target.setValue(source);
}
 
function mapIntegerToString(source,target) { 
	target.setValue(source.getValue());
}
 
function mapDateTimeToStringDate(source,target) {
	var times = source.getValue().split("T");
	if(times.length > 1){
		target.setValue(times[0]);
	}else{
		target.setValue(source.getValue());
	}
}function mapDateTimeToStringTime(source,target) {
	var times = source.getValue().split("T");
	if(times.length > 1){
		target.setValue(times[1]);
	}else{
		target.setValue(source.getValue());
	}
}function mapStringToClaimRFollowupCodedElement(source,target){
	target.setValue('code', source);
	target.setValue('codeSystem' , "https://codesystem.x12.org/005010/889");
}function mapStringToCommunicatedDiagCode(source,target){
	var vals = source.split(":");
	if(vals.length > 2){
		target.setValue('code',vals[1]);
		target.setValue('codeSystem' , vals[0]);
	}
}function mapDiagCodeToString(source,target){
	var vals = "";
	
	vals += source.getValue("codeSystem");
	vals += ":";
	vals += source.getValue("code");
	target.setValue(vals);
}function mapStringToClaimErrorCode(source,target){
	target.setValue('code', source);
	target.setValue('codeSystem' , "https://codesystem.x12.org/005010/901");
}function mapStringToClaimResponseAdjudicationReview(source,target){
	target.setValue('code', source);
	target.setValue('codeSystem' , "https://codesystem.x12.org/005010/306");
}function mapToSV1ProductOrServiceCode(source,target){
	var vals = source.split(":");
	if(vals.length > 1){
		target.setValue('code', vals[1]);
		var regex = /^\d/.test(vals[1]);
		
		if(vals[0] == "HC"){
			if(regex){
				target.setValue('codeSystem', "http://www.ama-assn.org/go/cpt");
			}else{
				target.setValue('codeSystem', "http://www.cms.gov/Medicare/Coding/HCPCSReleaseCodeSets");
			}
		}else if(vals[0] == "N4"){
			target.setValue('codeSystem', "http://hl7.org/fhir/sid/ndc");
		}else{
			target.setValue('codeSystem', vals[0]);
		}
	}
}function mapProductOrServiceCodeToSV(source,target){
	var vals = "";
		if(source.getValue('codeSystem') == "http://hl7.org/fhir/sid/ndc"){
			vals += "N4";
		}else if(source.getValue('codeSystem') == "http://www.ama-assn.org/go/cpt" || source.getValue('codeSystem') == "http://www.cms.gov/Medicare/Coding/HCPCSReleaseCodeSets"){
			vals += "HC";
		}
	vals += ":";
	vals += source.getValue('code');
	if(source.getValue('originalText')){
		vals += ":::::";
		vals += source.getValue('originalText');
	}else if(source.getValue('displayName')){
		vals += ":::::";
		vals += source.getValue('displayName');
	}

	target.setValue(vals);
}function mapToSV1ModifierCode(source,target){
	var vals = source.split(":");
	
	if(vals.length > 2){
			var regex = /^\d/.test(vals[2]);
			
			if(vals[0] == "HC"){
				if(regex){
					target.setValue('codeSystem', "http://www.ama-assn.org/go/cpt");
				}else{
					target.setValue('codeSystem', "http://www.cms.gov/Medicare/Coding/HCPCSReleaseCodeSets");
				}
			}else if(vals[0] == "N4"){
				target.setValue('codeSystem', "http://hl7.org/fhir/sid/ndc");
			}else{
				target.setValue('codeSystem', vals[0]);
			}
			target.setValue('code', vals[2]);
	}
}function mapToSV1ProductOrServiceCodeEnd(source,target){
	var vals = source.split(":");
	if(vals.length > 7){
		target.setValue('code', vals[7]);
		var regex = /^\d/.test(vals[7]);
		
		if(vals[0] == "HC"){
			if(regex){
				target.setValue('codeSystem', "http://www.ama-assn.org/go/cpt");
			}else{
				target.setValue('codeSystem', "http://www.cms.gov/Medicare/Coding/HCPCSReleaseCodeSets");
			}
		}else if(vals[0] == "N4"){
			target.setValue('codeSystem', "http://hl7.org/fhir/sid/ndc");
		}else{
			target.setValue('codeSystem', vals[0]);
		}
	}
}function mapStringToPhysicalQuantityUSD(source,target){
	target.setValueSafely('value' , source);
	target.setValue('unit' , "USD");
}function isNM1UtilizationManagementOrganizationUMOName2010A(target){if ('PR' === target.value() || '2B' === target.value() || '00' === target.value()) {return true;} else {return false;}}function isMSGMessageText2000E(target){if ('freeFormMessage' === target.value()) {return true;} else {return false;}}function isDTPDischargeDate2000E(target){if ('dischargeDates' === target.value()) {return true;} else {return false;}}function isDTPEventDate2000E(target){if ('patientEvent' === target.value()) {return true;} else {return false;}}function isDTPAdmissionDate2000E(target){if ('admissionDates' === target.value()) {return true;} else {return false;}}function isSV2InstitutionalServiceLine2000F(target){if ('institutional' === target.value()) {return true;} else {return false;}}function isSV1ProfessionalService2000F(target){if ('professional' === target.value()) {return true;} else {return false;}}function isLoop2010D(target){if ('beneficiary' === target.value()) {return true;} else {return false;}}function isINSDependentRelationship2010D(target){if ('beneficiary' === target.value()) {return true;} else {return false;}}function isN4DependentCityStateZIPCode2010D(target){if ('beneficiary' === target.value()) {return true;} else {return false;}}function isDMGDependentDemographicInformation2010D(target){if ('beneficiary' === target.value()) {return true;} else {return false;}}function isNM1DependentName2010D(target){if ('beneficiary' === target.value()) {return true;} else {return false;}}function isN3DependentAddress2010D(target){if ('beneficiary' === target.value()) {return true;} else {return false;}}function isLoop2010C(target){if ('subscriber' === target.value()) {return true;} else {return false;}}function isN3SubscriberAddress2010C(target){if ('subscriber' === target.value()) {return true;} else {return false;}}function isDMGSubscriberDemographicInformation2010C(target){if ('subscriber' === target.value()) {return true;} else {return false;}}function isNM1SubscriberName2010C(target){if ('subscriber' === target.value()) {return true;} else {return false;}}function isN4SubscriberCityStateZIPCode2010C(target){if ('subscriber' === target.value()) {return true;} else {return false;}}function isLoop2010B(target){if ('X3' === target.value() || '1P' === target.value() || 'QV' === target.value() || 'IT' === target.value()) {return true;} else {return false;}}function isN3RequesterAddress2010B(target){if ('X3' === target.value() || '1P' === target.value() || 'QV' === target.value() || 'IT' === target.value()) {return true;} else {return false;}}function isNM1RequesterName2010B(target){if ('X3' === target.value() || '1P' === target.value() || 'QV' === target.value() || 'IT' === target.value()) {return true;} else {return false;}}function isN4RequesterCityStateZIPCode2010B(target){if ('X3' === target.value() || '1P' === target.value() || 'QV' === target.value() || 'IT' === target.value()) {return true;} else {return false;}}function sourceCheckFilter(target,properties) { if (properties.containsKey('VALUESET')) {     if (properties.get('VALUESET').containsKey(target)) {                  return true;     } }   return false } function targetCheckFilter(target,properties) { if (properties.containsKey('VALUESET')) {     if (properties.get('VALUESET').containsKey(target.value())) {                  return true;     } }   return false } 