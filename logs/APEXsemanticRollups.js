try {
 load("nashorn:mozilla_compat.js");
} 
catch (e) 
{ 
}
importPackage(org.mdmi.core.engine.javascript);
function ProcedureStatus_COMPUTED(value) {value.getXValue().addValue('code', 'completed');}function ObservationStatus_COMPUTED(value) {value.getXValue().addValue('code', 'final');}function fIRSTNAME123_to_PatientNameRollupRollUp(value,param1) {value.getXValue().addValue('given', param1 );}function lASTNAME_to_PatientNameRollupRollUp(value,param1) {value.getXValue().addValue('family', param1 );}function EncounterStatus_COMPUTED(value) {value.getXValue().addValue('code', 'finished');}