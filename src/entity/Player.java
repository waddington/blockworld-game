package entity;

import collision.AABB;
import collision.Collision;
import io.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import render.Animation;
import render.Camera;
import render.Model;
import render.Shader;
import world.World;

import static org.lwjgl.glfw.GLFW.*;

public class Player {
	private AABB boundingBox;
	private Model model;
	private Animation texture;
	private Transform transform;
	private int moveMultiplier = 15;

	public Player() {
		float[] vertices = new float[] {-1f, 1f, 0, 1f, 1f, 0, 1f, -1f, 0, -1f, -1f, 0};
		float[] texture = new float[] {0, 0, 1, 0, 1, 1, 0, 1,};
		int[] indices = new int[] {0, 1, 2, 2, 3, 0};

		this.model = new Model(vertices, texture, indices);
		this.texture = new Animation(5, 15, "an");

		this.transform = new Transform();
		this.transform.scale = new Vector3f(16,16,1);

		this.boundingBox = new AABB(new Vector2f(this.transform.pos.x, this.transform.pos.y), new Vector2f(1,1));
	}

	public void update(float delta, Window window, Camera camera, World world) {
		// Movement
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

		AABB[] boxes = new AABB[25];
		for (int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				boxes[i + (j*5)] = world.getTileBoundingBox(
						(int)(((this.transform.pos.x/2) + 0.5f) - (5/2)) + i,
						(int)(((-this.transform.pos.y/2) + 0.5f) - (5/2)) + j
				);
			}
		}

		// Collision checks

		this.boundingBox.getCenter().set(this.transform.pos.x, this.transform.pos.y);

		AABB box = null;
		for (int i=0; i<boxes.length; i++) {
			if (boxes[i] != null) {
				if (box == null) {
					box = boxes[i];
				}

				Vector2f length1 = box.getCenter().sub(this.transform.pos.x, this.transform.pos.y, new Vector2f());
				Vector2f length2 = boxes[i].getCenter().sub(this.transform.pos.x, this.transform.pos.y, new Vector2f());

				if (length1.lengthSquared() > length2.lengthSquared()) {
					box = boxes[i];
				}
			}
		}

		if (box != null) {
			Collision data = this.boundingBox.getCollision(box);
			if (data.isIntersecting) {
				this.boundingBox.correctPosition(box, data);
				this.transform.pos.set(this.boundingBox.getCenter(), 0);
			}

			for (int i=0; i<boxes.length; i++) {
				if (boxes[i] != null) {
					if (box == null) {
						box = boxes[i];
					}

					Vector2f length1 = box.getCenter().sub(this.transform.pos.x, this.transform.pos.y, new Vector2f());
					Vector2f length2 = boxes[i].getCenter().sub(this.transform.pos.x, this.transform.pos.y, new Vector2f());

					if (length1.lengthSquared() > length2.lengthSquared()) {
						box = boxes[i];
					}
				}
			}

			data = this.boundingBox.getCollision(box);
			if (data.isIntersecting) {
				this.boundingBox.correctPosition(box, data);
				this.transform.pos.set(this.boundingBox.getCenter(), 0);
			}

		}

//		camera.setPosition(this.transform.pos.mul(-world.getScale(), new Vector3f()));
		camera.getPosition().lerp(this.transform.pos.mul(-world.getScale(), new Vector3f()), 0.2f);
	}

	public void render(Shader shader, Camera camera) {
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", this.transform.getProjection(camera.getProjection()));

		this.texture.bind(0);
		this.model.render();
	}
}
