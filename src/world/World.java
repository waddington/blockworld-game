package world;

import collision.AABB;
import io.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class World {
	private final int view = 22;
	private int width;
	private int height;
	private int scale;
	private byte[] tiles;
	private Matrix4f world;
	private AABB[] boundingBoxes;

	public World() {
		this.width = 64;
		this.height = 64;
		this.scale = 16;

		this.tiles = new byte[this.width * this.height];

		this.world = new Matrix4f().setTranslation(new Vector3f(0));
		this.world.scale(this.scale);

		this.boundingBoxes = new AABB[this.width* this.height];
	}

	public World(String world) {
		this.width = 64;
		this.height = 64;
		this.scale = 16;

		this.world = new Matrix4f().setTranslation(new Vector3f(0));
		this.world.scale(this.scale);

		try {
			BufferedImage tileSheet = ImageIO.read(new File("./levels/" + world + "_tiles.png"));
			//BufferedImage entitySheet = ImageIO.read(new File("./levels/" + world + "_entities.png"));

			int tsWidth = tileSheet.getWidth();
			int tsHeight = tileSheet.getHeight();

			int[] colorTileSheet = tileSheet.getRGB(0,0, tsWidth, tsHeight, null, 0, tsWidth);

			this.tiles = new byte[tsWidth * tsHeight];
			this.boundingBoxes = new AABB[tsWidth * tsHeight];

			for (int y=0; y<tsHeight; y++) {
				for (int x=0; x<tsWidth; x++) {
					int red = (colorTileSheet[x + (y * tsWidth)] >> 16) & 0xFF;

					Tile t;

					try {
						t = Tile.tiles[red];
					} catch (ArrayIndexOutOfBoundsException e) {
						t = null;
					}

					if (t != null) {
						setTile(t, x, y);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Matrix4f getWorldMatrix() {
		return this.world;
	}

	public void render(TileRenderer tileRenderer, Shader shader, Camera camera, Window window) {
		int posX = ((int) camera.getPosition().x + (window.getWidth()/2)) / (this.scale * 2);
		int posY = ((int) camera.getPosition().y - (window.getWidth()/2)) / (this.scale * 2);

		for (int i=0; i<this.view; i++) {
			for (int j=0; j<this.view; j++) {
				Tile tile = getTile(i-posX, j+posY);

				if (tile != null) {
					tileRenderer.renderTile(tile, i-posX, -j-posY, shader, this.world, camera);
				}
			}
		}
	}

	public void setTile(Tile tile, int x, int y) {
		this.tiles[x + (y*this.width)] = tile.getId();

		if (tile.isSolid()) {
			this.boundingBoxes[x + (y * this.width)] = new AABB(new Vector2f(x*2, - y*2), new Vector2f(1,1));
		} else {
			this.boundingBoxes[x + (y * this.width)] = null;
		}
	}

	public Tile getTile(int x, int y) {
		try {
			return Tile.tiles[this.tiles[x + (y*this.width)]];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public AABB getTileBoundingBox(int x, int y) {
		try {
			return this.boundingBoxes[x + (y * this.width)];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public void correctCamera(Camera camera, Window window) {
		Vector3f position = camera.getPosition();

		int w = -this.width * scale * 2;
		int h = this.height * scale * 2;

		if (position.x > - (window.getWidth()/2) + scale)
			position.x = -(window.getWidth()/2) + scale;
		if (position.x < w + (window.getWidth()/2) + scale)
			position.x = w + (window.getWidth()/2) + scale;

		if (position.y < (window.getHeight()/2) - scale)
			position.y = (window.getHeight()/2) - scale;
		if (position.y > h - (window.getHeight()/2) - scale)
			position.y = h - (window.getHeight()/2) - scale;
	}

	public int getScale() {
		return this.scale;
	}
}
