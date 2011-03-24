package @package@;

import br.ufmg.lcc.arangitester.annotations.Login;
import br.ufmg.lcc.arangitester.annotations.Test;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;

@Login(user = "", password = "")
public class FirstFunctionalTest {
   
    @Test(order = 1, value = "Description of test.")
    public void inicio() {
        FirstPage firstPage = UiComponentFactory.getInstance(FirstPage.class);
        firstPage.invoke();
        firstPage.select1.select("Item1");
        firstPage.input1.type ("writing");
    }
}
