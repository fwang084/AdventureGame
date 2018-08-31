package ca.vanzeben.game.entities;

import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.Level;

public abstract class Entity {
	public int x, y;
	private SpriteSheet sheet;
	protected int tickCount;
	private int width;
	private int height;
	protected Level level;
	private boolean isDead = false;
	public Entity(int x, int y, Level level, int tickCount, SpriteSheet sheet){
		this.x = x;
		this.y = y;
		this.tickCount = tickCount;
		this.level = level;
		this.sheet = sheet;
		
		this.width = sheet.getSpriteWidth();
		this.height = sheet.getSpriteHeight();
	}
	public void tick(){
		tickCount++;
	}
	public void render(Screen screen, int row, int col){
		screen.render(x, y, sheet, row, col, Screen.MirrorDirection.NONE);
	}
	public double angleTowardEntity(Entity other) {
		int dx = other.centerX() - this.centerX();
		int dy = other.centerY() - this.centerY();
		double angle = Math.atan2(dy, dx);
		return angle;
	}
	public void markAsDead() {
		isDead = true;
	}
	public boolean isDead() {
		return isDead;
	}
	public boolean isHitting(Entity other) {
		if(areIntervalsOverlapping(this.leftX(), this.rightX(), other.leftX(), other.rightX())&&
			areIntervalsOverlapping(this.topY(), this.bottomY(), other.topY(), other.bottomY())){
			return true;
		}
		return false;
	}
	private boolean areIntervalsOverlapping(int low1, int high1, int low2, int high2) {
		if(high1 < low2) {
			return false;
		}
		if(high2 < low1) {
			return false;
		}
		return true;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public SpriteSheet getSheet() {
		return sheet;
	}
	public void setSheet(SpriteSheet sheet) {
		this.sheet = sheet;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
	public int centerX() {
		return x + this.width/2;
	}
	public int centerY(){
		return y + this.height/2;
	}
	public int topY() {
		return y;
	}
	public int bottomY(){
		return y + height;
	}
	public int leftX() {
		return x;
	}
	public int rightX() {
		return x + width;
	}
	
	
}
