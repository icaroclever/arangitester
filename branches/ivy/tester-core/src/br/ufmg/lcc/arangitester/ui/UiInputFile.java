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
package br.ufmg.lcc.arangitester.ui;

import java.io.File;

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.exceptions.LccException;

/**
 * InputText com type file. Permite o upload de arquivos.
 * 
 * @author Lucas Gonçalves
 */
public class UiInputFile extends UiComponent {

	/**
	 * Adiciona um arquivo ao inputtext.
	 * 
	 * @param file
	 *            Arquivo que deseja fazer upload.
	 */
	public void attachFile(File file) {
		if (!file.exists()) {
			throw new LccException(String.format("O arquivo %s para upload não existe", file.getAbsolutePath()));
		}
		getSel().attachFile(super.getComponentLocator(), file.toURI().toString());
	}

	/**
	 * Adiciona um arquivo ao inputtext.
	 * 
	 * @param file
	 *            Arquivo que deseja fazer upload.
	 */
	@Logger(value="Anexando arquivo")
	public void attachFile(String file) {
		this.attachFile(new File(file));
	}
}
