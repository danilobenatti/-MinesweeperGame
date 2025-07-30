package br.com.ecosensor.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import br.com.ecosensor.exception.ExplosionException;

class FieldTest {
	
	private FieldOfBoard field;
	
	@BeforeEach
	void startTest() {
		this.field = new FieldOfBoard(3, 4);
	}
	
	/**
	 * Fieldboard(line, column)
	 * 		1	2	3	4	5
	 * 1	F	F	F	F	F
	 * 2	F	F	T	T	T
	 * 3	F	M	T	X	T
	 * 4	F	M	T	T	T
	 * 5	F	F	F	F	F
	 * 
	 * @param line
	 * @param column
	 */
	@ParameterizedTest
	@CsvSource(delimiter = ',',
		value = { "2,3", "2,4", "2,5", "3,3", "3,5", "4,3", "4,4", "4,5" })
	void isNeighborTest(int line, int column) {
		FieldOfBoard neighbor = new FieldOfBoard(line, column);
		boolean result = this.field.addNeighbor(neighbor);
		assertTrue(result);
	}
	
	@ParameterizedTest
	@CsvSource(delimiter = ',',
		value = { "1,1", "1,2", "1,3", "1,4", "1,5", "2,1", "2,2", "3,1",
				"3,2", "4,1", "4,2", "5,1", "5,2", "5,3", "5,4", "5,5" })
	void notNeighborTest(int line, int column) {
		FieldOfBoard neighbor = new FieldOfBoard(line, column);
		boolean result = this.field.addNeighbor(neighbor);
		assertFalse(result);
	}
	
	@Test
	void toggleTagTest() {
		assertFalse(this.field.isMarkned());
	}
	
	@Test
	void toggleTagOnceTimeTest() {
		this.field.changeTag();
		assertTrue(this.field.isMarkned());
	}
	
	@Test
	void toggleTagTwicetimeTest() {
		this.field.changeTag();
		this.field.changeTag();
		assertFalse(this.field.isMarkned());
	}
	
	@Test
	void toggleTagNotMinedUnmarked() {
		assertTrue(this.field.open());
	}
	
	@Test
	void toggleTagNotMinedMarked() {
		this.field.changeTag();
		assertFalse(this.field.open());
	}
	
	@Test
	void toggleTagMinedMarked() {
		this.field.changeTag();
		this.field.undermine();
		assertFalse(this.field.open());
	}
	
	@Test
	void toggleTagMinedUnmarked() {
		this.field.undermine();
		assertThrows(ExplosionException.class, field::open);
	}
	
	@Test
	void toggleTagWithNeighbor1() {
		FieldOfBoard neighbor1 = new FieldOfBoard(1, 2);
		FieldOfBoard neighbor2 = new FieldOfBoard(2, 3);
		
		neighbor2.addNeighbor(neighbor1);
		
		field.addNeighbor(neighbor2);
		
		field.open();
		
		assertTrue(neighbor1.isOpen() && neighbor2.isOpen());
	}
	
	@Test
	void toggleTagWithNeighbor2() {
		FieldOfBoard neighbor1 = new FieldOfBoard(1, 1);
		FieldOfBoard neighbor2 = new FieldOfBoard(1, 2);
		neighbor2.undermine();
		FieldOfBoard neighbor3 = new FieldOfBoard(2, 3);
		
		neighbor3.addNeighbor(neighbor1);
		neighbor3.addNeighbor(neighbor2);
		
		field.addNeighbor(neighbor3);
		
		field.open();
		
		assertTrue(neighbor3.isOpen() && neighbor1.isClose());
	}
	
	@Test
	void NeighborMineTest() {
		FieldOfBoard neighbor0 = new FieldOfBoard(2, 3);
		FieldOfBoard neighbor1 = new FieldOfBoard(3, 3);
		FieldOfBoard neighbor2 = new FieldOfBoard(3, 2);
		FieldOfBoard neighbor3 = new FieldOfBoard(4, 2);
		
		neighbor2.undermine();
		neighbor3.undermine();
		
		
		neighbor0.addNeighbor(neighbor2);
		neighbor1.addNeighbor(neighbor2);
		neighbor1.addNeighbor(neighbor3);
		
		assertEquals(1, neighbor0.neighboringMines());
		assertEquals(2, neighbor1.neighboringMines());
	}
	
	@Test
	void ToStringTest() {
		FieldOfBoard neighbor = new FieldOfBoard(3, 2);
		neighbor.undermine();
		
		assertEquals("?", neighbor.toString());
		
		neighbor.setOpenned(true);
		
		assertEquals("*", neighbor.toString());
	}
}
