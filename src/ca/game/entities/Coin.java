package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Coin extends Entity{
	private final int x, y;
	private int width, height;
	private Level level;
	private int value;
	public Coin(int x, int y, Level level, int value) {
		super(x, y, level, 0, SpriteSheet.tileSheet);
		this.x = x;
		this.y = y;
		this.value = value;

		this.width = SpriteSheet.tileSheet.getSpriteWidth();
		this.height = SpriteSheet.tileSheet.getSpriteHeight();
	}
	public int getValue() {
		return this.value;
	}
}
