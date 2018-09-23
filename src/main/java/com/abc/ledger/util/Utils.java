package com.abc.ledger.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abc.ledger.enums.AccountType;
import com.abc.ledger.enums.TransactionType;
import com.abc.ledger.pojo.Position;
import com.abc.ledger.pojo.TransactionDetails;
import com.abc.ledger.transaction.QuantityCalculator;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Utils {

	public static final String TXN_FILE = "1537277231233_Input_Transactions.txt";
	public static final String POSITION_FILE = "Input_StartOfDay_Positions.txt";
	public static final String FILE_HEADER = "Instrument,Account,AccountType,Quantity,Delta";
	public static final String END_OF_DAY_FILE = "EndOfDay_Positions.txt";
	public static final String NEW_LINE_SEPARATOR = "\n";
	public static final String CSV_SEPARATOR = ",";

	private Utils() {
		// Shouldn't instantiate the class
	}

	/**
	 * This methos reads a json file and creates a nested map containing the
	 * transaction details
	 * 
	 * @return transactionMap
	 * @throws IOException
	 */
	public static Map<String, Map<String, Long>> readTxnJson() throws IOException {

		ClassLoader classLoader = QuantityCalculator.class.getClassLoader();
		BufferedReader reader = new BufferedReader(new FileReader(classLoader.getResource(TXN_FILE).getFile()));
		String json = "";
		Map<String, Map<String, Long>> transactionMap = new HashMap();
		try {
			StringBuilder sb = new StringBuilder();
			String line = reader.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = reader.readLine();
			}
			json = sb.toString();
			Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
			TransactionDetails[] arr = gson.fromJson(json, TransactionDetails[].class);

			for (TransactionDetails transact : arr) {
				if (transactionMap.containsKey(transact.getInstrument())) {
					Map<String, Long> transactionTypeMap = transactionMap.get(transact.getInstrument());
					if (transactionTypeMap.containsKey(transact.getTransactionType())) {
						long txnQuantity = transactionTypeMap.get(transact.getTransactionType());
						txnQuantity += transact.getTransactionQuantity();
						transactionTypeMap.put(transact.getTransactionType(), txnQuantity);
					} else {
						transactionTypeMap.put(transact.getTransactionType(), transact.getTransactionQuantity());
					}
				} else {
					Map<String, Long> innerMap = new HashMap<String, Long>();
					innerMap.put(transact.getTransactionType(), transact.getTransactionQuantity());
					transactionMap.put(transact.getInstrument(), innerMap);
				}
			}
		} finally {
			reader.close();
		}
		return transactionMap;
	}

	/**
	 * This method reads the position text file
	 * 
	 * @return positionList
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static List<Position> readPositionCsv() throws NumberFormatException, IOException {

		String line = null;
		BufferedReader stream = null;
		ClassLoader classLoader = QuantityCalculator.class.getClassLoader();
		List<Position> positionList = new ArrayList<Position>();
		try {
			stream = new BufferedReader(new FileReader(classLoader.getResource(POSITION_FILE).getFile()));
			stream.readLine();
			while ((line = stream.readLine()) != null) {
				String[] splitted = line.split(",");
				Position position = new Position();
				position.setInstrument(splitted[0]);
				position.setAccount(splitted[1]);
				position.setAccType(splitted[2]);
				position.setQuantity(Long.valueOf(splitted[3]));
				positionList.add(position);
			}

		} finally {
			if (stream != null)
				stream.close();
		}
		return positionList;

	}

	/**
	 * This method reads calculates the end positions for the day
	 * 
	 * @param startPositionList
	 *            list of the start positions of the day
	 * @param transactionMap
	 *            the map containing the transaction details of instruments
	 * @return
	 */
	public static List<Position> getEndPositionList(List<Position> startPositionList,
			Map<String, Map<String, Long>> transactionMap) {

		List<Position> endPositionList = new ArrayList<Position>();
		for (Position position : startPositionList) {
			Long initialQuantity = position.getQuantity();
			Long netVolume = 0l;
			if (transactionMap.containsKey(position.getInstrument())) {

				Map<String, Long> innerMap = transactionMap.get(position.getInstrument());
				for (Map.Entry<String, Long> innerMapEntry : innerMap.entrySet()) {
					String name = innerMapEntry.getKey();
					Long quantity = 0l;
					innerMapEntry.getValue();
					if ((name.equals(TransactionType.SELL.getValue())
							&& position.getAccType().equals(AccountType.INTERNAL.getValue()))
							|| (name.equals(TransactionType.BUY.getValue())
									&& position.getAccType().equals(AccountType.EXTERNAL.getValue()))) {
						quantity = innerMapEntry.getValue() + position.getQuantity();
						position.setQuantity(quantity);
					} else if (name.equals(TransactionType.BUY.getValue())
							&& position.getAccType().equals(AccountType.INTERNAL.getValue())
							|| name.equals(TransactionType.SELL.getValue())
									&& position.getAccType().equals(AccountType.EXTERNAL.getValue())) {
						quantity = position.getQuantity() - innerMapEntry.getValue();
						position.setQuantity(quantity);
					}

				}
				netVolume = position.getQuantity() - initialQuantity;
				position.setNetVoluume(netVolume);
			} else {
				position.setNetVoluume(netVolume);
			}
			endPositionList.add(position);
		}
		return endPositionList;

	}

	/**
	 * This method creates the end of the day position csv file
	 * 
	 * @param endPositionList
	 *            list of all end of the day positions
	 * @return
	 */
	public static void createEndOfDayFile(List<Position> endPositionList) {

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(END_OF_DAY_FILE);
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append(NEW_LINE_SEPARATOR);

			for (Position position : endPositionList) {
				fileWriter.append(position.getInstrument());
				fileWriter.append(CSV_SEPARATOR);
				fileWriter.append(position.getAccount());
				fileWriter.append(CSV_SEPARATOR);
				fileWriter.append(position.getAccType());
				fileWriter.append(CSV_SEPARATOR);
				fileWriter.append(String.valueOf(position.getQuantity()));
				fileWriter.append(CSV_SEPARATOR);
				fileWriter.append(String.valueOf(position.getNetVoluume()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
