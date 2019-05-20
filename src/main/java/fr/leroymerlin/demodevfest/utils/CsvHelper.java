package fr.leroymerlin.demodevfest.utils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *Csv helper for reader file
 */
public final class CsvHelper {

	/**
	 * private Constructor because is a helper class
	 */
	private CsvHelper(){
		super();
	}

	/**
	 * Create a CSV reader from a csv file name.
	 * @param csvFile the csv file name
	 * @return the {@link CSVReader} without the first line
	 */
	public static CSVReader getReader(String csvFile) {
		try {
			InputStream resourceAsStream = CsvHelper.class.getClassLoader()
														  .getResourceAsStream(csvFile);

			InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);

			CSVParser csvParser = new CSVParserBuilder().withSeparator('\t')
														.build();
			CSVReader reader = new CSVReaderBuilder(inputStreamReader)
								   .withCSVParser(csvParser)
								   .build();

			reader.skip(1);
			return reader;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
