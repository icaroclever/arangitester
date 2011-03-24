package @package@;

import br.ufmg.lcc.arangitester.annotations.Page;
import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.ui.UiInputText;
import br.ufmg.lcc.arangitester.ui.UiPage;
import br.ufmg.lcc.arangitester.ui.UiSelect;

@Page(url = "/fisrttest.do")
public class FirstPage extends UiPage {

    @Ui(id = "input1")
    public UiInputText input1;

    @Ui(id = "select1")
    public UiSelect select1;

}
