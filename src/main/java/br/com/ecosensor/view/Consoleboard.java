package br.com.ecosensor.view;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import br.com.ecosensor.exception.ExitException;
import br.com.ecosensor.exception.ExplosionException;
import br.com.ecosensor.model.Gameboard;

public class Consoleboard {
	
	private Gameboard gameboard;
	
	private Scanner scanner = new Scanner(System.in);
	
	public Consoleboard(Gameboard gameboard) {
		this.gameboard = gameboard;
		executeGame();
	}
	
	private void executeGame() {
		try {
			boolean keepgoing = true;
			while (keepgoing) {
				gamecycle();
				System.out.print("Another game? (Y/n) ");
				if (scanner.next().equalsIgnoreCase("n")) {
					keepgoing = false;
				} else {
					this.gameboard.restart();
				}
			}
		} catch (ExitException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void gamecycle() {
		try {
			while (!this.gameboard.goalComplete()) {
				System.out.println(this.gameboard);
				
				String value = valueCapture("Inform position(line, column): ");
				
				Iterator<Integer> mapToInt = Arrays.stream(value.split(","))
						.mapToInt(Integer::parseInt).iterator();
				
				value = valueCapture("1 - open | 2 - (des)mark: ");
				
				if (value.equalsIgnoreCase("1")) {
					this.gameboard.open(mapToInt.next(), mapToInt.next());
				} else if (value.equalsIgnoreCase("2")) {
					this.gameboard.changeTag(mapToInt.next(), mapToInt.next());
				}
				
			}
			System.out.println(this.gameboard);
			System.out.println("Game winner!");
		} catch (ExplosionException e) {
			System.out.println(this.gameboard);
			System.out.println("Game over!");
		}
		
	}
	
	private String valueCapture(String text) {
		System.out.print(text);
		String str = scanner.next();
		if (str.equalsIgnoreCase("exit")) {
			System.out.println("Exit game!!!");
			throw new ExitException();
		}
		return str;
	}
}
