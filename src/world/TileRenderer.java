package world;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;

import java.util.HashMap;
import java.util.Map;

public class TileRenderer {
	private Map<String, Texture> tileTextures;
	private Model model;

	public TileRenderer() {
		this.tileTextures = new HashMap<>();

		float[] vertices = new float[] {-1f, 1f, 0, 1f, 1f, 0, 1f, -1f, 0, -1f, -1f, 0};
		float[] texture = new float[] {0, 0, 1, 0, 1, 1, 0, 1,};
		int[] indices = new int[] {0, 1, 2, 2, 3, 0};

		this.model = new Model(vertices, texture, indices);

		for (int i=0; i<Tile.tiles.length; i++) {
			if (Tile.tiles[i] != null) {
				if (!this.tileTextures.containsKey(Tile.tiles[i].getTexture())) {
					String tex = Tile.tiles[i].getTexture();
					this.tileTextures.put(tex, new Texture(tex + ".png"));
				}
			}

		}
	}

	public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera camera) {
		shader.bind();

		if (this.tileTextures.containsKey(tile.getTexture())) {
			this.tileTextures.get(tile.getTexture()).bind(0);
		}

		Matrix4f tilePosition = new Matrix4f().translate(new Vector3f(x*2, y*2, 0));

		Matrix4f target = new Matrix4f();
		camera.getProjection().mul(world, target);

		target.mul(tilePosition);

		shader.setUniform("sampler", 0);
		shader.setUniform("projection", target);

		this.model.render();
	}
}
