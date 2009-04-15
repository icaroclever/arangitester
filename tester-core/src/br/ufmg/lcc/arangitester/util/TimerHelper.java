package br.ufmg.lcc.arangitester.util;

/**
 * Classe para controle de intervalos de tempos. Ao criar uma instancia desta classe um cronometro será iniciado. 
 * Quando chamar o método stop. Pode-se chamar os métodos getMin e getSec para obter os minutos e segundos respectivamentes.
 * Se chamar o método getMin ou getSec sem antes chamar o stop isso causará o stop automaticamente.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class TimerHelper {
	long start;
	Long end;
	long elapsedTime;

	public TimerHelper() {
		start = System.currentTimeMillis();
	}

	public void stop() {
		end = System.currentTimeMillis();
		elapsedTime = end - start;
	}

	public long getMin() {
		if (end == null){
			stop();
		}
		return elapsedTime / (1000 * 60);
	}

	public long getSec() {
		if (end == null){
			stop();
		}
		return (elapsedTime / 1000) % 60;
	}
}
