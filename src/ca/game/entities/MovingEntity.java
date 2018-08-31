package ca.vanzeben.game.entities;

import javax.tools.DocumentationTool.Location;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public abstract class MovingEntity extends Entity{
	protected double xSpeed;
	protected double ySpeed;
	protected int speed;
	public MovingEntity(int x, int y, Level level, int tickCount, SpriteSheet sheet, int speed) {
		super(x, y, level, tickCount, sheet);
		this.speed = speed;
	}
	public abstract void move();
	public void setRandomDirection() {
		double randomAngle = Math.random()*2*Math.PI;
		double xComponent = Math.cos(randomAngle);
		double yComponent = Math.sin(randomAngle);
		
		this.xSpeed = (speed*xComponent);
		this.ySpeed = (speed*yComponent);
	}
	//Choose random direction once per second
	public void moveRandom() {
		if((tickCount/60) != (tickCount+1)/60) {
			setRandomDirection();
		}
		x += xSpeed;
		y += ySpeed;
	}
	public void moveTowardEntity(Entity entity) {
		moveTowardWorldCoordinates(entity.centerX(), entity.centerY());
	}
	private double getAngleToward(int x, int y) {
		double dx = x - this.x;
		double dy = y - this.y;
		return Math.atan2(dy,  dx);
	}
	private void moveTowardWorldCoordinates(int x, int y) {
		double angle = Math.atan2(y-this.y, x-this.x);
		xSpeed = speed*Math.cos(angle);
		ySpeed = speed*Math.sin(angle);
		this.x+=xSpeed;
		this.y+=ySpeed;
		
	}
	public void moveInDirection(double angle){
		xSpeed = speed*Math.cos(angle);
		ySpeed = speed*Math.sin(angle);
		this.x+=xSpeed;
		this.y+=ySpeed;
	}
	public void tick() {
		super.tick();
		move();
	}
	public void render(Screen screen, int row, int col) {
		super.render(screen, row, col);
	}
	
}
