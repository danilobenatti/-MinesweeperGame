package br.com.ecosensor;

import br.com.ecosensor.model.Gameboard;
import br.com.ecosensor.view.Consoleboard;

public class Application {
	
	public static void main(String[] args) {
		
		Gameboard game = new Gameboard(6, 6, 5);
		new Consoleboard(game);
	}
}
