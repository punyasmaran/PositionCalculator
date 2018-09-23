package com.abc.ledger.test;

import java.util.ArrayList;
import java.util.List;

import com.abc.ledger.pojo.Position;
import com.abc.ledger.util.Utils;

public class FileReadWriteTest {

	public static void main(String[] args) {
		System.out.println("Write CSV file:");
		List<Position> endPositionList = new ArrayList<Position>();
		Position firstPosition = new Position();
		firstPosition.setInstrument("IBM");
		firstPosition.setAccount("101");
		firstPosition.setAccType("E");
		firstPosition.setQuantity(101000l);
		firstPosition.setNetVoluume(1000l);
		
		Position secondPosition = new Position();
		secondPosition.setInstrument("IBM");
		secondPosition.setAccount("201");
		secondPosition.setAccType("I");
		secondPosition.setQuantity(-101000l);
		secondPosition.setNetVoluume(-1000l);
		
		endPositionList.add(firstPosition);
		endPositionList.add(secondPosition);
		
		Utils.createEndOfDayFile(endPositionList);
	}
}
