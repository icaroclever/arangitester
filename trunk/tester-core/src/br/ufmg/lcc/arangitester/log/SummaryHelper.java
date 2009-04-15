package br.ufmg.lcc.arangitester.log;

import java.util.Date;

/**
 * Make all statics from runned tests
 * 
 * @author Lucas Gonçalves
 * 
 */
public class SummaryHelper {
	private int total;
	private int successful;
	private int fail;
	private int skip;
	private int time;
	
	public SummaryHelper(FunctionalSuite result) {
		for (UseCase use : result.getCases()) {
			for (TestCase test : use.getTestcases()) {
				total++;
				if (test.isSkip()) {
					skip++;
				} else {
					boolean isSucc = true;
					for (Object log : test.getlogs()) {
						if (log instanceof Error) {
							isSucc = false;
						}
					}
					if (isSucc) {
						successful++;
					} else {
						fail++;
					}
				}
			}
		}

		if ( !result.getCases().isEmpty() ){
			UseCase first = result.getCases().get(0);
			UseCase last = result.getCases().get(result.getCases().size() -1);
			Date start = first.getStartTime();
			Date end = last.getEndTime();
			time = (int)(end.getTime() - start.getTime()) / (1000 * 60);
		}
	}

	public int getTotal() {
		return total;
	}

	public int getSuccessful() {
		return successful;
	}

	public int getFail() {
		return fail;
	}

	public int getSkip() {
		return skip;
	}

	public float getPercent() {
		return (float) ((float) successful / ((float) total - (float) skip) * 100);
	}
	
	public int getTotalTime(){
		return time;
	}
}
