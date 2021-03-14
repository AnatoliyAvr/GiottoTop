package by.tolikavr.plc4j.utilits;

import android.text.InputFilter;
import android.widget.EditText;

public class Utils {

  public static InputFilter[] filters() {
    InputFilter[] filters = new InputFilter[1];
    filters[0] = (source, start, end, dest, dstart, dend) -> {
      if (end > start) {
        String destTxt = dest.toString();
        String resultingTxt = destTxt.substring(0, dstart)
            + source.subSequence(start, end)
            + destTxt.substring(dend);
        if (!resultingTxt
            .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
          return "";
        } else {
          String[] splits = resultingTxt.split("\\.");
          for (String split : splits) {
            if (Integer.parseInt(split) > 255) {
              return "";
            }
          }
        }
      }
      return null;
    };
    return filters;
  }
}
