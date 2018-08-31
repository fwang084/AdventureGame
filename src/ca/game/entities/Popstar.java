package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Popstar extends MovingEntity {
	public Popstar(int x, int y, Level level, int tickCount, SpriteSheet sheet, int speed) {
		super(x, y, level, tickCount, sheet, speed);
	}

	@Override
	public void move() {
		this.moveRandom();
	}

	@Override
	public void render(Screen screen, int row, int col) {
		super.render(screen, row, col);
	}

}