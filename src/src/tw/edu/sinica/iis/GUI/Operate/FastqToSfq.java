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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FastqToSfq {
	String src = "";
	String dst = "";

	public FastqToSfq(final String srcFastq, final String dstSfq) {
		src = srcFastq;
		dst = dstSfq;
	}

	public void convert() throws IOException {
		BufferedReader Readfile = new BufferedReader(new FileReader(src));
		BufferedWriter Writefile = new BufferedWriter(new FileWriter(dst));

		String temp = "";
		String name = "";
		String seq = "";
		String score = "";

		while (Readfile.ready()) {
			name = Readfile.readLine();

			if (name.charAt(0) == '@') {
				name = name.substring(1); // name
			} else {
				break;
			}

			seq = Readfile.readLine(); // seq
			temp = Readfile.readLine(); // quality separator

			if (temp.charAt(0) == '+') {
				; // do nothing
			} else {
				break;
			}

			score = Readfile.readLine();
			Writefile.write(name + "\t" + seq + "\t" + score + "\n");
		}

		Readfile.close();
		Writefile.close();
	}
}