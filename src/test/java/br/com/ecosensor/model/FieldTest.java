package br.com.ecosensor.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Assertions;

import br.com.ecosensor.exception.ExplosionException;

class FieldTest {
	
	private Fieldboard field;
	
	@BeforeEach
	void startTest() {
		this.field = new Fieldboard(3, 3);
	}
	
	/**
	 * 		1	2	3	4	5
	 * 1	F	F	F	F	F
	 * 2	F	T	T	T	F
	 * 3	F	T	X	T	F
	 * 4	F	T	T	T	F
	 * 5	F	F	F	F	F
	 * 
	 * @param x
	 * @param y
	 */
	@ParameterizedTest
	@CsvSource(delimiter = ',',
		value = { "2,2", "3,2", "4,2", "2,3", "4,3", "2,4", "3,4", "4,4" })
	void isNeighborTrueTest(int x, int y) {
		Fieldboard neighbor = new Fieldboard(x, y);
		boolean result = this.field.addNeighbor(neighbor);
		assertTrue(result);
	}
	
	@ParameterizedTest
	@CsvSource(delimiter = ',', value = { "1,1", "1,2", "1,3", "1,4", "1,5",
			"2,1", "3,1", "4,1", "5,1", "5,2", "5,3", "5,4", "5,5" })
	void isNeighborFalseTest(int x, int y) {
		Fieldboard neighbor = new Fieldboard(x, y);
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
		Assertions.assertThrows(ExplosionException.class, () -> {
			this.field.open();
		});
	}
	
	@Test
	void toggleTagWithNeighbor1() {
		Fieldboard neighbor1 = new Fieldboard(1, 1);
		Fieldboard neighbor2 = new Fieldboard(2, 2);
		
		neighbor2.addNeighbor(neighbor1);
		
		field.addNeighbor(neighbor2);
		
		field.open();
		
		assertTrue(neighbor2.isOpen() && neighbor1.isOpen());
	}
	
	@Test
	void toggleTagWithNeighbor2() {
		Fieldboard neighbor1 = new Fieldboard(1, 1);
		Fieldboard neighbor12 = new Fieldboard(1, 2);
		neighbor12.undermine();
		Fieldboard neighbor2 = new Fieldboard(2, 2);
		
		neighbor2.addNeighbor(neighbor1);
		neighbor2.addNeighbor(neighbor12);
		
		field.addNeighbor(neighbor2);
		
		field.open();
		
		assertTrue(neighbor2.isOpen() && neighbor1.isClose());
	}
	
}
