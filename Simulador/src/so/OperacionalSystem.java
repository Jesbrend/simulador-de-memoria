package so;

import so.cpu.CpuManager;
import so.memory.MemoryManager;
import so.memory.Strategy;
import so.scheduler.Scheduler;

public class OperacionalSystem {

	private static MemoryManager mm;
	private static CpuManager cm;
	private static Scheduler scheduler;

	public static MemoryManager getMm() {
		return mm;
	}

	public static void setMm(MemoryManager mm) {
		OperacionalSystem.mm = mm;
	}

	public static CpuManager getCm() {
		return cm;
	}

	public static void setCm(CpuManager cm) {
		OperacionalSystem.cm = cm;
	}

	public static Scheduler getScheduler() {
		return scheduler;
	}

	public static void setScheduler(Scheduler scheduler) {
		OperacionalSystem.scheduler = scheduler;
	}

	public static Process systemCall(SystemCallType type, Process p, Integer value) {
		
		if (type.equals(SystemCallType.WRITE_PROCESS)) {
			p.setSizeInMemory(value);
			mm.writeProcess(p);
		} else if (type.equals(SystemCallType.DELETE_PROCESS)) {
			mm.deleteProcess(p);
		} else if (type.equals(SystemCallType.CREATE_PROCESS)) {			
			if (cm == null) {
				cm = new CpuManager();
			}
			if (mm == null) {
				mm = new MemoryManager(Strategy.WORST_FIT);
			}
			return new Process();

		}
		return null;
	}
}