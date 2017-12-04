package world;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import render.Camera;
import render.Shader;

public class World {
	private byte[] tiles;
	private int width;
	private int height;
	private Matrix4f world;

	public World() {
		this.width = 16;
		this.height = 16;
		this.tiles = new byte[this.width * this.height];

		this.world = new Matrix4f().setTranslation(new Vector3f(0));
		this.world.scale(16);
	}

	public void render(TileRenderer tileRenderer, Shader shader, Camera camera) {
		for (int i=0; i<this.height; i++) {
			for (int j=0; j<this.width; j++) {
				tileRenderer.renderTile(this.tiles[j+(i*this.width)], j, -i, shader, this.world, camera);
			}
		}
	}
}
