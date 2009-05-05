package br.ufmg.lcc.arangitester.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define Xpath pattern to locate a line in a table. Follow variable can be used in xpath.
 * <ol>
 * <li>${tableXpath} - xpath defined in Ui annotation of table. If id or name is defined in table´s Ui
 * annotation it will transformed in xpath locator.</li>
 * <li>${tableId} - id defined in Ui annotation of table.</li>
 * <li>${tableName} - tableName name defined in Ui annotation of table.</li>
 * <li>${index} - Line index</li>
 * </ol>
 * Sample: <code>
 *  @Ui(locator = "xpath=//table[@class='tablePadrao']")
 *  public UiTable<Linha> result;
 *
 *  @Line(xpath = "/${tableXpath}/tbody/tr[${index} + 1]")
 *  public static class Linha extends UiSimpleLine {
 *       @Ui(locator = "//a[@class='btEditar']")
 *       public UiButton btnEditar;
 *  }
 * </code> then if involke <code>
 *  result.getLine(0).btnEditar.click();
 *  </code>
 *             the element clicked is //table[@class='tablePadrao']/tbody/tr[1]//a[@class='btEditar']
 * @author Lucas Gonçalves
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Line {
    /**
     * @return Xpath expression to locate each line. Ex. ${tableXpath}/tbody/tr[${index}].
     */
    String xpath() default "${tableXpath}/tbody/tr[${beginIndex}]";

    /**
     * @return Define de beginIndex on default xpath of line "${tableXpath}/tbody/tr[${beginIndex}]". 1 Based index.
     */
    int beginIndex() default 2;
}
