package mrcpsp.process.concurrent

import java.util.concurrent.ThreadPoolExecutor

class RejectedExecutionHandlerImpl implements java.util.concurrent.RejectedExecutionHandler {

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		println(r.toString() + " is rejected");
	}

}
