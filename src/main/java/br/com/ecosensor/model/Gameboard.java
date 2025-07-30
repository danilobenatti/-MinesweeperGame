package br.com.ecosensor.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import br.com.ecosensor.exception.ExplosionException;

public class Gameboard {
	
	private int lines;
	private int columns;
	private int mines;
	
	private List<FieldOfBoard> fields = new ArrayList<>();
	
	public Gameboard(int lines, int columns, int mines) {
		this.lines = lines;
		this.columns = columns;
		this.mines = mines;
		
		generateFields();
		associateNeighbors();
		raffleMines();
		
	}
	
	public void open(int line, int column) {
		try {
			this.fields.parallelStream()
					.filter(c -> c.getLine() == line && c.getColumn() == column)
					.findFirst().ifPresent(c -> c.open());
		} catch (ExplosionException e) {
			this.fields.forEach(f -> f.setOpenned(true));
			throw e;
		}
	}
	
	public void changeTag(int line, int column) {
		this.fields.parallelStream()
				.filter(c -> c.getLine() == line && c.getColumn() == column)
				.findFirst().ifPresent(c -> c.changeTag());
	}
	
	private void generateFields() {
		for (int i = 0; i < lines; i++) {
			for (int j = 0; j < columns; j++) {
				this.fields.add(new FieldOfBoard(i, j));
			}
		}
	}
	
	private void associateNeighbors() {
		for (FieldOfBoard field1 : this.fields) {
			for (FieldOfBoard field2 : this.fields) {
				field1.addNeighbor(field2);
			}
		}
	}
	
	private void raffleMines() {
		long armedMines = 0;
		Predicate<FieldOfBoard> mineded = f -> f.isMineded();
		do {
			int rand = (int) (Math.random() * this.fields.size());
			this.fields.get(rand).undermine();
			armedMines = this.fields.stream().filter(mineded).count();
		} while (armedMines < this.mines);
	}
	
	public boolean goalComplete() {
		return this.fields.stream().allMatch(f -> f.goalComplete());
	}
	
	public void restart() {
		this.fields.stream().forEach(c -> c.restartGame());
		raffleMines();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(StringUtils.SPACE);
		sb.append(StringUtils.SPACE);
		
		for (int c = 0; c < this.columns; c++) {
			sb.append(StringUtils.SPACE);
			sb.append(c);
			sb.append(StringUtils.SPACE);
		}
		sb.append(StringUtils.LF);
		
		int i = 0;
		for (int l = 0; l < this.lines; l++) {
			sb.append(l);
			sb.append(StringUtils.SPACE);
			for (int c = 0; c < this.columns; c++) {
				sb.append(StringUtils.SPACE);
				sb.append(this.fields.get(i));
				sb.append(StringUtils.SPACE);
				i++;
			}
			sb.append(StringUtils.LF);
		}
		return sb.toString();
	}
	
}
