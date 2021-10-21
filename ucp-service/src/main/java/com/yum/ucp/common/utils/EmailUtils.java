package com.yum.ucp.common.utils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Administrator on 2017/4/27.
 */
public class EmailUtils {
    private static final Random RANDOM = new Random();

    private EmailUtils() {
    }

    static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    static void notNull(Object object, String message) {
        if(object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    static String randomAlphabetic(int count) {
        return random(count, 0, 0, true, false, (char[])null, RANDOM);
    }

    private static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
        if(count == 0) {
            return "";
        } else if(count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        } else {
            if(start == 0 && end == 0) {
                end = 123;
                start = 32;
                if(!letters && !numbers) {
                    start = 0;
                    end = 2147483647;
                }
            }

            StringBuffer buffer = new StringBuffer();
            int gap = end - start;

            while(true) {
                while(count-- != 0) {
                    char ch;
                    if(chars == null) {
                        ch = (char)(random.nextInt(gap) + start);
                    } else {
                        ch = chars[random.nextInt(gap) + start];
                    }

                    if(letters && numbers && Character.isLetterOrDigit(ch) || letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
                        buffer.append(ch);
                    } else {
                        ++count;
                    }
                }

                return buffer.toString();
            }
        }
    }

    static void writeMimeMessage(File resultFile, MimeMessage mimeMessage) throws IOException, MessagingException {
        FileOutputStream fos = null;
        if(mimeMessage == null) {
            throw new IllegalArgumentException("mimeMessage is null");
        } else if(resultFile == null) {
            throw new IllegalArgumentException("resulFile is null");
        } else {
            if(resultFile.getParentFile() != null) {
                resultFile.getParentFile().mkdirs();
            }

            try {
                fos = new FileOutputStream(resultFile);
                mimeMessage.writeTo(fos);
                fos.flush();
                fos.close();
                fos = null;
            } finally {
                if(fos != null) {
                    try {
                        fos.close();
                        fos = null;
                    } catch (Exception var9) {
                        ;
                    }
                }

            }

        }
    }
}
