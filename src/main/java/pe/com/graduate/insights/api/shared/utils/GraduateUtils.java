package pe.com.graduate.insights.api.shared.utils;

import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GraduateUtils {

  private static final Random RANDOM = new Random();

  public String generateRandomSixDigitNumber() {
    return String.valueOf(
        ConstantsUtils.NUMBER_100000 + RANDOM.nextInt(ConstantsUtils.NUMBER_900000));
  }

  public String templateWithVariables(String template, String user, String code) {
    return template.replace(ConstantsUtils.USER_KEY, user).replace(ConstantsUtils.CODE_KEY, code);
  }
}
