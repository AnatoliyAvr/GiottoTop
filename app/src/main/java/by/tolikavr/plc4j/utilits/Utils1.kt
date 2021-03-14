package by.tolikavr.plc4j

import android.text.InputFilter


fun filters1(): Array<InputFilter?> {
  val filters = arrayOfNulls<InputFilter>(1)
  filters[0] = InputFilter { source, start, end, dest, dstart, dend ->
    if (end > start) {
      val destTxt = dest.toString()
      val resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend)
      if (!resultingTxt.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?".toRegex())) {
        return@InputFilter ""
      } else {
        val splits = resultingTxt.split("\\.").toTypedArray()
        for (i in splits.indices) {
          if (Integer.valueOf(splits[i]) > 255) {
            return@InputFilter ""
          }
        }
      }
    }
    null
  }
  return filters
}