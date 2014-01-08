package examples;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import tw.edu.sinica.iis.SSHadoop.SSHadoopUtils;

public class LogParseExample {
	public static void main(String[] args) {
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader bufferedReader;

		try {
			bufferedReader = new BufferedReader(new FileReader(
					"test/brush.details.log"));

			String line = null;
			try {
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line).append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SSHadoopUtils utils = new SSHadoopUtils();

		String[] rst = { "", "" };

		rst = utils.getCBStepAndJobId(stringBuffer.toString());

		System.out.println("tool name: " + rst[0]);
		System.out.println("job id: " + rst[1]);
	}
}
