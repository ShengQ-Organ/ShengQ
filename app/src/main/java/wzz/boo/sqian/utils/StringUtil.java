package wzz.boo.sqian.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String getTime1(String time) {
        if (time != null && time.length() >= 16) {
            return time.substring(0, 16);
        }
        return time;
    }

    public static String getImgName(String url) {
        String imgName = "";
        if (url != null) {
            int start = url.lastIndexOf("/");
            imgName = url.substring(start + 1, url.length());
        }
        return imgName;
    }

    public static String getFolderPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        int index = path.lastIndexOf("/");
        if (index == -1) {
            return "";
        }
        return path.substring(0, index);
    }

    public static String getNameFromUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        int index = url.lastIndexOf("/");
        if (index == -1) {
            return null;
        }
        return url.substring(index + 1);
    }

    public static String formatTime(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) {
            return null;
        }
        Calendar now = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = dateFormat.parse(timeStr);
            Calendar pre = Calendar.getInstance();
            pre.setTime(date);
            if (now.get(Calendar.YEAR) == pre.get(Calendar.YEAR)) {
                if (now.get(Calendar.DAY_OF_YEAR) == pre.get(Calendar.DAY_OF_YEAR)) {
                    return "今天" + timeStr.substring(11, 16);
                } else {
                    return timeStr.substring(5, 16);
                }
            } else {
                return timeStr.substring(0, 16);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串空判断
     * <pre>is null or its length is 0 or it is made by space</pre>
     *
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return
     * true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 字符串非空判断
     * <pre>is not null or its length is not 0 or it is not made by space</pre>
     */
    public static boolean isNotBlank(String str) {
        return (str != null && str.trim().length() != 0);
    }

    /**
     * 手机号码正确性校验
     *
     * @return
     */
    public static boolean isMobileNO(String mobileNo) {
        if (isBlank(mobileNo) || mobileNo.length() != 11) {
            return false;
        } else {
            String telRegex = "1\\d{10}|14[57]\\d{8}|15[012356789]\\d{8}|18[01256789]\\d{8}|17[01678]\\d{8}";
            return mobileNo.matches(telRegex);
        }
    }

    private static final String TAG = "StringUtil";

    /**
     * 格式化未读待办任务数(大于99显示99+)
     *
     * @return
     */
    public static String formatUnreadJobCount(String jobCount) {
        if (StringUtil.isBlank(jobCount)) {
            return "";
        }
        try {
            int count = Integer.valueOf(jobCount);
            //0条未读消息，不显示
            if (count == 0) {
                return "";
            }
            //未读消息数小于100，直接显示
            if (count < 100) {
                return jobCount;
            }
            //未读消息数>=100，显示“99+”
            if (count >= 100) {
                return "99+";
            }
        } catch (Exception e) {
            Log.e(TAG, "未读消息数整型转换异常：" + e.getMessage());
        }
        return "";
    }

    /**
     * 身份证号判断
     *
     * @param idcardNO
     * @return
     */
    private static boolean isIdcardNO(String idcardNO) {
        if (StringUtil.isBlank(idcardNO)) {
            return false;
        }
        //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        return idNumPattern.matcher(idcardNO).matches();
    }

    /**
     * 定义为全局static字段，防止被多次创建（findbugs找到了）
     */
    private static String[] arrCh = new String[]{"1", "0", "X", "9", "8", "7",
            "6", "5", "4", "3", "2"};

    /**
     * 检测身份证是否合法
     *
     * @param idcardNO 身份证号
     * @return
     */
    public static boolean checkIdcard(String idcardNO) {
        idcardNO = idcardNO.toUpperCase();
        Pattern pattern = Pattern.compile("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)");
//        Pattern pattern = Pattern.compile("(^\\d{17}([0-9]|X)$)");
        Matcher matcher = pattern.matcher(idcardNO);
        if (!matcher.matches()) {
            System.out
                    .println("输入的身份证号长度不对，或者号码不符合规定！\n15位号码应全为数字，18位号码末位可以为数字或X。");
            return false;
        }
        int len;

        len = idcardNO.length();
        if (len == 15) {

//            String arr1 = sfz.substring(0, 6);
            String arr2 = idcardNO.substring(6, 8);
            String arr3 = idcardNO.substring(8, 10);
            String arr4 = idcardNO.substring(10, 12);
//            String arr5 = sfz.substring(12, 15);
            if (Integer.parseInt(arr3) > 12 || Integer.parseInt(arr4) > 31) {
                //月份或日期过长
                return false;
            }
            Date dtmBirth = new Date("19" + arr2 + "/" + arr3 + "/" + arr4);

            boolean bGoodDay;
            bGoodDay = (dtmBirth.getYear() == Integer.parseInt(arr2)
                    && (dtmBirth.getMonth() + 1) == Integer.parseInt(arr3) && (dtmBirth
                    .getDate() == Integer.parseInt(arr4)));
            if (!bGoodDay) {
                System.out.println("输入的身份证号里出生日期不对！");
                return false;
            } else {
                Integer[] arrInt = new Integer[]{7, 9, 10, 5, 8, 4, 2, 1, 6,
                        3, 7, 9, 10, 5, 8, 4, 2};

                int nTemp = 0;
                idcardNO = idcardNO.substring(0, 6) + "19"
                        + idcardNO.substring(6, idcardNO.length());
                for (int i = 0; i < 17; i++) {
                    nTemp += Integer.parseInt(idcardNO.substring(i, i + 1))
                            * arrInt[i];
                }
//                sfz += arrCh[nTemp % 11];
                return true;
            }
        }
        if (len == 18) {
            String arr1 = idcardNO.substring(0, 6);
            String arr2 = idcardNO.substring(6, 10);
            String arr3 = idcardNO.substring(10, 12);
            String arr4 = idcardNO.substring(12, 14);
            String arr5 = idcardNO.substring(14, 17);
            System.out.println("arr1" + arr1 + "arr2" + arr2 + "arr3" + arr3
                    + "arr4" + arr4 + "arr5" + arr5);
            if (Integer.parseInt(arr3) > 12 || Integer.parseInt(arr4) > 31) {
                //月份或日期过长
                return false;
            }
            // 检查生日日期是否正确
            Date dtmBirth = new Date(arr2 + "/" + arr3 + "/" + arr4);
            boolean bGoodDay;
            bGoodDay = (dtmBirth.getYear() + 1900 == Integer.parseInt(arr2)
                    && (dtmBirth.getMonth() + 1) == Integer.parseInt(arr3) && (dtmBirth
                    .getDate() == Integer.parseInt(arr4)));
            System.out.println(dtmBirth.getYear() + "===="
                    + (dtmBirth.getMonth() + 1) + "=====" + dtmBirth.getDate());
            if (!bGoodDay) {
                System.out.println("输入的身份证号里出生日期不对！");
                return false;
            } else {
                // 检验18位身份证的校验码是否正确。
                // 校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
                String valnum;
                int[] arrInt = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
                        10, 5, 8, 4, 2};

                int nTemp = 0;
                for (int i = 0; i < 17; i++) {
                    System.out.println("i" + i);
                    System.out.println(idcardNO.substring(i, i + 1));
                    nTemp += Integer.parseInt(idcardNO.substring(i, i + 1))
                            * arrInt[i];
                }
                valnum = arrCh[nTemp % 11];
                System.out.println(idcardNO.substring(17, 18));
                if (!valnum.equals(idcardNO.substring(17, 18))) {
                    System.out.println("18位身份证的校验码不正确！应该为：" + valnum);
                    return false;
                }
                return true;
            }
        }

        return false;
    }

    /**
     * @param str 判断是不是合格小数
     */
    public static boolean isPointNumber(String str) {
        if (str.trim().length() == 0) return false;

        if (str.indexOf(".") != str.lastIndexOf(".")) {
            return false;
        }

        if (str.endsWith(".")) {
            return false;
        }

        if (str.trim().length() >= 2 && str.charAt(0) == 48 && str.charAt(1) != 46) {
            //最少两位，第一位是0 第二位不是.
            return false;
        }

        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr == 46) {
                //小数点
            } else if (chr < 48 || chr > 57) {
                return false;
            }
        }


        //判断是不是0.00000这种格式
        if (str.length() > 3 && Double.parseDouble(str) == 0) {
            return false;
        }
//         int a=Integer.parseInt(str);
//          if(a>=1 && a<=31){
//           return true;
//          }     
        return true;
    }

    /**
     * @return 0-100范围
     */
    public static boolean isPercent(String string) {
        if (Double.parseDouble(string) < 0 || Double.parseDouble(string) > 100) {
            return false;
        }
        return true;
    }

    /**
     * @param str 判断是不是数字
     */
    public static boolean isNumeric(String str) {
        if (str.trim().length() == 0) return false;
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57) {
                return false;
            }
        }
//	     int a=Integer.parseInt(str);
//	      if(a>=1 && a<=31){
//	       return true;
//	      }     
        return true;
    }

    /**
     * 转m3u8格式
     *
     * @return
     */
    public static String getLivePlayUrl(String playUrl) {
        //playUrl  rtmp://play.lss.qupai.me/zhiboji/zhiboji-83R?auth_key=1466048000-0-1211-ecaace473792551908e3e2a4316a2f4a
        playUrl = playUrl.substring(0, playUrl.indexOf("?"));

        playUrl = playUrl.replace("rtmp", "http");

        //flv格式
        playUrl = playUrl.replace(".flv", "");

        playUrl = playUrl + ".m3u8";

        return playUrl;
    }

    /***
     * 转成时间
     * "2015-03-23  12:12:12"
     * "yyyy-MM-dd HH:mm:ss"
     */
    public static String GTMToLocal(String timestampString, String formats) {
        Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat(formats).format(new Date(timestamp));
        return date;
    }

    /**
     * 时间比较，date1小，返回true 否则返回false
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean dateCompare(String date1, String date2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            LogUtils.e(dt1 + "++" + dt2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return false;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }


    /**
     * 手机号加密处理
     * @param mobileNo
     * @return
     */
    public static String mobileEncrypt(String mobileNo){
        if (isBlank(mobileNo)||mobileNo.length()!=11){
            return "***********";
        }
        StringBuffer buffer = new StringBuffer(mobileNo);
        buffer.replace(3, 7, "****");
        return buffer.toString();
    }

}
