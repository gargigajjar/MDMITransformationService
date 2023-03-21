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
function mapStringToCodedElement(source,target) {
	target.setValue('code', source);
}

function mapStringToOBSStatus(source,target) {
	target.setValue('code', "final");
}

function mapStringToPROCStatus(source,target) {
	target.setValue('code', "completed");
}

function mapStringToEnrollmentCodedElement(source,target) {
	target.setValue('code', "50001-2");
	target.setValue('codeSystem', "urn:guid:94458cc8-b2dd-4d4b-9b99-8b955f7ac708");
	target.setValue('displayName', "School Enrollment Status");
}

function mapStringToBMICode(source,target) {
	target.setValue('code', '39156-5');
	target.setValue('codeSystem', 'http://loinc.org');
	target.setValue('displayName', 'Body mass index (BMI)');
}

function mapStringToSexualOrientationCode(source,target) {
	target.setValue('code', '76690-7');
	target.setValue('codeSystem', 'http://loinc.org');
	target.setValue('displayName', 'Sexual orientation');
}function mapCodedElementToString(source,target) {
	target.setValue(source.getValue('code'));	
}
 
 
function mapStringToPersonName(source,target) {
	Utils.StringToPatientName(source,target);
	target.setValue("fullName", source);
}

 function mapPersonNameToString(source,target) {
 
	 var personName = '';
	 
	 var prefixSource = source.getXValue('prefix');
	
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
 function mapStringToTelecom(source,target) {
target.setValue('value',source);
}
 function mapTelecomToString(source,target) {
target.setValue(source.getValue('value'));
}
function mapStringToInstanceIdentifier(source,target) {
	target.setValue('extension', source );
}

function mapInstanceIdentifierToString(source,target) {
	var value;
	if (source.getValue('root') != null && source.getValue('extension') != null) {
		value = source.getValue('root') + ":" + source.getValue('extension');
	} else {
		if (source.getValue('root') != null) {
			value = source.getValue('root');
		}
		if (source.getValue('extension') != null) {
			value = source.getValue('extension');
		}
	}
	if (value != null) {
		target.setValue(value);
	}
}

function mapStringToAddress(source,target) {	
	Utils.StringToAddress(source,target);
}
 function mapAddressToString(source,target) {
target.setValue(source.getXValueAsString('streetAddressLine') + ' ' + source.getXValueAsString('city') + ' ' + source.getXValueAsString('state') + ' ' + source.getXValueAsString('postalCode'));
}
function mapStringToBoolean(source,target) { 
	target.setValue(source);
}
 
function mapStringToDateTime(source,target) {
	target.setValue(org.mdmi.core.engine.javascript.Utils.FormatDate("yyyyMMddHHmmssZ","yyyy-MM-dd'T'hh:mm:ss",source));
}
 function mapDateTimeToString(source,target) {
target.setValue(source.getValue());
}
function mapStringToInteger(source,target) { 
	target.setValue(source);
}
 
function mapIntegerToString(source,target) { 
	target.setValue(source.getValue());
}
 
function mapContainerToString(source,target) {}function mapStringToContainer(source,target) {}function mapFHIR_AddressToAddress(source, target) {

	if (source.getValue('city') != null) {
		target.setValue('city', source.getValue('city').getValue("value"));
	}
	if (source.getValue('country') != null) {
		target.setValue('country', source.getValue('country').getValue("value"));
	}
	if (source.getValue('state') != null) {
		target.setValue('state', source.getValue('state').getValue("value"));
	}
	if (source.getValue('postalCode') != null) {
		target.setValue('postalCode', source.getValue('postalCode').getValue("value"));
	}

	var lineSource = source.getXValue('line');
	var streetAddressLineTarget = target.getXValue('streetAddressLine');

	if (lineSource != null) {
		var index;
		for (index = 0; index < lineSource.getValues().size(); index++) {
			var fhirString = lineSource.getValues().get(index);
			var value = fhirString.getValue("value");
			streetAddressLineTarget.addValue(value);
		}
	}

}
function mapAddressToFHIR_Address(source, target) {

	if (source.getValue('city') != null) {
		target.setValueSafely('city.value', source.getValue('city'));
	}

	if (source.getValue('country') != null) {
		target.setValueSafely('country.value', source.getValue('country'));
	}

	if (source.getValue('county') != null) {
		target.setValueSafely('district.value', source.getValue('county'));
	}

	if (source.getValue('state') != null) {
		target.setValueSafely('state.value', source.getValue('state'));
	}

	if (source.getValue('type') != null) {
		target.setValueSafely('type.value', source.getValue('type'));
	}

	if (source.getValue('use') != null) {
		if(source.getValue('use').startsWith('postal')){
			target.setValueSafely('type.value', 'postal');
		}else{
			target.setValueSafely('use.value', source.getValue('use'));
		}
	}

	if (source.getValue('postalCode') != null) {
		target.setValueSafely('postalCode.value', source.getValue('postalCode'));
	}

	if (source.getValue('PeriodStart') != null) {
		target.setValueSafely('period.start.value', source.getValue('PeriodStart'));
	}

	if (source.getValue('PeriodEnd') != null) {
		target.setValueSafely('period.end.value', source.getValue('PeriodEnd'));
	}

	var streetSource = source.getXValue('streetAddressLine');
	if (streetSource != null) {
		target.setValueSafely('line.value', streetSource.getValues());
	}

}function mapFHIR_ContactPointToAddress(source,target) { 
}
 function mapAddressToFHIR_ContactPoint(source,target) { 
}
 function mapFHIR_booleanToBoolean(source,target) {
if (source.getValue('value') != null) {
target.setValue(source.getValue('value'));
}
}

 function mapBooleanToFHIR_boolean(source, target) {
	var val = source.getValue();
	if (val != null) {
		if(val.toLowerCase().startsWith('y') || val.toLowerCase().startsWith('t')){
			target.setValue('value', 'true');
		}else if(val.toLowerCase().startsWith('n') || val.toLowerCase().startsWith('f')){
			target.setValue('value', 'false');
		}else{
			target.setValue('value', 'true');
		}
	}else{
			target.setValue('value', 'true');
		}
}function mapFHIR_CodeableConceptToCodedElement(s,t) {

		if (s.getValue("coding") != null) {
				var sc =  s.getValue("coding");
				if (sc.getValue("code") != null) {
					t.setValue("code", sc.getValue("code").getValue("value"));
				}
				if (sc.getValue("system") != null) {
					t.setValue("codeSystem", sc.getValue("system").getValue("value"));
				}
				if (sc.getValue("display") != null) {
					t.setValue("displayName", sc.getValue("display").getValue("value"));
				}
			}
 	 
}function mapCodedElementToFHIR_CodeableConcept(source, target) {
	if (source.getValue('code') && (source.getValue('codeSystem') == null)) {
		target.setValueSafely('coding.display.value', source.getValue('code'));
	}
	else{
	if (source.getValue('code')) {
		target.setValueSafely('coding.code.value', source.getValue('code'));
	}

	if (source.getValue('originalText')) {
		target.setValueSafely('text.value', source.getValue('originalText'));
	} else if (source.getValue('displayName')) {
		target.setValueSafely('text.value', source.getValue('displayName'));
	}

	if (source.getValue('displayName') ) {
		target.setValueSafely('coding.display.value', source
				.getValue('displayName'));
	}else if (source.getValue('codeSystemName') ) {
		target.setValueSafely('coding.display.value', source
				.getValue('codeSystemName'));
	}

	if (source.getValue('codeSystem') ) {
			var sys = source.getValue('codeSystem');
		var regex = /^([0-2])((\.0)|(\.[1-9][0-9]*))*$/gm;
		
		if (regex.test(sys)){
			sys = "urn:oid:" + sys;
	    }
		target.setValueSafely('coding.system.value', sys);
	}
	
	}
	
	var translations = source.getXValue('translation');
	var i;
	for (i = 0; i < translations.getValues().size(); i++) {

		var s = translations.getValues().get(i);
		var t = target.addValueSafely('coding');

		if (s.getValueByName('code') ) {
			var c = t.addValueSafely('code');
			c.setValue('value', s.getValueByName('code'));
		}

		if (s.getValueByName('codeSystem')  ) {
			var cs = t.addValueSafely('system');
			var sys = s.getValueByName('codeSystem');
			var regex = /^([0-2])((\.0)|(\.[1-9][0-9]*))*$/gm;
		
			if (regex.test(sys)){
				sys = "urn:oid:" + sys;
	    	}
			cs.setValue('value', sys);
		}

		if (s.getValueByName('originalText')  ) {

			var dn = t.addValueSafely('display');
			dn.setValue('value', s.getValueByName('originalText'));

		} else if (s.getValueByName('displayName')  ) {

			var dn = t.addValueSafely('display');
			dn.setValue('value', s.getValueByName('displayName'));
		}else if (s.getValueByName('codeSystemName')  ) {

			var dn = t.addValueSafely('display');
			dn.setValue('value', s.getValueByName('codeSystemName'));
		}


	}

}function mapFHIR_CodingToCodedElement(s, t) {
    if (s.getValue("code") != null) {
        t.setValue("code", s.getValue("code").getValue("value"));
    }
    if (s.getValue("system") != null) {
        t.setValue("codeSystem", s.getValue("system").getValue("value"));
    }
    if (s.getValue("display") != null) {
        t.setValue("displayName", s.getValue("display").getValue("value"));
    }
}
function mapCodedElementToFHIR_Coding(s, t) {

	if (s.getValue('code') != null) {
		t.setValueSafely('code.value', s.getValue('code'));
	}

	if (s.getValue('displayName') != null) {
		t.setValueSafely('display.value', s.getValue('displayName'));
	}else if (s.getValue('codeSystemName') != null) {
		t.setValueSafely('display.value', s.getValue('codeSystemName'));
	}

	if (s.getValue('codeSystem') ) {
			var sys = s.getValue('codeSystem');
		var regex = /^([0-2])((\.0)|(\.[1-9][0-9]*))*$/gm;
		
		if (regex.test(sys)){
			sys = "urn:oid:" + sys;
	    }
		t.setValueSafely('system.value', sys);
	}

	var translations = s.getXValue('translation');
	var i;
	for (i = 0; i < translations.getValues().size(); i++) {

		var src = translations.getValues().get(i);

		if (src.getValueByName('code') ) {
			var c = t.addValueSafely('code');
			c.setValue('value', src.getValueByName('code'));
		}

		if (src.getValueByName('codeSystem')  ) {
			var cs = t.addValueSafely('system');
			var sys = src.getValueByName('codeSystem');
			var regex = /^([0-2])((\.0)|(\.[1-9][0-9]*))*$/gm;
		
			if (regex.test(sys)){
				sys = "urn:oid:" + sys;
	    	}
			cs.setValue('value', sys);
		}

		if (src.getValueByName('originalText')  ) {

			var dn = t.addValueSafely('display');
			dn.setValue('value', src.getValueByName('originalText'));

		} else if (src.getValueByName('displayName')  ) {

			var dn = t.addValueSafely('display');
			dn.setValue('value', src.getValueByName('displayName'));
		}else if (src.getValueByName('codeSystemName')  ) {

			var dn = t.addValueSafely('display');
			dn.setValue('value', src.getValueByName('codeSystemName'));
		}

	}
} function mapCodedElementToFHIR_Reference(source,target) {
 
	 var display = '';
	
	if (source.getValue('displayName') != null) {
		target.setValueSafely('display.value', source.getValue('displayName'));		
	}
	
	
	if (source.getValue('originalText') != null) {
		target.setValueSafely('display.value', source.getValue('originalText') );		
	} 
	
	
	var translations = source.getXValue('translation');

	for (i = 0; i < translations.getValues().size(); i++) {
 
		var s = translations.getValues().get(i);
 		if (s.getValue('displayName') != null) {
 
			target.setValueSafely('display.value', s.getValueByName('displayName'));

		}

	}
	 
}function mapFHIR_codeToCodedElement(source,target) {

if (source.getValue('value') != null) {
target.setValue('code', source.getValue('value'));
}
}

function mapCodedElementToFHIR_codeArray(source,target) {
	var days = source.getXValue('code');
	if (days != null) {
		target.setValueSafely('value', days.getValues());
		print(days.getValues());
	}
}function mapCodedElementToFHIR_code(source, target) {
    if (source.getValue('code') != null) {
        target.setValueSafely('value', source.getValue('code'));
    } else {
        if (source.getValue("displayName") != null) {
            target.setValueSafely('value', source.getValue('displayName'));
        }
    }
}
function mapFHIR_dateToDateTime(source,target) {

if (source.getValue('value') != null) {
	target.setValue( org.mdmi.core.engine.javascript.Utils
			.FormatDate("yyyy-MM-dd", "yyyy-MM-dd",
					source.getValue()));
}


}function mapDateTimeToFHIR_date(source, target) {
	target.setValue('value', org.mdmi.core.engine.javascript.Utils.FormatDate(
			"yyyy-MM-dd'T'hh:mm:ss+zzzz", "yyyy-MM-dd", source.getValue()));
}function mapFHIR_dateTimeToDateTime(source,target) {
	target.setValue('value', org.mdmi.core.engine.javascript.Utils
			.FormatDate("yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd'T'hh:mm:ss",
					source.getValue()));
}function mapDateTimeToFHIR_dateTime(source, target) {

		target.setValue('value', org.mdmi.core.engine.javascript.Utils
			.FormatDate("yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd'T'hh:mm:ss+zzzz",
					source.getValue()));
}function mapFHIR_instantToDateTime(source,target) {
	 target.setValue( org.mdmi.core.engine.javascript.Utils
				.FormatDate("yyyy-MM-dd'T'hh:mm:ss+zzzz", "yyyy-MM-dd'T'hh:mm:ss",
						source.getValue('value')));
	}function mapDateTimeToFHIR_instant(source,target) {
 target.setValueSafely('value', org.mdmi.core.engine.javascript.Utils
			.FormatDate("yyyy-MM-dd'T'hh:mm:ss+zzzz", "yyyy-MM-dd'T'hh:mm:ss+zzzz",
					source.getValue()));
}function mapDocumentCustodianOrganizationEmailContactTelecomToFHIR_ContactPoint(source,target) {
target.setValueSafely('use.value', 'work');
target.setValueSafely('system.value', 'email');
target.setValueSafely('value.value', source.getValue('value'));
}function mapDocumentCustodianOrganizationFaxConcatTelecomToFHIR_ContactPoint(source,target) {
target.setValueSafely('use.value', 'work');
target.setValueSafely('system.value', 'fax');
target.setValueSafely('value.value', source.getValue('value'));
}function DocumentCustodianOrganizationPagerContactTelecomToFHIR_ContactPoint(source,target) {
target.setValueSafely('use.value', 'work');
target.setValueSafely('system.value', 'pager');
target.setValueSafely('value.value', source.getValue('value'));
}function mapDocumentCustodianOrganizationPhoneTelecomToFHIR_ContactPoint(source,target) {
target.setValueSafely('use.value', 'work');
target.setValueSafely('system.value', 'phone');
target.setValueSafely('value.value', source.getValue('value'));
}function mapDocumentCustodianOrganizationSMSContactTelemcomToFHIR_ContactPoint(source,target) {
target.setValueSafely('use.value', 'work');
target.setValueSafely('system.value', 'sms');
target.setValueSafely('value.value', source.getValue('value'));
}function mapDocumentCustodianOrganizationURLTelecomToFHIR_ContactPoint(source,target) {
target.setValueSafely('use.value', 'work');
target.setValueSafely('system.value', 'url');
target.setValueSafely('value.value', source.getValue('value'));
}function mapFHIR_AddressToString(source,target) { 
}
 function mapStringToFHIR_Address(source,target) { 
}
 function mapFHIR_AgeToPhysicalQuantity(source,target) { 
}
 function mapPhysicalQuantityToFHIR_Age(source,target) { 
}
 function mapFHIR_AnnotationToString(source,target)
{
  if(source.getValue('text') != null) {
        target.setValue(source.getValue('text').getValue("value"));
    }
}function mapStringToFHIR_Annotation(source,target) {

 	 
					 target.setValueSafely('text.value', source.getValue());
			 

}function mapFHIR_CodeableConceptToString(source,target) { 
}
 function mapStringToFHIR_CodeableConcept(source,target) { 
	
	 target.setValueSafely('text.value', source.getValue());
	 
}
 function mapPatientEmailContactTelecomToFHIR_ContactPoint(source,target) {
	target.setValueSafely('use.value', source.getValue('use'));	
	target.setValueSafely('system.value', 'email');
	target.setValueSafely('value.value', source.getValue('value'));
}function mapFHIR_ContactPointToString(source,target) { 
}
 function mapStringToFHIR_ContactPoint(source,target) { 
}
 function mapFHIR_ContactPointToTelecom(source,target) {
	if(source.getValue('use') != null) {
		target.setValue('use', source.getValue('use').getValue('value'));
	}
	if(source.getValue('value') != null) {
		target.setValue('value', source.getValue('value').getValue('value'));
	}
}

function mapTelecomToFHIR_ContactPoint(source,target) {

	if(source.getValue('use') != null) {
		target.setValueSafely('use.value', source.getValue('use'));
	}
	if(source.getValue('value') != null) {
		target.setValueSafely('value.value', source.getValue('value'));
	}
	if(source.getValue('system') != null) {
        target.setValueSafely('system.value', source.getValue('system'));
    }else{
		target.setValueSafely('system.value', 'phone');
	}

	if(source.getValue('PeriodStart') != null) {
		target.setValueSafely('period.start.value', source.getValue('PeriodStart'));
	}

	if(source.getValue('PeriodEnd') != null) {
		target.setValueSafely('period.end.value', source.getValue('PeriodEnd'));
	}

	if(source.getValue('rank') != null) {
		target.setValueSafely('rank.value', source.getValue('rank'));
	}
}function mapFHIR_HumanNameToPersonName(source, target) {

	var familySource = source.getXValue('family');
	var familyTarget = target.getXValue('family');
	var index;
	if (familySource != null) {

		for (index = 0; index < familySource.getValues().size(); index++) {
			var fhirString = familySource.getValues().get(index);
			var value = fhirString.getValue("value");
			familyTarget.addValue(value);

		}
	}

	var givenSource = source.getXValue('given');
	var givenTarget = target.getXValue('given');

	if (givenSource != null) {

		for (index = 0; index < givenSource.getValues().size(); index++) {
			var fhirString = givenSource.getValues().get(index);
			var value = fhirString.getValue("value");
			givenTarget.addValue(value);

		}
	}

	var suffixSource = source.getXValue('suffix');
	var suffixTarget = target.getXValue('suffix');

	if (suffixSource != null) {

		for (index = 0; index < suffixSource.getValues().size(); index++) {
			var fhirString = suffixSource.getValues().get(index);
			var value = fhirString.getValue("value");
			suffixTarget.addValue(value);

		}
	}

	var prefixSource = source.getXValue('prefix');
	var prefixTarget = target.getXValue('prefix');

	if (prefixSource != null) {

		for (index = 0; index < prefixSource.getValues().size(); index++) {
			var fhirString = prefixSource.getValues().get(index);
			var value = fhirString.getValue("value");
			prefixTarget.addValue(value);

		}
	}

}function mapPersonNameToFHIR_HumanName(source, target) {


    var familySource = source.getXValue('family');
    if(familySource != null) {
        target.setValueSafely('family.value', familySource.getValues());
    }

    var givenSource = source.getXValue('given');
    if(givenSource != null) {
        target.setValueSafely('given.value', givenSource.getValues());
    }

    var prefixSource = source.getXValue('prefix');
    if(prefixSource != null) {
        target.setValueSafely('prefix.value', prefixSource.getValues());
    }

    var suffixSource = source.getXValue('suffix');
    if(suffixSource != null) {
        target.setValueSafely('suffix.value', suffixSource.getValues());
    }

	var temp = source.getValue('use');
	if(temp){
		if(temp.startsWith('L')){
			target.setValueSafely('use.value', 'official');
		}else{
			target.setValueSafely('use.value', source.getValue('use'));
		}
	}

	if(source.getValue('PeriodStart')){
		target.setValueSafely('period.start.value', source.getValue('PeriodStart'));
	}

	if(source.getValue('PeriodEnd')){
		target.setValueSafely('period.end.value', source.getValue('PeriodEnd'));
	}

}function mapFHIR_HumanNameToString(source,target) { 
}
 function mapStringToFHIR_HumanName(source,target) { 
}
 function mapFHIR_IdentifierToInstanceIdentifier(source, target) {

	if (source.getValue('system') != null) {
		target.setValue('root', source.getValue('system').getValue("value"));
	}
	if (source.getValue('value') != null) {
		target
				.setValue('extension', source.getValue('value').getValue(
						"value"));
	}

	// if(source.getValue('assigner') != null) {
	// target.setValue('root',
	// source.getValue('assigner').getValue("reference").getValue("value"));
	// }

}function mapInstanceIdentifierToFHIR_Identifier(source,target) {
	if(source.getValue('extension') == null && source.getValue('root') != null){
		 target.setValueSafely('value.value', source.getValue('root'));
	}else{
		if(source.getValue('assigningAuthorityName') != null) {
        	target.setValueSafely('value.value', source.getValue('assigningAuthorityName'));
		}
	if(source.getValue('extension') != null) {
        target.setValueSafely('value.value', source.getValue('extension'));
	}
	else if (source.getValue('root') != null) {
        target.setValueSafely('value.value', source.getValue('root'));
    }
	if(source.getValue('root') != null) {
		var sys = source.getValue('root');
		var regex = /^([0-2])((\.0)|(\.[1-9][0-9]*))*$/gm;
		
		if (regex.test(sys)){
			sys = "urn:oid:" + sys;
	    }
		target.setValueSafely('system.value', sys);
    }
	}
	if(source.getValue('displayable') != null) {
        target.setValueSafely('assigner.display.value', source.getValue('displayable'));
	}

	if(source.getValue('use') != null) {
        target.setValueSafely('use.value', source.getValue('use'));
	}	

	if(source.getValue('PeriodStart') != null) {
        target.setValueSafely('period.start.value', source.getValue('PeriodStart'));
	}
	
	if(source.getValue('PeriodEnd') != null) {
        target.setValueSafely('period.end.value', source.getValue('PeriodEnd'));
	}
	
	if(source.getValue('type') != null) {
        if(source.getValue('type').startsWith('SS')){
		
			target.setValueSafely('type.coding.code.value', 'SS');
    		target.setValueSafely('type.coding.display.value', 'Social Security Number');
			target.setValueSafely('type.coding.system.value', 'http://terminology.hl7.org/CodeSystem/v2-0203');
    		target.setValueSafely('type.text.value', 'Social Security Number');
    		
		}else if(source.getValue('type').startsWith('MR')){
		
			target.setValueSafely('type.coding.code.value', 'MR');
    		target.setValueSafely('type.coding.display.value', 'Medical record number');
			target.setValueSafely('type.coding.system.value', 'http://terminology.hl7.org/CodeSystem/v2-0203');
    		target.setValueSafely('type.text.value', 'Medical record number');
    		
		}else if(source.getValue('type').startsWith('DL')){
		
			target.setValueSafely('type.coding.code.value', 'DL');
    		target.setValueSafely('type.coding.display.value', "Driver's license number");
			target.setValueSafely('type.coding.system.value', 'http://terminology.hl7.org/CodeSystem/v2-0203');
    		target.setValueSafely('type.text.value', "Driver's license number");
    		
		}else if(source.getValue('type').startsWith('VN')){
		
			target.setValueSafely('type.coding.code.value', 'VN');
    		target.setValueSafely('type.coding.display.value', "visit number");
			target.setValueSafely('type.coding.system.value', 'http://terminology.hl7.org/CodeSystem/v2-0203');
    		target.setValueSafely('type.text.value', "visit number");
    		
		}else if(source.getValue('type').startsWith('XX')){
		
			target.setValueSafely('type.coding.code.value', 'XX');
    		target.setValueSafely('type.coding.display.value', "Organization identifier");
			target.setValueSafely('type.coding.system.value', 'http://terminology.hl7.org/CodeSystem/v2-0203');
    		target.setValueSafely('type.text.value', "Organization identifier");
    		
		}else if(source.getValue('type').startsWith('NPI')){
		
			target.setValueSafely('type.coding.code.value', 'NPI');
    		target.setValueSafely('type.coding.display.value', "National provider identifier");
			target.setValueSafely('type.coding.system.value', 'http://terminology.hl7.org/CodeSystem/v2-0203');
    		target.setValueSafely('type.text.value', "National provider identifier");
    		
		}else if(source.getValue('type').startsWith('TAX')){
		
			target.setValueSafely('type.coding.code.value', 'TAX');
    		target.setValueSafely('type.coding.display.value', "Tax ID number");
			target.setValueSafely('type.coding.system.value', 'http://terminology.hl7.org/CodeSystem/v2-0203');
    		target.setValueSafely('type.text.value', source.getValue('displayable'));
    		
		}
		
	}
}function mapFHIR_MoneyToPhysicalQuantity(source, target) {

	if (source.getValue('value') != null) {
		target.setValue('value', source.getValue('value').getValue('value'));
	}

	if (source.getValue('unit') != null) {
		target.setValue('unit', source.getValue('unit').getValue('value'));
	}

}function mapPhysicalQuantityToFHIR_Money(source,target)
{

	 	if (source.getValue("value") != null) {
					 target.setValueSafely('value.value', source.getValue('value'));
				}
				
		if (source.getValue("unit") != null) {
					 target.setValueSafely('unit.value', source.getValue('unit'));
				}		

}function mapFHIR_QuantityToPhysicalQuantity(source,target)
{

if (source.getValue('value') != null) {
target.setValue('value', source.getValue('value').getValue('value'));
}

if (source.getValue('unit') != null) {
target.setValue('unit', source.getValue('unit').getValue('value'));
}


}function mapPhysicalQuantityToFHIR_Quantity(source,target)
{

	 	if (source.getValue("value") != null) {
					 target.setValueSafely('value.value', source.getValue('value'));
				}
				
		if (source.getValue("unit") != null) {
					 target.setValueSafely('unit.value', source.getValue('unit'));
				}		

}function mapFHIR_RangeToRange(source,target)
{

if (source.getValue('value') != null) {
target.setValue('value', source.getValue('value').getValue('value'));
}

if (source.getValue('unit') != null) {
target.setValue('unit', source.getValue('unit').getValue('value'));
}


}function mapRangeToFHIR_Range(source,target)
{

if (source.getValue('low') != null) {
	target.setValueSafely('low.value', source.getValue('low').getValue('value'));
	target.setValueSafely('low.unit', source.getValue('low').getValue('unit'));
}

if (source.getValue('high') != null) {
	target.setValueSafely('low.value', source.getValue('high').getValue('value'));
	target.setValueSafely('low.unit', source.getValue('high').getValue('unit'));
}




}function mapFHIR_RatioToCodedElement(source,target) { 
}
 function mapFHIR_RatioToString(source,target) { 
}
 function mapStringToFHIR_Ratio(source,target) { 
}
 function mapFHIR_ReferenceToInstanceIdentifier(s,t) { 
    if (s.getValue("reference") != null) {
        var sc =  s.getValue("reference");
        if (sc.getValue("value") != null) {
            t.setValue("extension", sc.getValue("value"));
        }
    }
}
 function mapInstanceIdentifierToFHIR_Reference(source, target, properties, conversionrule) {
	var fhirResource = org.mdmi.core.engine.javascript.Utils.getSemanticProperyQualifier(conversionrule, 'fhirResource:');
	//target.setValueSafely('type.value', fhirResource);
	if(fhirResource != null){
		fhirResource = fhirResource + "/";
	}

	 if(source.getValue('assigningAuthorityName') != null) {
    	            target.setValueSafely('display.value', source.getValue('assigningAuthorityName'));
    }	

    if (properties.get('REFERENCEURL') != null) {
        if (source.getValue('extension') != null) {
            target.setValueSafely('reference.value', properties.get('REFERENCEURL') + fhirResource + source.getValue('extension'));
        }
        if (source.getValue('root') != null) {
            target.setValueSafely('reference.value', properties.get('REFERENCEURL') + fhirResource + source.getValue('root'));
        }
    } else {
        if (source.getValue('extension') != null && source.getValue('root') != null) {
            target.setValueSafely('reference.value', fhirResource + source.getValue('root') + '::' + source.getValue('extension'));
        } else {
            if (source.getValue('extension') != null) {
                target.setValueSafely('reference.value', fhirResource + source.getValue('extension'));
            }
            if (source.getValue('root') != null) {
                target.setValueSafely('reference.value', fhirResource + source.getValue('root'));
            }
        }
    }

}

function mapInstanceIdentifierToFHIR_OrganizationReference(source, target, properties, conversionrule) {
	var fhirResource = "Organization/";

	 if(source.getValue('assigningAuthorityName') != null) {
    	            target.setValueSafely('display.value', source.getValue('assigningAuthorityName'));
    }	

    if (properties.get('REFERENCEURL') != null) {
        if (source.getValue('extension') != null) {
            target.setValueSafely('reference.value', properties.get('REFERENCEURL') + fhirResource + source.getValue('extension'));
        }
        if (source.getValue('root') != null) {
            target.setValueSafely('reference.value', properties.get('REFERENCEURL') + fhirResource + source.getValue('root'));
        }
    } else {
        if (source.getValue('extension') != null && source.getValue('root') != null) {
            target.setValueSafely('reference.value', fhirResource + source.getValue('root') + '::' + source.getValue('extension'));
        } else {
            if (source.getValue('extension') != null) {
                target.setValueSafely('reference.value', fhirResource + source.getValue('extension'));
            }
            if (source.getValue('root') != null) {
                target.setValueSafely('reference.value', fhirResource + source.getValue('root'));
            }
        }
    }

}


function mapInstanceIdentifierToFHIR_ProvenanceAgentReference(source, target, properties, conversionrule) {
	
	 if(source.getValue('assigningAuthorityName') != null) {
    	  target.setValueSafely('display.value', source.getValue('assigningAuthorityName'));
    }	
	var str = source.getValue('root');
    if (source.getValue('root') != null && str.startsWith('Organization')) {	
		target.setValueSafely('reference.value', str);
    }else{
      	mapInstanceIdentifierToFHIR_Reference(source, target, properties, conversionrule);     
    } 

}function mapFHIR_SimpleQuantityToPhysicalQuantity(source,target)
{
if (source.getValue('value') != null) {
target.setValue('value', source.getValue('value').getValue('value'));
}

if (source.getValue('unit') != null) {
target.setValue('unit', source.getValue('unit').getValue('value'));
}
}function mapPhysicalQuantityToFHIR_SimpleQuantity(source,target) {
 	if (source.getValue("value") != null) {
					 target.setValueSafely('value.value', source.getValue('value'));
				}
				
		if (source.getValue("unit") != null) {
					 target.setValueSafely('unit.value', source.getValue('unit'));
				}		
}function mapFHIR_dateToString(source,target) {

		if (source.getValue('value') != null) {
	target.setValue( org.mdmi.core.engine.javascript.Utils
			.FormatDate("yyyy-MM-dd", "yyyy-MM-dd",
					source.getValue('value')));
	}

}function mapStringToFHIR_date(source, target) {
	target.setValue('value', org.mdmi.core.engine.javascript.Utils.FormatDate(
			"yyyy-MM-dd'T'hh:mm:ss+zzzz", "yyyy-MM-dd", source.getValue()));
}
function mapFHIR_fhirstringToDateTime(source,target) {
if (source.getValue('value') != null) {
	target.setValue('value', org.mdmi.core.engine.javascript.Utils.FormatDate(
			"yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd'T'hh:mm:ss", source.getValue('value')));
}
}function mapFHIR_fhirstringToString(source,target) {
if (source.getValue('value') != null) {
	target.setValue(source.getValue('value'));
}
}function mapStringToFHIR_fhirstring(source,target) {
	target.setValueSafely('value',source.getValue());
}function mapFHIR_idToInstanceIdentifier(source, target) {

	if (source.getValue('value') != null) {
		target.setValue('root', source.getValue("value"));
	}
}function mapInstanceIdentifierToFHIR_id(source,target) {
	
		 if(source.getValue('extension') != null && source.getValue('root') != null )  {
    		 target.setValueSafely('value', source.getValue('root') + '::' + source.getValue('extension'));
    	 } else {
    	      if(source.getValue('extension') != null) {
    	            target.setValueSafely('value', source.getValue('extension'));
    	        }

    	        if(source.getValue('root') != null) {
    	            target.setValueSafely('value', source.getValue('root'));
    	        }  	

    	 }

}
 function mapFHIR_integerToInteger(source, target) {
	if (source.getValue('value') != null) {
		target.setValue(source.getValue('value'));
	}
}function mapIntegerToFHIR_integer(source,target)
{
 target.setValue('value', source.getValue());
}
function mapFHIR_stringToInstanceIdentifier(source, target) {

    
    if(source.getValue('value') != null) {
        target.setValue('extension', source.getValue('value').getValue("value"));
    }
    
}function mapInstanceIdentifierToFHIR_string(source,target) { 
}
 function mapFHIR_stringToString(source,target) { 
}
 function mapStringToFHIR_string(source,target) { 
}
 function mapGuardianBusinessPhoneContactTelecomToFHIR_ContactPoint(source,target) {
target.setValueSafely('use.value', 'work');
target.setValueSafely('system.value', 'phone');
target.setValueSafely('value.value', source.getValue('value'));
}function mapGuardianCellPhoneContactTelecomToFHIR_ContactPoint(source,target) {
target.setValueSafely('use.value', 'mobile');
target.setValueSafely('system.value', 'phone');
target.setValueSafely('value.value', source.getValue('value'));
}function mapGuardianHomePhoneContactTelecomToFHIR_ContactPoint(source,target) {
target.setValueSafely('use.value', 'home');
target.setValueSafely('system.value', 'phone');
target.setValueSafely('value.value', source.getValue('value'));
}function mapPatientPhoneContactTelecomToFHIR_ContactPoint(source,target) {
	
    if(source.getValue('use').startsWith('home'))
    {
        target.setValue('use', 'HP');
    }
    else
    if(source.getValue('use').startsWith('work'))
    {
        target.setValue('use', 'WP');
    }
    else
    if(source.getValue('use').startsWith('mobile'))
    {
        target.setValue('use', 'MC');
    }
    else
    {
        target.setValue('use', 'home');
    }
	 
	target.setValueSafely('system.value', 'phone');
	target.setValueSafely('value.value', source.getValue('value'));
}

 function mapPersonNameToFHIR_Reference(source,target) {
 
	 var personName = '';
	 
	 var prefixSource = source.getXValue('prefix');
	 
	 print('aaaaaaaa');
	
//	 var prefixTarget = target.getXValue('prefix');

		for (i = 0; i < prefixSource.getValues().size(); i++) {
			personName += ' ' + prefixSource.getValue(i);
//			prefixTarget.addValue(prefixSource.getValue(i));
		}
		 print('aaaaaaaa');
		var givenSource = source.getXValue('given');
//		var givenTarget = target.getXValue('given');

		for (i = 0; i < givenSource.getValues().size(); i++) {
			personName += ' ' + givenSource.getValue(i);
//			givenTarget.addValue(givenSource.getValue(i));
		}
		 print('aaaaaaaa');
		var familySource = source.getXValue('family');
//		var familyTarget = target.getXValue('family');

		for (i = 0; i < familySource.getValues().size(); i++) {
			personName += ' ' + familySource.getValue(i);
//			familyTarget.addValue(familySource.getValue(i));
		}
		 print('aaaaaaaa');
		 target.setValueSafely('display.value', personName);
	 
} function mapStringToFHIR_Reference(source,target) {
 	 target.setValueSafely('display.value', source.getValue());	 
 }
function mapStringToFHIR_dateTime(source, target) {
	target.setValue('value', org.mdmi.core.engine.javascript.Utils.FormatDate(
			"yyyy-mm-dd hh:mm:ss", "yyyy-MM-dd'T'hh:mm:ss", source.getValue()));
}
function mapDateTimeToFHIR_time(source,target) {}function mapFHIR_timeToDateTime(source,target) {}function mapStringToContainer(source,target) {}function mapContainerToString(source,target) {}function mapStringToFHIR_uri(source,target) {}function mapFHIR_uriToString(source,target) {}function mapPersonNameToFHIR_fhirstring(source,target) {
	
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

		target.setValueSafely('value',personName);
}function mapFHIR_fhirstringToPersonName(source,target) {}function mapStringToFHIR_Narrative(source,target) {
	//print(source);
	target.setValueSafely('div.value' , source.getValue());
}function mapFHIR_NarrativeToString(source,target) {}function mapInstanceIdentifierToFHIR_fhirstring(source,target) {
	if(source.getValue('root') != null) {
		var sys = source.getValue('root');
		var regex = /^([0-2])((\.0)|(\.[1-9][0-9]*))*$/gm;
		
		if (regex.test(sys)){
			sys = "urn:oid:" + sys;
	    }
		target.setValueSafely('value', sys);
    }
}function mapFHIR_fhirstringToInstanceIdentifier(source,target) {}function mapContainerToFHIR_Reference(source,target) {}function mapFHIR_ReferenceToContainer(source,target) {}function mapUnitToFHIR_Duration(source,target) {
	target.setValueSafely('value.value', source.getValue());
}function mapFHIR_DurationToUnit(source,target) {}function mapStringToFHIR_url(source,target) {
	target.setValue("value", source.getValue());
}function mapFHIR_urlToString(source,target) {}function mapPeriodToFHIR_Period(source,target) {}function mapFHIR_PeriodToPeriod(source,target) {}function mapDateTimeToFHIR_Period(source,target) {}function mapFHIR_PeriodToDateTime(source,target) {}function mapDecimalToFHIR_decimal(source,target) {
	if(source.getValue() != null){
		target.setValueSafely('value', source.getvalue());
	}
}function mapFHIR_decimalToDecimal(source,target) {}function mapContainerToFHIR_dateTime(source,target) {}function mapFHIR_dateTimeToContainer(source,target) {}function mapIntegerToFHIR_positiveInt(source,target) {
	if(source.getValue()){
		target.setValue('value' , source.getValue());
	}
}function mapFHIR_positiveIntToInteger(source,target) {}function mapRatioToFHIR_Ratio(source,target) {}function mapFHIR_RatioToRatio(source,target) {}function mapStringToFHIR_markdown(source,target) {
	target.setValue('value', source.getValue());
}function mapFHIR_markdownToString(source,target) {}function mapTimePeriodToFHIR_id(source,target) {}function mapFHIR_idToTimePeriod(source,target) {}