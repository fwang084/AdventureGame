package ca.vanzeben.game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import ca.vanzeben.game.Game;
import ca.vanzeben.game.entities.Coin;
import ca.vanzeben.game.entities.Entity;
import ca.vanzeben.game.entities.Fireball;
import ca.vanzeben.game.entities.Player;
import ca.vanzeben.game.entities.Popstar;
import ca.vanzeben.game.entities.Tower;
import ca.vanzeben.game.entities.Wumpus;
import ca.vanzeben.game.gfx.Screen;
import ca.vanzeben.game.gfx.SpriteSheet;
import ca.vanzeben.game.level.tiles.Tile;

public class Level {
	private static final int originalTileSize = 30;		// from sprite sheet
	private static final int scaleFactor = 2;         // how much to scale the tiles up for display
	
	public static final int tileSize = originalTileSize*scaleFactor; // each pixel in game ends up
																								 									 // being this large
	
	private int[][] levelTileIds;
	private int levelImageWidth;
	private int levelImageHeight;
	private String imagePath;
	private BufferedImage levelSourceimage;

	private Player player;
	private ArrayList<Entity> entityList;
	
	public Level(String imagePath) {
		entityList = new ArrayList<Entity>();
		if (imagePath != null) {
			this.imagePath = imagePath;
			this.loadLevelFromFile();
		} else {
			this.levelImageWidth = 64;
			this.levelImageHeight = 64;
			levelTileIds = new int[levelImageHeight][levelImageWidth];
			this.generateLevel();
		}

	}

	private void loadLevelFromFile() {
		try {
			this.levelSourceimage = ImageIO.read(Level.class.getResource(this.imagePath));
			this.levelImageWidth = this.levelSourceimage.getWidth();
			this.levelImageHeight = this.levelSourceimage.getHeight();
			levelTileIds = new int[levelImageHeight][levelImageWidth];
			this.loadTiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadTiles() {
		int[] tileColours = this.levelSourceimage.getRGB(0, 0, levelImageWidth,
				levelImageHeight, null, 0, levelImageWidth);
		for (int y = 0; y < levelImageHeight; y++) {
			for (int x = 0; x < levelImageWidth; x++) {
				tileCheck: for (Tile t : Tile.tiles) {
					if (t != null
							&& t.getLevelColour() == tileColours[x + y * levelImageWidth]) {
						this.levelTileIds[y][x] = t.getId();
						break tileCheck;
					}
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void saveLevelToFile() {
		try {
			ImageIO.write(levelSourceimage, "png",
					new File(Level.class.getResource(this.imagePath).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Creates new Coin instance at world coordinates, x, y
	 * @param x world coordinate x
	 * @param y world coordinate y
	 */
	public void addCoin(int x, int y, int value) {
		Coin coin = new Coin(x, y, this, value);
		entityList.add(coin);
	}
	public void addWumpus(int x, int y){
		Wumpus w = new Wumpus(x, y, this, 0, SpriteSheet.tileSheet, 2, player);
		entityList.add(w);
	}
	public void addPopstar(int x, int y) {
		Popstar p = new Popstar(x, y, this, 0, SpriteSheet.wizardSheet, 1);
		entityList.add(p);
	}
	public void addTower(int x, int y) {
		Tower t = new Tower(x, y, this, 0, SpriteSheet.tileSheet, player);
		entityList.add(t);
	}

	public void setTileAt(int x, int y, Tile newTile) {
		this.levelTileIds[y][x] = newTile.getId();
		levelSourceimage.setRGB(x, y, newTile.getLevelColour());
	}

	public void generateLevel() {
		for (int y = 0; y < levelImageHeight; y++) {
			for (int x = 0; x < levelImageWidth; x++) {
				if (x * y % 10 < 7) {
					levelTileIds[y][x] = Tile.GRASS.getId();
				} else {
					levelTileIds[y][x] = Tile.STONE.getId();
				}
			}
		}
	}

	/***
	 * Run tick() on everything in this level to prepare for next game frame.
	 */
	public void tick() {
		// Run tick() for all entities
		player.tick();
		for(int i = 0; i < this.entityList.size(); i++) {
			Entity e = entityList.get(i);
			if(e.isDead()){
				entityList.remove(e);
				i--;
			}else{
				e.tick();
			}
			if(e instanceof Tower) {
				for(int j = 0; j < ((Tower) e).getFireballs().size(); j++){
					Fireball f = ((Tower) e).getFireballs().get(j);
					f.tick();
				}
			}
		}

		// Run tick() for all tiles
		for (Tile t : Tile.tiles) {
			if (t == null) {
				break;
			}
			t.tick();
		}
		runPlayerCollisionDetect();
	}
	private void runPlayerCollisionDetect() {
		for(Entity e: this.entityList) {
			if(player.isHitting(e)){
				player.handleCollision(e);
			}
		}
	}
	public void renderTiles(Screen screen) {
		for (int tileY = screen.getTopY()
				/ tileSize; tileY < screen.getBottomY() / tileSize + 1; tileY++) {
			for (int tileX = (screen.getLeftX() / tileSize); tileX < screen.getRightX() / tileSize + 1; tileX++) {
				getTileAtSourceImageCoordinates(tileX, tileY).render(screen, this, tileX * tileSize,
						tileY * tileSize, tileSize, tileSize);
			}
		}
	}

	public void renderEntities(Screen screen) {
		for(Entity e: entityList) {
			if(e instanceof Coin){
				e.render(screen, 16, 12);
			}
			if(e instanceof Wumpus){
				e.render(screen,  11, 0);
			}
			if(e instanceof Popstar) {
				e.render(screen, 5, 4);
			}
			if(e instanceof Tower) {
				e.render(screen, 12, 10);
				for(int j = 0; j < ((Tower) e).getFireballs().size(); j++){
					Fireball f = ((Tower) e).getFireballs().get(j);
					f.render(screen, 16, 14);
				}
			}
		}
		player.render(screen);
	}

	public Tile getTileTypeAtWorldCoordinates(int x, int y) {
		if (0 > x || x >= this.getLevelWidth() || 0 > y || y >= this.getLevelHeight())
			return Tile.VOID;
		
		int sourcex = x / this.tileSize;
		int sourcey = y / this.tileSize;
		
		Game.getScreen().highlightTileAtWorldCoordinates(x, y, tileSize);
		
		int tileId = levelTileIds[sourcey][sourcex];
		return Tile.tiles.get(tileId);
	}
	
	public Tile getTileAtSourceImageCoordinates(int x, int y) {
		if (0 > x || x >= levelImageWidth || 0 > y || y >= levelImageHeight)
			return Tile.VOID;
		
		int tileId = levelTileIds[y][x];
		return Tile.tiles.get(tileId);
	}

	public void addPlayer(Player player) {
		this.player = player;
	}

	/***
	 * Return size of level image. NOTE: this is NOT the width of the level
	 * itself. Only the level's source image.
	 * 
	 * @return
	 */
	public int getLevelImageWidth() {
		return levelImageWidth;
	}

	/***
	 * Return size of level image. NOTE: this is NOT the height of the level
	 * itself. Only the level's source image.
	 * 
	 * @return
	 */
	public int getLevelImageHeight() {
		return levelImageHeight;
	}

	/***
	 * Return pixel width of the level in world coordinates. Note: this is
	 * different from the size of the level *image* which is scaled up by a factor
	 * of levelScaleFactor to create the level
	 * 
	 * @return
	 */
	public int getLevelWidth() {
		return getLevelImageWidth() * this.tileSize;
	}

	/***
	 * Return pixel height of the level in world coordinates. Note: this is
	 * different from the size of the level *image* which is scaled up by a factor
	 * of levelScaleFactor to create the level
	 * 
	 * @return
	 */
	public int getLevelHeight() {
		return getLevelImageHeight() * this.tileSize;
	}

	public int getTileDisplaySize() {
		return this.tileSize;
	}
}
