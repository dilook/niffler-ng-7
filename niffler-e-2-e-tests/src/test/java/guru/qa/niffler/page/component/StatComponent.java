package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class StatComponent extends BaseComponent<StatComponent> {

  public StatComponent() {
    super($("#stat"));
  }

  private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
  private final SelenideElement diagram = self.$("canvas[role='img']");


  public File getDiagramScreenshot() {
    diagram.shouldBe(visible);
    Selenide.sleep(2000); //coz of canvas animation
    return diagram.screenshot();
  }


  @Step("Check spending diagram")
  public StatComponent checkSpendingDiagram(BufferedImage expectedImage) throws IOException {
    BufferedImage actualImage = ImageIO.read(getDiagramScreenshot());
    assertFalse(new ScreenDiffResult(
            actualImage,
            expectedImage
    ));
    return this;
  }

  public StatComponent checkCategories(String... categoryNames) {
    bubbles.shouldHave(textsInAnyOrder(categoryNames));
    return this;
  }
}
