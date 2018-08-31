package ca.vanzeben.game.entities;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.InputHandler;
import ca.vanzeben.game.gfx.Font;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;
import ca.vanzeben.game.level.tiles.Tile;

public class Player extends MovingEntity {
	public static enum MOVE_DIR{
		UP, DOWN, LEFT, RIGHT, NONE
	};
	@Override
	public void move() {
		int xDir = 0;
		int yDir = 0;
		if (input != null) {
			if (input.up.isPressed()) {
				yDir--;
			}
			if (input.down.isPressed()) {
				yDir++;
			}
			if (input.left.isPressed()) {
				xDir--;
			}
			if (input.right.isPressed()) {
				xDir++;
			}
			if(input.x.isPressed()){
				isSolid = false;
			} else {
				isSolid = true;
			}
		}

		if (xDir != 0 || yDir != 0) {
			move(xDir * speed, yDir * speed);
			isMoving = true;
		} else {
			isMoving = false;
		}
	}
	private static final int PLAYER_SPEED = 10;
	private boolean debug = false;
	private boolean isSolid;

	protected String name;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected MOVE_DIR movingDir = MOVE_DIR.NONE;
	

	private InputHandler input;
	protected boolean isSwimming = false;
	private String username;
	private int money;


	public Player(Level level, int x, int y, int tickCount, SpriteSheet sheet, int speed,
			InputHandler input, String username) {
		super(x, y, level, tickCount, sheet, speed);
		this.input = input;
		this.username = username;
		money = 0;
	}

	/***
	 * Move the player by dx, dy
	 * 
	 * @param dx
	 * @param dy
	 */
	public void move(int dx, int dy) {
		if (dx != 0 && dy != 0) {
			move(dx, 0);
			move(0, dy);
			numSteps--;
			return;
		}

		numSteps++;
		
	//	if (!willCollideWithTile(dx, dy)|| isSolid == false) {
			if (dy < 0)
				movingDir = MOVE_DIR.UP;
			if (dy > 0)
				movingDir = MOVE_DIR.DOWN;
			if (dx < 0)
				movingDir = MOVE_DIR.LEFT;
			if (dx > 0)
				movingDir = MOVE_DIR.RIGHT;
			x += dx;
			y += dy;
		//}
	}

	public Tile getCurrentTileType() {
		return this.getLevel().getTileTypeAtWorldCoordinates(x, y);
	}

	public String getName() {
		return name;
	}

	public int getNumSteps() {
		return numSteps;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public MOVE_DIR getMovingDir() {
		return movingDir;
	}


	public void tick() {
		move();
		tickCount++;
	}

	public void render(Screen screen) {
		if(this.input.x.isPressed()){
			screen.render(x, y, getSheet(), 6, 6, Screen.MirrorDirection.NONE);
		} else {
			Tile current = this.getCurrentTileType();
			if(current.equals(Tile.WATER)){
				screen.render(x, y, getSheet(), 6, 4, Screen.MirrorDirection.NONE);
			} else {
				renderAnimatedStanding(screen);
			}
		}
		if (debug) {
			renderDebuggingElements(screen);
		}

		if (username != null) {
			screen.renderTextAtWorldCoordinates(username, Font.DEFAULT, centerX() - Font.DEFAULT.getWidthOf(username)/2, y - 10, 1);
		}
	}

	private void renderAnimatedStanding(Screen screen) {
		if (tickCount % 60 < 15) {
			screen.render(x, y, getSheet(), 0, 0, Screen.MirrorDirection.NONE);
		} else if (15 <= tickCount % 60 && tickCount % 60 < 30) {
			screen.render(x, y, getSheet(), 0, 1, Screen.MirrorDirection.NONE);
		} else if (30 <= tickCount % 60 && tickCount % 60 < 45) {
			screen.render(x, y, getSheet(), 0, 2, Screen.MirrorDirection.NONE);
		} else {
			screen.render(x, y, getSheet(), 0, 3, Screen.MirrorDirection.NONE);
		}
	}

	/***
	 * Check if player is going to collide with a solid tile if x changes by dx
	 * and y changes by dy
	 * 
	 * @param dx
	 * @param dy
	 * @return
	 */

	public String getUsername() {
		return this.username;
	}
	public void handleCollision(Entity e) {
		if(e instanceof Coin){
			money +=((Coin)e).getValue();
			e.markAsDead();
		}
	}

	private void renderDebuggingElements(Screen screen) {
		screen.highlightTileAtWorldCoordinates(leftX(), topY(),
				getLevel().getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(leftX(), bottomY(),
				getLevel().getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(rightX(), topY(),
				getLevel().getTileDisplaySize());
		screen.highlightTileAtWorldCoordinates(rightX(), bottomY(),
				getLevel().getTileDisplaySize());

		Font.DEFAULT.render("" + x + ", " + y, screen,
				x - ((username.length() - 1) / 2 * 8), y - 10, 1);
	}

}