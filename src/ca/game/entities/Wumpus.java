
package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;



public class Wumpus extends MovingEntity{
	private Entity e;
	public Wumpus(int x, int y, Level level, int tickCount, SpriteSheet sheet, int speed, Entity e) {
		super(x, y, level, tickCount, sheet, speed);
		this.e = e;
	}

	@Override
	public void move() {
		this.moveTowardEntity(this.e);
	}
	

	

	@Override
	public void render(Screen screen, int row, int col) {
		super.render(screen, row, col);
	}
}
