/*******************************************************************************
 * Copyright (c) 2022 MDIX, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompany this distribution and is available at 
 * https\://www.apache.org/licenses/LICENSE-2.0.
 *
 * Contributors:
 *     seanmuir - initial API and implementation
 *
 *******************************************************************************/
package org.mdmi.rt.service.web;

import java.util.List;

import org.mdmi.MessageModel;
import org.mdmi.SemanticElementRelationship;
import org.mdmi.core.ElementValueSet;
import org.mdmi.core.IElementValue;
import org.mdmi.core.engine.semanticprocessors.ISemanticProcessor;

/**
 * @author seanmuir
 *
 */
public class ProcessRelationships implements ISemanticProcessor {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mdmi.core.engine.semanticprocessors.ISemanticProcessor#getName()
	 */
	@Override
	public String getName() {
		return "ProcessRelationships";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mdmi.core.engine.semanticprocessors.ISemanticProcessor#canProcess(org.mdmi.MessageModel)
	 */
	@Override
	public boolean canProcess(MessageModel messageModel) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mdmi.core.engine.semanticprocessors.ISemanticProcessor#processSemanticModel(org.mdmi.core.ElementValueSet)
	 */
	@Override
	public void processSemanticModel(ElementValueSet semanticModel) {
		for (IElementValue element : semanticModel.getAllElementValues()) {
			if (!element.getSemanticElement().getRelationships().isEmpty()) {

				for (SemanticElementRelationship r : element.getSemanticElement().getRelationships()) {

					List<IElementValue> relatedElements = semanticModel.getElementValuesByName(
						r.getRelatedSemanticElement());

					for (IElementValue relatedElement : relatedElements) {
						for (IElementValue correctParent : relatedElement.getParent().getChildren()) {
							if (correctParent.getName().equals(element.getName())) {
								relatedElement.setParent(correctParent);
								correctParent.addChild(relatedElement);

							}

						}

					}

				}

			}
		}

	}

}
