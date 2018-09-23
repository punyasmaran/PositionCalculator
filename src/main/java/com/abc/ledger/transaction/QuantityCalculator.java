package com.abc.ledger.transaction;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.abc.ledger.pojo.Position;
import com.abc.ledger.util.Utils;

public class QuantityCalculator {
	
	public static void main(String[] args) throws IOException {

		Map<String, Map<String, Long>> transactionMap = Utils.readTxnJson();
		List<Position> startPositionList = Utils.readPositionCsv();
		List<Position> endPositionList = Utils.getEndPositionList(startPositionList, transactionMap);
		Utils.createEndOfDayFile(endPositionList);
		Collections.sort(endPositionList, new Comparator<Position>() {
			public int compare(Position initialPosition, Position nextPosition) {
				return (int) (initialPosition.getNetVoluume() - nextPosition.getNetVoluume());
			}
		});

		System.out.println("largest and lowest net transaction volumes for the day ::");

		System.out.println("Instrument " + endPositionList.get(0).getInstrument() + " has the lowest net volume of "
				+ endPositionList.get(0).getNetVoluume());

		System.out.println("Instrument " + endPositionList.get(endPositionList.size() - 1).getInstrument()
				+ " has the highest net volume of " + endPositionList.get(endPositionList.size() - 1).getNetVoluume());
	}

}
