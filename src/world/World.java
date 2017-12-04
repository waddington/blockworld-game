package world;

import io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;

public class World {
	private int width;
	private int height;
	private int scale;
	private byte[] tiles;
	private Matrix4f world;

	public World() {
		this.width = 64;
		this.height = 64;
		this.scale = 16;

		this.tiles = new byte[this.width * this.height];

		this.world = new Matrix4f().setTranslation(new Vector3f(0));
		this.world.scale(this.scale);
	}

	public void render(TileRenderer tileRenderer, Shader shader, Camera camera) {
		for (int i=0; i<this.height; i++) {
			for (int j=0; j<this.width; j++) {
				tileRenderer.renderTile(this.tiles[j+(i*this.width)], j, -i, shader, this.world, camera);
			}
		}
	}

	public void setTile(Tile tile, int x, int y) {
		this.tiles[x + (y*this.width)] = tile.getId();
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
}
