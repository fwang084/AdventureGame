package ca.vanzeben.game.entities;

import java.util.ArrayList;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public class Tower extends Entity {
	private ArrayList<Fireball> fireballs;
	private Entity e;
	public Tower(int x, int y, Level level, int tickCount, SpriteSheet sheet, Entity e) {
		super(x, y, level, tickCount, sheet);
		fireballs = new ArrayList<Fireball>();
		this.e = e;
	}
	@Override
	public void tick() {
		super.tick();
		if((tickCount/60) != (tickCount+1)/60) {
			Fireball f = new Fireball(this.x, this.y, this.level, 0, SpriteSheet.tileSheet, 5, angleTowardEntity(e));
			fireballs.add(f);
		}
		for(int i = 0; i < fireballs.size(); i++) {
			if(fireballs.get(i).centerX() > 5000 || fireballs.get(i).centerX() < 0
					||fireballs.get(i).centerY() > 5000 || fireballs.get(i).centerY() < 0){
				fireballs.remove(i);
				i--;
			}
		}
	}
	@Override
	public void render(Screen screen, int row, int col) {
		super.render(screen, row, col);
	}
	public ArrayList<Fireball> getFireballs() {
		return fireballs;
	}

}
