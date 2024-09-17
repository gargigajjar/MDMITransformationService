/*******************************************************************************
 * Copyright (c) 2024 Owner.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Owner - initial API and implementation
 *
 *******************************************************************************/
package org.mdmi.rt.service.web;

import org.mdmi.SemanticElement;
import org.mdmi.core.ElementValueSet;
import org.mdmi.core.IElementValue;
import org.mdmi.core.engine.XValue;
import org.mdmi.core.engine.semanticprocessors.ConfigurableSemanticProcessor;

/**
 * @author Owner
 *
 */
public class Normalizer extends ConfigurableSemanticProcessor {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mdmi.core.engine.semanticprocessors.ISemanticProcessor#processSemanticModel(org.mdmi.core.ElementValueSet)
	 */
	@Override
	public void processSemanticModel(ElementValueSet elementSet) {
		// TODO Auto-generated method stub
		// List<IElementValue> parentValue = null;
		String refValue = "";
		String idValue = "";
		for (IElementValue semanticElement : elementSet.getAllElementValues()) {
			SemanticElement se = semanticElement.getSemanticElement();

			if (se.getRelationshipByName("NORMALIZE") != null) {
				if (se.getRelationshipByName("NORMALIZE").getRelatedSemanticElement() != null) {
					if (semanticElement.getChildren() != null) {
						for (IElementValue child : semanticElement.getChildren()) {

							if (child.getSemanticElement().getSyntaxNode() != null &&
									child.getSemanticElement().getSyntaxNode().getLocation().equals("reference")) {
								XValue xvalue = (XValue) child.getXValue();
								refValue = (String) xvalue.getValueByName("value");
								String[] temp = refValue.split("/");
								refValue = temp[temp.length - 1];
							}
							// System.err.println("child " + child.getName());
						}
					} else {
						XValue xvalue = (XValue) semanticElement.getXValue();
						refValue = (String) xvalue.getValueByName("value");
					}

					SemanticElement parent = se.getRelationshipByName("NORMALIZE").getRelatedSemanticElement();
					// System.err.println(se.getName() + " : inside resolve condition : " + parent.getName());

					for (IElementValue parentValue : elementSet.getElementValuesByName(parent)) {
						if (parent.getDatatype().getName().equals("Container")) {
							for (IElementValue id : parentValue.getChildren()) {
								if (id.getSemanticElement().getSyntaxNode() != null &&
										id.getSemanticElement().getSyntaxNode().getLocation().equals("id")) {
									XValue xvalue = (XValue) id.getXValue();
									idValue = (String) xvalue.getValueByName("value");
									break;
								}
							}
						}

						if (idValue.equals(refValue) && parentValue.getChildren() != null) {
							for (IElementValue child : parentValue.getChildren()) {
								semanticElement.addChild(child);

								// System.err.println("Children :: " + child.getName());
							}

						}
					}

				}
			}
		}

	}

}
