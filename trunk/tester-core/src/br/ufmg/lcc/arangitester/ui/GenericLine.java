package br.ufmg.lcc.arangitester.ui;

import br.ufmg.lcc.arangitester.annotations.RequestConfig;
import br.ufmg.lcc.arangitester.annotations.Ui;

public class GenericLine extends UiSimpleLine{

	@Ui(desc = "Delete #{index}", id = "checkDelete")
	private UiCheckBox checkDelete;

	@Ui(desc = "View #{index}", id = "viewImg")
	private UiImage view;

	@Ui(desc = "Modify #{index}", id = "modifyImg")
	private UiImage modify;

	@Ui(desc = "Save #{index}", id = "saveImg")
	private UiImage save;

	@RequestConfig(window=IRequest.Window.CLOSE)
	@Ui(desc = "Select #{index}", id = "selectImg")
	private UiImage select;
	
	@RequestConfig(confirmation=IRequest.Confirmation.OK)
	@Ui(desc = "Cancelar  #{index}", id = "cancelImg")
	public UiImage cancel;
	
	public UiCheckBox getCheckDelete() {
		return checkDelete;
	}

	public void setCheckDelete(UiCheckBox checkDelete) {
		this.checkDelete = checkDelete;
	}

	public UiImage getView() {
		return view;
	}

	public void setView(UiImage view) {
		this.view = view;
	}

	public UiImage getModify() {
		return modify;
	}

	public void setModify(UiImage modify) {
		this.modify = modify;
	}

	public UiImage getSave() {
		return save;
	}

	public void setSave(UiImage save) {
		this.save = save;
	}

	public UiImage getSelect() {
		return select;
	}

	public void setSelect(UiImage select) {
		this.select = select;
	}

	public UiImage getCancel() {
		return cancel;
	}

	public void setCancel(UiImage cancel) {
		this.cancel = cancel;
	}
}
