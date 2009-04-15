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
