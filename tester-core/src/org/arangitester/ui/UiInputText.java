/*
 * Copyright 2000 Universidade Federal de Minas Gerais.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arangitester.ui;

import org.apache.commons.lang.StringUtils;
import org.arangitester.Context;
import org.arangitester.annotations.Date;
import org.arangitester.annotations.Length;
import org.arangitester.annotations.Logger;
import org.arangitester.annotations.Number;
import org.arangitester.exceptions.ArangiTesterException;
import org.arangitester.exceptions.EnvException;
import org.arangitester.exceptions.WrongValueException;
import org.arangitester.log.IResult;
import org.arangitester.ui.actions.IUiClickable;
import org.arangitester.util.ArangiTesterStringUtils;


import com.thoughtworks.selenium.SeleniumException;

public class UiInputText extends UiComponent {

	private IResult	log	= Context.getInstance().getResult();

	@Logger("Preenchendo ${this.componentDesc}: ${arg0}")
	public void type(String text) {
		waitElement(getComponentLocator());
		setPreviewslyActionValue(text);
		getSel().focus(getComponentLocator());
		getSel().type(getComponentLocator(), text);
		getSel().fireEvent(getComponentLocator(), "blur");
		getSel().fireEvent(getComponentLocator(), "change");
	}

	@Override
	public void verifyPreviewslyAction() {
		String expectedText = (String) getPreviewslyActionValue();
		if (expectedText != null)
			verifyValue(expectedText);
	}

	@Logger("Verificando: ${this.componentDesc}: '${arg0}'")
	public void verifyValue(String expectedText) {
		waitElement(getComponentLocator());
		String realValue = getSel().getValue(getComponentLocator()).trim();
		if (!realValue.equals(expectedText.trim())) {
			throw new WrongValueException("Valor esperado: " + expectedText + ". Valor atual: '" + realValue + "'");
		}
	}

	@Logger("Verificando valor positivo: ${this.componentDesc}: '${arg0}'")
	public void verifyPositiveValue() {
		waitElement(getComponentLocator());
		String realValue = getSel().getValue(getComponentLocator()).trim();
		int value = Integer.parseInt(realValue);
		if (value < 0) {
			throw new WrongValueException("'Valor positivo esperado, mas não presente. Valor atual: " + value + "'");
		}
	}

	public String getValue() {
		waitElement(getComponentLocator());
		return getSel().getValue(getComponentLocator());
	}

	@Logger("Validando tamanho máximo: ${this.componentDesc} ${arg2.max}")
	public void validateLength(UiButton saveButton, UiDiv divMessage, Length lccLength) {
		String previousValue = getSel().getValue(getComponentLocator());

		try {
			String bigText = StringUtils.repeat("a", lccLength.max());
			getSel().type(getComponentLocator(), bigText);
			getSel().typeKeys(getComponentLocator(), "a");

			/*
			 * Pre condicao: o campo mensagem deve obrigatoriamente não ser vazio.
			 * *** Provisoriamente: quando o campo checkSize for true, o campo mensagem pode ser vazio,
			 * tendo em vista que o usuario não conseguirá preencher o campo com mais caracteres
			 * que o permitido, sem alterar a requisição da página. Quando conseguirmos fazer isto,
			 * testaremos a mensagem independente do checkSize.
			 */
			if (!StringUtils.isNotBlank(lccLength.msg()) && lccLength.checkSize() == false)
				throw new EnvException("Validação de tamanho do campo - Não foi informada uma mesagem a ser verificada e checkSize é false");

			// Verifica o maxLenght do campo
			if (lccLength.checkSize()) {
				int fieldLength = getSel().getValue(getComponentLocator()).length();
				if (fieldLength > lccLength.max()) {
					throw new WrongValueException("Validação de tamanho não esta correto. O campo aceita " +
							"mais de " + lccLength.max() + " caracteres.");
				} else if (fieldLength < lccLength.max()) {
					throw new WrongValueException("Validação de tamanho não esta correto. O campo aceita " +
							"no máximo " + fieldLength + " caracteres, quando deveria aceitar " + lccLength.max());
				}

				/* 
				 * *** Provisioramente, não será necessáro testar a mensagem quando checkSize for true,
				 * ou seja, quando o campo não aceitar que o usuário envie mais caracteres que o permitido.
				 * Quando conseguirmos alterar a requisição no teste, enviando mais caracteres que o permitido
				 * pelo campo, aí sim testaremos a mensagem de erro, que é um reflexo de que a validação está sendo
				 * feita no programa, e não apenas na interface.
				 * Sendo assim, o método acaba aqui.
				 */
				return;
			}

			// Verifica a validação de tamanho, feita na aplicação.
			saveButton.clickWithOutLogger();
			divMessage.verifyTextInsideWithoutLog(lccLength.msg());

		} catch (WrongValueException e) {
			throw e;
		} finally {
			getSel().type(getComponentLocator(), previousValue);
		}

	}

	@Logger("Validação de Número: ${this.componentDesc} min: ${arg2.min} max: ${arg2.max}")
	public void validateNumber(UiButton saveButton, UiDiv divMessage, Number lccNumber) {
		/* Stores previous value of the field which is being validated */
		String previousValue = getSel().getValue(getComponentLocator());

		try {

			/* Validates wrong number format. */
			if (StringUtils.isNotBlank(lccNumber.msgWrongValue())) {
				log.addInfo("verificando formato numérico.");
				getSel().type(getComponentLocator(), "x");
				saveButton.clickWithOutLogger();
				if (!divMessage.isTextInsidePresentWithOutWait(lccNumber.msgWrongValue())) {
					throw new WrongValueException("Validação de formato de número inválida. Mensagem NÃO presente: " + lccNumber.msgWrongValue());
				}
			}

			if (lccNumber.min() > lccNumber.max()) {
				throw new WrongValueException("Validação de valores mínimo e máximo não foi executada. O valor máximo deve " +
													"ser maior do que o mínimo. Corrija as anotações na classe de teste.");
			}

			String maxLengthStr;
			try {
				maxLengthStr = getSel().getAttribute(getComponentLocator() + "@maxlength");
			} catch (SeleniumException e) {
				maxLengthStr = "2";
			}
			int maxLength = Integer.valueOf(maxLengthStr);

			/* Validates minimum values. */
			if (StringUtils.isNotBlank(lccNumber.msgMinValue())) {
				log.addInfo("verificando valor mínimo para campo numérico.");
				String minValue = null;
				if ((lccNumber.min() - 1 < 0) && (maxLength > 1)) {
					minValue = String.valueOf(lccNumber.min() - 1);
				} else if ((maxLength > 0) && (lccNumber.min() > 0)) {
					minValue = String.valueOf(lccNumber.min() - 1);
				}
				getSel().type(getComponentLocator(), minValue);
				if (getSel().getValue(getComponentLocator()).equals(String.valueOf(minValue))) {
					saveButton.clickWithOutLogger();
					if (!divMessage.isTextInsidePresentWithOutWait(lccNumber.msgMinValue())) {
						throw new WrongValueException("Validação de valor mínimo para número inválida. Mensagem NÃO presente: " + lccNumber.msgMinValue());
					}
				}
			}

			/* Validates maximum values. */
			if (StringUtils.isNotBlank(lccNumber.msgMaxValue())) {
				log.addInfo("verificando valor máximo para campo numérico.");
				String maxValue = null;
				try {
					maxValue = ArangiTesterStringUtils.fill('9', maxLength);
				} catch (ArangiTesterException e) {
					maxValue = String.valueOf(Long.MAX_VALUE);
				}

				if (lccNumber.max() > Long.valueOf(maxValue)) {
					throw new WrongValueException("Valor máximo tem número de caracteres maior do que o máximo permitido. Corrigir erro na anotação" +
														" da classe de mapeamento.");
				} else if (lccNumber.max() < Long.valueOf(maxValue)) {
					maxValue = String.valueOf(lccNumber.max() + 1);
					getSel().type(getComponentLocator(), maxValue);
					if (getSel().getValue(getComponentLocator()).equals(String.valueOf(maxValue))) { // Try Javascript validation
						// finally try server
						saveButton.clickWithOutLogger();
						if (!divMessage.isTextInsidePresentWithOutWait(lccNumber.msgMaxValue())) {
							throw new WrongValueException("Validação de valor máximo de número inválida. Mensagem NÃO presente: " + lccNumber.msgMaxValue());
						}
					}
				}
			}

		} catch (WrongValueException e) {
			throw e;
		} finally {
			getSel().type(getComponentLocator(), previousValue);
		}
	}

	@Logger("Validando Data: ${this.componentDesc} min: ${arg2.min} max: ${arg2.max}")
	public void validateDateFormat(UiButton saveButton, UiDiv divMessage, Date lccDate) {
		getSel().type(getComponentLocator(), "t");
		saveButton.clickWithOutLogger();
		if (!divMessage.isTextInsidePresentWithOutWait(lccDate.msg())) {
			throw new WrongValueException("Validação de data inválida. Mensagem NÃO presente: " + lccDate.msg());
		}

		if (lccDate.max() == lccDate.min())
			return;

		String maxLengthStr;
		try {
			maxLengthStr = getSel().getAttribute(getComponentLocator() + "@maxlength");
		} catch (SeleniumException e) {
			maxLengthStr = "2";
		}
		int maxLength = Integer.valueOf(maxLengthStr);
		if (!(maxLength == 1 && lccDate.min() - 1 < 0)) { // Min Validation needed
			String minValue = String.valueOf(lccDate.min() - 1);
			getSel().type(getComponentLocator(), minValue);
			if (getSel().getValue(getComponentLocator()).equals(String.valueOf(minValue))) {
				saveButton.clickWithOutLogger();
				if (lccDate.msg().length == 0 || !divMessage.isTextInsidePresentWithOutWait(lccDate.msg())) {
					throw new WrongValueException("Validação de data inválida. Mensagem NÃO presente: " + lccDate.msg());
				}
			}
		}

		String maxValue = String.valueOf(lccDate.max() + 1);
		getSel().type(getComponentLocator(), maxValue);

		if (getSel().getValue(getComponentLocator()).equals(String.valueOf(maxValue))) { // Try Javascript validation
			// finally try server
			saveButton.clickWithOutLogger();
			if (lccDate.msg().length == 0 || !divMessage.isTextInsidePresentWithOutWait(lccDate.msg())) {
				throw new WrongValueException("Validação de data inválida. Mensagem NÃO presente: " + lccDate.msg());
			}
		}

		String invalidDate = "01/00/2000";
		getSel().type(getComponentLocator(), invalidDate);
		if (getSel().getValue(getComponentLocator()).equals(String.valueOf(invalidDate))) { // Try Javascript validation
			// finally try server
			saveButton.clickWithOutLogger();
			if (lccDate.msg().length == 0 || !divMessage.isTextInsidePresentWithOutWait(lccDate.msg())) {
				throw new WrongValueException("Validação de data inválida. Mensagem NÃO presente: " + lccDate.msg());
			}
		}
		invalidDate = "01/15/2000";
		getSel().type(getComponentLocator(), invalidDate);
		if (getSel().getValue(getComponentLocator()).equals(String.valueOf(invalidDate))) { // Try Javascript validation
			// finally try server
			saveButton.clickWithOutLogger();
			if (lccDate.msg().length == 0 || !divMessage.isTextInsidePresentWithOutWait(lccDate.msg())) {
				throw new WrongValueException("Validação de data inválida. Mensagem NÃO presente: " + lccDate.msg());
			}
		}

		invalidDate = "00/01/2000";
		getSel().type(getComponentLocator(), invalidDate);
		if (getSel().getValue(getComponentLocator()).equals(String.valueOf(invalidDate))) { // Try Javascript validation
			// finally try server
			saveButton.clickWithOutLogger();
			if (lccDate.msg().length == 0 || !divMessage.isTextInsidePresentWithOutWait(lccDate.msg())) {
				throw new WrongValueException("Validação de data inválida. Mensagem NÃO presente: " + lccDate.msg());
			}
		}
		invalidDate = "49/01/2000";
		getSel().type(getComponentLocator(), invalidDate);
		if (getSel().getValue(getComponentLocator()).equals(String.valueOf(invalidDate))) { // Try Javascript validation
			// finally try server
			saveButton.clickWithOutLogger();
			if (lccDate.msg().length == 0 || !divMessage.isTextInsidePresentWithOutWait(lccDate.msg())) {
				throw new WrongValueException("Validação de data inválida. Mensagem NÃO presente: " + lccDate.msg());
			}
		}

		invalidDate = "09/01/-198";
		getSel().type(getComponentLocator(), invalidDate);
		if (getSel().getValue(getComponentLocator()).equals(String.valueOf(invalidDate))) { // Try Javascript validation
			// finally try server
			saveButton.clickWithOutLogger();
			if (lccDate.msg().length == 0 || !divMessage.isTextInsidePresentWithOutWait(lccDate.msg())) {
				throw new WrongValueException("Validação de data inválida. Mensagem NÃO presente: " + lccDate.msg());
			}
		}
	}

	@Override
	public void validate(IUiClickable saveButton, IUiComponent divMessage) {

		Number numberValidation = getConfig(Number.class);

		try {
			if (numberValidation != null) {
				validateNumber((UiButton) saveButton, (UiDiv) divMessage, numberValidation);
			}

			Length lenghtValidation = getConfig(Length.class);
			if (lenghtValidation != null) {
				validateLength((UiButton) saveButton, (UiDiv) divMessage, lenghtValidation);
			}

			Date dateValidation = getConfig(Date.class);
			if (dateValidation != null) {
				validateDateFormat((UiButton) saveButton, (UiDiv) divMessage, dateValidation);
			}
		} catch (ArangiTesterException e) {
			log.addError(e.getMessage());
		}
	}

	@Override
	public String getComponentTag() {
		return "input";
	}
}