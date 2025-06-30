package pe.com.graduate.insights.api.domain.utils;

import java.util.Random;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GraduateUtils {

  public String generateRandomSixDigitNumber() {
    Random random = new Random();
    return String.valueOf(
        ConstantsUtils.NUMBER_100000 + random.nextInt(ConstantsUtils.NUMBER_900000));
  }

  public String templateWithVariables(String template, String user, String code) {
    return template.replace(ConstantsUtils.USER_KEY, user).replace(ConstantsUtils.CODE_KEY, code);
  }
}
