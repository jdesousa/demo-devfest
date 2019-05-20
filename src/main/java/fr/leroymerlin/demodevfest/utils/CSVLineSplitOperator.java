package fr.leroymerlin.demodevfest.utils;

import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Utils class to split line operator
 */
@Slf4j
@AllArgsConstructor
public class CSVLineSplitOperator implements Spliterator<String[]> {

	private CSVReader source;

	@Override
	public boolean tryAdvance(Consumer<? super String[]> action) {
		try {
			String[] line = this.source.readNext();
			if (line == null) {
				return false;
			}
			action.accept(line);
			return true;
		} catch (IOException ex) {
			log.error("Error of read next line", ex);
			return false;
		}
	}

	@Override
	public Spliterator<String[]> trySplit() {
		return null;
	}

	@Override
	public long estimateSize() {
		return Long.MAX_VALUE;
	}

	@Override
	public int characteristics() {
		return IMMUTABLE | NONNULL | ORDERED;
	}
}
