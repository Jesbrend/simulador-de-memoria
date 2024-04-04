package so;

public class Execute {

	public static void main(String[] args) {

		Process p1 = OperacionalSystem.systemCall(SystemCallType.CREATE_PROCESS, null, null);
		OperacionalSystem.systemCall(SystemCallType.WRITE_PROCESS, p1, 20);

		Process p2 = OperacionalSystem.systemCall(SystemCallType.CREATE_PROCESS, null, null);
		OperacionalSystem.systemCall(SystemCallType.WRITE_PROCESS, p2, 38);
		
		Process p3 = OperacionalSystem.systemCall(SystemCallType.CREATE_PROCESS, null, null);
		OperacionalSystem.systemCall(SystemCallType.WRITE_PROCESS, p3, 38);
		
		Process p4 = OperacionalSystem.systemCall(SystemCallType.CREATE_PROCESS, null, null);
		OperacionalSystem.systemCall(SystemCallType.WRITE_PROCESS, p4, 20);
		
		OperacionalSystem.systemCall(SystemCallType.DELETE_PROCESS, p2, null);
		
		Process p5 = OperacionalSystem.systemCall(SystemCallType.CREATE_PROCESS, null, null);
		OperacionalSystem.systemCall(SystemCallType.WRITE_PROCESS, p5, 40);
		
	}

}




