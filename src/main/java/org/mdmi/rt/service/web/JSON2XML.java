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
	 * (non-Javadoc)
	 *
	 * @see org.mdmi.core.engine.preprocessors.IPreProcessor#canProcess(org.mdmi.MessageModel)
	 */
	@Override
	public boolean canProcess(MessageModel messageModel) {
		if ("TeeSix".equals(messageModel.getGroup().getName())) {
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
		JSONObject json = new JSONObject(mdmiMessage.getDataAsString());
		mdmiMessage.setData("<root>" + XML.toString(json) + "</root>");
	}

}
