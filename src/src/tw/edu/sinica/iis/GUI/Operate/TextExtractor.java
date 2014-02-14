/*
 * (C) Copyright 2013 The CloudDOE Project and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *      Wei-Chun Chung (wcchung@iis.sinica.edu.tw)
 *      Yu-Chun Wang (zxaustin@iis.sinica.edu.tw)
 * 
 * CloudDOE Project:
 *      http://clouddoe.iis.sinica.edu.tw/
 */

package tw.edu.sinica.iis.GUI.Operate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class TextExtractor {
	public static String getCharsetFileText(File file, String charset) {
		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader aa = new InputStreamReader(new FileInputStream(
					file), charset);
			int index = 0;
			while ((index = aa.read()) != -1) {
				sb.append((char) index);
				// System.out.print((char)index+" ");
			}
			aa.close();
		} catch (UnsupportedEncodingException ex1) {
		} catch (FileNotFoundException ex) {
			/** @todo Handle this exception */
		} catch (IOException ex) {
			/** @todo Handle this exception */
		}
		return sb.toString();

	}

	public static String getFileText(File file) {

		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			br.close();
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
			/** @todo Handle this exception */
		}
		return sb.toString();
	}

	public static void textToFile(String fileName, String text) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fileName));
			bw.write(text);
			bw.close();
		} catch (IOException ex) {
		}

	}

	public static void textToFileUTF8(String fileName, String text) {
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			osw.write(text);
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
