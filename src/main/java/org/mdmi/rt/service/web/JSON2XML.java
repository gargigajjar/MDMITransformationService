/*******************************************************************************
 * Copyright (c) 2022 seanmuir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     seanmuir - initial API and implementation
 *
 *******************************************************************************/
package org.mdmi.rt.service.web;

import org.json.JSONObject;
import org.json.XML;
import org.mdmi.MessageModel;
import org.mdmi.core.MdmiMessage;
import org.mdmi.core.engine.preprocessors.IPreProcessor;

/**
 * @author seanmuir
 *
 */
public class JSON2XML implements IPreProcessor {

	/*
	 * String container = "QSOFA\n" + "S_NEWS\n" + "PresenceOfAbdominalInjury\n" + "MISTInjury\n" + "POCUSEntry\n" +
	 * "ESI1_SBP90\n" + "IntubationMedicationEntry\n" + "PainEntry\n" + "IVEntry\n" + "AndjunctRRSaO2Entry\n" +
	 * "MechSupDeviceEntry3C\n" + "AirwayInterventionsEntry\n" + "RenalReplacementEntry\n" + "S_MT\n" +
	 * "PresenceOfChestInjury\n" + "AndjunctRRSaO2Entry3C\n" + "VentilationEntry\n" + "C_TA_I_Hypotension\n" +
	 * "CirculationInterventionsSceneEntry\n" + "C_TA_I_Intubated\n" + "FluidInputEntry\n" + "O2Entry\n" +
	 * "MESS_Shock\n" + "FluidsHighCrystalloidsTrigger\n" + "BloodTypeEntry\n" + "CovidCaseDayEntry\n" +
	 * "SceneAirwayInterventionsEntry\n" +
	 * "S_ULTIMAO2Entry MESS_Shock FluidsHighCrystalloidsTrigger BloodTypeEntry SceneAirwayInterventionsEntry PatientLocationEntry QSOFA MoveToAdjuncts S_NEWS T6CaseUniqueId PresenceOfAbdominalInjury CensusAlerts S_ULTIMA"
	 * ;
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see org.mdmi.core.engine.preprocessors.IPreProcessor#canProcess(org.mdmi.MessageModel)
	 */
	@Override
	public boolean canProcess(MessageModel messageModel) {
		if ("PERATON".equals(messageModel.getGroup().getName())) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mdmi.core.engine.preprocessors.IPreProcessor#getName()
	 */
	@Override
	public String getName() {
		return "JSON2XML";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mdmi.cor.engine.preprocessors.IPreProcessor#processMessage(org.mdmi.MessageModel, org.mdmi.core.MdmiMessage)
	 */
	@Override
	public void processMessage(MessageModel arg0, MdmiMessage mdmiMessage) {

		JSONObject json = new JSONObject(mdmiMessage.getDataAsString().replace("null", "\"\"").replace("N/A", "NA"));

		/*
		 * JSONArray fields = json.getJSONArray("fields");
		 *
		 * ArrayList<JSONObject> tobecloned = new ArrayList<JSONObject>();
		 *
		 * for (int fctr = 0; fctr < fields.length(); fctr++) {
		 * JSONObject field = (JSONObject) fields.get(fctr);
		 *
		 * if (field.has("name")) {
		 * String fieldName = field.getString("name");
		 * if (container.contains(fieldName)) {
		 * field.put("container", "T6Observation");
		 * }
		 * }
		 *
		 * if (field.has("value")) {
		 *
		 * Object value = field.get("value");
		 *
		 * if (value instanceof JSONArray) {
		 *
		 * JSONArray values = (JSONArray) value;
		 *
		 * for (int vctr = 1; vctr < values.length(); vctr++) {
		 * JSONObject clone = new JSONObject(field.toString());
		 * clone.remove("value");
		 * clone.put("value", values.get(vctr));
		 * tobecloned.add(clone);
		 * }
		 * String value0 = values.getString(0);
		 * field.remove("value");
		 * field.put("value", value0);
		 * }
		 * }
		 *
		 * }
		 * for (JSONObject cloned : tobecloned) {
		 * fields.put(cloned);
		 * }
		 */

		System.err.println(json.toString());
		mdmiMessage.setData("<root>" + XML.toString(json) + "</root>");
	}

}
