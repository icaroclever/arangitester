package @package@.login;

import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.ui.UiButton;
import br.ufmg.lcc.arangitester.ui.UiInputText;
import br.ufmg.lcc.arangitester.ui.UiPage;

public class LoginPage extends UiPage {
    @Ui(id="username")
    public UiInputText username;
    
    @Ui(id="password")
    public UiInputText password;
    
    @Ui(id="btnLogin")
    public UiButton btnLogin;
}
