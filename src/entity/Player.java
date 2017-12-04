package entity;

import io.Window;
import org.joml.Vector3f;
import render.Camera;
import render.Model;
import render.Shader;
import render.Texture;
import world.World;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
	private Model model;
	private Texture texture;
	private Transform transform;
	private int moveMultiplier = 15;

	public Player() {
		float[] vertices = new float[] {-1f, 1f, 0, 1f, 1f, 0, 1f, -1f, 0, -1f, -1f, 0};
		float[] texture = new float[] {0, 0, 1, 0, 1, 1, 0, 1,};
		int[] indices = new int[] {0, 1, 2, 2, 3, 0};

		this.model = new Model(vertices, texture, indices);
		this.texture = new Texture("test.png");

		this.transform = new Transform();
		this.transform.scale = new Vector3f(16,16,1);
	}

	public void update(float delta, Window window, Camera camera, World world) {
		if (window.getInput().isKeyDown(GLFW_KEY_A)) {
			this.transform.pos.add(new Vector3f(-moveMultiplier *delta, 0, 0));
		} else if (window.getInput().isKeyDown(GLFW_KEY_D)) {
			this.transform.pos.add(new Vector3f(moveMultiplier *delta, 0, 0));
		}

		if (window.getInput().isKeyDown(GLFW_KEY_W)) {
			this.transform.pos.add(new Vector3f(0, moveMultiplier *delta, 0));
		} else if (window.getInput().isKeyDown(GLFW_KEY_S)) {
			this.transform.pos.add(new Vector3f(0, -moveMultiplier *delta, 0));
		}

		camera.setPosition(this.transform.pos.mul(-world.getScale(), new Vector3f()));
	}

	public void render(Shader shader, Camera camera) {
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", this.transform.getProjection(camera.getProjection()));

		this.texture.bind(0);
		this.model.render();
	}
}
