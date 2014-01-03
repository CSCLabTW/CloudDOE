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