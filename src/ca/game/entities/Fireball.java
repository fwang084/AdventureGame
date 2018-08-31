package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Fireball extends MovingEntity {
	private double angle;
	public Fireball(int x, int y, Level level, int tickCount, SpriteSheet sheet, int speed, double angle) {
		super(x, y, level, tickCount, sheet, speed);
		this.angle = angle;
	}

	@Override
	public void move() {
		this.moveInDirection(this.angle);
	}
	@Override
	public void tick() {
		super.tick();
		move();
	}
	@Override
	public void render(Screen screen, int row, int col) {
		super.render(screen, row, col);
	}

}
