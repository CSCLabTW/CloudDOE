package examples;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import tw.edu.sinica.iis.SSHadoop.SSHCBRun;
import tw.edu.sinica.iis.SSHadoop.SSHExec;
import tw.edu.sinica.iis.SSHadoop.SSHSession;
import tw.edu.sinica.iis.SSHadoop.SSHadoopCmd;
import tw.edu.sinica.iis.SSHadoop.SSHadoopUtils;

public class SSHExecExample {
	public static void main(String[] args) {
		SSHSession session = new SSHSession("hadoop", "140.109.18.202");
		session.setPass("1qazse4");
		session.setKnownHosts(System.getProperty("user.home")
				+ "/.ssh/known_hosts");
		session.setIdentity(System.getProperty("user.home")
				+ "/.ssh/id_rsa_hadoop");
		session.initSession();
		session.openSession();

		SSHadoopCmd cmds = new SSHadoopCmd();
		cmds.setUserBase("1353033336746Zxaustin");
		cmds.setServerSpecialCmd("source /etc/profile");

		String testCmd = cmds
				.jarHdp("1353033336746Zxaustin/main/CloudBrush2.jar",
						"",
						"-asm 1353033336746Zxaustin/123final -reads 1353033336746Zxaustin/S101-2/ -readlen 101 -k 25 -work 1353033336746Zxaustin/123final &");

		Callable<String> channel = new SSHCBRun(session.getSession(), testCmd);
		FutureTask<String> futureTask = new FutureTask<String>(channel);

		// Thread thread = new Thread(futureTask);
		// thread.start();

		while (true) {
			if (futureTask.isDone()) {
				System.out.println("Submit CB done");
				break;
			}
		}

		/* get CB status */
		SSHadoopUtils utils = new SSHadoopUtils();

		Callable<String> channel2 = new SSHExec(session.getSession(),
				cmds.CBStatus("123final"));
		boolean isDone = false;

		Callable<String> channel3 = new SSHExec(session.getSession(),
				cmds.CBStepAndId("123final", 30));
		double[] results = { 0.0, 0.0 };

		// for monitoring test
		while (true) {
			FutureTask<String> futureTask2 = new FutureTask<String>(channel2);

			Thread thread2 = new Thread(futureTask2);
			thread2.start();

			// get whether cb is done
			while (true) {
				if (futureTask2.isDone()) {
					// try {
					// isDone = utils.getCBStatus(futureTask2.get());
					// } catch (InterruptedException e) {
					// e.printStackTrace();
					// } catch (ExecutionException e) {
					// e.printStackTrace();
					// }
					break;
				}
			}

			// if done then close session and leave, or do check
			if (isDone) {
				session.closeSession();
				break;
			} else {
				String toolNameAndJobId[] = { "", "" };

				FutureTask<String> futureTask3 = new FutureTask<String>(
						channel3);

				Thread thread3 = new Thread(futureTask3);
				thread3.start();

				// get cb step and job id
				while (true) {
					if (futureTask3.isDone()) {
						try {
							toolNameAndJobId = utils
									.getCBStepAndJobId(futureTask3.get());
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						break;
					}
				}

				// if we get the job id
				if (!"".equals(toolNameAndJobId[1])) {
					Callable<String> channel4 = new SSHExec(
							session.getSession(),
							cmds.jobStatusHdp(toolNameAndJobId[1]));

					// new a job for get status
					while (true) {
						FutureTask<String> futureTask4 = new FutureTask<String>(
								channel4);

						Thread thread4 = new Thread(futureTask4);
						thread4.start();

						// if we get the status
						while (true) {
							if (futureTask4.isDone()) {
								try {
									results = utils.getJobStatus(futureTask4
											.get());
								} catch (InterruptedException e) {
									e.printStackTrace();
								} catch (ExecutionException e) {
									e.printStackTrace();
								}
								break;
							}
						}

						// print the status and check if we should get again
						// till finish
						System.out.println(toolNameAndJobId[0] + " map: "
								+ String.valueOf(results[0]) + "% red: "
								+ String.valueOf(results[1]) + "%");
						if (results[0] != 0.0 && results[1] != 0.0)
							break;
					}
				}
			}
		}
		// session.closeSession();
	}
}
