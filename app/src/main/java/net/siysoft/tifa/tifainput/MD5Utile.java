package net.siysoft.tifa.tifainput;

import java.security.MessageDigest;

/**
 * @项目名 ：MRIM
 * @包名 ：com.jxgm.mrim.utiles
 * @User ： hezhihu89 by：贺志虎
 * @创建时间 ：2016 年 01 月 11 日   14时 14分.
 * @类的描述 :  加密密码 使用MD5值
 */
public class MD5Utile {

    //十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String encodeByMD5(String originString) {

        if (originString != null) try {
            synchronized (MD5Utile.class) {

                //创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String resultString = getString(results);
                return resultString.toUpperCase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    private static String getString(byte[] results) {
        StringBuffer sb = null;
        if (results != null) {
            sb = new StringBuffer();
            for (int x = 0; x < results.length; x++) {
                sb.append(byteToHexString(results[x]));
            }
        } else {
            throw new RuntimeException("数组为null");
        }

        return sb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
