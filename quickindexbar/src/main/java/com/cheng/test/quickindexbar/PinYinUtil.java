package com.cheng.test.quickindexbar;

import android.text.TextUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {

	public static String getPinYin(String chinese) {
		if (TextUtils.isEmpty(chinese)) {
			return null;
		}


		// 用来设置转换的拼音的大小写及声调

		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 设置转换的拼音为大写
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 设置转换的拼音为不带声调

		String pinYin = new String();
		char[] charArray = chinese.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			// 去掉空格
			if (Character.isSpace(charArray[i])) {
				continue;
			}
			// 只对汉字转换
			if (charArray[i] > 127) {
				// 可能为汉字
				try {
					// 由于多音字的存在，所以返回的是字符串数组
					String[] pinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(charArray[i], format);
					if (pinyinStringArray != null) {
						// 只取第一个
						pinYin += pinyinStringArray[0];
					}

				} catch (BadHanyuPinyinOutputFormatCombination e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// 说明转化失败，不是汉字，比如o(∩_∩)o等，则忽略，无需处理
				}
			} else {
				// 肯定不是汉字，应该是键盘能输入的字符，这些字符能排序，但不能获取拼音，所以直接拼接
				pinYin += charArray[i];
			}

		}
		return pinYin;
	}

}
