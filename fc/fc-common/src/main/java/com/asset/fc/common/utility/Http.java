package com.asset.fc.common.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nour.ihab
 */
public class Http {

    public static String getBody(HttpServletRequest request) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream(); // retrive the body of the request as bytes
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));//read the input streams as a bufferred charcters
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }
        }

        String body = stringBuilder.toString(); //return the string into the body
        return body;
    }

}
