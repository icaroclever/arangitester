package @package@.login;

import br.ufmg.lcc.arangitester.annotations.Field;
import br.ufmg.lcc.arangitester.boot.BaseLoginController;
import br.ufmg.lcc.arangitester.exceptions.FatalException;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;

public class LoginController extends BaseLoginController {

    @Override
    protected void login(String user, String password, Field[] fields) throws FatalException {
        LoginPage loginPage = UiComponentFactory.getInstance(LoginPage.class);
        loginPage.username.type(user);
        loginPage.password.type(password);
        loginPage.btnLogin.click();
    }

}
